package com.elend.gate.notify;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * zookeeper仲裁
 *
 * @author liyongquan
 */
public abstract class Arbitrator implements Watcher {
    private final static Logger log = LoggerFactory.getLogger(Arbitrator.class);

    private CountDownLatch connectedSignal = new CountDownLatch(1);

    private ZooKeeper zk;

    /**
     * 根节点
     */
    private String root;

    /**
     * 子节点前缀
     */
    private String prefix;

    /**
     * 集群url格式：例如：183.136.136.166:2181,113.106.100.63:2181,115.238.170.51:2181
     */
    private String hosts;

    /** 登录ID */
    private long regId;

    /**
     * 连接的超时时间, 毫秒
     */
    private int sessionTimeOut = 5000;

    /**
     * 初始化方法
     */
    public void init() {
        log.info("Arbitrator init...");
        try {
            connectedSignal = new CountDownLatch(1);
            zk = new ZooKeeper(hosts, sessionTimeOut, this);
            // 等待连接完成
            connectedSignal.await();
            // 建立根节点
            Stat state = zk.exists(root, true);
            if (state == null) {
                zk.create(root, new byte[0], Ids.OPEN_ACL_UNSAFE,
                          CreateMode.PERSISTENT);
            }
            // 注册子节点
            String path = zk.create(root + "/" + prefix, new byte[0],
                                    Ids.OPEN_ACL_UNSAFE,
                                    CreateMode.EPHEMERAL_SEQUENTIAL);
            regId = Long.parseLong(path.substring(new String(root + "/"
                    + prefix).length()));
            log.info("zookeeper 创建节点 {}成功",path);
        } catch (Exception e) {
            log.error("init Arbitrator error:" + e);
            e.printStackTrace();
        }
    }

    public boolean isMaster() throws KeeperException, InterruptedException {
        List<String> list = zk.getChildren(root, true);
        long min = -1;
        // 获取最小的id作为master
        for (String l : list) {
            long id = Long.parseLong(l.substring(prefix.length()));
            if (min < 0 || id < min) {
                min = id;
            }
        }
        if (min == regId) {
            return true;
        }
        return false;
    }

    public void process(WatchedEvent event) {
        // 连接建立, 回调process接口时, 其event.getState()为KeeperState.SyncConnected
        if (event.getState() == KeeperState.SyncConnected) {
            // 放开闸门, wait在connect方法上的线程将被唤醒
            connectedSignal.countDown();
            log.info("zookeeper SyncConnected");
        } else if (event.getState() == KeeperState.Expired) {// session超时处理
            log.info("zookeeper session expired. now rebuilding");
            // session expired, may be never happending.
            // close old client and rebuild new client
            close();
            init();
        }
        try {
            if (isMaster()) {
                masterEvent();
            } else {
                notMasterEvent();
            }
        } catch (KeeperException|InterruptedException e) {
            log.error("zookeeper仲裁类异常,"+e.getMessage(),e);
        } 
    }

    public void close() {
        log.info("Arbitrator close");
        if (zk != null) {
            try {
                zk.close();
                zk = null;
            } catch (InterruptedException e) {
                // ignore exception
            }
        }
    }

    /**
     * 仲裁时如果是master触此事件
     */
    public abstract void masterEvent();

    /**
     * 仲裁时不为master触发此事件
     */
    public abstract void notMasterEvent();

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getHosts() {
        return hosts;
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public int getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(int sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    public ZooKeeper getZk() {
        return zk;
    }

    public void setZk(ZooKeeper zk) {
        this.zk = zk;
    }

}
