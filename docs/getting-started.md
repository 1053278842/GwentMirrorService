# 启动与部署

## 前置

| 项 | 要求 |
| --- | --- |
| JDK | 8+（`pom.xml` 里 `java.version=1.8`） |
| Maven | 3.6+ |
| MySQL | 8.0，字符集 `utf8mb4` / 排序规则 `utf8mb4_0900_ai_ci` |

> 排序规则用 `utf8mb4_0900_ai_ci`（MySQL 8 专有），MariaDB 不支持，导入会报错。

## 建库并导入

```bash
mysql -uroot -p -e "CREATE DATABASE GwentMirror CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;"
mysql -uroot -p GwentMirror < GwentMirror.sql
```

`GwentMirror.sql` 是 Navicat 导出的脚本：7 张表 + 477 套样例卡组，`DROP TABLE IF EXISTS` 开头，
重复导入会清空重建。

## 改配置

`src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://<host>:3306/GwentMirror
    username: <user>
    password: <pass>
server:
  port: 8081
```

仓库里的默认值指向作者的线上库，**本地跑必须换掉**。

## 启动

```bash
mvn spring-boot:run
# 或
mvn -DskipTests package && java -jar target/GwentMirror-*.jar --server.port=8081
```

## 验证

```bash
curl http://localhost:8081/deck/api/maxid
curl "http://localhost:8081/deck/api/getRandomDecksLast?num=3"
curl -X POST http://localhost:8081/version/api/getId
```

三条都有返回就说明「服务 → MySQL」通了。卡组数量：

```bash
mysql -ugwent -pgwent123 GwentMirror -e "select count(*) from deck;"
```

## 在一体化容器里

容器里不需要你装 JDK 或 MySQL，服务由 `docker/wait-for-mysql.sh` 拉起：

* 配置**不走** `application.yml`，而是命令行参数覆盖：
  `--spring.datasource.url/username/password`，账号是 `gwent / gwent123`；
* 数据库地址固定 `127.0.0.1:3306`（同容器内的 mysqld），不对外暴露；
* 启动前脚本会循环探测 `mysql -h127.0.0.1 -ugwent -p…`，直到能 `SELECT 1` 才 `exec java`，
  避免后端先于数据库启动而崩；
* 日志在容器内 `/var/log/gwent/backend.log`（`docker logs -f gwent-mirror` 也能看到），
  MyBatis 的 stdout SQL 日志较啰嗦，排查问题时可临时把 `mybatis-plus.configuration.log-impl` 关掉。

完整镜像说明见 [GwentMirrorAllInOne 的 docker.md](https://github.com/1053278842/GwentMirrorAllInOne/blob/main/docs/docker.md)。