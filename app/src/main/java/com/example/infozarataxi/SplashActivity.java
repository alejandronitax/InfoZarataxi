package com.example.infozarataxi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private final int DurationSplash = 1500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.imageView4);
        TextView textoSplash = findViewById(R.id.textView8);

        Animation leftImage, downText;

        leftImage = AnimationUtils.loadAnimation(this, R.anim.lefttraslate);
        downText = AnimationUtils.loadAnimation(this, R.anim.downtraslate);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar.isShowing()) {
            actionBar.hide();
        }
        else {
            actionBar.show();
        }

        imageView.setAnimation(leftImage);
        textoSplash.setAnimation(downText);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefe = getSharedPreferences("preferences", Context.MODE_PRIVATE);
                Intent intent;

                if (!prefe.getString("licencia","").isEmpty()){
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    intent = new Intent(SplashActivity.this, InsertCabbie.class);
                }

                startActivity(intent);
                finish();

            }
        },DurationSplash);
    }
}
