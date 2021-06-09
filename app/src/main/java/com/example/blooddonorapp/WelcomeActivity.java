package com.example.blooddonorapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                    finish();
                }else{
                    startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                    finish();
                }

                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        }, 1500);
    }
}
