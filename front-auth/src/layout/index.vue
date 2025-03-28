<template>
  <main>
    <div>我是布局main</div>
    <!--    下面 主体-->
    <section>
      <div>我是布局主体section</div>
      <section>
        <div>我是布局主体--RouterView</div>
        <RouterView/>
      </section>
    </section>
  </main>
</template>

<script>
export default {
  name: "index"
}
</script>

<style>

</style>
