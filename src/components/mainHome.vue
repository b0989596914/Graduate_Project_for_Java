<template>
  <h2 class="title-upload">Upload Project List</h2>
  <h4 class="title-select">Select One Project and Get Visualization</h4>
  <div id="new_butt" class="dropdown">
    <button class="button-new dropdown-toggle">New </button>
    <div class="outWrap_div">
      <div class="bottom_wrap"></div>
      <div id="new_butt_content" class="dropdown-content">
        <div>
          <router-link to="/chooseFile" >Choose File</router-link>
          <router-link to="/githubUpload">Use Github URL</router-link>
        </div>
      </div>
    </div>
  </div>
  <div class="history_list">
    <div class="project-container" v-for="(item, index) in uploaded_list" :key="index">
      <button class="outside-button" @click="handleButtonClick(index-1)">
        <p class="project-number">{{ number_list[index-1] }}</p>
        <br/>
        <p class="project-name">{{uploaded_list[index]}}</p>
      </button>
    </div>
  </div>
  
</template>

<script>
import uploaded_list from "./UploadedFile_list.json";

export default {
  data() {
    return {
      file_list: "",
      uploaded_list:[],
      number_list: ["𓃥","𓃠","𓃰","𓃱", "𓃯", "𓃭", "𓃸", "𓃵", "𓃗", "𓃘", "𓃙"],
    };
  },
  created(){
    this.readUploadedFile_list();
  },
  methods: {
    handleButtonClick(index) {      
      this.$router.push("/endPoint");

    },
    readUploadedFile_list() {
      this.uploaded_list = uploaded_list[0];
    }
  },
};
</script>

<style>
.history_list {
  text-align: center;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  margin-left: 20%;
  margin-right: 20%;
}

.outWrap_div {
  position: absolute;
  display: block;
}

.bottom_wrap {
  display: none;
  min-width: 180px;
  height: 115px;
  margin-top: 20px;
  margin-left: -40px;
  
  background-color: #B0BEC5;
  border-radius: 20px;
  position: absolute;
  z-index: 1;
}

#new_butt {
  position: absolute;
  display: inline-block;
  margin-left: 20%;
  margin-top: -2%;
  border-radius: 15px;
}

#new_butt_content {
  display: none;
  position: absolute;
  background-color: #f1f1f1;
  min-width: 160px;
  z-index: 1;
  border-radius: 15px;
  margin-top: 30px;
  margin-left: -30px;
}

#new_butt_content a {
  padding: 12px 16px;
  text-decoration: none;
  display: block;
  border-radius: 15px;
}

#new_butt_content a:hover {
  background-color: #ddd;
  border-radius: 15px;
}

#new_butt:hover #new_butt_content {
  position: absolute;
  display: block;
  border-radius: 15px;
}

#new_butt:hover .bottom_wrap {
  position: absolute;
  display: block;
  border-radius: 15px;
}

.project-name{
  font-size: 18px;
  /* margin-left: 20%; */
  text-align: center;
}

.project-number{
  font-size: 50px;
  color: #546E7A;
}

.project-container{
  width: calc(33.33% - 10px); /* 三列布局，10px是列间距 */
  margin-bottom: 20px; /* 控制行间距 */
  padding: 10px;
  box-sizing: border-box; /* 防止边框和填充导致元素超出容器 */
  margin-top: 15px; 
}

.outside-button{
  background-color: #cfd8dc;
  min-height: 150px;
  min-width: 250px;
  text-align: center;
  position: relative;
  
  margin-left: 5%;
  padding: 10px;
  border-radius: 20px;
  box-shadow: 0 0px 3px rgba(0, 0, 0, 0.27);
}

.button-new{
  margin-left: 18%;
  background-color: #546E7A;
  color: #ffffff;
  height: 50px;
  margin-top: -25px;
  font-size: 18px;
  border-radius: 15px;
  text-align: center;
  position: absolute;
}

.dropdown-menu {
  position: relative;
}

.dropdown-item {
  position: fixed;
}

.title-upload{
  color: #37474F;
  margin-top: 5%;
  margin-left: 40%;
}

.title-select{
  color: #78909C;
  margin-left: 35%;
}

.text_align {
  text-align: center;
}

ol {
  margin: 0;
  padding: 0;
  list-style-position: inside;
  list-style-type: decimal;
  margin-left: 20px;
}

* {
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
}
</style>
