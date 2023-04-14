package com.ll.gwentmirror.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/11/1 13:53
 * @Description : 文件的上传与下载
 */
@Slf4j
@Controller
public class FileController {
    @Value("${file.uploadUrl}")
    public String path;

    @RequestMapping("/uploadFile")
    public String uploadFile() {
        return "upload";
    }

    @PostMapping("/upload")
    public ResponseEntity<String>  upload(MultipartFile file) throws IOException {
        //获取文件名
        try {
            String fileName = "GwentMirror.7z";
            // 指定文件保存位置
            String uploadDir = "";
            File savefile;
            // 判断当前操作系统类型
            String osName = System.getProperty("os.name").toLowerCase();
            if (osName.contains("windows")) {
                Resource resource = new ClassPathResource("/GwentMirror.7z");
                savefile = resource.getFile();
            } else if (osName.contains("linux")) {
                uploadDir = "/www/jar/GwentMirror/uploads/";
                savefile = new File(uploadDir + fileName);
            } else {
                // 如果当前操作系统不是Windows或Linux，则抛出异常
                throw new UnsupportedOperationException("Unsupported operating system: " + osName);
            }

            //将文件保存到指定位置
            file.transferTo(savefile);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }


    @GetMapping("/download")
    public ResponseEntity<InputStreamResource> download(HttpServletRequest request) throws IOException {
        // 指定文件保存位置
        String fileName = "GwentMirror.7z";
        // 指定文件保存位置
        String uploadDir = "";
        File savefile;
        // 判断当前操作系统类型
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("windows")) {
            Resource resource = new ClassPathResource("/GwentMirror.7z");
            savefile = resource.getFile();
        } else if (osName.contains("linux")) {
            uploadDir = "/www/jar/GwentMirror/uploads/";
            savefile = new File(uploadDir + fileName);
        } else {
            // 如果当前操作系统不是Windows或Linux，则抛出异常
            throw new UnsupportedOperationException("Unsupported operating system: " + osName);
        }

        // 指定要下载的文件路径
        String filePath = savefile.getPath();
        // 从指定路径读取文件
        Path path = Paths.get(filePath);
        Resource resource = new UrlResource(path.toUri());
        // 设置响应头信息
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", fileName));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        // 返回文件的字节流
        InputStreamResource isr = new InputStreamResource(resource.getInputStream());
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(isr);
    }

}
