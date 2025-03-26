<template>
  <el-container>
    <el-header
        style="line-height: 100px; height: 100px;font-size: 20px;font-weight: bold;font-family: Microsoft YaHei;">
      <!--   头部信息   -->
      <el-row :gutter="20">
        <el-col :span="8">
          <div>拨开命运迷雾</div>
        </el-col>
        <el-col :span="8" :offset="8">
          <div>
            <el-dropdown>
              <i class="el-icon-user-solid" style="margin-right: 15px;font-size: 20px; line-height: 60px; ">
                {{ currentUser.userEmail }} </i>
              <el-dropdown-menu>
                <el-dropdown-item>个人信息</el-dropdown-item>
                <el-dropdown-item>退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </el-col>
      </el-row>
    </el-header>
    <el-container>
      <el-aside width="250px" style="background-color: rgb(238, 241, 246)">
        <el-menu :default-openeds="['1','2']">
          <el-submenu v-for="m in menus" :key="m.menuId" :index="m.menuId">
            <template slot="title"><i :class="m.iconClass"></i>{{ m.name }}</template>
            <el-menu-item v-for="child in m.children" :index="child.menuId" :key="child.menuId">
              <router-link :to="`${child.path}`"> {{ child.name }}</router-link>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import {getCurrentUser} from "@/api/login";
import {v4 as uuidv4} from 'uuid';

export default {
  name: 'DashboardView',
  components: {},
  data() {
    return {
      currentUser: {},
      menus: [
        {
          parentId: "0",
          menuId: "1",
          path: "",
          name: "系统设置",
          iconClass: "el-icon-s-tools",
          children: [
            {
              parentId: "1",
              menuId: uuidv4(),
              path: "/lov",
              name: "字典管理",
            },
            {
              parentId: "1",
              menuId: uuidv4(),
              path: "/temp",
              name: "模板管理",
            },
          ]
        },
        {
          parentId: "0",
          menuId: "2",
          path: "",
          name: "拨开迷雾",
          iconClass: "el-icon-lightning",
          children: [
            {
              parentId: "2",
              menuId: uuidv4(),
              path: "/lov",
              name: "双色球最新数据",
            },
            {
              parentId: "2",
              menuId: uuidv4(),
              path: "/temp",
              name: "预测双色球",
            },
          ]
        },
      ]
    }
  },
  created() {
    //将获取当前登录用户信息
    getCurrentUser().then((res) => {
      this.currentUser = res.data;
    })
  },
  mounted() {
  },
  beforeDestroy() {
  }
}
</script>
<style scoped>

</style>
