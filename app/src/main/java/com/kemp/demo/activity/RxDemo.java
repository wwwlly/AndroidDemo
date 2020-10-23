package com.kemp.demo.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.kemp.demo.R;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava是一个基于事件流、实现异步操作的库。
 * <p>
 * 被观察者（Observable）通过 事件（Event）订阅（Subscribe）观察者（Observer）。
 * 意思是观察者观察被观察者。
 */
public class RxDemo extends AppCompatActivity {

    private static final String TAG = "RxDemo";

    private Subscription mSubscription; // 用于保存Subscription对象

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx);

//        test1();
//        test2();
//        test3();
//        test4();
//        test5();
//        test6();
//        test7();
//        test8();
//        test9();
        test10();
//        testBackpressure4();
//        testDo();
    }

    /**
     * 参考jianshu.com/p/a406b94f3188
     */
    private void test1() {
        //1. 创建被观察者 Observable 对象
        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            //在复写的subscribe（）里定义需要发送的事件
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "生产事件");
                // 通过 ObservableEmitter类对象产生事件并通知观察者
                // ObservableEmitter类介绍
                // a. 定义：事件发射器
                // b. 作用：定义需要发送的事件 & 向观察者发送事件
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        });

        //2. 创建观察者 （Observer ）对象
        Observer<Integer> observer = new Observer<Integer>() {
            // 观察者接收事件前，默认最先调用复写 onSubscribe（）
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "开始采用subscribe连接");
            }

            // 当被观察者生产Next事件 & 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onNext(Integer value) {
                Log.d(TAG, "对Next事件作出响应" + value);
            }

            // 当被观察者生产Error事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "对Error事件作出响应");
            }

            // 当被观察者生产Complete事件& 观察者接收到时，会调用该复写方法 进行响应
            @Override
            public void onComplete() {
                Log.d(TAG, "对Complete事件作出响应");
            }
        };

        observable.subscribe(observer);
    }

    /**
     * 轮训 无限次 无条件
     * 使用interval实现
     */
    private void test2() {
        Observable.interval(2, 1, TimeUnit.SECONDS)
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.d(TAG, "第 " + aLong + " 次轮训");
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Log.d(TAG, "onNext " + aLong);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    /**
     * 多级缓存，依次从内存缓存、磁盘缓存、网络获取数据
     * <p>
     * concat 组合多个被观察者一起发送数据，合并后 按发送顺序串行执行
     */
    private void test3() {

        //内存Observable
        Observable<String> memory = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //模拟获取内存缓存数据
                String memoryCache = null;
//                String memoryCache = "内存缓存的中数据";

                if (memoryCache != null) {
                    emitter.onNext(memoryCache);
                } else {
                    emitter.onComplete();
                }
            }
        });

        //磁盘Observable
        Observable<String> disk = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                //模拟获取磁盘缓存数据
                String diskCache = null;
