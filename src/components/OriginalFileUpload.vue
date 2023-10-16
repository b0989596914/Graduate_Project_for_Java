<template>
<div>
  <div class="title-con">
    <h1 class="bold-text">File Upload</h1>
    <p style="font-size: 25px;">Choose ZIP File<br>and<br>Wait for a while</p>
  </div>
  <div>
    <button class="page-btn gogoHome" @click="gohome()"> ◀ Home </button>
    <!-- 以下為防呆按鈕 -->
    <button class="page-btn toendpoint-button"  v-if="can_touch_button == true" @click="endPoint()" >Show EndPoint ▶ </button>
      
  </div>
  <div class="upload-con">
    <div>
        <div class="single-upload_ori">
          <form 
            id="singleUploadForm"
            name="singleUploadForm"
            @submit.prevent="submitForm" 
          >
            <div class="upload_ori_background">
              <br />
              <label class="custom-file-upload">
                <input
                  id="singleFileUploadInput"
                  type="file"
                  name="file"
                  class=""
                  required
                  @change="handleFileChange($event)"
                />
                <!-- 有上傳資料，顯示資料，且已submit -->
                <div class="white-dash_have_submit" v-if="is_choose_file==2">
                  <p class="icon-upload">🗂</p>
                  <p class="Drag-file">{{file_name_show}}</p>
                  <div class="input-file-button_have_submit">file have been chosen, thank you</div>
                </div>
                <!-- 有上傳資料，顯示資料 -->
                <div class="white-dash_have_file" v-if="is_choose_file==1">
                  <p class="icon-upload">📁</p>
                  <p class="Drag-file">{{file_name_show}}</p>
                  <div class="input-file-button_have_file">file have been submitted, please submit</div>
                </div>
                <!-- 未上傳資料，顯示點選上傳區塊 -->
                <div class="white-dash" v-if="is_choose_file==0">
                  <p class="icon-upload">⮉</p>
                  <p class="Drag-file">Drag File Here</p>
                  <div class="input-file-button">Select file from your computer</div>
                </div>
                
              </label>              
            </div>
            <div class="button_right_back_ori_upload">
              <button type="submit" class="upload-submit-btn" id="submit_button">
                  Submit
              </button>
            </div>
          </form>
        </div>
        <hr />
        <div class="upload-response">
          <div id="singleFileUploadError">{{ uploadError }}</div>
          <div id="singleFileUploadSuccess" v-html="uploadSuccess"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      fileInput: null,
      uploadError: "",
      uploadSuccess: "",
      checkResult: "",
      can_touch_button: false,
      is_choose_file: 0,
      file_name_show: "", 
    };
  },
  methods: {
    gohome() {
      // sessionStorage.setItem()
      this.$router.push("/");
    },
    endPoint() {
      this.$router.push("/endPoint");
    },
    submitForm() {
      if (!this.fileInput) {
        this.uploadError = "Please select a file";
      } else {
        this.uploadSingleFile(this.fileInput);        
      }
    },
    uploadSingleFile(file) {
      var formData = new FormData();
      formData.append("file", file);

      var xhr = new XMLHttpRequest();
      xhr.open("POST", "/uploadFile");
      xhr.onload = () => {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if (xhr.status === 200) {
          // console.log(response.success_unzip);
          console.log(response.have_json);
          this.check(response.success_unzip, response.flag, response.have_json);

          if (this.checkResult == "yes") {
            this.uploadError = "";
            this.uploadSuccess = `<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='${response.fileDownloadUri}' target='_blank'>${response.fileDownloadUri}</a></p>`;
            this.can_touch_button = true;
            this.is_choose_file = 2;
          } else {
            (this.fileInput = null), (this.githubUrlInput = "");
            this.gitBranch = "master";
            this.uploadError = "";
            this.uploadSuccess = "";
            this.checkResult = "yes";
            alert("發生錯誤，請重新上傳專案 !!");
          }
        } else {
          console.log("error");
          this.uploadSuccess = "";
          this.uploadError =
            (response && response.message) || "Some Error Occurred";
          }
          if (this.can_touch_button == false) {
            this.gitBranch = "master";
            this.checkResult = "yes";
            alert("專案錯誤，請檢查是否為 SpringBoot 專案，並重新上傳專案 !!");
          }
          
          this.$emit("can_touch_button",this.can_touch_button);
        };
        
        xhr.send(formData);
    },
    check(getIsSuccess, getFlag,getHave_json) {
      if (getIsSuccess === "1") {
        // 有檔案
        if (getFlag === "1" && getHave_json === "1") {
          this.checkResult = "yes";
        } else {
          this.checkResult = "no";
        }
      } else if (getIsSuccess === "0") {
        this.checkResult = "error";
      } else if (getIsSuccess === "-1") {
        this.checkResult = "empty";
      }
    },
    handleFileChange(event) {
      var validFileExtensions = ["zip"]; // 可接受的文件類型，例如 .zip（可以添加更多檔案類型至array中）
      var file = event.target.files[0];
      

      if (file) {
        
        var fileName = file.name;
        var fileExt = fileName.split(".").pop(); // 獲取檔案類型

        if (!validFileExtensions.includes(fileExt)) {
          alert(
            "Invalid file type. Acceptable file types: " +
              validFileExtensions.join(", ")
          );
          this.fileInput = null;          
        } else {
          this.fileInput = file;
          this.is_choose_file = 1;
          this.file_name_show = fileName;
        }
      }
    },
  },
};
</script>

