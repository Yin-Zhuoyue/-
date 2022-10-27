import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

axios.defaults.baseURL = '/api';
Vue.config.productionTip = false;
Vue.prototype.$axios = axios;
Vue.use(ElementUI);
new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app');
// 添加请求拦截器，在请求头中加token
axios.interceptors.request.use(
    config => {
        if (window.sessionStorage.getItem('token')) {
            config.headers.token = window.sessionStorage.getItem('token')
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    });