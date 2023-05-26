package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class community extends AppCompatActivity{

    TextView this_userC;
    Button new_write;

    public class JSONTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... urls) {

            HttpURLConnection con = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urls[0] + "com");

                con = (HttpURLConnection)url.openConnection();
                con.connect();

//                con.setRequestMethod("GET");

                InputStream stream = con.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();

                String line = "";

                while ((line = reader.readLine()) != null) buffer.append(line);

                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                con.disconnect();
                try {
                    if(reader != null)
                        reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s1) {

            super.onPostExecute(s1);
            try{
                JSONArray jsget = new JSONArray(s1);
                JSONObject jsonObject = jsget.getJSONObject(0);
                Object a = onC(jsget);
            }catch(JSONException e){
                e.printStackTrace();
//                Log.v("Error", String.valueOf(e));
            }

        }
    }

    public Object onC(JSONArray i) throws JSONException {
        System.out.println(i);
        JSONObject[] jsonObjectArray = new JSONObject[i.length()];
        LinearLayout li = findViewById((R.id.F_list));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, // width
                LinearLayout.LayoutParams.WRAP_CONTENT);// height
        Button[] button = new Button[i.length()];
        for (int k = 0; k < i.length(); k++) {
            JSONObject j = i.getJSONObject(k);
            jsonObjectArray[k] = j;
            button[k] = new Button(this);
            button[k].setGravity(Gravity.START);
            button[k].setId(k);
            button[k].setText(j.getString("community_title").toString());
            button[k].setLayoutParams(params);// 버튼의 텍스트 설정
            li.addView(button[k]);
            String jsonTitle = j.getString("community_title");
            String jsonContent = j.getString("community_content");
            String JsonName = j.getString("user_name");
            String JsonId = j.getString("user_user_id");
            button[k].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(getApplicationContext(), "버튼입력", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), content.class);
                    intent.putExtra("Title", jsonTitle);
                    intent.putExtra("Content", jsonContent);
                    intent.putExtra("Name", JsonName);
                    intent.putExtra("Id", JsonId);
                    startActivity(intent);
                }
            });
        }

        System.out.println(jsonObjectArray[0].toString());

        return li;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        this_userC = (TextView) findViewById(R.id.this_userC);
        new_write = (Button) findViewById(R.id.write);

        Intent putdata = getIntent();
        String thisId = putdata.getStringExtra("thisId");
        this_userC.setText(thisId);
        new JSONTask().execute("http://192.168.0.29:7878/");

        if (thisId == null){
            new_write.setVisibility(View.GONE);
        }else{
            new_write.setVisibility(View.VISIBLE);
        }

        new_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goWrite = new Intent(getApplicationContext(), board_create.class);
                goWrite.putExtra("thisId", thisId);
                startActivity(goWrite);
            }
        });

    }

}
