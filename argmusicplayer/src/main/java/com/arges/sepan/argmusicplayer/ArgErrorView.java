package com.arges.sepan.argmusicplayer;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by arges on 4/5/2017.
 */

public class ArgErrorView extends RelativeLayout {
    public ArgErrorView(Context context) {
        super(context);
        inflate(getContext(), R.layout.errorpanel, this);
        init();
    }

    public ArgErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.errorpanel, this);
        init();
    }

    public ArgErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        inflate(getContext(), R.layout.errorpanel, this);
    }
    public void show(){
        this.setVisibility(VISIBLE);
    }
    public void hide(){
        this.setVisibility(INVISIBLE);
    }
    public interface OnClickListener{
        void onClick();
    }
}
