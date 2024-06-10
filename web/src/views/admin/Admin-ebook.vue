<template>
  <a-layout-content
    :style="{
      background: '#fff',
      padding: '24px',
      margin: 0,
      minHeight: '280px',
    }"
  >
    <a-table
      :columns="columns"
      :data-source="ebooks"
      :pagination="pagination"
      :loading="loading"
      @change="handleTableChange"
    >
      <template #cover="{ text: cover }">
        <img v-if="cover" :src="cover" alt="avatar" />
      </template>

      <template v-slot:action="{ text, record }">
        <a-space size="small">
          <a-button type="primary" @click="edit">编辑</a-button>
          <a-button type="ghost">删除</a-button>
        </a-space>
      </template>
    </a-table>
  </a-layout-content>
  <a-modal
    v-model:visible="modalVisible"
    title="电子书表单"
    @ok="handleLoading"
    :confirm-loading="modalLoading"
  >
    <!--    <template #footer>-->
    <!--      <a-button key="back" @click="handleCancel">Return</a-button>-->
    <!--      <a-button key="submit" type="primary" :loading="loading" @click="handleOk">Submit</a-button>-->
    <!--    </template>-->
    <p>test</p>
  </a-modal>
</template>
<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import axios from "axios";

export default defineComponent({
  name: "AdminEbook",
  setup() {
    const ebooks = ref();
    //current 当前的页数
    //pageSize 每页的个数
    const pagination = ref({
      current: 1,
      pageSize: 2,
      total: 0,
    });
    const loading = ref(false);
    const columns = [
      {
        title: "封面",
        dataIndex: "cover",
        slots: { customRender: "cover" },
      },
      {
        title: "名称",
        dataIndex: "name",
      },
      {
        title: "分类",
        slots: { customRender: "category" },
      },
      {
        title: "文档数",
        dataIndex: "docCount",
      },
      {
        title: "阅读数",
        dataIndex: "viewCount",
      },
      {
        title: "点赞数",
        dataIndex: "voteCount",
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
        .get("/ebook/list", {
          params: {
            page: p.page,
            size: p.size,
          },
        })
        .then((response) => {
          loading.value = false;
          const data = response.data;
          ebooks.value = data.content.list;
          // 重置分页按钮
          pagination.value.current = p.page;
          pagination.value.total = data.content.total;
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
      handleQuery({ page: 1, size: pagination.value.pageSize });
    });
    // 表单-----
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    const handleLoading = () => {
      modalLoading.value = true;
      setTimeout(() => {
        modalVisible.value = false;
        modalLoading.value = false;
      }, 2000);
    };
    // 编辑
    const edit = () => {
      modalVisible.value = true;
    };
    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      edit,
      modalVisible,
      modalLoading,
      handleLoading,
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
