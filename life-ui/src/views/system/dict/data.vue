<template>
  <div class="koi-flex">
    <KoiCard>
      <!-- 搜索条件 -->
      <el-form v-show="showSearch" :inline="true">
        <el-form-item label="字典名称" prop="lovId">
          <el-select v-model="searchParams.lovId" clearable style="width: 240px" @keyup.enter.native="handleListPage">
            <el-option v-for="item in dictOptions" :key="item.lovId" :label="item.lovName" :value="item.lovId" />
          </el-select>
        </el-form-item>
        <el-form-item label="值内容" prop="valueContent">
          <el-input
            placeholder="请输入值内容"
            v-model="searchParams.valueContent"
            clearable
            style="width: 240px"
            @keyup.enter.native="handleListPage"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="search" plain v-throttle="handleSearch">搜索</el-button>
          <el-button type="danger" icon="refresh" plain v-debounce="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格头部按钮 -->
      <el-row :gutter="10">
        <el-col :span="1.5" v-auth="['system:role:add']">
          <el-button type="primary" icon="plus" plain @click="handleAdd()">新增</el-button>
        </el-col>
        <el-col :span="1.5" v-auth="['system:role:update']">
          <el-button type="success" icon="edit" plain @click="handleUpdate()" :disabled="single">修改</el-button>
        </el-col>
        <KoiToolbar v-model:showSearch="showSearch" @refreshTable="handleListPage"></KoiToolbar>
      </el-row>

      <div class="h-20px"></div>
      <!-- 数据表格 -->
      <el-table
        v-loading="loading"
        border
        :data="tableList"
        empty-text="暂时没有数据哟🌻"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="字典编码" prop="lovCode" width="180" align="center"></el-table-column>
        <el-table-column label="字典名称" prop="lovName" width="100" align="center"></el-table-column>
        <el-table-column label="值编码" prop="valueCode" width="120" align="center"></el-table-column>
        <el-table-column label="值内容" prop="valueContent" width="120" align="center"></el-table-column>
        <!-- 注意：如果后端数据返回的是字符串"0" OR "1"，这里的active-value AND inactive-value不需要加冒号，会认为是字符串，否则：后端返回是0 AND 1数字，则需要添加冒号 -->
        <el-table-column label="值状态" prop="enabledFlag" width="100" align="center">
          <template #default="scope">
            <el-switch
              v-model="scope.row.enabledFlag"
              active-text="启用"
              inactive-text="停用"
              :active-value="true"
              :inactive-value="false"
              :inline-prompt="true"
              @change="handleSwitch(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="值排序" prop="valueOrder" width="90" align="center"></el-table-column>
        <el-table-column
          label="字典备注"
          prop="description"
          width="210"
          :show-overflow-tooltip="true"
          align="center"
        ></el-table-column>
        <el-table-column label="创建时间" prop="createdTime" width="175" align="center"></el-table-column>
        <el-table-column label="操作" align="center" width="130" fixed="right">
          <template #default="{ row }">
            <el-tooltip content="修改🌻" placement="top">
              <el-button
                type="primary"
                icon="Edit"
                circle
                plain
                @click="handleUpdate(row)"
                v-auth="['system:role:update']"
              ></el-button>
            </el-tooltip>
            <el-tooltip content="删除🌻" placement="top">
              <el-button
                type="danger"
                icon="Delete"
                circle
                plain
                @click="handleDelete(row)"
                v-auth="['system:role:delete']"
              ></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <div class="h-20px"></div>
      <!-- {{ searchParams.pageNo }} --- {{ searchParams.pageSize }} -->
      <!-- 分页 -->
      <el-pagination
        background
        v-model:current-page="searchParams.pageNo"
        v-model:page-size="searchParams.pageSize"
        v-show="total > 0"
        :page-sizes="[10, 20, 50, 100, 200]"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleListPage"
        @current-change="handleListPage"
      />

      <!-- 添加 OR 修改 -->
      <KoiDialog
        ref="koiDialogRef"
        :title="title"
        @koiConfirm="handleConfirm"
        @koiCancel="handleCancel"
        :loading="confirmLoading"
      >
        <template #content>
          <el-form ref="formRef" :rules="rules" :model="form" label-width="80px" status-icon>
            <el-row>
              <el-col :sm="{ span: 12 }" :xs="{ span: 24 }" class="p-l-10px">
                <el-form-item label="值编码" prop="valueCode">
                  <el-input v-model="form.valueCode" placeholder="请输入值编码" clearable />
                </el-form-item>
              </el-col>
              <el-col :sm="{ span: 12 }" :xs="{ span: 24 }">
                <el-form-item label="值内容" prop="valueContent">
                  <el-input v-model="form.valueContent" placeholder="请输入值内容" clearable />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :sm="{ span: 12 }" :xs="{ span: 24 }" class="p-l-10px">
                <el-form-item label="字典状态" prop="enabledFlag">
                  <el-select v-model="form.enabledFlag" placeholder="请选择字典状态" style="width: 260px" clearable>
                    <el-option label="启用" :value="true" />
                    <el-option label="停用" :value="false" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :sm="{ span: 12 }" :xs="{ span: 24 }" class="p-l-10px">
                <el-form-item label="字典排序" prop="valueOrder">
                  <el-input-number v-model="form.valueOrder" style="width: 260px" clearable />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :sm="{ span: 24 }" :xs="{ span: 24 }">
                <el-form-item label="字典备注" prop="description">
                  <el-input v-model="form.description" :rows="3" type="textarea" placeholder="请输入字典备注" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          {{ form }}
        </template>
      </KoiDialog>
    </KoiCard>
  </div>
