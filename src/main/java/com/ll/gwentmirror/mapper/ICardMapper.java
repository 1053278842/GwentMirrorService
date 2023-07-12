package com.ll.gwentmirror.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ll.gwentmirror.entity.Card;
import com.ll.gwentmirror.entity.Deck;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/24 22:37
 * @Description :
 */
@Mapper
public interface ICardMapper extends BaseMapper<Card> {

    /**
     * search
     * @param cid
     * @return
     */
    List<Card> findByCid(@Param(value = "cid") Integer cid);
}
