package com.example.testjeux;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Missile {
    private Bitmap bitmap;
    private float x, y;
    private static final float SPEED = 20.0f;
    private boolean isActive = true; // Pour savoir si le missile est toujours en jeu

    public Missile(Context context, float x, float y, Bitmap bitmap) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (!TriangleActivity.getPauseButtonState()) {
            y -= SPEED; // Le missile monte vers le haut
        }
    }

    public void draw(Canvas canvas) {
        if (isActive) {
            canvas.drawBitmap(bitmap, x, y, null);
        }
    }

    public boolean checkCollision(Asteroid asteroid) {
        float missileLeft = x;
        float missileRight = x + bitmap.getWidth();
        float missileTop = y;
        float missileBottom = y + bitmap.getHeight();

        float asteroidLeft = asteroid.getX();
        float asteroidRight = asteroid.getX() + asteroid.getWidth();
        float asteroidTop = asteroid.getY();
        float asteroidBottom = asteroid.getY() + asteroid.getHeight();

        return missileLeft < asteroidRight && missileRight > asteroidLeft &&
                missileTop < asteroidBottom && missileBottom > asteroidTop;
    }

    public void destroy() {
        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public float getY() {
        return y;
    }
}
