package com.ll.gwentmirror.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ll.gwentmirror.entity.*;
import com.mysql.cj.xdevapi.JsonArray;
import org.apache.ibatis.jdbc.Null;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/26 20:15
 * @Description :
 */
@Component("beansUtils")
public class BeansUtils {

    public static List<Player> battleInfoJsonMapToPlayerList(Map<String,Object> map){
        List<Player> playerList = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> players = (List<Map<String,Object>>) map.get("players");
        for (Map<String,Object> playerMap : players) {
            Player playerWrap = new Player();
            playerWrap.setName((String) playerMap.get("player_name"));
            playerWrap.setServiceId((Long) playerMap.get("service_id"));
            playerWrap.setParagonLevel((Integer) playerMap.get("paragon_level"));
            playerWrap.setMmr((Integer) playerMap.get("MMR"));
            playerWrap.setRank((Integer) playerMap.get("rank"));
            playerList.add(playerWrap);
        }
        return playerList;
    }

    public static Deck battleInfoJsonMapToDeckList(Map<String,Object> map){
        Deck resultDeck = new Deck();
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> players = (List<Map<String,Object>>) map.get("players");
        for (Map<String,Object> playerMap : players) {
            // decks为空时不存入decks数据库字段
            @SuppressWarnings("unchecked")
            List<Integer> deckIdsList = (List<Integer>) playerMap.get("decks");
            // 2 是最基本的leader卡和stratagem卡
            if (deckIdsList.size() <= 2){
               continue;
            }
            Deck deck = new Deck();
            deck.setId((Long) playerMap.get("deckId"));
            StringBuilder cardsText = new StringBuilder();
            for (Integer ctId : deckIdsList) {
                cardsText.append(ctId).append(",");
            }
            deck.setCards(cardsText.toString());
            String playerName = (String) playerMap.get("player_name");
            Long playerServiceId = (Long) playerMap.get("service_id");
            deck.setName("By."+playerName);
            deck.setAuthor(playerName);
            deck.setFromPlayerId(playerServiceId);
            deck.setTime(new Timestamp(System.currentTimeMillis()));
            deck.setLeaderCardId((Integer) playerMap.get("leader_id"));
            deck.setStratagemCardId((Integer) playerMap.get("stratagem_id"));
            deck.setFactionId((Integer) playerMap.get("faction_id"));
            resultDeck = deck;
        }
        return resultDeck;
    }

    public static DeckJson deckToDeckJons(Deck deck){
        DeckJson deckJson = new DeckJson();
        deckJson.setTime(deck.getTime());
        deckJson.setDeckName(deck.getName());
        deckJson.setDeckAuthor(deck.getAuthor());
        deckJson.setFactionId(deck.getFactionId());
        deckJson.setLeaderCtId(deck.getLeaderCardId());
        deckJson.setStratagemCtId(deck.getStratagemCardId());
        deckJson.setDeckId(deck.getId());
        deckJson.setWebDeckId(deck.getDeckWebId());
        deckJson.setFromPlayerId(deck.getFromPlayerId());
        // str格式转化list<Integer>
        String ctIdsText = deck.getCards();
        List<Integer> sortedCtIds = new ArrayList<>();
        if(ctIdsText != null && ctIdsText.length() != 0){
            String[] ctIdsTexts =ctIdsText.split(",");
            for (String idsText : ctIdsTexts) {
                sortedCtIds.add(Integer.valueOf(idsText));
            }
        }
        deckJson.setSortedCtIds(sortedCtIds);
        // card
        deckJson.setCardList(deck.getCardList());
        return deckJson;
    }

    public static Battle battleInfoJsonToBattle(Map<String, Object> map) {
        Battle resultBattle = new Battle();
        resultBattle.setGameStatus((Integer) map.get("GameStatus"));
        resultBattle.setGameMode((Integer) map.get("GameMode"));
        resultBattle.setGameId((Long) map.get("GameId"));
        resultBattle.setGameBattleType((Integer) map.get("BattleType"));
        resultBattle.setGameEndReason((Integer) map.get("end_game_reason"));
        return resultBattle;
    }

    public static List<PlayerBattleInfo> battleInfoJsonMapToPlayerBattleInfoList(Deck deck, Battle battle, Map<String, Object> map) {
        List<PlayerBattleInfo> results = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<Map<String,Object>> players = (List<Map<String,Object>>) map.get("players");
        for (Map<String,Object> player : players) {
            PlayerBattleInfo pbi = new PlayerBattleInfo();
            pbi.setGameId(battle.getGameId());
            pbi.setPlayerId((Long) player.get("service_id"));
            if (deck.getFromPlayerId() == player.get("service_id")){
                pbi.setDeckId(deck.getId());
            }
            // 1 2 3
            int winnerPlayerIndex = (int) map.get("winner_playId");
            if (winnerPlayerIndex-1 == players.indexOf(player)){
                pbi.setIsWin(1);
            }else{
                pbi.setIsWin(0);
            }
            pbi.setRoundInfo(JSONArray.parseArray(JSON.toJSONString(map.get("roundInfos"))));
            pbi.setWinRoundIndex((Integer) map.get("curr_round_index"));
            results.add(pbi);
        }
        return results;
    }
}
