package com.example.android_node_go;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.AsynchronousChannelGroup;

public class MainActivity extends AppCompatActivity {
    TextView jsonView;
    EditText editT1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonView = findViewById(R.id.jsonObj);
        editT1 = (EditText) findViewById(R.id.ed1);

        findViewById(R.id.btn_json).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new JSONTask().execute("http://192.168.0.192:7878/");
            }
        });

        findViewById(R.id.btn_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SendTask().execute("http://192.168.0.192:7878/");
            }
        });
    }

    public class JSONTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... urls) {

            HttpURLConnection con = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(urls[0]);

                con = (HttpURLConnection)url.openConnection();
                con.connect();

                con.setRequestMethod("GET");

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
            String[] nodeid = s1.split(" ");
            jsonView.setText("id : " + nodeid[0] + "\n" + "pw : " + nodeid[1]);

        }
    }
    public class SendTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... urls){

            HttpURLConnection con = null;
            BufferedReader writer = null;
            try {

                URL url = new URL(urls[0]);
                con = (HttpURLConnection)url.openConnection();
//                con.connect();

                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Cache-Control", "no-cache");
                con.setRequestProperty("Content-Type", "application/json");

                String userText = editT1.getText().toString();
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("text", userText);

                System.out.println(userText);
// 왜 쓰는지 알 수 없느 부분
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(jsonParam.toString());
                wr.flush();
                wr.close();

                writer = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();


                while ((inputLine = writer.readLine()) != null){
                    response.append(inputLine);
                }

                writer.close();
                System.out.println(response.toString());
// 여기까지
                return jsonParam.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch(Exception e){
                e.printStackTrace();
            }finally{
                con.disconnect();
            }
            return null;
        }

    protected void onPostExecute(String result){
        if(result != null){
            System.out.println("전송완료 전송된 값 : " + result);
        }else {
            System.out.println("전송실패");
        }
        }
    }
}