<template>
  <div class="wrap">
    <img src="../../assets/imgs/whitelogo.png" alt class="login_logo" />
    <div class="login_view">
      <el-form
        :model="ruleForm"
        :rules="rules"
        ref="ruleForm"
        v-if="loginCur == 0 || loginCur == 1"
      >
        <div class="login_item">
          <div class="login_ul">
            <div
              class="login_li"
              v-for="(item, index) in logintab"
              :key="index"
              @click="tabClick(index)"
              :class="loginCur == index ? 'login_licur' : ''"
            >
              {{ item }}
              <span class="login_li_line" v-if="loginCur == index"></span>
            </div>
          </div>
          <!-- 账号登录 -->
          <div class="accountLogin" v-if="loginCur == 0">
            <el-form-item prop="username" :error="formRules.username">
              <el-input
                type="text"
                class="accountLoginInput"
                v-model="ruleForm.username"
                placeholder="账号"
              ></el-input>
            </el-form-item>
            <el-form-item prop="password" :error="formRules.password">
              <el-input
                type="password"
                class="accountLoginInput"
                v-model="ruleForm.password"
                placeholder="密码"
                show-password
              ></el-input>
            </el-form-item>
            <el-form-item
              prop="captchaCode"
              :error="formRules.captchaCode"
              class="verificationCode_view"
              v-if="captchaCode_falg && imageBase64"
            >
              <div class="verificationCode_view">
                <el-input
                  type="text"
                  class="accountLoginInput verificationCode_ipt"
                  maxlength="4"
                  v-model="ruleForm.captchaCode"
                  placeholder="请输入验证码"
                ></el-input>
                <img :src="imageBase64" alt class="verificationCode_img" />
                <div class="verificationCode_btn" @click="getAuthCaptcha">
                  看不清
                  <span>刷新</span>
                </div>
              </div>
            </el-form-item>
          </div>
          <!-- 短信登录 -->
          <div class="smsLogin" v-else-if="loginCur == 1">
            <el-form-item prop="phone" :error="formRules.phone">
              <el-input
                type="text"
                class="smsinput smsinputBorder"
                v-model="ruleForm.phone"
                placeholder="手机号"
                oninput="value=value.replace(/[^\d]/g,'')"
              >
                <el-select slot="prepend" v-model="ruleForm.telAreaCode" class="selectCss">
                  <el-option
                    v-for="item in telAreaCodeList"
                    :key="item.valueCode"
                    :label="item.valueContent"
                    :value="item.valueCode"
                  ></el-option>
                </el-select>
              </el-input>
            </el-form-item>
            <el-form-item prop="captchaCode" :error="formRules.captchaCode">
              <el-input
                type="Number"
                class="smsinput codecss"
                v-model="ruleForm.captchaCode"
                maxlength="6"
                placeholder="请输入验证码"
                oninput="if(value.length>8)value=value.slice(0,8)"
              >
                <el-button
                  class="code_btn"
                  :class="
                    isDisabled
                      ? 'code_btn_no'
                      : !isDisabled && isDisabledOne
                        ? 'code_btn_noOne'
                        : ''
                  "
                  type="primary"
                  @click="sendSms"
                  slot="append"
                >{{ content }}</el-button>
              </el-input>
            </el-form-item>
          </div>
          <!-- 扫码登录 -->
          <div class="codeScanning" v-else-if="loginCur == 2">
            <div class="codeScanning">
              请打开
              <span>微信</span> 扫一扫登录
            </div>
            <img class="codeScanning_img" src="../../assets/imgs/loginbg.png" alt />
          </div>
          <el-form-item v-if="loginCur != 2">
            <el-button class="login_btn login_btn_cur" type="primary" @click="Login('ruleForm')">登录</el-button>
          </el-form-item>
          <div
            class="login_bottom login_bottom_cur"
            :class="loginCur != 0 ? 'login_bottom_cur' : ''"
          >
<!--            <div class="login_bottom_l" v-if="loginCur == 0" >忘记密码</div>-->
            <div class="login_bottom_r" @click="goRegister">
              暂无账号
              <span>去注册</span>
            </div>
            <div class="login_bottom_r" >
              <span><br/></span>
            </div>
            <div class="login_bottom_r" @click="goHome">
              先预览->
              <span>首页</span>
            </div>
          </div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { AUTH_SERVER, LOGINCODE } from '@/api/severApi'
