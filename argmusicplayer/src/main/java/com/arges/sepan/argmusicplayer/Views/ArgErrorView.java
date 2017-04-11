package com.arges.sepan.argmusicplayer.Views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.arges.sepan.argmusicplayer.R;


public class ArgErrorView extends RelativeLayout {
    public ArgErrorView(Context context) {
        super(context);
        inflate(getContext(), R.layout.error_panel_layout, this);
        init();
    }

    public ArgErrorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(getContext(), R.layout.error_panel_layout, this);
        init();
    }

    public ArgErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private void init(){
        inflate(getContext(), R.layout.error_panel_layout, this);
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
