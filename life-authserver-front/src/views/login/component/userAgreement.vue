<template>
  <div class="wrap" v-if="dialogVisible">
    <div class="agreement_model" @click="this.dialogVisible = false"></div>
    <div class="agreement">
      <div class="agreement_view">
        <img
          src="../../../assets/imgs/xx.png"
          alt=""
          class="agreement_view_xx"
          @click="clickDialog"
        />
        <div class="agreement_view_title">服务协议条款</div>
        <div class="agreement_cont">
          <div class="agr_cont_ul">
            <div class="agr_cont_li">
              《鉴权的一生用户注册协议》（以下简称“本协议”）作为鉴权的一生网站（以下简称“本网站”）提供服务的依据，确定个人及企业用户在何种条件、以何种方式使用本网站及本网站的服务（具体载体包括但不限于网页、APP应用程序、微信公众号平台等）。在使用本网站及相关服务前，您应当注册成为本网站用户。进行用户注册时，请您认真阅读本协议，一旦完成注册即表示您已经知悉并了解本协议，接受本协议的条款约束，本协议当即具有合同效力。如您对本协议的任何条款表示异议，您应当选择不使用本网站。本协议包括基于本协议制定的各项规则，所有规则为本协议不可分割的一部分，与本协议具有同等效力。本网站有权随时变更本协议并在本页面发布。当您继续使用本网站及相关服务，则视为您完全接受协议的变更。
            </div>
            <div class="agr_cont_li agr_cont_li_title">一、服务描述及定义</div>
            <div class="agr_cont_li">
              本网站是基本的鉴权操作等。
            </div>
            <div class="agr_cont_li agr_cont_li_title">鉴权科技有限公司</div>
          </div>
        </div>
        <div class="agreement_btn_all">
          <div class="agreement_btn_l" @click="clickDialog">不同意</div>
          <div class="agreement_btn_r" @click="checkedClick">已阅读并同意此条款</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'userAgreement',
  data () {
    return {
      dialogVisible: false
    }
  },
  created () {
  },
  methods: {
    showDialog () {
      this.dialogVisible = true
    },
    clickDialog () {
      this.dialogVisible = false
      this.$emit('checkedClick', [])
    },
    checkedClick () {
      this.dialogVisible = false
      this.$emit('checkedClick', ['1'])
    }
  }
}
</script>

<style lang='scss' scoped>
.wrap {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 1;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  .agreement_model {
    position: fixed;
    left: 0;
    top: 0;
    background: rgba(0, 0, 0, 0.3);
    width: 100%;
    height: 100%;
    z-index: 0;
  }
  .agreement {
    width: 40%;
    min-height: 55vh;
    background: #ffffff;
    border-radius: 8px;
    display: flex;
    flex-direction: column;
    align-items: center;
    position: fixed;
    .agreement_view {
      width: 94.5%;
      position: relative;
      text-align: center;
      .agreement_view_xx {
        position: absolute;
        right: 0;
        top: 26px;
        width: 17px;
        height: 17px;
        background: #cccccc;
        cursor: pointer;
      }
      .agreement_view_title {
        width: 100%;
        text-align: center;
        font-size: 18px;
        line-height: 28px;
        font-weight: 800;
        color: #333333;
        margin: 20px 0 20px 0;
      }
      .agreement_cont {
        width: 100%;
        height: 400px;
        background: #ffffff;
        border: 1px solid #ebebeb;
        overflow: auto;
        overflow-y: scroll;
        .agr_cont_ul {
          width: 96%;
          margin-left: 16px;
          margin-top: 16px;
          .agr_cont_li {
            font-size: 14px;
            font-weight: 400;
            color: #333333;
            line-height: 22px;
            margin-bottom: 14px;
            text-indent: 24px;
            text-align: left;
          }
          .agr_cont_li_title {
            font-size: 16px;
            font-weight: 600;
            text-indent: 0px;
          }
        }
      }
      /*滚动条样式*/
      .agreement_cont::-webkit-scrollbar {
        /*滚动条整体样式*/
        width: 12px; /*高宽分别对应横竖滚动条的尺寸*/
        height: 5px;
        cursor: pointer;
      }
      .agreement_cont::-webkit-scrollbar-thumb {
        /*滚动条里面小方块*/
        border-radius: 7px;
        -webkit-box-shadow: inset 0 0 5px rgba(66, 36, 36, 0.2);
        background: rgba(0, 0, 0, 0.1);
        cursor: pointer;
      }
      .agreement_cont::-webkit-scrollbar-track {
        /*滚动条里面轨道*/
        /* -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2); */
        background-color: transparent;
        border-radius: 0;
        /* background: rgba(0, 0, 0, 0.1); */
      }
      .agreement_btn_all {
        width: 100%;
        display: flex;
        flex-direction: row;
        justify-content: center;
        align-items: center;
        margin: 16px 0 24px 0;

        .agreement_btn_l {
          width: 118px;
          height: 38px;
          display: inline-block;
          line-height: 40px;
          text-align: center;
          background: #FFFFFF;
          border-radius: 2px;
          border: 1px solid #D6D6D6;
          font-size: 16px;
          font-weight: 500;
          color: #333333;
          cursor: pointer;
        }
        .agreement_btn_r {
          margin-left: 16px;
          width: 200px;
          height: 40px;
          display: inline-block;
          line-height: 40px;
          text-align: center;
          background: #1ba1ba;
          border-radius: 2px;
          font-size: 16px;
          font-weight: 500;
          color: #ffffff;
          cursor: pointer;
        }
      }
    }
  }
}
</style>
