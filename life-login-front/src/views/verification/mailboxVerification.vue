<template>
  <div class="wrap"> 
    <img class="wrap_img" src="../../assets/login.png">
    <div class="wrap_cont">
        <div class="wrap_cont_status" :class="validateStatus ? '' : 'wrapRed'">{{validateStatus ? '验证成功！' : '验证失败！' }}</div>
        <div class="wrap_cont_login" @click="goLogin">去登录</div>
    </div>
  </div>
</template>

<script> 
export default {
  name: 'mailboxVerification',
  data () { 
    return {  
      validateStatus:''
    }
  }, 
  mounted () {
    this.validateStatus = this.$route.query.validateStatus || ''
  },
  methods: { 
    goLogin(){
       window.location.href = `${process.env.VUE_APP_API_HOST}/login`
    }
  }
}
</script>
<style lang="scss" scoped>
.wrap {
  width: 100%;
   height: 100%;  
   background: #fff;
   display: flex;
   flex-direction: column; 
   align-items: center;
   .wrap_img{
     margin-bottom: 20px;
     margin-top: 10%;
   }
   .wrap_cont{
     width: 400px;
     height: 223px;
     border: 1px solid #d6d6d657;
     .wrap_cont_status{
       text-align: center;
       margin-top: 50px;
       font-size: 16px;
       color: #333; 
     }
     .wrapRed{
        color: red;
      }
     .wrap_cont_login{
       margin-top: 100px;
      text-align: center; 
       font-size: 14px;
       color: #1ba1ba;
       cursor: pointer;
     }
   }
}

</style>
