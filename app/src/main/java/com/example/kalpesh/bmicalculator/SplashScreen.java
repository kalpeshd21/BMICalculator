package com.example.kalpesh.bmicalculator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import static android.R.attr.animation;

public class SplashScreen extends AppCompatActivity {
    ImageView imgSplash;
    TextView tvTitle;
    Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imgSplash=(ImageView)findViewById(R.id.imgSplash);
        tvTitle=(TextView)findViewById(R.id.tvTitle);



        imgSplash = (ImageView)findViewById(R.id.imgSplash);
        anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.a1);
        imgSplash.startAnimation(anim);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();


            }
        }).start();

    }
}
