<template>
  <div class="codedisplay">
    <h1>選擇程式碼</h1>
    <CodeDisplay />
  </div>
</template>

<script>
// @ is an alias to /src
import CodeDisplay from "@/components/CodeDisplay.vue";

export default {
  name: "CodeDisplayView",
  components: {
    CodeDisplay,
  },
};
</script>

<style>
.showCode {
  text-align: center;
  margin: 20px;
}

h1 {
  font-size: 24px;
  color: #333;
}

p {
  font-size: 16px;
  color: #666;
}
</style>
