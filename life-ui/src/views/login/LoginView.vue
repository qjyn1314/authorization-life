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
import {getClient, inspirational, pictureCode} from '@/api/login'
import {Message} from 'element-ui'
import store from "@/store";
import pubsub from 'pubsub-js';

export default {
  name: 'TempPage',
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
    this.pictureAndClient();
    this.inspirationalSentence();

    let statusStr = "<lable style=\"background-color:#EBEDF1; padding:10px; color: #7A7B92;border-radius: 12px;\">未提交</label>";
    let number = statusStr.indexOf('未提交');
    console.log("number-->", number)

    let testArr = [
      {
        id: 10,
        name: "zhangsan",
        age: 25
      },
      {
        id: 11,
        name: "zhangsan",
        age: 26
      },
      {
        id: 12,
        name: "李四",
        age: 27
      },
      {
        id: 14,
        name: "李四",
        age: 270
      },
      {
        id: 13,
        name: "王五",
        age: 28
      },
    ]

    let count = 0;
    for (let i = 0; i < testArr.length; i++) {
      for (let j = 1; j < testArr.length; j++) {
        if (testArr[i].id !== testArr[j].id && testArr[i].name + testArr[i].age === testArr[j].name + testArr[j].age) {
          ++count;
        }
      }
    }
    console.log('count-->', count)
    if (count > 0) {
      Message.info("有重复数据.")
    }

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
        this.loginForm.redirect_uri = res.data.redirectUri;
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
            Message.error("请输入验证码.")
          } else {
            //交给store进行登录并进行存储token以及当前登录用户信息
            store.dispatch('ssoAuth/securityLogin', this.loginForm)
          }
        } else {
          return false;
        }
      });
    }
  },
  mounted() {
    //生命周期钩子
    // eslint-disable-next-line vue/valid-next-tick
    this.$nextTick(() => {
      //在页面加载成功后为username输入框获取焦点事件
      this.$refs.username.focus();
    }, 12)

    //在组件生命周期中, 一旦加载页面, 就开始订阅消息 订阅消息 名称为: pubsub_001
    this.pubsubId = pubsub.subscribe('showCaptcha', (name, value) => {
      this.showCaptcha = value.showCaptcha;
      this.refreshCode();
    })

  },
  beforeDestroy() {
    pubsub.unsubscribe(this.pubsubId);
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