package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.diary.adapter.MainAdapter;
import com.example.diary.db.MyDbManager;

public class SavesNote extends AppCompatActivity {
    private MyDbManager myDbManager;
    private EditText edTitle, edText;
    private RecyclerView rcView;
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saves_note);
        init();
    }

    private void init(){
        myDbManager = new MyDbManager(this);
        edTitle = findViewById(R.id.edTitile);
        edText = findViewById(R.id.edText);
        rcView = findViewById(R.id.rcView);
        mainAdapter = new MainAdapter(this);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        rcView.setAdapter(mainAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
        mainAdapter.updateAdapter(myDbManager.getFromDb());
    }

    public void onClickSave(View view){
        mainAdapter.updateAdapter(myDbManager.getFromDb());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDbManager.closeDb();
    }

    public void toStr4(View v){
        Intent intent = new Intent(this, Str4.class);
        startActivity(intent);
    }
}