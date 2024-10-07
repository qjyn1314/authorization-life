<template>
  <div>
    <el-container class="login_view_container">
      <el-aside class="login_view_aside" :width="'890px'"></el-aside>
      <el-container>
        <el-header :height="'180px'">
          <h1>命运之门</h1>
        </el-header>
        <el-main>
          <el-row>
            <el-col :span="20">
              <el-form label-width="100px" :model="loginForm">
                <el-form-item>
                  <el-input ref="username" v-model="loginForm.username" placeholder="邮箱"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-input v-model="loginForm.password" placeholder="密码"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-row>
                    <el-col :span="12">
                      <el-input v-model="loginForm.captchaCode" placeholder="验证码"></el-input>
                    </el-col>
                    <el-col :span="12">
                      <div style="width: 190px;height: 40px; line-height: 40px">
                        <el-image :src="captcha.imageBase64"></el-image>
                      </div>
                    </el-col>
                  </el-row>
                </el-form-item>
                <el-form-item>
                  <el-row>
                    <el-col :span="6" :offset="18">
                      <el-button type="text" size="small">忘记密码</el-button>
                    </el-col>
                  </el-row>
                </el-form-item>
                <el-form-item>
                  <el-row>
                    <el-col :span="24">
                      <el-button type="danger" style="width: 100%;">登录</el-button>
                    </el-col>
                  </el-row>
                </el-form-item>
                <el-form-item>
                  <el-row>
                    <el-col :span="6">
                      <el-button type="text" icon="el-icon-chat-round">微信</el-button>
                    </el-col>
                    <el-col :span="6" :offset="12">
                      <el-button type="text">立即注册</el-button>
                    </el-col>
                  </el-row>
                </el-form-item>
              </el-form>
            </el-col>
          </el-row>
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
    this.pictureAndClient();
  },
  methods: {
    pictureAndClient() {
      // 使用URL API来获取域名
      const url = new URL(window.location.href);
      // 根据浏览器地址获取认证信息
      getClient({domain: url.hostname}).then((res) => {
        console.log(res)
      })
      // 刷新图片验证码
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
    //生命周期钩子
    // eslint-disable-next-line vue/valid-next-tick
    this.$nextTick(() => {
      //在页面加载成功后为username输入框获取焦点事件
      this.$refs.username.focus();
    }, 12)
  },
  beforeDestroy() {
  }
}

</script>
<style scoped>
.el-aside {
  text-align: center;
  line-height: 100%;
}

.login_view_container {
  height: 100%;
}

.login_view_aside {
  //background: url("../../assets/images/loginback.png") no-repeat center;
}

.el-header {
  text-align: center;
  line-height: 180px;
}
</style>