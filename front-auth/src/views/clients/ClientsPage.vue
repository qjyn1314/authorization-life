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
                                :title="'域名：' + urlData.domainName + '；CLIENT_KEY：' + urlData.clientId">
                <div>
                  <h3><span>授权模式：</span>{{ urlData.grantTypes }}</h3>
                  <h3><span>授权域：</span>{{ urlData.scopes }}</h3>
                  <h3><span>回调URI：</span>{{ urlData.redirectUri }}</h3>
                  <span>授权路径：</span>
                  <h4>{{ urlData.redirectUrl }}</h4>
                  <el-button link type="primary" @click="copySource(urlData.redirectUrl)" :icon="DocumentCopy">
                    复制到剪切板
                  </el-button>
                  |
                  <el-link type="warning" underline :href="urlData.redirectUrl" target="_blank" :icon="Promotion">
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
            <el-button type="primary" :icon="DocumentAdd" @click="createClient">新增</el-button>
          </el-col>
        </el-row>
        <!-- table表格 -->
        <el-table :data="pageInfo.list" border style="width: 100%" max-height="800">
          <el-table-column fixed prop="clientId" label="CLIENT_KEY" width="230"/>
          <el-table-column prop="clientSecretBak" label="CLIENT_SECRET" width="230">
            <template #default="scope">
              <el-popover effect="light" trigger="hover" placement="top" width="auto">
                <template #default>
                  <div>真实密钥: {{ scope.row.clientSecretBak }}</div>
                  <div>加密密钥: {{ scope.row.clientSecret }}</div>
                </template>
                <template #reference>
                  <el-tag>{{ scope.row.clientSecretBak }}</el-tag>
                </template>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column prop="domainName" label="客户端域名" width="230"/>
          <el-table-column prop="grantTypes" show-overflow-tooltip label="授权模式" width="160"/>
          <el-table-column prop="scopes" show-overflow-tooltip label="授权域" width="160"/>
          <el-table-column prop="redirectUri" label="回调URI" width="280"/>
          <el-table-column prop="accessTokenTimeout" label="访问授权超时时间(秒)" width="100"/>
          <el-table-column prop="refreshTokenTimeout" label="刷新授权超时时间(秒)" width="100"/>
          <el-table-column prop="tenantId" label="租户ID" width="120"/>
          <el-table-column fixed="right" label="操作" min-width="150">
            <template #default="{row}">
              <el-button link type="primary" size="large" @click="authorizationURL(row)" :icon="Collection">
                生成授权路径
              </el-button>
              <el-button link type="success" size="large" :icon="Reading" @click="updateClient(row.clientId)">EDIT
              </el-button>
              <el-button link type="primary" size="large" :icon="Notebook" @click="detailClient(row.clientId)">
                DETAIL
              </el-button>

              <el-popconfirm
                  class="box-item"
                  title="确定删除此客户端授权信息吗？"
                  placement="top-start"
                  confirm-button-type="danger"
                  width="250"
                  @confirm="removeClient(row.clientId)"
              >
                <template #reference>
                  <el-button link type="danger" size="large" :icon="DocumentDelete">
                    DELETE
                  </el-button>
                </template>
              </el-popconfirm>
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

    <!--  添加/编辑client信息表单的抽屉  -->
    <el-drawer v-model="drawer.visible" :show-close="false"
               :direction="drawer.direction" size="50%" @close-auto-focus="drawerClose()">
      <template #header="{ close, titleId, titleClass }">
        <p :id="titleId" :class="titleClass">{{ drawer.header }}</p>
        <el-button type="danger" @click="close" :icon="CircleCloseFilled">
          关闭
        </el-button>
      </template>
      <!--   添加或编辑的表单   -->
      <el-form :model="clientInfo" size="large" label-position="right" label-width="auto"
               style="max-width: 1000px;max-height: 1000px;">
        <el-form-item label="客户端域名" :rules="[
        { required: true, message: '客户端域名必填项' },
      ]">
          <el-input v-model="clientInfo.domainName"/>
        </el-form-item>
        <el-form-item label="CLIENT_KEY" :rules="[
        { required: true, message: 'CLIENT_KEY必填项' },
      ]">
          <el-input v-model="clientInfo.clientId"/>
        </el-form-item>
        <el-form-item label="CLIENT_SECRET" :rules="[
        { required: true, message: 'CLIENT_SECRET必填项' },
      ]">
          <el-input v-model="clientInfo.clientSecretBak"/>
        </el-form-item>
        <el-form-item label="回调URI" :rules="[
        { required: true, message: '回调URI必填项' },
      ]">
          <el-select v-model="redirectUrlFooter.redirectUrl"
                     clearable multiple
                     placeholder="添加并选择回调URI">
            <el-option
                v-for="item in redirectUrlFooter.redirectUrlOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
            <template #footer>
              <el-button v-if="!redirectUrlFooter.isUrlAdding" text bg size="small" @click="onAddUrlOption('url')">
                添加一个选项
              </el-button>
              <template v-else>
                <el-input
                    v-model="redirectUrlFooter.optionName"
                    clearable
                    class="option-input"
                    placeholder="请输入一个选项"
                    size="large"
                />
                <el-divider>
                  <el-icon>
                    <star-filled/>
                  </el-icon>
                </el-divider>
                <el-button type="primary" size="small" @click="onAddUrlConfirm('url')">
                  添加选项
                </el-button>
                <el-button size="small" @click="onAddUrlClear">关闭输入框</el-button>
              </template>
            </template>
          </el-select>
        </el-form-item>
        <el-form-item label="授权模式" :rules="[
        { required: true, message: '授权模式必填项' },
      ]">
          <el-select v-model="redirectUrlFooter.grantType"
                     clearable multiple
                     placeholder="选择授权模式">
            <el-option
                v-for="item in redirectUrlFooter.grantTypeOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="授权域" :rules="[
        { required: true, message: '授权域必填项' },
      ]">
          <el-select v-model="redirectUrlFooter.scope"
                     clearable multiple
                     placeholder="添加并选择授权域">
            <el-option
                v-for="item in redirectUrlFooter.scopeOption"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
            <template #footer>
              <el-button v-if="!redirectUrlFooter.isScopeAdding" text bg size="small" @click="onAddUrlOption('scope')">
                添加一个选项
              </el-button>
              <template v-else>
                <el-input
                    v-model="redirectUrlFooter.optionName"
                    clearable
                    class="option-input"
                    placeholder="请输入一个选项"
                    size="large"
                />
                <el-divider>
                  <el-icon>
                    <star-filled/>
                  </el-icon>
                </el-divider>
                <el-button type="primary" size="small" @click="onAddUrlConfirm('scope')">
                  添加选项
                </el-button>
                <el-button size="small" @click="onAddUrlClear">关闭输入框</el-button>
              </template>
            </template>
          </el-select>
        </el-form-item>
        <p style="text-align: left;color: royalblue;">
          30分钟：1800 秒；1小时：3600 秒；24小时：86 400 秒；
        </p>
        <el-form-item label="访问授权超时时间(秒)"
                      :rules="[
        { required: true, message: '时间(秒)必填项' },
        { type: 'number', message: '时间(秒)必须是数字' },
      ]">
          <el-input-number v-model="clientInfo.accessTokenTimeout"/>

        </el-form-item>
        <el-form-item label="刷新授权超时时间(秒)"
                      :rules="[
        { required: true, message: '时间(秒)必填项' },
        { type: 'number', message: '时间(秒)必须是数字' },
      ]">
          <el-input-number v-model="clientInfo.refreshTokenTimeout"/>
        </el-form-item>
        <el-form-item label="附加信息">
          <el-input type="textarea" v-model="clientInfo.additionalInformation"/>
        </el-form-item>
        <el-form-item v-show="drawer.submit">
          <el-button type="primary" @click="onSubmit">Create</el-button>
          <el-button @click="drawer.visible = false">Cancel</el-button>
        </el-form-item>
      </el-form>
    </el-drawer>
  </div>
