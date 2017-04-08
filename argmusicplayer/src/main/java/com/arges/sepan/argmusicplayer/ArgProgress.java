package com.arges.sepan.argmusicplayer;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class ArgProgress extends RelativeLayout {
    private String message = "";
    private int maxTime = -1;
    private Context context;
    private ImageView ivHour, ivMinute;
    private TextView tvMessage;
    private Animation rotationHour, rotationMinute;
    public ArgProgress(Context context) {
        super(context);
        init(context);
    }

    public ArgProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ArgProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        inflate(getContext(), R.layout.progresspanel, this);
        this.tvMessage = (TextView)findViewById(R.id.tvProgressMessage);
        this.ivHour = (ImageView)findViewById(R.id.imgViewProgressHour);
        this.ivMinute = (ImageView)findViewById(R.id.imgViewProgressMinute);
        this.context = context;
    }

    private boolean isAnimationsInitialized(){
        return rotationHour != null && rotationMinute != null;
    }
    private void initAnimations(){
        rotationMinute = AnimationUtils.loadAnimation(context, R.anim.btn_rotate_back);
        rotationMinute.setRepeatCount(Animation.INFINITE);
        rotationHour = AnimationUtils.loadAnimation(context, R.anim.rotate_ibre_saet);
        rotationHour.setRepeatCount(Animation.INFINITE);
    }

    public void start(){
        if(isAnimationsInitialized()){
            this.setVisibility(VISIBLE);
            ivHour.startAnimation(rotationHour);
            ivMinute.startAnimation(rotationMinute);
            tvMessage.setText(message);

            if(maxTime != -1){
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        stop();
                    }
                },maxTime);
            }
        }else{
            initAnimations();
            start();
        }
    }
    public void stop(){
        ivHour.clearAnimation();
        ivMinute.clearAnimation();
        this.setVisibility(INVISIBLE);
    }
    public void setMessage(String message){
        this.message = message;
    }
    public void setMaxTime(int maxTime){
        this.maxTime = maxTime;
    }

    @Override
    public void setBackgroundColor(int color){
        super.setBackgroundColor(color);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

}
