package com.lmwdelivery.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.lmwdelivery.R;
import com.lmwdelivery.constants.BaseActivity;
import com.lmwdelivery.fragments.PolicyFragment;
import com.lmwdelivery.fragments.ProfileFragment;
import com.lmwdelivery.fragments.TermAndCondFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Policy extends BaseActivity {
    @BindView(R.id.tabs)
    TabLayout tabs;
    private PolicyFragment policyFragment;
    private TermAndCondFragment termAndCondFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        ButterKnife.bind(this);
        bindWidgetsWithAnEvent();
        setupTabLayout();
    }
    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_back:
                setScaleOnButton(view);
                finishAfterTransition();
                break;
        }
    }
    private void setupTabLayout() {

        tabs.addTab(tabs.newTab().setText("Policy"),true);
        tabs.addTab(tabs.newTab().setText("Term And Condition"));
    }
    private void bindWidgetsWithAnEvent()
    {
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setCurrentTabFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
    private void setCurrentTabFragment(int tabPosition)
    {
        switch (tabPosition)
        {
            case 0 :
                replaceFragment(new PolicyFragment());
                break;
            case 1 :
                replaceFragment(new TermAndCondFragment());
                break;
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }
}
