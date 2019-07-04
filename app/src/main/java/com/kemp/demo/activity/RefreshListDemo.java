package com.kemp.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kemp.demo.R;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

public class RefreshListDemo extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private AppBarLayout appBar;
    private TwinklingRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_list);

        appBar = findViewById(R.id.app_bar);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        RecyclerView rv = findViewById(R.id.recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View item = LayoutInflater.from(RefreshListDemo.this)
                        .inflate(R.layout.item_refresh_list, parent, false);
                return new Holder(item);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                Holder h = (Holder) holder;
                h.textView.setText("item " + position);
            }

            @Override
            public int getItemCount() {
                return 100;
            }

            class Holder extends RecyclerView.ViewHolder {
                TextView textView;

                public Holder(View itemView) {
                    super(itemView);
                    textView = itemView.findViewById(R.id.text);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        appBar.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        appBar.removeOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        swipeRefreshLayout.setEnabled(verticalOffset == 0);
    }
}
