# authserver-lifetime ：权限认证的一生。

## 前后端分离：

### 自定义maven工程 与 spring-authorization-server 深度整合示例。

## 授权中心：

### authserver-life

#### spring-security深度整合的模块，颁发token。


## 公共模块：

### common-life

#### 每个业务工程都将引用此工程，每次请求都将获取gateway中解析(前端请求头的token)过后的的jwt-token。

## 网关：

### gateway-life

#### 将鉴定并拦截token。

## 使用授权的服务：

### use-life

#### 具体业务服务。

## 前端工程：

### authserver-lifetime-front

#### 具体的使用authorization-server功能的页面。

### 使用的框架版本号及前端工程。

#### springboot 




