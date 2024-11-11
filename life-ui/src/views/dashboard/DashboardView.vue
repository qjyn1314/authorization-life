<template>
  <div>
    <h2>登录成功后的首页</h2>
  </div>
</template>

<script>
import {setValueExpires,setValue} from '@/utils/cookie-util'

export default {
  name: 'DashboardView',
  components: {},
  created() {
    //VUE_APP_COOKIE_HOST
    // setValue("test", "test");
    var str  = "10017664,10003738,10034346,10008839,10008843,10008845,10008830,10008847,10008841,10008834,10008836,10096187";
    let mdmCodes = ["10036105","10017664","10017663","10017850","10094238","10000291","10100453","10034568","10017656"];
    let mdmCode = "";
    for (let i = 0; i < mdmCodes.length; i++) {
      if(str.indexOf(mdmCodes[i]) > -1) {
        mdmCode = mdmCodes[i]
        break;
      }
    }
    if(mdmCode){
      console.log(mdmCode);
    }

  },
  mounted() {
  },
  beforeDestroy() {
  }
}
</script>
<style scoped>

</style>
