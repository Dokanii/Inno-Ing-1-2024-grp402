package com.example.testjeux;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import java.util.Objects;

public class TriangleActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PAUSE = 1;
    private static boolean PauseButtonState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_triangle);

        // Supprimer la barre d'action
        Objects.requireNonNull(getSupportActionBar()).hide();

        // Trouvez le bouton dans la mise en page de l'activité
        Button button = findViewById(R.id.pauseButton);

        // Définition d'un écouteur d'événements pour le bouton
        button.setOnClickListener(v -> {
            // Lorsque le bouton est cliqué, lancez l'activité PauseButtonActivity
            Intent intent = new Intent(TriangleActivity.this, PauseButtonActivity.class);
            startActivityForResult(intent, REQUEST_CODE_PAUSE);
            PauseButtonState = true;
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(TriangleActivity.this, SettingsActivity.class);
            startActivity(intent);
            PauseButtonState = true;
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PAUSE) {
            if (resultCode == RESULT_OK) {
                // Restart the game
                finish();  // Finish current activity
                startActivity(new Intent(this, TriangleActivity.class));  // Start a new instance of the activity
            } else if (resultCode == RESULT_CANCELED) {
                // Go back to the main menu
                finish();  // Finish current activity
                startActivity(new Intent(this, MainActivity.class));
            } else if (resultCode == RESULT_FIRST_USER) {
                // Resume the game
                PauseButtonState = false;
            }
        }
    }

    public static boolean getPauseButtonState() {
        return PauseButtonState;
    }

    public static void resetPauseButtonState() {
        PauseButtonState = false;
    }
}
