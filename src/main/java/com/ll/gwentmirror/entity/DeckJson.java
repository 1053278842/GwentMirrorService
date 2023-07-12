package com.ll.gwentmirror.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.sql.Timestamp;
/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/25 19:45
 * @Description : 用于接收python传入的批量deck对象的json格式，服务于spring装载
 */
@Data
public class DeckJson implements Serializable {
    private Long deckId;
    private List<Integer> sortedCtIds;
    private Long fromPlayerId;
    private Integer leaderCtId;
    private Integer stratagemCtId;
    private Timestamp time;
    private String deckName;
    private String deckAuthor;
    private Integer webDeckId;
    private Integer factionId;
    private List<Card> cardList;
}
