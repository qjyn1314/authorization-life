<template>
  <div class="koi-flex">
    <KoiCard>
      <!-- 搜索条件 -->
      <el-form v-show="showSearch" :inline="true">
        <el-form-item label="字典名称" prop="lovName">
          <el-input
            placeholder="请输入字典名称"
            v-model="searchParams.lovName"
            clearable
            style="width: 240px"
            @keyup.enter.native="handleListPage"
          ></el-input>
        </el-form-item>
        <el-form-item label="字典编码" prop="lovCode">
          <el-input
            placeholder="请输入字典编码"
            v-model="searchParams.lovCode"
            clearable
            style="width: 240px"
            @keyup.enter.native="handleListPage"
          ></el-input>
        </el-form-item>
        <el-form-item label="字典状态" prop="enabledFlag">
          <el-select
            placeholder="请选择字典状态"
            v-model="searchParams.enabledFlag"
            clearable
            style="width: 240px"
            @keyup.enter.native="handleListPage"
          >
            <el-option label="启用" :value="true" />
            <el-option label="停用" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="search" plain v-debounce="handleSearch">搜索</el-button>
          <el-button type="danger" icon="refresh" plain v-throttle="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>

      <!-- 表格头部按钮 -->
      <el-row :gutter="10">
        <el-col :span="1.5" v-auth="['system:role:add']">
          <el-button type="primary" icon="plus" plain @click="handleAdd()">新增</el-button>
        </el-col>
        <el-col :span="1.5" v-auth="['system:role:delete']">
          <el-button type="danger" icon="delete" plain @click="handleBatchDelete()" :disabled="multiple">删除</el-button>
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
        <el-table-column label="字典名称" prop="lovName" width="120" align="center"></el-table-column>
        <el-table-column label="字典编码" prop="lovCode" width="180" align="center">
          <template #default="scope">
            <RouterLink :to="`/system/dict/data/${scope.row.lovId}`">
              <span>{{ scope.row.lovCode }}</span>
            </RouterLink>
          </template>
        </el-table-column>
        <el-table-column label="字典类型" prop="lovTypeCodeContent" width="90" align="center"></el-table-column>
        <el-table-column
          label="字典备注"
          prop="description"
          width="230"
          :show-overflow-tooltip="true"
          align="center"
        ></el-table-column>
        <el-table-column label="创建时间" prop="createdTime" width="175" align="center"></el-table-column>
        <!-- 注意：如果后端数据返回的是字符串"0" OR "1"，这里的active-value AND inactive-value不需要加冒号，会认为是字符串，否则：后端返回是0 AND 1数字，则需要添加冒号 -->
        <el-table-column label="字典状态" prop="enabledFlag" width="100" align="center">
          <template #default="scope">
            <KoiTag :tagOptions="koiDicts.FLAG_STATUS" :value="scope.row.enabledFlag" ></KoiTag>
          </template>
        </el-table-column>
        <el-table-column label="修改时间" prop="updatedTime" width="175" align="center"></el-table-column>
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
      <KoiDrawer
        ref="koiDrawerRef"
        :title="title"
        @koiConfirm="handleConfirm"
        @koiCancel="handleCancel"
        :loading="confirmLoading"
      >
        <template #content>
          <el-form ref="formRef" :rules="rules" :model="form" label-width="80px" status-icon>
            <el-row>
              <el-col :sm="{ span: 24 }" :xs="{ span: 24 }">
                <el-form-item label="字典名称" prop="lovName">
                  <el-input v-model="form.lovName" placeholder="请输入字典名称" clearable />
                </el-form-item>
              </el-col>
              <el-col :sm="{ span: 24 }" :xs="{ span: 24 }">
                <el-form-item label="字典编码" prop="lovCode">
                  <el-input v-model="form.lovCode" placeholder="请输入字典编码" clearable />
                </el-form-item>
              </el-col>
              <el-col :sm="{ span: 24 }" :xs="{ span: 24 }">
                <el-form-item label="字典类型" prop="lovTypeCode">
                  <el-select v-model="form.lovTypeCode" placeholder="请选择字典类型" clearable>
                    <el-option label="固定值" value="FIXED" />
                    <el-option label="链接" value="URL" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :sm="{ span: 24 }" :xs="{ span: 24 }">
                <el-form-item label="字典状态" prop="enabledFlag">
                  <el-select v-model="form.enabledFlag" placeholder="请选择字典状态" clearable>
                    <el-option label="启用" :value="true" />
                    <el-option label="停用" :value="false" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :sm="{ span: 24 }" :xs="{ span: 24 }">
                <el-form-item label="字典备注" prop="description">
                  <el-input v-model="form.description" :rows="5" type="textarea" placeholder="请输入字典备注" />
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <!--          {{ form }}-->
        </template>
      </KoiDrawer>
    </KoiCard>
  </div>
</template>

<script setup lang="ts" name="dictTypePage">
// 字典主表页面
import { nextTick, onMounted, reactive, ref } from "vue";
import {
  koiMsgBox,
  koiMsgError,
  koiMsgInfo,
  koiMsgSuccess,
  koiMsgWarning,
  koiNoticeError,
  koiNoticeSuccess
} from "@/utils/koi.ts";
// @ts-ignore
import { add, batchDelete, getById, listPage, update } from "@/api/system/dict/index.ts";
import { useKoiDict } from "@/hooks/dicts/index.ts";

const { koiDicts } = useKoiDict(["FLAG_STATUS"]);

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
  lovName: "",
  lovCode: "",
  enabledFlag: null
});

const total = ref<number>(0);

