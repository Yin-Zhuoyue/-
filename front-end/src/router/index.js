import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '../views/Login.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: Login
  },
  {
    path: '/task',
    name: 'task',
    component: () => import('../views/TaskView.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
];

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path === '/login') {
    next();
  } else {
    let token = window.sessionStorage.getItem('token');

    if (token === null || token === '') {
      next('/login');
    } else {
      next();
    }
  }
});
export default router
