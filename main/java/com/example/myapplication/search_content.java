package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class search_content extends AppCompatActivity {

    String thisId;
    String s_title;
    TextView user_id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_content);

        Intent putdate = getIntent();
        thisId = putdate.getStringExtra("thisId");
        s_title = putdate.getStringExtra("searchEd");
        user_id = (TextView) findViewById(R.id.this_userF);
        user_id.setText(thisId);

        new SearchTesk().execute("http://chh.n-e.kr:7878/", thisId);
    }

    public class SearchTesk extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection con = null;
            BufferedReader writer = null;
            try {
                URL url = new URL(urls[0] + "search");
                con = (HttpURLConnection) url.openConnection();
//                con.setRequestMethod("POST");
                con.setDoOutput(true);
                con.setDoInput(true);
                con.setRequestProperty("Cache-Control", "no-cache");
                con.setRequestProperty("Content-Type", "application/json");

                String searchTitle = s_title;
                String Eduserid = urls[1];
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("search_title", searchTitle);
                jsonParam.put("Eduserid", Eduserid);

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
            try{
                JSONArray searchget = new JSONArray(result);
                System.out.println(searchget);
                JSONObject jsonObject = searchget.getJSONObject(0);
                Object a = onCC(searchget);
            }catch(JSONException e){
                e.printStackTrace();
//                Log.v("Error", String.valueOf(e));
            }
        }
    }

    public Object onCC(JSONArray i) throws JSONException {
        System.out.println("Check 값 : " + i);
        JSONObject[] jsonObjectArray = new JSONObject[i.length()];
        LinearLayout li = findViewById((R.id.FF_list));
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

}
