package com.smilexie.weekview.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.smile.calendar.ui.AddDeleteActivity;
import com.smilexie.weekview.sample.systemcalendar.SyncSystemCalendarEvent;
import com.smilexie.weekview.sample.systemcalendar.SyncSystemEventActivity;


/**
 * 用于展示日程事件
 * Website: https://github.com/xiewenfeng
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.buttonBasic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BasicActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.buttonAsynchronous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AsynchronousActivity.class);
                startActivity(intent);
            }
        });

        //添加日程
        findViewById(R.id.buttonAddSchedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddDeleteActivity.class);
                startActivity(intent);
            }
        });

        //读取手机系统日程,向日程中添加事件
        findViewById(R.id.buttonSyncSystemSchedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SyncSystemCalendarEvent.class);
                startActivity(intent);
            }
        });

        //展示系统日历事件
        findViewById(R.id.buttonSyncSystemEvent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SyncSystemEventActivity.class);
                startActivity(intent);
            }
        });

    }

}
