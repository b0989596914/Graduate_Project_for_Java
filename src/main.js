import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import 'bootstrap/dist/css/bootstrap.css';
import 'bootstrap-vue-next/dist/bootstrap-vue-next.css';


const app = createApp(App);

// var isHomeActive = false;
// var isUploadActive = false;
// var isEndPointActive = false;

// app.provide('isHomeActive', isHomeActive);
// app.provide('isUploadActive', isUploadActive);
// app.provide('isEndPointActive', isEndPointActive);

// app.config.globalProperties.$isHomeActive = false;
// app.config.globalProperties.$isUploadActive = false;
// app.config.globalProperties.$isEndPointActive = false;


app.provide("global", {
  baseURL: "http://localhost:9100",
});
app.use(store).use(router).mount("#app");
