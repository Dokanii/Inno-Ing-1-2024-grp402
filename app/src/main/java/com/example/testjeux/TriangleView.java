package com.example.testjeux;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TriangleView extends View {

    private Bitmap backgroundBitmap;
    private Bitmap characterBitmap;
    private int desiredWidth; // Largeur souhaitée de l'image
    private int desiredHeight; // Hauteur souhaitée de l'image
    private float characterX, characterY; // Position du personnage
    private boolean isMoving; // Indique si le personnage est en mouvement
    private int backgroundHeight; // Hauteur de l'image de fond

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Charger l'image de fond depuis les ressources
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fond);
        // Charger l'image du personnage depuis les ressources
        characterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b99c708025ea1ae4a3a5484907990c4e);
        desiredWidth = 200; // Largeur souhaitée de l'image (en pixels)
        desiredHeight = 200; // Hauteur souhaitée de l'image (en pixels)
        characterX = 450; // Position X initiale du personnage
        characterY = 1100; // Position Y initiale du personnage
        isMoving = false; // Initialement, le personnage ne bouge pas
        backgroundHeight = backgroundBitmap.getHeight(); // Récupérer la hauteur de l'image de fond
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Redimensionner l'image de fond pour qu'elle remplisse l'écran
        Bitmap resizedBackgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, getWidth(), getHeight(), true);

        // Dessiner l'image de fond redimensionnée
        canvas.drawBitmap(resizedBackgroundBitmap, 0, 0, null);

        // Redimensionner l'image du personnage à la taille souhaitée
        Bitmap resizedCharacterBitmap = getResizedBitmap(characterBitmap, desiredWidth, desiredHeight);

        // Dessiner l'image du personnage à sa position actuelle
        canvas.drawBitmap(resizedCharacterBitmap, characterX, characterY, null);
    }

    // Méthode pour redimensionner une bitmap
    private Bitmap getResizedBitmap(Bitmap bitmap, int width, int height) {
        // Calcul du facteur d'échelle
        float scaleWidth = ((float) width) / bitmap.getWidth();
        float scaleHeight = ((float) height) / bitmap.getHeight();

        // Création d'une matrice pour le facteur d'échelle
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // Application de la matrice à la bitmap
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float touchX = event.getX();
                // Vérifier si le toucher est sur le côté gauche ou droit de l'écran
                if (touchX > getWidth() / 2) {
                    // Toucher sur le côté droit de l'écran
                    moveCharacter(true);  // Déplacer vers la droite
                } else {
                    // Toucher sur le côté gauche de l'écran
                    moveCharacter(false); // Déplacer vers la gauche
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // Arrêter le mouvement du personnage
                isMoving = false;
                break;
        }
        return true;
    }

    private void moveCharacter(final boolean moveRight) {
        if (!isMoving) {
            isMoving = true;
            final float speed = 20.0f; // Vitesse du mouvement

            Runnable moveRunnable = new Runnable() {
                @Override
                public void run() {
                    if (isMoving) {
                        // Mettre à jour characterX en fonction de la direction
                        if (moveRight) {
                            characterX += speed;
                        } else {
                            characterX -= speed;
                        }

                        // Assurer que le personnage reste dans les limites de la vue
                        characterX = Math.max(0, Math.min(characterX, getWidth() - desiredWidth));

                        // Faire défiler l'image de fond
                        moveBackground(true);

                        // Redessiner la vue
                        invalidate();

                        // Récursion pour continuer le mouvement
                        postDelayed(this, 16); // environ 60 FPS
                    }
                }
            };

            post(moveRunnable);
        }
    }

    private void moveBackground(final boolean moveUp) {
        final float speed = 10.0f; // Vitesse du défilement
        // Mettre à jour characterY en fonction de la direction
        if (moveUp) {
            characterY -= speed;
        } else {
            characterY += speed;
        }

        // Si l'image de fond sort complètement de l'écran en haut, réinitialiser sa position Y et charger une nouvelle image de fond
        if (characterY <= -backgroundHeight) {
            characterY = 0;
            backgroundBitmap.recycle(); // Libérer la mémoire de l'image de fond actuelle
            backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fond);
            backgroundHeight = backgroundBitmap.getHeight(); // Mettre à jour la hauteur de l'image de fond
        }

        // Assurer que le personnage reste dans les limites de la vue
        characterY = Math.max(0, Math.min(characterY, getHeight() - desiredHeight));
    }
}
