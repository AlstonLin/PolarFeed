package io.alstonlin.wildhacksproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class AddEventsActivity extends AppCompatActivity {


    public final static String EXTRA_CODE = "io.alstonlin.wildhacksproject.CODE";
    public final static String EXTRA_INTERNET = "io.alstonlin.wildhacksproject.INTERNET";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_events, menu);
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

    public void addEvent(View view) {

        String[] information = new String[]{
                ((EditText)findViewById(R.id.event)).getText().toString(),
                ((EditText)findViewById(R.id.location)).getText().toString(),
                ((EditText)findViewById(R.id.description)).getText().toString()
        };
        if (!information[0].equals(information[1]) && !information[1].equals(information[2])) {
            Event event = new Event("", information[0], information[1], information[2]);
            try {
                try {
                    String code = DAO.getInstance().postEvent(event);
                    Intent intent = new Intent(this, AppActivity.class);
                    intent.putExtra(MainActivity.EXTRA_CODE, code);
                    intent.putExtra(MainActivity.EXTRA_INTERNET, true);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else{
            Toast.makeText(this,"Please input valid informaiton",Toast.LENGTH_SHORT).show();
        }
    }

    private void nextTask(String s){
        Intent intent = new Intent(AddEventsActivity.this, AppActivity.class);
        if(s == null){
            Random r = new Random();
            intent.putExtra(EXTRA_CODE, (r.nextInt(100)+1000));
        } else{
            intent.putExtra(EXTRA_CODE, Integer.parseInt(s));
        }
        intent.putExtra(EXTRA_INTERNET, false);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
