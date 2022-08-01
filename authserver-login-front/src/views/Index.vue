<template>
  <content-layout :showSearch="false" v-loading="loading">
    <template slot="title">系统首页</template>
    <template slot="body"> </template>
  </content-layout>
</template>

<script> 
import { ContentLayout } from '@/components'
export default {
  name: 'Index',
  components: {
    ContentLayout
  },
  data () {
    return {
      loading: false
    }
  },
  computed: {
  },
  mounted () {
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
</style>
