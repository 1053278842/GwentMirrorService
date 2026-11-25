# GwentMirrorService · 卡组镜像站后端

Spring Boot 2.6.3 + MyBatis-Plus + MySQL 的卡组/战报服务。
桌面记牌器上报对局、爬虫灌卡组、网站查询卡组，都打到这一个服务上。

> 本目录是 [gwent-mirror-all](../README.md) 一体化项目的后端层；
> 单独跑、单独改都可以，容器里它是 `docker/wait-for-mysql.sh` 拉起的那个 Java 进程（端口 8081）。

---

## 1. 它负责什么

| 能力 | 入口 |
| --- | --- |
| 卡组查询（全量 / 按包含的卡牌 / 最新随机） | `GET /deck/api/getDecks`、`/deck/api/getDecksByIds`、`/deck/api/getRandomDecksLast` |
| 爬虫批量入库 | `POST /deck/api/post`（`List<DeckJson>`） |
| 官网卡组 id 水位 | `GET /deck/api/maxid` |
| 对局战报入库 | `POST /battle/api/save` |
| 版本水位（客户端判断是否更新本地卡组库） | `POST /version/api/getId` |
| 客户端安装包下载 / 探活 | `GET /download`、`/upload`、`/good` |

## 2. 代码结构

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

数据库表结构 + 477 个样例卡组在 [GwentMirror.sql](GwentMirror.sql)，
容器首次启动时由 `docker/entrypoint.sh` 自动导入。

## 3. 单独运行

```bash
# 1) 准备库（MySQL 8，字符集 utf8mb4 / 排序规则 utf8mb4_0900_ai_ci）
mysql -uroot -p -e "CREATE DATABASE GwentMirror CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;"
mysql -uroot -p GwentMirror < GwentMirror.sql

# 2) 改配置：src/main/resources/application.yml 的 datasource
#    （仓库里默认指向作者的线上库，本地跑必须换掉）

# 3) 启动
mvn spring-boot:run
# 或
mvn -DskipTests package && java -jar target/GwentMirror-*.jar --server.port=8081
```

验证：

```bash
curl http://localhost:8081/deck/api/maxid
curl "http://localhost:8081/deck/api/getRandomDecksLast?num=3"
curl -X POST http://localhost:8081/version/api/getId
```

## 4. 关键实现

### 4.1 卡组指纹去重

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

### 4.2 战报拆解（`BeansUtils` + `BattleServiceImpl`）

`POST /battle/api/save` 的报文是记牌器从游戏内存读出来的原始结构（字段名见 `desktop/main/dao/CardDao.py:getBattleInfo`）：

```jsonc
{ "GameStatus": 3, "GameMode": 1, "GameId": 1732..., "BattleType": 2,
  "roundInfos": [...], "curr_round_index": 2, "winner_playId": 1, "end_game_reason": 0,
  "players": [ { "player_name": "...", "service_id": 900..., "MMR": 9600, "rank": 0,
                 "leader_id": 200087, "stratagem_id": 0, "faction_id": 4,
                 "decks": [200087, 0, 202115, ...], "deckId": 123456789 } ] }
```

服务端拆成 `player` / `deck` / `battle` / `player_battle_info` 四类实体，同一事务内：
插 battle 拿主键 → 两条 pbi 插入拿 id → 回填 battle 的 `left/right_player_battle_info_id`。

### 4.3 按包含卡牌检索

```sql
SELECT ... FROM deck
WHERE 1=1
  <foreach collection="idArray" item="cid" open="and" separator="and">
    LOCATE(#{cid}, cards) > 0 OR stratagem_card_id = #{cid}
  </foreach>
ORDER BY ${sortFiled} DESC
LIMIT #{pageStart}, #{pageEnd}
```

`cards` 存的是逗号分隔的 id 串，用 `LOCATE` 做子串匹配；因为战略卡 id 不一定写进 `cards`，额外补了 `stratagem_card_id` 条件。

## 5. 相对上游修的两个必现问题

| 位置 | 现象 | 修法 |
| --- | --- | --- |
| `entity/Deck.java` | `cardList` 是运行期派生字段，MyBatis-Plus 却把它当 `card_list` 列查，`/deck/api/getDecks` 直接 500（`Unknown column 'card_list'`） | 加 `@TableField(exist = false)` |
| `controller/DeckController.java` | `/getRandomDecksLast` 用 `sorted((a,b) -> new Random().nextInt())` 当比较器，违反 TimSort 传递性契约，数据一多就抛 `Comparison method violates its general contract!` → 随机 500 | 先转 DTO 再 `Collections.shuffle` 取前 N |
| `resources/mapper/PlayerMapper.xml` | MySQL 8.0.2 起 `RANK` 是保留字，插玩家时 SQL 语法错误，`/battle/api/save` 全链路 500 | 列名加反引号 `` `rank` `` |

## 6. 和一键容器的关系

* 容器里用 `SPRING_DATASOURCE_*` 之外的方式覆盖配置：`docker/wait-for-mysql.sh` 以命令行参数传
  `--spring.datasource.url/username/password`，账号是 `gwent/gwent123`；
* 数据库地址固定 `127.0.0.1:3306`（同容器内的 mysqld），不对外暴露；
* 日志在容器内 `/var/log/gwent/backend.log`（`docker logs -f gwent-mirror` 也能看到），
  MyBatis 的 stdout SQL 日志较啰嗦，排查问题时可临时把 `mybatis-plus.configuration.log-impl` 关掉。