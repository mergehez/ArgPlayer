package com.arges.sepan.argmusicplayer.PlayerViews;

import android.content.Context;
import android.util.AttributeSet;

import com.arges.sepan.argmusicplayer.ArgPlayerFullScreenViewRoot;
import com.arges.sepan.argmusicplayer.R;

public class ArgPlayerFullScreenView extends ArgPlayerFullScreenViewRoot {
    public ArgPlayerFullScreenView(Context context) {
        super(context);
    }

    public ArgPlayerFullScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArgPlayerFullScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, int layoutResId) {
        super.init(context, R.layout.player_fullscreen_layout);
    }
}
