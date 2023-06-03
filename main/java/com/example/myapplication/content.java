package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class content extends AppCompatActivity {
    TextView Tv_Title;
    TextView Tv_Name;
    TextView Tv_Content;
    TextView Tv_id;
    Button del;
    Button up;
    LinearLayout la;
    String content_number;

    public class SendTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection con = null;
            BufferedReader writer = null;
            try {
                URL url = new URL(urls[0] + "check");
                con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Cache-Control", "no-cache");
                con.setRequestProperty("Content-Type", "application/json");

                String check_id = Tv_id.getText().toString();
                String check_title = Tv_Title.getText().toString();
                String check_content = Tv_Content.getText().toString();
                String check_name = Tv_Name.getText().toString();
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("check_id", check_id);
                jsonParam.put("check_title", check_title);
                jsonParam.put("check_content", check_content);
                jsonParam.put("check_name", check_name);

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(jsonParam.toString());
                wr.flush();
                wr.close();

                writer = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = writer.readLine()) != null) {
                    response.append(inputLine);
                }

                writer.close();

                return response.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            System.out.println(result);
            try{
                JSONObject jsset = new JSONObject(result);
                String this_check = jsset.getString("check");
                content_number = jsset.getString("number").toString();
                if ("S".equals(this_check)){
                    la.setVisibility(View.VISIBLE);
                } else if ("D".equals(this_check)) {
                    la.setVisibility(View.GONE);
            }
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    public class DelTesk extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection con = null;
            BufferedReader writer = null;
            try {
                URL url = new URL(urls[0] + "del");
                con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Cache-Control", "no-cache");
                con.setRequestProperty("Content-Type", "application/json");

                String check_id = Tv_id.getText().toString();
                String check_title = Tv_Title.getText().toString();
                String check_content = Tv_Content.getText().toString();
                String check_name = Tv_Name.getText().toString();
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("check_id", check_id);
                jsonParam.put("check_title", check_title);
                jsonParam.put("check_content", check_content);
                jsonParam.put("check_name", check_name);

                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(jsonParam.toString());
                wr.flush();
                wr.close();

                writer = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = writer.readLine()) != null) {
                    response.append(inputLine);
                }

                writer.close();

                return response.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return null;
        }

        protected void onPostExecute(String result) {
            System.out.println(result);
            if ("del_ok".equals(result)) {
                System.out.println("안드로이드 : 삭제완료");
                Intent back = new Intent(getApplicationContext(), community.class);
                back.putExtra("thisId", Tv_id.getText().toString());
                startActivity(back);
            } else if ("del_fail".equals(result)) {
                System.out.println("안드로이드 : 삭제실패");
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.content);
        super.onCreate(savedInstanceState);

        Tv_Title = (TextView) findViewById(R.id.title_vi);
        Tv_Name = (TextView) findViewById(R.id.name_vi);
        Tv_Content = (TextView) findViewById(R.id.content_vi);
        Tv_id = (TextView) findViewById(R.id.this_user);
        del = (Button) findViewById(R.id.delete);
        up = (Button) findViewById(R.id.update);
        la = (LinearLayout) findViewById(R.id.del_up);

        Intent intent = getIntent();
        String Title = intent.getStringExtra("Title");
        String Content = intent.getStringExtra("Content");
        String Name = intent.getStringExtra("Name");
        String Id = intent.getStringExtra("Id");

//        System.out.println(Title + " " + Content + " " + Name + " " + Id);

        Tv_Title.setText(Title);
        Tv_Name.setText(Name);
        Tv_Content.setText(Content);
        Tv_id.setText(Id);

        new SendTask().execute("http://192.168.0.29:7878/");

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DelTesk().execute("http://192.168.0.29:7878/");
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goup = new Intent(getApplicationContext(), update.class);
                goup.putExtra("thisId", Tv_id.getText().toString());
                goup.putExtra("thisTitle", Tv_Title.getText().toString());
                goup.putExtra("thisContent", Tv_Content.getText().toString());
                goup.putExtra("thisName", Tv_Name.getText().toString());
                goup.putExtra("thisNumber", content_number);
                startActivity(goup);
                finish();
            }
        });
    }
}