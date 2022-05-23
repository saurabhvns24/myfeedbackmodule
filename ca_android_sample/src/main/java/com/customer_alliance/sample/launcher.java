package com.customer_alliance.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import android.widget.Button;

import com.customer_alliance.sdk.startFeedBack.StartFeedBack;

public class launcher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        Button launch = findViewById(R.id.launch_btn);
        launch.setOnClickListener(view -> {
            new StartFeedBack(launcher.this,"akjnajjbap6rfjzy6kyvuutn6l2lbsc7dqw4ivama1vvxirq").showFeedback();
        });
    }
}


