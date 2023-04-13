package com.ll.gwentmirror.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface IDeckMapper extends BaseMapper<Deck> {

    /**
     * 批量插入deck
     *
     * @param list Deck 列表
     */
    void insertBatchDeck(List<Deck> list);

    /**
     *  插入一个deck，做去重处理，同时会增加deck中的total字段
     * @param deck Deck表对象
     */
    void insertDeckOnDuplication(Deck deck);

    /**
     * search input
     * @param idArray
     * @param sortFiled
     * @param pageStart
     * @param pageEnd
     * @param isNoPage 是否分页->limit
     * @return
     */
    List<Deck> getListByIds(@Param(value = "idArray")String[] idArray,@Param(value = "sortFiled") String sortFiled,
                            @Param(value = "pageStart")int pageStart,@Param(value = "pageEnd") int pageEnd,@Param(value = "isNoPage") boolean isNoPage);
}