</template>

<script setup lang="ts" name="dictDataPage">
// 字典子表
import { nextTick, onMounted, reactive, ref } from "vue";
// @ts-ignore
import { koiMsgBox, koiMsgError, koiMsgInfo, koiMsgWarning, koiNoticeError, koiNoticeSuccess } from "@/utils/koi";
// @ts-ignore
import {
  addLovValue,
  allLovData,
  batchDelete,
  batchDeleteLovValue,
  getLovValueById,
  lovValuePage,
  updateLovValue
} from "@/api/system/dict";
import { useRoute } from "vue-router";

// 表格加载动画Loading
const loading = ref(false);
// 是否显示搜索表单[默认显示]
const showSearch = ref<boolean>(true); // 默认显示搜索条件
// 表格数据
const tableList = ref<any>([]);

// 查询参数
const searchParams = ref({
  pageNo: 1, // 第几页
  pageSize: 10, // 每页显示多少条
  lovId: "",
  valueContent: ""
});

const total = ref<number>(0);

/** 重置搜索参数 */
const resetSearchParams = () => {
  searchParams.value = {
    pageNo: 1,
    pageSize: 10,
    lovId: routeParam.value,
    valueContent: ""
  };
};

/** 搜索 */
const handleSearch = () => {
  console.log("搜索");
  searchParams.value.pageNo = 1;
  handleListPage();
};

/** 重置 */
const resetSearch = () => {
  console.log("重置搜索");
  resetSearchParams();
  handleListPage();
};

/** @current-change：点击分页组件页码发生变化：例如：切换第2、3页 OR 上一页 AND 下一页 OR 跳转某一页 */
/** @size-change：点击分页组件下拉选中条数发生变化：例如：选择10条/页、20条/页等 */
// 分页查询，@current-change AND @size-change都会触发分页，调用后端分页接口
/** 数据表格 */
const handleListPage = async () => {
  // total.value = 400;
  try {
    loading.value = true;
    tableList.value = []; // 重置表格数据
    const res: any = await lovValuePage(searchParams.value);
    console.log("字典数据表格数据->", res.data);
    tableList.value = res.data.list;
    total.value = res.data.total;
    loading.value = false;
  } catch (error) {
    console.log(error);
    koiMsgError("数据查询失败，请刷新重试🌻");
  }
};

/** 数据表格[删除、批量删除等刷新使用] */
const handleTableData = async () => {
  try {
    const res: any = await lovValuePage(searchParams.value);
    console.log("字典数据表格数据->", res.data);
    tableList.value = res.data.list;
    total.value = res.data.total;
    console.log("字典数据表格数据");
  } catch (error) {
    console.log(error);
    koiMsgError("数据查询失败，请刷新重试🌻");
  }
};

