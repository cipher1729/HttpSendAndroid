package com.testconnectionwithserver;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {
    String url="http://10.170.196.118:3000/addPath";
    float[] sampleValues ={-90.03f, 20.33f};
    InputStream is;
    //String url="http://192.168.0.17:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);



        Thread t = new Thread(runnable);
        t.start();
        Toast.makeText(MainActivity.this, "Posted", Toast.LENGTH_LONG).show();

    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            try {
                URL myUrl = new URL(url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)myUrl.openConnection();
               // httpURLConnection.connect();
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                JSONObject recordObject= new JSONObject();
                recordObject.put("origin", "delhi");
                recordObject.put("destination", "denver");
                JSONArray boundsArray = new JSONArray();
                JSONObject firstBoundObject = new JSONObject();
                JSONObject secondBoundObject = new JSONObject();

                /* creating first object and inserting at first position of array*/
                firstBoundObject.put("latNE", 23.00);
                firstBoundObject.put("longNE", 12.00);
                firstBoundObject.put("latSW", -23.00);
                firstBoundObject.put("longSW", -23.00);
                secondBoundObject.put("latNE", 11.00);
                secondBoundObject.put("longNE", 11.00);
                secondBoundObject.put("latSW", -113.00);
                secondBoundObject.put("longSW", -11.00);

                boundsArray.put(0,firstBoundObject);
                boundsArray.put(1, secondBoundObject);
                recordObject.put("bounds", boundsArray);
                /*Uri.Builder builder = new Uri.Builder();
                String latitude="lat";

                for(int i=0;i<sampleValues.length;i++)
                {
                    builder.appendQueryParameter(latitude+Integer.toString(i), Float.toString(sampleValues[i]));
                }


                String query = builder.build().getEncodedQuery();

                bufferedWriter.write(query);
                */

                bufferedWriter.write(recordObject.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                httpURLConnection.connect();

                is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String line;
                StringBuilder sb = new StringBuilder();
                line= bufferedReader.readLine();
                while(line!=null)
                {
                    sb.append(line);
                    line= bufferedReader.readLine();
                }

                is.close();
                 os.close();

            } catch (Exception e) {

                e.printStackTrace();
            }


        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
