package com.lmwdelivery.activities;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lmwdelivery.BuildConfig;
import com.lmwdelivery.R;
import com.lmwdelivery.constants.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static java.lang.String.valueOf;

public class Dashboard extends BaseActivity {
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    Toolbar toolbar;
    boolean isClick = true;
    boolean doubleBackToExitPressedOnce = false;
    ImageView menu_items;
    Fragment currentFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        ButterKnife.bind(this);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();
        setAppVersion();
    }
    void setupDrawerToggle() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
                isClick = true;
            }

            /**
             * Called when a drawer has settled in a completely open state.
             */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
                isClick = false;

            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }
    @OnClick({R.id.menu_items, R.id.ll_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_items:
                if (isClick) {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                    isClick = false;
                    if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                        isClick = false;
                    }
                    Log.d("Getting Boolean2", valueOf(isClick));
                } else {
                    mDrawerLayout.closeDrawers();
                    isClick = true;
                    Log.d("Getting Boolean3", valueOf(isClick));
                }
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    isClick = false;
                }

                break;

            case R.id.ll_share:
                mDrawerLayout.closeDrawers();
                shareAppIntent();
                break;


        }
    }




    @Override
    public void onBackPressed() {


        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);

//        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            FragmentManager fm = getSupportFragmentManager();
//
//            if (fm.getBackStackEntryCount() > 0) {
//                fm.popBackStack();
//            } else if ((currentFragment instanceof HomeFragment)) {
//
//                //TODO
//            } else {
//               // setFragment(new HomeFragment());
//
//            }
//        }
    }

    //share
    private void shareAppIntent() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Download LMW app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void setAppVersion() {
        TextView version = findViewById(R.id.app_version);
        version.setText("v - " + BuildConfig.VERSION_NAME);
    }
}