const route = useRoute();
let routeParam = ref();

onMounted(() => {
  routeParam.value = route.params.lovId || ""; // 有值
  searchParams.value.lovId = routeParam.value;
  // 获取表格数据
  handleListPage();
  handleDictType();
  handleFormDict();
  handleTableDict();
});

// 下拉框数据
const tagOptions = ref();

/** 字典翻译下拉框 */
const handleFormDict = async () => {
  try {
    tagOptions.value = [];
    // const res: any = await listDataByType("sys_tag_type");
    // console.log("字典数据", res.data);
    // tagOptions.value = res.data;
  } catch (error) {
    console.log(error);
  }
};

// 下拉框数据
const dictLabelOptions = ref();

/** 字典翻译下拉框 */
const handleTableDict = async () => {
  try {
    dictLabelOptions.value = [];
    // const res: any = await listDataByType(searchParams.value.dictType);
    // console.log("字典数据", res.data);
    // dictLabelOptions.value = res.data;
  } catch (error) {
    console.log(error);
  }
};

// 字典类型名称下拉框
const dictOptions = ref();

/** 字典类型名称下拉框 */
const handleDictType = async () => {
  try {
    const res: any = await allLovData({});
    dictOptions.value = res.data;
  } catch (error) {
    console.log(error);
  }
};

const ids = ref([]); // 选中数组
const single = ref<boolean>(true); // 非单个禁用
const multiple = ref<boolean>(true); // 非多个禁用

/** 是否多选 */
const handleSelectionChange = (selection: any) => {
  console.log(selection);
  ids.value = selection.map((item: any) => item.lovValueId);
  single.value = selection.length != 1; // 单选
  multiple.value = !selection.length; // 多选
};

/** 添加 */
const handleAdd = () => {
  // 打开对话框
  koiDialogRef.value.koiOpen();
  koiNoticeSuccess("添加🌻");
  // 重置表单
  resetForm();
  // 标题
  title.value = "字典添加";
  form.value.enabledFlag = true;
  form.value.lovId = routeParam.value;
};

/** 回显数据 */
const handleEcho = async (id: any) => {
  if (id == null || id == "") {
    koiMsgWarning("请选择需要修改的数据🌻");
    return;
  }
  try {
    const res: any = await getLovValueById({ lovValueId: id });
    console.log(res.data);
    form.value = res.data;
  } catch (error) {
    koiNoticeError("数据获取失败，请刷新重试🌻");
    console.log(error);
  }
};

/** 修改 */
const handleUpdate = async (row?: any) => {
  // 打开对话框
  koiDialogRef.value.koiOpen();
  koiNoticeSuccess("修改🌻");
  // 重置表单
  resetForm();
  // 标题
  title.value = "字典修改";
  const lovValueId = row ? row.lovValueId : ids.value[0];
  if (lovValueId == null || lovValueId == "") {
    koiMsgError("请选中需要修改的数据🌻");
  }
  console.log(lovValueId);
  // 回显数据
  handleEcho(lovValueId);
};

// 添加 OR 修改对话框Ref
const koiDialogRef = ref();
// 标题
const title = ref("字典管理");
// form表单Ref
const formRef = ref<any>();
// form表单
let form = ref<any>({
  lovId: "",
  lovCode: "",
  valueCode: "",
  valueContent: "",
  enabledFlag: true,
  valueOrder: 1,
  description: ""
});

/** 清空表单数据 */
const resetForm = () => {
  // 等待 DOM 更新完成
  nextTick(() => {
    if (formRef.value) {
      // 重置该表单项，将其值重置为初始值，并移除校验结果
      formRef.value.resetFields();
    }
  });
  form.value = {
    lovId: "",
    lovCode: "",
    valueCode: "",
    valueContent: "",
    enabledFlag: true,
    valueOrder: 1,
    description: ""
  };
};

