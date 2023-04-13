package com.ll.gwentmirror.controller;

import com.ll.gwentmirror.entity.*;
import com.ll.gwentmirror.service.IVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/26 16:26
 * @Description :
 */
@Slf4j
@RestController
@RequestMapping(("/version/api"))
public class VersionController {

    @Autowired
    private IVersionService versionService;


    @PostMapping("/getId")
    public Version getId(){
        return versionService.findNewOne();
    }
}
