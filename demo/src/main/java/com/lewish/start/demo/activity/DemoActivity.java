package com.lewish.start.demo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.lewish.start.demo.R;
import com.lewish.start.demo.fragment.BounceBallFragment;
import com.lewish.start.demo.fragment.DianZanfragment;
import com.lewish.start.demo.fragment.LoadingFragment;
import com.lewish.start.demo.fragment.Rotate3DFragment;
import com.lewish.start.demo.fragment.SticknessFragment;
import com.lewish.start.demo.fragment.WaitingDotsFragment;
import com.lewish.start.demo.fragment.WaveViewFragment;
import com.lewish.start.demo.utils.Constants;
/**
 * author: sundong
 * created at 2017/1/18 10:30
 */
public class DemoActivity extends AppCompatActivity {
    private int mFragmentType;
    private FragmentTransaction mTransaction;
    private FrameLayout mFlContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        initVariables();
        initView(savedInstanceState);
        loadData();
    }

    private void loadData() {
        switch (mFragmentType) {
            case Constants.FRAGMENT_TYPE_DIANZAN:
                addFragment(R.id.fl_content, new DianZanfragment());
                break;
            case Constants.FRAGMENT_TYPE_BOUNCEBALL:
                addFragment(R.id.fl_content, new BounceBallFragment());
                break;
            case Constants.FRAGMENT_TYPE_ROTATE3D:
                addFragment(R.id.fl_content, new Rotate3DFragment());
                break;
            case Constants.FRAGMENT_TYPE_LOADINGVIEW:
                addFragment(R.id.fl_content, new LoadingFragment());
                break;
            case Constants.FRAGMENT_TYPE_STICKNESSVIEW:
                addFragment(R.id.fl_content, new SticknessFragment());
                break;
            case Constants.FRAGMENT_TYPE_WAITINGDOTS:
                addFragment(R.id.fl_content, new WaitingDotsFragment());
                break;
            case Constants.FRAGMENT_TYPE_WAVEVIEW:
                addFragment(R.id.fl_content, new WaveViewFragment());
                break;
        }
    }

    private void initView(Bundle savedInstanceState) {
        mFlContent = (FrameLayout) findViewById(R.id.fl_content);
    }

    private void initVariables() {
        Bundle bundle = getIntent().getExtras();
        mFragmentType = bundle.getInt(Constants.FRAGMENT_TYPE, 0);
    }

    private void addFragment(int layoutId, Fragment fragment) {
        mTransaction = getSupportFragmentManager().beginTransaction();
        mTransaction.replace(layoutId, fragment);
        mTransaction.commitAllowingStateLoss();
    }
}
