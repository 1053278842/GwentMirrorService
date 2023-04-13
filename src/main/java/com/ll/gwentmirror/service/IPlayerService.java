package com.ll.gwentmirror.service;

import com.ll.gwentmirror.entity.Player;

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
public interface IPlayerService {
    /**
     * 批量插入player，走mapper定制sql语句
     * @param playerList
     */
    void saveList(List<Player> playerList);
}
