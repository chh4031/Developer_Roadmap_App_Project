package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class board_create extends AppCompatActivity {

    TextView this_userB;
    EditText title, content;
    Button result_click;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_create);

        this_userB = (TextView) findViewById(R.id.this_userB);
        title = (EditText) findViewById(R.id.title_ed);
        result_click = (Button) findViewById(R.id.result_button);

        content = (EditText) findViewById(R.id.content_ed);

        Intent putdata = getIntent();
        String thisId = putdata.getStringExtra("thisId");
        this_userB.setText(thisId);

        result_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendTask().execute("http://chh.n-e.kr:7878/", thisId);
            }
        });
    }

    public class SendTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection con = null;
            BufferedReader writer = null;
            try {
                URL url = new URL(urls[0] + "create");
                con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Cache-Control", "no-cache");
                con.setRequestProperty("Content-Type", "application/json");

                String Edtitle = title.getText().toString();
                String Edcontent = content.getText().toString();
                String Eduserid = urls[1];
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("Edtitle", Edtitle);
                jsonParam.put("Edcontent", Edcontent);
                jsonParam.put("Eduserid", urls[1]);

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
            Intent move = new Intent(getApplicationContext(), community.class);
            move.putExtra("thisId", result);
//            System.out.println(result);
            startActivity(move);
            finish();

        }
    }
}