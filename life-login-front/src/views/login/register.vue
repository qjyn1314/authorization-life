<template>
  <div class="wrap">
    <img src="../../assets/imgs/whitelogo.png" alt class="login_logo" />
    <div class="login_view">
      <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="120px">
        <div class="login_title">企业注册</div>
        <div class="login_item">
          <el-form-item label="用户姓名：" prop="realName" :error="formRules.realName" class="fitem">
            <el-input
              class="smsinput"
              v-model="ruleForm.realName"
              auto-complete="off"
              placeholder="请输入您的用户姓名"
              clearable
            ></el-input>
            <img src="../../assets/imgs/complete.png" alt class="fitem_img" v-if="realName_falg" />
          </el-form-item>
          <el-form-item
            label="企业名称："
            prop="companyName"
            :error="formRules.companyName"
            class="fitem"
          >
            <el-input
              class="smsinput"
              v-model="ruleForm.companyName"
              placeholder="请输入营业执照上的企业名称"
              clearable
            ></el-input>
            <img src="../../assets/imgs/complete.png" alt class="fitem_img" v-if="companyName_falg" />
          </el-form-item>
          <el-form-item
            label="社会信用代码:"
            prop="creditCode"
            :error="formRules.creditCode"
            class="fitem"
          >
            <el-input
              class="smsinput"
              v-model="ruleForm.creditCode"
              placeholder="请输入社会信用代码"
              clearable
            ></el-input>
            <img src="../../assets/imgs/complete.png" alt class="fitem_img" v-if="creditCode_falg" />
          </el-form-item>
          <el-form-item label="邮箱:" prop="email" :error="formRules.email" class="fitem">
            <el-input
              class="smsinput"
              v-model="ruleForm.email"
              placeholder="请输入可用邮箱用于接收平台邮件"
              clearable
            ></el-input>
            <img src="../../assets/imgs/complete.png" alt class="fitem_img" v-if="email_falg" />
          </el-form-item>
          <el-form-item label="手机号码:" prop="telephone" :error="formRules.telephone" class="fitem">
            <el-input
              class="smsinput smsinputBorder"
              v-model="ruleForm.telephone"
              placeholder="可用于登录及找回密码"
              oninput="value=value.replace(/[^\d]/g,'')"
            >
              <el-select slot="prepend" v-model="ruleForm.areaCode" filterable class="selectCss">
                <el-option
                  v-for="item in telAreaCodeList"
                  :key="item.valueCode"
                  :label="item.valueContent"
                  :value="item.valueCode"
                ></el-option>
              </el-select>
            </el-input>
            <img src="../../assets/imgs/complete.png" alt class="fitem_img" v-if="telephone_falg" />
          </el-form-item>
          <el-form-item label="验证码:" prop="code" :error="formRules.code" class="fitem">
            <el-input
              class="smsinput codecss"
              v-model="ruleForm.code"
              placeholder="请输入验证码"
              oninput="value=value.replace(/[^\d]/g,'')"
            >
              <el-button
                class="code_btn"
                :class="
                  isDisabled ? 'code_btn_no' : !isDisabled && isDisabledOne ? 'code_btn_noOne' : ''
                "
                type="primary"
                @click="sendSms"
                slot="append"
              >{{ content }}</el-button>
            </el-input>
            <img src="../../assets/imgs/complete.png" alt class="fitem_img" v-if="code_falg" />
          </el-form-item>
          <el-form-item label="业务方向:" prop="campDesc" :error="formRules.campDesc" class="fitem">
            <el-select
              class="smsinput"
              style="width: 100%"
              v-model="ruleForm.campDesc"
              placeholder="请选择企业在本平台主要业务方向"
            >
              <el-option
                v-for="item in campDescOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              ></el-option>
            </el-select>
            <img src="../../assets/imgs/complete.png" alt class="fitem_img" v-if="campDesc_falg" />
          </el-form-item>
          <el-form-item prop="checked" class="fitem">
            <el-checkbox-group
              v-model="ruleForm.checked"
              style="line-height: 12px; display: inline-block"
            >
              <el-checkbox label="1" name="checked">我已阅读并同意</el-checkbox>
            </el-checkbox-group>
            <span @click="showUserAgreement" class="agreement" style="line-height: 20px">《鉴权的一生用户协议》</span>
          </el-form-item>
          <div class="login_btn_view">
            <el-button
              class="login_btn login_btn_cur login_btn_one"
              type="primary"
              @click="submitForm('ruleForm')"
            >注册</el-button>
          </div>
          <div class="login_bottom login_bottom_cur">
            <div class="login_bottom_r" @click="goLogin">
              已有帐号
              <span>去登录</span>
            </div>
          </div>
        </div>
      </el-form>
    </div>
    <userAgreement ref="userAgreement" @checkedClick="checkedClick"></userAgreement>
  </div>
</template>

