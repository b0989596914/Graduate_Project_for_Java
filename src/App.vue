<template>
  <div>
    <nav class="navbar">
      <div class="container-fluid" style="margin: 1em">
        <a class="nav_p" href="#">
          <img
            src="./assets/pngegg.png"
            alt="Logo"
            height="40"
            class="d-inline-block align-text-top"
          />
          API 測試之除錯視覺化輔助工具
        </a>
        <ul class="nav nav-pills">
          <li class="nav-item">
            <button class="nav_list" :class="{ 'orange-button': $route.path === '/' }">
              <router-link to="/" class="btn_color" > Home Page</router-link>
            </button>
          </li>
          <li class="nav-item">
            <div id="narbar_butt" class="dropdown">
              <button class="nav_list btn_color" :class="{ 'orange-button': $route.path === '/chooseFile' ||  $route.path === '/githubUpload'}">Upload File</button>
              <div id="narbar_butt_content" class="dropdown-content">
                <router-link to="/chooseFile">Choose File</router-link>
                <router-link to="/githubUpload">Use Github URL</router-link>
              </div>
            </div>
          </li>
          <li class="nav-item">
            <button class="nav_list" :class="{ 'orange-button': $route.path === '/endPoint' || $route.path.includes('/about')  || $route.path.includes('/codedisplay')}">
              <router-link to="/endPoint" class="btn_color">
                EndPoint</router-link
              >
            </button>
          </li>
        </ul>
      </div>
    </nav>
    <router-view />
  </div>
</template>

<style>
.orange-button {
  background-color: #ff8a65;
}

nav {
  background-color: #263238;
  height: 100px;
}

.nav_p {
  color: #ffffff;
  font-size: 27px;
  font-family: Verdana, Geneva, Tahoma, sans-serif;
}

nav a {
  font-size: 16px;
  text-decoration-line: none;
}

.nav_list {
  background-color: #263238;
  border-radius: 10px 10px 0px 0px;
  border-bottom: #ffffff 2px solid;
}

/* .nav_list a.router-link-exact-active {
  color: #ffffff;
  background-color: #ff8a65;
  border-radius: 10px 10px 0px 0px;
  padding: 15%;
  position: relative;
  display: inline;
  margin-left: -15%;
} */

/* button .nav_list a.router-link-exact-active {
  color: #FFFFFF;
  background-color: #dc5555;
  display: block;
  z-index: 2;
 
} */

.btn_color {
  color: #ffffff;
}

/* .btn_color .router-link-active {
  color: #FFFFFF;
} */

#narbar_butt {
  position: relative;
  display: inline-block;
}

#narbar_butt_content {
  display: none;
  position: absolute;
  background-color: #f1f1f1;
  min-width: 160px;
  z-index: 1;
}
#narbar_butt_content a {
  padding: 12px 16px;
  text-decoration: none;
  display: block;
}

#narbar_butt_content a:hover {
  background-color: #ddd;
}

#narbar_butt:hover #narbar_butt_content {
  display: block;
}
.orange-button {
  background-color: #ff8a65;
}
</style>