<style>

.button_right_back_ori_upload {
  padding: 3px;
  margin: 2%;
  text-align: right;
}

.upload-submit-btn{
  background-color: #ff8a65;
  color: #ffffff;
  min-width: 100px;
  border-radius: 10px;
  margin-left: 55%;
}
.bold-text {
  font-weight: bold; /* 将字体加粗 */
  font-size: 45px;
}
.icon-upload{
  color: #455A64;
  scale: 3;
  margin-top: 6%;
  margin-left: 50%;
  margin-right: 50%;
}
.Drag-file{
  color: #90A4AE;
  margin-top: 2%;
  font-size: 20px;
}

.white-dash{
  border: 3px dashed #fff;
  /* padding:10px; */
  border-radius: 20px;
  height: 200px;
  min-width: 60%;
  margin-left: -75%;
  margin-right: -75%;
  /* margin-top: 3%; */
  margin-bottom: 12%;
}

.white-dash_have_file {
  border: 3px dashed #fff;
  /* padding:10px; */
  border-radius: 20px;
  height: 200px;
  min-width: 50%;
  margin-left: -48%;
  margin-right: -48%;
  /* margin-top: 3%; */
  margin-bottom: 12%;
}

.white-dash_have_submit {
  border: 3px dashed #fff;
  /* padding:10px; */
  border-radius: 20px;
  height: 200px;
  min-width: 55%;
  margin-left: -65%;
  margin-right: -65%;
  /* margin-top: 3%; */
  margin-bottom: 12%;
}

.upload_ori_background {
  background-color: #d9d9d9;
  border-radius: 20px;
  margin: 2%;
  padding: 5px;
  /* margin-top: 3%; */
}

.input-file-button{
  color: #fff;
  background-color: #546E7A;
  border-radius: 20px;
  padding: 6px 12px;
  width: 40%;
  margin-left: 30%;
  margin-top: 4%;
  position: relative;
}

.input-file-button_have_file {
  color: #fff;
  background-color: #546E7A;
  border-radius: 20px;
  padding: 6px 12px;
  width: 60%;
  margin-left: 20%;
  margin-top: 4%;
  position: relative;
}

.input-file-button_have_submit {
  color: #fff;
  background-color: #546E7A;
  border-radius: 20px;
  padding: 6px 12px;
  width: 50%;
  margin-left: 25%;
  margin-top: 4%;
  position: relative;
}

input[type="file"] {
    display: none;
}

.custom-file-upload {
    /* border: 1px solid #ccc; */
    /* display: inline-block; */
    padding: 6px 12px;
    cursor: pointer;
    /* background-color: #546E7A; */
    /* margin-top: 8%; */
    /* margin-bottom: 10%; */
}

