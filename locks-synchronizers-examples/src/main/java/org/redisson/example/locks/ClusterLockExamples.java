package org.redisson.example.locks;

import com.sun.xml.internal.ws.util.StringUtils;

import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.concurrent.TimeUnit;

/**
 * Created by oopcoder at 2022/8/23 22:20 .
 */

public class ClusterLockExamples {

  /**
   * redis连接前缀
   */
  private static final String REDIS_PREFIX = "redis://";

  public static void main(String[] args) throws InterruptedException {
    // connects to 127.0.0.1:6379 by default

    Config config = new Config();
    //集群
    config.useClusterServers().addNodeAddress(
        REDIS_PREFIX + "127.0.0.1:6379",
        //REDIS_PREFIX + "172.31.20.62:6003",
        //REDIS_PREFIX + "172.31.20.62:6004",
        //REDIS_PREFIX + "172.31.20.62:6005",
        REDIS_PREFIX + "172.31.20.81:6000",
        REDIS_PREFIX + "172.31.20.81:6001",
        REDIS_PREFIX + "172.31.20.81:6002");

    RedissonClient redisson = Redisson.create(config);

    RLock lock = redisson.getLock("lock");
    lock.lock(2, TimeUnit.SECONDS);

    Thread t = new Thread() {
      public void run() {
        RLock lock1 = redisson.getLock("lock");
        lock1.lock();
        lock1.unlock();
      }

      ;
    };

    t.start();
    t.join();

    redisson.shutdown();
  }
}
