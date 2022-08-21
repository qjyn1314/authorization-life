# authorization-life ：授权的生活。

## 前后端分离：

### 自定义maven工程 与 spring-authorization-server 深度整合示例。

## 授权中心：

### life-authserver

#### spring-security深度整合的模块，颁发token。


## 公共模块：

### life-parent life-common  life-core

#### 每个业务工程都将引用此工程，每次请求都将获取gateway中解析(前端请求头的token)过后的的jwt-token。

## 自定义start模块：

### life-start-parent

#### 每个业务工程需要使用时，则需要引入相关依赖

## 网关：

### life-gateway 

#### 将鉴定并拦截token。

## 使用授权的服务：

### life-system

#### 具体业务服务。

## 前端工程：

### life-authserver-front

#### 具体的使用authorization-server功能的页面。

### 使用的框架版本号及前端工程。

#### springboot 

## oauth2.0的授权模式

### OAuth 2.0 授权四种方式 ：https://www.ruanyifeng.com/blog/2019/04/oauth-grant-types.html 
### OAuth2.0  授权模式总结 ：https://learnku.com/articles/20082 


