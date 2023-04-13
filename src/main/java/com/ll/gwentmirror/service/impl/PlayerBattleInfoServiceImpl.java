package com.ll.gwentmirror.service.impl;

import com.ll.gwentmirror.entity.PlayerBattleInfo;
import com.ll.gwentmirror.mapper.IPlayerBattleInfoMapper;
import com.ll.gwentmirror.service.IBattleService;
import com.ll.gwentmirror.service.IPlayerBattleInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
public class PlayerBattleInfoServiceImpl implements IPlayerBattleInfoService {

    @Autowired
    private IPlayerBattleInfoMapper pbiMapper;

    @Override
    public List<Integer> saveBatchAndReturnIds(List<PlayerBattleInfo> playerBattleInfos) {
        List<Integer> ids = new ArrayList<>();
        for (PlayerBattleInfo pbi : playerBattleInfos) {
            pbiMapper.insert(pbi);
            ids.add(pbi.getId());
        }
        return ids;
    }
}
