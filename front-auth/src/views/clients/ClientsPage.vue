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
        <!--描述列表-->
        <el-descriptions title="客户端授权路径">
          <!--第一个描述项-->
          <el-descriptions-item>
            <!--折叠面板-->
            <el-collapse v-model="authorizationUrls">
              <el-collapse-item title="路径001" name="1">
                <div>
                  Consistent with real life: in line with the process and logic of real
                  life, and comply with languages and habits that the users are used to;
                </div>
                <div>
                  <el-button type="primary">复制路径</el-button>
                  <el-button type="primary">点击开始授权</el-button>
                </div>
              </el-collapse-item>
            </el-collapse>
          </el-descriptions-item>
        </el-descriptions>
        <!-- table表格 -->
        <el-table :data="pageData" style="width: 100%">
          <el-table-column fixed prop="domainName" label="客户端域名" width="150"/>
          <el-table-column prop="clientId" label="CLIENT_ID" width="120"/>
          <el-table-column prop="clientSecret" label="CLIENT_SECRET" width="120"/>
          <el-table-column prop="grantTypes" label="授权类型" width="120"/>
          <el-table-column prop="accessTokenTimeout" label="访问授权超时时间" width="600"/>
          <el-table-column prop="refreshTokenTimeout" label="刷新授权超时时间" width="120"/>
          <el-table-column prop="tenantId" label="租户ID" width="120"/>
          <el-table-column fixed="right" label="操作" min-width="120">
            <template #default="{row}">
              <el-button link type="primary" size="small">
                Detail
              </el-button>
              <el-button link type="primary" size="small">Edit</el-button>
              <el-button link type="danger" size="small" @click="authorizationURL(row)">生成授权路径</el-button>
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
      pageData: [],
      authorizationUrls: []
    }
  },
  methods: {
    initData() {
      clientPage(this.searchParams).then(res => {
        console.log(res);
        this.pageData = res.data.list;
      });
    },
    authorizationURL(row) {
      console.log('row', row.clientId);

    },
    onQuery() {

    },
    onReset() {

    }
  }
}
</script>

<style scoped lang="scss">

</style>