<template>
  <a-layout>
    <a-layout-sider width="200" style="background: #fff">
      <a-menu
        mode="inline"
        :style="{ height: '100%', borderRight: 0 }"
        @click="handleClick"
      >
        <!--        v-model:openKeys="openKeys"-->
        <a-menu-item key="welcome">
          <Mail-outlined />
          欢迎
        </a-menu-item>
        <a-sub-menu v-for="item in level1" :key="item.id">
          <template v-slot:title>
            <span><user-outlined />{{ item.name }}</span>
          </template>
          <a-menu-item v-for="child in item.children" :key="child.id">
            <MailOutlined />
            <span>{{ child.name }}</span>
          </a-menu-item>
        </a-sub-menu>
      </a-menu>
    </a-layout-sider>
    <a-layout-content
      :style="{
        background: '#fff',
        padding: '24px',
        margin: 0,
        minHeight: '280px',
      }"
    >
      <div class="welcome" v-show="isShowWelcome">
        <h1>欢迎使用jess知识库</h1>
      </div>
      <a-list
        v-show="!isShowWelcome"
        item-layout="vertical"
        size="large"
        :data-source="ebooks"
        :grid="{ gutter: 20, column: 3 }"
      >
        <template #renderItem="{ item }">
          <a-list-item key="item.name">
            <template #actions>
              <span v-for="{ icon, text } in actions" :key="text">
                <component :is="icon" style="margin-right: 8px" />
                {{ text }}
              </span>
            </template>

            <a-list-item-meta :description="item.description">
              <template #title>
                <a :href="item.href">{{ item.name }}</a>
              </template>
              <template #avatar>
                <a-avatar :src="item.cover" />
              </template>
            </a-list-item-meta>
          </a-list-item>
        </template>
      </a-list>
    </a-layout-content>
  </a-layout>
</template>

<script lang="ts">
import { defineComponent, onMounted, reactive, ref, toRef } from "vue";
import axios from "axios";
import {
  StarOutlined,
  LikeOutlined,
  MessageOutlined,
} from "@ant-design/icons-vue";
import { message } from "ant-design-vue";
import { Tool } from "@/util/tool";

const listData: Record<string, string>[] = [];

// for (let i = 0; i < 23; i++) {
//   listData.push({
//     href: "https://www.antdv.com/",
//     title: `ant design vue part ${i}`,
//     avatar: "https://joeschmoe.io/api/v1/random",
//     description:
//       "Ant Design, a design language for background applications, is refined by Ant UED Team.",
//     content:
//       "We supply a series of design principles, practical patterns and high quality design resources (Sketch and Axure), to help people create their product prototypes beautifully and efficiently.",
//   });
// }
export default defineComponent({
  name: "HomeView",

  setup() {
    console.log("setup");
    const ebooks = ref();
    // const ebooks1 = reactive({ books: [] });
    const isShowWelcome = ref(true);

    let categorys: any;
    const level1 = ref();

    const handleQueryCategory = () => {
      axios.get("/category/all").then((response) => {
        const data = response.data;
        if (!data.success) return message.error(data.message);

        categorys = data.content;
        console.log("原始数组", categorys);
        level1.value = [];
        level1.value = Tool.array2Tree(categorys, 0);
        console.log("树形数组", level1.value);
        // 加载完分类后，再加载电子书，否则如果分类树加载很慢，则电子书渲染会报错
      });
    };
    const handleClick = (value: any) => {
      // console.log("点击菜单", value);
      isShowWelcome.value = value.key === "welcome";
    };

    onMounted(() => {
      handleQueryCategory();
      axios
        .get("/ebook/list", {
          params: {
            page: 1,
            size: 1000,
          },
        })
        .then((response) => {
          const data = response.data;
          ebooks.value = data.content.list;
          // ebooks1.books = data.content;
          // console.log(response);
        });
    });
    return {
      isShowWelcome,
      handleClick,
      level1,
      ebooks,
      // book: toRef(ebooks1, "books"),
      listData,
      pagination: {
        onChange: (page: number) => {
          console.log(page);
        },
        pageSize: 3,
      },
      actions: [
        { icon: StarOutlined, text: "156" },
        { icon: LikeOutlined, text: "156" },
        { icon: MessageOutlined, text: "2" },
      ],
    };
  },
});
</script>
<style scoped>
.ant-avatar {
  width: 50px;
  height: 50px;
  line-height: 50px;
  border-radius: 8%;
  margin: 5px 0;
}
</style>
