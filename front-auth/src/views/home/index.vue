<template>
  <section>
    <div class="img-box">
    </div>
    <hr/>
    <section class="one">
      <div>我是首页index-section</div>
      <header class="major">
        <div>我是首页index-header</div>
      </header>
    </section>
    <section class="intro">
      <div>我是首页index-section</div>
    </section>
  </section>
</template>

<script>

export default {
  data() {
    return {}
  }
}

</script>

<style scoped>
.img-box {
  width: 120px;
  height: auto;
  margin: 2rem auto 0;
}

.intro {
  max-width: 1200px;
  margin: 0 auto;

  .social-list {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 2.5rem 0;

    .iconfont {
      font-size: 2rem;
      margin: 5px;
    }
  }

  .zs-list {
    display: flex;

    .zs {
      display: inline-block;
      width: 120px;
      margin: 1rem;
    }

  }
}

.one {
  max-width: 1200px;
  margin: 2rem auto;

  .major {
    text-align: center;
    margin-bottom: 2rem;

    h1 {
      margin-bottom: 1rem;
    }

    p:nth-child(3) {
      margin-bottom: 1rem;
    }

    a {
      margin: .5rem;
    }
  }
}
</style>
