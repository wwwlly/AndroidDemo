package com.kemp.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kemp.demo.base.ShowTextActivity;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

/**
 * 1.多个线程执行完后才去执行一个方法；
 */
public class ThreadDemo extends ShowTextActivity {

    private static final String TAG = "ThreadDemo";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        test1();
//        test2();
//        test3();
        test4();
    }

    /**
     * 3个线程顺序执行完再去执行公共方法
     * join()方法是等待该线程终止。其实就是join()方法将挂起调用线程的执行，直到被调用的对象完成它的执行。
     * 这个方法的缺点是效率低，3个线程顺序执行完才去执行公共方法。
     */
    private void test1() {
        Vector<Thread> threads = new Vector<>();
        threads.add(thread1);
        thread1.start();

        threads.add(thread2);
        thread2.start();

        threads.add(thread3);
        thread3.start();

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        commonMethod();
    }

    /**
     * 同一个类MyThread的两个实例的对象属性lock是否是同一个
     */
    private void test2() {
        MyThread thread1 = new MyThread();
        MyThread thread2 = new MyThread();

        Log.d(TAG, "thread1'lock is same to thread2'lock " + (thread1.lock == thread2.lock));
    }

    /**
     * 使用CountDownLatch实现
     * CountDownLatch这个类使一个线程等待其他线程各自执行完毕后再执行
     */
    private void test3() {
        final CountDownLatch latch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            final int p = i + 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "thread" + p + " running");
                    latch.countDown();
                }
            }.start();
        }

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        commonMethod();
    }

    /**
     * 使用CyclicBarrier实现
     * 它的作用就是会让所有线程都等待完成后才会继续下一步行动
     */
    private void test4() {
        final CyclicBarrier barrier = new CyclicBarrier(5);

        for (int i = 0; i < 5; i++) {
            final int p = i + 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Log.d(TAG, "thread" + p + " running");
                    try {
                        barrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        commonMethod();
    }

    private synchronized void commonMethod() {
        Log.d(TAG, "invoke commonMethod");
        appendText("invoke commonMethod");
    }

    private class MyThread extends Thread {

        private Object lock = new Object();

    }

    private Thread thread1 = new Thread() {
        @Override
        public void run() {
            Log.d(TAG, "thread1 running");
//            appendText("thread1 running");
        }
    };

    private Thread thread2 = new Thread() {
        @Override
        public void run() {
            Log.d(TAG, "thread2 running");
//            appendText("thread2 running");
        }
    };

    private Thread thread3 = new Thread() {
        @Override
        public void run() {
            Log.d(TAG, "thread3 running");
//            appendText("thread3 running");
        }
    };
}
