package com.lewish.start.demo.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.lewish.start.demo.R;
import com.lewish.start.demo.fragment.BounceBallFragment;
import com.lewish.start.demo.fragment.SearchFragment;
import com.lewish.start.demo.fragment.Demo8Fragment;
import com.lewish.start.demo.fragment.AnimLoadingFragment;
import com.lewish.start.demo.fragment.HeartBubblesFragment;
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
            case Constants.FRAGMENT_TYPE_HEARTBUBBLESVIEW:
                addFragment(R.id.fl_content, new HeartBubblesFragment());
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
            case Constants.FRAGMENT_TYPE_DEMO8:
                addFragment(R.id.fl_content, new Demo8Fragment());
                break;
            case Constants.FRAGMENT_TYPE_ANIMLOADING:
                addFragment(R.id.fl_content, new AnimLoadingFragment());
                break;
            case Constants.FRAGMENT_TYPE_SEARCHVIEW:
                addFragment(R.id.fl_content, new SearchFragment());
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
