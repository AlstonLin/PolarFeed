package io.alstonlin.wildhacksproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends FragmentActivity {
    public final static String EXTRA_CODE = "io.alstonlin.wildhacksproject";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button joinButton = (Button) findViewById(R.id.join);
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = ((EditText) findViewById(R.id.code)).getText().toString();
                Intent intent = new Intent(MainActivity.this, AppActivity.class);

                //TODO: Validate code

                intent.putExtra(EXTRA_CODE, code);
                startActivity(intent);
            }
        });
    }
}
