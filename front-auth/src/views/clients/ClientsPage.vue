<template>
  <div>
    我是client管理页面
    {{ clients }}
  </div>
</template>

<script>
export default {
  name: "ClientsPage.vue",
  data() {
    return {
      clients: [
        {
          name:"name001",
        }
      ],
    }
  }
}
</script>

<style scoped lang="scss">

</style>