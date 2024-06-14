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
          <a-button type="primary" @click="handleQuery()">查询</a-button>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="add()">新增</a-button>
        </a-form-item>
      </a-form>
    </p>
    <!--     添加:pagination 不让页面分页显示,记得加冒号-->
    <a-table
      :columns="columns"
      :data-source="level1"
      :loading="loading"
      :pagination="false"
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
    title="表单表单"
    @ok="handleLoading"
    :confirm-loading="modalLoading"
  >
    <a-form :model="category" :label-col="{ span: 6 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>
      <a-form-item label="顺序">
        <a-input v-model:value="category.sort" />
      </a-form-item>
      <a-form-item label="父分类">
        <a-input v-model:value="category.parent" />
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
  name: "AdminCategory",
  setup() {
    const categorys = ref();
    /**
     * [{id:"",
     * parent:"",
     * name:"",
     * children:[{
     *   id:"",
     *   parent:"",
     *   name:""
     * }]
     * }]
     */
    const level1 = ref();
    const loading = ref(false);
    const columns = [
      {
        title: "名称",
        dataIndex: "name",
      },
      {
        title: "父分类",
        key: "parent",
        dataIndex: "parent",
      },
      {
        title: "排序",
        dataIndex: "sort",
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
    // 查询传入的参数是: 从哪一页开始查,每页是多少个
    const handleQuery = () => {
      loading.value = true;
      axios.get("/category/all").then((response) => {
        loading.value = false;
        const data = response.data;
        // 查询对page 和size 进行数据校验
        if (data.success) {
          categorys.value = data.content;
          // console.log("categorys.value", categorys.value);
          level1.value = [];
          level1.value = Tool.array2Tree(categorys.value, 0);
          // console.log("level", level1.value);
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
      handleQuery();
    };
    // page ,size 是跟后端的属性相对应的
    onMounted(() => {
      handleQuery();
    });

    // 表单-----
    interface recordType {
      name: string;
      sort: number;
      parent: number;
    }

    //获取每一列的属性
    const category = ref<recordType>({
      name: "",
      sort: 0,
      parent: 0,
    });
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    // 编辑修改表单数据
    const handleLoading = () => {
      modalLoading.value = true;
      axios.post("/category/save", category.value).then((response) => {
        modalLoading.value = false; // 后端只要返回数据,我们就去掉loading 效果
        const data = response.data;
        if (data.success) {
          // 如果返回的值是成功的话,就表示上传成功了
          modalVisible.value = false;

          // 重新加载列表
          handleQuery();
        } else {
          message.error(data.message);
        }
      });
    };
    // 编辑
    const edit = (record: recordType) => {
      modalVisible.value = true;
      // 将表单每行的数据复制传给category, 这样在编辑没保存之前,这不会实时修改页面的
      // 这个是利用JSON.parse(JSON.stringify)深拷贝对象的原来
      category.value = Tool.copy(record);
    };

    // 新增
    const add = () => {
      modalVisible.value = true;
      category.value = {
        name: "",
        sort: 0,
        parent: 0,
      };
    };

    //删除
    const handleDelete = (id: number) => {
      axios.delete("category/delete/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          //重新加载列表
          handleQuery();
        }
      });
    };

    const param = ref();
    param.value = {};

    return {
      categorys,
      level1,
      columns,
      loading,
      handleTableChange,
      handleQuery,
      add,
      edit,
      modalVisible,
      modalLoading,
      handleLoading,
      category,
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
