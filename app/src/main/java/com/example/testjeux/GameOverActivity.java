package com.example.testjeux;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class GameOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over);

        // Supprimer la barre d'action
        Objects.requireNonNull(getSupportActionBar()).hide();

        MusicManager.playGameOverMusic(this);

        Button restartButton = findViewById(R.id.restartButton);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Restart la partie
                MusicManager.restartMusic(GameOverActivity.this);
                Intent intent = new Intent(GameOverActivity.this, TriangleActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
