package com.arges.sepan.argmusicplayer.PlayerViews;

import android.content.Context;
import android.util.AttributeSet;

import com.arges.sepan.argmusicplayer.ArgPlayerLargeViewRoot;
import com.arges.sepan.argmusicplayer.R;


public class ArgPlayerLargeView extends ArgPlayerLargeViewRoot {
    public ArgPlayerLargeView(Context context) {
        super(context);
    }

    public ArgPlayerLargeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArgPlayerLargeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, int layoutResId) {
        super.init(context, R.layout.player_large_layout);
    }
}
