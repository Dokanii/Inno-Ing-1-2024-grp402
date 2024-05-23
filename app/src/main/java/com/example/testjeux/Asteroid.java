package com.example.testjeux;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

public class Asteroid {

    private float x;
    private float y;
    private float speed;
    private int size;
    private Bitmap bitmap;
    private int desiredWidth;
    private int desiredHeight;
    private int taille;



    // Constructeur


    public Asteroid(Context context, float x, float y) {
        bitmap= BitmapFactory.decodeResource(context.getResources(), R.drawable.asteroid2);
        this.x = x;
        this.y = y;
        taille = (int) (Math.random() * (250 - 100 + 1) + 100);
        desiredWidth = taille;
        desiredHeight = taille;
        //this.speed = (float) (Math.random() * 5 + 1);
        this.speed = 30;
        this.bitmap = getResizedAsteroidBitmap(bitmap, desiredWidth, desiredHeight);
    }

    private Bitmap getResizedAsteroidBitmap(Bitmap bitmap, int width, int height) {
        float scaleWidth = ((float) width) / bitmap.getWidth();
        float scaleHeight = ((float) height) / bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    // Méthode de mise à jour de l'astéroïde
    public void update() {
        y += speed;
    }

    // Méthode de dessin de l'astéroïde
    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x, y, null);
    }
    public float getY() {return y;}
    public float getX() {return x;}
    public float getWidth() {return desiredWidth;}
    public float getHeight() {return desiredHeight;}



}