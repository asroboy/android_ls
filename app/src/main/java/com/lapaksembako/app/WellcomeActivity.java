package com.lapaksembako.app;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lapaksembako.app.adapter.SectionsPagerAdapter;
import com.lapaksembako.app.helper.Common;
import com.lapaksembako.app.helper.Preferences;

import java.util.Timer;
import java.util.TimerTask;

public class WellcomeActivity extends AppCompatActivity implements View.OnClickListener {

    Timer swipeTimer = new Timer();
    ViewPager viewPager;
    int NUM_PAGES = 4;
    int currentPage = 0;
    boolean touched = false;
    Handler handler = new Handler();
    Runnable update;
    Button buttonSkip, buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcome);

        buttonSkip = findViewById(R.id.buttonSkip);
        buttonNext = findViewById(R.id.buttonNext);

        viewPager = findViewById(R.id.view_pager);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);

        final TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        startPagerAutoSwipe();

        buttonSkip.setOnClickListener(this);
        buttonNext.setOnClickListener(this);
    }


    private void jumpToMainPage(){
        flagIsWellcome();
        startActivity(new Intent(WellcomeActivity.this, TermOfUseActivity.class));
        swipeTimer.cancel();
        swipeTimer.purge();
        finish();
    }

    private void startPagerAutoSwipe() {
        update = new Runnable() {
            public void run() {
                if (!touched) {
                    if (currentPage == NUM_PAGES) {
//                        currentPage = 0;
                        jumpToMainPage();
                    }
                    if (currentPage < NUM_PAGES)
                        viewPager.setCurrentItem(currentPage++, true);
                }
            }
        };
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 10, 3000);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.buttonSkip){
            jumpToMainPage();
        }
        if(id == R.id.buttonNext){
            viewPager.setCurrentItem(currentPage++, true);
        }
    }

    private void flagIsWellcome(){
        Preferences preferences = new Preferences();
        preferences.init(getApplicationContext());
        preferences.saveBoolean(Common.IS_WELLCOME, false);
    }
}
