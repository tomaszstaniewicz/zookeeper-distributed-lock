package eee;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class DistributedLockDemo {

    private static CountDownLatch connectionLatch = new CountDownLatch(1);

    public static void main(String[] args) throws IOException, InterruptedException {
        String instanceName = args[0];

        final ZooKeeper zooKeeper = connect("localhost:2181");
                //new ZooKeeper("localhost:2181", 30000, null);
        String lockPath = "/LOCK_PATH";
        String lockName = "DISTRIBUTED_LOCK";

        final DistributedLock distributedLock = new DistributedLock(
                zooKeeper,
                lockPath,
                lockName
        );
        distributedLock.lock();

        for (int i = 0; i < 60; i++) {
            System.out.println("instance " + instanceName + " is processing");
            Thread.sleep(1000);
        }
        distributedLock.unlock();
    }

    public static ZooKeeper connect(String host)
            throws IOException,
            InterruptedException {
        ZooKeeper zoo = new ZooKeeper(host, 20000, we -> {
            if (we.getState() == Watcher.Event.KeeperState.SyncConnected) {
                connectionLatch.countDown();
            }
        });

        connectionLatch.await();
        return zoo;
    }

}