</template>

<script>

import {clientPage, delClient, genAuthorizationUrl, getClient, getSelectVal, saveClient} from "@/api/api-clients";
import {
  CircleCloseFilled,
  Collection,
  DocumentAdd,
  DocumentCopy,
  DocumentDelete,
  Notebook,
  Promotion,
  Reading,
  StarFilled,
  Warning
} from "@element-plus/icons-vue";
import {AUTH_SERVER} from "@/utils/global-util";
import {prompt} from "@/utils/msg-util";
import {useClipboard} from "@vueuse/core";

export default {
  name: "ClientsPage",
  components: {StarFilled},
  setup() {
    // 复制到剪切板功能: 在http路径下复制失败, 需要添加 legacy: true 参数即可.
    const {copied, copy} = useClipboard({legacy: true});
    return {copy, copied}
  },
  created() {
    this.getTableData();
    this.initGrantTypes();
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
      drawer: {
        visible: false,
        header: '',
        direction: 'rtl',
        submit: true
      },
      clientInfo: {
        clientId: "",
        clientSecretBak: "",
        domainName: "",
        redirectUri: "",
        grantTypes: "",
        scopes: "",
        accessTokenTimeout: 0,
        refreshTokenTimeout: 0,
      },
      clearClientInfo: {
        clientId: "",
        clientSecretBak: "",
        domainName: "",
        redirectUri: "",
        grantTypes: "",
        scopes: "",
        accessTokenTimeout: 0,
        refreshTokenTimeout: 0,
      },
      redirectUrlFooter: {
        isUrlAdding: false,
        isGrantAdding: false,
        isScopeAdding: false,
        optionName: "",
        redirectUrl: [],
        redirectUrlOption: [],
        grantType: [],
        grantTypeOption: [],
        scope: [],
        scopeOption: [],
      }
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
    initGrantTypes() {
      getSelectVal().then(res => {
        res.data['grantType'].forEach(item => {
          this.redirectUrlFooter.grantTypeOption.push({label: item, value: item});
        });
        res.data['scope'].forEach(item => {
          this.redirectUrlFooter.scopeOption.push({label: item, value: item});
        });
      })
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
    copySource(source) {
      this.copy(source)
      this.copied ? prompt.success("复制成功") : prompt.error("复制失败")
    },
    createClient() {
      this.drawer.visible = true;
      this.drawer.header = '手工添加客户端授权信息';
      this.drawer.direction = 'rtl';
      this.drawer.submit = true;
    },
    updateClient(clientId) {
      this.detail(clientId)
      this.drawer.visible = true;
      this.drawer.header = '编辑客户端授权信息';
      this.drawer.direction = 'ltr';
      this.drawer.submit = true;
    },
    detailClient(clientId) {
      this.detail(clientId)
      this.drawer.visible = true;
      this.drawer.header = '客户端授权信息';
      this.drawer.direction = 'ttb';
      this.drawer.submit = false;
    },
    drawerClose() {
      this.clientInfo = this.clearClientInfo;
      this.drawer.visible = false;
      this.redirectUrlFooter.grantType = [];
      this.redirectUrlFooter.scope = [];
      this.redirectUrlFooter.redirectUrl = [];
    },
    detail(clientId) {
      getClient({clientId: clientId}).then((res) => {
        if (res.code === '-1') {
          prompt.error(res.msg)
        } else {
          this.initClientDetail(res.data);
        }
      })
    },
    initClientDetail(clientInfo) {
      console.log(clientInfo);
      this.clientInfo = clientInfo;

      let redirectUris = clientInfo.redirectUri.split(",");
      this.redirectUrlFooter.redirectUrl = redirectUris;
      redirectUris.forEach((item, index) => {
        this.redirectUrlFooter.redirectUrlOption.push({label: item, value: item});
      })

      this.redirectUrlFooter.grantType = clientInfo.grantTypes.split(",");

      let scopesList = clientInfo.scopes.split(",");
      this.redirectUrlFooter.scope = scopesList;
      scopesList.forEach((item, index) => {
        this.redirectUrlFooter.scopeOption.push({label: item, value: item});
      })
      this.redirectUrlFooter.scopeOption = Array.from(new Set(this.redirectUrlFooter.scopeOption))

    },
    removeClient(clientId) {
      delClient({clientId: clientId}).then((res) => {
        console.log(res)
        if (res.code === '-1') {
          prompt.error(res.msg)
        } else {
          prompt.success("删除成功.")
          this.getTableData();
        }
      })
    },
    onAddUrlOption(type) {
      this.redirectUrlFooter.optionName = '';
      if (type === 'url') {
        this.redirectUrlFooter.isUrlAdding = true;
      } else if (type === 'scope') {
        this.redirectUrlFooter.isScopeAdding = true;
      }
    },
    onAddUrlConfirm(type) {
      if (!this.redirectUrlFooter.optionName) {
        return;
      }
      if (type === 'url') {
        let filterArr = this.redirectUrlFooter.redirectUrlOption.filter(item => {
          return item.label === this.redirectUrlFooter.optionName;
        })
        if (filterArr.length > 0) {
          prompt.warning("请勿添加重复项.")
          return;
        }
        this.redirectUrlFooter.redirectUrlOption.push({
          label: this.redirectUrlFooter.optionName,
          value: this.redirectUrlFooter.optionName
        });
      } else if (type === 'scope') {
        let filterArr = this.redirectUrlFooter.scopeOption.filter(item => {
          return item.label === this.redirectUrlFooter.optionName;
        })
        if (filterArr.length > 0) {
          prompt.warning("请勿添加重复项.")
          return;
        }
        this.redirectUrlFooter.scopeOption.push({
          label: this.redirectUrlFooter.optionName,
          value: this.redirectUrlFooter.optionName
        });
      }
      this.redirectUrlFooter.optionName = '';
    },
    onAddUrlClear() {
      this.redirectUrlFooter.optionName = ''
      this.redirectUrlFooter.isUrlAdding = false
      this.redirectUrlFooter.isScopeAdding = false
    },
    onSubmit() {
      this.clientInfo.redirectUri = this.redirectUrlFooter.redirectUrl.join(",");
      this.clientInfo.grantTypes = this.redirectUrlFooter.grantType.join(",");
      this.clientInfo.scopes = this.redirectUrlFooter.scope.join(",");
      console.log(this.clientInfo);

      saveClient(this.clientInfo).then((res) => {
        if (res.code === '-1') {
          prompt.error(res.msg)
        } else {
          prompt.success("保存成功.")
          this.getTableData();
          this.drawerClose();
        }
      });

    }
  },
  computed: {
    Warning() {
      return Warning
    },
    CircleCloseFilled() {
      return CircleCloseFilled
    },
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