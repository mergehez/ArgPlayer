package com.arges.sepan.argmusicplayer;

import android.content.Context;
import android.util.AttributeSet;

import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudio;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudioList;


public class ArgPlayerView extends ArgPlayerViewRoot {
    @Override
    void setEmbeddedImageBitmap(byte[] byteArray) {
    }

    @Override
    void onAudioNameChanged(ArgAudio audio) {

    }

    @Override
    void onPlaylistAudioChanged(ArgAudioList list) {

    }
    public ArgPlayerView(Context context) {
        super(context);
    }

    public ArgPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArgPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void init(Context context, int layoutResId) {
        super.init(context, layoutResId);
    }
}
