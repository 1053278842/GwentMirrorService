package com.ll.gwentmirror.service;

import com.ll.gwentmirror.entity.Deck;
import com.ll.gwentmirror.entity.DeckJson;

import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/24 22:55
 * @Description :
 */
public interface IDeckService {

    /***
     * 批量插入deck对象
     * @param decks
     */
    void saveBatchDeck(List<Deck> decks);

    /**
     * 返回最大的web_id。解决爬虫每次批量爬取重复deck造成的性能浪费问题
     * @return deck.deck_web_id
     */
    Long getMaxWebId();

    /**
     * 根据faction_id 获取 一定长度的的deck数据
     * @param factionId
     * @param size
     * @return
     */
    List<DeckJson> getListByFactionId(int factionId, int size);

    /**
     * 获取全部
     * @param size
     * @return
     */
    List<DeckJson> getList(int size);

    /**
     * 插入一个dekc
     * @param deck
     */
    void saveDeck(Deck deck);

    /**
     * 对应search deck 页面，返回数据按照update——time排序
     * 分页
     * @param idArray
     * @param page
     * @return
     */
    List<DeckJson> getListByIds(String[] idArray, int page);

    /**
     * 对应search deck 页面，返回数据按照update——time排序
     * 不分页
     * @param idArray
     * @return
     */
    List<DeckJson> getListByIds(String[] idArray);
}
