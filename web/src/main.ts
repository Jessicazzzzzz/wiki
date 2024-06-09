import { createApp } from "vue";
import Antd from "ant-design-vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "ant-design-vue/dist/reset.css";
import * as Icons from "@ant-design/icons-vue";

const app = createApp(App);
app.use(Antd).use(store).use(router).mount("#app");
// 全局注册 icons
// 相当于是全局注册组件
const icons: any = Icons;
for (const i in icons) {
  app.component(i, icons[i]);
}
