<template>
<div class="codeDisplay-container" >
  
  <h2 class="title-style">Method Code</h2>
  <div class="line">Now is in : {{className}}(class)  {{method}}(method)</div>
    <div class="inside">
        <div>
            
            <div>
                <pre><code ref="highlightCode" class="java">{{ fileContent }}</code></pre>
            </div>
        </div>
    </div>
    <button class="goToMermaid" @click="goToMermaid()"> ◀ Back </button>
    <button class="goToEndpoint" @click="goToEndpoint()"> ◀◀ Go To Endpoint </button>
</div>
</template>


<script>
import hljs from "highlight.js/lib/core"; // 導入 highlight.js 的資料庫
import java from "highlight.js/lib/languages/java"; // 導入 Java 語言的 highlight.js 模組
// import "highlight.js/styles/monokai-sublime.css";
// import "highlight.js/styles/github-dark-dimmed.css";
// import "highlight.js/styles/gml.css";
// import "highlight.js/styles/github.css";
import "highlight.js/styles/devibeans.css";
// import "highlight.js/styles/androidstudio.css"; // 很像 intelliJ 裡面的 dark
// import "highlight.js/styles/an-old-hope.css";
// import "highlight.js/styles/panda-syntax-dark.css";

hljs.registerLanguage("java", java);

export default {
  data() {
    return {
      className: '',
      method: '',
      fileContent: null, // 顯示的 Java 程式碼
    };
  },
  created() {
    // 在这里获取路由参数
    const codeClass = this.$route.params.class;
    const codeMethod = this.$route.params.method;
    

    // 在这里根据路由参数加载不同的程式码内容
    this.loadCode(codeClass, codeMethod);
  },
  computed: {
    // 在這裡可以對收到的 Props 資料進行處理，然後返回處理後的結果
    processedMessage() {
      return this.message;
    },
  },
  watch: {
    fileContent() {
      if (this.$refs.highlightCode) {
        hljs.highlightBlock(this.$refs.highlightCode);
      }
    },
  },
  methods: {
    goToEndpoint() {
      // sessionStorage.setItem()
      this.$router.push({name:'endPoint'});
    },
    goToMermaid() {
      const messageToChild = this.$route.params.messageToChild;
      const type = this.$route.params.type;
      this.$router.push({ name: 'MermaidTest', params: { type,messageToChild } });
    },

    async loadCode(classParam, methodParam) {
      this.className = classParam ;
      this.method = methodParam;

      if (classParam != null && methodParam != null)
        try {
          const response = await fetch(
            "/showCode/" +
              classParam +
              "/" +
              methodParam +
              ".txt"
          );

          var script = document.createElement("script");
          script.onload = () => window.hljs.initHighlighting();
          script.src =
            "//cdnjs.cloudflare.com/ajax/libs/highlight.js/9.7.0/highlight.min.js";
          document.body.appendChild(script);

          if (response.ok) {
            this.fileContent = await response.json();
          } else {
            console.error("Error loading file:", response.statusText);
          }
        } catch (error) {
          console.error("Error reading file:", error);
        }
    },
  },
};
</script>

<style>
.goToMermaid{
  background-color: #78909C;
  color: #fff;
  border-radius: 15px;
  font-size: 100%;
  margin-left: 15%;
  margin-bottom: 2%;
}
.goToEndpoint{
  background-color: #78909C;
  color: #fff;
  border-radius: 15px;
  font-size: 100%;
  margin-left: 45%;
  margin-bottom: 2%;
}
.line{
  color: #fff;
  font-weight: lighter;
  margin-left: 6%;
}
.codeDisplay-container {
  max-width: 1200px;
  max-height: 500%;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 5%;
  background-color: #B0BEC5;
  margin-top: 50px;
  min-height: 200px;
  text-align: left;
  position: relative;
  padding: 20px;
  border-radius: 20px;
}
.title-style {
  color: #455A64;
  font-weight: bold;
  margin-top: 5%;
  margin-left: 6%;
} 
 .inside {
  text-align: left;
  max-width: 1200px;
  margin-top: 2%;
  margin-left: 5%;
  margin-right: 5%;
  margin-bottom: 5%;
  background-color: #eceff1;
  min-height: 200px;
  text-align: left;
  position: relative;
  padding: 20px;
  border-radius: 20px;
}
</style>