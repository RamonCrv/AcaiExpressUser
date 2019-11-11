package com.example.gs.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class Carregando extends AppCompatActivity {
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.color2));
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_carregando);
        caregando();
        startt();
        progressBar.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
                //  Intent i = new Intent(Um.this,MainActivity.class);
                //  startActivity(i);
                finish();
            }
        },2000);
    }

    private void caregando() {
        AlertDialog.Builder megaBox = new AlertDialog.Builder(this);
        megaBox.setTitle("Entrando");
        megaBox.setMessage("Caregando...");

    }
    private void startt(){
        this.progressBar = findViewById(R.id.progressBar);
    }
}

