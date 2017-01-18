package com.lewish.start.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lewish.start.demo.R;

/**
 * Created by Administrator on 2017/1/18 10:37.
 */

public class Rotate3DFragment extends Fragment {
    private View mContentView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_rotate3d,container,false);
        return mContentView;
    }
}
