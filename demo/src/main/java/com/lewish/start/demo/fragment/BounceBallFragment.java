package com.lewish.start.demo.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lewish.start.demo.R;
import com.lewish.start.demo.view.BounceBallView;

/**
 * author: sundong
 * created at 2017/1/18 10:30
 */
public class BounceBallFragment extends Fragment {
    private View mContentView;
    private BounceBallView loadingView;
    private Button btn_changeColor;
    private int colors[] = new int[]{
            Color.parseColor("#ff0000"), Color.parseColor("#00ff00"),
            Color.parseColor("#0000ff"), Color.parseColor("#ffff00")
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_bounceball,container,false);
        initView(mContentView,savedInstanceState);
        initListener();
        return mContentView;
    }

    private void initListener() {
        btn_changeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = (int) (Math.random() * 4);
                loadingView.setColor(colors[index]);
            }
        });
    }

    private void initView(View mContentView, Bundle savedInstanceState) {
        btn_changeColor = (Button) mContentView.findViewById(R.id.btn_changeColor);
        loadingView = (BounceBallView) mContentView.findViewById(R.id.loadingView);
    }
}
