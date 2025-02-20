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

    private int shotsFired = 0; // Compteur de missiles tirés
    private static final int MAX_SHOTS_BEFORE_MOVE = 3; // Nombre de tirs avant déplacement



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
        TurretBullet bullet = new TurretBullet(context, bulletX, bulletY);
        TriangleView.addEnemyBullet(bullet);

        shotsFired++; // Incrémenter le nombre de tirs

        // Si la tourelle a tiré 3 missiles, elle se déplace
        if (shotsFired >= MAX_SHOTS_BEFORE_MOVE) {
            moveTurret();
            shotsFired = 0; // Réinitialiser le compteur après déplacement
        }
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

    private void moveTurret() {
        Random random = new Random();

        // Nouvelle position aléatoire sur l'axe X (toujours visible à l'écran)
        int newX = random.nextInt(TriangleView.getScreenWidth() - bitmap.getWidth());
        int newY = random.nextInt(400); // Garde la tourelle en haut de l'écran

        x = newX;
        y = newY;
    }


}
