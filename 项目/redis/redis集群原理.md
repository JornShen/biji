####  复制模式

![](redispic/c12.png)

/usr/local/bin/redis-server  redis服务程序

/usr/local/bin/redis-cli redis客户端

PIDFILE=/var/run/redis_${REDISPORT}.pid

pid 文件位置


1. Redis默认不是以守护进程的方式运行，可以通过该配置项修改，使用yes启用守护进程
    daemonize no
2. 当Redis以守护进程方式运行时，Redis默认会把pid写入/var/run/redis.pid文件，可以通过pidfile指定
    pidfile /var/run/redis.pid


ftp 配置 21

```
anonymous_enable=NO             # 关闭匿名登录
local_enable=YES        # 允许本地用户登录
write_enable=YES        # 启用可以修改文件的 FTP 命令
local_umask=022             # 本地用户创建文件的 umask 值
dirmessage_enable=YES           # 当用户第一次进入新目录时显示提示消息
xferlog_enable=YES      # 一个存有详细的上传和下载信息的日志文件
connect_from_port_20=YES        # 在服务器上针对 PORT 类型的连接使用端口 20（FTP 数据）
xferlog_std_format=YES          # 保持标准日志文件格式
listen=NO               # 阻止 vsftpd 在独立模式下运行
listen_ipv6=YES             # vsftpd 将监听 ipv6 而不是 IPv4，你可以根据你的网络情况设置
pam_service_name=vsftpd         # vsftpd 将使用的 PAM 验证设备的名字
userlist_enable=YES             # 允许 vsftpd 加载用户名字列表
tcp_wrappers=YES        # 打开 tcp 包装器
allow_writeable_chroot=YES

```

命令
```
# systemctl start vsftpd
# systemctl enable vsftpd
# service vsftpd start

```

![](redispic/c13.png)

slaveof ip port
此数据库将成为发送目的数据库的从服务器


配置

```
slaveof 192.168.1.1 6379
```

Sub slaves instead will always receive the replication stream identical to the one sent by the top-level master to the intermediate slaves

主服务器设置密码的时候，从服务器需要

redis-cli 中：

config set masterauth <password>

或者在配置文件中:

masterauth <password>

![](redispic/c14.png)

![](redispic/c15.png)

命令传播

![](redispic/c16.png)

![](redispic/c17.png)

新版的复制功能

PSYNC

![](redispic/c18.png)

![](redispic/c20.png)

部分重同步的实现

![](redispic/c19.png)

![](redispic/c21.png)

![](redispic/c22.png)

![](redispic/c23.png)

![](redispic/c24.png)

![](redispic/c25.png)

服务器运行ID

![](redispic/c26.png)

![](redispic/c27.png)

![](redispic/c28.png)

![](redispic/c29.png)

![](redispic/c30.png)

![](redispic/c31.png)

![](redispic/c32.png)

![](redispic/c33.png)

![](redispic/c34.png)

![](redispic/c35.png)

同步

![](redispic/c36.png)

![](redispic/c37.png)

![](redispic/c38.png)

心跳检测

![](redispic/c39.png)

![](redispic/c40.png)

![](redispic/c41.png)

![](redispic/c42.png)

A--> B --> C

如果 B 挂掉了，C 将不会收到 A 更新操作。而将B重新连接以后，将会重新连接

哨兵模式

![](redispic/d1.png)

![](redispic/d2.png)

![](redispic/d3.png)

![](redispic/d4.png)

![](redispic/d6.png)

![](redispic/d5.png)

![](redispic/d7.png)

![](redispic/d8.png)

![](redispic/d9.png)

![](redispic/d10.png)

![](redispic/d11.png)

![](redispic/d12.png)

![](redispic/d13.png)

![](redispic/d14.png)

![](redispic/d15.png)

![](redispic/d16.png)

![](redispic/d17.png)

![](redispic/d18.png)

![](redispic/d19.png)

![](redispic/d20.png)

![](redispic/d21.png)

![](redispic/d22.png)

![](redispic/d23.png)

![](redispic/d24.png)

![](redispic/d25.png)

![](redispic/d26.png)

![](redispic/d27.png)

![](redispic/d28.png)

![](redispic/d29.png)

![](redispic/d30.png)

![](redispic/d31.png)

选举和故障转移

![](redispic/d32.png)

![](redispic/d33.png)

![](redispic/d34.png)

![](redispic/d36.png)

![](redispic/d35.png)

![](redispic/d37.png)

![](redispic/d38.png)

![](redispic/d39.png)

![](redispic/d40.png)
