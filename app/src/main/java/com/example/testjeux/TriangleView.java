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


import android.graphics.Paint;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TriangleView extends View {

    private Bitmap backgroundBitmap;
    private Bitmap background2Bitmap;
    private Bitmap characterBitmap;
    private Bitmap flammeBitmap;
    private int desiredWidth;
    private int desiredHeight;
    private float characterX, fond1Y, fond2Y;
    private boolean isMoving;
    private int backgroundHeight;
    private int background2Height;

    private float flammeX, flammeY;

    private boolean isGameOver = false;

    private List<Asteroid> asteroids = new ArrayList<>();
    private int frameCount = 0;
    private int fond1Height;
    private int fond2Height;

    private int score = 0;

    private static final float BACKGROUND_SPEED = 40.0f;

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
        background2Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fond);
        characterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spaceship);
        flammeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.flamme);

        desiredWidth = 150;
        desiredHeight = 150;
        characterX = 450;
        fond1Y = 0;
        fond2Y = 0;
        flammeX = characterX;
        isMoving = false;
        backgroundHeight = backgroundBitmap.getHeight();
        background2Height = background2Bitmap.getHeight();
        fond1Height = backgroundHeight;
        fond2Height = background2Height;
        startGeneratingAsteroids();
        startUpdatingAsteroids();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        Bitmap resizedBackgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, getWidth(), getHeight(), true);
        canvas.drawBitmap(resizedBackgroundBitmap, 0, fond1Y, null);

        Bitmap resizedBackground2Bitmap = Bitmap.createScaledBitmap(background2Bitmap, getWidth(), getHeight(), true);
        canvas.drawBitmap(resizedBackground2Bitmap, 0, fond2Y - getHeight(), null);


        Bitmap resizedCharacterBitmap = getResizedBitmap(characterBitmap, desiredWidth, desiredHeight);
        canvas.drawBitmap(resizedCharacterBitmap, characterX, 1400, null);

        Bitmap resizedFlammeBitmap = getResizedBitmap(flammeBitmap, desiredWidth-50, desiredHeight-50);
        canvas.drawBitmap(resizedFlammeBitmap, flammeX, flammeY, null);
        hideFlamme();

        // Dessiner les astéroïdes
        for (Asteroid asteroid : asteroids) {
            asteroid.draw(canvas);
        }

        // Afficher le score sur l'écran
        Paint scorePaint = new Paint();
        scorePaint.setTextSize(50);
        scorePaint.setColor(Color.WHITE);
        canvas.drawText("Score: " + score, 20, 50, scorePaint);
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
                showFlamme();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isMoving = false;
                hideFlamme();
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
                        showFlamme();
                        moveBackground();
                        invalidate();
                        postDelayed(this, 16);
                    }
                }
            };
            post(moveRunnable);
        }
    }

    private void moveBackground() {
        updateBackgroundPositions();
        invalidate();
    }

    private void updateBackgroundPositions() {
        fond1Y = (fond1Y + BACKGROUND_SPEED) % backgroundHeight;
        fond2Y = (fond2Y + BACKGROUND_SPEED) % background2Height;
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

        boolean collisionDetected = false;

        for (Asteroid asteroid : asteroids) {
            if (checkCollision(asteroid)) {
                // Collision détectée, arrête le jeu
                stopGame();
                collisionDetected = true;
                break;
            }
        }

        // Si aucune collision n'est détectée, augmenter le score
        if (!collisionDetected) {
            score += 1;
        }

        invalidate(); // Redessine la vue
    }

    private void startGeneratingAsteroids() {
        // Crée une tâche périodique pour générer les astéroïdes toutes les X millisecondes
        Runnable asteroidGenerator = new Runnable() {
            @Override
            public void run() {
                if (isGameOver == false){
                generateAsteroid(getContext());
                postDelayed(this, 2000);} // Génère un astéroïde toutes les 1.5 secondes
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
                if (isGameOver == false){
                    update();
                    postDelayed(this, 10);}
            }
        };

        // Lance la mise à jour des astéroïdes
        post(asteroidUpdater);
    }

    private boolean checkCollision(Asteroid asteroid) {
        // Coordonnées du vaisseau spatial
        float spaceshipLeft = characterX;
        float spaceshipRight = characterX + desiredWidth;
        float spaceshipTop = 1400;
        float spaceshipBottom = 1400 + desiredHeight;

        // Coordonnées de l'astéroïde
        float asteroidLeft = asteroid.getX();
        float asteroidRight = asteroid.getX() + asteroid.getWidth();
        float asteroidTop = asteroid.getY();
        float asteroidBottom = asteroid.getY() + asteroid.getHeight();

        // Vérifie si les coordonnées du vaisseau spatial se trouvent à l'intérieur des coordonnées de l'astéroïde
        return spaceshipLeft < asteroidRight && spaceshipRight > asteroidLeft &&
                spaceshipTop < asteroidBottom && spaceshipBottom > asteroidTop;
    }

    private void stopGame() {
        // Arrête la génération d'astéroïdes
        isGameOver = true;
        // Désactive la possibilité de déplacer le personnage
        isMoving = false;
    }

    private void showFlamme() {
        flammeX = characterX+25; // Positionner la flamme sur le personnage
        flammeY = 1400 + desiredHeight-5; // Positionner la flamme juste en dessous du personnage
        invalidate(); // Redessiner la vue pour afficher la flamme
    }

    // Cacher la flamme
    private void hideFlamme() {
        flammeY = getHeight(); // Faire disparaître la flamme en dehors de l'écran
        invalidate(); // Redessiner la vue pour cacher la flamme
    }
}