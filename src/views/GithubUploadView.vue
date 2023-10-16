<template>
  <div id="github_back">
    <div>
      <GithubUrlUpload  :accept="inputContent" @can_touch_button="getValFromChild"></GithubUrlUpload>
      <!-- <div class="show_btn">
        <button class="page-btn home-button" @click="gohome">◀ Home</button>
        <button class="page-btn toendpoint-button" v-if="inputValFromChild==true" @click="endPoint">Show EndPoint ＞＞</button>
        <button class="page-btn toendpoint-button" @click="endPoint">Show EndPoint ▶</button>
      </div> -->
    </div>
  </div>
</template>

<script>
// @ is an alias to /src
import GithubUrlUpload from "@/components/GithubUrlUpload.vue";

export default {
  name: "GithubUploadView",
  components: {
    GithubUrlUpload,
  },
  data() {
    return {
      inputContent: "",
      inputValFromChild: false
    } 
  },
  methods: {
    getValFromChild(val) {
      this.inputValFromChild = val;
    },
    gohome() {
      // sessionStorage.setItem()
      this.$router.push("/");
    },
    endPoint() {
      this.$router.push("/endPoint");
    },
  },
};
</script>

<style>

#github_back {
  background-color: #CFD8DC;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  position: absolute;
  z-index: -1; /* 確保背景 navbar 下面 */
}

</style>
