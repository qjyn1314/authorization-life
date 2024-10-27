# life-ui

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

### 使用 vue ui 启动可视化界面进行创建vue项目

```
1.在管理员命令行窗口中输入: vue ui
2.选择vue2, 指定项目名称: life-ui
3.安装 vuex, vue-router.
```

### 手动安装的组件:

```

发送ajax请求
npm install axios

发布事件或订阅事件
npm install pubsub-js

uuid的生成
npm install uuid

ElementUI导入
npm install element-ui

操作cookie
npm install js-cookie

安装element-ui按需引入-未安装
npm install babel-plugin-component

表单提交的data序列化
npm install qs

bootstrap样式引入
npm install bootstrap

```

### nginx的配置项-开发环境

```

    server {
        listen       80;
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
                proxy_pass http://127.0.0.1:8888;
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

### nginx配置项-测试和生产环境

```
    # 测试环境的nginx配置项
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

    # 生产环境的配置项
    server {
        listen       80;
        server_name  prod.authorization.life;
		#后端服务gateway
        location /prod-api/ {
		       proxy_pass http://127.0.0.1:9000/;
               proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
		       proxy_set_header X-Forwarded-Proto $scheme;
		       proxy_set_header Host $http_host;
		       proxy_redirect off;
        }
        # 前端VUE工程
        location / {
            root html/prod_dist;
            try_files $uri $uri/ /index.html;
        }
    }

```