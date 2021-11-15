package com.arges.sepan.argmusicplayer;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.arges.sepan.argmusicplayer.Models.ArgAudio;


public abstract class ArgPlayerLargeViewRoot extends ArgPlayerSmallViewRoot {
    protected AppCompatImageView imageView;
    protected AppCompatTextView tvAudioName;

    public ArgPlayerLargeViewRoot(Context context) {
        super(context);
    }

    public ArgPlayerLargeViewRoot(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArgPlayerLargeViewRoot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, int layoutResId) {
        super.init(context, layoutResId);
        imageView = findViewById(R.id.imageViewAudio);
        tvAudioName = findViewById(R.id.tvAudioName);
    }

    @Override
    protected void setEmbeddedImageBitmap(byte[] byteArray) {
        if(byteArray!=null)
            imageView.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
        else
            imageView.setImageResource(R.drawable.mergesoft);
    }

    @Override
    protected void onAudioNameChanged(ArgAudio audio) {
        tvAudioName.setText(audio.getTitle());
    }

}
