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
    <a-form :model="doc" :label-col="{ span: 6 }">
      <a-form-item label="名称">
        <a-input v-model:value="doc.name" />
      </a-form-item>
      <a-form-item label="选择父文档">
        <a-tree-select
          v-model:value="doc.parent"
          show-search
          style="width: 100%"
          :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
          placeholder="请选择父文档"
          allow-clear
          tree-default-expand-all
          :tree-data="treeSelectData"
          tree-node-filter-prop="label"
          :replaceFields="{ label: 'name', key: 'id', value: 'id' }"
        >
        </a-tree-select>
      </a-form-item>

      <!--      <a-form-item label="父文档">-->
      <!--        <a-select ref="select" v-model:value="doc.parent">-->
      <!--          <a-select-option value="0">无</a-select-option>-->
      <!--          <a-select-option-->
      <!--            :value="c.id"-->
      <!--            v-for="c in level1"-->
      <!--            :key="c.id"-->
      <!--            :disabled="doc.id === c.id"-->
      <!--            >{{ c.name }}-->
      <!--          </a-select-option>-->
      <!--        </a-select>-->
      <!--      </a-form-item>-->
      <a-form-item label="顺序">
        <a-input v-model:value="doc.sort" />
      </a-form-item>
    </a-form>
  </a-modal>
</template>
<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import axios from "axios";
import { message } from "ant-design-vue";
import { Tool } from "@/util/tool";
import { LocationQueryValue, useRoute } from "vue-router";

export default defineComponent({
  name: "AdminDoc",
  setup() {
    const docs = ref();
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
    // 因为要添加父节点中无的数据,所以应该将level1中的数据复制到新的变量中去
    const treeSelectData = ref();
    treeSelectData.value = [];
    // 获取路由路径
    // path ,query, param,fullpath,name ,meta
    const route = useRoute();

    const level1 = ref();
    const loading = ref(false);
    const columns = [
      {
        title: "名称",
        dataIndex: "name",
      },
      {
        title: "父文档",
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
      axios.get("/doc/all").then((response) => {
        loading.value = false;
        const data = response.data;
        // 查询对page 和size 进行数据校验
        if (data.success) {
          docs.value = data.content;
          // console.log("docs.value", docs.value);
          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
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
      id: number | null | undefined;
      ebookId: LocationQueryValue | LocationQueryValue[];
    }

    //获取每一列的属性
    const doc = ref<recordType>({
      name: "",
      sort: 0,
      parent: 0,
      id: undefined,
      ebookId: "",
    });
    const modalVisible = ref(false);
    const modalLoading = ref(false);
    // 编辑修改表单数据
    const handleLoading = () => {
      modalLoading.value = true;
      axios.post("/doc/save", doc.value).then((response) => {
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

    /**
     * 将某节点及其子孙节点全部置为disabled
     */
    const setDisable = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点
          console.log("disabled", node);
          // 将目标节点设置为disabled
          node.disabled = true;

          // 遍历所有子节点，将所有子节点全部都加上disabled
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              setDisable(children, children[j].id);
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            setDisable(children, id);
          }
        }
      }
    };
    const ids: Array<string> = [];
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点

          // 将目标节点ID 放入数组中
          ids.push(node.id);

          // 遍历所有子节点，将所有子节点全部都放入ids
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            for (let j = 0; j < children.length; j++) {
              getDeleteIds(children, children[j].id);
            }
          }
        } else {
          // 如果当前节点不是目标节点，则到其子节点再找找看。
          const children = node.children;
          if (Tool.isNotEmpty(children)) {
            getDeleteIds(children, id);
          }
        }
      }
    };
    // 编辑
    const edit = (record: recordType) => {
      modalVisible.value = true;
      // 将表单每行的数据复制传给doc, 这样在编辑没保存之前,这不会实时修改页面的
      // 这个是利用JSON.parse(JSON.stringify)深拷贝对象的原来
      doc.value = Tool.copy(record);
      treeSelectData.value = Tool.copy(level1.value);
      // 将当前节点以及它的子孙节点变成disable
      setDisable(treeSelectData.value, record.id);
      treeSelectData.value.unshift({ id: 0, name: "无" });
    };

    // 新增
    const add = () => {
      modalVisible.value = true;
      doc.value = {
        name: "",
        sort: 0,
        parent: 0,
        id: null,
        ebookId: route.query.ebookId,
      };
      treeSelectData.value = Tool.copy(level1.value);
      treeSelectData.value.unshift({ id: 0, name: "无" });
    };

    //删除
    const handleDelete = (id: number) => {
      getDeleteIds(level1.value, id);
      
      axios.delete("doc/delete/" + ids.join(",")).then((response) => {
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
      docs,
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
      doc,
      handleDelete,
      param,
      treeSelectData,
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
