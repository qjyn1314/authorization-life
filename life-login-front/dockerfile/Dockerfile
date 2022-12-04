FROM hub.authorization.life/library/nginx:2.3.0
#设置时区和语言环境变量
ENV TZ Asia/Shanghai
#编译方式
LABEL compile=node nginxDir=login port=80  configType=none
# 配置初始化脚本
ADD authserver-service.tar.gz /app
ADD nginx-web.conf /usr/local/nginx/conf/conf.d

# 配置Volume
VOLUME /data/applogs
# 工作目录
WORKDIR /app
# 配置启动命令
CMD ["nginx", "-g", "daemon off;"]