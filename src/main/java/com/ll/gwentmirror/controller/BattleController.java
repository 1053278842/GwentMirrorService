package com.ll.gwentmirror.controller;

import com.ll.gwentmirror.entity.Battle;
import com.ll.gwentmirror.entity.Deck;
import com.ll.gwentmirror.entity.Player;
import com.ll.gwentmirror.entity.PlayerBattleInfo;
import com.ll.gwentmirror.service.IBattleService;
import com.ll.gwentmirror.service.IDeckService;
import com.ll.gwentmirror.service.IPlayerBattleInfoService;
import com.ll.gwentmirror.service.IPlayerService;
import com.ll.gwentmirror.utils.BeansUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * |       |\__/,|   (`\
 * |    _.|o o  |_   ) )
 * |  -(((---(((--------
 * if sudden problems,please don't look for me,Thank you~
 *
 * @Author :      Liu Long
 * @CreateTime :  2022/10/26 16:26
 * @Description :
 */
@Slf4j
@RestController
@RequestMapping(("/battle/api"))
public class BattleController {

    @Autowired
    private IBattleService battleService;


    @PostMapping("/save")
    public String save(@RequestBody Map<String,Object> battleInfoJson){
        log.info("获得战斗数据！");
        List<Player> playerList = BeansUtils.battleInfoJsonMapToPlayerList(battleInfoJson);
        Deck deck = BeansUtils.battleInfoJsonMapToDeckList(battleInfoJson);
        Battle battle = BeansUtils.battleInfoJsonToBattle(battleInfoJson);
        List<PlayerBattleInfo> playerBattleInfos = BeansUtils.battleInfoJsonMapToPlayerBattleInfoList(deck,battle,battleInfoJson);
        battleService.saveBattleInfo(playerList,deck,battle,playerBattleInfos);

        log.info(battleInfoJson.toString());
        log.info(playerList.toString());
        log.info(deck.toString());
        log.info(battle.toString());
        log.info(playerBattleInfos.toString());

        return "";
    }
}
