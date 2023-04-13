package com.ll.gwentmirror.service.impl;

import com.ll.gwentmirror.entity.Battle;
import com.ll.gwentmirror.entity.Deck;
import com.ll.gwentmirror.entity.Player;
import com.ll.gwentmirror.entity.PlayerBattleInfo;
import com.ll.gwentmirror.mapper.IBattleMapper;
import com.ll.gwentmirror.service.IBattleService;
import com.ll.gwentmirror.service.IDeckService;
import com.ll.gwentmirror.service.IPlayerBattleInfoService;
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
public class BattleServiceImpl implements IBattleService {

    private final IPlayerService playerService;
    private final IDeckService deckService;
    private final IPlayerBattleInfoService pbiService;

    private final IBattleMapper battleMapper;

    public BattleServiceImpl(IPlayerService playerService, IDeckService deckService, IPlayerBattleInfoService pbiService, IBattleMapper battleMapper) {
        this.playerService = playerService;
        this.deckService = deckService;
        this.pbiService = pbiService;
        this.battleMapper = battleMapper;
    }

    @Override
    public void saveBattleInfo(List<Player> playerList, Deck deck, Battle battle, List<PlayerBattleInfo> playerBattleInfos) {
        // 1。插入battle 获取返回的id
        // 2。根据id赋值pbi,插入获取返回的两个id
        // 3. 将两个pbi id 按照顺序赋值给battle,更新数据库
        playerService.saveList(playerList);
        deckService.saveDeck(deck);
        Battle insertedBattle = this.saveBattle(battle);
        Long battleId = insertedBattle.getGameId();
        for (PlayerBattleInfo pbi : playerBattleInfos) {
            pbi.setGameId(battleId);
        }
        List<Integer> pbiIds = pbiService.saveBatchAndReturnIds(playerBattleInfos);
        insertedBattle.setLeftPlayerBattleInfoId(pbiIds.get(0));
        insertedBattle.setRightPlayerBattleInfoId(pbiIds.get(1));
        this.updateDeck(insertedBattle);
    }

    @Override
    public Battle saveBattle(Battle battle) {
        battleMapper.insert(battle);
        return battle;
    }

    @Override
    public void updateDeck(Battle battle) {
        battleMapper.updateById(battle);
    }
}
