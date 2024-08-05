package com.example.testjeux;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

public class MusicManager {

    private static MediaPlayer mediaPlayer;

    public static void startMusic(Context context) {
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
}
