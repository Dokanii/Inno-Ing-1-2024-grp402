package com.example.testjeux;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class TriangleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new TriangleView(this, null));
    }
}