# 相对上游的改动

上游代码直接跑会踩三个必现的 500。改动都很小，但每个都卡住一整条链路。

## 1. `entity/Deck.java`：`cardList` 被当成数据库列

`Deck` 里有一个运行期派生字段：

```java
private List<Card> cardList;
```

MyBatis-Plus 默认把驼峰字段映射成下划线列名，于是 `selectList()` 生成的 SQL 变成
`SELECT …, card_list FROM deck`，而表里没有这一列：

```text
java.sql.SQLSyntaxErrorException: Unknown column 'card_list' in 'field list'
```

`/deck/api/getDecks` 直接 500。修法是声明它不存在于表中：

```java
@TableField(exist = false)
private List<Card> cardList;
```

## 2. `DeckController.getRandomDecksLast`：把随机数当比较器

```java
// 原实现
allDeck.stream()
    .map(BeansUtils::deckToDeckJons)
    .sorted((a, b) -> new Random().nextInt())   // 违反比较器契约
    .limit(num)
```

比较器必须满足传递性，随机返回值不满足；数据一多，TimSort 会直接抛：

```text
java.lang.IllegalArgumentException: Comparison method violates its general contract!
```

现象是首页随机推荐偶发 500（卡组越多越容易触发）。改成先转 DTO 再洗牌：

```java
List<DeckJson> deckJsons = allDeck.stream().map(BeansUtils::deckToDeckJons).collect(Collectors.toList());
Collections.shuffle(deckJsons, new Random());
return deckJsons.stream().limit(num).collect(Collectors.toList());
```

## 3. `mapper/PlayerMapper.xml`：`rank` 是 MySQL 8 保留字

```sql
INSERT INTO player (service_id, name, paragon_level, mmr, rank) VALUES ...
```

MySQL 8.0.2 起 `RANK` 是窗口函数保留字，未加引号时报：

```text
You have an error in your SQL syntax; ... near 'rank) VALUES'
```

整个 `/battle/api/save` 因此 500（`player` 是事务里第一个插入）。修法是加反引号：

```sql
INSERT INTO player (service_id, name, paragon_level, mmr, `rank`) VALUES ...
ON DUPLICATE KEY UPDATE ..., `rank` = VALUES(`rank`), ...
```

## 改动波及面

三处都不改接口签名、不改表结构，只影响 SQL 生成与排序，
所以前端与爬虫无需配合；上游若要合回去，三处都是单文件小改。

> 背景：这些是整合时为了让服务真的能跑起来而做的修复，
> 逐条原因写在 [GwentMirrorAllInOne 的 changes.md](https://github.com/1053278842/GwentMirrorAllInOne/blob/main/docs/changes.md) 里。