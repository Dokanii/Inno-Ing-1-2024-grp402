package com.example.testjeux;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;

public class PauseButton extends androidx.appcompat.widget.AppCompatButton {

    public PauseButton(Context context) {
        super(context);
        init();
    }

    public PauseButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        setText("Pause");
        setOnClickListener(v -> {
            // TriangleView.isGameOver = true;
        });
    }
}