package com.lewish.start.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lewish.start.demo.R;
import com.lewish.start.demo.utils.Constants;

/**
 * author: sundong
 * created at 2017/1/18 10:30
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_dianzan,
            btn_bounceball,
            btn_rotate3D,
            btn_loadingview,
            btn_sticknessview,
            btn_waitingdots,
            btn_waveview,
            btn_demo8,
            btn_demo9,
            btn_demo10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        btn_dianzan = (Button) findViewById(R.id.btn_heart_bubbles);
        btn_bounceball = (Button) findViewById(R.id.btn_bounceball);
        btn_rotate3D = (Button) findViewById(R.id.btn_rotate3D);
        btn_loadingview = (Button) findViewById(R.id.btn_loadingview);
        btn_sticknessview = (Button) findViewById(R.id.btn_sticknessview);
        btn_waitingdots = (Button) findViewById(R.id.btn_waitingdots);
        btn_waveview = (Button) findViewById(R.id.btn_waveview);
        btn_demo8 = (Button) findViewById(R.id.btn_demo8);
        btn_demo9 = (Button) findViewById(R.id.btn_anim_loading);
        btn_demo10 = (Button) findViewById(R.id.btn_searchview);
    }

    private void initListener() {
        btn_dianzan.setOnClickListener(this);
        btn_bounceball.setOnClickListener(this);
        btn_rotate3D.setOnClickListener(this);
        btn_loadingview.setOnClickListener(this);
        btn_sticknessview.setOnClickListener(this);
        btn_waitingdots.setOnClickListener(this);
        btn_waveview.setOnClickListener(this);
        btn_demo8.setOnClickListener(this);
        btn_demo9.setOnClickListener(this);
        btn_demo10.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.btn_heart_bubbles:
                bundle.putInt(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_TYPE_HEARTBUBBLESVIEW);
                break;
            case R.id.btn_bounceball:
                bundle.putInt(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_TYPE_BOUNCEBALL);
                break;
            case R.id.btn_rotate3D:
                bundle.putInt(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_TYPE_ROTATE3D);
                break;
            case R.id.btn_loadingview:
                bundle.putInt(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_TYPE_LOADINGVIEW);
                break;
            case R.id.btn_sticknessview:
                bundle.putInt(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_TYPE_STICKNESSVIEW);
                break;
            case R.id.btn_waitingdots:
                bundle.putInt(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_TYPE_WAITINGDOTS);
                break;
            case R.id.btn_waveview:
                bundle.putInt(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_TYPE_WAVEVIEW);
                break;
            case R.id.btn_demo8:
                bundle.putInt(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_TYPE_DEMO8);
                break;
            case R.id.btn_anim_loading:
                bundle.putInt(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_TYPE_ANIMLOADING);
                break;
            case R.id.btn_searchview:
                bundle.putInt(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_TYPE_SEARCHVIEW);
                break;
        }
        openActivity(DemoActivity.class, bundle);
    }

    /**
     * 跳转到指定Activity（传参）
     *
     * @param cls
     * @param bundle
     */
    public void openActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 跳转到制定Activity（不传参）
     *
     * @param cls
     */
    public void openActivity(Class<?> cls) {
        openActivity(cls, null);
    }
}
