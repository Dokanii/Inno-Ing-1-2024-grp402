package com.example.testjeux;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import java.util.Random;

public class KamikazeShip {
    private Bitmap bitmap;
    private float x, y;
    private static final float SPEED_Y = 20.0f; // Vitesse de descente
    private static final float SPEED_X = 10.0f; // Vitesse de suivi horizontal
    private boolean isDestroyed = false;
    private float targetX; // Position cible du joueur

    public KamikazeShip(Context context, float startX) {
        this.bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.spaceship2);
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap, 150, 150, true);
        this.x = startX;
        this.y = -bitmap.getHeight(); // Spawn en haut de l'écran
        this.targetX = startX; // Au départ, il ne suit pas encore le joueur
    }

    public void update(float playerX) {
        if (isDestroyed) return;


        // Suivre la position du joueur horizontalement
        if (playerX > x) {
            x += SPEED_X; // Se déplace vers la droite
        } else if (playerX < x) {
            x -= SPEED_X; // Se déplace vers la gauche
        }

        // Descendre vers le joueur
        y += SPEED_Y;
    }

    public void draw(Canvas canvas) {
        if (!isDestroyed) {
            canvas.drawBitmap(bitmap, x, y, null);
        }
    }

    public boolean checkCollision(float spaceshipX, float spaceshipY, int spaceshipWidth, int spaceshipHeight) {
        // 🔹 Vérifier si les rectangles des sprites se superposent (collision approximative)
        Rect enemyRect = new Rect((int)x, (int)y, (int)(x + bitmap.getWidth()), (int)(y + bitmap.getHeight()));
        Rect spaceshipRect = new Rect((int)spaceshipX, (int)spaceshipY, (int)(spaceshipX + spaceshipWidth), (int)(spaceshipY + spaceshipHeight));

        if (!Rect.intersects(enemyRect, spaceshipRect)) {
            return false; // Pas de collision
        }

        // 🔹 Vérification fine avec le canal alpha
        int startX = Math.max((int) x, (int) spaceshipX);
        int startY = Math.max((int) y, (int) spaceshipY);
        int endX = Math.min((int) (x + bitmap.getWidth()), (int) (spaceshipX + spaceshipWidth));
        int endY = Math.min((int) (y + bitmap.getHeight()), (int) (spaceshipY + spaceshipHeight));

        for (int pixelX = startX; pixelX < endX; pixelX++) {
            for (int pixelY = startY; pixelY < endY; pixelY++) {
                int enemyPixel = bitmap.getPixel(pixelX - (int) x, pixelY - (int) y);

                // 🔹 Vérifier si le pixel est opaque (alpha > 0)
                if ((enemyPixel >> 24) != 0) {
                    return true; // Collision détectée sur un pixel visible
                }
            }
        }

        return false; // Aucune collision pixel-perfect détectée
    }


    public boolean isOutOfScreen(int screenHeight) {
        return y > screenHeight;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
}
