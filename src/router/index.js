import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import ChooseFile from "../views/ChooseUploadView.vue";
import GithubFile from "../views/GithubUploadView.vue";
import EndPoint from "../views/EndPointView.vue";
import CodeDisplayView from "../views/CodeDisplayView.vue";
import CodeDisplay from "../components/CodeDisplay.vue";
import About from "../views/AboutView.vue"
import MermaidTest from "../components/mermaid_test.vue";


const routes = [
  {
    path: "/about",
    name: "About",
    component: About,
  },
  {
    path: '/about/:type/:messageToChild',
    name: 'MermaidTest',
    component: MermaidTest,
    props: true,
  }, 
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path: "/chooseFile",
    name: "chooseFile",
    component: ChooseFile,
  },
  {
    path: "/githubUpload",
    name: "githubUpload",
    component: GithubFile,
  },
  {
    path: "/endPoint",
    name: "endPoint",
    component: EndPoint,
  },
  {
    path: "/codedisplay",
    name: "codedisplay",
    component: CodeDisplayView,
  },
  { 
    path: '/codedisplay/:class/:method/:type/:messageToChild',
    name: 'code',
    component: CodeDisplay,
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});


export default router;
