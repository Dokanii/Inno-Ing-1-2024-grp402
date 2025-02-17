package com.example.testjeux;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;

public class MusicManager {

    private static MediaPlayer mediaPlayer;

    public static void startMusic(Context context) {
        if (!isMusicEnabled(context)) {
            return; // Ne pas jouer la musique si elle est désactivée
        }

        if (mediaPlayer == null) {
            Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.duelo_maestro);
            mediaPlayer = MediaPlayer.create(context, uri);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        } else if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }


    public static void stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public static void restartMusic(Context context) {
        stopMusic();  // Arrête et libère le MediaPlayer
        startMusic(context);  // Redémarre la musique
    }

    public static void playGameOverMusic(Context context) {
        stopMusic(); // Arrête la musique principale

        Uri uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.game_over_song);
        mediaPlayer = MediaPlayer.create(context, uri);
        mediaPlayer.setLooping(false); // Jouer une seule fois
        mediaPlayer.start();
    }

    public static boolean isMusicEnabled(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        return preferences.getBoolean("musicEnabled", true);
    }


}
