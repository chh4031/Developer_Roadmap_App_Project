package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class community extends AppCompatActivity{

    TextView this_userC;
    Button new_write;
    Button home;
    Button re;
    Button search;
    EditText search_ed;
    String thisId;

//    데이터를 보내는 부분
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
                System.out.println(jsget);
                JSONObject jsonObject = jsget.getJSONObject(0);
                Object a = onC(jsget);
            }catch(JSONException e){
                e.printStackTrace();
//                Log.v("Error", String.valueOf(e));
            }

        }
    }
// 게시글 목록을 띄워주는 부분
    public Object onC(JSONArray i) throws JSONException {
        System.out.println("Check 값 : " + i);
            JSONObject[] jsonObjectArray = new JSONObject[i.length()];
            LinearLayout li = findViewById((R.id.F_list));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, // width
                    LinearLayout.LayoutParams.WRAP_CONTENT);// height
            params.setMargins(0, 0, 0, 4);
            Button[] button = new Button[i.length()];
            for (int k = 0; k < i.length(); k++) {
                JSONObject j = i.getJSONObject(k);
                jsonObjectArray[k] = j;
                button[k] = new Button(this);
                button[k].setGravity(Gravity.START);
                button[k].setId(k);
                button[k].setText(j.getString("community_title").toString());
                button[k].setLayoutParams(params);// 버튼의 텍스트 설정
                button[k].setBackgroundDrawable(getResources().getDrawable(R.drawable.content_style));
                li.addView(button[k]);
                String jsonTitle = j.getString("community_title");
                String jsonContent = j.getString("community_content");
                String JsonName = j.getString("user_name");
                //            String JsonId = j.getString("user_user_id");

                button[k].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //                    Toast.makeText(getApplicationContext(), "버튼입력", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), content.class);
                        intent.putExtra("Title", jsonTitle);
                        intent.putExtra("Content", jsonContent);
                        intent.putExtra("Name", JsonName);
                        intent.putExtra("Id", thisId);
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
        home = (Button) findViewById(R.id.home);
        re = (Button) findViewById(R.id.re);
        search = (Button) findViewById(R.id.search);
        search_ed = (EditText) findViewById(R.id.search_edit);

        Intent putdata = getIntent();
        thisId = putdata.getStringExtra("thisId");
        this_userC.setText(thisId);
        new JSONTask().execute("http://chh.n-e.kr:7878/");

        if (thisId == null){
            new_write.setVisibility(View.GONE);
        }else{
            new_write.setVisibility(View.VISIBLE);
        }

        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent restart = new Intent(getApplicationContext(), community.class);
                restart.putExtra("thisId", thisId);
                startActivity(restart);
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
                goHome.putExtra("thisId", thisId);
                startActivity(goHome);
                finish();
            }
        });

        new_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goWrite = new Intent(getApplicationContext(), board_create.class);
                goWrite.putExtra("thisId", thisId);
                startActivity(goWrite);
                finish();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goSearch = new Intent(getApplicationContext(), search_content.class);
                goSearch.putExtra("thisId", thisId);
                goSearch.putExtra("searchEd", search_ed.getText().toString());
                startActivity(goSearch);
                finish();
            }
        });

    }

}