//                String diskCache = "磁盘缓存中的数据";

                if (diskCache != null) {
                    emitter.onNext(diskCache);
                } else {
                    emitter.onComplete();
                }
            }
        });

        //网络Observable
        Observable<String> net = Observable.just("服务器中的数据");

        Observable.concat(memory, disk, net)
                .firstElement()
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.d(TAG, "最终获取的数据来源 =  " + s);
                    }
                });
    }

    /**
     * 合并数据源
     * <p>
     * concat 组合多个被观察者一起发送数据，合并后 按发送顺序串行执行
     * merge 组合多个被观察者一起发送数据，合并后 按时间线并行执行
     * zip 合并多个被观察者（Observable）发送的事件，生成一个新的事件序列（即组合过后的事件序列），并最终发送
     * reduce 把被观察者需要发送的事件聚合成1个事件 & 发送
     * collect 将被观察者Observable发送的数据事件收集到一个数据结构里
     * startWith 在一个被观察者发送事件前，追加发送一些数据或一个新的被观察者
     * count 统计被观察者发送事件的数量
     */
    @SuppressLint("CheckResult")
    private void test4() {

        Observable<String> file = Observable.just("本地文件");
        Observable<String> net = Observable.just("网络");

        Observable.merge(file, net)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "数据源：" + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });

        Observable.zip(file, net, new BiFunction<String, String, String>() {
            @Override
            public String apply(String s, String s2) throws Exception {
                return s + "和" + s2;
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "数据源：" + s);
            }
        });
    }

    /**
     * 线程控制（切换 / 调度 ）
     * <p>
     * subscribeOn()设置被观察者工作线程类型
     * observeOn()设置观察者工作线程类型
     * <p>
     * 调度的线程类型
     * Schedulers.immediate() 不指定工作线程 默认
     * AndroidSchedulers.mainThread() Android主线程 操作UI
     * Schedulers.newThread() 常规新线程 耗时等操作
     * Schedulers.io() io操作线程 网络请求、读写文件等io密集型操作
     * Schedulers.computation() CPU计算操作线程 大量计算操作
     */
    private void test5() {

        Observable<Integer> observable = Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "被观察者工作线程：" + Thread.currentThread().getName());

                emitter.onNext(1);
                emitter.onComplete();
            }
        });

        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe");
                Log.d(TAG, "观察者工作线程：" + Thread.currentThread().getName());
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext " + integer);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };

        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 功能防抖
     */
    private void test6() {

        Button button = findViewById(R.id.btn1);

        RxView.clicks(button)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(Object o) {
                        Log.d(TAG, "onNext");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    /**
     * RXandroid监听edittext输入事件
     */
    private void test7() {

        EditText et = findViewById(R.id.et);

        RxTextView.textChanges(et)
                .debounce(1, TimeUnit.SECONDS).skip(1)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CharSequence>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        Log.d(TAG, "输入字符：" + charSequence);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    /**
     * 对于异步订阅关系，存在 被观察者发送事件速度 与观察者接收事件速度 不匹配的情况
     * <p>
     * 执行此方法会导致内存溢出
     */
    private void test8() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

                for (int i = 0; ; i++) {
                    Log.d(TAG, "发送了事件" + i);
                    Thread.sleep(10);
                    // 发送事件速度：10ms / 个
                    emitter.onNext(i);
                }

            }
        }).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                .subscribe(new Observer<Integer>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.d(TAG, "开始采用subscribe连接");
                    }

                    @Override
                    public void onNext(Integer value) {

                        try {
                            // 接收事件速度：5s / 个
                            Thread.sleep(5000);
                            Log.d(TAG, "接收到了事件" + value);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }

                });
    }

    /**
     * Flowable基本使用
     * 背压策略 参考（https://www.jianshu.com/p/ceb48ed8719d）
     * <p>
     * 原理：
     * 1.避免 出现事件发送和接收速度不匹配；通过响应式拉取Subscription.request(n)或反馈控制FlowableEmitter.request()
     * 2.当出现了事件发送和接收速度不匹配是的解决方案 即背压(Backpressure)缓存区(队列)存放策略大小是128，
     * 超出缓存区大小的时间进行丢弃、保留、报错的措施。
     * <p>
     * Flowable.create()第二个参数含义当缓存区大小存满，被观察者仍然继续发送下1个事件时，该如何处理的策略方式
     * BackpressureStrategy.ERROR 处理方式：直接抛出异常MissingBackpressureException
     * BackpressureStrategy.MISSING 处理方式：友好提示：缓存区满了
     * BackpressureStrategy.BUFFER 处理方式：将缓存区大小设置成无限大
     * BackpressureStrategy.DROP 处理方式：超过缓存区大小（128）的事件丢弃
     * BackpressureStrategy.LATEST 处理方式：只保存最新（最后）事件，超过缓存区大小（128）的事件丢弃
     * 即如果发送了150个事件，缓存区里会保存129个事件（第1-第128 + 第150事件）
     * <p>
     * 以下几种情况会抛出异常
     * 控制 观察者接收事件 的速度
     * 1.异步情况下，观察者不接收事件(不调用Subscription.request(n))，被观察者继续发送事件至超出
     * 缓存区大小（128）;
     * 2.同步情况下，被观察者发送事件数量 > 观察者接收事件数量。即观察者需要3个事件，被观察者却发送4个事件，当
     * 发送第4个事件时抛出异常；
     * 3.同步情况下，观察者不接收事件(不调用Subscription.request(n))，被观察者发送完第一个事件后就会抛出异常
     * 同步情况没有缓存区；
     * 控制 被观察者发送事件 的速度
     * 4.同步情况下，当FlowableEmitter.requested()减到0时，则代表观察者已经不可接收事件，此时被观察者若继续
     * 发送事件，则会抛出异常;
     */
    private void testBackpressure1() {
        // 1. 创建被观察者Flowable
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                // 一共发送4个事件
                Log.d(TAG, "发送事件 1");
                emitter.onNext(1);
                Log.d(TAG, "发送事件 2");
                emitter.onNext(2);
                Log.d(TAG, "发送事件 3");
                emitter.onNext(3);
                Log.d(TAG, "发送事件 4");
                emitter.onNext(4);
                Log.d(TAG, "发送完成");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        // 对比Observer传入的Disposable参数，Subscriber此处传入的参数 = Subscription
                        // 相同点：Subscription参数具备Disposable参数的作用，即Disposable.dispose()切断连接, 同样的调用Subscription.cancel()切断连接
                        // 不同点：Subscription增加了void request(long n)

                        s.request(3);
                        // 作用：决定观察者能够接收多少个事件
                        // 如设置了s.request(3)，这就说明观察者能够接收3个事件（多出的事件存放在缓存区）
                        // 官方默认推荐使用Long.MAX_VALUE，即s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });

    }

    /**
     * 观察者不接收事件的情况下，被观察者继续发送事件 & 存放到缓存区；再按需取出
     */
    private void testBackpressure2() {

        Button btn = findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSubscription.request(2);
            }

        });

        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "发送事件 1");
                emitter.onNext(1);
                Log.d(TAG, "发送事件 2");
                emitter.onNext(2);
                Log.d(TAG, "发送事件 3");
                emitter.onNext(3);
                Log.d(TAG, "发送事件 4");
                emitter.onNext(4);
                Log.d(TAG, "发送完成");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;
                        // 保存Subscription对象，等待点击按钮时（调用request(2)）观察者再接收事件
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });

    }

    /**
     * 观察者不接收事件的情况下，被观察者继续发送事件至超出缓存区大小（128）
     * 缓存区最大128
     */
    private void testBackpressure3() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                // 一共发送129个事件，即超出了缓存区的大小
                for (int i = 0; i < 129; i++) {
                    Log.d(TAG, "发送了事件" + i);
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        // 默认不设置可接收事件大小
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
    }

    /**
     * 在异步订阅关系中，反向控制的原理是：通过RxJava内部固定调用被观察者线程中的request(n)
     * 从而 反向控制被观察者的发送事件速度
     */
    private void testBackpressure4() {

        // 被观察者：一共需要发送500个事件，但真正开始发送事件的前提 = FlowableEmitter.requested()返回值 ≠ 0
        // 观察者：每次接收事件数量 = 48（点击按钮）
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {

                Log.d(TAG, "观察者可接收事件数量 = " + emitter.requested());
                boolean flag; //设置标记位控制

                // 被观察者一共需要发送500个事件
                for (int i = 0; i < 500; i++) {
                    flag = false;

                    // 若requested() == 0则不发送
                    while (emitter.requested() == 0) {
//                        Log.d(TAG, "emitter.requested() == 0");
                        if (!flag) {
                            Log.d(TAG, "不再发送");
                            flag = true;
                        }
                    }
                    // requested() ≠ 0 才发送
                    Log.d(TAG, "发送了事件" + i + "，观察者可接收事件数量 = " + emitter.requested());
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR).subscribeOn(Schedulers.io()) // 设置被观察者在io线程中进行
                .observeOn(AndroidSchedulers.mainThread()) // 设置观察者在主线程中进行
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;
                        // 初始状态 = 不接收事件；通过点击按钮接收事件
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "接收到了事件" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });

        // 点击按钮才会接收事件 = 48 / 次
        Button btn = findViewById(R.id.btn1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 点击按钮 则 接收48个事件
                mSubscription.request(48);
            }
        });
    }

    /**
     * flatMap 将被观察者发送的事件序列进行 拆分 & 单独转换，再合并成一个新的事件序列，最后再进行发送
     * <p>
     * 变换操作符
     * map()
     * flatMap()
     * concatMap()
     * buffer()
     */
    @SuppressLint("CheckResult")
    private void test9() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "生产事件");
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
            }
        }).flatMap(new Function<Integer, ObservableSource<String>>() {
            @Override
            public ObservableSource<String> apply(Integer integer) throws Exception {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 3; i++) {
                    list.add("事件" + integer + ",子事件" + i);
                }
                return Observable.fromIterable(list);
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                Log.d(TAG, "accept: " + s);
            }
        });
    }

    /**
     * do 在某个事件的生命周期中调用
     */
    private void testDo() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onError(new Throwable("发生错误了"));
            }
        })
                // 1. 当Observable每发送1次数据事件就会调用1次
                .doOnEach(new Consumer<Notification<Integer>>() {
                    @Override
                    public void accept(Notification<Integer> integerNotification) throws Exception {
                        Log.d(TAG, "doOnEach: " + integerNotification.getValue());
                    }
                })
                // 2. 执行Next事件前调用
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "doOnNext: " + integer);
                    }
                })
                // 3. 执行Next事件后调用
                .doAfterNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "doAfterNext: " + integer);
                    }
                })
                // 4. Observable正常发送事件完毕后调用
                .doOnComplete(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "doOnComplete: ");
                    }
                })
                // 5. Observable发送错误事件时调用
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d(TAG, "doOnError: " + throwable.getMessage());
                    }
                })
                // 6. 观察者订阅时调用
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Log.e(TAG, "doOnSubscribe: ");
                    }
                })
                // 7. Observable发送事件完毕后调用，无论正常发送完毕 / 异常终止
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "doAfterTerminate: ");
                    }
                })
                // 8. 最后执行
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        Log.e(TAG, "doFinally: ");
                    }
                })
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {
                        Log.d(TAG, "接收到了事件" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "对Error事件作出响应");
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "对Complete事件作出响应");
                    }
                });
    }

    /**
     * 条件/布尔操作符
     *
     * all() 判断发送的每项数据是否都满足 设置的函数条件 观察的是结果Boolean
     * contains()
     * isEmpty()
     * amb()
     * takeWhile() 获取满足条件的事件
     * takeUntil()
     * skipWhile() 跳过满足条件的事件
     * skipUntil()
     * defaultIfEmpty()
     * sequenceEqual()
     */
    @SuppressLint("CheckResult")
    private void test10() {

        Observable.just(1, 2, 3, 4, 5, 6)
                .all(new Predicate<Integer>() {
                    // 该函数用于判断Observable发送的10个数据是否都满足integer<=10
                    @Override
                    public boolean test(Integer integer) throws Exception {
//                        return (integer <= 3);// false
                        return (integer <= 10);// true
                    }
                }).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) throws Exception {
                Log.d(TAG, "result is " + aBoolean);
            }

        });
    }
}
