const { defineConfig } = require("@vue/cli-service");

module.exports = defineConfig({
  transpileDependencies: true,
  publicPath:
    process.env.NODE_ENV === "production" ? "/production-sub-path/" : "/",
  devServer: {
    open: true,
    https: false,
    port: 8080,
    proxy: {
      // "/": {
      //   target: "http://localhost:9100", // 替換為Spring Boot後端的URL
      //   ws: false,
      //   changeOrigin: true,
      //   pathRewrite: {
      //     "^/": "/",
      //   },
      // },
      [process.env.VUE_APP_BASE_API]: {
        target: "http://localhost:9100", // 替換為Spring Boot後端的URL
        ws: false,
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_APP_BASE_API]: "/",
        },
      },
    },
  },
});
