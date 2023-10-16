<template>
  <div class="endpoint-container">
    <div>
      <strong><h2 class="title-style">Click Button and Get into API👉</h2></strong>
    </div>
    <div id="output" class="div-output" v-if="showContent === 'default'">
    
      <span v-for="(line, index) in outputData" :key="index">
        <template v-if="getAnnotationClass(line) !== 'normal-style'">
          <span class="line-break"></span>
          <span v-if="getAnnotationClass(line) === 'post-style'">
            <button
              :id="'button-' + index"
              class="post-button inline-button control-position"
              @click="handleButtonClick(index, 'page' , 'Post')"
              
            >
              {{ line }}
            </button>
          </span>
          <span v-else-if="getAnnotationClass(line) === 'get-style'">
            <button
              :id="'button-' + index"
              class="get-button inline-button control-position"
              @click="handleButtonClick(index, 'page' , 'Get')"
            >
              {{ line }}
            </button>
          </span>
          <span v-else-if="getAnnotationClass(line) === 'put-style'">
            <button
              :id="'button-' + index"
              class="put-button inline-button control-position"
              @click="handleButtonClick(index, 'page' , 'Put')"
            >
              {{ line }}
            </button>
          </span>
          <span v-else-if="getAnnotationClass(line) === 'delete-style'">
            <button
              :id="'button-' + index"
              class="delete-button inline-button control-position"
              @click="handleButtonClick(index, 'page' , 'Delete')"
            >
              {{ line }}
            </button>
          </span>
        </template>
        <span v-else class="new-line  endpoint-size" >
          {{ line }}
        </span>
      
      </span>
    </div>
    <div v-if="showContent === 'page'" >
      <div v-for="item in show_child" :key="item" >{{ item }}</div>
      <button @click="back_to_default()">back</button>
    </div>
    <button class="home-button" @click="homeTo()"> ◀ Home </button>
  </div>
</template>

<script>
import data from "./AST_output.json";
import ChildComponent from "@/components/mermaid_test.vue";

const output = [];
var output_count = 0;

export default {
  components: {
    ChildComponent,
  },
  data() {
    return {
      outputData: "",
      loadedData: null,
      showContent: "default",
      show_child: "",
    };
  },
  mounted() {
    this.loadJSON();
  },
  methods: {
    homeTo() {
      // sessionStorage.setItem()
      this.$router.push({name:'home'});
    },
    back_to_default() {
      this.showContent = "default";
      this.show_child = "";
    },
    handleButtonClick(index, page ,type) {
       this.showContent = page;
      if (output[index / 2] != undefined) {
        if (index == 0) {
          const parts = output[0].split(" - ");
          this.show_child = parts;
        } else {
          const parts = output[index / 2].split(" - ");
          this.show_child = parts;
        }
        var messageToChild = index / 2;
        console.log(messageToChild);
        this.$router.push({ name: 'MermaidTest', params: { type , messageToChild} });
      }
      // 在這裡可以處理按下按鈕的邏輯
      // console.log(output);
    },
    getAnnotationClass(line) {
      if (line === "Post") {
        return "post-style";
      } else if (line === "Get") {
        return "get-style";
      } else if (line === "Put") {
        return "put-style";
      } else if (line === "Delete") {
        return "delete-style";
      } else {
        return "normal-style";
      }
    },
    displayHierarchy(item) {
      const hierarchy = [];
      var hierarchy_count = 0;

      var annotations = item.annotations;
      var methodName = item.methodName;
      var className = item.className;
      var endpoint = item.endpoint;
      var childMethods = item.child_methods;

      //避免childMethods輸出不必要資料
      if (annotations !== undefined) {
        hierarchy[hierarchy_count++] = annotations;
      }
      if (methodName !== undefined) {
        hierarchy[hierarchy_count++] = methodName;
        output[output_count] = "";
        output[output_count] += "Method Name: " + methodName;
      }
      if (className !== undefined) {
        output[output_count] +=
          " - Class Name: " + childMethodClassName + " - ";
        hierarchy[hierarchy_count++] = className;
      }
      if (endpoint !== undefined) {
        hierarchy[hierarchy_count++] = endpoint;
      }

      //處理childMethods那堆括號
      if (childMethods && childMethods.length > 0) {
        for (var j = 0; j < childMethods.length; j++) {
          var childMethod = childMethods[j];
          var childMethodName = childMethod.methodName;
          var childMethodClassName = childMethod.method_className;
          var grandchildMethods = childMethod.child_methods;

          if (
            childMethodName !== undefined &&
            childMethodClassName !== undefined
          ) {
            if (!output[output_count]) {
              output[output_count] = ""; // 初始化 output[output_count]
            }
            output[output_count] +=
              "Method Name: " +
              childMethodName +
              " - Class Name: " +
              childMethodClassName +
              " - ";
          }

          if (grandchildMethods && grandchildMethods.length > 0) {
            if (!output[output_count]) {
              output[output_count] = ""; // 初始化 output[output_count]
            }
            output[output_count] += this.displayHierarchy({
              child_methods: grandchildMethods,
            });
          }
        }
      }
      hierarchy_count = 0;
      return hierarchy;
    },
    loadJSON: function () {
      let output_lines = [];

      for (var i = 0; i < data.length; i++) {
        var hierarchyArray = this.displayHierarchy(data[i]);
        output_lines.push(hierarchyArray[0]);
        output_lines.push(hierarchyArray[3]);
        output_count++;
      }

      this.outputData = output_lines;
    },
  },
};
</script>

<style>
.link{
  color: #fff;
}
.home-button{
  background-color: #78909C;
  color: #fff;
  border-radius: 15px;
  font-size: 100%;
  margin-left: 45%;
  margin-bottom: 2%;
}
.endpoint-container {
  max-width: 1200px;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 2%;
  background-color: #cfd8dc;
  margin-top: 40px;
  min-height: 200px;
  text-align: left;
  position: relative;
  padding: 2px;
  border-radius: 20px;
}
.title-style {
  color: #455a64;
  font-weight: bold;
  margin-left: 6%;
}
.div-output {
  text-align: left;
  max-width: 1200px;
  margin-top: 2%;
  margin-left: 5%;
  margin-right: 5%;
  margin-bottom: 2%;
  background-color: #eceff1;
  min-height: 200px;
  text-align: left;
  position: relative;
  padding: 20px;
  border-radius: 20px;
}
.output-line {
  text-align: left;
}
.line-break {
  display: block;
}
.inline-button {
  display: inline;
  white-space: nowrap;
}
.new-line {
  display: inline-block;
  width: 320px;
  margin-left: 5px;
}
.control-position {
  margin-left: 3%;
  margin-right: 8%;
  font-size: 130%;
}
/* POST */
.post-button {
  background-color: #FFE57F;
  color: #455a64;
  border-radius: 10px;
  padding: 5px 10px;
  width: 10%;
  margin-bottom: 2%;
}

/* GET */
.get-button {
  background-color: #B2EBF2;
  color: #455a64;
  border-radius: 10px;
  padding: 5px 10px;
  width: 10%;
  margin-bottom: 2%;
}

/* PUT */
.put-button {
  background-color: #cef2b2;
  color: #455a64;
  border-radius: 10px;
  padding: 5px 10px;
  width: 10%;
  margin-bottom: 2%;
}

/* DELETE */
.delete-button {
  background-color: #f1b7b7;
  color: #455a64;
  border-radius: 10px;
  padding: 5px 10px;
  width: 10%;
  margin-bottom: 2%;
}
.endpoint-size{
  font-size: 130%;
}
</style>
