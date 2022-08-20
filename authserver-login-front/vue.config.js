const path = require("path");
const IS_DEV = ['development'].includes(process.env.NODE_ENV); 
const CompressionWebpackPlugin = require("compression-webpack-plugin"); // 开启gzip压缩， 按需引用
const productionGzipExtensions = ['js', 'css']; //压缩的文件类型

module.exports = {
  // 部署应用时的基本 URL 
  // build时构建文件的目录 构建时传入 --no-clean 可关闭该行为 
  publicPath: '/login/',
  outputDir: "dist",
  // build时放置生成的静态资源 (js、css、img、fonts) 的 (相对于 outputDir 的) 目录
  assetsDir: "static",

  // 指定生成的 index.html 的输出路径 (相对于 outputDir)。也可以是一个绝对路径。
  indexPath: "index.html",

  // 默认在生成的静态资源文件名中包含hash以控制缓存
  filenameHashing: true,

  productionSourceMap: IS_DEV,

  // 是否使用包含运行时编译器的 Vue 构建版本
  runtimeCompiler: false,

  // Babel 显式转译列表
  transpileDependencies: [],

  //   lintOnSave：{ type:Boolean default:true } 问你是否使用eslint
  lintOnSave: false,

  // 设置生成的 HTML 中 <link rel="stylesheet"> 和 <script> 标签的 crossorigin 属性（注：仅影响构建时注入的标签）
  crossorigin: "",

  // 在生成的 HTML 中的 <link rel="stylesheet"> 和 <script> 标签上启用 Subresource Integrity (SRI)
  integrity: false,

  // 如果这个值是一个对象，则会通过 webpack-merge 合并到最终的配置中
  // 如果你需要基于环境有条件地配置行为，或者想要直接修改配置，那就换成一个函数 (该函数会在环境变量被设置之后懒执行)。该方法的第一个参数会收到已经解析好的配置。在函数内，你可以直接修改配置，或者返回一个将会被合并的对象
  configureWebpack: {    // 配置打包 js、css文件为.gz格式，优化加载速度  （参考：https://blog.csdn.net/qq_31677507/article/details/102742196）
    devtool: IS_DEV ? 'source-map' : false, 
    plugins: !IS_DEV ? [
      new CompressionWebpackPlugin({
        /* [file]被替换为原始资产文件名。
           [path]替换为原始资产的路径。
           [dir]替换为原始资产的目录。
           [name]被替换为原始资产的文件名。
           [ext]替换为原始资产的扩展名。
           [query]被查询替换。*/
        filename: '[path].gz[query]',
        //压缩算法
        algorithm: 'gzip',
        //匹配文件
        // test: /\.js$|\.css$|\.html$/,
        test: new RegExp('\\.(' + productionGzipExtensions.join('|') + ')$'),
        //压缩超过此大小的文件,以字节为单位
        threshold: 10240,
        minRatio: 0.8 
      })
    ] : []
  },
  chainWebpack (config) {
    config.resolve.symlinks(true); // 修复热更新失效
    config
      .plugin('html')
      .tap(args => {
        args[0].path = `${IS_DEV ? '/static' : './static'}`
        return args
      })
      config.resolve.alias 
      .set("@style", path.resolve("styles"))
      .set("@", path.resolve("src")); 
 
   const types = ["vue-modules", "vue", "normal-modules", "normal"];
    types.forEach(type =>
      addStyleResource(config.module.rule("stylus").oneOf(type))
    );
  },
  // css的处理
  css: {
    // 当为true时，css文件名可省略 module 默认为 false
    requireModuleExtension: true,
    // 是否将组件中的 CSS 提取至一个独立的 CSS 文件中,当作为一个库构建时，你也可以将其设置为 false 免得用户自己导入 CSS
    // 默认生产环境下是 true，开发环境下是 false
    extract: false,
    // 是否为 CSS 开启 source map。设置为 true 之后可能会影响构建的性能
    sourceMap: IS_DEV, //************ */ sourceMap为false时  编译完的样式在style标签中显示 不在各个css中显示
    //向 CSS 相关的 loader 传递选项(支持 css-loader postcss-loader sass-loader less-loader stylus-loader)
    loaderOptions: {
      css: {},
      less: {},
      sass: {
        // additionalData: `$baseUrl: "${process.env.NODE_ENV === 'test' ? '/pc/' : '/pc/'}";`
      },
      stylus: {}
    }
  },
  devServer: {
    host: '0.0.0.0',
    port: 8080,
    // open: true,
    //配置自动启动浏览器
    hotOnly: true,
    disableHostCheck: true,//解决127.0.0.1指向其他域名时出现"Invalid Host header"问题
    proxy: { 
      "/api": { 
        target: "https://www.authorization.life",
        changeOrigin: true,
        pathRewrite:{
          '/api':''
        }
      } 
    },
  }, 
  // 是否为 Babel 或 TypeScript 使用 thread-loader
  parallel: require("os").cpus().length > 1,

  // 向 PWA 插件传递选项
  pwa: {},

  // 可以用来传递任何第三方插件选项
  pluginOptions: {}
};


// 定义函数addStyleResource
function addStyleResource (rule) {
  rule
    .use("style-resource")
    .loader("style-resources-loader")
    .options({
      patterns: [path.resolve(__dirname, "./src/util/css/variable.styl")] //后面的路径改成你自己放公共stylus变量的路径
    });
}
