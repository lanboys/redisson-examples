package org.redisson.example.locks;

/**
 * Created by oopcoder at 2022/8/22 21:54 .
 */

import java.util.concurrent.CompletableFuture;

public class TestMain {

  /**
   * 可以尝试 分别运行 testNormalThread(); 和 testCompletableFuture(); 来感受区别
   *
   * @param args
   */
  public static void main(String[] args) {

    System.out.println("我是Main线程，我结束后，不影响线程的运行：" + Thread.currentThread().getName());
    testNormalThread();
    //testCompletableFuture();
  }

  /**
   * 自己使用 Thread 方式模拟 守护线程和非守护线程
   */
  public static void testNormalThread() {
    Thread thread = new Thread(() -> {
      System.out.println("我是子线程,Main线程结束后，我依然运行：" + Thread.currentThread().getName());

      try {
        while (true) {
          Thread.sleep(1000L);
          System.out.println("======执行操作 A====");
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    // 将线程设置为守护线程
    //        thread.setDaemon(true);
    thread.start();
  }

  /**
   * CompletableFuture 使用 voidCompletableFuture.get() 和不使用 voidCompletableFuture.get() 的区别
   */
  public static void testCompletableFuture() {
    CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
      System.out.println("我是子线程,Main线程结束后，我依然运行：" + Thread.currentThread().getName());

      try {
        Thread.sleep(1000L);
        System.out.println("======执行操作 A====");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }).whenCompleteAsync((r, e) -> {
      System.out.println("======执行操作 B====");
    });
    try {
      // 该方法会阻塞Main线程，告诉Main线程，我执行完了，你才可以结束
      voidCompletableFuture.get();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}