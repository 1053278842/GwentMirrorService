package com.ll.gwentmirror.controller;

import com.ll.gwentmirror.entity.Deck;
import com.ll.gwentmirror.entity.DeckJson;
import com.ll.gwentmirror.service.IDeckService;
import com.ll.gwentmirror.utils.BeansUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
@RequestMapping(("/deck/api"))
public class DeckController {

    @Autowired
    private IDeckService deckService;

    @RequestMapping("/maxid")
    public Long maxid(){
        return deckService.getMaxWebId();
    }

    @RequestMapping("/getDecks")
    public List<DeckJson> getDecks(){
        final int size = 5000;
        return deckService.getList(size);
    }

    /**
     * 分页查找
     * @param ids
     * @param page
     * @return
     */
    @RequestMapping("/getDecksByIds")
    public List<DeckJson> getDecksByIds(@RequestParam(name = "ids",defaultValue = "") String ids,
                                        @RequestParam(name="page",required = false)Integer page){
        ids = ids.replace("\"","");
        String[] idArray = ids.split(",");
        // 兼容分页请求
        if(page == null || page == 0){
            return deckService.getListByIds(idArray);
        }else{
            return deckService.getListByIds(idArray,page);
        }
    }

    @GetMapping("/getRandomDecksLast")
    public List<DeckJson> getRandomDecksLast(@RequestParam(name = "num", defaultValue = "0") Integer num) {
        List<Deck> allDeck = deckService.getListLast();
        // 如果传参为0或者为空，则返回搜索结果
        // 反之返回随机num个
        if (num == 0) {
            return allDeck.stream()
                    .map(BeansUtils::deckToDeckJons)
                    .collect(Collectors.toList());
        } else {
            // 原实现用 sorted((a,b) -> new Random().nextInt()) 当比较器，
            // 违反 TimSort 的传递性契约，数据量一多就会抛
            // "Comparison method violates its general contract!" -> 接口 500。
            // 改成先转 DTO 再洗牌取前 num 个，语义相同且稳定。
            List<DeckJson> deckJsons = allDeck.stream()
                    .map(BeansUtils::deckToDeckJons)
                    .collect(Collectors.toList());
            Collections.shuffle(deckJsons, new Random());
            return deckJsons.stream().limit(num).collect(Collectors.toList());
        }
    }
    @PostMapping("/post")
    public String post(@RequestBody List<DeckJson> deckJsons){
        log.info("正在批量插入官网上爬取的卡组！");
        // 存储用的deck
        List<Deck> decks = new ArrayList<>();
        for (DeckJson deckJson : deckJsons) {
            Deck deck = new Deck();

            deck.setId(deckJson.getDeckId());
            // 列表json转字符串，split=","
            StringBuilder cardsStr = new StringBuilder();
            for (Integer cardId : deckJson.getSortedCtIds()) {
                cardsStr.append(cardId).append(",");
            }
            deck.setCards(cardsStr.toString());
            deck.setName(deckJson.getDeckName());
            deck.setAuthor(deckJson.getDeckAuthor());
            deck.setFromPlayerId(deckJson.getFromPlayerId());
            deck.setTime(deckJson.getTime());
            deck.setLeaderCardId(deckJson.getLeaderCtId());
            deck.setStratagemCardId(deckJson.getStratagemCtId());
            deck.setDeckWebId(deckJson.getWebDeckId());
            deck.setFactionId(deckJson.getFactionId());

            decks.add(deck);
        }
        deckService.saveBatchDeck(decks);
        return "";
    }
}
