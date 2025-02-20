package com.example.testjeux;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
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
import java.util.Stack;
import java.util.Random;


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

    private static final float BACKGROUND_SPEED = 6.0f;
    private static final int INITIAL_ASTEROID_GENERATION_DELAY = 1600; // Délai initial de génération des astérooïdes
    private int asteroidGenerationDelay = INITIAL_ASTEROID_GENERATION_DELAY;

    private List<Missile> missiles = new ArrayList<>();
    private List<MissilePack> missilePacks = new ArrayList<>();
    private Bitmap missileBitmap;
    private Bitmap missilePackBitmap;

    private Bitmap missileIconBitmap;
    private boolean canShoot = false; // Le joueur peut tirer ?

    private Stack<Bitmap> missileStack = new Stack<>();  // Pile de missiles stockés
    private float missileIconX, missileIconY; // Position de l’icône du missile
    private static final int MAX_MISSILES = 5;  // Nombre max de missiles stockables

    private List<ExplosionGif> explosions = new ArrayList<>();

    private List<Turret> turrets = new ArrayList<>();
    private static List<TurretBullet> enemyBullets = new ArrayList<>();

    public static void addEnemyBullet(TurretBullet bullet) {
        enemyBullets.add(bullet);
    }



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

        missileBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.laser);
        missilePackBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.missile);
        startGeneratingMissilePacks();

        missileIconBitmap = getResizedBitmap(
                BitmapFactory.decodeResource(getResources(), R.drawable.missile),
                115, // Largeur de l'icône du missile
                115  // Hauteur de l'icône du missile
        );


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
        hideFlamme();


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

        for (Missile missile : missiles) {
            missile.draw(canvas);
        }

        for (MissilePack pack : missilePacks) {
            pack.draw(canvas);
        }

        for (ExplosionGif explosion : explosions) {
            explosion.draw(canvas);
        }

        for (Turret turret : turrets) {
            turret.draw(canvas);
        }



        drawMissileIcons(canvas);


        Paint scorePaint = new Paint();
        scorePaint.setTextSize(60);
        scorePaint.setColor(Color.WHITE);
        canvas.drawText("Score: " + score, 33, 50, scorePaint);

        Paint bulletPaint = new Paint();
        for (TurretBullet bullet : enemyBullets) {
            bullet.draw(canvas);
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
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Vérifier si le joueur touche une icône de missile
                if (!missileStack.isEmpty()) {
                    float iconWidth = 80; // Taille d'une icône de missile
                    float iconHeight = 80;
                    float startX = getWidth() / 2 - (missileStack.size() * 50) / 2;
                    float startY = getHeight() - 120;

                    // Vérifie si le toucher est dans la zone des icônes de missile
                    for (int i = 0; i < missileStack.size(); i++) {
                        float missileX = startX + i * 50;
                        float missileY = startY;

                        if (touchX >= missileX && touchX <= missileX + iconWidth &&
                                touchY >= missileY && touchY <= missileY + iconHeight) {
                            shootMissile();
                            return true; // Évite d'interpréter ce clic comme un déplacement du vaisseau
                        }
                    }
                }

                // Si le joueur ne touche pas une icône de missile, il peut déplacer le vaisseau
                moveCharacter(touchX > getWidth() / 2);
                showFlamme();
                break;

            case MotionEvent.ACTION_MOVE:
                moveCharacter(touchX > (float) getWidth() / 2);
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
            final float speed = 35.0f;

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
            backgroundHandler.postDelayed(this, 10); // Update toutes les 10 millisecondes
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
        int asteroidY = -400;
        if (!TriangleActivity.getPauseButtonState()){
            Asteroid asteroid = new Asteroid(getContext(), asteroidX, asteroidY);
            asteroids.add(asteroid);
        }
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

        if (!TriangleActivity.getPauseButtonState()) {
            updateAsteroids();
            updateShields();
        }

        frameCount++;
        if (frameCount % (60 * 10) == 0) {
            generateShield();
        }

        // Mise à jour des missiles
        Iterator<Missile> missileIterator = missiles.iterator();
        while (missileIterator.hasNext()) {
            Missile missile = missileIterator.next();
            missile.update();

            // Vérification de la collision avec les astéroïdes
            Iterator<Asteroid> asteroidIterator = asteroids.iterator();
            while (asteroidIterator.hasNext()) {
                Asteroid asteroid = asteroidIterator.next();
                if (missile.checkCollision(asteroid)) {
                    missile.destroy();  // Désactive le missile
                    // Créer l'explosion à la position de l'astéroïde avec le GIF
                    ExplosionGif explosion = new ExplosionGif(getContext(), R.raw.explosion, (int)asteroid.getX(), (int)asteroid.getY());
                    explosions.add(explosion);
                    asteroidIterator.remove();  // Supprime l'astéroïde
                    missileIterator.remove();  // Supprime le missile
                    break;  // Sort de la boucle car un missile ne peut toucher qu'un astéroïde
                }
            }

            // Supprimer le missile s'il sort de l'écran
            if (missile.getY() < 0) {
                missileIterator.remove();
            }
        }

        // Vérification de la collision avec les astéroïdes (pour le vaisseau)
        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {
            Asteroid asteroid = asteroidIterator.next();
            asteroid.update();
            if (checkCollision(asteroid)) {
                if (isShieldActive) {
                    isShieldActive = false;
                    asteroidIterator.remove();
                    // Créer l'explosion à la position de l'astéroïde avec le GIF
                    ExplosionGif explosion = new ExplosionGif(getContext(), R.raw.explosion, (int)asteroid.getX(), (int)asteroid.getY());
                    explosions.add(explosion);
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

        // 🔹 **Mise à jour et gestion des boucliers**
        Iterator<Shield> shieldIterator = shields.iterator();
        while (shieldIterator.hasNext()) {
            Shield shield = shieldIterator.next();
            shield.update();
            if (checkShieldCollision(shield)) {
                shieldIterator.remove();
            }
        }

        // Vérification de la collision avec les packs de missiles
        Iterator<MissilePack> packIterator = missilePacks.iterator();
        while (packIterator.hasNext()) {
            MissilePack pack = packIterator.next();
            pack.update();
            if (checkMissilePackCollision(pack)) {
                canShoot = true;  // Active la possibilité de tirer
                packIterator.remove();
            }
        }

        Iterator<ExplosionGif> explosionIterator = explosions.iterator();
        while (explosionIterator.hasNext()) {
            ExplosionGif explosion = explosionIterator.next();
            explosion.update();
            if (explosion.isFinished()) {
                explosionIterator.remove();
            }
        }

        if (frameCount % (60 * 10) == 0) {  // Toutes les 10 secondes
            generateTurret();
        }

        Iterator<Turret> turretIterator = turrets.iterator();
        while (turretIterator.hasNext()) {
            Turret turret = turretIterator.next();
            turret.update();

            // Réutiliser l'iterator déjà défini ailleurs dans update()
            for (int i = 0; i < missiles.size(); i++) {
                Missile missile = missiles.get(i);
                if (turret.checkCollision(missile)) {
                    missiles.remove(i);
                    turretIterator.remove();
                    ExplosionGif explosion = new ExplosionGif(getContext(), R.raw.explosion, (int)turret.getX(), (int)turret.getY());
                    explosions.add(explosion);
                    break;
                }
            }
        }

        Iterator<TurretBullet> bulletIterator = enemyBullets.iterator();
        while (bulletIterator.hasNext()) {
            TurretBullet bullet = bulletIterator.next();
            bullet.update();

            if (bullet.checkCollision(characterX, 1400, desiredWidth, desiredHeight)) {
                if (isShieldActive) {
                    // Le bouclier est actif : le missile est absorbé et le bouclier se désactive (ou sa durabilité est réduite)
                    isShieldActive = false;
                    bulletIterator.remove();
                } else {
                    // Sinon, le missile touche le vaisseau et le jeu se termine
                    stopGame();
                    return;
                }
            }
        }



    }


    private void startGeneratingAsteroids() {
        Runnable asteroidGenerator = new Runnable() {
            @Override
            public void run() {
                if (!isGameOver) {
                    generateAsteroid(getContext());
                    // Diminuer le délai de génération à chaque nouvelle génération
                    asteroidGenerationDelay -= 25; // Réduire le délai de 100 millisecondes à chaque nouvelle génération
                    // Limiter le délai minimal de génération
                    asteroidGenerationDelay = Math.max(500, asteroidGenerationDelay); // Limiter à 1000 millisecondes (1 seconde) minimum
                    postDelayed(this, asteroidGenerationDelay);
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
        Bitmap spaceshipBitmap = getResizedBitmap(characterBitmap, desiredWidth, desiredHeight);
        Bitmap asteroidBitmap = getResizedBitmap(asteroid.getBitmap(), (int) asteroid.getWidth(), (int) asteroid.getHeight());

        int left = (int) Math.max(characterX, asteroid.getX());
        int right = (int) Math.min(characterX + desiredWidth, asteroid.getX() + asteroid.getWidth());
        int top = (int) Math.max(1400, asteroid.getY());
        int bottom = (int) Math.min(1400 + desiredHeight, asteroid.getY() + asteroid.getHeight());

        for (int x = left; x < right; x++) {
            for (int y = top; y < bottom; y++) {
                int shipX = x - (int) characterX;
                int shipY = y - 1400;
                int asteroidX = x - (int) asteroid.getX();
                int asteroidY = y - (int) asteroid.getY();

                if (isPixelOpaque(spaceshipBitmap, shipX, shipY) && isPixelOpaque(asteroidBitmap, asteroidX, asteroidY)) {
                    return true; // Collision détectée
                }
            }
        }
        return false;
    }

    private boolean checkShieldCollision(Shield shield) {
        Bitmap spaceshipBitmap = getResizedBitmap(characterBitmap, desiredWidth, desiredHeight);
        Bitmap shieldBitmap = getResizedBitmap(shield.getBitmap(), shield.getWidth(), shield.getHeight());

        int left = (int) Math.max(characterX, shield.getX());
        int right = (int) Math.min(characterX + desiredWidth, shield.getX() + shield.getWidth());
        int top = (int) Math.max(1400, shield.getY());
        int bottom = (int) Math.min(1400 + desiredHeight, shield.getY() + shield.getHeight());

        for (int x = left; x < right; x++) {
            for (int y = top; y < bottom; y++) {
                int shipX = x - (int) characterX;
                int shipY = y - 1400;
                int shieldX = x - (int) shield.getX();
                int shieldY = y - (int) shield.getY();

                if (isPixelOpaque(spaceshipBitmap, shipX, shipY) && isPixelOpaque(shieldBitmap, shieldX, shieldY)) {
                    isShieldActive = true;
                    power = true;
                    return true; // Collision avec un pixel opaque du bouclier
                }
            }
        }
        return false;
    }



    private boolean isPixelOpaque(Bitmap bitmap, int x, int y) {
        if (x < 0 || x >= bitmap.getWidth() || y < 0 || y >= bitmap.getHeight()) {
            return false; // Hors limites
        }

        int pixel = bitmap.getPixel(x, y);
        return (pixel >> 24) != 0; // Vérifie si l’alpha est différent de 0
    }


    private void stopGame() {
        isGameOver = true;
        isMoving = false;

        // Vider les listes pour nettoyer l'écran
        asteroids.clear();       // Déjà existant
        missiles.clear();        // Supprime les missiles du joueur
        turrets.clear();         // Supprime les tourelles ennemies
        enemyBullets.clear();    // Supprime les missiles ennemis

        // Créer l'intent et ajouter le score en extra
        Intent intent = new Intent(getContext(), GameOverActivity.class);
        intent.putExtra("score", score);
        getContext().startActivity(intent);

        // Terminer l'activité en cours
        Activity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
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
        if (!TriangleActivity.getPauseButtonState()){
            Shield shield = new Shield(getContext(), shieldX, shieldY, resizedShieldBitmap);
            shields.add(shield);
        }
    }

    private void startGeneratingShield() {
        Runnable shieldGenerator = new Runnable() {
            @Override
            public void run() {
                if (!isGameOver) {
                    if (power == false) {
                        generateShield();
                        postDelayed(this, 20000);}
                }
            }
        };
        post(shieldGenerator);
    }

    private void generateMissilePack() {
        int packX = (int) (Math.random() * getWidth());
        int packY = 0;
        Bitmap resizedMissilePackBitmap = getResizedBitmap(missilePackBitmap, 100, 100);
        if (!TriangleActivity.getPauseButtonState()) {
            MissilePack pack = new MissilePack(getContext(), packX, packY, resizedMissilePackBitmap);
            missilePacks.add(pack);
        }
    }

    private void startGeneratingMissilePacks() {
        Runnable missilePackGenerator = new Runnable() {
            @Override
            public void run() {
                if (!isGameOver) {
                    generateMissilePack();
                    postDelayed(this, 20000); // Un pack toutes les 20 secondes
                }
            }
        };
        post(missilePackGenerator);
    }

    private boolean checkMissilePackCollision(MissilePack pack) {
        Bitmap spaceshipBitmap = getResizedBitmap(characterBitmap, desiredWidth, desiredHeight);
        Bitmap packBitmap = getResizedBitmap(pack.getBitmap(), pack.getWidth(), pack.getHeight());

        int left = (int) Math.max(characterX, pack.getX());
        int right = (int) Math.min(characterX + desiredWidth, pack.getX() + pack.getWidth());
        int top = (int) Math.max(1400, pack.getY());
        int bottom = (int) Math.min(1400 + desiredHeight, pack.getY() + pack.getHeight());

        for (int x = left; x < right; x++) {
            for (int y = top; y < bottom; y++) {
                int shipX = x - (int) characterX;
                int shipY = y - 1400;
                int packX = x - (int) pack.getX();
                int packY = y - (int) pack.getY();

                if (isPixelOpaque(spaceshipBitmap, shipX, shipY) && isPixelOpaque(packBitmap, packX, packY)) {
                    if (missileStack.size() < MAX_MISSILES) {  // Limite de stockage
                        missileStack.push(getResizedBitmap(missileBitmap, 80, 80));
                    }
                    return true;
                }
            }
        }
        return false;
    }


    private void shootMissile() {
        if (!missileStack.isEmpty()) {
            missileStack.pop(); // Retire un missile stocké

            Bitmap resizedMissileBitmap = getResizedBitmap(missileBitmap, 50, 100);
            Missile missile = new Missile(getContext(), characterX + desiredWidth / 2 - 25, 1300, resizedMissileBitmap);
            missiles.add(missile);
        }
    }


    private void drawMissileIcons(Canvas canvas) {
        float startX = getWidth() / 2 - (missileStack.size() * 50) / 2; // Centrage des icônes
        float startY = getHeight() - 120; // Position en bas de l’écran

        for (int i = 0; i < missileStack.size(); i++) {
            canvas.drawBitmap(missileIconBitmap, startX + i * 50, startY, null);
        }

        // Sauvegarde la position de la première icône pour la détection tactile
        if (!missileStack.isEmpty()) {
            missileIconX = startX;
            missileIconY = startY;
        }
    }

    private void generateTurret() {
        Random random = new Random();
        int turretX = random.nextInt(getWidth() - 100); // Position aléatoire en haut de l'écran
        int turretY = random.nextInt(400); // Ne pas trop haut ni trop bas

        Bitmap turretBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.spaceship2);
        turretBitmap = Bitmap.createScaledBitmap(turretBitmap, desiredWidth, desiredHeight, true);

        Turret turret = new Turret(getContext(), turretBitmap, turretX, turretY);
        turrets.add(turret);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }







}