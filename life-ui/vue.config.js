const {defineConfig} = require('@vue/cli-service')

// const IS_DEV = process.env.NODE_ENV === 'development';
// 配置文件参考: https://blog.csdn.net/L_R_H_/article/details/140602086
// 配置文件参考: https://blog.csdn.net/a123456234/article/details/140966496
module.exports = defineConfig({
    // vue-cli-service build --mode 区分多个环境 参考:  https://www.cnblogs.com/grow-up-up/p/17210430.html
    // build时构建文件的目录 构建时传入 --no-clean 可关闭该行为
    publicPath: process.env.NODE_ENV === 'development' ? './' : '/',
    // 将构建好的文件输出到哪里
    outputDir: 'dist',
    // 放置静态资源的地方 (js/css/img/font/...)
    assetsDir: 'assets',
    // 指定生成的 index.html 的输出路径 (相对于 outputDir)
    // 可以是相对路径也可以是绝对路径
    indexPath: 'index.html',
    // 默认在生成的静态资源文件名中包含 hash 以便更好的控制缓存
    filenameHashing: true,
    //默认情况下 babel-loader 会忽略所有 node_modules 中的文件。你可以启用本选项，以避免构建后的代码中出现未转译的第三方依赖。
    transpileDependencies: true,
    // 默认代码语法检查为true
    lintOnSave: false,
    //是否使用包含运行时编译器的 Vue 构建版本。设置为 true 后你就可以在 Vue 组件中使用 template 选项了，但是这会让你的应用额外增加 10kb 左右。
    runtimeCompiler: true,
    //开启代理服务器
    devServer: {
        port: process.env.VUE_APP_PORT,
        host: process.env.VUE_APP_HOST,
        webSocketServer: false,
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
    },
    pages: {
        index: { // 默认入口
            // page 的入口
            entry: 'src/main.js',
            // 模板来源
            template: 'public/index.html',
            // 在 dist/index.html 的输出
            filename: 'index.html',
            // 当使用 title 选项时，
            // template 中的 <title> 标签需要是 <title><%= htmlWebpackPlugin.options.title %></title>
            title: '拨开命运迷雾',
            // 片段注入到页面的地点，
            // 可以是 (head / end / body / top / bottom)
            // chunks: ['chunk-vendors', 'chunk-common', 'index']
        },
    },

})
