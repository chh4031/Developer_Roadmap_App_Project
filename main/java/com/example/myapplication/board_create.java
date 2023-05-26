package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class board_create extends AppCompatActivity{

    TextView this_userB;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_create);

        this_userB = (TextView) findViewById(R.id.this_userB);

        Intent putdata = getIntent();
        String thisId = putdata.getStringExtra("thisId");
        this_userB.setText(thisId);
    }

}