
docker build . -t=cherish520/cherish:0.0.4

docker tag cherish520/cherish:0.0.4 registry.cn-shenzhen.aliyuncs.com/cherish520/cherish:0.0.4

docker login --username=785427346@qq.com registry.cn-shenzhen.aliyuncs.com

docker push registry.cn-shenzhen.aliyuncs.com/cherish520/cherish:0.0.4


### 注意 default.conf 文件，要放到 /data/share/ 下

### 参考资料
1. Link 极客时间 张磊 https://time.geekbang.org/column/article/14653
2. 容器十年 ，一部软件交付编年史 https://www.kubernetes.org.cn/5541.html
3. CNCF x Alibaba 云原生技术公开课  https://edu.aliyun.com/roadmap/cloudnative
4. 自己动手写Docker-陈显鹭.pdf
5. Kubernetes in Action中文版.pdf
