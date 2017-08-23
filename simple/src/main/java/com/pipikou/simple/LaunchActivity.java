package com.pipikou.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by boboyuwu on 2017/8/23.
 */

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity_layout);
    }

    public void toThreeHead(View view){
        ThreeHeaderPinnedActivity.GoToThreeHeaderActivity(this);
    }

    public void toTwoHead(View view){
        TwoHeaderPinnedActivity.GoToTwoHeaderActivity(this);
    }


    public void toOneHead(View view){
        OneHeaderPinnedActivity.GoToOneHeaderActivity(this);
    }

    public void toNoHead(View view){
        NoHeaderPinnedActivity.GoToSecondActivity(this);
    }
}
