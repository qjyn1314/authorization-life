<template>
  <el-container>
    <el-row>
      <el-col :span="2">
        <h3 @click="goBack">
          <el-button type="text" icon="el-icon-back" circle>返回</el-button>
        </h3>
      </el-col>
      <el-col :span="22">
        <h2>名称:【{{ this.initLovData.lovName || '' }}】编码:『{{ this.initLovData.lovCode || '' }}』的明细</h2>
      </el-col>
    </el-row>
    <el-header>
      <el-row :gutter="10">
        <el-col :span="2">
          <el-button round icon="el-icon-plus" @click="addData">新增</el-button>
        </el-col>
        <el-col :span="1">
          <el-button type="success" plain round icon="el-icon-download">导出</el-button>
        </el-col>
      </el-row>
    </el-header>
    <el-main>
      <el-table
          :data="tableData"
          border
          style="width: 100%">
        <el-table-column
            fixed
            prop="valueCode"
            label="值代码">
        </el-table-column>
        <el-table-column
            prop="valueContent"
            label="值内容">
        </el-table-column>
        <el-table-column
            prop="description"
            label="描述"
            width="200">
        </el-table-column>
        <el-table-column
            prop="valueOrder"
            label="排序号"
            width="90">
        </el-table-column>
        <el-table-column
            label="是否启用"
            width="90">
          <template slot-scope="{row}">
            <el-tag>
              {{ conversionEnabled(row.enabledFlag) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            width="200">
          <template slot-scope="{row}">
            <el-row>
              <el-col :span="6">
                <el-button type="info" icon="el-icon-info" circle size="medium" @click="detail(row)"></el-button>
              </el-col>
              <el-col :span="6">
                <el-button type="primary" icon="el-icon-edit" circle size="medium"
                           @click="edit(row)"></el-button>
              </el-col>
              <el-col :span="6">
                <el-popconfirm
                    confirm-button-text='确定'
                    cancel-button-text='暂不删除'
                    icon="el-icon-info"
                    icon-color="red"
                    title="确定删除吗？"
                    @confirm="del(row)"
                >
                  <el-button type="danger" icon="el-icon-delete" circle size="medium" slot="reference"></el-button>
                </el-popconfirm>
              </el-col>
            </el-row>
          </template>
        </el-table-column>
      </el-table>
    </el-main>

    <el-footer>
      <pagination v-show="pageInfo.total>0" :total="pageInfo.total" :page.sync="tableSearch.pageNum"
                  :limit.sync="tableSearch.pageSize" @pagination="getPageInfo"/>
    </el-footer>

    <el-drawer
        :title="drawerInfo.title"
        :visible.sync="drawerInfo.viewFlag"
        :direction="drawerInfo.direction"
        :size="'65%'"
        @close="closeDrawer"
    >

      <el-form ref="form" :model="addFormData" label-position="right" size="medium" label-width="100px"
               label-suffix=":">
        <el-row align="middle">
          <el-col :span="20">
            <el-form-item label="值代码">
              <el-input v-model="addFormData.valueCode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="值内容">
              <el-input v-model="addFormData.valueContent"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-col :span="6">
          <el-form-item label="排序号">
            <el-input-number v-model="addFormData.valueOrder" :min="1" :max="999999" :step="3"></el-input-number>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item label="值集描述">
            <el-input type="textarea" :rows="5" v-model="addFormData.description"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="是否启用">
            <el-switch v-model="addFormData.enabledFlag">是否启用</el-switch>
          </el-form-item>
        </el-col>
        <el-col :span="20">
          <el-form-item :hidden="drawerInfo.hiddenFlag">
            <el-button type="primary" plain @click="onSaveData">保存</el-button>
            <el-button plain @click="onCancelSave">取消</el-button>
          </el-form-item>
        </el-col>
      </el-form>

    </el-drawer>

  </el-container>
</template>

<script>
import Pagination from '@/components/Pagination' // secondary package based on el-pagination
import {deleteLovValue, oneLov, oneLovValue, pageLovValue, saveLovValue, updateLovValue} from "@/api/lov";
import {Message} from "element-ui";

export default {
  name: 'LovValuePage',
  components: {Pagination},
  filters: {
    enabledFlagFilter(status) {
      let enabledFlagMap = {
        true: '已启用',
        false: '未启用',
      }
      return enabledFlagMap[status] || 'default'; // 默认为default
    }
  },
  data() {
    return {
      tableSearch: {
        pageNum: 1,
        pageSize: 10,
        lovId: '',
      },
      pageInfo: {total: 0},
      tableData: [],
      drawerInfo: {
        title: '',
        viewFlag: false,
        direction: 'ltr',
        hiddenFlag: false
      },
      addFormData: {},
      initAddFormData: {},
      initLovData: {}
    }
  },
  created() {
    this.initLovValuePahe();
  },
  methods: {
    initLovValuePahe() {
      if (!this.$route.query.lovId) {
        Message.error("未找到值集主信息")
        this.initLovData = {};
        return
      }
      this.getLovData({lovId: this.$route.query.lovId})
    },
    getLovData(params) {
      if (params === null) {
        return
      }
      oneLov(params).then(res => {
        if (!res.data.lovName || !res.data.lovCode) {
          Message.error("未找到值集主信息")
        }
        this.initLovData = res.data;
        if (this.initLovData.lovId) {
          this.getPageInfo();
        }
      })
    },
    getPageInfo() {
      this.tableSearch.lovId = this.initLovData.lovId
      pageLovValue(this.tableSearch).then(res => {
        if (res.data === null) {
          return;
        }
        this.tableData = res.data.list;
        this.pageInfo = res.data;
      })
    },
    getOneData(params) {
      if (params === null) {
        return
      }
      oneLovValue(params).then(res => {
        if (res.data === null) {
          return;
        }
        this.addFormData = res.data;
      })
    },
    addData() {
      this.drawerInfo.title = '添加值集明细';
      this.drawerInfo.viewFlag = true;
      this.drawerInfo.hiddenFlag = false;
      this.drawerInfo.direction = 'ltr';
    },
    detail(value) {
      let params = {lovValueId: value.lovValueId};
      this.getOneData(params)
      this.drawerInfo.title = '值集明细详情';
      this.drawerInfo.viewFlag = true;
      this.drawerInfo.hiddenFlag = true;
      this.drawerInfo.direction = 'rtl';
    },
    edit(value) {
      let params = {lovValueId: value.lovValueId};
      this.getOneData(params)
      this.drawerInfo.title = '编辑值集明细';
      this.drawerInfo.viewFlag = true;
      this.drawerInfo.hiddenFlag = false;
      this.drawerInfo.direction = 'rtl';
    },
    del(value) {
      let params = {lovValueId: value.lovValueId};
      deleteLovValue(params).then((res) => {
        if (res) {
          this.initLovValuePahe();
        }
      })
    },
    onSaveData() {
      this.addFormData.lovId = this.initLovData.lovId
      this.addFormData.lovCode = this.initLovData.lovCode
      if (this.addFormData.lovValueId) {
        updateLovValue(this.addFormData).then((res) => {
          if (res) {
            this.closeDrawer();
          }
        })
      } else {
        saveLovValue(this.addFormData).then((res) => {
          if (res) {
            this.closeDrawer();
          }
        })
      }
    },
    onCancelSave() {
      this.closeDrawer();
    },
    closeDrawer() {
      this.drawerInfo.viewFlag = false;
      this.addFormData = {};
      this.initLovValuePahe();
    },
    conversionEnabled(value) {
      return value ? '已启用' : '未启用';
    },
    goBack() {
      this.$router.push({path: '/lov'});
    }
  },
  mounted() {
  },
  beforeDestroy() {
    this.initLovData.lovId = null;
  }
}
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
  margin-top: 20px;

  &:last-child {
    margin-bottom: 0;
  }
}

.el-row el-col {
  border-radius: 4px;
}
</style>