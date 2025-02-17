package com.example.testjeux;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class MissilePack {
    private Bitmap bitmap;
    private float x, y;
    private static final float SPEED = 20.0f;

    public MissilePack(Context context, float x, float y, Bitmap bitmap) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
    }

    public void update() {
        if (!TriangleActivity.getPauseButtonState()) {
            y += SPEED; // Le pack descend
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getWidth() {
        return bitmap.getWidth();
    }

    public int getHeight() {
        return bitmap.getHeight();
    }

    public Bitmap getBitmap() {
        return bitmap;
    }


}
