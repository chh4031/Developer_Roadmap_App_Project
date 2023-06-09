package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class android_roadmap extends AppCompatActivity {

    Button java_btn;
    Button kotlin_btn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.androidroadmap);

        java_btn = (Button) findViewById(R.id.JAVA_btn);
        kotlin_btn = (Button) findViewById(R.id.KOTLIN_btn);

        java_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goJAVA = new Intent(getApplicationContext(), search_content.class);
                goJAVA.putExtra("searchEd", "java");
                startActivity(goJAVA);
                finish();
            }
        });

        kotlin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goKOTLIN = new Intent(getApplicationContext(), search_content.class);
                goKOTLIN.putExtra("searchEd", "kotlin");
                startActivity(goKOTLIN);
                finish();
            }
        });
    }
}
