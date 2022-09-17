<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>vue-初体验</title>
    <script src="../js/vue.js"></script>
</head>
<body>

<div id="app">
    <h1>{{ message }}</h1>
    <h2>{{ name }}</h2>
    <h3>{{ age }}</h3>
    <h3><a v-bind:href="url">点击我即可跳转至百度首页。</a></h3>
    <h3><a :href="url">点击我即可跳转至百度首页。</a></h3>
</div>

<script>
    Vue.config.productionTip = false;
    // let 变量； const 常量；
    const app = new Vue({
        // 选项
        el: '#app',
        data: {
            message: 'Vue Hello World!!!',
            name: '张三',
            age: 25,
            url: 'https://www.baidu.com'
        }
    })
</script>

</body>
</html>