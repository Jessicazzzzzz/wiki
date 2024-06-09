import { createApp } from "vue";
import Antd from "ant-design-vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import "ant-design-vue/dist/reset.css";
import * as Icons from "@ant-design/icons-vue";
import axios from "axios";
// 全局配置axios 的域
axios.defaults.baseURL = process.env.VUE_APP_SERVER;
const app = createApp(App);
app.use(Antd).use(store).use(router).mount("#app");
// 全局注册 icons
// 相当于是全局注册组件
const icons: any = Icons;
for (const i in icons) {
  app.component(i, icons[i]);
}
console.log("环境", process.env.NODE_ENV);
console.log("环境", process.env.VUE_APP_SERVER);
