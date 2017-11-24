package com.lewish.start.rotateview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button mBtn;
    private OddView mOddView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOddView = findViewById(R.id.mOddView);
        findViewById(R.id.mBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOddView.setProgress(1f,10000);
            }
        });

    }
}
