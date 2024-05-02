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

public class TriangleView extends View {
    private Bitmap backgroundBitmap;
    private Bitmap characterBitmap;
    private int desiredWidth;
    private int desiredHeight;
    private float characterX, characterY;
    private boolean isMoving;
    private int backgroundHeight;

    public TriangleView(Context context) {
        super(context);
        init();
    }

    public TriangleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fond);
        characterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b99c708025ea1ae4a3a5484907990c4e);

        desiredWidth = 200;
        desiredHeight = 200;
        characterX = 450;
        characterY = 0;
        isMoving = false;
        backgroundHeight = backgroundBitmap.getHeight();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        @SuppressLint("DrawAllocation") Bitmap resizedBackgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, getWidth(), getHeight(), true);
        canvas.drawBitmap(resizedBackgroundBitmap, 0, characterY, null);

        Bitmap resizedCharacterBitmap = getResizedBitmap(characterBitmap, desiredWidth, desiredHeight);
        canvas.drawBitmap(resizedCharacterBitmap, characterX, 1100, null);
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
}