<template>
  <a-layout-header class="header">
    <div class="logo" />
    <a-menu theme="dark" mode="horizontal" :style="{ lineHeight: '64px' }">
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user">
        <router-link to="/admin/user"> 用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/ebook">
        <router-link to="/admin/ebook"> 电子书管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category">
        <router-link to="/admin/category"> 分类管理</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about"> 关于我们</router-link>
      </a-menu-item>
    </a-menu>
    <a class="login-menu" v-show="user.id">
      <span>您好:{{ user.name }}</span>
    </a>
    <a class="login-menu" @click="showLoginModal" v-show="!user.id">
      <span>登录</span>
    </a>
    <a-popconfirm
      title="确认退出登录?"
      ok-text="Yes"
      cancel-text="No"
      @confirm="logout()"
    >
      <a class="login-menu" v-show="user.id">
        <span>退出登录</span>
      </a>
    </a-popconfirm>

    <a-modal
      title="登录"
      v-model:visible="loginModalVisible"
      :confirm-loading="loginModalLoading"
      @ok="login"
    >
      <a-form
        :model="loginUser"
        :label-col="{ span: 6 }"
        :wrapper-col="{ span: 18 }"
      >
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.loginName"></a-input>
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password"></a-input>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-around;
}

.logo {
  width: 120px;
  height: 31px;
  background: rgba(255, 255, 255, 0.3);
}

.login-menu {
  color: #cccccc;
}
</style>
<script lang="ts">
import { computed, defineComponent, ref } from "vue";
import axios from "axios";
import { message } from "ant-design-vue";
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: "TheHeader",

  setup() {
    // 登录的
    const loginUser = ref({
      loginName: "test",
      password: "test",
    });
    // 登录后保存用户信息
    // 这个就是从后端redis 中获取的
    // 给它初始化{},可以防止空指针异常
    // const user = ref();
    // user.value = {};
    const user = computed(() => store.state.user);
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };
    // 登录
    const login = () => {
      // console.log("开始登录");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password + KEY);
      axios.post("/user/login", loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功");
          store.commit("setUser", data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    // 退出登录
    const logout = () => {
      console.log("退出登录");

      axios.get("/user/logout/" + user.value.token).then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("退出登录");
          store.commit("setUser", {});
        } else {
          message.error(data.message);
        }
      });
    };

    return {
      login,
      loginUser,
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      user,
      logout,
    };
  },
});
</script>
