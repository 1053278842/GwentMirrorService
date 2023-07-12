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
 * @CreateTime :  2023/06/28 16:17
 * @Description :
 */
@TableName("card")
@Data
public class Card implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField("cid")
    private Integer cid;
    @TableField("name")
    private String name;
    @TableField("description")
    private String description;
    @TableField("tip")
    private String tip;
    @TableField("availability")
    private Integer availability;
    @TableField("rarity")
    private Integer rarity;
    @TableField("factionId")
    private Integer factionId;
    @TableField("secondaryFactionId")
    private Integer secondaryFactionId;
    @TableField("tier")
    private Integer tier;
    @TableField("power")
    private Integer power;
    @TableField("provision")
    private Integer provision;
    @TableField("initialTimer")
    private Integer initialTimer;
    @TableField("armor")
    private Integer armor;
    @TableField("type")
    private Integer type;

}

