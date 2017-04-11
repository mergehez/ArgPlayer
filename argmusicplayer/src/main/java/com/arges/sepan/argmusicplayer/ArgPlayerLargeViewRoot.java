package com.arges.sepan.argmusicplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.arges.sepan.argmusicplayer.IndependentClasses.Arg;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudio;


public class ArgPlayerLargeViewRoot extends ArgPlayerSmallViewRoot {
    protected ImageView imageView;
    protected TextView tvAudioName;

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
        super.init(context,layoutResId);
        imageView = (ImageView) findViewById(R.id.imageViewAudio);
        tvAudioName = (TextView)findViewById(R.id.tvAudioName);
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
