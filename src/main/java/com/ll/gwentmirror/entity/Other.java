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
@TableName("other")
@Data
public class Other implements Serializable {
    @TableId(value="id",type = IdType.AUTO)
    private Long id;
    @TableField("good")
    private Integer good;
}

