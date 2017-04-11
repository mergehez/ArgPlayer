package com.arges.sepan.argmusicplayer.PlayerViews;

import android.content.Context;
import android.util.AttributeSet;

import com.arges.sepan.argmusicplayer.ArgPlayerSmallViewRoot;
import com.arges.sepan.argmusicplayer.R;

public class ArgPlayerSmallView extends ArgPlayerSmallViewRoot {
    public ArgPlayerSmallView(Context context) {
        super(context);
    }

    public ArgPlayerSmallView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArgPlayerSmallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, int layoutResId) {
        super.init(context, R.layout.player_small_layout);
    }
}
