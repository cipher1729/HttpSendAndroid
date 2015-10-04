package com.testconnectionwithserver;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

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
    String url="http://mapheroku.herokuapp.com/test";
    float[] sampleValues ={-90.03f, 20.33f};
    //String url="http://192.168.0.17:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);



        Thread t = new Thread(runnable);
        t.start();

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
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                Uri.Builder builder = new Uri.Builder();
                String latitude="lat";
                for(int i=0;i<sampleValues.length;i++)
                {
                    builder.appendQueryParameter(latitude+Integer.toString(i), Float.toString(sampleValues[i]));
                }

                /*Uri.Builder builder = new Uri.Builder().appendQueryParameter("something","elza");
                        //.appendQueryParameter("second","two")
                        //.appendQueryParameter("third","three");
                */

                String query = builder.build().getEncodedQuery();

                bufferedWriter.write(query);
                bufferedWriter.flush();
                bufferedWriter.close();
                httpURLConnection.connect();
                InputStream is = httpURLConnection.getInputStream();
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
