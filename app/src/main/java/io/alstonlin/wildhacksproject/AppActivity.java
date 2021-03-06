package io.alstonlin.wildhacksproject;

import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.io.Serializable;
import java.util.List;


/**
 * The Activity that contains the entire app after login.
 */
public class AppActivity extends AppCompatActivity implements CameraFragment.OnFragmentInteractionListener, FeedFragment.OnFragmentInteractionListener, Serializable {

    private boolean internet;
    private String code;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        code = (getIntent().getStringExtra(MainActivity.EXTRA_CODE)==null?""+getIntent().getIntExtra(MainActivity.EXTRA_CODE,-1):getIntent().getStringExtra(MainActivity.EXTRA_CODE));
        internet = getIntent().getBooleanExtra(MainActivity.EXTRA_INTERNET, true);
        setupDAO();
        setContentView(R.layout.activity_app);
        setupTabs();
        setupViewPager();
    }

    private void setupDAO(){
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        DAO.instantiate(true, this, Integer.parseInt((code.equals("-1")?"":code)), deviceId);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void refresh(View view) {
        List<android.support.v4.app.Fragment> allFragments = getSupportFragmentManager().getFragments();
        ((FeedFragment) allFragments.get(1)).resetAdapter();
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        findViewById(R.id.loader).setVisibility(View.VISIBLE);
        findViewById(R.id.imageView7).startAnimation(shake);
    }
}
