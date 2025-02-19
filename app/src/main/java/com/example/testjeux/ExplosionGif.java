package com.example.testjeux;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.os.SystemClock;

public class ExplosionGif {
    private Movie movie;
    private long movieStart = 0;
    private int x, y;
    private boolean finished;

    public ExplosionGif(Context context, int resourceId, int x, int y) {
        // Charge le GIF depuis les ressources (par exemple dans res/raw/explosion.gif)
        movie = Movie.decodeStream(context.getResources().openRawResource(resourceId));
        this.x = x;
        this.y = y;
        finished = false;
    }

    public void update() {
        long now = SystemClock.uptimeMillis();
        if (movieStart == 0) {
            movieStart = now;
        }
        // Si le temps écoulé dépasse la durée du GIF, on le considère terminé
        if (movie != null && now - movieStart >= movie.duration()) {
            finished = true;
        }
    }

    public void draw(Canvas canvas) {
        if (movie == null || finished) return;
        long now = SystemClock.uptimeMillis();
        int relTime = (int)((now - movieStart) % movie.duration());
        movie.setTime(relTime);
        canvas.save();
        // Optionnel : centrer le GIF à l'emplacement souhaité
        canvas.translate(x, y);
        movie.draw(canvas, 0, 0);
        canvas.restore();
    }

    public boolean isFinished() {
        return finished;
    }
}
