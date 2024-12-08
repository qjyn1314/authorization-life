<template>
  <el-container>
    <el-header>
      <el-form :inline="true" :model="tableSearch">
        <el-row class="el-row" :gutter="0">
          <el-col :span="6">
            <el-form-item label="值集编码">
              <el-input v-model="tableSearch.lovCode" placeholder="值集编码"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="值集名称">
              <el-input v-model="tableSearch.lovName" placeholder="值集名称"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="值集类型">
              <el-select v-model="tableSearch.lovTypeCode" filterable clearable placeholder="请选择">
                <el-option label="常量" value="FIXED"></el-option>
                <el-option label="URL" value="URL"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-form-item>
              <el-button type="primary" round icon="el-icon-search" @click="searchTable">查询</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="1">
            <el-form-item>
              <el-button type="danger" round icon="el-icon-refresh-right" @click="resetSearch">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-header>
    <el-header>
      <el-row :gutter="10">
        <el-col :span="3">
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
            prop="lovCode"
            label="值集代码">
        </el-table-column>
        <el-table-column
            prop="lovName"
            label="值集名称">
        </el-table-column>
        <el-table-column
            prop="lovTypeCode"
            label="值集类型"
            width="100">
          <template slot-scope="{row}">
            <el-tag type="success">
              {{ row.lovTypeCode }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="description"
            label="描述"
            width="400">
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
            width="300">
          <template slot-scope="{row}">
            <el-row>
              <el-col :span="4">
                <el-button type="info" icon="el-icon-info" circle size="medium" @click="detail(row)"></el-button>
              </el-col>
              <el-col :span="4">
                <el-button type="primary" icon="el-icon-edit" circle size="medium"
                           @click="edit(row)"></el-button>
              </el-col>
              <el-col :span="4">
                <el-popconfirm
                    confirm-button-text='确定'
                    cancel-button-text='暂不删除'
                    icon="el-icon-info"
                    icon-color="red"
                    title="确定删除记录以及其下的值集明细值数据吗？"
                    @confirm="del(row)"
                >
                  <el-button type="danger" icon="el-icon-delete" circle size="medium" slot="reference"></el-button>
                </el-popconfirm>
              </el-col>
              <el-col :span="4">
                <el-button type="text" icon="el-icon-plus" round size="medium"
                           @click="addChild(row)">添加明细
                </el-button>
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
            <el-form-item label="值集编码">
              <el-input v-model="addFormData.lovCode"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="值集名称">
              <el-input v-model="addFormData.lovName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label="值集类型">
              <el-col :span="6">
                <el-select v-model="addFormData.lovTypeCode" filterable clearable placeholder="请选择" size="medium">
                  <el-option label="常量" value="FIXED"></el-option>
                  <el-option label="URL" value="URL"></el-option>
                </el-select>
              </el-col>
            </el-form-item>
          </el-col>
        </el-row>
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
import {deleteLov, oneLov, pageLov, saveLov, updateLov,} from "@/api/lov";

export default {
  name: 'LovPage',
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
        lovCode: '',
        lovName: '',
        lovTypeCode: '',
      },
      initTableSearch: {
        pageNum: 1,
        pageSize: 10,
        lovCode: '',
        lovName: '',
        lovTypeCode: '',
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
    }
  },
  created() {
    this.getPageInfo();
  },
  methods: {
    getPageInfo() {
      pageLov(this.tableSearch).then(res => {
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
      oneLov(params).then(res => {
        if (res.data === null) {
          return;
        }
        this.addFormData = res.data;
      })
    },
    searchTable() {
      this.getPageInfo();
    },
    resetSearch() {
      //浅拷贝对象, 赋值为初始化查询参数
      this.tableSearch = Object.assign({}, this.initTableSearch);
      this.getPageInfo();
    },
    addData() {
      this.drawerInfo.title = '添加值集';
      this.drawerInfo.viewFlag = true;
      this.drawerInfo.hiddenFlag = false;
      this.drawerInfo.direction = 'ltr';
    },
    detail(value) {
      let params = {lovId: value.lovId};
      this.getOneData(params)
      this.drawerInfo.title = '值集详情';
      this.drawerInfo.viewFlag = true;
      this.drawerInfo.hiddenFlag = true;
      this.drawerInfo.direction = 'rtl';
    },
    edit(value) {
      let params = {lovId: value.lovId};
      this.getOneData(params)
      this.drawerInfo.title = '编辑值集';
      this.drawerInfo.viewFlag = true;
      this.drawerInfo.hiddenFlag = false;
      this.drawerInfo.direction = 'rtl';
    },
    del(value) {
      let params = {lovId: value.lovId};
      deleteLov(params).then((res) => {
        if (res) {
          this.resetSearch()
        }
      })
    },
    onSaveData() {
      if (this.addFormData.lovId) {
        updateLov(this.addFormData).then((res) => {
          if (res) {
            this.closeDrawer();
          }
        })
      } else {
        saveLov(this.addFormData).then((res) => {
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
      this.resetSearch()
    },
    conversionEnabled(value) {
      return value ? '已启用' : '未启用';
    },
    addChild(value) {
      this.$router.push({
        path: '/lovValue',
        query: {lovId: value.lovId}
      });
    }
  },
  mounted() {
  },
  beforeDestroy() {
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