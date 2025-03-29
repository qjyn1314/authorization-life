<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <el-form :inline="true" :model="tableSearch">
          <el-form-item label="关键词">
            <el-input v-model="tableSearch.searchKey" placeholder="关键词" clearable/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="onQuery">查询</el-button>
            <el-button type="warning" @click="onReset">重置</el-button>
          </el-form-item>
        </el-form>
      </el-header>
      <el-main>
        <!--描述列表-->
        <el-descriptions title="客户端授权路径">
          <!--第一个描述项-->
          <el-descriptions-item>
            <!--折叠面板-->
            <el-collapse>
              <el-collapse-item v-for="(urlData,index) in authorizationUrls" :key="index"
                                :title="'域名：' + urlData.domainName + '；CLIENT_ID：' + urlData.clientId">
                <div>
                  <h3><span>授权范围：</span>{{ urlData.scopes }}</h3>
                  <span>授权路径：</span>
                  <h4>{{ urlData.redirectUri }}</h4>
                  <el-button link type="primary" @click="copyUrl(urlData.redirectUri)" :icon="DocumentCopy">
                    复制到剪切板
                  </el-button>
                  |
                  <el-link type="warning" underline :href="urlData.redirectUri" target="_blank" :icon="Promotion">
                    请求授权
                  </el-link>
                </div>
              </el-collapse-item>
            </el-collapse>
          </el-descriptions-item>
        </el-descriptions>
        <!--    分割线    -->
        <el-row :gutter="20">
          <el-col :span="1">
            <el-button type="primary" :icon="DocumentAdd">新增</el-button>
          </el-col>
        </el-row>
        <!-- table表格 -->
        <el-table :data="pageInfo.list" border style="width: 100%" max-height="800">
          <el-table-column fixed prop="clientId" label="CLIENT_ID" width="230"/>
          <el-table-column prop="clientSecretBak" label="CLIENT_SECRET" width="230">
            <template #default="scope">
              <el-popover effect="light" trigger="hover" placement="top" width="auto">
                <template #default>
                  <div>真实密钥: {{ scope.row.clientSecretBak }}</div>
                  <div>验证密钥: {{ scope.row.clientSecret }}</div>
                </template>
                <template #reference>
                  <el-tag>{{ scope.row.clientSecretBak }}</el-tag>
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column prop="domainName" label="客户端域名" width="230"/>
          <el-table-column prop="redirectUri" label="回调URI" width="280"/>
          <el-table-column prop="grantTypes" show-overflow-tooltip label="授权类型" width="160"/>
          <el-table-column prop="accessTokenTimeout" label="访问授权超时时间(毫秒)" width="100"/>
          <el-table-column prop="refreshTokenTimeout" label="刷新授权超时时间(毫秒)" width="100"/>
          <el-table-column prop="tenantId" label="租户ID" width="120"/>
          <el-table-column fixed="right" label="操作" min-width="150">
            <template #default="{row}">
              <el-button link type="primary" size="large" @click="authorizationURL(row)" :icon="Collection">
                生成授权路径
              </el-button>
              <el-button link type="success" size="large" :icon="Reading">EDIT</el-button>
              <el-button link type="primary" size="large" :icon="Notebook">
                DETAIL
              </el-button>
              <el-button link type="danger" size="large" :icon="DocumentDelete">
                DELETE
              </el-button>
            </template>
          </el-table-column>
        </el-table>

      </el-main>
      <el-footer>
        <el-pagination v-show="pageInfo.total > 0" background layout="total, sizes, prev, pager, next, jumper "
                       :total="pageInfo.total"
                       v-model:current-page="tableSearch.pageNum" v-model:page-size="tableSearch.pageSize"
                       @change="getTableData"/>
      </el-footer>
    </el-container>
  </div>
</template>

<script>

import {clientPage, genAuthorizationUrl} from "@/api/api-clients";
import {
  Collection,
  DocumentAdd,
  DocumentCopy,
  DocumentDelete,
  Notebook,
  Promotion,
  Reading
} from "@element-plus/icons-vue";
import {AUTH_SERVER} from "@/utils/global-util";
import {prompt} from "@/utils/msg-util";
import {useClipboard} from "@vueuse/core";

export default {
  name: "ClientsPage",
  setup() {
    // 复制到剪切板功能: 在http路径下复制失败, 需要添加 legacy: true 参数即可.
    const {copied, copy} = useClipboard({legacy: true});
    return {copy, copied}
  },
  created() {
    this.getTableData();
  },
  data() {
    return {
      tableSearch: {
        searchKey: "",
        pageNum: 1,
        pageSize: 3,
      },
      defTableSearch: {
        searchKey: "",
        pageNum: 1,
        pageSize: 3,
      },
      pageInfo: {total: 0},
      authorizationUrls: [],
    }
  },
  methods: {
    getTableData() {
      clientPage(this.tableSearch).then(res => {
        if (res.data === null) {
          return;
        }
        this.pageInfo = res.data;
      });
    },
    authorizationURL(row) {
      this.authorizationUrls = [];
      let hostOrigin = window.location.origin + process.env.VUE_APP_BASE_API + AUTH_SERVER;
      console.log(hostOrigin);
      genAuthorizationUrl({clientId: row.clientId, hostOrigin: hostOrigin}).then(res => {
        if (res.data == null) {
          return;
        }
        this.authorizationUrls = res.data;
      })
    },
    onQuery() {
      this.getTableData()
    },
    onReset() {
      this.tableSearch = this.defTableSearch;
      this.getTableData()
    },
    copyUrl(url) {
      this.copy(url)
      let copied = this.copied;
      copied ? prompt.success("复制成功") : prompt.error("复制失败")
    },
    requestUrl(url) {
      //在新标签中打开url路径
      console.log(url)

    }
  },
  computed: {
    DocumentCopy() {
      return DocumentCopy
    },
    Collection() {
      return Collection
    },
    Promotion() {
      return Promotion
    },
    DocumentDelete() {
      return DocumentDelete
    },
    Notebook() {
      return Notebook
    },
    Reading() {
      return Reading
    },
    DocumentAdd() {
      return DocumentAdd
    }
  }
}
</script>

<style>
.el-row {
  margin-bottom: 12px;
}

.el-row:last-child {
  margin-bottom: 0;
}

.el-col {
  border-radius: 4px;
}
</style>