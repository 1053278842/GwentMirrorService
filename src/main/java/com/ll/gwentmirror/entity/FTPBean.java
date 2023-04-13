package com.ll.gwentmirror.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/11/1 16:09
 * @Description :
 */
@Data
@Component
@ConfigurationProperties(prefix = "ftp")
public class FTPBean {
    private String hostname;
    private int port;
    private String username;
    private String password;
    private String savePath;
}
