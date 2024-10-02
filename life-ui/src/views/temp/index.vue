<template>
  <h2>模板列表页面</h2>
</template>

<script>
import {pageTemp} from "@/api/template";

export default {
  // eslint-disable-next-line vue/multi-word-component-names
  name: 'temp',
  components: {},
  created() {
    // pageTemp(res => {
    //   console.log(res)
    // })
    let params = {pageNum: 1, pageSize: 10}
    pageTemp(params).then(res => {
      console.log(res)
    })
  },
  mounted() {

  },
  beforeDestroy() {
  }
}
</script>

<style scoped>

</style>