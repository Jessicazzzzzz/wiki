import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import HomeView from "../views/HomeView.vue";
import AboutView from "@/views/AboutView.vue";
import AdminEbook from "@/views/admin/Admin-ebook.vue";
import AdminCategory from "@/views/admin/Admin-category.vue";
import AdminDoc from "@/views/admin/Admin-doc.vue";
import Doc from "@/views/DocView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/about",
    name: "about",
    component: AboutView,
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    // component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: "/doc",
    name: "Doc",
    component: Doc,
  },
  {
    path: "/admin/ebook",
    name: "AdminEbook",
    component: AdminEbook,
  },
  {
    path: "/admin/doc",
    name: "AdminDoc",
    component: AdminDoc,
  },
  {
    path: "/admin/category",
    name: "AdminCategory",
    component: AdminCategory,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
