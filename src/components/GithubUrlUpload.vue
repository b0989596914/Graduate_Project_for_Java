<template>
  <div class="container-wrapper">
    <div class="title-container">
      <h1 class="bold-text">File Upload</h1>
      <p style="font-size: 25px">
        Input Github URL<br />and<br />Wait for a while
      </p>
    </div>
    <div>
      <button class="page-btn Github_home-button" @click="gohome"> ◀ Home</button>
      <!-- 以下為防呆按鈕 -->
      <button class="page-btn toendpoint-button"  v-if="can_touch_button == true" @click="endPoint" >Show EndPoint ▶ </button>
    </div>
    <div class="github-container">
      <div>
        <div>
          <form
            id="singleUploadForm"
            name="singleUploadForm"
            @submit.prevent="submitForm"
          >
            <div class="input_back">
              
              <p class="illustrate">Input Github URL</p>
              <input
                id="testforGithubUrl"
                type="url"
                name="url"
                placeholder="https://github.com/b0989596914/Graduate_Project.git"
                class="file-input"
                required
                @change="handleGithubUrlChange($event)"
              />
              <br />
              <p class="illustrate">Input URL Branch</p>
              <input
                id="testforGithubUrl"
                type="text"
                name="url"
                placeholder="main"
                class="file-input"
                required
                @change="handleGitBranchChange($event)"
              />
              <br /><br />
            </div>

            <div class="button_right_back">
              <button
                type="submit"
                class="submit-btn"
                id="submitGithubURL_button"
              >
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
      githubUrlInput: "",
      gitBranch: "master",
      uploadError: "",
      uploadSuccess: "",
      checkResult: "no",
      can_touch_button: false,
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
      if (!this.githubUrlInput) {
        this.uploadError = "Please enter a Github URL";
      } else {
        this.uploadGithubUrl(this.githubUrlInput, this.gitBranch);
      }
    },
    uploadGithubUrl(url, branch) {
      console.log("Github URL:", url);
      var formData = new FormData();
      formData.append("url", url);
      formData.append("branch", branch);

      var xhr = new XMLHttpRequest();
      xhr.open("POST", "/uploadUrlFile");

      xhr.onload = () => {
        console.log(xhr.responseText);
        var response = JSON.parse(xhr.responseText);
        if (xhr.status === 200) {
          if(response.fileName == "null"){
            alert("請確認 Branch 名稱是否存在 ~ ");
          }
          else {
            this.check(response.success_unzip, response.flag, response.have_json);
  
            if (this.checkResult == "yes") {
              this.uploadError = "";
              this.uploadSuccess = `<p>File Uploaded Successfully.</p><p>DownloadUrl : <a href='${response.fileDownloadUri}' target='_blank'>${response.fileDownloadUri}</a></p>`;
              this.can_touch_button = true;
            } else {
              (this.fileInput = null), (this.githubUrlInput = "");
              this.gitBranch = "master";
              this.uploadError = "";
              this.uploadSuccess = "";
              this.checkResult = "";
              alert("發生錯誤，請重新上傳專案 !!");
            }
          }
        } else {
          this.uploadSuccess = "";
          this.uploadError =
            (response && response.message) || "Some Error Occurred";
          
          this.$emit("can_touch_button", this.can_touch_button);
        }
        
      };
      xhr.send(formData);
    },
    check(getIsSuccess, getFlag, getHave_json) {
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
    handleGithubUrlChange(event) {
      const url = event.target.value;
      if (url.startsWith("https://github.com")) {
        this.githubUrlInput = url;
      } else {
        // URL 若不是以 "https://github.com" 為開頭，則要求用户重新輸入檔案下載網址
        this.githubUrlInput = event.target.value = ""; // 清空之前的輸入值
        alert(
          "Please enter a valid Github URL starting with 'https://github.com'"
        );
      }
    },

    handleGitBranchChange(event) {
      const branch = event.target.value;
      if (branch != null) {
        this.gitBranch = branch;
      } else {
        this.gitBranch = event.target.value = ""; // 清空之前的輸入值
        alert("Please enter a valid Github branch");
      }
    },
  },
};
</script>

<style>
.illustrate{
  margin-left: -62%;
  margin-top: 4%;
}

.button_right_back {
  padding: 3px;
  margin: 2%;
  text-align: right;
}

.input_back {
  background-color: #d9d9d9;
  border-radius: 20px;
  margin: 2%;
  padding: 5px;
}

.container-wrapper {
  display: inline-block;
}

.Github_home-button {
  background-color: #78909c;
  color: #fff;
  border-radius: 15px;
  font-size: 100%;
  margin-left: 12%;
  margin-top: 40%;
  margin-bottom: 2%;
  position: fixed;
}

.toendpoint-button {
  background-color: #78909c;
  color: #fff;
  border-radius: 15px;
  font-size: 100%;
  margin-left: 28%;
  margin-top: 40%;
  position: fixed;
  margin-bottom: 2%;
}

.left_title {
  /* margin-left: 13%; */
  /* margin-top: 40px; */
  text-align: right;
}
.bold-text {
  font-weight: bold; /* 将字体加粗 */
  font-size: 45px;
}

.title-container {
  /* max-width: 750px; */
  /* margin-right: auto; */
  margin-top: 15%;
  position: fixed;
  margin-left: 18%;
  padding: 20px;
  text-align: right;
}

.github-container {
  /* max-width: 750px; */
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
  margin: 0 5px 5px 0;
  padding: 10px 16px;
  cursor: pointer;
  border-radius: 5px;
  /* background: #999; */
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
  display: initial;
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

h1,
h2,
h3,
h4,
h5,
h6 {
  margin-top: 20px;
  margin-bottom: 20px;
}

h1 {
  font-size: 1.7em;
}

a {
  color: #f1ca53;
}

.left-aligned-input {
  text-align: left; /* 设置文本左对齐 */
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

button:hover a:hover{
  color: #ffffff;
}

button:active {
  background-color: #d790fb;
}

input {
  font-size: 1rem;
}

input[type="URL"] {
  border: 1px solid rgba(143, 140, 137, 0.66);
  padding: 6px;
  max-width: 100%;
  text-align: left;
}

input[type="text"] {
  border: 1px solid rgba(143, 140, 137, 0.66);
  padding: 6px;
  max-width: 100%;
  color: gray;
  text-align: left;
  margin-bottom: 1.5%;
}

.file-input {
  margin-top: -1%;
  position: relative;
  width: 100%;
}

.submit-btn {
  background-color: #ff8a65;
  color: #ffffff;
  min-width: 100px;
  border-radius: 10px;
  margin-right: 0;
}

@media screen and (min-width: 500px) {
  .file-input {
    width: calc(100% - 115px);
  }

  .submit-btn {
    /* display: inline-block; */
    margin-top: 0;
    margin-left: 85%;
  }
}

/* .gitURL-upload { */
/* padding-bottom: 20px;
  margin-bottom: 20px; */
/* border-bottom: 1px solid #e8e8e8; */
/* } */

.upload-response {
  overflow-x: hidden;
  word-break: break-all;
}
</style>
