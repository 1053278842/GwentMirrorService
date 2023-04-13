package com.ll.gwentmirror.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.gwentmirror.entity.Battle;
import com.ll.gwentmirror.entity.PlayerBattleInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;

import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/28 0:55
 * @Description :
 */
@Mapper
public interface IPlayerBattleInfoMapper extends BaseMapper<PlayerBattleInfo> {

    /**
     * 测试
     * @return
     */
    @ResultMap("mybatis-plus_PlayerBattleInfo")
    List<PlayerBattleInfo> selectAll();
}
