mac os安装zookeeper步骤：

1、验证mac支持brew安装zookeeper：brew info zookeeper

2、安装：brew install zookeeper

3、安装后目录：ls /usr/local/etc/zookeeper。已经有了缺省的配置文件了

     defaults        log4j.properties    zoo.cfg         zoo_sample.cfg
    
4、启动服务
    
    zkServer
    zkServer status(查看zkServer状态)
    zkServer start （启动zkServer）