package com.project.demo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import com.project.demo.util.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class splitClassAndSave {
    public static void buildShownCode(String showCode_path, SimpleName nowClassName, SimpleName name, MethodDeclaration n) {
        String NowClassPath = showCode_path;

        NowClassPath = NowClassPath + nowClassName + "/";

        // 建立資料夾 ( 以Class為區分 )
        File nowClassDir = new File(NowClassPath);
        if (!nowClassDir.exists()) { // 不存在檔案時，先建立
            nowClassDir.mkdirs();
        }

        ObjectMapper objectMapper = new ObjectMapper();

        // 建立、寫入檔案
        try {

            String fileName = NowClassPath + name + ".txt";
            FileWriter fileWriter = new FileWriter(fileName);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            String method_json = objectMapper.writeValueAsString(String.valueOf(n));

            bufferedWriter.write(method_json);
            bufferedWriter.flush(); // 把記憶體資料寫進去

            bufferedWriter.close();
//            System.out.println("Method " + fileName + " 寫入成功！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, ArrayList> showCode_FileList(String folderPath) throws JsonProcessingException {
        File folder = new File(folderPath);
        Map<String, ArrayList> jsonData = new HashMap<>();
        Map<String, ArrayList> data_arrList = new HashMap<>();

        ArrayList showCode_arrList = new ArrayList();


        if (folder.exists() && folder.isDirectory()) {
            String[] files = folder.list();
            if (files != null) {
                for (String fileName : files) {
//                    System.out.println(fileName);
                    ArrayList temporary = new ArrayList();
                    File folderInto = new File(folderPath + "/" +  fileName);
                    if (folderInto.exists() && folderInto.isDirectory()) {
                        String[] f = folderInto.list();
                        if (f != null) {
                            for (String Name : f) {
                                showCode_arrList.add(Name.substring(0,Name.lastIndexOf(".txt")));
                                temporary.add(Name.substring(0,Name.lastIndexOf(".txt")));
                            }
                        }else {
                            System.out.println("資料夾為空");

                        }
                    }
                    data_arrList.put(fileName,temporary);
                    jsonData.put(fileName, (ArrayList) showCode_arrList.clone());
                    showCode_arrList.clear();
                }
                ObjectMapper objectMapper = new ObjectMapper();
                String json = objectMapper.writeValueAsString(data_arrList);

                try {
                    FileWriter fw = new FileWriter("./uploads/All_Class/All_Class_Method.json");

                    BufferedWriter bw = new BufferedWriter(fw);

                    bw.write(json);    // 寫入資料
                    bw.flush(); // 把記憶體資料寫進去
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {
                System.out.println("資料夾為空");
            }
        } else {
            System.out.println("資料夾不存在或不是資料夾");
        }
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        // 使用 ObjectWriter 物件將 Java 對象轉換為 JSON 字串
//        ObjectWriter writer = objectMapper.writerWithDefaultPrettyPrinter();
//        try {
//            // 將 JSON 字串寫入到指定檔案
//            File outputFile = new File("output.json");
//            writer.writeValue(outputFile, jsonData);
//
//            System.out.println("JSON 檔案建立成功！");
//        } catch (IOException e) {
//            System.err.println("建立 JSON 檔案時發生錯誤：" + e.getMessage());
//        }
        return jsonData;
    }

    public static String finalZipCode(){

        // 指定要壓縮的檔案所在目錄
        String directoryPath = "./showCode/";

        // 指定壓縮檔的名稱
        String zipFileName = "output.zip";

//        try {
//            // 建立一個ZipOutputStream，將資料寫入壓縮檔案
//            FileOutputStream fos = new FileOutputStream(zipFileName);
//            ZipOutputStream zipOut = new ZipOutputStream(fos);
//
//            // 開始遞迴處理資料夾
//            File sourceFolder = new File(directoryPath);
//            addToZip(zipOut, sourceFolder, sourceFolder.getName());
//
//            // 關閉壓縮檔案的輸出流
//            zipOut.close();
//            fos.close();
//
//            System.out.println("資料夾壓縮成功！");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return new File(zipFileName).getAbsolutePath();
    }

    private static void addToZip(ZipOutputStream zipOut, File file, String entryName) throws IOException {
        if (file.isDirectory()) {
            // 處理資料夾內的檔案和子資料夾
            File[] files = file.listFiles();
            for (File subFile : files) {
                String newEntryName = entryName + "/" + subFile.getName();
                addToZip(zipOut, subFile, newEntryName);
            }
        } else {
            // 如果是檔案，則加入壓縮檔案
            ZipEntry zipEntry = new ZipEntry(entryName);
            zipOut.putNextEntry(zipEntry);

            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zipOut.write(buffer, 0, length);
            }

            zipOut.closeEntry();
            fis.close();
        }
    }
}
