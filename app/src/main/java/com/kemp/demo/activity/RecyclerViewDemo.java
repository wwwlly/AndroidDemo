package com.kemp.demo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.ajguan.library.EasyRefreshLayout;
import com.ajguan.library.LoadModel;
import com.kemp.demo.R;
import com.kemp.demo.adapter.MyRecyclerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by wangkp on 2018/1/2.
 */

public class RecyclerViewDemo extends AppCompatActivity implements EasyRefreshLayout.EasyEvent, MyRecyclerAdapter.OnItemLongClickListener{

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

//        LinearLayoutManager lm = new LinearLayoutManager(this);
//        lm.setOrientation(LinearLayoutManager.VERTICAL);
//        recyclerView.setLayoutManager(lm);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter.setOnItemLongClickListener(this);
        recyclerView.setAdapter(adapter);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        easyRefreshLayout.addEasyEvent(this);
//        easyRefreshLayout.setLoadMoreModel(LoadModel.NONE);
    }

    private void initDatas() {
        for (int i = 0; i < 15; i++) {
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

    private ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            final int swipeFlags = 0;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            //得到当拖拽的viewHolder的Position
            int fromPosition = viewHolder.getAdapterPosition();
            //拿到当前拖拽到的item的viewHolder
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(datas, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(datas, i, i - 1);
                }
            }
            adapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }
    });

    @Override
    public void onItemLongClick(RecyclerView.ViewHolder vh) {
        //判断被拖拽的是否是前两个，如果不是则执行拖拽
        if (vh.getLayoutPosition() != 0 && vh.getLayoutPosition() != 1) {
            itemTouchHelper.startDrag(vh);
        }
    }
}
