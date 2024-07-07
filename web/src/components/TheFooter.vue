<template>
  <a-layout-footer style="text-align: center">
    Jess电子书 {{ user.name }}
  </a-layout-footer>
</template>

<style scoped></style>
<script>
import { defineComponent, computed } from "vue";
import store from "@/store";

export default defineComponent({
  name: "TheFooter",
  setup() {
    const user = computed(() => store.state.user);
    return {
      user,
    };
  },
});
</script>
