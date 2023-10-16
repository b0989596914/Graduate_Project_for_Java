package com.project.demo.util;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

public class FileUtils {
    public static void deleteFile(String relativePath) {
        Path path = Paths.get(relativePath);

        try {
            // Delete file or directory
            Files.delete(path);
            System.out.println("File or directory deleted successfully");
        } catch (NoSuchFileException ex) {
            System.out.printf("No such file or directory: %s\n", path);
        } catch (DirectoryNotEmptyException ex) {
            System.out.printf("Directory %s is not empty\n", path);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
    public static void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    deleteDirectory(file);
                }
            }
        }
        directory.delete();
    }
    public static void copyFolder(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdirs();
            }

            String[] files = source.list();
            if (files != null) {
                for (String file : files) {
                    File srcFile = new File(source, file);
                    File destFile = new File(destination, file);
                    copyFolder(srcFile, destFile);
                }
            }
        } else {
            Files.copy(source.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public static void addJsonDirectory(String createPath){
        Path directoryPath = Paths.get(createPath);

        if (Files.exists(directoryPath)) {
            // 目錄已存在，執行相應處理
        } else {
            try {
                if (Files.isWritable(directoryPath.getParent())) {
                    Files.createDirectories(directoryPath);
                } else {
                    // 沒有寫入權限，處理相應錯誤
                }
            } catch (IOException e) {
                // 處理IO異常
            }
        }
    }
}