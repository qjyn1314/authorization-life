<template>
  <div id="app">
    <RouterView/>
  </div>
</template>
<script>
export default {}
</script>
<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.ant-modal div[aria-hidden="true"] {
  display: none !important;
}
</style>
