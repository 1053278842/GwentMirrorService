package com.ll.gwentmirror.service;

import com.ll.gwentmirror.entity.PlayerBattleInfo;

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
public interface IPlayerBattleInfoService {
    /**
     * 批量插入，带有更新
     * @param playerBattleInfos e
     * @return 返回每个pbi对象插入后的主键id组成的列表
     */
    List<Integer> saveBatchAndReturnIds(List<PlayerBattleInfo> playerBattleInfos);
}
