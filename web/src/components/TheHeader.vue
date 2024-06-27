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
    <a class="login-menu" @click="showLoginModal">
      <span>登录</span>
    </a>
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
<script>
import { defineComponent, ref } from "vue";

export default defineComponent({
  name: "TheHeader",
  setup() {
    const loginUser = ref({
      loginName: "test",
      password: "test",
    });

    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };
    // 退出登录
    const login = () => {
      console.log("开始登录");
    };

    return {
      login,
      loginUser,
      loginModalVisible,
      loginModalLoading,
      showLoginModal,
    };
  },
});
</script>
