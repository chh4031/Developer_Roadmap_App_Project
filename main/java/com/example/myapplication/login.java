package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class login extends AppCompatActivity {
    EditText edid;
    EditText edpw;

    Button loginbtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        edid = (EditText) findViewById(R.id.etUsername);
        edpw = (EditText) findViewById(R.id.etPassword);
        loginbtn = (Button) findViewById(R.id.btnLogin);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendTask().execute("http://192.168.0.17:7878/");
            }
        });
    }

    public class SendTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection con = null;
            BufferedReader writer = null;
            try {
                URL url = new URL(urls[0] + "sign");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Cache-Control", "no-cache");
                con.setRequestProperty("Content-Type", "application/json");

                String login_id = edid.getText().toString();
                String login_pw = edpw.getText().toString();
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("login_id", login_id);
                jsonParam.put("login_pw", login_pw);

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
            if (result != null) {
                Toast.makeText(login.this, result, Toast.LENGTH_SHORT).show();
                if(result.equals("로그인 성공")) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Intent godata = new Intent(login.this, MainActivity.class);
                    godata.putExtra("key", "1");
                    startActivity(intent);
                    startActivity(godata);

                }
            } else {
                Toast.makeText(login.this, "전송실패", Toast.LENGTH_SHORT).show();
            }
        }
    }
}