package com.arges.sepan.argmusicplayer;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.arges.sepan.argmusicplayer.IndependentClasses.Arg;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudio;


public class ArgPlayerLargeViewRoot extends ArgPlayerSmallViewRoot {
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
        imageView = (AppCompatImageView) findViewById(R.id.imageViewAudio);
        tvAudioName = (AppCompatTextView) findViewById(R.id.tvAudioName);
    }

    @Override
    void setEmbeddedImageBitmap(byte[] byteArray) {
        if(byteArray!=null)
            imageView.setImageBitmap(Arg.byteArrayToBitmap(byteArray));
        else
            imageView.setImageResource(R.drawable.mergesoft);
    }

    @Override
    void onAudioNameChanged(ArgAudio audio) {
        tvAudioName.setText(audio.getTitle());
    }

}
