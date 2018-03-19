package com.kemp.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.kemp.demo.dragger2.DaggerA;
import com.kemp.demo.dragger2.DaggerB;

import javax.inject.Inject;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

/**
 * 参考：
 * <p>
 * https://github.com/googlesamples/android-architecture.git
 * <p>
 * https://www.jianshu.com/p/39d1df6c877d
 * Created by wangkp on 2018/2/24.
 */

public class Dagger2Demo extends AppCompatActivity {

    public static final String TAG = Dagger2Demo.class.getSimpleName();

    @Inject
    DaggerA daggerA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerDagger2Demo_MyComponent.builder().myModule(new MyModule()).build().inject(this);
        Log.d(TAG, daggerA == null ? "daggerA is null" : "daggerA is not null");
    }

    @Module
    public class MyModule {

        @Provides
        DaggerB providerDaggerB() {
            return new DaggerB();
        }
    }

    @Component(modules = MyModule.class)
    public interface MyComponent {
        void inject(Dagger2Demo activity);
    }
}
