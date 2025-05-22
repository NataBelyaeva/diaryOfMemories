package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Str3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_str3);
    }


    public void toStr4(View v){
        Intent intent = new Intent(this, Str4.class);
        startActivity(intent);
    }
}