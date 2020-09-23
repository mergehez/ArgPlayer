package com.arges.sepan.argmusicplayer.Views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;


public class SquareWidthImageView extends AppCompatImageView {
    public SquareWidthImageView(Context context) {
        super(context);
    }

    public SquareWidthImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareWidthImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
