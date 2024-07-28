<template>
  <a-layout>
    <a-layout-content
      :style="{
        background: '#fff',
        padding: '24px',
        margin: 0,
        minHeight: '280px',
      }"
    >
      <h3 v-if="treeData!.length===0">对不起,找不到相关文档</h3>
      <a-row>
        <a-col :span="6">
          <a-tree
            v-if="level1.length > 0"
            @select="onSelect"
            :tree-data="treeData"
            :replaceFields="{ title: 'name', key: 'id', value: 'id' }"
            :default-expand-all="true"
            :default-selected-keys="defaultSelectedKeys"
          >
          </a-tree>
        </a-col>
        <a-col :span="18">
          <div>
            <h2>{{ doc.name }}</h2>
            <div>
              <span>阅读数:{{ doc.viewCount }}</span>
              <span>点赞数:{{ doc.voteCount }}</span>
            </div>
            <a-divider
              style="height: 2px; background-color: #9999cc"
            ></a-divider>
          </div>
          <div class="wangeditor" :innerHTML="html"></div>
          <div class="vote-div">
            <a-button
              type="primary"
              shape="round"
              :size="'large'"
              @click="vote"
            >
              <template #icon>
                <LikeOutlined /> &nbsp; 点赞:{{ doc.voteCount }}
              </template>
            </a-button>
          </div>
        </a-col>
      </a-row>
    </a-layout-content>
  </a-layout>
</template>
<script lang="ts">
import { defineComponent, onMounted, ref } from "vue";
import axios from "axios";
import { message } from "ant-design-vue";
import type { TreeProps } from "ant-design-vue";
import { Tool } from "@/util/tool";
import { useRoute } from "vue-router";

export default defineComponent({
  name: "DocView",

  setup() {
    const docs = ref();
    const html = ref();

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
    // const treeData = ref<TreeProps["treeData"]>();
    const treeData = ref();
    treeData.value = [];
    // 获取路由路径
    // path ,query, param,fullpath,name ,meta
    const route = useRoute();

    const level1 = ref();
    level1.value = [];
    const loading = ref(false);
    const defaultSelectedKeys = ref();
    defaultSelectedKeys.value = [];

    // p 是前端的url 带的请求参数,
    // axios 的请求写法是固定的,需要带参数,就通过对象的形式,包裹到params中去的
    //  因为参数可能会很多,所以我们就结构p 中一部分的参数
    // 查询传入的参数是: 从哪一页开始查,每页是多少个
    const d = ref([]);

    // 当前选中的文档
    const doc = ref();
    doc.value = {};

    // 内容查询
    const handleQueryContent = (id: number) => {
      axios.get("/doc/find-content/" + id).then((response) => {
        const data = response.data;
        if (data.success) {
          html.value = data.content;
        } else message.error(data.message);
      });
    };
    const handleQuery = () => {
      loading.value = true;
      axios.get("/doc/all/" + route.query.ebookId).then((response) => {
        loading.value = false;
        const data = response.data;
        // 查询对page 和size 进行数据校验
        if (data.success) {
          docs.value = data.content;
          // console.log("docs.value", docs.value);
          d.value = Tool.copy(docs.value);
          if (!d.value) return;
          d.value.forEach((item: any) => {
            if (
              typeof item.parent === "string" ||
              typeof item.parent === "number"
            )
              item.parent = { parent: item.parent };
            // console.log("item", item);
          });
          // d.value.unshift({ id: 0, name: "无" });

          // treeData.value = d.value;
          level1.value = [];
          level1.value = Tool.array2Tree(docs.value, 0);
          treeData.value = Tool.array2Tree2(d.value, 0);
          if (Tool.isNotEmpty(treeData)) {
            defaultSelectedKeys.value = [treeData.value[0].id];
            handleQueryContent(treeData.value[0].id);
            // 将第一个节点的内容赋给doc
            doc.value = treeData.value[0];
          }
        } else {
          message.error(data.message);
        }
      });
    };

    const onSelect = (selectedKeys: any, info: any) => {
      console.log("selectedKeys", selectedKeys);
      if (Tool.isNotEmpty(selectedKeys)) {
        handleQueryContent(selectedKeys[0]);
        //选中某一节点,加载该节点的文档信息
        doc.value = info.selectedNodes[0];
      }
    };

    //点赞方法
    const vote = () => {
      axios.get("/doc/vote/" + doc.value.id).then((response) => {
        const data = response.data;
        if (data.success) {
          doc.value.voteCount++;
        } else {
          message.error(data.message);
        }
      });
    };

    // page ,size 是跟后端的属性相对应的
    onMounted(() => {
      handleQuery();
    });

    return {
      level1,
      onSelect,
      html,
      treeData,
      defaultSelectedKeys,
      doc,
      vote,
    };
  },
});
</script>

<style>
/* table 样式 */
.wangeditor table {
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
}

.wangeditor table td,
.wangeditor table th {
  border-bottom: 1px solid #ccc;
  border-right: 1px solid #ccc;
  padding: 3px 5px;
}

.wangeditor table th {
  border-bottom: 2px solid #ccc;
  text-align: center;
}

/* blockquote 样式 */
.wangeditor blockquote {
  display: block;
  border-left: 8px solid #d0e5f2;
  padding: 5px 10px;
  margin: 10px 0;
  line-height: 1.4;
  font-size: 100%;
  background-color: #f1f1f1;
}

/* code 样式 */
.wangeditor code {
  display: inline-block;
  *display: inline;
  *zoom: 1;
  background-color: #f1f1f1;
  border-radius: 3px;
  padding: 3px 5px;
  margin: 0 3px;
}

.wangeditor pre code {
  display: block;
}

/* ul ol 样式 */
.wangeditor ul,
ol {
  margin: 10px 0 10px 20px;
}

.wangeditor blockquote p {
  font-family: "SFMono-Regular", Consolas, "Liberation Mono", Menlo, Courier,
    monospace;
  margin: 20px 10px !important;
  font-size: 16px !important;
  font-weight: 600;
}

.vote-div {
  padding: 15px;
  text-align: center;
}
</style>
