package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class roadmap extends AppCompatActivity{

    Button android_btn;
    TextView this_user;
    String thisId;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.roadmap);

        android_btn = (Button) findViewById(R.id.androidButton);
        this_user = (TextView) findViewById(R.id.this_user);

        Intent get = getIntent();
        thisId = get.getStringExtra("thisId");
        this_user.setText(thisId);

        android_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent android_go = new Intent(getApplicationContext(), android_roadmap.class);
                android_go.putExtra("thisId", thisId);
                startActivity(android_go);
                finish();

            }
        });
    }
}
