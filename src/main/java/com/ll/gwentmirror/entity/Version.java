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
 * @CreateTime :  2022/10/29 16:00
 * @Description :
 */
@Data
@TableName("version")
public class Version implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @TableField("address")
    private Integer address;

    @TableField("address_gog")
    private Integer addressGOG;

    @TableField("address_gy")
    private Integer addressGY;

    @TableField("name")
    private String name;

    @TableField("start_time")
    private Timestamp startTime;

    @TableField("context")
    private String context;

    @TableField("create_time")
    private Timestamp createTime;

    @TableField("update_time")
    private Timestamp updateTime;

    @TableField("can_auto_update")
    private Integer canAutoUpdate;

    @TableField("order_mode")
    private Integer orderMode;
}
