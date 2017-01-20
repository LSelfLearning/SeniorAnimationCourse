package com.lewish.start.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lewish.start.demo.R;
import com.lewish.start.demo.view.WaitingDotsView;

/**
 * author: sundong
 * created at 2017/1/18 11:15
 */
public class WaitingDotsFragment extends Fragment {
    private View mContentView;
    private WaitingDotsView dotsTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_waitingdots, container, false);
        dotsTextView = (WaitingDotsView) mContentView.findViewById(R.id.dots);
        return mContentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        dotsTextView.start();
    }
}
