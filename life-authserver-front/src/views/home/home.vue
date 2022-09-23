<template>
  <content-layout :showSearch="false" v-loading="loading">
    <template slot="title">系统首页</template>
    <template slot="body">

      <h2>恭喜你登录成功了。</h2>
      <h3>此处可以为系统首页的内容，并做一些统计工作。</h3>
      <h4>我们将在这里完成对 accessToken 的获取。</h4>
      <h4>获取的accessToken对象是：{{ accessToken }}</h4>
    </template>
  </content-layout>
</template>

<script>
import {ContentLayout} from '@/components'
import {getOauth2TokenByCode} from '@/api/common'

export default {
  name: 'home',
  components: {
    ContentLayout,
  },
  data() {
    return {
      loading: false,
      tokenByCode: {

      },
      accessToken: {

      }
    }
  },
  computed: {},
  mounted() {

  },
  methods: {
    getAccessToken() {
      console.log(tokenByCode)
      getOauth2TokenByCode(tokenByCode).then(res => {
        console.log(res);
      })
    }
  }
}
</script>
<style lang="scss" scoped>
</style>
