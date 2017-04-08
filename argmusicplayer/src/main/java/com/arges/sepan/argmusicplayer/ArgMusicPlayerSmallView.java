package com.arges.sepan.argmusicplayer;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

/**
 * Created by arges on 4/4/2017.
 */

public class ArgMusicPlayerSmallView extends ArgMusicPlayerRoot {

    public ArgMusicPlayerSmallView(Context context) {
        super(context);
    }
    public ArgMusicPlayerSmallView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ArgMusicPlayerSmallView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, int layoutResId) {
        super.init(context,R.layout.musicplayerpanel);
    }
}
