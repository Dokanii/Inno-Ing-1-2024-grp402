// PauseButtonActivity.java
package com.example.testjeux;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class PauseButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pause_button);

        Objects.requireNonNull(getSupportActionBar()).hide();

        PauseButton button = findViewById(R.id.retourButton);
        button.setText("Retour");

        button.setOnClickListener(v -> {
            finish();
        });

    }
}