import { getAuthLogin, getLoginSms } from '@/api/login'
import { getAuthCaptchaCode, getAuthCaptcha } from '@/api/common'
import { clearLoginCache } from '@/utils/index'
export default {
  name: 'login',
  data () {
    let checkPhone = (rule, value, callback) => {
      // const phoneReg = /^1[3|4|5|7|8][0-9]{9}$/
      const phoneReg = /^1[0-9]{10}$/
      if (!value) {
        return callback(new Error('手机号不能为空'))
      }
      setTimeout(() => {
        if (!Number.isInteger(+value)) {
          callback(new Error('请输入数字值'))
        } else {
          if (phoneReg.test(value)) {
            callback()
          } else {
            callback(new Error('手机号码格式不正确'))
          }
        }
      }, 100)
    }
    return {
      loading: false,
      loginCur: 0,
      // logintab: ['账号登录', '短信登录', '扫码登录'],
      logintab: ['账号登录', '短信登录'],
      imageBase64: '../../assets/imgs/backupsCode.png',
      ruleForm: {
        needToastErr: 0,
        username: '',
        password: '',
        phone: '',
        telAreaCode: '+86',
        captchaCode: '',
        captchaUuid: '',
        client_id: LOGINCODE.client_id,
        client_secret: LOGINCODE.client_secret,
        redirect_uri: LOGINCODE.redirect_uri
      },
      rules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
        phone: [
          { required: true, validator: checkPhone, trigger: 'blur' }
        ],
        captchaCode: [{ required: true, message: '请输入4位验证码', trigger: 'blur' }]
      },
      captchaCode_falg: false,
      telAreaCodeList: [{ 'valueCode': '+86', 'valueContent': '+86' }],
      isDisabledOne: false,
      isDisabled: false, // 控制按钮是否可以点击（false:可以点击，true:不可点击）
      content: '获取验证码', // 发送验证码按钮的初始显示文字
      timer: null,
      count: '',
      model: {},
      redirect_uri: '',
      formRules: { // 后端返回验证错误信息
        username: '',
        password: '',
        phone: '',
        captchaCode: ''
      }
    }
  },
  computed: {
  },
  created () {
    clearLoginCache()
    this.redirect_uri = this.$route.query.redirect_uri || ''
  },
  methods: {
    tabClick (index) {
      // eslint-disable-next-line eqeqeq
      if (index == 2) return
      this.loginCur = index
      this.formRules = {
        username: '',
        password: '',
        phone: '',
        captchaCode: ''
      }
      this.$refs['ruleForm'].resetFields()
      this.ruleForm.captchaCode = ''
      this.ruleForm.captchaUuid = ''
    },
    // 发送验证码
    sendSms () {
      if (this.isDisabled) {
        return
      }
      // 校验手机号
      // eslint-disable-next-line eqeqeq
      if (!this.ruleForm.phone) {
        this.$message.error('请输入手机号')
        return
      }
      if (!(/^1[34578]\d{9}$/.test(this.ruleForm.phone))) {
        this.$message.error('请输入正确的手机号')
        return
      }
      // 请求后台发送验证码至手机
      getAuthCaptchaCode({ telephone: this.ruleForm.phone }).then((res) => {
        if (Number(res.data.code) === 0) {
          this.isDisabled = false
          this.ruleForm.captchaUuid = res.data.data || ''
          this.$message.success('短信发送成功')
        }
      })
      // 控制倒计时及按钮是否可以点击
      const TIME_COUNT = 60
      this.count = TIME_COUNT
      this.timer = window.setInterval(() => {
        // eslint-disable-next-line eqeqeq
        if (this.count > 0 && this.count <= TIME_COUNT) {
          // 倒计时时不可点击
          this.isDisabled = true
          // 计时秒数
          this.count--
          // 更新按钮的文字内容
          this.content = this.count + 's后重新获取'
        } else {
          // 倒计时完，可点击
          this.isDisabledOne = true
          this.isDisabled = false
          // 更新按钮文字内容
          this.content = '获取验证码'
          // 清空定时器!!!
          clearInterval(this.timer)
          this.timer = null
        }
      }, 1000)
    },
    Login (formName) { // 账号和短信登录
      let self = this
      self.formRules = {
        username: '',
        password: '',
        phone: '',
        captchaCode: ''
      }
      self.$refs[formName].validate((valid) => {
        if (valid) {
          // eslint-disable-next-line eqeqeq
          if (self.loginCur == 0) {
            getAuthLogin(self.ruleForm).then(resdata => {
              let res = resdata.data
              // eslint-disable-next-line eqeqeq
              if (res.code == '-2') { 
                self.formRules = res.data.errorVO || {} // 后台返回的错误信息赋值给变量errorMsg
                if (self.formRules.defaultDesc) {
                  self.$message.error(self.formRules.defaultDesc)
                }

                // eslint-disable-next-line eqeqeq
                if (res.data.showCaptchaCode) {
                  self.getAuthCaptcha()
                }
              } else {
                self.getRedirectUri()
              }
            })
            // eslint-disable-next-line eqeqeq
          } else if (self.loginCur == 1) {
            getLoginSms(self.ruleForm).then(resdata => {
               let res = resdata.data
              // eslint-disable-next-line eqeqeq
              if (res.code == '-2') {
                self.formRules = res.data.errorVO || {} // 后台返回的错误信息赋值给变量errorMsg
                if (self.formRules.defaultDesc) {
                  self.$message.error(self.formRules.defaultDesc)
                }
              } else {
                self.getRedirectUri()
              }
            })
          }
        } else {
          return false
        }
      })
    },
    getRedirectUri () {
      console.log("已登录成功...")
      console.log("授权码模式...")
      console.log("请求授权确认..."+`${LOGINCODE.loginRedirectUrl}/${AUTH_SERVER}/oauth2/authorize?response_type=${LOGINCODE.response_type}&client_id=${this.ruleForm.client_id}&scope=${LOGINCODE.scope}&state=${LOGINCODE.state}&redirect_uri=${this.redirect_uri ? encodeURIComponent(this.redirect_uri) : encodeURIComponent(LOGINCODE.redirect_uri)}`)
      window.location.href = `${LOGINCODE.loginRedirectUrl}/${AUTH_SERVER}/oauth2/authorize?response_type=${LOGINCODE.response_type}&client_id=${this.ruleForm.client_id}&scope=${LOGINCODE.scope}&state=${LOGINCODE.state}&redirect_uri=${this.redirect_uri ? encodeURIComponent(this.redirect_uri) : encodeURIComponent(LOGINCODE.redirect_uri)}`
    },
    getAuthCaptcha () { // 账号登录 -在登录失败10次之后，获取验证码图片接口
      getAuthCaptcha().then(res => {
        if (Number(res.data.code) === 0) {
          this.ruleForm.captchaUuid = res.data.data.uuid
          this.imageBase64 = res.data.data.imageBase64
          this.captchaCode_falg = true
        }
      })
    },
    goRegister () {
      this.$router.push({
        name: 'register'
      })
    },
    goHome () {
      this.$router.push({
        name: 'home'
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.wrap {
  width: 100%;
  min-height: 100vh;
  background: url("../../assets/imgs/loginbg.png") no-repeat center;
  background-size: 100% 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  .login_logo {
    width: 200px;
    min-height: 60px;
    margin-bottom: 40px;
    margin-top: 80px;
  }
  .login_view {
    width: 360px;
    border-radius: 8px;
    background: #fff;
    .login_item {
      width: 280x;
      height: auto;
      margin: 24px 40px 0 40px;
      .login_ul {
        width: 100%;
        display: flex;
        flex-direction: row;
        justify-content: space-around;
        align-items: center;
        margin-bottom: 34px;
        .login_li {
          color: #666666;
          font-size: 16px;
          font-weight: 400;
          position: relative;
          cursor: pointer;
          .login_li_line {
            display: inline-block;
            position: absolute;
            left: 0;
            bottom: -10px;
            width: 64px;
            height: 2px;
            background: #1ba1ba;
          }
        }
        .login_licur {
          color: #1ba1ba;
          font-weight: 500;
        }
      }
      .accountLogin {
        .accountLoginInput .el-input__inner {
          width: 280px;
          height: 32px;
          background: #ffffff;
          border-radius: 2px;
        }
        .verificationCode_view {
          position: relative;
          ::v-deep .verificationCode_ipt .el-input__inner {
            width: 56.8%;
            border-bottom-right-radius: 0px;
            border-top-right-radius: 0px;
          }
          .verificationCode_btn {
            position: absolute;
            top: 30px;
            right: 0px;
            color: #666666;
            font-size: 14px;
            cursor: pointer;
            span {
              color: #1ba1ba;
            }
          }
          .verificationCode_img {
            cursor: pointer;
            position: absolute;
            top: 0px;
            right: 2px;
            width: 42%;
            height: 30px;
            border: 1px solid #d6d6d6;
            border-left: none;
            border-bottom-right-radius: 2px;

            border-top-right-radius: 2px;
          }
        }
      }
      .smsLogin {
        ::v-deep .smsinput .el-input-group__append {
          border: none !important;
        }
        .smsinput .el-input__inner {
          height: 32px;
          border-radius: 2px;
          border-top-right-radius: 0px;
          border-bottom-right-radius: 0px;
        }
        .smsinputBorder .el-input__inner {
          border-top-left-radius: 0px;
          border-bottom-left-radius: 0px;
        }
        ::v-deep .selectCss {
          width: 80px;
          border-radius: 0;
        }
        .selectCss ::v-deep .el-input__inner {
          border-left: none;
          border-radius: 0;
        }
        .code_btn {
          width: 120px;
          height: 32px;
          background: #1ba1ba;
          border-radius: 2px;
          color: #fff;
          border-top-left-radius: 0px;
          border-bottom-left-radius: 0px;
        }
        .code_btn_no {
          background: #f0f0f0;
          height: 32px;
          color: #999999;
          border: 1px solid #d6d6d6;
          border-left: none; 
          margin: 0px -10px 0 -10px;
        }
        .codecss {
          height: 32px;
          border-top-right-radius: 0px;
          border-bottom-right-radius: 0px;
        }
        ::v-deep .el-input-group__append,
        ::v-deep .el-input-group__prepend {
          border-radius: 0px !important;
          vertical-align: center !important;
          background-color: #fff !important;
        }
        .code_btn_noOne {
          height: 32px;
          background: #fff;
          color: #1ba1ba;
          border-radius: 2px;
          border: 1px solid #1ba1ba;
          margin: 0px -10px 0 -10px;
        }
      }
      .codeScanning {
        display: flex;
        flex-direction: column;
        align-items: center;
        margin-bottom: 24px;
        .codeScanning {
          display: flex;
          flex-direction: row;
          font-size: 12px;
          font-weight: 400;
          color: #666666;
          line-height: 18px;
          margin-bottom: 8px;
          span {
            color: #1ba1ba;
          }
        }
        .codeScanning_img {
          width: 160px;
          height: 160px;
        }
      }
      .login_btn {
        width: 280px;
        height: 40px;
        background: #f0f0f0;
        border-radius: 2px;
        border: 1px solid #d6d6d6;
        color: #999999;
        margin-top: 10px;
        font-weight: 800;
        letter-spacing: 2px;
      }
      .login_btn_cur {
        background: #1ba1ba;
        color: #fff;
        border-radius: 2px;
        border: 1px solid #d6d6d6;
      }
      .login_bottom {
        width: 100%;
        display: flex;
        flex-direction: row;
        justify-content: space-between;
        align-items: center;
        font-size: 14px;
        font-weight: 400;
        line-height: 22px;
        margin-bottom: 24px;
        .login_bottom_l {
          color: #1ba1ba;
        }
        .login_bottom_r {
          color: #666666;
          cursor: pointer;
          span {
            color: #1ba1ba;
          }
        }
      }
      .login_bottom_cur {
        justify-content: center;
      }
    }
  }
}
::v-deep input::-webkit-outer-spin-button,
::v-deep input::-webkit-inner-spin-button {
  -webkit-appearance: none !important;
}
::v-deep input[type="number"] {
  -moz-appearance: textfield !important;
}
::v-deep .el-select .el-input .el-select__caret {
  line-height: 32px;
}
</style>
