<template>
  <a-layout-content
    :style="{
      background: '#fff',
      padding: '24px',
      margin: 0,
      minHeight: '280px',
    }"
  >
    <a-row :gutter="24">
      <a-col :span="8">
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

        <!--         v-if="level1.length > 0" 添加这个才能
                    :default-expand-all-rows="true"生效
                    因为  :default-expand-all-rows="true"它是只有在页面初始化的时候生效一次
                    而一开始页面是没有数据的吗所以它就无法展开,之后数据有了, 就不会生效了
        
        -->
        <a-table
          v-if="level1.length > 0"
          :columns="columns"
          :data-source="level1"
          :loading="loading"
          :pagination="false"
          size="small"
          :default-expand-all-rows="true"
        >
          <template #name="{ text, record }">
            {{ record.sort }} {{ text }}
          </template>

          <template v-slot:action="{ text, record }">
            <a-space size="small">
              <a-button type="primary" @click="edit(record)" size="small"
                >编辑
              </a-button>

              <a-popconfirm
                title="删除后不可以恢复,确认删除?"
                ok-text="Yes"
                cancel-text="No"
                @confirm="handleDelete(record.id)"
              >
                <a-button type="ghost" size="small">删除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </a-table>
      </a-col>
      <a-col :span="16">
        <!--        <a-modal-->
        <!--            v-model:visible="modalVisible"-->
        <!--            title="表单表单"-->
        <!--            @ok="handleLoading"-->
        <!--            :confirm-loading="modalLoading"-->
        <!--        >-->
        <p>
          <a-form layout="inline" :model="param">
            <a-form-item>
              <a-button type="primary" @click="handleLoading">保存</a-button>
            </a-form-item>
          </a-form>
        </p>
        <a-form :model="doc" :label-col="{ span: 6 }" layout="vertical">
          <a-form-item>
            <a-input v-model:value="doc.name" placeholder="名称" />
          </a-form-item>

          <a-form-item>
            <a-tree-select
              v-model:value="doc.parent"
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              placeholder="请选择父文档"
              allow-clear
              tree-default-expand-all
              :tree-data="treeData"
              tree-node-filter-prop="label"
              :fieldNames="{ label: 'name', key: 'id', value: 'id' }"
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
          <a-form-item>
            <a-input v-model:value="doc.sort" placeholder="顺序" />
          </a-form-item>
          <a-form-item>
            <div id="content"></div>
          </a-form-item>
        </a-form>
        <!--        </a-modal>-->
      </a-col>
    </a-row>
  </a-layout-content>
</template>
<script lang="ts">
import { createVNode, defineComponent, onMounted, ref } from "vue";
import axios from "axios";
import { message, Modal, TreeSelectProps } from "ant-design-vue";
import { Tool } from "@/util/tool";
import { LocationQueryValue, useRoute } from "vue-router";
import { ExclamationCircleOutlined } from "@ant-design/icons-vue";
import E from "wangeditor";
import i18next from "i18next";

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
    const treeData = ref<TreeSelectProps["treeData"]>();
    // treeData.value = [];

    // 获取路由路径
    // path ,query, param,fullpath,name ,meta
    const route = useRoute();

    const level1 = ref();
    level1.value = [];
    const loading = ref(false);

    const columns = [
      {
        title: "名称",
        dataIndex: "name",
        slots: { customRender: "name" },
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
    const d = ref();
    const handleQuery = () => {
      loading.value = true;
      axios.get("/doc/all").then((response) => {
        loading.value = false;
        const data = response.data;
        // 查询对page 和size 进行数据校验
        if (data.success) {
          docs.value = data.content;
          // console.log("docs.value", docs.value);
          d.value = Tool.copy(docs.value);
          d.value.forEach((item: any) => {
            item.parent = { parent: item.parent };
            // console.log("item", item);
          });
          d.value.unshift({ id: 0, name: "无" });
          console.log("d", d.value);
          // level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          // level1.value = Tool.array2Tree(d.value, 0);

          console.log("level", level1.value);
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
      editor.create();
    });

    // 表单-----

    const editor = new E("#content");
    editor.i18next = i18next;
    editor.config.zIndex = 0;
    //获取每一列的属性
    const doc: any = ref({});

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
    const deleteNames: Array<string> = [];
    const getDeleteIds = (treeSelectData: any, id: any) => {
      // console.log(treeSelectData, id);
      // 遍历数组，即遍历某一层节点
      for (let i = 0; i < treeSelectData.length; i++) {
        const node = treeSelectData[i];
        if (node.id === id) {
          // 如果当前节点就是目标节点

          // 将目标节点ID 放入数组中
          ids.push(node.id);
          deleteNames.push(node.name);

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
    const edit = (record: any) => {
      modalVisible.value = true;
      // 将表单每行的数据复制传给doc, 这样在编辑没保存之前,这不会实时修改页面的
      // 这个是利用JSON.parse(JSON.stringify)深拷贝对象的原来
      console.log("record", record);
      doc.value = Tool.copy(record);
      // doc.value?.parent  = doc
      console.log("doc value", doc.value);
      treeData.value = Tool.copy(d.value) || [];
      console.log("tree value", treeData.value);
      // 将当前节点以及它的子孙节点变成disable
      setDisable(treeData.value, record.id);
      // treeData!.value!.unshift({ id: 0, name: "无" });
    };

    // 新增
    const add = () => {
      modalVisible.value = true;

      doc.value = {
        ebookId: route.query.ebookId,
      };

      treeData.value = Tool.copy(d.value);
      // treeData!.value!.unshift({ id: 0, name: "无" });
    };

    //删除

    const handleDelete = (id: number) => {
      // console.log(ids);
      // console.log(deleteNames);
      ids.length = 0;
      deleteNames.length = 0;
      getDeleteIds(level1.value, id);
      Modal.confirm({
        title: "重要提醒",
        icon: createVNode(ExclamationCircleOutlined),
        content:
          "将删除：【" +
          deleteNames.join("，") +
          "】删除后不可恢复，确认删除？",
        onOk() {
          // console.log(ids)
          axios.delete("/doc/delete/" + ids.join(",")).then((response) => {
            const data = response.data; // data = commonResp
            if (data.success) {
              // 重新加载列表
              handleQuery();
            } else {
              message.error(data.message);
            }
          });
        },
      });
      // axios.delete("doc/delete/" + ids.join(",")).then((response) => {
      //   const data = response.data;
      //   if (data.success) {
      //     //重新加载列表
      //     handleQuery();
      //   }
      // });
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
      treeData,
      d,
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
