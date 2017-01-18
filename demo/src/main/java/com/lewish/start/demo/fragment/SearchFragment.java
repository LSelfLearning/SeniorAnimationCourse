package com.lewish.start.demo.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lewish.start.demo.R;
import com.lewish.start.demo.view.SearchView;

/**
 * author: sundong
 * created at 2017/1/18 11:15
 */
public class SearchFragment extends Fragment {
    private View mContentView;
    private SearchView mSwitchView;
    private Button btnSwitch;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_search,container,false);
        mSwitchView = (SearchView) mContentView.findViewById(R.id.view_search);
        btnSwitch = (Button) mContentView.findViewById(R.id.btn_search);
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwitchView.start();
            }
        });
        return mContentView;
    }
}
