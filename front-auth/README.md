# front-auth

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).

安装依赖命令:

npm install uuid

或者

npm install uuid --save 


element-plus/icons-vue  图标

vueuse/core  更快捷的使用vue3

axios  发起ajax请求

core-js  核心js

element-plus  UI框架

js-cookie  更快捷的操作cookie

//pubsub-js  监听事件

qs 一个用于处理URL查询字符串的库，主要用于解析和字符串化URL查询参数‌

vue vue3框架版本

vue-router  vue3路由

vuex vue的状态管理模式

mitt  mitt是一个轻量级的事件总线库,用于实现跨组件通信  

nprogress  进度条

notepadd: https://github.com/notepad-plus-plus/notepad-plus-plus

# nginx的配置信息

```
    # 进入到网站的首页
    server {
        # 监听浏览器的80端口
        listen       80;
        # 访问域名
        server_name  www.authorization.life;
        #后端服务gateway
        location /dev-api/ {
		       proxy_pass http://127.0.0.1:9000/;
               proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		       proxy_set_header X-Forwarded-Proto $scheme;
		       proxy_set_header Host $http_host;
		       proxy_redirect off;
        }
        # 前端VUE工程
        location / {
                proxy_pass http://127.0.0.1:18888;
                proxy_http_version 1.1;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection $connection_upgrade;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header X-Forwarded-Proto $scheme;
                proxy_set_header Host $http_host;
                proxy_redirect off;
        }
    }
    # 点击登录按钮时进行发起授权请求,并跳转到登录页->
    server {
        # 监听浏览器的80端口
        listen       80;
        # 访问域名
        server_name  passport-dev.authorization.life;
        #后端服务gateway
        location /dev-api/ {
		       proxy_pass http://127.0.0.1:9000/;
               proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		       proxy_set_header X-Forwarded-Proto $scheme;
		       proxy_set_header Host $http_host;
		       proxy_redirect off;
        }
        # 前端VUE工程
        location / {
                proxy_pass http://127.0.0.1:18888;
                proxy_http_version 1.1;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection $connection_upgrade;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header X-Forwarded-Proto $scheme;
                proxy_set_header Host $http_host;
                proxy_redirect off;
        }
    }
    
    
    server {
        listen       80;
        server_name  test.authorization.life;
        #后端服务gateway
        location /test-api/ {
		       proxy_pass http://127.0.0.1:9000/;
               proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		       proxy_set_header X-Forwarded-Proto $scheme;
		       proxy_set_header Host $http_host;
		       proxy_redirect off;
        }
        # 前端VUE工程
        location / {
            root html/test_dist;
            try_files $uri $uri/ /index.html;
        }
    }
    
    server {
        listen       80;
        server_name  password-test.authorization.life;
        #后端服务gateway
        location /test-api/ {
		       proxy_pass http://127.0.0.1:9000/;
               proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		       proxy_set_header X-Forwarded-Proto $scheme;
		       proxy_set_header Host $http_host;
		       proxy_redirect off;
        }
        # 前端VUE工程
        location / {
            root html/test_dist;
            try_files $uri $uri/ /index.html;
        }
    }

```
# 参考:

https://github.com/smltq/spring-boot-demo/blob/master/security-oauth2-auth-code/README.md


https://www.cnblogs.com/linianhui/p/oauth2-authorization.html

```

    # 进入到网站的首页
    server {
        # 监听浏览器的80端口
        listen       80;
        # 访问域名
        server_name  dev.authorization.life;
        #后端服务gateway
        location /dev-api/ {
		       proxy_pass http://127.0.0.1:9000/;
               proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		       proxy_set_header X-Forwarded-Proto $scheme;
		       proxy_set_header Host $http_host;
		       proxy_redirect off;
        }
        # 前端VUE工程
        location / {
                proxy_pass http://127.0.0.1:18888;
                proxy_http_version 1.1;
                proxy_set_header Upgrade $http_upgrade;
                proxy_set_header Connection $connection_upgrade;
                proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
                proxy_set_header X-Forwarded-Proto $scheme;
                proxy_set_header Host $http_host;
                proxy_redirect off;
        }
    }
```
