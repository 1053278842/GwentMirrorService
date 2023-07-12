package com.ll.gwentmirror.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ll.gwentmirror.entity.Card;
import com.ll.gwentmirror.entity.Deck;
import com.ll.gwentmirror.entity.DeckJson;
import com.ll.gwentmirror.mapper.ICardMapper;
import com.ll.gwentmirror.mapper.IDeckMapper;
import com.ll.gwentmirror.service.IDeckService;
import com.ll.gwentmirror.utils.BeansUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/25 15:14
 * @Description :
 */
@Transactional(rollbackFor = Exception.class)
@Slf4j
@Service
public class DeckServiceImpl extends ServiceImpl<IDeckMapper,Deck> implements IDeckService{

    private final IDeckMapper deckMapper;
    private final ICardMapper cardMapper;

    public DeckServiceImpl(IDeckMapper deckMapper, ICardMapper cardMapper) {
        this.deckMapper = deckMapper;
        this.cardMapper = cardMapper;
    }

    @Override
    public void saveBatchDeck(List<Deck> decks) {
        deckMapper.insertBatchDeck(decks);
    }

    @Override
    public Long getMaxWebId() {
        QueryWrapper<Deck> queryWrapper = new QueryWrapper<Deck>();
        queryWrapper.select("IFNULL(MAX(deck_web_id),0) as max_deck_web_id");
        Map<String, Object> map = this.getMap(queryWrapper);

        return (Long) map.get("max_deck_web_id");
    }

    @Override
    public List<DeckJson> getListByFactionId(int factionId, int size) {
        List<Deck> decks =  deckMapper.selectList(
                new QueryWrapper<Deck>()
                .eq("faction_id",factionId)
                .last("limit "+size)
        );
        List<DeckJson> decksJson = new ArrayList<>();
        for (Deck deck : decks) {
            decksJson.add(BeansUtils.deckToDeckJons(deck));
        }
        return decksJson;
    }

    @Override
    public List<DeckJson> getList(int size) {
        List<Deck> decks =  deckMapper.selectList(
                new QueryWrapper<Deck>()
                        .last("limit "+size)
        );
        List<DeckJson> decksJson = new ArrayList<>();
        for (Deck deck : decks) {
            decksJson.add(BeansUtils.deckToDeckJons(deck));
        }
        return decksJson;
    }

    @Override
    public void saveDeck(Deck deck) {
        deckMapper.insertDeckOnDuplication(deck);
    }

    /**
     * 分頁
     * @param idArray
     * @param page
     * @return
     */
    @Override
    public List<DeckJson> getListByIds(String[] idArray, int page) {
        final int pageSize = 5;
        final String sortFiled = "update_time";
        int pageStart = (page-1)*pageSize;
        List<Deck> decks = deckMapper.getListByIds(idArray,sortFiled,pageStart, pageSize,false);

        List<DeckJson> decksJson = new ArrayList<>();
        for (Deck deck : decks) {
            decksJson.add(BeansUtils.deckToDeckJons(deck));
        }
        return decksJson;
    }

    /**
     * 不分頁
     * @param idArray
     * @return
     */
    @Override
    public List<DeckJson> getListByIds(String[] idArray) {
        final String sortFiled = "update_time";
        List<Deck> decks = deckMapper.getListByIds(idArray,sortFiled,0, 0,true);
        decks.forEach(deck -> {
//            List<String> cards = Arrays.asList(deck.getCards().split(","));
//            deck.setCardList(cardMapper.selectList(new QueryWrapper<Card>().in("cid",cards)));
        });
        List<DeckJson> decksJson = new ArrayList<>();
        for (Deck deck : decks) {
            decksJson.add(BeansUtils.deckToDeckJons(deck));
        }
        return decksJson;
    }

}
