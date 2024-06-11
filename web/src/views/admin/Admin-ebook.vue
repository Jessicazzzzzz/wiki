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
          <a-input v-model:value="param.name" placeholder="名称"></a-input>
        </a-form-item>
        <a-form-item>
          <a-button
            type="primary"
            @click="handleQuery({ page: 1, size: pagination.pageSize })"
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
          <a-button type="primary" @click="edit(record)">编辑</a-button>

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
    title="电子书表单"
    @ok="handleLoading"
    :confirm-loading="modalLoading"
  >
    <a-form :model="ebook" :label-col="{ span: 6 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类1">
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="分类2">
        <a-input v-model:value="ebook.category2Id" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description" type="textarea" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import axios from "axios";
import { message } from "ant-design-vue";
import { Tool } from "@/util/tool";

export default defineComponent({
  name: "AdminEbook",
  setup() {
    const ebooks = ref();
    //current 当前的页数
    //pageSize 每页的个数
    const pagination = ref({
      current: 1,
      pageSize: 10,
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
        title: "分类1",
        dataIndex: "category1Id",
      },
      {
        title: "分类2",
        dataIndex: "category2Id",
      },
      // {
      //   title: "分类",
      //   slots: { customRender: "category" },
      // },
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
            name: param.value.name,
          },
        })
        .then((response) => {
          loading.value = false;
          const data = response.data;
          // 查询对page 和size 进行数据校验
          if (data.success) {
            ebooks.value = data.content.list;
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
      handleQuery({ page: 1, size: pagination.value.pageSize });
    });

    // 表单-----
    interface recordType {
      name: string | null | undefined;
      cover: string | null | undefined;
      category1Id: number | null | undefined;
      category2Id: number | null | undefined;
      description: string | null | undefined;
    }

    //获取每一列的属性
    const ebook = ref<recordType>({
      name: "",
      cover: "",
      category1Id: 0,
      category2Id: 0,
      description: "",
    });
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    // 编辑修改表单数据
    const handleLoading = () => {
      modalLoading.value = true;
      axios.post("/ebook/save", ebook.value).then((response) => {
        modalLoading.value = false; // 后端只要返回数据,我们就去掉loading 效果
        const data = response.data;
        if (data.success) {
          // 如果返回的值是成功的话,就表示上传成功了
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
    // 编辑
    const edit = (record: recordType) => {
      modalVisible.value = true;
      // 将表单每行的数据复制传给ebook, 这样在编辑没保存之前,这不会实时修改页面的
      // 这个是利用JSON.parse(JSON.stringify)深拷贝对象的原来
      ebook.value = Tool.copy(record);
    };

    // 新增
    const add = () => {
      modalVisible.value = true;
      ebook.value = {
        name: null,
        cover: null,
        category1Id: null,
        category2Id: null,
        description: null,
      };
    };

    //删除
    const handleDelete = (id: number) => {
      axios.delete("ebook/delete/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          //重新加载列表
          handleQuery({
            page: pagination.value.current,
            size: pagination.value.pageSize,
          });
        }
      });
    };

    const param = ref();
    param.value = {};

    return {
      ebooks,
      pagination,
      columns,
      loading,
      handleTableChange,
      handleQuery,
      add,
      edit,
      modalVisible,
      modalLoading,
      handleLoading,
      ebook,
      handleDelete,
      param,
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
