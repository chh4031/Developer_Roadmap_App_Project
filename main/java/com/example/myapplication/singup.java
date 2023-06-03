package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class singup extends AppCompatActivity{

    EditText edname;
    EditText edid;
    EditText edpw;
    Button Register;
    RadioButton agree;
    RadioButton disagree;
    RadioGroup select;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singup);

        //위제 아이디 가져오기
        edname = (EditText) findViewById(R.id.singup_name);
        edid = (EditText) findViewById(R.id.singup_id);
        edpw = (EditText) findViewById(R.id.singup_pw);
        Register = (Button) findViewById(R.id.singup_Register);
        agree = (RadioButton) findViewById(R.id.singup_agree);
        disagree = (RadioButton) findViewById(R.id.singup_disagree);
        select = (RadioGroup) findViewById(R.id.singup_select);

        disagree.setChecked(true);

        select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId){
                    case R.id.singup_agree:
                        Register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(edname.length() == 0 || edid.length() == 0 || edpw.length() == 0){
                                    Toast.makeText(getApplicationContext(), "응 공백 안되", Toast.LENGTH_SHORT).show();
                                }else {
                                    new SendTask().execute("http://192.168.0.29:7878/");
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                        break;
                    case R.id.singup_disagree:
                        Register.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(), "응 공백 안되", Toast.LENGTH_SHORT).show();
                            }});
                        break;
                }
            }
        });
    }

        //회원가입에서 데이터를 보내는 코드
        public class SendTask extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... urls){

                HttpURLConnection con = null;
                BufferedReader writer = null;
                try {

                    URL url = new URL(urls[0]);
                    con = (HttpURLConnection)url.openConnection();
//                    con.connect();

                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("Cache-Control", "no-cache");
                    con.setRequestProperty("Content-Type", "application/json");

                    String singup_name = edname.getText().toString();
                    String singup_id = edid.getText().toString();
                    String singup_pw = edpw.getText().toString();
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("singup_name", singup_name);
                    jsonParam.put("singup_id", singup_id);
                    jsonParam.put("singup_pw", singup_pw);

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
    