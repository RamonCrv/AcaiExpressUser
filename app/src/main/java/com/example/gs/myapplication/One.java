package com.example.gs.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class One extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_one);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Intent i = new Intent(One.this,Login.class);
                //startActivity(i);
                finish();
            }
        }, 1500);
    }
}
