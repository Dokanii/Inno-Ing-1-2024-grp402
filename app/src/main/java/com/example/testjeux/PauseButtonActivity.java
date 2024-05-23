// PauseButtonActivity.java
package com.example.testjeux;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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

        // Find the "Recommencer" button in the layout
        Button buttonRecommencer = findViewById(R.id.button_recommencer);

        // Set an OnClickListener for the "Recommencer" button
        buttonRecommencer.setOnClickListener(v -> {
            // Create a new Intent to start TriangleActivity
            Intent intent = new Intent(PauseButtonActivity.this, TriangleActivity.class);

            // Start the new Intent
            startActivity(intent);
        });

    }


}