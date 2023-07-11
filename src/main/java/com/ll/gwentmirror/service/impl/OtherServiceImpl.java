package com.ll.gwentmirror.service.impl;

import com.ll.gwentmirror.entity.Other;
import com.ll.gwentmirror.mapper.IBattleMapper;
import com.ll.gwentmirror.mapper.IOtherMapper;
import com.ll.gwentmirror.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/28 16:15
 * @Description :
 */
@Transactional(rollbackFor = Exception.class)
@Slf4j
@Service
public class OtherServiceImpl implements IOtherService {

    private final IOtherMapper otherMapper;

    public OtherServiceImpl(IOtherMapper otherMapper) {
        this.otherMapper = otherMapper;
    }

    @Override
    public void good() {
        Other other = otherMapper.selectOne(null);
        if (other != null) {
            other.setGood(other.getGood() + 1);
            otherMapper.updateById(other);
        }
    }
}
