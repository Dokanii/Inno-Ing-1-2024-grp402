package com.example.testjeux;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TriangleView extends View {

    private Bitmap backgroundBitmap;
    private Bitmap characterBitmap;
    private int desiredWidth; // Largeur souhaitée de l'image
    private int desiredHeight; // Hauteur souhaitée de l'image
    private float characterX, characterY; // Position du personnage
    private boolean isMoving; // Indique si le personnage est en mouvement
    private int backgroundHeight; // Hauteur de l'image de fond
    private int score; // Score du joueur
    private float characterSpeed; // Vitesse actuelle du personnage
    private long lastFrameTime; // Temps du dernier frame (en millisecondes)

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Charger l'image de fond depuis les ressources
        backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fond);
        // Charger l'image du personnage depuis les ressources
        characterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b99c708025ea1ae4a3a5484907990c4e);
        desiredWidth = 200; // Largeur souhaitée de l'image (en pixels)
        desiredHeight = 200; // Hauteur souhaitée de l'image (en pixels)
        characterX = 450; // Position X initiale du personnage
        characterY = 0; // Position Y initiale du personnage
        isMoving = false; // Initialement, le personnage ne bouge pas
        backgroundHeight = backgroundBitmap.getHeight(); // Récupérer la hauteur de l'image de fond
        score = 0;
        // Initialisation du temps du dernier frame
        lastFrameTime = System.currentTimeMillis();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Redimensionner l'image de fond pour qu'elle remplisse l'écran
        Bitmap resizedBackgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, getWidth(), getHeight(), true);

        // Dessiner l'image de fond redimensionnée avec un décalage vertical pour simuler le défilement
        canvas.drawBitmap(resizedBackgroundBitmap, 0, characterY, null);

        // Redimensionner l'image du personnage à la taille souhaitée
        Bitmap resizedCharacterBitmap = getResizedBitmap(characterBitmap, desiredWidth, desiredHeight);

        // Dessiner l'image du personnage à sa position actuelle
        canvas.drawBitmap(resizedCharacterBitmap, characterX, 1100, null);

        // Calculer le temps écoulé depuis le dernier frame
        long currentTime = System.currentTimeMillis();
        float deltaTime = (currentTime - lastFrameTime) / 1000.0f; // Convertir en secondes

        // Mettre à jour la vitesse du personnage en pixels par seconde
        updateCharacterSpeed(deltaTime);

        // Calculer le score en fonction de la vitesse actuelle du personnage
        calculateScore();

        // Mise à jour du temps du dernier frame
        lastFrameTime = currentTime;
    }

    /*protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Trouver et initialiser votre instance de TriangleView
        TriangleView triangleView = findViewById(R.id.triangleView);

        // ... Autres initialisations de votre activité ...

        // Mise à jour du TextView avec le score actuel
        updateScoreTextView(triangleView.getScore());
    }*/

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
            final float speed = 20.0f; // Vitesse du mouvement du personnage

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
                        moveBackground(); // Appel à moveBackground ici

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

    private void moveBackground() {
        final float backgroundSpeed = 50.0f; // Vitesse du défilement de l'image de fond
        // Mettre à jour characterY pour déplacer l'image de fond vers le haut avec une vitesse différente
        characterY += backgroundSpeed;

        // Si l'image de fond sort complètement de l'écran en haut, réinitialiser sa position Y
        if (characterY <= -backgroundHeight) {
            characterY = getHeight();
        }
    }

    private void updateCharacterSpeed(float deltaTime) {
        // Définir la vitesse de déplacement du personnage en pixels par seconde
        float speed = 200.0f; // Exemple de vitesse en pixels par seconde

        // Mettre à jour la position du personnage en fonction de la vitesse et du temps écoulé
        characterY += speed * deltaTime; // Déplacement vers le bas

        // Réinitialiser la position Y si nécessaire
        if (characterY >= getHeight()) {
            characterY = 0; // Réinitialiser en haut de l'écran
        }

        // Mettre à jour la vitesse actuelle du personnage
        characterSpeed = speed;
    }



    private void calculateScore() {
        // Coefficient de score en fonction de la vitesse du personnage
        float speedCoefficient = 0.5f;

        // Calculer le score en fonction de la vitesse actuelle du personnage
        score += (int) (characterSpeed * speedCoefficient);

        // Afficher ou utiliser le score dans votre application
        Log.d("Score", "Score: " + score);
        updateScoreTextView(score);
    }

    public int getScore() {
        return score;
    }

    private void updateScoreTextView(int score) {
        // Trouver le TextView par son ID
        TextView textViewScore = findViewById(R.id.textViewScore);

        // Mettre à jour le texte du TextView avec le score actuel
        textViewScore.setText("Score: " + score);
    }

    // Supposons que vous avez une instance de TriangleView appelée triangleView
    int currentScore = triangleView.getScore();

    // Trouver le TextView par son ID
    TextView textViewScore = findViewById(R.id.textViewScore);

    // Mettre à jour le texte du TextView avec le score actuel
    textViewScore.setText("Score: " + currentScore);

}
