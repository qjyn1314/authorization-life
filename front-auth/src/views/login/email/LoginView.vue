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
            <el-col>
              <el-row>
                <el-col :span="20" :offset="2">
                  <el-form :model="loginForm" :rules="rules" ref="ruleForm">
                    <el-form-item prop="username">
                      <el-input ref="username" v-model="loginForm.username" placeholder="邮箱"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                      <el-input type="password" v-model="loginForm.password" placeholder="密码"></el-input>
                    </el-form-item>
                  </el-form>
                </el-col>
              </el-row>
              <div v-if="showCaptcha">
                <el-row>
                  <el-col :span="10" :offset="2">
                    <el-form-item prop="captchaCode">
                      <el-input v-model="loginForm.captchaCode" placeholder="验证码"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="10">
                    <div style="width: 190px;height: 40px; line-height: 40px;"
                         @click="refreshCode">
                      <el-image :src="captcha.imageBase64"></el-image>
                    </div>
                  </el-col>
                </el-row>

                <el-row>
                  <el-col :span="20" :offset="2">
                    <h6 style="color: red">密码输入错误累计十次将锁定三小时.</h6>
                  </el-col>
                </el-row>
              </div>

              <el-row>
                <el-col :span="6" :offset="18">
                  <el-form-item>
                    <el-link type="info" size="small" @click="goResetPwd" :icon="Promotion">忘记密码</el-link>
                  </el-form-item>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="8" :offset="2">
                  <el-button type="danger" style="width: 100%" @click="ssoLogin">登录</el-button>
                </el-col>
                <el-col :span="8" :offset="4">
                  <el-button style="width: 100%" @click="goReg">注册</el-button>
                </el-col>
              </el-row>

              <el-row>
                <el-col :span="20" :offset="2">
                  <el-divider>
                    <el-icon>
                      <star-filled/>
                    </el-icon>
                  </el-divider>
                </el-col>
              </el-row>

              <!--
                   iconfont字体图标库:
                   https://www.iconfont.cn/
                   官网->帮助->代码应用
                   https://www.iconfont.cn/help/detail
              -->
              <el-row>
                <el-col :span="4" :offset="2">
                  <el-link class="iconfont icon-weixin" :underline="false" type="success"
                           href="http://weixin.qq.com"></el-link>
                </el-col>
                <el-col :span="1">
                  <el-link class="iconfont icon-qq" :underline="false" type="primary" href="http://im.qq.com"></el-link>
                </el-col>
              </el-row>

            </el-col>
          </el-row>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import {getClient, inspirational, oauthLogin, pictureCode} from '@/api/login-api'
import {prompt} from "@/utils/msg-util";
import {ChatDotRound, Promotion, StarFilled} from "@element-plus/icons-vue";

export default {
  name: 'LoginView',
  computed: {
    ChatDotRound() {
      return ChatDotRound
    },
    Promotion() {
      return Promotion
    }
  },
  components: {StarFilled},
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
    console.log("当前路径中的参数:", this.$route.query)
    this.loginForm.redirect_uri = this.$route.query.redirect_uri
    console.log(this.loginForm.redirect_uri)
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
      console.log('this.loginForm', this.loginForm);
      oauthLogin(this.loginForm).then((res) => {
        if (res.code === 0) {
          // 登录成功
          console.log('登录成功信息', res)
          this.oauth2Token()
        } else {
          this.showCaptcha = res.data.showCaptchaCode;
          prompt.error(res.msg)
        }
      })

      // 在登录接口请求成功之后, 再次请求 oauth2/token接口


    },
    oauth2Token() {

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
  height: 635px;
}

.login_view_aside {
  background: url("../../../assets/images/loginback.png") no-repeat center;
}

.el-header {
  text-align: center;
  line-height: 180px;
}
</style>