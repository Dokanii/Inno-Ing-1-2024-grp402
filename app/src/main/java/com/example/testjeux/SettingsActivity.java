package com.example.testjeux;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private Switch musicSwitch;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Supprimer la barre d'action
        Objects.requireNonNull(getSupportActionBar()).hide();

        preferences = getSharedPreferences("GameSettings", Context.MODE_PRIVATE);

        // Récupérer l'état de la musique
        boolean isMusicEnabled = preferences.getBoolean("musicEnabled", true);

        // Initialiser le Switch
        musicSwitch = findViewById(R.id.musicSwitch);
        musicSwitch.setChecked(isMusicEnabled);

        // Gérer le changement d'état du Switch
        musicSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("musicEnabled", isChecked);
            editor.apply();

            if (isChecked) {
                MusicManager.startMusic(SettingsActivity.this);
            } else {
                MusicManager.pauseMusic();
            }
        });
    }
}
