package com.ll.gwentmirror.utils;

import com.ll.gwentmirror.entity.FTPBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Arrays;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/11/1 16:12
 * @Description :
 */
@Slf4j
@Component
public class ftpUtils {

    @Autowired
    private FTPBean reFtpBean;

    private static FTPBean ftpBean;

    @PostConstruct
    public  void init() {
        ftpBean = reFtpBean;
        log.info("初始化完成");
    }


    public static FTPClient getConnection() {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setConnectTimeout(10*1000);
        try {
            // 设置连接机器
            ftpClient.connect(ftpBean.getHostname(), ftpBean.getPort());
            ftpClient.login(ftpBean.getUsername(), ftpBean.getPassword());
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                log.info("ftp连接失败");
                ftpClient.disconnect(); // 断开连接
                return null;
            } else {
                log.info("ftp连接成功");
            }

            FTPClientConfig config = new FTPClientConfig(ftpClient.getSystemType().split(" ")[0]);
            config.setServerLanguageCode("zh");
            ftpClient.configure(config);


            // 将文件类型设置成二进制
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
        return ftpClient;
    }



    public static void uploadFile(String fileName, InputStream inputStream) {

        FTPClient ftpClient = getConnection();
        if (ftpClient == null) {
            return;
        }
        try {
            boolean result = ftpClient.storeFile(fileName, inputStream);
            log.info("文件是否保存成功：" + result);
            inputStream.close();
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 文件下载

    /**
     *
     * @param fileName
     * @param localPath  不指定表示下载到当前项目下
     */
    public static void downloadFile( String localPath, String fileName){
        FTPClient ftpClient = getConnection();
        if (ftpClient == null) {
            return;
        }
        try {
            FTPFile[] ftpFiles = ftpClient.listFiles();
            for (FTPFile ftpFile : ftpFiles) {
                if (fileName.equals(ftpFile.getName())) {
                    File file = new File(localPath+fileName);
                    OutputStream outputStream = new FileOutputStream(file);
                    boolean result = ftpClient.retrieveFile(ftpFile.getName(), outputStream);
                    log.info("下载结果：" + result);
                    outputStream.close();
                }
            }
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


