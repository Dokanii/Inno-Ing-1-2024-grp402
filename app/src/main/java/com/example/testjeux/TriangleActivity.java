package com.example.testjeux;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import java.util.Objects;

public class TriangleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triangle);

        // Supprimer la barre d'action
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Trouvez le bouton dans la mise en page de l'activité
        // Button button = findViewById(R.id.pauseButton);

        // Définition d'un écouteur d'événements pour le bouton
        //button.setOnClickListener(v -> {
            // Lorsque le bouton est cliqué, lancez l'activité PauseButtonActivity
            //Intent intent = new Intent(TriangleActivity.this, PauseButtonActivity.class);
            //startActivity(intent);
        //});
    }

}

