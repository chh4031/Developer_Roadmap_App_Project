package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button main_btn1;
    Button main_btn2;
    Button main_btn3;
    Button main_btn4;
    Button main_btn5;
    TextView this_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);

        main_btn1 = (Button) findViewById(R.id.main_btn1);
        main_btn2 = (Button) findViewById(R.id.main_btn2);
        main_btn3 = (Button) findViewById(R.id.main_btn3);
        main_btn4 = (Button) findViewById(R.id.main_btn4);
        main_btn5 = (Button) findViewById(R.id.main_btn5);
        this_user = (TextView) findViewById(R.id.this_user);

        super.onCreate(savedInstanceState);

        Intent putdata = getIntent();
        String thisId = putdata.getStringExtra("thisId");
        this_user.setText(thisId);
//        System.out.println(key);
        if (thisId != null){
            main_btn3.setVisibility(View.GONE);
            main_btn4.setVisibility(View.GONE);
            main_btn5.setVisibility(View.VISIBLE);
        }else{
            main_btn3.setVisibility(View.VISIBLE);
            main_btn4.setVisibility(View.VISIBLE);
        }


        main_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), community.class);
                intent.putExtra("thisId", thisId);
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

        main_btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                main_btn3.setVisibility(View.VISIBLE);
                main_btn4.setVisibility(View.VISIBLE);
                main_btn5.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}