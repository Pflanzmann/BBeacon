package com.bbeacon.uI.activitys;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.bbeacon.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("OwnLog", "Start App");

    }
}