
## 【按官方样例】kubeadm install k8s 单机集群

### @see kubeadm install
https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/install-kubeadm/

- 官方给出的 /etc/yum.repos.d/kubernetes.repo 由于 https://packages.cloud.google.com 连不上，使用 http://mirrors.aliyun.com/kubernetes 置换之

```shell
[kubernetes]
name=Kubernetes
baseurl=http://mirrors.aliyun.com/kubernetes/yum/repos/kubernetes-el7-$basearch
enabled=1
gpgcheck=0
repo_gpgcheck=0
gpgkey=http://mirrors.aliyun.com/kubernetes/yum/doc/yum-key.gpg http://mirrors.aliyun.com/kubernetes/yum/doc/rpm-package-key.gpg
exclude=kubelet kubeadm kubectl
```

#### install kubelet kubeadm kubectl
```shell
sudo yum install -y kubelet kubeadm kubectl --disableexcludes=kubernetes
```

### @see kubeadm init a k8s cluster
https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/create-cluster-kubeadm/

#### 执行 kubeadm init
```
# // 10.244.0.0/16 是网络插件 flannel 的默认 cidr 网段
kubeadm init --pod-network-cidr 10.244.0.0/16
```

#### 肯定遇到 k8s.gcr.io 网络不通问题
1. 手动下载以下镜像，具体版本号 `kubeadm config images list` 确认下，持续有更新 kubeadm 新版必定有新依赖，自己修改成对应版本号哈。
```shell
k8s.gcr.io/kube-apiserver:v1.21.2
k8s.gcr.io/kube-controller-manager:v1.21.2
k8s.gcr.io/kube-scheduler:v1.21.2
k8s.gcr.io/kube-proxy:v1.21.2
k8s.gcr.io/pause:3.4.1
k8s.gcr.io/etcd:3.4.13-0
k8s.gcr.io/coredns/coredns:v1.8.0
```

2. 因为 k8s.gcr.io 网络不通，所以从 registry.aliyuncs.com/google_containers 拉镜像后打 tag
```shell
docker pull registry.aliyuncs.com/google_containers/kube-apiserver:v1.21.2
docker pull registry.aliyuncs.com/google_containers/kube-controller-manager:v1.21.2
docker pull registry.aliyuncs.com/google_containers/kube-scheduler:v1.21.2
docker pull registry.aliyuncs.com/google_containers/kube-proxy:v1.21.2
docker pull registry.aliyuncs.com/google_containers/pause:3.4.1
docker pull registry.aliyuncs.com/google_containers/etcd:3.4.13-0
docker pull registry.aliyuncs.com/google_containers/coredns/coredns:v1.8.0
```

3. 打 tag
```shell
docker tag registry.aliyuncs.com/google_containers/kube-apiserver:v1.21.2 k8s.gcr.io/kube-apiserver:v1.21.2
docker tag registry.aliyuncs.com/google_containers/kube-controller-manager:v1.21.2 k8s.gcr.io/kube-controller-manager:v1.21.2
docker tag registry.aliyuncs.com/google_containers/kube-scheduler:v1.21.2 k8s.gcr.io/kube-scheduler:v1.21.2
docker tag registry.aliyuncs.com/google_containers/kube-proxy:v1.21.2 k8s.gcr.io/kube-proxy:v1.21.2
docker tag registry.aliyuncs.com/google_containers/pause:3.4.1 k8s.gcr.io/pause:3.4.1
docker tag registry.aliyuncs.com/google_containers/etcd:3.4.13-0 k8s.gcr.io/etcd:3.4.13-0
docker tag registry.aliyuncs.com/google_containers/coredns/coredns:v1.8.0 k8s.gcr.io/coredns/coredns:v1.8.0
```

4. (可选) 若 coredns/coredns:v1.8.0 不存在，直接去 dokcer hub 搜索便可，注意发现版本号没有 'v' 开头
```shell
docker pull coredns/coredns:1.8.0
docker tag coredns/coredns:1.8.0 k8s.gcr.io/coredns/coredns:v1.8.0
```

### 按照官方提示再继续
https://kubernetes.io/docs/setup/production-environment/tools/kubeadm/create-cluster-kubeadm/#more-information

- 然后安装网络插件 Flannel (小白就用这个吧)，
  [Flannel 官方提示执行以下命令](https://github.com/flannel-io/flannel#deploying-flannel-manually)

```shell
kubectl apply -f https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
```

- 上面或许直接下不来文件，那就先 wget 再 apply 吧
```shell
wget https://raw.githubusercontent.com/coreos/flannel/master/Documentation/kube-flannel.yml
kubectl apply -f kube-flannel.yml
```

- for example for a single-machine Kubernetes cluster for development, run:
- 单机集群版允许 k8s master 节点也作为 worker，可调度 pod
```shell
kubectl taint nodes --all node-role.kubernetes.io/master-
```

- 大功告成，看看吧
```shell
kubectl get nodes

kubectl get pods --all-namespaces
```