/** 重置搜索参数 */
const resetSearchParams = () => {
  searchParams.value = {
    pageNo: 1,
    pageSize: 10,
    lovName: "",
    lovCode: "",
    enabledFlag: null
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
  total.value = 400;
  try {
    loading.value = true;
    tableList.value = []; // 重置表格数据
    const res: any = await listPage(searchParams.value);
    console.log("字典数据表格数据->", res.data);
    tableList.value = res.data.list;
    total.value = res.data.total;
    loading.value = false;
  } catch (error) {
    console.log(error);
    koiNoticeError("数据查询失败，请刷新重试🌻");
  }
};

/** 数据表格[删除、批量删除等刷新使用] */
const handleTableData = async () => {
  try {
    const res: any = await listPage(searchParams.value);
    console.log("字典数据表格数据->", res.data);
    tableList.value = res.data.list;
    total.value = res.data.total;
  } catch (error) {
    console.log(error);
    koiNoticeError("数据查询失败，请刷新重试🌻");
  }
};

// 静态页面防止报错(可直接删除)
// @ts-ignore
const handleStaticPage = () => {
  listPage(searchParams.value);
};

onMounted(() => {
  // 获取表格数据
  handleListPage();
});

const ids = ref([]); // 选中数组
const single = ref<boolean>(true); // 非单个禁用
const multiple = ref<boolean>(true); // 非多个禁用

/** 是否多选 */
const handleSelectionChange = (selection: any) => {
  // console.log(selection);
  ids.value = selection.map((item: any) => item.lovId);
  single.value = selection.length != 1; // 单选
  multiple.value = !selection.length; // 多选
};

/** 添加 */
const handleAdd = () => {
  // 打开抽屉
  koiDrawerRef.value.koiOpen();
  koiMsgSuccess("添加🌻");
  // 重置表单
  resetForm();
  // 标题
  title.value = "字典添加";
  form.value.enabledFlag = true;
};

/** 回显数据 */
const handleEcho = async (id: any) => {
  console.log("回显数据ID", id);
  if (id == null || id == "") {
    koiMsgWarning("请选择需要修改的数据🌻");
    return;
  }
  try {
    const res: any = await getById({ lovId: id });
    console.log(res.data);
    form.value = res.data;
  } catch (error) {
    koiNoticeError("数据获取失败，请刷新重试🌻");
    console.log(error);
  }
};

/** 修改 */
const handleUpdate = async (row?: any) => {
  // 打开抽屉
  koiDrawerRef.value.koiOpen();
  koiMsgSuccess("修改🌻");
  // 重置表单
  resetForm();
  // 标题
  title.value = "字典修改";
  const lovId = row ? row.lovId : ids.value[0];
  if (lovId == null || lovId == "") {
    koiMsgError("请选中需要修改的数据🌻");
  }
  console.log(lovId);
  // 回显数据
  handleEcho(lovId);
};

// 添加 OR 修改抽屉Ref
const koiDrawerRef = ref();
// 标题
const title = ref("字典类型管理");
// form表单Ref
const formRef = ref<any>();
// form表单
let form = ref<any>({
  lovId: "",
  lovTypeCode: "",
  lovCode: "",
  lovName: "",
  enabledFlag: "",
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
    lovTypeCode: "",
    lovCode: "",
    lovName: "",
    enabledFlag: "",
    description: ""
  };
};

/** 表单规则 */
const rules = reactive({
  lovTypeCode: [{ required: true, message: "请输入字典类型", trigger: "blur" }],
  lovCode: [{ required: true, message: "请输入字典编码", trigger: "blur" }],
  lovName: [{ required: true, message: "请输入字典名字", trigger: "blur" }],
  enabledFlag: [{ required: true, message: "请输入选择字典状态", trigger: "blur" }],
  description: [{ required: true, message: "请输入字典描述", trigger: "blur" }]
});

// 确定按钮是否显示Loading
const confirmLoading = ref(false);

/** 确定  */
const handleConfirm = () => {
  if (!formRef.value) return;
  confirmLoading.value = true;
  (formRef.value as any).validate(async (valid: any) => {
    if (valid) {
      console.log("表单ID", form.value.lovId);
      if (form.value.lovId != null && form.value.lovId != "") {
        try {
          await update(form.value);
          koiNoticeSuccess("修改成功🌻");
          confirmLoading.value = false;
          koiDrawerRef.value.koiQuickClose();
          resetForm();
          handleListPage();
        } catch (error) {
          console.log(error);
          confirmLoading.value = false;
          koiNoticeError("修改失败，请刷新重试🌻");
        }
      } else {
        try {
          await add(form.value);
          koiNoticeSuccess("添加成功🌻");
          confirmLoading.value = false;
          koiDrawerRef.value.koiQuickClose();
          resetForm();
          handleListPage();
        } catch (error) {
          console.log(error);
          confirmLoading.value = false;
          koiNoticeError("添加失败，请刷新重试🌻");
        }
      }
    } else {
      koiMsgError("验证失败，请检查填写内容🌻");
      confirmLoading.value = false;
    }
  });
};

/** 取消 */
const handleCancel = () => {
  koiDrawerRef.value.koiClose();
};

/** 删除 */
const handleDelete = (row: any) => {
  const id = row.lovId;
  if (id == null || id == "") {
    koiMsgWarning("请选中需要删除的数据🌻");
    return;
  }
  koiMsgBox("您确认需要删除字典名称[" + row.lovName + "]么？")
    .then(async () => {
      const res: any = await batchDelete([id]);
      if (res.code !== "0") {
        return;
      }
      koiNoticeSuccess("删除成功🌻");
      resetForm();
      handleListPage();
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
      const res: any = await batchDelete(ids.value);
      if (res.code !== "0") {
        return;
      }
      koiNoticeSuccess("批量删除成功🌻");
      handleTableData();
    })
    .catch(() => {
      koiMsgError("已取消🌻");
    });
};
</script>

<style lang="scss" scoped></style>
