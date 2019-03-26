package com.kemp.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class RxDemo extends AppCompatActivity {

    private static final String TAG = "RxDemo";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        testRx1();
//        testRx1();
//        testObservable(createObservable());
//        testRx2();

    }

    private void testRx1() {
        Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long aLong) {
                        return 10 - aLong.intValue();
                    }
                })
                .takeUntil(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer <= 0;
                    }
                }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext");
            }
        });
    }

    private Observable<Integer> createObservable() {
        return Observable
                .interval(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long aLong) {
                        return 10 - aLong.intValue();
                    }
                })
                .takeUntil(new Func1<Integer, Boolean>() {
                    @Override
                    public Boolean call(Integer integer) {
                        return integer <= 0;
                    }
                });
    }

    private void testObservable(Observable<Integer> observable) {
        observable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext");
            }
        });
    }

    private void testObservable2(Observable<Integer> observable, final Observable<Integer> target) {
        observable.subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError");
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext");
            }
        });
    }

    private void testRx2() {
        Observable<Integer> observable = createObservable();
        Log.d(TAG, "Observable created");
        try {
            Thread.sleep(10 * 1000);
            testObservable(observable);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
