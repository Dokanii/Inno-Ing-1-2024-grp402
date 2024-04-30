package com.example.testjeux;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
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

    private void init() {
        setText("Pause");
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the click event here
            }
        });
    }
}