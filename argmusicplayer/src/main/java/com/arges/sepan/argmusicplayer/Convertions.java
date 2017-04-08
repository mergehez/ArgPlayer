package com.arges.sepan.argmusicplayer;

import android.content.Context;

class Convertions {
    Context context;
    float scale;
    Convertions(Context context){
        this.context = context;
        scale = context.getResources().getDisplayMetrics().density;
    }
    int dpToPx(int dp){
        return (int)(dp*scale+0.5f);
    }
    int pxToDp(int px){
        return (int)((px-0.5f)/scale);
    }
    static int dpToPx(int dp, Context context){
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp*scale+0.5f);
    }
}