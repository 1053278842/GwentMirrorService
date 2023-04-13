package com.ll.gwentmirror.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.gwentmirror.entity.Player;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/28 16:12
 * @Description :
 */
@Mapper
public interface IPlayerMapper extends BaseMapper<Player> {
    /**
     * 批量插入player，主键重复时会自增repeatedTime字段次数
     * @param playerList rt
     */
    void saveBatchAddRepeatedTime(List<Player> playerList);
}
