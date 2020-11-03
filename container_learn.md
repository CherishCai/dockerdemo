

# 挂载 & overlay UNION FS
mkdir -p lowerdir upperdir workdir mergeddir

echo "lowerdir" > lowerdir/lowerdir.txt
echo "upperdir" > upperdir/upperdir.txt

echo "lowerdir" > lowerdir/file1.txt
echo "upperdir" > upperdir/file1.txt

echo "lowerdir" > lowerdir/update.txt
echo "lowerdir" > lowerdir/del.txt

tree 

mount -t overlay -o lowerdir=./lowerdir,upperdir=./upperdir,workdir=./workdir overlay ./mergeddir

tree 

cat mergeddir/*

echo "mergeddir" > mergeddir/update.txt
echo "mergeddir" > mergeddir/add.txt
rm -f mergeddir/del.txt

tree

cat mergeddir/*
cat lowerdir/update.txt && cat upperdir/update.txt && cat mergeddir/update.txt

# umount 卸载
mount | grep overlay
umount $(pwd)/mergeddir

tree
rm -rf lowerdir upperdir workdir mergeddir


# 借用已有的 MergedDir 文件系统，演示容器视图 Namespaces

## docker inspect [imageId]
docker inspect registry.cn-shenzhen.aliyuncs.com/cherish520/cherish:0.0.4

{
    "LowerDir": "/var/lib/docker/overlay2/f685830c23105a024dc729dbfacdcd84281cb1babc216a9e0a9ccd3525c46da9/diff:/var/lib/docker/overlay2/bd11e9a678b9d2ebc54857defbc0e135755cba58e471be1f718d941202dfd05f/diff:/var/lib/docker/overlay2/2f3d6a609392b43371be098f5a24242c35ae0900f9b5cd503be328be42e30fe1/diff:/var/lib/docker/overlay2/6695a9d7f67379631f981771d9623546db6db4d3f7d608ac0f9a2845c63de925/diff:/var/lib/docker/overlay2/a8eb8e6448110481d8e850482dc0a1b44b8983133726dc2a3c13183978c2f81a/diff:/var/lib/docker/overlay2/46a8441704feecb423ea093e593e6af47479cc233d5e5e6411cb8a7883bde9a9/diff:/var/lib/docker/overlay2/0dec6fe26b53fb17bc91ca7d22153d42feb5b1d5fe882e7b01186a7be3b41325/diff",
    "MergedDir": "/var/lib/docker/overlay2/006b7f45123054de0c036dedb40c097b24c3ef6f1dfd2680cd13e1077504ef92/merged",
    "UpperDir": "/var/lib/docker/overlay2/006b7f45123054de0c036dedb40c097b24c3ef6f1dfd2680cd13e1077504ef92/diff",
    "WorkDir": "/var/lib/docker/overlay2/006b7f45123054de0c036dedb40c097b24c3ef6f1dfd2680cd13e1077504ef92/work"
}

-- 取 MergedDir 演示 namespaces

## docker inspect [containerId] , 以 k8s 启动的容器为准
{
    "LowerDir": "/var/lib/docker/overlay2/77b9bd2f5643a9145170769b9d12820af43c9ef7de4737846fc6f2789caecb27-init/diff:/var/lib/docker/overlay2/006b7f45123054de0c036dedb40c097b24c3ef6f1dfd2680cd13e1077504ef92/diff:/var/lib/docker/overlay2/f685830c23105a024dc729dbfacdcd84281cb1babc216a9e0a9ccd3525c46da9/diff:/var/lib/docker/overlay2/bd11e9a678b9d2ebc54857defbc0e135755cba58e471be1f718d941202dfd05f/diff:/var/lib/docker/overlay2/2f3d6a609392b43371be098f5a24242c35ae0900f9b5cd503be328be42e30fe1/diff:/var/lib/docker/overlay2/6695a9d7f67379631f981771d9623546db6db4d3f7d608ac0f9a2845c63de925/diff:/var/lib/docker/overlay2/a8eb8e6448110481d8e850482dc0a1b44b8983133726dc2a3c13183978c2f81a/diff:/var/lib/docker/overlay2/46a8441704feecb423ea093e593e6af47479cc233d5e5e6411cb8a7883bde9a9/diff:/var/lib/docker/overlay2/0dec6fe26b53fb17bc91ca7d22153d42feb5b1d5fe882e7b01186a7be3b41325/diff",
    "MergedDir": "/var/lib/docker/overlay2/77b9bd2f5643a9145170769b9d12820af43c9ef7de4737846fc6f2789caecb27/merged",
    "UpperDir": "/var/lib/docker/overlay2/77b9bd2f5643a9145170769b9d12820af43c9ef7de4737846fc6f2789caecb27/diff",
    "WorkDir": "/var/lib/docker/overlay2/77b9bd2f5643a9145170769b9d12820af43c9ef7de4737846fc6f2789caecb27/work"
}

## cd 到 merged, 尝试 chroot . ; sudo unshare --uts --pid --mount --fork --mount-proc /bin/bash && hostname container0xx && exec bash
cd /var/lib/docker/overlay2/77b9bd2f5643a9145170769b9d12820af43c9ef7de4737846fc6f2789caecb27/merged

sudo unshare --uts --pid --mount --fork --mount-proc /bin/bash 
hostname container0xx
exec bash

### pstree 确认已经进入新 namespaces 空间
pstree -pl

nohup usr/java/jdk/bin/java -Dserver.port=8088 -jar *.jar &

curl 127.0.0.1:8088
pstree -pl

readlink /proc/[pid]/ns/pid

chroot .
/bin/ls -lh

### 新开一个 shell 登录宿主机, 从外部看容器进程 
pstree -pl | grep java


# 启动 pod
kubectl apply -f cherish-demo-pod.yaml

# 可看到有 nginx, java , pause 镜像,容器
docker ps 

# 请求数据
curl 127.0.0.1:8001 -i

curl 127.0.0.1:8002 -i
curl 127.0.0.1:8002/java -i

# exec 进入容器
kubectl exec -it cherish-demo-pod -c nginx-demo -- /bin/sh

kubectl exec -it cherish-demo-pod -c demo1 -- /bin/sh

```$shell
netstat -an

curl 127.0.0.1:80
curl 127.0.0.1:80/java
```

# limit , CGroups
cd /sys/fs/cgroup

### docker 关注 /sys/fs/cgroup/cpu/docker/[containerId]/*

### k8s 关注 /sys/fs/cgroup/cpu/kubepods/besteffort/pod[podId]/[containerId]
cat cpu.cfs_period_us
cat cpu.cfs_quota_us

echo 20000 > cpu.cfs_quota_us

