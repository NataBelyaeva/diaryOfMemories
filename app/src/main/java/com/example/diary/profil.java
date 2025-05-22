package com.example.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class profil extends AppCompatActivity {

    Dialog dialog, dialog2;
    Button buttonAbolition, buttonSave, buttonCancel, buttonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


        dialog = new Dialog(profil.this);
        dialog.setContentView(R.layout.fragment_name);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.fragment));
        dialog.setCancelable(false);

        buttonAbolition = dialog.findViewById(R.id.button8);
        buttonSave = dialog.findViewById(R.id.button10);

        buttonAbolition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        LinearLayout myLayout = findViewById(R.id.linearLayout2);
        myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открываем диалог
                dialog.show();
            }
        });

        dialog2 = new Dialog(profil.this);
        dialog2.setContentView(R.layout.fragment_exit);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog2.getWindow().setBackgroundDrawable(getDrawable(R.drawable.fragment));
        dialog2.setCancelable(false);

        buttonCancel = dialog2.findViewById(R.id.buttonCancel);
        buttonExit = dialog2.findViewById(R.id.buttonEXIT);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });

        Button myLayout2 = findViewById(R.id.button7);
        myLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открываем диалог
                dialog2.show();
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                v.getContext().startActivity(intent);
            }
        });


    }
    public void toStr4(View v){
        Intent intent = new Intent(this, Str4.class);
        startActivity(intent);
    }

    public void toMainActivity(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}