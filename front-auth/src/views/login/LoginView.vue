<template>
  <div>
    <el-container class="login_view_container">
      <el-aside class="login_view_aside" :width="'890px'">
        <div class="login_view_aside_inspirational">
          <p>{{ inspirational }}</p>
        </div>
      </el-aside>
      <el-container>
        <el-header :height="'180px'">
          <h1>命运迷雾</h1>
        </el-header>
        <el-main>
          <el-row>
            <el-col :span="20">
              <el-form label-width="100px" :model="loginForm" :rules="rules" ref="ruleForm">
                <el-form-item prop="username">
                  <el-input ref="username" v-model="loginForm.username" placeholder="邮箱"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                  <el-input type="password" v-model="loginForm.password" placeholder="密码"></el-input>
                </el-form-item>
                <el-row v-if="showCaptcha">
                  <el-col :span="12">
                    <el-form-item prop="captchaCode">
                      <el-input v-model="loginForm.captchaCode" placeholder="验证码"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <div style="width: 190px;height: 40px; line-height: 40px; margin-right: 5px" @click="refreshCode">
                      <el-image :src="captcha.imageBase64"></el-image>
                    </div>
                  </el-col>
                </el-row>
                <el-row v-if="showCaptcha">
                  <h6 style="color: red">密码输入错误十次将锁定三小时.</h6>
                </el-row>
                <el-form-item>
                  <el-row>
                    <el-col :span="6" :offset="18">
                      <el-button type="text" size="small" @click="goResetPwd">忘记密码</el-button>
                    </el-col>
                  </el-row>
                </el-form-item>
                <el-form-item>
                  <el-row>
                    <el-col :span="24">
                      <el-button type="danger" style="width: 100%;" @click="ssoLogin">登录</el-button>
                    </el-col>
                  </el-row>
                </el-form-item>
                <el-form-item>
                  <el-row>
                    <el-col :span="6">
                      <el-button type="text" icon="el-icon-chat-round">微信</el-button>
                    </el-col>
                    <el-col :span="6" :offset="12">
                      <el-button type="text" @click="goReg">立即注册</el-button>
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
import {getClient, inspirational, pictureCode} from '@/api/login-api'
import {prompt} from "@/utils/msg-util";

export default {
  name: 'LoginView',
  components: {},
  data() {
    return {
      inspirational: '',
      showCaptcha: false,
      loginForm: {
        username: '',
        password: '',
        captchaUuid: '',
        captchaCode: '',
        client_id: '',
        client_secret: '',
        redirect_uri: '',
      },
      captcha: {
        imageBase64: '',
      },
      rules: {
        username: [{required: true, message: '请输入邮箱', trigger: 'blur'},],
        password: [{required: true, message: '请输入密码', trigger: 'blur'},]
      }
    }
  },
  created() {
    // 获取地址栏中的回调地址并赋值给登录表单. 如果没有回调地址, 则从client信息中赋值回调地址

    this.pictureAndClient();
    this.inspirationalSentence();
  },
  methods: {
    inspirationalSentence() {
      inspirational().then((res) => {
        this.inspirational = res.data;
      });
    },
    pictureAndClient() {
      // 使用URL API来获取域名
      const url = new URL(window.location.href);
      // 根据浏览器地址获取认证信息
      getClient({domain: url.hostname}).then((res) => {
        if (!res) {
          return;
        }
        this.loginForm.client_id = res.data.clientId;
        this.loginForm.client_secret = res.data.clientSecret;
        // 如果回调地址不存在则赋值
        if (!this.loginForm.redirect_uri) {
          this.loginForm.redirect_uri = res.data.redirectUri;
        }
      })
      this.refreshCode();
    },
    refreshCode() {
      if (!this.showCaptcha) {
        return
      }
      // 刷新图片验证码
      pictureCode().then((res) => {
        if (!res) {
          return
        }
        this.captcha.imageBase64 = res.data.imageBase64;
        this.loginForm.captchaUuid = res.data.uuid;
      })
    },
    ssoLogin() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          if (this.showCaptcha && this.loginForm.captchaCode === '') {
            prompt.error("请输入验证码.")
          } else {
            this.oauth2SsoLogin();
          }
        } else {
          return false;
        }
      });
    },
    goReg() {
      this.$router.push({path: '/register'});
    },
    goResetPwd() {
      this.$router.push({path: '/reset-pwd'});
    },
    oauth2SsoLogin() {
      // 请求登录接口

      // 在登录接口请求成功之后, 再次请求 oauth2/token接口


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

.login_view_aside_inspirational {
  display: flex;
  width: auto;
  height: 200px;
  margin-top: 270px;
  text-align: center;
  padding-left: 100px;
  padding-right: 100px;
}

.login_view_aside_inspirational p {
  font-size: 30px;
  font-weight: bold;
}

.el-aside {
  text-align: center;
}

.login_view_container {
  height: 685px;
}

.login_view_aside {
  background: url("../../assets/images/loginback.png") no-repeat center;
  //background: url("../../assets/images/天气之子.jpg") no-repeat center;
  //background: url("../../assets/images/云和山风景4K壁纸_彼岸图网.jpg") no-repeat center;
}

.el-header {
  text-align: center;
  line-height: 180px;
}
</style>