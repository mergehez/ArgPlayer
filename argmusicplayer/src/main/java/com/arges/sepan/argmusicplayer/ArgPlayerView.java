package com.arges.sepan.argmusicplayer;

import android.content.Context;
import android.util.AttributeSet;

import com.arges.sepan.argmusicplayer.Models.ArgAudio;
import com.arges.sepan.argmusicplayer.Models.ArgAudioList;


public class ArgPlayerView extends ArgPlayerViewRoot {
    @Override
    protected void setEmbeddedImageBitmap(byte[] byteArray) {
    }

    @Override
    protected void onAudioNameChanged(ArgAudio audio) {

    }

    @Override
    protected void onPlaylistAudioChanged(ArgAudioList list) {

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
