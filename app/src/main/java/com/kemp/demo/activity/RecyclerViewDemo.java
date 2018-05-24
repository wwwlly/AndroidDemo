package com.kemp.demo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.kemp.demo.R;
import com.kemp.demo.adapter.MyRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangkp on 2018/1/2.
 */

public class RecyclerViewDemo extends AppCompatActivity implements EasyRefreshLayout.EasyEvent{

    private EasyRefreshLayout easyRefreshLayout;
    private RecyclerView recyclerView;

    private MyRecyclerAdapter<String> adapter;
    private List<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyler);

        easyRefreshLayout = findViewById(R.id.easy_refresh_view);
        recyclerView = findViewById(R.id.recycler_view);
        initDatas();
        adapter = new MyRecyclerAdapter<>(this, datas);

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        recyclerView.setAdapter(adapter);
        easyRefreshLayout.addEasyEvent(this);
//        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
    }

    private void initDatas() {
        for (int i = 0; i < 5; i++) {
            datas.add(String.valueOf(i));
        }
    }

    @Override
    public void onLoadMore() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                easyRefreshLayout.loadMoreComplete();
            }
        },2000);
    }

    @Override
    public void onRefreshing() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                easyRefreshLayout.refreshComplete();
            }
        },2000);
    }
}
