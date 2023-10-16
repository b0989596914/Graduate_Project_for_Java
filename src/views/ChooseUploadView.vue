<template>
  <div id="upload_back">
    <OriginalFileUpload :accept="inputContent" @can_touch_button="getValFromChild"></OriginalFileUpload>
    <!-- <OriginalFileUpload /> -->
    <!-- <p style="color:#dd2020">接收子層內容：{{inputValFromChild}}</p> -->
    <!-- <button class="page-btn" @click="gohome">＜＜ Home</button> -->
    <!-- <button class="page-btn" v-if="inputValFromChild==true" @click="endPoint">Show EndPoint ＞＞</button> -->
  </div>
</template>

<script>
// @ is an alias to /src
import OriginalFileUpload from "@/components/OriginalFileUpload.vue";

export default {
  name: "ChooseUploadView",
  components: {
    OriginalFileUpload,
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

#upload_back {
  background-color: #CFD8DC;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  position: absolute;
  z-index: -1; /* 確保背景 navbar 下面 */
}
</style>
