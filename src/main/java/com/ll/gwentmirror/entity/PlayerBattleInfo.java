package com.ll.gwentmirror.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.mysql.cj.xdevapi.JsonArray;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/28 0:43
 * @Description :
 */
@Data
@TableName(value = "player_battle_info",autoResultMap = true)
public class PlayerBattleInfo implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("game_id")
    private Long gameId;
    @TableField("player_id")
    private Long playerId;
    @TableField("deck_id")
    private Long deckId;
    @TableField("is_win")
    private Integer isWin;
    @TableField(value = "round_info",typeHandler = JacksonTypeHandler.class)
    private JSONArray roundInfo;
    @TableField("win_round_index")
    private Integer winRoundIndex;
    @TableField("create_time")
    private Timestamp createTime;
    @TableField("update_time")
    private Timestamp updateTime;
}
