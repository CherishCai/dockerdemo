
docker build . -t=cherish520/cherish:0.0.4

docker tag cherish520/cherish:0.0.4 registry.cn-shenzhen.aliyuncs.com/cherish520/cherish:0.0.4

docker login --username=785427346@qq.com registry.cn-shenzhen.aliyuncs.com

docker push registry.cn-shenzhen.aliyuncs.com/cherish520/cherish:0.0.4


### 注意 default.conf 文件，要放到 /data/share/ 下