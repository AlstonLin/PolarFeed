package io.alstonlin.wildhacksproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

/**
 * Entry point of the app. This is the login page.
 */
public class MainActivity extends FragmentActivity {
    public final static String EXTRA_CODE = "io.alstonlin.wildhacksproject.CODE";
    public final static String EXTRA_INTERNET = "io.alstonlin.wildhacksproject.INTERNET";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button joinButton = (Button) findViewById(R.id.join);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = ((EditText) findViewById(R.id.code)).getText().toString();
                Switch switc = (Switch) findViewById(R.id.internet);
                try {
                    int code = Integer.parseInt(s);
                    boolean internet = switc.isChecked();

                    Intent intent = new Intent(MainActivity.this, AppActivity.class);
                    intent.putExtra(EXTRA_CODE, code);
                    intent.putExtra(EXTRA_INTERNET, internet);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, "Invalid Code", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
