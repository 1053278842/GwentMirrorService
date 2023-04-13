package com.ll.gwentmirror.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @CreateTime :  2022/10/27 16:15
 * @Description :
 */
@TableName("battle")
@Data
public class Battle implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("game_id")
    private Long gameId;
    @TableField("game_status")
    private Integer gameStatus;
    @TableField("game_mode")
    private Integer gameMode;
    @TableField("game_battle_type")
    private Integer gameBattleType;
    @TableField("game_end_reason")
    private Integer gameEndReason;
    @TableField("left_player_battle_info_id")
    private Integer leftPlayerBattleInfoId;
    @TableField("right_player_battle_info_id")
    private Integer rightPlayerBattleInfoId;
    @TableField("create_time")
    private Timestamp createTime;
    @TableField("update_time")
    private Timestamp updateTime;

}

