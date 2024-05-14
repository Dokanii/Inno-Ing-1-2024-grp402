package com.example.testjeux;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    private TextView textViewScore;
    private int currentScore = 0; // Score initial

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        // Trouver le TextView dans le layout XML par son ID
        textViewScore = findViewById(R.id.textViewScore);

        // Mettre à jour le score initial dans le TextView
        updateScore();

        // Vous pouvez mettre à jour le score à partir d'ici selon votre logique de jeu
        // Par exemple, pour tester, augmentez le score toutes les quelques secondes
        simulateScoreUpdate();
    }

    // Méthode pour mettre à jour le score affiché dans le TextView
    private void updateScore() {
        textViewScore.setText("Score: " + currentScore);
    }

    // Méthode pour simuler la mise à jour du score (à des fins de démonstration)
    private void simulateScoreUpdate() {
        // Utilisation d'un Thread pour simuler une augmentation de score toutes les 2 secondes
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(2000); // Pause de 2 secondes
                        currentScore += 10; // Augmentation de score
                        // Mettre à jour le score affiché dans le Thread de l'interface utilisateur
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateScore(); // Mettre à jour le score affiché
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
