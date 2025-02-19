package com.example.testjeux;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class TurretBullet {
    private float x, y;
    private static final float SPEED = 15.0f;
    private boolean isDestroyed = false;
    private Bitmap bulletBitmap; // Le bitmap représentant l'image du missile

    // Le constructeur prend désormais un Context pour charger le bitmap
    public TurretBullet(Context context, float x, float y) {
        this.x = x;
        this.y = y;
        // Charger l'image du missile. Remplace R.drawable.missile_enemy par ton image.
        bulletBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.laser2);
        bulletBitmap = Bitmap.createScaledBitmap(bulletBitmap, 80, 80, true);
    }

    public void update() {
        if (!isDestroyed) {
            y += SPEED;
        }
    }

    // Utiliser drawBitmap au lieu de drawRect
    public void draw(Canvas canvas) {
        if (!isDestroyed && bulletBitmap != null) {
            canvas.drawBitmap(bulletBitmap, x, y, null);
        }
    }

    public boolean checkCollision(float spaceshipX, float spaceshipY, int spaceshipWidth, int spaceshipHeight) {
        Rect bulletRect = new Rect((int)x, (int)y, (int)(x + bulletBitmap.getWidth()), (int)(y + bulletBitmap.getHeight()));
        Rect spaceshipRect = new Rect((int)spaceshipX, (int)spaceshipY, (int)(spaceshipX + spaceshipWidth), (int)(spaceshipY + spaceshipHeight));

        if (Rect.intersects(bulletRect, spaceshipRect)) {
            isDestroyed = true;
            return true;
        }
        return false;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }
}
