package com.arges.sepan.argmusicplayer.Views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;


public class SquareHeightImageView extends android.support.v7.widget.AppCompatImageView {
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
