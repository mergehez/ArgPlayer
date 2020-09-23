package com.arges.sepan.argmusicplayer.Views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;


public class SquareHeightImageView extends AppCompatImageView {
    public SquareHeightImageView(Context context) {
        super(context);
    }

    public SquareHeightImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareHeightImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}
