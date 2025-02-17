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
        MusicManager.pauseMusic();

        Objects.requireNonNull(getSupportActionBar()).hide();

        Button buttonRetour = findViewById(R.id.retourButton);
        buttonRetour.setText("Retour");

        buttonRetour.setOnClickListener(v -> {
            MusicManager.startMusic(this);
            TriangleActivity.resetPauseButtonState();
            setResult(RESULT_FIRST_USER);  // Custom result code for resuming the game
            finish();
        });

        Button buttonRecommencer = findViewById(R.id.button_recommencer);

        buttonRecommencer.setOnClickListener(v -> {
            MusicManager.restartMusic(this);
            TriangleActivity.resetPauseButtonState();
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);  // Signal that we want to restart
            finish();  // Close this activity
        });

        Button buttonRetourMenu = findViewById(R.id.button_retourMenu);

        buttonRetourMenu.setOnClickListener(v -> {
            MusicManager.restartMusic(this);
            TriangleActivity.resetPauseButtonState();
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);  // Signal that we want to go back to the menu
            finish();  // Close this activity
        });
    }
}
