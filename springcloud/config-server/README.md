### 简介
RSA非对称加密有着非常强大的安全性，HTTPS的SSL加密就是使用这种方法进行HTTPS请求加密传输的。因为RSA算法会涉及Private Key和Public Key分别用来加密和解密，所以称为非对称加密。Private Key和Public Key有互操作性，即用private key加密的可以用public key解密，用public key加密的可以用private key解密。传统的单向认证则只用public key进行加密，有private key的一方才可进行解密。例如，一个web服务器会有一对private key和public key。浏览器客户端保存着服务器的public key。当客户端需要向服务器发送数据时，就用服务器的public key进行加密，然后服务器收到数据时，再用private key进行解密。客户端验证服务器是否为真实的服务器时，会根据服务器提供的public key和自己本地保存的public key作比较，一致的话才能验证服务器的真实性。

> 在我们的config server中，一些对加密要求比较高的可以采用RSA算法进行数据的加密和解密

### 生成测试Keystore
我们需要使用jdk自带的keytool工具生成一个keystore，里边保存了private key的信息，使用如下命令行：
```
keytool -genkeypair -alias config-server-key -keyalg RSA -dname "CN=Config Server,OU=Xuqian,O=My Own Company,L=Beijing,S=Beijing,C=CN" -keypass changeit -keystore server.jks -storepass changeit
```
- genkeypair 参数即产生一对public key和private key。
- alias 指定key的别名，用于区分同一keystore中不同的key。
- keyalg 指定生成key的算法，这里使用默认的RSA
- dname 指定common name，即CN，用以验证key的身份。其中各项皆为自定义参数，OU为单位名称，O为组织名称，L为城市，S为省份/州，C为国家
- keypass 为key的密码
- keystore 为keystore的文件名
- storepass 访问keystore的密码

> 上述工具将产生的 privte key 保存在了名为server.jks的 key store 中。到目前为止，我们只产生了 private key，Spring Cloud Config Server 会根据我们提供的 key 的信息，每次会用程序生成一个 public key，参考如下源代码org.springframework.security.rsa.crypto.KeyStoreKeyFactory：
 这里使用了 Java Security API 来对key进行操作。参见注释。然后上边的信息通过 configserver 中的 bootstrap.xml 配置文件提供：

```
 encrypt:
  #key: Thisismysecretkey
  key-store:
    location: file://${user.home}/development/keys/server.jks
    password: changeit
    alias: config-server-key
    secret: changeit
```

因为我们不能同时使用对称加密和非对称加密，所以我们把 `encrypt.key` 配置注释掉，然后指定非对称加密的参数：
- location： Keystore 的文件路径
- password： keystore 的密码
- alias： key 的别名
- secret： key的密码

### 测试
我们继续使用 encrypt API加密一项测试数据
```
curl http://localhost:8888/encrypt -d lind123
```
返回加密后的字符：
```
AQAPWOUOh4WVexGgVv+bgtKc5E0d5Aba8VUKnzEXh27HyKSAbW+wyzDwZTbk5QYfXpoCAs413rdeNIdR2ez44nkjT5V+438/VQExySzjZPhP0xYXi9YIaJqA3+Ji+IWK8hrGtJ4dzxIkmItiimCOirLdZzZGDm/yklMUVh7lARSNuMxXGKlpdBPKYWdqHm57ob6Sb0ivm4H4mL1n4d3QUCuE7hh2F4Aw4oln7XueyMkRPTtPy8OpnBEEZhRfmaL/auVZquLU5jjMNJk9JiWOy+DSTscViY/MZ+dypv6F4AfDdVvog89sNmPzcUT+zmB8jXHdjLoKy+63RG326WffY9OPuImW6/kCWZHV6Vws55hHqRy713W6yDBlrQ/gYC3Wils=
```
然后测试解密
```
curl http://localhost:8888/decrypt -d AQAPWOUOh4+bgtKc5E0d5Aba8VUKnzEXh27HyKSAbW+wyzDwZTbk5QYfXpoCAs413rdeNIdR2ez44nkjT5V+438/VQExySzjZPhP0xYXi9YIaJqA3+Ji+IWK8hrGtJ4dzxIkmItiimCOirLdZzZGDm/yklMUVh7lARSNuMxXGKlpdBPKYWdqHm57ob6Sb0ivm4H4mL1n4d3QUCuE7hh2F4Aw4oln7XueyMkRPTtPy8OpnBEEZhRfmaL/auVZquLU5jjMNJk9JiWOy+DSTscViY/MZ+dypv6F4AfDdVvog89sNmPzcUT+zmB8jXHdjLoKy+63RG326WffY9OPuImW6/kCWZHV6Vws55hHqRy713W6yDBlrQ/gYC3Wils=
```
会返回
```
lind123
```

### 应用到项目
添加依赖
```
implementation('org.springframework.security:spring-security-rsa')

```
bootstrap.yml内容
```
user:
  password: '{cipher}AQAPWOUOh4WVexGgVv+bgtKc5E0d5Aba8VUKnzEXh27HyKSAbW+wyzDwZTbk5QYfXpoCAs413rdeNIdR2ez44nkjT5V+438/VQExySzjZPhP0xYXi9YIaJqA3+Ji+IWK8hrGtJ4dzxIkmItiimCOirLdZzZGDm/yklMUVh7lARSNuMxXGKlpdBPKYWdqHm57ob6Sb0ivm4H4mL1n4d3QUCuE7hh2F4Aw4oln7XueyMkRPTtPy8OpnBEEZhRfmaL/auVZquLU5jjMNJk9JiWOy+DSTscViY/MZ+dypv6F4AfDdVvog89sNmPzcUT+zmB8jXHdjLoKy+63RG326WffY9OPuImW6/kCWZHV6Vws55hHqRy713W6yDBlrQ/gYC3Wils='
```
访问：http://localhost:8888/service1/svt

返回内容已经解密了
```
{

 "user.password": "23456789"

}

```