package com.example.testjeux;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TriangleView extends View {

    private Bitmap backgroundBitmap;
    private Bitmap characterBitmap;
    private int desiredWidth;
    private int desiredHeight;
    private float characterX, characterY;
    private boolean isMoving;
    private int backgroundHeight;

    private List<Asteroid> asteroids = new ArrayList<>();
    private int frameCount = 0;

    public TriangleView(Context context) {
        super(context);
        init(context);

    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fond);
        characterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b99c708025ea1ae4a3a5484907990c4e);

        desiredWidth = 200;
        desiredHeight = 200;
        characterX = 450;
        characterY = 0;
        isMoving = false;
        backgroundHeight = backgroundBitmap.getHeight();
        startGeneratingAsteroids();
        startUpdatingAsteroids();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        @SuppressLint("DrawAllocation") Bitmap resizedBackgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, getWidth(), getHeight(), true);
        canvas.drawBitmap(resizedBackgroundBitmap, 0, characterY, null);

        Bitmap resizedCharacterBitmap = getResizedBitmap(characterBitmap, desiredWidth, desiredHeight);
        canvas.drawBitmap(resizedCharacterBitmap, characterX, 1100, null);

        // Dessiner les astéroïdes
        for (Asteroid asteroid : asteroids) {
            asteroid.draw(canvas);
        }
    }


    private Bitmap getResizedBitmap(Bitmap bitmap, int width, int height) {
        float scaleWidth = ((float) width) / bitmap.getWidth();
        float scaleHeight = ((float) height) / bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float touchX = event.getX();
                moveCharacter(touchX > (float) getWidth() / 2);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isMoving = false;
                break;
        }
        return true;
    }

    private void moveCharacter(final boolean moveRight) {
        if (!isMoving) {
            isMoving = true;
            final float speed = 20.0f;

            Runnable moveRunnable = new Runnable() {
                @Override
                public void run() {


                    if (isMoving) {
                        if (moveRight) {
                            characterX += speed;
                        } else {
                            characterX -= speed;
                        }
                        characterX = Math.max(0, Math.min(characterX, getWidth() - desiredWidth));
                        moveBackground();
                        //update();
                        invalidate();
                        postDelayed(this, 16);
                    }
                }
            };
            post(moveRunnable);
        }
    }

    private void moveBackground() {
        final float backgroundSpeed = 50.0f;
        characterY+= backgroundSpeed;

        if (characterY <= -backgroundHeight) {
            characterY = getHeight();
        }
    }

    private void generateAsteroid(Context context) {
        // Générer une position aléatoire sur l'axe X
        int asteroidX = (int) (Math.random() * getWidth());

        // Générer une position aléatoire sur l'axe Y (en haut de l'écran)
        int asteroidY = 0;

        // Créer un nouvel astéroïde
        Asteroid asteroid = new Asteroid(getContext(), asteroidX, asteroidY);

        // Ajouter l'astéroïde à la liste des astéroïdes
        asteroids.add(asteroid);
    }
    private void updateAsteroids() {
        // Liste temporaire pour stocker les astéroïdes à supprimer
        List<Asteroid> asteroidsToRemove = new ArrayList<>();

        // Mettre à jour la position des astéroïdes
        for (Asteroid asteroid : asteroids) {
            asteroid.update();

            // Vérifier si l'astéroïde est sorti de l'écran
            if (asteroid.getY() > getHeight()) {
                // Ajouter l'astéroïde à la liste des astéroïdes à supprimer
                asteroidsToRemove.add(asteroid);
            }
        }

        // Supprimer les astéroïdes de la liste principale
        asteroids.removeAll(asteroidsToRemove);
        invalidate();
    }

    private void update() {
        updateAsteroids(); // Met à jour la position des astéroïdes
        invalidate(); // Redessine la vue
    }

    private void startGeneratingAsteroids() {
        // Crée une tâche périodique pour générer les astéroïdes toutes les X millisecondes
        Runnable asteroidGenerator = new Runnable() {
            @Override
            public void run() {
                generateAsteroid(getContext());
                postDelayed(this, 2000); // Génère un astéroïde toutes les 1.5 secondes
            }
        };

        // Lance la génération des astéroïdes
        post(asteroidGenerator);
    }

    private void startUpdatingAsteroids() {
        // Crée une tâche périodique pour mettre à jour les astéroïdes toutes les X millisecondes
        Runnable asteroidUpdater = new Runnable() {
            @Override
            public void run() {
                update();
                postDelayed(this, 10); // Met à jour les astéroïdes environ toutes les 16 millisecondes
            }
        };

        // Lance la mise à jour des astéroïdes
        post(asteroidUpdater);
    }
}