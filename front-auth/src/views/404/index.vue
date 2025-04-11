<template>
  <div>
    <section class="error-page">
      <div class="info">
        <img
            src="https://storytale-public2.b-cdn.net/2021/08/16/79ea3d66-1f76-4b28-ae87-a77aa9a7dbb0-Sailor.png?height=348"/>
        <h2 class="error-title"> 404 ERROR</h2>
        <p>您所访问的页面不存在，它或已被移动，删除或者重命名。</p>
        <p>请访问其他页面</p>
        <nav>
          <RouterLink class="iconfont icon-fanhuidaoshouye" :to="{ path: '/', replace: true }">Back To Home</RouterLink>
        </nav>

      </div>
    </section>
  </div>

</template>
<script>
export default {
  data() {
    return {}
  }
}
</script>


<style scoped>
.error-page {
  background: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  height: calc(100vh - 200px);
  box-sizing: border-box;
  text-align: center;

  .error-title {
    font-size: 40px;
    padding-bottom: 3px;
    font-weight: bold;
  }

  p {
    font-size: 16px;
    padding: 5px 0;
    color: rgba(243, 35, 35, 0.82);
  }

  img {
    width: 100%;
  }

}
</style>
