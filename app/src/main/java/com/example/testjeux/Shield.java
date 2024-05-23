package com.example.testjeux;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Shield {

    private Bitmap bitmap;
    private float x, y;
    private int width, height;
    private static final float SPEED = 10.0f;

    public Shield(Context context, float x, float y, Bitmap bitmap) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        this.width = bitmap.getWidth();
        this.height = bitmap.getHeight();
    }

    public void update() {
        y += SPEED;
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
        return width;
    }

    public int getHeight() {
        return height;
    }
}
