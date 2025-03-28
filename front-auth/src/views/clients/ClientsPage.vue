<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <el-form :inline="true" :model="searchParams">
          <el-form-item label="searchKey">
            <el-input v-model="searchParams.searchKey" placeholder="searchKey" clearable/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onQuery">Query</el-button>
            <el-button type="warning" @click="onReset">Reset</el-button>
          </el-form-item>
        </el-form>
      </el-header>
      <el-main>

        <el-table :data="pageData" style="width: 100%">
          <el-table-column fixed prop="domainName" label="客户端域名" width="150"/>
          <el-table-column prop="clientId" label="CLIENT_ID" width="120"/>
          <el-table-column prop="clientSecret" label="CLIENT_SECRET" width="120"/>
          <el-table-column prop="grantTypes" label="授权类型" width="120"/>
          <el-table-column prop="accessTokenTimeout" label="访问授权超时时间" width="600"/>
          <el-table-column prop="refreshTokenTimeout" label="刷新授权超时时间" width="120"/>
          <el-table-column prop="tenantId" label="租户ID" width="120"/>
          <el-table-column fixed="right" label="操作" min-width="120">
            <template #default>
              <el-button link type="primary" size="small" @click="handleClick">
                Detail
              </el-button>
              <el-button link type="primary" size="small">Edit</el-button>
            </template>
          </el-table-column>
        </el-table>

      </el-main>
      <el-footer>Footer</el-footer>
    </el-container>
  </div>
</template>

<script>

import {clientPage} from "@/api/api-clients";

export default {
  name: "ClientsPage",
  created() {
    this.initData();
  },
  data() {
    return {
      searchParams: {
        searchKey: "",
        pageNo: 1,
        pageSize: 5,
      },
      pageData: [

      ],
    }
  },
  methods: {
    initData() {
      clientPage(this.searchParams).then(res => {
        console.log(res);
        this.pageData = res.data.list;
      });
    },
  }
}
</script>

<style scoped lang="scss">

</style>