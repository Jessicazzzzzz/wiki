<template>
  <a-layout-footer style="text-align: center">
    Jess电子书 {{ user.name }}
  </a-layout-footer>
</template>

<style scoped></style>
<script lang="ts">
import { defineComponent, computed, onMounted } from "vue";
import store from "@/store";
import { Tool } from "@/util/tool";

export default defineComponent({
  name: "TheFooter",
  setup() {
    const user = computed(() => store.state.user);
    let websocket: any;
    let token: any;
    const onOpen = () => {
      console.log("websocket 连接成功,状态码:", websocket.readyState);
    };
    const onMessage = (event: any) => {
      console.log("websocket 收到消息:", event.data);
    };
    const onError = () => {
      console.log("websocket 连接错误,状态码:", websocket.readyState);
    };
    const onClose = () => {
      console.log("websocket 连接关闭,状态码:", websocket.readyState);
    };
    const initWebSocket = () => {
      websocket.onopen = onOpen;
      websocket.onmessage = onMessage;
      websocket.onerror = onError;
      websocket.onclose = onClose;
    };
    onMounted(() => {
      if ("WebSocket" in window) {
        // 检查浏览器是否支持websocket
        token = Tool.uuid(10);
        websocket = new WebSocket(
          process.env.VUE_APP_WS_SERVER + "/ws/" + token
        );
        initWebSocket();
      } else {
        alert("当前浏览器 不支持");
      }
    });
    return {
      user,
    };
  },
});
</script>
