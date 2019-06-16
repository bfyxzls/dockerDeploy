# 基于docker的开发环境部署
## 主要包括以下程序的自动化部署
1. mysql
2. mongodb
3. redis
4. rabbitmq
## 使用以下命令部署
1. 启动程序
```
cd dockerDeploy
docker-compose up -d
```
2. 删除部署
```
cd dockerDeploy
docker-compose down
```
## eurekaServer配置
```
server.port: 6761
management.port: 6762
application.name: eurekaServer
eureka:
  instance:
    hostname: localhost
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.hostname}:${server.port}/eureka/
---
eureka:
  profile: svt
  instance:
    hostname: eureka-server
  client:
    registerWithEureka: false
    fetchRegistry: false
    serviceUrl:
      defaultZone: http://${eureka.hostname}:${server.port}/eureka/
```
## eurekaClient客户端配置（具体的服务）
```
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:6761/eureka/
```