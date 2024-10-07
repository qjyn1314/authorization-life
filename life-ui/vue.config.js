const {defineConfig} = require('@vue/cli-service')

// const IS_DEV = process.env.NODE_ENV === 'development';

module.exports = defineConfig({
    // vue-cli-service build --mode 区分多个环境 参考:  https://www.cnblogs.com/grow-up-up/p/17210430.html
    publicPath: process.env.BASE_URL,
    transpileDependencies: true,
    // 默认代码语法检查为true
    lintOnSave: false,
    //开启代理服务器
    devServer: {
        port: process.env.VUE_APP_PORT,
        host: process.env.VUE_APP_HOST,
        allowedHosts: "all",
        open: true,
        proxy: {
            [process.env.VUE_APP_BASE_API]: {
                target: process.env.VUE_APP_PROXY_TARGET,
                changeOrigin: true,
                ws: true,
                pathRewrite: {
                    ['^' + process.env.VUE_APP_BASE_API]: ''
                }
            }
        },
    }
})
