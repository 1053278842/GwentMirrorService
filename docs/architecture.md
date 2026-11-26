# 代码结构与关键实现

## 目录

```text
src/main/java/com/ll/gwentmirror/
├─ GwentMirrorApplication.java     # 启动类
├─ config/WebMvcConfig.java        # /static/** 资源映射
├─ controller/
│  ├─ DeckController.java          # 卡组查询 + 批量写入 + 官网 id 水位
│  ├─ BattleController.java        # 战报接收
│  ├─ VersionController.java       # 版本水位
│  └─ FileController.java          # 安装包上传/下载
├─ entity/                         # Deck / DeckJson / Player / Battle / PlayerBattleInfo / Version / Card
├─ mapper/                         # MyBatis-Plus Mapper（自定义 SQL 在 resources/mapper/*.xml）
├─ service/impl/
│  ├─ DeckServiceImpl.java         # 分页、按 id 集合匹配、版本增量
│  └─ BattleServiceImpl.java       # 一个事务里写 battle + 2×player_battle_info + deck
└─ utils/BeansUtils.java           # 客户端 JSON → 实体（最核心的转换层）
```

数据库表结构 + 477 个样例卡组在 [../GwentMirror.sql](../GwentMirror.sql)，
容器首次启动时由 `docker/entrypoint.sh` 自动导入。

## 三层职责

| 层 | 只做一件事 |
| --- | --- |
| Controller | 接参、做参数兜底（如 `ids` 去引号、`page` 缺省当 0） |
| ServiceImpl | 事务边界、组装 DTO、调用多个 Mapper |
| Mapper / XML | 手写 SQL：批量插入、`ON DUPLICATE KEY`、`LOCATE` 匹配 |

`@Transactional(rollbackFor = Exception.class)` 加在 `DeckServiceImpl` 与 `BattleServiceImpl` 类上，
所以一局战报要么全成功、要么全回滚。

## 战报转换（BeansUtils）

`POST /battle/api/save` 的报文是记牌器从游戏内存读出来的原始结构
（字段名见 `desktop/main/dao/CardDao.py:getBattleInfo`）：

```jsonc
{ "GameStatus": 3, "GameMode": 1, "GameId": 1732..., "BattleType": 2,
  "roundInfos": [...], "curr_round_index": 2, "winner_playId": 1, "end_game_reason": 0,
  "players": [ { "player_name": "...", "service_id": 900..., "MMR": 9600, "rank": 0,
                 "leader_id": 200087, "stratagem_id": 0, "faction_id": 4,
                 "decks": [200087, 0, 202115, ...], "deckId": 123456789 } ] }
```

`BeansUtils` 拆成四类实体：

| 方法 | 产出 | 说明 |
| --- | --- | --- |
| `battleInfoJsonMapToPlayerList()` | `List<Player>` | `service_id / name / paragon_level / MMR / rank` |
| `battleInfoJsonMapToDeckList()` | `Deck` | 取「牌表长度 > 2」的那一方，`decks` 拼成逗号串，`name = By.<玩家名>` |
| `battleInfoJsonToBattle()` | `Battle` | 模式、结束原因、`game_id` |
| `battleInfoJsonMapToPlayerBattleInfoList()` | `List<PlayerBattleInfo>` | 逐回合小分存 JSON，`winner_playId` 换算成 `is_win` |

## 事务顺序（BattleServiceImpl）

```text
1. playerService.saveList()      -- INSERT ... ON DUPLICATE KEY UPDATE repeated_times+1
2. deckService.saveDeck()        -- INSERT ... ON DUPLICATE KEY UPDATE total+1 / 刷新名称时间
3. battleMapper.insert(battle)   -- 拿到自增主键 -> battleId
4. pbiService.saveBatchAndReturnIds()  -- 两条战报插入，回填 game_id，拿回两个 id
5. battleMapper.updateById()     -- 回填 left/right_player_battle_info_id
```

## 分页与排序

`DeckServiceImpl.getListByIds()` 有两种模式：

| 调用 | 行为 |
| --- | --- |
| `page` 缺省或 0 | 不分页，返回全部命中卡组（按 `update_time` 倒序） |
| `page >= 1` | 每页 5 条，`LIMIT (page-1)*5, 5` |

`getListLast()` 取的是「`create_time` 晚于版本表最新记录」的卡组——也就是本次版本之后新增的，
供前端首屏随机推荐。

## 编码要留意的两处

* `Player` 表的 `rank` 是 MySQL 8 保留字，XML 里必须写 `` `rank` ``；
* `Deck.cardList` 是运行期派生字段，实体上必须标 `@TableField(exist = false)`，
  否则 MyBatis-Plus 会把它当列查（详见 [changes.md](changes.md)）。