<script>
import userAgreement from './component/userAgreement'
import { getRegister, getSendCode } from '@/api/common'
export default {
  name: 'register',
  data () {
    let checkRealName = (rule, value, callback) => {
      if (!value) {
        this.realName_falg = false
        return callback(new Error('请输入您的用户姓名'))
      } else {
        if (!this.formRules_falg) {
          this.realName_falg = true
        }
        callback()
      }
    }
    let checkCompanyName = (rule, value, callback) => {
      if (!value) {
        this.companyName_falg = false
        return callback(new Error('请输入营业执照上的企业名称'))
      } else {
        if (!this.formRules_falg) {
          this.companyName_falg = true
        }
        callback()
      }
    }
    let reg = /^[A-Za-z0-9]{18}$/
    let checkcreditCode = (rule, value, callback) => {
      if (!reg.test(value)) {
        this.creditCode_falg = false
        callback(new Error('社会信用代码是18位大小写字母、数字组成！'))
      } else {
        if (!this.formRules_falg) {
          this.creditCode_falg = true
        }
        callback()
      }
    }
    let checkEmail = (rule, value, callback) => {
      const mailReg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/
      if (!value) {
        this.email_falg = false
        return callback(new Error('邮箱不能为空'))
      }
      setTimeout(() => {
        if (mailReg.test(value)) {
          if (!this.formRules_falg) {
            this.email_falg = true
          }
          callback()
        } else {
          this.email_falg = false
          callback(new Error('请输入正确的邮箱格式'))
        }
      }, 100)
    }
    let checkPhone = (rule, value, callback) => {
      // const phoneReg = /^1[3|4|5|7|8][0-9]{9}$/
      const phoneReg = /^1[0-9]{10}$/
      if (!value) {
        this.telephone_falg = false
        return callback(new Error('手机号不能为空'))
      }
      setTimeout(() => {
        // Number.isInteger是es6验证数字是否为整数的方法,但是我实际用的时候输入的数字总是识别成字符串
        // 所以我就在前面加了一个+实现隐式转换
        if (!Number.isInteger(+value)) {
          this.telephone_falg = false
          callback(new Error('请输入数字值'))
        } else {
          if (phoneReg.test(value)) {
            if (!this.formRules_falg) {
              this.telephone_falg = true
            }
            callback()
          } else {
            this.telephone_falg = false
            callback(new Error('手机号码格式不正确'))
          }
        }
      }, 100)
    }
    let checkcode = (rule, value, callback) => {
      const codeReg = /^[0-9]{6}$/
      if (!value) {
        this.code_falg = false
        return callback(new Error('请输入验证码'))
      }
      setTimeout(() => {
        // Number.isInteger是es6验证数字是否为整数的方法,但是我实际用的时候输入的数字总是识别成字符串
        // 所以我就在前面加了一个+实现隐式转换
        if (!Number.isInteger(+value)) {
          this.code_falg = false
          callback(new Error('请输入数字值'))
        } else {
          if (codeReg.test(value)) {
            if (!this.formRules_falg) {
              this.code_falg = true
            }
            callback()
          } else {
            this.code_falg = false
            callback(new Error('验证码只能输入6位数字'))
          }
        }
      }, 100)
    }
    let chackCampDesc = (rule, value, callback) => {
      if (!value) {
        this.campDesc_falg = false
        return callback(new Error('请选择企业在本平台主要业务方向'))
      } else {
        if (!this.formRules_falg) {
          this.campDesc_falg = true
        }
        callback()
      }
    }
    return {
      ruleForm: {
        needToastErr: 1,
        realName: '',
        companyName: '',
        creditCode: '',
        email: '',
        telephone: '',
        areaCode: '+86',
        code: '',
        campDesc: '',
        checked: []
      },
      realName_falg: false,
      companyName_falg: false,
      creditCode_falg: false,
      email_falg: false,
      telephone_falg: false,
      code_falg: false,
      campDesc_falg: false,
      rules: {
        realName: [{ required: true, validator: checkRealName, message: '请输入您的用户姓名', trigger: 'blur' }],
        companyName: [{ required: true, validator: checkCompanyName, message: '请输入营业执照上的企业名称', trigger: 'blur' }],
        creditCode: [
          { required: true, validator: checkcreditCode, message: '请输入18位社会信用代码', trigger: 'blur' }
        ],
        email: [
          { required: true, validator: checkEmail, trigger: 'blur' }
        ],
        telephone: [
          { required: true, validator: checkPhone, trigger: 'blur' }
        ],
        code: [{ required: true, validator: checkcode, message: '请输入6位验证码', trigger: 'blur' }],
        campDesc: [{ required: true, validator: chackCampDesc, message: '请选择企业在本平台主要业务方向', trigger: 'change' }],
        checked: [{ required: true, message: '请勾选用户协议', trigger: 'change' }]
      },
      telAreaCodeList: [{ 'valueCode': '+86', 'valueContent': '+86' }],
      campDescOptions: [{
        value: 'PURCHASE',
        label: '采购方'
      }, {
        value: 'SUPPLIER',
        label: '供应方'
      }],
      isDisabled: false, // 控制按钮是否可以点击（false:可以点击，true:不可点击
      isDisabledOne: false,
      content: '获取验证码', // 发送验证码按钮的初始显示文字
      timer: null,
      count: '',
      model: {},
      formRules: {
        realName: '',
        companyName: '',
        creditCode: '',
        email: '',
        telephone: '',
        code: '',
        campDesc: '',
        checked: ''
      },
      formRules_falg: false
    }
  },
  components: {
    userAgreement
  },
  mounted () {
  },
  methods: {
    // 发送验证码
    sendSms () {
      if (this.isDisabled) {
        return
      }
      // 校验手机号
      // eslint-disable-next-line eqeqeq
      if (!this.ruleForm.telephone) {
        this.$message.error('请输入手机号')
        return
      }
      if (!(/^1[34578]\d{9}$/.test(this.ruleForm.telephone))) {
        this.$message.error('请输入正确的手机号')
        return
      }
      getSendCode({ telephone: this.ruleForm.telephone }).then((result) => {
        if (Number(result.data.code) === 0) {
          this.isDisabled = false
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
          this.content = '重新获取'
          // 清空定时器!!!
          clearInterval(this.timer)
          this.timer = null
        }
      }, 1000)
    },
    submitForm (formName) { // 保存头信息
      let self = this
      self.formRules = {
        realName: '',
        companyName: '',
        creditCode: '',
        email: '',
        telephone: '',
        code: '',
        campDesc: ''
      }
      self.$refs[formName].validate((valid) => {
        if (valid) {
          getRegister(self.ruleForm).then((result) => {
            if (Number(result.data.code) === 0) {
              self.$message.success('注册成功')
              self.$router.replace({
                name: 'login',
                query: {}
              })
            } else if (result.data.code == '-2') {
              self.formRules = result.data.data || {}
              self.setformRules(self.formRules)
            }
          })
        } else {
          return false
        }
      })
    },
    setformRules (formRules) {
      this.realName_falg = !formRules.realName
      this.companyName_falg = !formRules.companyName
      this.creditCode_falg = !formRules.creditCode
      this.email_falg = !formRules.email
      this.telephone_falg = !formRules.telephone
      this.code_falg = !formRules.code
      this.campDesc_falg = !formRules.campDesc
      if (formRules.realName || formRules.companyName || formRules.creditCode || formRules.email || formRules.telephone || formRules.code || formRules.campDesc) {
        this.formRules_falg = true
      } else {
        this.formRules_falg = false
      }
    },
    goLogin () {
      this.$router.push({
        name: 'login',
        query: {}
      })
    },
    showUserAgreement () {
      this.$refs.userAgreement.showDialog()
    },
    checkedClick (ev) {
      this.ruleForm.checked = ev
    }
  }
}
</script>
<style lang="scss" scoped>
.wrap {
  width: 100%;
  min-height: 100vh;
  background: url("../../assets/imgs/loginbg.jpg") no-repeat;
  background-size: 100% 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  .login_logo {
    width: 200px;
    margin-bottom: 40px;
    margin-top: 80px;
  }
  .login_view {
    width: 472px;
    border-radius: 8px;
    background: #fff;
    margin-bottom: 100px;
    .login_title {
      font-size: 20px;
      color: #333333;
      margin-top: 24px;
      margin-bottom: 30px;
      font-weight: 700;
      width: 100%;
      text-align: center;
    }
    .login_item {
      width: 384px;
      height: auto;
      margin: 24px 40px 0 40px;
      ::v-deep.smsinput .el-input-group__append {
        border: none;
      }
      .smsinput >>> .el-input__inner {
        height: 32px;
        border-radius: 2px;
        border-top-right-radius: 0px;
        border-bottom-right-radius: 0px;
      }
      .smsinputBorder >>> .el-input__inner {
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
        border-radius: 0px;
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
        // border-top-left-radius: 0px;
        // border-bottom-left-radius: 0px;
        margin: 0px -10px 0 -10px;
      }
      .codecss {
        height: 32px;
        border-top-right-radius: 0px;
        border-bottom-right-radius: 0px;
      }
      ::v-deep .el-input-group__append,
      ::v-deep .el-input-group__prepend {
        border-radius: 2px !important;
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
      .agreement {
        color: #1ba1ba;
        cursor: pointer;
        display: inline-block;
      }
      .login_btn_view {
        width: 100%;
        text-align: center;
      }
      .login_btn {
        width: 384px;
        height: 40px;
        background: #f0f0f0;
        border-radius: 2px;
        border: 1px solid #d6d6d6;
        color: #999999;
        margin-top: 10px;
        font-weight: 800;
        font-size: 16px;
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
        margin-top: 16px;
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
    .fitem {
      position: relative;
      .fitem_img {
        width: 24px;
        height: 24px;
        position: absolute;
        top: 5px;
        right: -30px;
      }
    }
  }
}
</style>
