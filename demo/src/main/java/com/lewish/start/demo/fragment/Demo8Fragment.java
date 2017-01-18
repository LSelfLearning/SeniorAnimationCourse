package com.lewish.start.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lewish.start.demo.R;

/**
 * author: sundong
 * created at 2017/1/18 11:15
 */
public class Demo8Fragment extends Fragment {
    private View mContentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.demo8, container, false);
        return mContentView;
    }
}
