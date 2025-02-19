package com.example.testjeux;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import java.util.Random;

public class Turret {
    private Bitmap bitmap;
    private float x, y;
    private static final int SHOOT_INTERVAL = 2000; // Tire toutes les 2 secondes
    private long lastShootTime;
    private boolean isDestroyed;

    private Context context;


    public Turret(Context context, Bitmap bitmap, float x, float y) {
        this.context = context;  // Stocke le contexte pour usage ultérieur
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.isDestroyed = false;
        this.lastShootTime = System.currentTimeMillis();
    }


    public void update() {
        if (isDestroyed) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastShootTime >= SHOOT_INTERVAL) {
            shoot();
            lastShootTime = currentTime;
        }
    }

    public void draw(Canvas canvas) {
        if (!isDestroyed) {
            canvas.drawBitmap(bitmap, x, y, null);
        }
    }

    private void shoot() {
        if (isDestroyed) return;

        float bulletX = x + bitmap.getWidth() / 2;
        float bulletY = y + bitmap.getHeight();
        // Utilise le contexte fourni au constructeur de Turret
        TurretBullet bullet = new TurretBullet(context, bulletX, bulletY);
        TriangleView.addEnemyBullet(bullet);
    }


    public boolean checkCollision(Missile missile) {
        Rect turretRect = new Rect((int) x, (int) y, (int) (x + bitmap.getWidth()), (int) (y + bitmap.getHeight()));
        Rect missileRect = new Rect((int) missile.getX(), (int) missile.getY(),
                (int) (missile.getX() + missile.getBitmap().getWidth()),
                (int) (missile.getY() + missile.getBitmap().getHeight()));

        if (Rect.intersects(turretRect, missileRect)) {
            isDestroyed = true;
            return true;
        }
        return false;
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
