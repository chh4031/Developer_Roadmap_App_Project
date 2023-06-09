package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class update extends AppCompatActivity {

    TextView this_user;
    EditText up_title;
    EditText up_content;
    Button up_button;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update);

        this_user = (TextView) findViewById(R.id.this_userD);
        up_title = (EditText) findViewById(R.id.title_up);
        up_content = (EditText) findViewById(R.id.content_up);
        up_button = (Button) findViewById(R.id.update_button);

        Intent intent = getIntent();
        String user = intent.getStringExtra("thisId").toString();
        String title = intent.getStringExtra("thisTitle").toString();
        String content = intent.getStringExtra("thisContent").toString();
        String number = intent.getStringExtra("thisNumber").toString();

        this_user.setText(user);
        up_title.setText(title);
        up_content.setText(content);

        up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new update.UpTask().execute("http://chh.n-e.kr:7878/", this_user.getText().toString(), number);
            }
        });

    }

    public class UpTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection con = null;
            BufferedReader writer = null;
            try {
                URL url = new URL(urls[0] + "up");
                con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Cache-Control", "no-cache");
                con.setRequestProperty("Content-Type", "application/json");

                String Uptitle = up_title.getText().toString();
                String Upcontent = up_content.getText().toString();
                String Upuserid = urls[1];
                int Upnumber = Integer.parseInt(urls[2]);
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("Uptitle", Uptitle);
                jsonParam.put("Upcontent", Upcontent);
                jsonParam.put("Upuserid", Upuserid);
                jsonParam.put("Upnumber", Upnumber);

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
            Intent gocom = new Intent(getApplicationContext(), community.class);
            gocom.putExtra("thisId", result);
//            System.out.println(result);
            startActivity(gocom);
            finish();

        }
    }

}
