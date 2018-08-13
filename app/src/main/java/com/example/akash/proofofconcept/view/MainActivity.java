package com.example.akash.proofofconcept.view;

import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.akash.proofofconcept.R;

public class MainActivity extends AppCompatActivity {
    @VisibleForTesting
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
