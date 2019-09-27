package com.example.gs.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.gs.myapplication.R;

public class lista extends AppCompatActivity {
    private String nome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
    }
    
    public  void setNome(String nome1){
        nome = this.nome;
    }
}
