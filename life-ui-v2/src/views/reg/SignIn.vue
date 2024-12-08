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
          <h1>欢迎注册《命运迷雾》</h1>
        </el-header>
        <el-main>
          <el-row>
            <el-col :span="20">
              <el-form label-width="100px" :model="loginForm" :rules="rules" ref="ruleForm">
                <el-form-item prop="username">
                  <el-input ref="username" v-model="loginForm.email" placeholder="邮箱"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                  <el-input type="password" v-model="loginForm.hashPassword" placeholder="密码"></el-input>
                </el-form-item>
                <el-row v-if="showCaptcha">
                  <el-col :span="12">
                    <el-form-item prop="captchaCode">
                      <el-input v-model="loginForm.captchaCode" placeholder="验证码"></el-input>
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <div style="width: 190px;height: 40px; line-height: 40px; margin-right: 5px;" @click="refreshCode">
                      <el-button type="text" :disabled="this.sendBtn !== '发送验证码'">{{ sendBtn }}</el-button>
                    </div>
                  </el-col>
                </el-row>
                <el-form-item>
                  <el-row>
                    <el-col :span="24">
                      <el-button type="danger" style="width: 100%;" @click="ssoReg">注册</el-button>
                    </el-col>
                  </el-row>
                </el-form-item>
                <el-form-item>
                  <el-row>
                    <el-col :span="6">
                      <el-button type="text" @click="goLogin">已有账号</el-button>
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
import {emailRegister, inspirational, sendEmailCode} from '@/api/login'
import {Message} from 'element-ui'

export default {
  name: 'SignIn',
  components: {},
  data() {
    return {
      inspirational: '',
      sendBtn: '发送验证码',
      showCaptcha: true,
      loginForm: {
        email: '',
        hashPassword: '',
        captchaUuid: '',
        captchaCode: '',
      },
      rules: {
        email: [{required: true, message: '请输入邮箱', trigger: 'blur'},],
        hashPassword: [{required: true, message: '请输入密码', trigger: 'blur'},]
      }
    }
  },
  created() {
    this.inspirationalSentence();
  },
  methods: {
    inspirationalSentence() {
      inspirational().then((res) => {
        this.inspirational = res.data;
      });
    },
    refreshCode() {
      if (!this.showCaptcha) {
        return
      }
      if (!this.loginForm.email || this.loginForm.email === '') {
        Message.error("请输入邮箱.")
        return;
      }
      let emailParams = {email: this.loginForm.email};
      // 重新发送邮箱验证码
      sendEmailCode(emailParams).then((res) => {
        if (!res) {
          return
        }
        this.loginForm.captchaUuid = res.data;
        Message.success("邮箱注册验证码已发送,请登录邮箱查收.")
        //在发送成功后进行倒计时描述更改 sendBtn 的值.60秒倒计时开始
        this.countdownTime();
      })
    },
    countdownTime() {

      // 设定倒计时时间（单位：秒）
      let secondsRemaining = 60;
      // 初始化倒计时
      this.intervalId = setInterval(() => {
        // 每秒递减倒计时
        secondsRemaining = secondsRemaining - 1;
        if (secondsRemaining >= 0) {
          this.sendBtn = secondsRemaining
          if (secondsRemaining <= 0) {
            clearInterval(this.intervalId);
            this.sendBtn = '发送验证码'
          }
        }
      }, 1000);

    },
    ssoReg() {
      this.$refs['ruleForm'].validate((valid) => {
        if (valid) {
          if (this.showCaptcha && this.loginForm.captchaCode === '') {
            Message.error("请输入验证码.")
          } else {
            //交给store进行登录并进行存储token以及当前登录用户信息
            emailRegister(this.loginForm).then((res) => {
              if (!res) {
                return
              }
              this.loginForm = {};
              Message.success("邮箱注册注册成功, 请登录.")
              this.goLogin()
            })
          }
        } else {
          return false;
        }
      });
    },
    goLogin() {
      this.$router.push({path: '/login'});
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