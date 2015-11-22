package io.alstonlin.wildhacksproject;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Activity that contains the entire app after login.
 */
public class AppActivity extends AppCompatActivity implements CameraFragment.OnFragmentInteractionListener, FeedFragment.OnFragmentInteractionListener, Serializable {

    private boolean internet;
    private int code;
    private TabLayout tabLayout;
    private Command callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        code = getIntent().getIntExtra(MainActivity.EXTRA_CODE, -1);
        internet = getIntent().getBooleanExtra(MainActivity.EXTRA_INTERNET, false);
        setupDAO();
        setupView();
        setupTabs();
        setupViewPager();
    }

    private void setupDAO(){
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DAO.instantiate(internet, this, code, deviceId, new Command() {
            @Override
            public void exec(ArrayList<ImageItem> items) {
                callback.exec(items);
            }
        });
    }

    private void setupView(){
        setContentView(R.layout.activity_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupTabs(){
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Camera"));
        tabLayout.addTab(tabLayout.newTab().setText("Feed"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
    }

    private void setupViewPager(){
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void setCallback(Command callback){
        this.callback = callback;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
