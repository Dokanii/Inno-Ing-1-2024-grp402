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

    private Bitmap bitmap;
    private int desiredWidth; // Largeur désirée de l'image
    private int desiredHeight; // Hauteur désirée de l'image
    private float imageX, imageY; // Position de l'image

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Charge l'image à partir des ressources
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b99c708025ea1ae4a3a5484907990c4e);
        desiredWidth = 200; // Largeur désirée de l'image (en pixels)
        desiredHeight = 200; // Hauteur désirée de l'image (en pixels)
        imageX = 450; // Position X initiale de l'image
        imageY = 1800; // Position Y initiale de l'image
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Redimensionne l'image à la taille désirée
        Bitmap resizedBitmap = getResizedBitmap(bitmap, desiredWidth, desiredHeight);

        // Dessine l'image aux coordonnées (imageX, imageY)
        canvas.drawBitmap(resizedBitmap, imageX, imageY, null);
    }

    // Méthode pour redimensionner une image Bitmap
    private Bitmap getResizedBitmap(Bitmap bitmap, int width, int height) {
        // Calcul des facteurs d'échelle pour redimensionner l'image
        float scaleWidth = ((float) width) / bitmap.getWidth();
        float scaleHeight = ((float) height) / bitmap.getHeight();

        // Crée une matrice de transformation pour redimensionner l'image
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // Applique la matrice de transformation à l'image
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Lorsque le doigt bouge sur l'écran, met à jour la position de l'image
                float touchX = event.getX();
                float touchY = event.getY();

                // Déplace l'image en diagonale vers la droite de l'écran
                if (touchX > getWidth() / 2) {
                    imageX = Math.min(getWidth() - desiredWidth, Math.max(0, imageX + touchX - getWidth() / 2));
                    imageY = Math.min(getHeight() - desiredHeight, Math.max(0, imageY + touchY - getHeight() / 2));
                }
                // Déplace l'image en diagonale vers la gauche de l'écran
                else {
                    imageX = Math.min(getWidth() - desiredWidth, Math.max(0, imageX - touchX + getWidth() / 2));
                    imageY = Math.min(getHeight() - desiredHeight, Math.max(0, imageY - touchY + getHeight() / 2));
                }

                invalidate(); // Redessine la vue
                return true;
        }
        return super.onTouchEvent(event);
    }

}
