package com.example.testjeux;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Trouve le bouton dans la mise en page de l'activité
        Button button = findViewById(R.id.button);

        // Définis un écouteur d'événements pour le bouton
        button.setOnClickListener(v -> {
            // Lorsque le bouton est cliqué, lance l'activité TriangleActivity
            Intent intent = new Intent(MainActivity.this, TriangleActivity.class);
            startActivity(intent);
        });

        Objects.requireNonNull(getSupportActionBar()).hide();

        // Start the background music
        MusicManager.startMusic(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the background music when the activity is destroyed
        MusicManager.stopMusic();
    }
}
