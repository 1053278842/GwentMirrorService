# 数据库

## 表结构

7 张表，全部在 [../GwentMirror.sql](../GwentMirror.sql)。

| 表 | 说明 |
| --- | --- |
| `deck` | 卡组。`cards` 是逗号分隔的卡牌 id 串；`id` = 卡组指纹；`deck_web_id` = 官网 id；`total` = 命中次数 |
| `player` | 玩家，`service_id` 主键，重复出现 `repeated_times + 1` |
| `battle` | 一场对局：`game_id`、模式、结束原因、左右两条 `player_battle_info` |
| `player_battle_info` | 单侧战报：牌表 id、胜负、逐回合小分（JSON） |
| `card` | 卡牌缓存表（前端已改用 `allCard.json`，保留兼容） |
| `version` | 版本水位线，`/version/api/getId` 用它判断客户端卡组库是否更新 |
| `other` | 杂项键值（运维用） |

字符集 `utf8mb4` / 排序规则 `utf8mb4_0900_ai_ci`。

## 卡组指纹去重

客户端和爬虫都用「牌表集合的哈希」当 `deck.id`（与顺序无关），入库走：

```sql
INSERT INTO deck (...) VALUES (...)
ON DUPLICATE KEY UPDATE
  total  = total + 1,
  name   = IF(time < VALUES(time), name, VALUES(name)),
  author = IF(time < VALUES(time), author, VALUES(author)),
  time   = IF(time < VALUES(time), time, VALUES(time));
```

同一套牌反复出现只累加 `total` 并刷新最新名称/时间，表不会膨胀。

对应的 Python 端（客户端与爬虫一致）：

```python
def transListCtIdToHash(decks):
    return hash(frozenset(decks))     # 与出牌顺序无关；可能为负，BIGINT 存得下
```

## 战报四表事务

`POST /battle/api/save` 一个事务内：

```text
player          INSERT ... ON DUPLICATE KEY UPDATE repeated_times + 1
deck            INSERT ... ON DUPLICATE KEY UPDATE total + 1
battle          INSERT（自增主键）
player_battle_info  INSERT ×2（左右各一，回填 game_id）
battle          UPDATE left/right_player_battle_info_id
```

落库后能看到一场对局把左右两条战报串起来：

```text
id  game_id         game_status  game_mode  game_battle_type  left_pbi  right_pbi
1   1795512379954   3            1          2                 1         2
2   1795512380477   3            1          2                 3         4
```

## 按包含卡牌检索

```sql
SELECT ... FROM deck
WHERE 1=1
  <foreach collection="idArray" item="cid" open="and" separator="and">
    LOCATE(#{cid}, cards) > 0 OR stratagem_card_id = #{cid}
  </foreach>
ORDER BY ${sortFiled} DESC
LIMIT #{pageStart}, #{pageEnd}
```

`cards` 存的是逗号分隔的 id 串，用 `LOCATE` 做子串匹配；
因为战略卡 id 不一定写进 `cards`（客户端单独报 `stratagem_card_id`），额外 `OR` 上它。

> 注意 `LOCATE` 是全表扫描，卡组量上万后会明显变慢；要优化得建关联表或加全文索引。

## 常用 SQL

```sql
-- 总量与水印
select count(*) from deck;
select max(deck_web_id) from deck;

-- 最近入库的 10 套
select id, name, author, deck_web_id, create_time from deck order by create_time desc limit 10;

-- 包含某张卡的卡组（等价于后端查询）
select id, name from deck where locate('202140', cards) > 0 or stratagem_card_id = 202140;

-- 对局与左右战报
select b.id, b.game_id, b.left_player_battle_info_id, b.right_player_battle_info_id
from battle b order by b.id desc limit 5;
```

## 字段陷阱

* `deck.cards` 是字符串不是关联表，「包含某张牌」只能用 `LOCATE`；
* 战略卡 id 可能只在 `stratagem_card_id` 里，查询别漏；
* `deck.id` 可能为负（Python `hash()` 的取值），列类型是 `BIGINT`，不要改成 `UNSIGNED`；
* `time` 是卡组发布时间（来自官网），`create_time` 是入库时间——前端「最新」用的是 `create_time`。