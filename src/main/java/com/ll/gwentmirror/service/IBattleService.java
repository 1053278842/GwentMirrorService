package com.ll.gwentmirror.service;

import com.ll.gwentmirror.entity.Battle;
import com.ll.gwentmirror.entity.Deck;
import com.ll.gwentmirror.entity.Player;
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
public interface IBattleService {
    /**
     * 事务插入player、deck、battle、playerBattleInfo表
     * @param playerList player列表，保存两个玩家信息
     * @param deck  是我方玩家的卡组信息
     * @param battle 战斗类型信息，缺省了对两个playerBattleInfos的主键
     * @param playerBattleInfos 玩家对局信息，阙胜乐battle的主键
     */
    void saveBattleInfo(List<Player> playerList, Deck deck, Battle battle, List<PlayerBattleInfo> playerBattleInfos);

    /**
     * 存储一个Battle,并获得他返回的唯一iD
     * @param battle b
     * @return 返回刚插入的battle对象，可用于获取插入后的的自增id
     */
    Battle saveBattle(Battle battle);

    /**
     * 根据battle的id进行更新
     * @param battle b
     */
    void updateDeck(Battle battle);
}
