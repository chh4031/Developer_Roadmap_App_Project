package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button main_btn1;
    Button main_btn2;
    Button main_btn3;
    Button main_btn4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main_btn1 = (Button) findViewById(R.id.main_btn1);
        main_btn2 = (Button) findViewById(R.id.main_btn2);
        main_btn3 = (Button) findViewById(R.id.main_btn3);
        main_btn4 = (Button) findViewById(R.id.main_btn4);

        main_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), community.class);
                startActivity(intent);
            }
        });

        main_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), roadmap.class);
                startActivity(intent);
            }
        });

        main_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), login.class);
                startActivity(intent);
            }
        });

        main_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), singup.class);
                startActivity(intent);
            }
        });


    }

}