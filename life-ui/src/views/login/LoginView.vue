<template>
  <div>
    <el-container>
      <el-aside :width="'890px'">Aside</el-aside>
      <el-container>
        <el-header :height="'180px'">改变命运系统</el-header>
        <el-main>
          <el-form label-position="left" label-width="80px" :model="loginForm">
            <el-form-item label="账号">
              <el-input v-model="loginForm.username"></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="loginForm.password"></el-input>
            </el-form-item>
            <el-form-item label="验证码">
              <el-row>
                <el-col :span="12">
                  <el-input v-model="loginForm.captchaCode"></el-input>
                </el-col>
                <el-col :span="12">
                  <div style="width: 190px;height: 40px; line-height: 40px">
                    <el-image :src="captcha.imageBase64"></el-image>
                  </div>
                </el-col>
              </el-row>
            </el-form-item>
          </el-form>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import {getClient, pictureCode} from '@/api/login'

export default {
  name: 'TempPage',
  components: {},
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        captchaUuid: '',
        captchaCode: '',
      },
      captcha: {
        imageBase64: '',
      }
    }
  },
  created() {
    // 刷新图片验证码
    this.flushedPictureCode();
    // 根据浏览器地址获取认证信息

  },
  methods: {
    flushedPictureCode() {
      getClient().then((res) => {
        console.log(res)
      })
      pictureCode().then((res) => {
        if (!res) {
          return
        }
        this.captcha.imageBase64 = res.data.imageBase64;
        this.loginForm.captchaUuid = res.data.uuid;
      })
    }
  },
  mounted() {

  },
  beforeDestroy() {
  }
}

</script>
<style scoped>
.el-aside {
  background-color: #D3DCE6;
  text-align: center;
  line-height: 690px;
}

.el-header {
  text-align: center;
  line-height: 180px;
}
</style>