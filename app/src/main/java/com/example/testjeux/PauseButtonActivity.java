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

        // Supprimer la barre d'action
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Trouvez le bouton dans la mise en page de l'activité
        Button button = findViewById(R.id.retourButton);

        // Définissez un écouteur d'événements pour le bouton
        button.setOnClickListener(v -> {
            // Lorsque le bouton est cliqué, lancez l'activité TriangleActivity
            Intent intent = new Intent(PauseButtonActivity.this, TriangleActivity.class);
            startActivity(intent);
        });

    }
}