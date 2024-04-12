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
    private int desiredWidth; // Desired width of the image
    private int desiredHeight; // Desired height of the image
    private float imageX, imageY; // Position of the image

    public TriangleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Load the image from resources
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.b99c708025ea1ae4a3a5484907990c4e);
        desiredWidth = 200; // Desired width of the image (in pixels)
        desiredHeight = 200; // Desired height of the image (in pixels)
        imageX = 450; // Initial X position of the image
        imageY = 1100; // Initial Y position of the image
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Resize the image to the desired size
        Bitmap resizedBitmap = getResizedBitmap(bitmap, desiredWidth, desiredHeight);

        // Draw the image at the current position
        canvas.drawBitmap(resizedBitmap, imageX, imageY, null);
    }

    // Method to resize a bitmap
    private Bitmap getResizedBitmap(Bitmap bitmap, int width, int height) {
        // Calculate the scale factor
        float scaleWidth = ((float) width) / bitmap.getWidth();
        float scaleHeight = ((float) height) / bitmap.getHeight();

        // Create a matrix for the scale factor
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // Apply the matrix to the bitmap
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Calculate the direction of the touch event
                float touchX = event.getX();
                float directionX = 0;
                if (touchX > getWidth() / 2) {
                    directionX = 1;
                } else {
                    directionX = -1;
                }

                // Calculate the new position of the image
                float newImageX = imageX + directionX * 10;

                // Update the position of the image
                imageX = Math.min(getWidth() - desiredWidth, Math.max(0, newImageX));

                invalidate(); // Redraw the view
                return true;
        }
        return super.onTouchEvent(event);
    }

}