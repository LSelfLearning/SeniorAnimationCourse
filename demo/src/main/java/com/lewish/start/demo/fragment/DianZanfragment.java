package com.lewish.start.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lewish.start.demo.R;
import com.lewish.start.demo.view.DianZanView;

/**
 * author: sundong
 * created at 2017/1/18 10:30
 */
public class DianZanfragment extends Fragment {
    private View mContentView;
    private DianZanView periscopeLayout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_dianzan, container, false);
        initView(mContentView,savedInstanceState);
        initListener();
        return mContentView;
    }

    private void initView(View mContentView, Bundle savedInstanceState) {
         periscopeLayout = (DianZanView) mContentView.findViewById(R.id.periscope);
    }

    private void initListener() {
        periscopeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                periscopeLayout.addHeart();
            }
        });
    }
}
