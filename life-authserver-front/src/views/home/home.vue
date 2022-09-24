<template>
  <content-layout :showSearch="true" v-loading="loading">
    <template slot="title">系统首页</template>
    <template slot="body">

      <h2>恭喜你登录成功了。</h2>
      <h3>此处可以为系统首页的内容，并做一些统计工作。</h3>
      <h4>我们将在这里完成对 accessToken 的获取。</h4>
      <h4>路径中的参数是：{{ this.$route.query }}</h4>
      <h4>获取的accessToken对象传参：{{ tokenByCode }}</h4>
      <h4>获取的accessToken对象是：{{ accessToken }}</h4>
    </template>
  </content-layout>
</template>

<script>
import {CODE_ACCESS_TOKEN} from '@/api/severApi'
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
        grant_type: CODE_ACCESS_TOKEN.grant_type,
        code: '',
        state: '',
        redirect_uri: CODE_ACCESS_TOKEN.redirect_uri,
        client_id: CODE_ACCESS_TOKEN.client_id
      },
      accessToken: {}
    }
  },
  computed: {},
  mounted() {
    // 在此处需要获取地址栏中的相关参数：code，然后根据code获取accessToken
    this.tokenByCode.code = this.$route.query.code;
    this.tokenByCode.state = this.$route.query.state;
    console.log(this.tokenByCode)
    getOauth2TokenByCode(this.tokenByCode).then(res => {
      console.log(res);
    })
  },
  methods: {}
}
</script>
<style lang="scss" scoped>
</style>
