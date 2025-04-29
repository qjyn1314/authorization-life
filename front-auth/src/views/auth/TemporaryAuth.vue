<template>
  <h2>我是临时授权页面用于获取临时code 和 state </h2>
</template>
<script>
import {oauth2TemporaryCode} from "@/stores/modules/user-auth-code";

export default {
  name: "TemporaryAuth",
  created() {
    const oauth2Code = oauth2TemporaryCode();
    console.log("进入到了临时授权页面.")
  }
}
</script>

<style>

</style>