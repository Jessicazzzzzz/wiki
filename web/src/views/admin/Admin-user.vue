<template>
  <a-layout-content
    :style="{
      background: '#fff',
      padding: '24px',
      margin: 0,
      minHeight: '280px',
    }"
  >
    <p>
      <a-form layout="inline" :model="param">
        <a-form-item>
          <a-input
            v-model:value="param.loginName"
            placeholder="登录名"
          ></a-input>
        </a-form-item>
        <a-form-item>
          <a-button
            type="primary"
            @click="handleQuery({ page: 1, size: pagination.pageSize })"
            @press-enter="handleQuery({ page: 1, size: pagination.pageSize })"
            >查询
          </a-button>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="add()">新增</a-button>
        </a-form-item>
      </a-form>
    </p>
    <a-table
      :columns="columns"
      :row-key="(record:any) => record.id"
      :data-source="users"
      :pagination="pagination"
      :loading="loading"
      @change="handleTableChange"
    >
      <template v-slot:action="{ text, record }">
        <a-space size="small">
          <a-button type="primary" @click="resetPassword(record)">
            重置密码
          </a-button>
          <a-button type="primary" @click="edit(record)"> 编辑</a-button>
          <a-popconfirm
            title="删除后不可以恢复,确认删除?"
            ok-text="Yes"
            cancel-text="No"
            @confirm="handleDelete(record.id)"
          >
            <a-button type="ghost">删除</a-button>
          </a-popconfirm>
        </a-space>
      </template>
    </a-table>
  </a-layout-content>
  <a-modal
    v-model:visible="modalVisible"
    title="用户表单"
    @ok="handleModalOk"
    :confirm-loading="modalLoading"
  >
    <a-form :model="user" :label-col="{ span: 6 }">
      <!--      !!user.id 校验 将number 转成 boolean -->
      <a-form-item label="登录名">
        <a-input v-model:value="user.loginName" :disabled="!!user.id" />
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="user.name" />
      </a-form-item>

      <a-form-item label="密码">
        <a-input v-model:value="user.password" type="password" />
      </a-form-item>
    </a-form>
  </a-modal>

  <a-modal
    title="重置密码"
    v-model:visible="resetModalVisible"
    :confirm-loading="resetModalLoading"
    @ok="handleResetModalOk"
  >
    <a-form :model="user" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="新密码">
        <a-input v-model:value="user.password" type="password" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import axios from "axios";
import { message } from "ant-design-vue";
import { Tool } from "@/util/tool";

declare let hexMd5: any;
declare let KEY: any;
export default defineComponent({
  name: "AdminUser",
  setup() {
    const param = ref();
    param.value = {};
    const users = ref();
    //current 当前的页数
    //pageSize 每页的个数
    const pagination = ref({
      current: 1,
      pageSize: 3,
      total: 0,
    });
    const loading = ref(false);
    const columns = [
      {
        title: "登录名",
        dataIndex: "loginName",
      },
      {
        title: "名称",
        dataIndex: "name",
      },
      // {
      //   title: "分类1",
      //   dataIndex: "category1Id",
      // },
      // {
      //   title: "分类2",
      //   dataIndex: "category2Id",
      // },
      {
        title: "密码",
        dataIndex: "password",
      },

      {
        title: "Action",
        key: "action",
        slots: { customRender: "action" },
      },
    ];
    // p 是前端的url 带的请求参数,
    // axios 的请求写法是固定的,需要带参数,就通过对象的形式,包裹到params中去的
    //  因为参数可能会很多,所以我们就结构p 中一部分的参数
    const handleQuery = (p: any) => {
      loading.value = true;
      axios
        .get("/user/list", {
          params: {
            page: p.page,
            size: p.size,
            loginName: param.value.loginName,
          },
        })
        .then((response) => {
          loading.value = false;
          const data = response.data;
          // 查询对page 和size 进行数据校验
          if (data.success) {
            users.value = data.content.list;
            // 重置分页按钮
            pagination.value.current = p.page;
            pagination.value.total = data.content.total;
          } else {
            message.error(data.message);
          }
        });
    };

    /**
     * 表格点击页触发
     */
    const handleTableChange = (pagitation: any) => {
      console.log("自带的分页参数都有什么嘛" + pagitation);
      handleQuery({
        page: pagitation.current,
        size: pagitation.pageSize,
      });
    };
    // page ,size 是跟后端的属性相对应的
    // pagination 中有自己的pageSize 的属性
    onMounted(() => {
      handleQuery({
        page: 1,
        size: pagination.value.pageSize,
      });
    });

    // -------- 表单 ---------
    const user = ref();
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleModalOk = () => {
      modalLoading.value = true;

      user.value.password = hexMd5(user.value.password + KEY);

      axios.post("/user/save", user.value).then((response) => {
        modalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          modalVisible.value = false;

          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 编辑
     */
    const edit = (record: any) => {
      modalVisible.value = true;
      user.value = Tool.copy(record);
    };

    /**
     * 新增
     */
    const add = () => {
      modalVisible.value = true;
      user.value = {};
    };

    const handleDelete = (id: number) => {
      axios.delete("/user/delete/" + id).then((response) => {
        const data = response.data; // data = commonResp
        if (data.success) {
          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    // -------- 重置密码 ---------
    const resetModalVisible = ref(false);
    const resetModalLoading = ref(false);
    const handleResetModalOk = () => {
      resetModalLoading.value = true;

      user.value.password = hexMd5(user.value.password + KEY);

      axios.post("/user/reset-password", user.value).then((response) => {
        resetModalLoading.value = false;
        const data = response.data; // data = commonResp
        if (data.success) {
          resetModalVisible.value = false;

          // 重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        } else {
          message.error(data.message);
        }
      });
    };

    /**
     * 重置密码
     */
    const resetPassword = (record: any) => {
      resetModalVisible.value = true;
      user.value = Tool.copy(record);
      user.value.password = null;
    };

    return {
      param,
      users,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,

      edit,
      add,

      user,
      modalVisible,
      modalLoading,
      handleModalOk,

      handleDelete,

      resetModalVisible,
      resetModalLoading,
      handleResetModalOk,
      resetPassword,
    };
  },
});
</script>
<style scoped>
img {
  width: 50px;
  height: 50px;
}
</style>
