package com.project.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.project.demo.Search_PomXml;
import com.project.demo.json.RunParser_search.VisitorFilesToGetData;
import com.project.demo.payload.UploadFileResponse;
import com.project.demo.service.FileStorageService;
import com.project.demo.splitClassAndSave;
import com.project.demo.util.FileUtils;
import com.project.demo.util.ZipUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.project.demo.util.FileUtils.copyFolder;


@RestController
@RequestMapping("/")
@CrossOrigin  //解決跨域問題
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    public String thisTimeFileToJson;
    int flag = 0, success_unzip = -1, have_json = 0;     // flag -> 判斷有無找到 Spring Boot 的核心資料 ; have_file -> 預設有檔案進來

    private static String fileUploadRootDir = null;

    @Value("${folder.unzip_path}")
    public String unzip_file_path;

    @Value("${folder.path}")
    public String uploads_path;

    @Value("${file.upload-dir}")
    public String zip_file_path;

    @Value("${folder.json_path}")
    public String json_file_path;

    @Value("${folder.showCode_path}")
    public String showCode_path;

    @Value("${spring.thymeleaf.prefix}")
    public String front_place;

    @Autowired
    public FileStorageService fileStorageService;

    @PostMapping(value = "/uploadFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        success_unzip = -1; // 沒有上傳
        have_json = 0;
        VisitorFilesToGetData.initData(showCode_path);
        try {
            FileUtils.deleteDirectory(new File(uploads_path));
            Files.createDirectories(Path.of(zip_file_path));
            Files.createDirectories(Path.of((unzip_file_path)));
            Files.createDirectories(Path.of((showCode_path)));   // 之後要顯示的程式碼資料夾

        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = fileStorageService.storeFile(file);       // 儲存 zip 檔案

        thisTimeFileToJson = unzip_file_path + "/" + fileName.substring(0,fileName.lastIndexOf(".zip"));

        // 解壓缩 ZIP 文件
        String zipFilePath = Paths.get(zip_file_path).toAbsolutePath()  + "/" + fileName;
        String destDirectory = String.valueOf(Paths.get(unzip_file_path).toAbsolutePath());
        try {
            ZipUtils.unzip(zipFilePath, destDirectory);
            success_unzip = 1;  // 解壓縮成功
            System.out.println("ZIP and UNZIP file extracted successfully.");
        } catch (IOException e) {
            success_unzip = 0; // 解壓縮失敗

            System.err.println("Error extracting ZIP file: " + e.getMessage());
        }

        try {
            // 抓解壓縮檔案的資料夾路徑
            Path absPath = Paths.get(new StringBuilder().append(thisTimeFileToJson).append("/").append("pom.xml").toString()).toAbsolutePath();
            System.out.println(absPath);
            System.out.println("有進入 SpringBoot Check");
            // 判斷Springboot專案
            flag = Search_PomXml.FindSpring(absPath.toString());

        } catch (Exception e) {
            success_unzip = 0;
            flag = 0;
            // 在這裡處理錯誤
            e.printStackTrace();
        }
        if (success_unzip == 0 || flag == 0){
            System.out.println("檔案有誤-readJson");
            return null;
        }
        else {
            try {
                FileUtils.deleteDirectory(new File(json_file_path));
                Files.createDirectories(Path.of(json_file_path));
                FileUtils.deleteDirectory(new File(uploads_path + "/all_Class/"));
                Files.createDirectories(Path.of(uploads_path + "/all_Class/"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (thisTimeFileToJson != null) {
                File file2 = new File(thisTimeFileToJson + "/src/main/java/");

                VisitorFilesToGetData.GoInFolder(file2);
                VisitorFilesToGetData.Collating_Data();   // 整理資料 -> 去除

                String jsonFile_str = VisitorFilesToGetData.Data_Printer(json_file_path);

                if(jsonFile_str.length() != 2){  // AST_json not null:[]
                    have_json = 1;
                }
                String front = front_place.substring(5,front_place.lastIndexOf("public"))+ "src/components/AST_output.json";
                System.out.println("front path: " + front);

                // 存一份到前端
                File src = new File(json_file_path + "/AST_output.json");
                File dest = new File(front);

                FileUtils.deleteFile(front);
                Files.copy(src.toPath(), dest.toPath());


                String sourceFolderPath = showCode_path.substring(0,showCode_path.length()-1);

                // 複製資料夾進入前端內
                String destinationFolderPath = front_place.substring(5) + "/showCode";
                FileUtils.deleteDirectory(new File(destinationFolderPath));

                try {
                    copyFolder(new File(sourceFolderPath), new File(destinationFolderPath));
                    System.out.println("資料夾複製完成！");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Map<String, ArrayList> jsonData_get = splitClassAndSave.showCode_FileList(showCode_path);

                front = front_place.substring(5,front_place.lastIndexOf("public"))+ "src/components/All_Class_Method.json";

                // 存一份到前端
                src = new File(uploads_path + "/all_Class/All_Class_Method.json");
                dest = new File(front);

                FileUtils.deleteFile(front);
                Files.copy(src.toPath(), dest.toPath());
            }
        }

        // Web MVC 框架的 URI builder
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize(),
                String.valueOf(success_unzip), String.valueOf(flag), String.valueOf(have_json));
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping(value = "/uploadUrlFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public UploadFileResponse uploadGithubUrlFile(@RequestParam("url") String url,
                                                  @RequestParam("branch") String branch) throws IOException {
        success_unzip = -1; // 沒有上傳
        have_json = 0;
        VisitorFilesToGetData.initData(showCode_path);
        try {
            FileUtils.deleteDirectory(new File(uploads_path));
            Files.createDirectories(Path.of(zip_file_path));
            Files.createDirectories(Path.of((unzip_file_path)));
            Files.createDirectories(Path.of((showCode_path)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String fileName = fileStorageService.storeURLFile(url, branch);       // 儲存 zip 檔案
        success_unzip = 1;  // 解壓縮成功
        thisTimeFileToJson = unzip_file_path + "/";

        try {
            // 抓解壓縮檔案的資料夾路徑
            Path absPath = Paths.get(
                    new StringBuilder().append(thisTimeFileToJson).append("/").append("pom.xml").toString()).toAbsolutePath();
            System.out.println(absPath);
            System.out.println("有進入 SpringBoot Check");
            // 判斷Springboot專案
            flag = Search_PomXml.FindSpring(absPath.toString());

        } catch (Exception e) {
            success_unzip = 0;
            flag = 0;
            // 在這裡處理錯誤
            e.printStackTrace();
        }
        if (success_unzip == 0 || flag == 0){
            System.out.println("檔案有誤-readJson");
            return new UploadFileResponse("null", url, "Github File -b ", 0,
                    null, null, null);
        }
        else {
            try {
                FileUtils.deleteDirectory(new File(json_file_path));
                Files.createDirectories(Path.of(json_file_path));
                FileUtils.deleteDirectory(new File(uploads_path + "/all_Class/"));
                Files.createDirectories(Path.of(uploads_path + "/all_Class/"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (thisTimeFileToJson != null) {
                File file2 = new File(thisTimeFileToJson + "/src/main/java/");

                VisitorFilesToGetData.GoInFolder(file2);
                VisitorFilesToGetData.Collating_Data();   // 整理資料 -> 去除

                String jsonFile_str = VisitorFilesToGetData.Data_Printer(json_file_path);

                if(jsonFile_str.length() != 2){  // AST_json not null:[]
                    have_json = 1;
                }
                String front = front_place.substring(5,front_place.lastIndexOf("public"))+ "src/components/AST_output.json";

                // 存一份到前端
                File src = new File(json_file_path + "/AST_output.json");
                File dest = new File(front);

                FileUtils.deleteFile(front);
                Files.copy(src.toPath(), dest.toPath());

                String sourceFolderPath = showCode_path.substring(0,showCode_path.length()-1);

                // 複製資料夾進入前端內
                String destinationFolderPath = front_place.substring(5) + "/showCode";
                FileUtils.deleteDirectory(new File(destinationFolderPath));

                try {
                    copyFolder(new File(sourceFolderPath), new File(destinationFolderPath));
                    System.out.println("資料夾複製完成！");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Map<String, ArrayList> jsonData_get = splitClassAndSave.showCode_FileList(showCode_path);

                front = front_place.substring(5,front_place.lastIndexOf("public"))+ "src/components/All_Class_Method.json";

//                 存一份到前端
                src = new File(uploads_path + "/all_Class/All_Class_Method.json");
                dest = new File(front);

                FileUtils.deleteFile(front);
                Files.copy(src.toPath(), dest.toPath());
            }
        }
        return new UploadFileResponse(fileName, url, "Github File -b " + branch, 0,
                String.valueOf(success_unzip), String.valueOf(flag), String.valueOf(have_json));
    }

    @Configuration
    class WebConfig implements WebMvcConfigurer {
        @Value("${spring.thymeleaf.prefix}")
        public String origin_path;

        @Override   // 覆寫了父類別的方法
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // 在 "/home/file/**" 前端 URL 訪問路徑，后面 file : xxxx 為本地資料夾映射
            registry.addResourceHandler("/home/**").addResourceLocations(origin_path);
            WebMvcConfigurer.super.addResourceHandlers(registry);
        }

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOriginPatterns("*")
                    .allowedMethods("GET","HEAD","POST","PUT","DELETE","OPTION")
                    .allowCredentials(true)
                    .maxAge(3600)
                    .allowedHeaders("*");
        }
    }
}
