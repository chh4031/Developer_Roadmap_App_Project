package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class content extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.content);

        TextView Tv_Title;
        TextView Tv_Name;
        TextView Tv_Content;

        Tv_Title = (TextView) findViewById(R.id.title_vi);
        Tv_Name = (TextView) findViewById(R.id.name_vi);
        Tv_Content = (TextView) findViewById(R.id.content_vi);

        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String Title = intent.getStringExtra("Title");
        String Content = intent.getStringExtra("Content");
        String Name = intent.getStringExtra("Name");
        String Id = intent.getStringExtra("Id");

        System.out.println(Title + " " + Content + " " + Name + " " + Id);

        Tv_Title.setText(Title);
        Tv_Name.setText(Name);
        Tv_Content.setText(Content);
    }
}
