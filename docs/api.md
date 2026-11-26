# 接口

端口 8081。网站侧统一走 `/gmapi/*`，由 nginx 去掉前缀转发过来。

## 能力总览

| 能力 | 入口 |
| --- | --- |
| 卡组查询（全量 / 按包含的卡牌 / 最新随机） | `GET /deck/api/getDecks`、`/deck/api/getDecksByIds`、`/deck/api/getRandomDecksLast` |
| 爬虫批量入库 | `POST /deck/api/post`（`List<DeckJson>`） |
| 官网卡组 id 水位 | `GET /deck/api/maxid` |
| 对局战报入库 | `POST /battle/api/save` |
| 版本水位（客户端判断是否更新本地卡组库） | `POST /version/api/getId` |
| 客户端安装包下载 / 探活 | `GET /download`、`/upload`、`/good` |

## 卡组

| 方法 | 路径 | 参数 | 说明 |
| --- | --- | --- | --- |
| GET | `/deck/api/getDecks` | — | 最多 5000 个卡组（客户端同步本地库用） |
| GET | `/deck/api/getDecksByIds` | `ids=1,2,3`、`page`（可选） | `page` 缺省/0 不分页；`page>=1` 每页 5 条 |
| GET | `/deck/api/getRandomDecksLast` | `num`（默认 0） | 版本水位之后的最新卡组；`num=0` 全量 |
| GET | `/deck/api/maxid` | — | 库内最大 `deck_web_id` |
| POST | `/deck/api/post` | JSON 数组 | 批量写入爬虫卡组 |

`getDecksByIds` 的匹配逻辑：卡组 `cards` 串里包含任一传入 id，或 `stratagem_card_id` 命中。

响应是 `DeckJson` 列表：

```jsonc
[{ "deckId": -9219704699830737657, "sortedCtIds": [202406, 202248, ...],
   "leaderCtId": 200164, "stratagemCtId": 202140,
   "time": "2022-11-05T11:51:01.000+00:00", "deckName": "...", "deckAuthor": "...",
   "webDeckId": 406954, "factionId": 32 }]
```

## 战报

`POST /battle/api/save`，报文字段与记牌器 `dao/CardDao.py:getBattleInfo()` 一一对应：

```jsonc
{
  "GameStatus": 3, "GameMode": 1, "GameId": 1732..., "BattleType": 2,
  "roundInfos": [{ "index": 0, "left_score": 12, "right_score": 8,
                   "starting_playerId": 1, "winner_playId": 1, "turn_index": 11 }, ...],
  "curr_round_index": 2, "winner_playId": 1, "end_game_reason": 0,
  "players": [
    { "player_name": "...", "service_id": 900000000000000001, "paragon_level": 0,
      "MMR": 9600, "rank": 0, "leader_id": 200087, "stratagem_id": 0, "faction_id": 4,
      "decks": [200087, 0, 202115, 201779, ...],   // leader + stratagem + 牌表
      "deckId": 1234567890123456789 }              // hash(frozenset(decks))
  ]
}
```

返回空字符串；失败抛 500（事务回滚）。
同一个结构可以用 [scripts/simulate_battle.py](https://github.com/1053278842/GwentMirrorAllInOne/blob/main/scripts/simulate_battle.py) 造。

## 版本

`POST /version/api/getId` 返回 `version` 表整行：

```jsonc
{ "id": 9, "address": 59309712, "addressGOG": 56389248, "addressGY": 0,
  "name": "Ver 0.4.3-beta", "canAutoUpdate": 1, "orderMode": 0, ... }
```

`address` 是客户端读内存的基址偏移；`canAutoUpdate=0` 时客户端会提示手动更新。

## 历史遗留

| 方法 | 路径 | 说明 |
| --- | --- | --- |
| GET | `/download` | 下载 `GwentMirror.7z` 客户端包 |
| POST | `/upload` | 上传客户端包（Linux 落 `/www/jar/GwentMirror/uploads/`） |
| GET | `/good` | 探活/统计埋点，写 `other` 表 |

## 验证

```bash
curl http://localhost:8081/deck/api/maxid
curl "http://localhost:8081/deck/api/getRandomDecksLast?num=3"
curl -X POST http://localhost:8081/version/api/getId
curl "http://localhost:8081/deck/api/getDecksByIds?ids=202140&page=0"
```

## 没有鉴权

接口全部匿名可用，包括 `POST /deck/api/post`。公网部署时请在 nginx 层加限制，
或只把 8081 绑定在 `127.0.0.1`。