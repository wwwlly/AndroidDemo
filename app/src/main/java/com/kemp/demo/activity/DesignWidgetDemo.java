package com.kemp.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.Toast;

import com.kemp.demo.R;

/**
 * Created by wangkp on 2018/1/31.
 */

public class DesignWidgetDemo extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_widget);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_vector_path);
        ab.setDisplayHomeAsUpEnabled(true);

        CalendarView calendarView = findViewById(R.id.calendar_view);
        boolean showWeekNumber = calendarView.getShowWeekNumber();
        Log.d(TAG,"showWeekNumber:" + showWeekNumber);
        int showWeekCount = calendarView.getShownWeekCount();
        Log.d(TAG,"showWeekCount:" + showWeekCount);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(this, "onOptionsItemSelected", Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
