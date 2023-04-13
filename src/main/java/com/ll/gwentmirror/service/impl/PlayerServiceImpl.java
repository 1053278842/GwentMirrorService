package com.ll.gwentmirror.service.impl;

import com.ll.gwentmirror.entity.Player;
import com.ll.gwentmirror.mapper.IPlayerMapper;
import com.ll.gwentmirror.service.IPlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
public class PlayerServiceImpl implements IPlayerService {

    @Autowired
    private IPlayerMapper playerMapper;

    @Override
    public void saveList(List<Player> playerList) {
        playerMapper.saveBatchAddRepeatedTime(playerList);
    }
}
