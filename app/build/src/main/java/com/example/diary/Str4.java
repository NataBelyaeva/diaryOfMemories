package com.example.diary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.diary.adapter.MainAdapter;
import com.example.diary.db.MyDbManager;

public class Str4 extends AppCompatActivity {
    private MyDbManager myDbManager;
    private EditText edTitle, edText;
    private RecyclerView rcView;
    private MainAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_str4);
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
        getItemTouchHelper().attachToRecyclerView(rcView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
        mainAdapter.updateAdapter(myDbManager.getFromDb());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDbManager.closeDb();
    }

    public void toProfil(View v){
        Intent intent = new Intent(this, profil.class);
        startActivity(intent);
    }

    public void toNewNote(View v){
        Intent intent = new Intent(this, Note.class);
        startActivity(intent);
    }


    private ItemTouchHelper getItemTouchHelper(){
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                mainAdapter.removeItem(viewHolder.getAdapterPosition(), myDbManager);
            }
        });
    }

    public void toMap(View v){
        Intent intent = new Intent(this, Map.class);
        startActivity(intent);
    }

}