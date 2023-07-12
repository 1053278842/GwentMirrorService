package com.ll.gwentmirror.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/24 22:38
 * @Description : deck对象用于存储用户提交的、爬虫官网爬取的卡组对象
 */
@Data
@TableName("deck")
public class Deck implements Serializable {
    @TableId(value="id",type = IdType.AUTO)
    private Long id;

    @TableField("cards")
    private String cards;

    @TableField("name")
    private String name;

    @TableField("author")
    private String author;

    @TableField("from_player_id")
    private Long fromPlayerId;

    @TableField("time")
    private Timestamp time;

    @TableField("leader_card_id")
    private Integer leaderCardId;

    @TableField("stratagem_card_id")
    private Integer stratagemCardId;

    @TableField("deck_web_id")
    private Integer deckWebId;

    @TableField("faction_id")
    private Integer factionId;

    @TableField("create_time")
    private Timestamp createTime;

    @TableField("update_time")
    private Timestamp updateTime;

    @TableField("total")
    private Integer total;

    private List<Card> cardList;
}
