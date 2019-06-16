在k8s出现之后，docker-swarm使用的人越来越少，但在本地集成开发环境的搭建上，使用它还是比较轻量级的，它比docker-compose最大的好处就是容器之间的共享和服务的治理，你不需要links容器，也不知道关心失败之后的重启，这些都于swarm来实现。
#### 对于docker-compose和docker-swarm的分工
1. docker-compose用来进行镜像的编排，同时将多个相关镜像构建
2. docker-swarm用来启动和管理容器，它不能构建镜像

#### 建立compose文件，不支持build,links,depends_on
```
services:
  db:
    image: arungupta/couchbase:latest
    ports:
      - 8091:8091
      - 8092:8092
      - 8093:8093
      - 11210:11210
```
#### swarm初始化
```
docker swarm init
```
#### 建立服务
```
docker stack deploy --compose-file=docker-compose-swarm.yml lind
```
#### 查看所有服务列表
```
docker stack ls
```
#### 查看指定服务
```
docker stack services lind
```
#### 更新某个服务
```
docker service update lind_service1 //有时我们的lind_service1依赖于configserver，当后者没有重启动，前者会一直报错，然后使用默认的配置，由于默认配置为localhost，所以就出现了连接失败的情况
```
#### 删除服务
```
docker stack  rm lind
```
#### 删除指定服务列表
```
docker service rm  $(docker service ls | awk '$2 ~ /lind_service/ {print $1}')
```
#### 删除某个镜像下的容器
```
docker rm -f $(docker ps | awk '$2 ~ /lind/ {print $1}')
```
#### 痛点
configserver是比较低层的服务，所有项目的配置都来自于这个服务，我们拿eureka配置为例，如果项目从配置中心拿不到它的配置，会使用默认的localhost:8761做为连接地址，这在某些不是localhost:8761的环境下，这简直就成了灾难。
> 解决方法：把configserver和eureka两个服务越来后，再把具体项目的容器删除，这时docker service会重新启动具体项目的容器，这时，它们就可以正常工作了。

#### 配置中心使用本地仓库
配置中心的配置文件一般存储在git远程，而如果太依赖远程也不是好事 ，你可以把仓库下载到本地，然后挂载到容器目录即可，但在进行操作时也有一些需要注意的点：
1. 配置中心的目录必须是git仓库，默认是master分支
2. 使用docker-comopse部署时，使用volumes把宿主目录挂载到容器目录，例如固定为config_repo，它可以在yml文件里提前声明。
* compose文件
```
 configserver:
   image: swarm_configserver
   ports:
     - "6200:6200"
     - "6201:6201"
   environment:
     SPRING_PROFILES_ACTIVE: development
     PORT: 6200
     BG_PORT: 6201
     EUREKA_PORT: 6761
     MY_IP: 192.168.170.30
   volumes:
        - /Users/lind.zhang/github/config_repo:/config_repo
```
* bootstramp.yml文件
```
spring:
  profiles: development
  cloud:
    config:
      server:
        git:
          uri: /config_repo
```

#### 本机使用docker swarm部署环境
1. 一定要设置eureka.instance.hostname为宿主机IP，或者容器里feign无法访问，宿主机调试没问题
2. docker-compose里的服务名要与spring.application.name区分开，避免冲突，前者在容器里可以ping通，后者只在feign里有效
3. 配置健康检查地址，一般都在application里设置，其它项目配置继承即可
```
spring:
  config:
    name: ${spring.application.name}
  cloud.config:
    allowOverride: true
    overrideNone: false
    overrideSystemProperties: false
    name: ${spring.application.name}
  profiles.active: development

management:
  context-path: /manage #从新设置监控检查的地址，默认为/actuator
  security.enabled: false
  health:
    diskspace.enabled: false
  endpoint:
    health.enabled: true
  endpoints:
    web:
      path-mapping.health: status
      base-path: /manage #since spring boot 2.0.0 M6
      expose: info,status,health

eureka:
  instance:
    statusPageUrlPath: ${management.context-path}/info
    healthCheckUrlPath: ${management.context-path}/${management.endpoints.web.path-mapping.health}
    preferIpAddress: false
    hostname: 192.168.170.30
  client:
    fetchRegistry: true
    registryFetchIntervalSeconds: 5
    healthcheck:
      enabled: true
```
#### 建立client，成功访问
```
@FeignClient("service2")
public interface Service2Client {
  @GetMapping("/get")
  String get();
}
@Autowired
Service2Client service2Client;

@GetMapping("/")
public String index() {
    return "hello index service1:" + service2Client.get();
}
```