<template>
  <el-container>
    <el-header>
      <el-form :inline="true" :model="tempSearch">
        <el-form-item label="模板编码">
          <el-input v-model="tempSearch.tempCode" placeholder="模板编码"></el-input>
        </el-form-item>
        <el-form-item label="模版描述">
          <el-input v-model="tempSearch.tempDesc" placeholder="模版描述"></el-input>
        </el-form-item>
        <el-form-item label="模板类型">
          <el-select v-model="tempSearch.tempType" placeholder="模板类型">
            <el-option label="邮件" value="EMAIL"></el-option>
            <el-option label="短信" value="SMS"></el-option>
            <el-option label="公告" value="ANNOUNCEMENT"></el-option>
            <el-option label="站内信" value="MESSAGES"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="searchTable">查询</el-button>
        </el-form-item>
      </el-form>
    </el-header>
    <el-main>
      <el-table
          :data="tableData"
          border
          style="width: 100%">
        <el-table-column
            fixed
            prop="tempCode"
            label="模板编码">
        </el-table-column>
        <el-table-column
            prop="tempDesc"
            label="模版描述">
        </el-table-column>
        <el-table-column
            prop="tempType"
            label="模板类型"
            width="100">
        </el-table-column>
        <el-table-column
            prop="content"
            label="模版内容"
            width="400">
        </el-table-column>
        <el-table-column
            prop="enabledFlag"
            label="是否启用"
            width="90">
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            width="100">
          <template slot-scope="scope">
            <el-button @click="detail(scope.row)" type="text" size="small">查看</el-button>
            <el-button type="text" size="small">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-main>
  </el-container>
</template>

<script>
import {pageTemp} from "@/api/template";

export default {
  name: 'TempPage',
  components: {},
  data() {
    return {
      tableData: [],
      tempSearch: {
        tempCode: '',
        tempDesc: '',
        tempType: '',
      }
    }
  },
  created() {
    let params = {pageNum: 1, pageSize: 10}
    pageTemp(params).then(res => {
      console.log(res)
      if (res.data === null) {
        return;
      }
      this.tableData = res.data.list;
    })
  },
  methods: {
    detail(value) {
      console.log(value);
    },
    searchTable() {

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