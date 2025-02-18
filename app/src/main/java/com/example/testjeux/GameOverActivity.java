package com.example.testjeux;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

        // Récupérer le score envoyé par TriangleView
        int lastScore = getIntent().getIntExtra("score", 0);

// Charger le meilleur score depuis SharedPreferences
        SharedPreferences preferences = getSharedPreferences("GameData", MODE_PRIVATE);
        int bestScore = preferences.getInt("bestScore", 0);

// Mettre à jour le meilleur score si nécessaire
        if (lastScore > bestScore) {
            bestScore = lastScore;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("bestScore", bestScore);
            editor.apply();
        }

// Afficher les scores
        TextView scoreText = findViewById(R.id.scoreText);
        scoreText.setText("Score : " + lastScore);

        TextView bestScoreText = findViewById(R.id.bestScoreText);
        bestScoreText.setText("Meilleur Score : " + bestScore);




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
