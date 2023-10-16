<template>
  <div class="about">
      <Mermaidtest />
  </div>
</template>

<script>
// @ is an alias to /src
import Mermaidtest from "@/components/mermaid_test.vue";
// import Mermaid from "mermaid";
// import content from "";

export default {
  name: "AboutView",
  components: {
    Mermaidtest,
  },
};
</script>

<style>
.about {
  display: flex;
}

.left {
  flex: 1;
}

.right {
  flex: 1;
}
/* .about {
  text-align: center;
  margin: 20px;
} */

h1 {
  font-size: 24px;
  color: #333;
}

p {
  font-size: 16px;
  color: #666;
}
</style>
