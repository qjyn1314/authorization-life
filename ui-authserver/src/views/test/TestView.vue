<template>
  <el-container>
    <el-header>
      <!--   头部信息   -->
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="grid-content bg-purple">测试命运迷雾</div>
        </el-col>
        <el-col :span="8" :offset="8">
          <div class="grid-content bg-purple">为了开发进行测试></div>
        </el-col>
      </el-row>
    </el-header>
    <el-container>
      <el-aside width="200px">
        <!--   左侧信息-将查询菜单 使用ul-li     -->
        我是左侧栅格布局
      </el-aside>
      <el-main>
        我是为了路由的页面显示
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
// import {setValueExpires,setValue} from '@/utils/cookie-util'

export default {
  name: 'DashboardView',
  components: {},
  created() {
    //VUE_APP_COOKIE_HOST
    // setValue("test", "test");
    var str = "10017664,10003738,10034346,10008839,10008843,10008845,10008830,10008847,10008841,10008834,10008836,10096187";
    let mdmCodes = ["10036105", "10017664", "10017663", "10017850", "10094238", "10000291", "10100453", "10034568", "10017656"];
    let mdmCode = "";
    for (let i = 0; i < mdmCodes.length; i++) {
      if (str.indexOf(mdmCodes[i]) > -1) {
        mdmCode = mdmCodes[i]
        break;
      }
    }
    if (mdmCode) {
      console.log(mdmCode);
    }

  },
  mounted() {
  },
  beforeDestroy() {
  }
}
</script>
<style scoped>

</style>
