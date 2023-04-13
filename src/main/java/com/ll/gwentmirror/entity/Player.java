package com.ll.gwentmirror.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/24 22:47
 * @Description :
 */
@Data
@TableName("player")
public class Player implements Serializable {

    @TableId(value="service_id")
    private Long serviceId;

    @TableField("name")
    private String name;

    @TableField("paragon_level")
    private Integer paragonLevel;

    @TableField("mmr")
    private Integer mmr;

    @TableField("rank")
    private Integer rank;

    @TableField("create_time")
    private Timestamp createTime;

    @TableField("update_time")
    private Timestamp updateTime;

    @TableField("repeated_times")
    private Integer repeatedTimes;

}
