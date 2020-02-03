package com.saiban.bahawalpurblooddonors;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        animation();
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3500);
                    startActivity(new Intent(getApplicationContext(), loginorsignup.class));
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }

    void animation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.a);
        animation.reset();
        ImageView Bl = (ImageView) findViewById(R.id.BLOOD);
        Bl.clearAnimation();
        Bl.startAnimation(animation);


    }


}
