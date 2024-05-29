package com.example.testjeux;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TriangleView extends View {

    private Bitmap backgroundBitmap;
    private Bitmap background2Bitmap;
    private Bitmap characterBitmap;
    private Bitmap flammeBitmap;
    private Bitmap shieldOn;
    private int desiredWidth;
    private int desiredHeight;
    private float characterX, fond1Y, fond2Y;
    private boolean isMoving;
    private int backgroundHeight;
    private int background2Height;

    private float flammeX, flammeY;

    private boolean isGameOver = false;

    private boolean power = false;
    private int score = 0;

    private List<Asteroid> asteroids = new ArrayList<>();
    private List<Shield> shields = new ArrayList<>();
    private int frameCount = 0;
    private int fond1Height;
    private int fond2Height;
    private Bitmap shieldBitmap;
    private float shieldX, shieldY;
    private boolean isShieldActive = false;

    private static final float BACKGROUND_SPEED = 4.0f;

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
        shieldBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.shield);
        shieldOn = BitmapFactory.decodeResource(getResources(), R.drawable.shieldon);

        desiredWidth = 150;
        desiredHeight = 150;
        characterX = 450;
        fond1Y = 0;
        fond2Y = 0;
        flammeX = characterX;
        shieldX = 0;
        shieldY = 0;
        isMoving = false;
        backgroundHeight = backgroundBitmap.getHeight();
        background2Height = background2Bitmap.getHeight();
        fond1Height = backgroundHeight;
        fond2Height = background2Height;
        startGeneratingAsteroids();
        startUpdatingAsteroids();
        startGeneratingShield();
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

        Bitmap resizedFlammeBitmap = getResizedBitmap(flammeBitmap, desiredWidth - 50, desiredHeight - 50);
        canvas.drawBitmap(resizedFlammeBitmap, flammeX, flammeY, null);


        for (Asteroid asteroid : asteroids) {
            asteroid.draw(canvas);
        }

        for (Shield shield : shields) {
            shield.draw(canvas);
        }

        if (isShieldActive) {
            Bitmap resizedShieldOnShipBitmap = getResizedBitmap(shieldOn, desiredWidth + 100, desiredHeight + 100);
            canvas.drawBitmap(resizedShieldOnShipBitmap, characterX - 50, 1400 - 50, null);
        }

        Paint scorePaint = new Paint();
        scorePaint.setTextSize(60);
        scorePaint.setColor(Color.WHITE);
        canvas.drawText("Score: " + score, 33, 50, scorePaint);
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

    private Handler backgroundHandler = new Handler();
    private Runnable backgroundUpdater = new Runnable() {
        @Override
        public void run() {
            updateBackgroundPositions();
            backgroundHandler.postDelayed(this, 10); // Update every 10 milliseconds
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        backgroundHandler.post(backgroundUpdater);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        backgroundHandler.removeCallbacks(backgroundUpdater);
    }

    private void updateBackgroundPositions() {
        fond1Y = (fond1Y + BACKGROUND_SPEED) % backgroundHeight;
        fond2Y = (fond2Y + BACKGROUND_SPEED) % background2Height;
        invalidate();
    }

    private void generateAsteroid(Context context) {
        int asteroidX = (int) (Math.random() * getWidth());
        int asteroidY = 0;
        Asteroid asteroid = new Asteroid(getContext(), asteroidX, asteroidY);
        asteroids.add(asteroid);
    }

    private void updateAsteroids() {
        List<Asteroid> asteroidsToRemove = new ArrayList<>();

        for (Asteroid asteroid : asteroids) {
            asteroid.update();
            if (asteroid.getY() > getHeight()) {
                asteroidsToRemove.add(asteroid);
            }
        }

        asteroids.removeAll(asteroidsToRemove);
        invalidate();
    }

    private void updateShields() {
        List<Shield> shieldsToRemove = new ArrayList<>();

        for (Shield shield : shields) {
            shield.update();
            if (shield.getY() > getHeight()) {
                shieldsToRemove.add(shield);
            }
        }

        shields.removeAll(shieldsToRemove);
        invalidate();
    }

    private void update() {
        boolean collisionDetected = false;
        updateAsteroids();
        updateShields();
        frameCount++;
        if (frameCount % (60 * 10) == 0) {
            generateShield();
        }

        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid asteroid = asteroidIterator.next();
            if (checkCollision(asteroid)) {
                if (isShieldActive) {
                    isShieldActive = false;
                    asteroidIterator.remove();
                    power = false;
                } else {
                    stopGame();
                    collisionDetected = true;
                    return;
                }
            }
            if (!collisionDetected) {
                score += 1;
            }
        }

        Iterator<Shield> shieldIterator = shields.iterator();
        while (shieldIterator.hasNext()) {
            Shield shield = shieldIterator.next();
            if (checkShieldCollision(shield)) {
                shieldIterator.remove();
            }
        }

        invalidate();
    }

    private void startGeneratingAsteroids() {
        Runnable asteroidGenerator = new Runnable() {
            @Override
            public void run() {
                if (!isGameOver) {
                    generateAsteroid(getContext());
                    postDelayed(this, 2000);
                }
            }
        };
        post(asteroidGenerator);
    }

    private void startUpdatingAsteroids() {
        Runnable asteroidUpdater = new Runnable() {
            @Override
            public void run() {
                if (!isGameOver) {
                    update();
                    postDelayed(this, 10);
                }
            }
        };
        post(asteroidUpdater);
    }

    private boolean checkCollision(Asteroid asteroid) {
        float spaceshipLeft = characterX;
        float spaceshipRight = characterX + desiredWidth;
        float spaceshipTop = 1400;
        float spaceshipBottom = 1400 + desiredHeight;

        float asteroidLeft = asteroid.getX();
        float asteroidRight = asteroid.getX() + asteroid.getWidth();
        float asteroidTop = asteroid.getY();
        float asteroidBottom = asteroid.getY() + asteroid.getHeight();

        return spaceshipLeft < asteroidRight && spaceshipRight > asteroidLeft &&
                spaceshipTop < asteroidBottom && spaceshipBottom > asteroidTop;
    }

    private boolean checkShieldCollision(Shield shield) {
        float spaceshipLeft = characterX;
        float spaceshipRight = characterX + desiredWidth;
        float spaceshipTop = 1400;
        float spaceshipBottom = 1400 + desiredHeight;

        float shieldLeft = shield.getX();
        float shieldRight = shield.getX() + shield.getWidth();
        float shieldTop = shield.getY();
        float shieldBottom = shield.getY() + shield.getHeight();

        if (shieldRight > spaceshipLeft && shieldLeft < spaceshipRight &&
                shieldBottom > spaceshipTop && shieldTop < spaceshipBottom) {
            isShieldActive = true;
            power = true;
            return true;
        }
        return false;
    }

    private void stopGame() {
        isGameOver = true;
        isMoving = false;

        // Start GameOverActivity
        Context context = getContext();
        Intent intent = new Intent(context, GameOverActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void showFlamme() {
        flammeX = characterX + 25;
        flammeY = 1400 + desiredWidth - 5;
        invalidate();
    }

    private void hideFlamme() {
        flammeY = getHeight();
        invalidate();
    }


    private void generateShield() {
        int shieldX = (int) (Math.random() * getWidth());
        int shieldY = 0;
        Bitmap resizedShieldBitmap = getResizedBitmap(shieldBitmap, 150, 150);
        Shield shield = new Shield(getContext(), shieldX, shieldY, resizedShieldBitmap);
        shields.add(shield);
    }

    private void startGeneratingShield() {
        Runnable shieldGenerator = new Runnable() {
            @Override
            public void run() {
                if (!isGameOver) {
                    if (power == false) {
                        generateShield();
                        postDelayed(this, 10000);}
                }
            }
        };
        post(shieldGenerator);
    }
}
