package com.example.mywallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    Animation topAnim,midAnim,botAnim;
    View firstLine,secondLine,thirdLine,fourLine,fiveLine,sixLne,sevenLine;
    TextView tvTitle,tvTagLine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        init();
        topAnim.setDuration(1500);
        midAnim.setDuration(1500);
        botAnim.setDuration(1500);
        firstLine.setAnimation(topAnim);
        secondLine.setAnimation(topAnim);
        thirdLine.setAnimation(topAnim);
        fourLine.setAnimation(topAnim);
        fiveLine.setAnimation(topAnim);
        sixLne.setAnimation(topAnim);
        sevenLine.setAnimation(topAnim);

        tvTitle.setAnimation(midAnim);

        tvTagLine.setAnimation(botAnim);

        new Handler() {
        }.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }
        },2200);
    }

    private void init() {
        topAnim= AnimationUtils.loadAnimation(this,R.anim.top_anim);
        midAnim= AnimationUtils.loadAnimation(this,R.anim.mid_anim);
        botAnim= AnimationUtils.loadAnimation(this,R.anim.bot_anim);

        firstLine=findViewById(R.id.firstLine);
        secondLine=findViewById(R.id.secondLine);
        thirdLine=findViewById(R.id.thirdLine);
        fourLine=findViewById(R.id.fourLine);
        fiveLine=findViewById(R.id.fiveLine);
        sixLne=findViewById(R.id.sixLine);
        sevenLine=findViewById(R.id.sevenLine);

        tvTitle=findViewById(R.id.tvTitle);
        tvTagLine=findViewById(R.id.tvTagLine);
    }

}