.container-wrapper {
  display: inline-block;
}

.gogoEndpoint{
  background-color: #78909c;
  color: #fff;
  border-radius: 15px;
  font-size: 100%;
  margin-left: 28%;
  margin-top: 40%;
  margin-bottom: 2%;  
  position: fixed;
}
.gogoHome{
  background-color: #78909c;
  color: #fff;
  border-radius: 15px;
  font-size: 100%;
  margin-left: 12%;
  margin-top: 40%;
  margin-bottom: 2%;
  position: fixed;
}
.title-con{
  /* max-width: 750px; */
  /* margin-right: auto; */
  margin-top: 15%;
  position: fixed;
  margin-left: 18%;
  padding: 20px;
  text-align: right;
}

.upload-con{
  background-color: #eceff1;
  box-shadow: 0 1px 11px rgba(0, 0, 0, 0.27);
  margin-top: 10%;
  /* height: 60%; */
  width: 750px;
  position: fixed;
  margin-left: 45%;
  padding: 20px;
  text-align: center;
  border-radius: 30px;
  min-height: 67%;
}

#tab1,
#tab2 {
  display: none;
}

.tab_css {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
}

.tab_css input {
  display: inline-flex;
}

.tab_css label {
  /* margin: 0 5px 5px 0; */
  padding: 10px 16px;
  cursor: pointer;
  border-radius: 5px;
  background: #999;
  color: #fff;
  opacity: 0.5;
}

.tab_content1,
.tab_content2 {
  order: 1;
  display: none;
  width: 100%;
  border-bottom: 3px solid #ddd;
  line-height: 1.6;
  font-size: 0.9em;
  padding: 15px;
  border: 0px solid #ddd;
  border-radius: 5px;
}

.tab_css input:checked + label,
.tab_css label:hover {
  opacity: 1;
  font-weight: bold;
}

.tab_css input:checked + label + .tab_content1,
.tab_css input:checked + label + .tab_content2 {
  display: border;
}

* {
  -webkit-box-sizing: border-box;
  -moz-box-sizing: border-box;
  box-sizing: border-box;
}

.clearfix:after {
  display: block;
  content: "";
  clear: both;
}

h1 {
  font-size: 1.7em;
}

a {
  color: #f1ca53;
}

a:hover {
  color: #ff8a8a;
}

button {
  box-shadow: none;
  border: 1px solid transparent;
  font-size: 14px;
  outline: none;
  line-height: 100%;
  white-space: nowrap;
  vertical-align: middle;
  padding: 0.6rem 1rem;
  border-radius: 2px;
  transition: all 0.2s ease-in-out;
  cursor: pointer;
  min-height: 38px;
}

button.primary {
  background-color: #ff8a65;
  box-shadow: 0 2px 2px 0 rgba(0, 0, 0, 0.12);
  color: #fff;
}

button:hover {
  background-color: #ff705a;
}

button:active {
  background-color: #d790fb;
}

input {
  font-size: 1rem;
}

input[type="file"] {
  border: 1px solid rgba(93, 92, 92, 0.66);
  padding: 6px;
  max-width: 100%;
  /* z-index: 5; */
}

.submit-btn {
  display: block;
  margin-top: 15px;
  min-width: 100px;
}

.file-input_ori {
  width: 100%;
}

.single-upload_ori {
  margin-bottom: 20px;
}

@media screen and (min-width: 500px) {
  .file-input_ori {
    width: calc(100% - 115px);
  }

  .submit-btn {
    /* display: inline-block; */
    margin-top: 0;
    margin-left: 10px;
  }
}

.upload {
  /* max-width: 750px; */
  margin: 10%;
  /* margin-left: auto; */
  /* margin-right: auto; */
  background-color: #fff;
  /* box-shadow: 0 1px 11px rgba(0, 0, 0, 0.27); */
  /* margin-top: 40px; */
  /* min-height: 200px; */
  /* position: relative; */
  /* padding: 20px; */
}

.upload-response {
  overflow-x: hidden;
  word-break: break-all;
  text-align: left;
  margin-left: 3%;
  padding: 1%;
}

</style>