/** 表单规则 */
const rules = reactive({
  valueCode: [{ required: true, message: "请输入值编码", trigger: "blur" }],
  valueContent: [{ required: true, message: "请输入之内容", trigger: "blur" }],
  valueOrder: [{ required: true, message: "请输入值排序", trigger: "blur" }],
  enabledFlag: [{ required: true, message: "请输入值状态", trigger: "blur" }]
});

// 确定按钮是否显示Loading
const confirmLoading = ref(false);

/** 确定  */
const handleConfirm = () => {
  if (!formRef.value) return;
  confirmLoading.value = true;
  (formRef.value as any).validate(async (valid: any) => {
    if (valid) {
      console.log("表单ID", form.value.lovValueId);
      if (form.value.lovValueId != null && form.value.lovValueId != "") {
        try {
          await updateLovValue(form.value);
          koiNoticeSuccess("修改成功🌻");
          confirmLoading.value = false;
          koiDialogRef.value.koiQuickClose();
          resetForm();
          handleListPage();
        } catch (error) {
          console.log(error);
          confirmLoading.value = false;
          koiNoticeError("修改失败，请刷新重试🌻");
        }
      } else {
        try {
          await addLovValue(form.value);
          koiNoticeSuccess("添加成功🌻");
          confirmLoading.value = false;
          koiDialogRef.value.koiQuickClose();
          resetForm();
          handleListPage();
        } catch (error) {
          console.log(error);
          confirmLoading.value = false;
          koiNoticeError("添加失败，请刷新重试🌻");
        }
      }
      handleTableDict();
      // let loadingTime = 1;
      // setInterval(() => {
      //   loadingTime--;
      //   if (loadingTime === 0) {
      //     koiNoticeSuccess("朕让你提交了么？信不信锤你🌻");
      //     confirmLoading.value = false;
      //     resetForm();
      //     koiDialogRef.value.koiQuickClose();
      //   }
      // }, 1000);
    } else {
      koiMsgError("验证失败，请检查填写内容🌻");
      confirmLoading.value = false;
    }
  });
};

/** 取消 */
const handleCancel = () => {
  koiDialogRef.value.koiClose();
};

/** 状态开关 */
const handleSwitch = (row: any) => {
  let text = row.enabledFlag ? "启用" : "停用";
  koiMsgBox("确认要[" + text + "]-[" + row.valueContent + "]字典吗？")
    .then(async () => {
      if (!row.lovValueId || !row.enabledFlag) {
        koiMsgWarning("请选择需要修改的数据🌻");
        return;
      }
      try {
        // await updateStatus(row.lovValueId, row.dictStatus);
        koiNoticeSuccess("修改成功🌻");
      } catch (error) {
        console.log(error);
        handleTableData();
        koiNoticeError("修改失败，请刷新重试🌻");
      }
    })
    .catch(() => {
      koiMsgError("已取消🌻");
    });
};

/** 删除 */
const handleDelete = (row: any) => {
  const id = row.lovValueId;
  if (id == null || id == "") {
    koiMsgWarning("请选中需要删除的数据🌻");
    return;
  }
  koiMsgBox("您确认需要删除字典名称[" + row.valueContent + "]么？")
    .then(async () => {
      const res: any = await batchDeleteLovValue([id]);
      if (res.code !== "0") {
        return;
      }
      handleTableData();
      koiNoticeSuccess("删除成功🌻");
    })
    .catch(() => {
      koiMsgError("已取消🌻");
    });
};

/** 批量删除 */
const handleBatchDelete = () => {
  if (ids.value.length == 0) {
    koiMsgInfo("请选择需要删除的数据🌻");
    return;
  }
  koiMsgBox("您确认需要进行批量删除么？")
    .then(async () => {
      try {
        // console.log("ids",ids.value)
        await batchDeleteLovValue(ids.value);
        handleTableData();
        koiNoticeSuccess("批量删除成功🌻");
      } catch (error) {
        console.log(error);
        handleTableData();
        koiNoticeError("批量删除失败，请刷新重试🌻");
      }
    })
    .catch(() => {
      koiMsgError("已取消🌻");
    });
};
</script>

<style lang="scss" scoped></style>
