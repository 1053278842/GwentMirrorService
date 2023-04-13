package com.ll.gwentmirror.service;

import com.ll.gwentmirror.entity.Player;
import com.ll.gwentmirror.entity.Version;

import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/28 16:14
 * @Description :
 */
public interface IVersionService {

    /**
     * 根据startTime得到最新的version对象
     * @return version Bean对象
     */
    Version findNewOne();
}
