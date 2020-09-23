package com.arges.sepan.argmusicplayer.IndependentClasses;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.PowerManager;
import android.view.View;
import android.view.ViewGroup;

import com.arges.sepan.argmusicplayer.Enums.ErrorType;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class Arg {
    public static void disableContent(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                Arg.disableContent((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }
    public static void enableContent(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                Arg.enableContent((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }
    }
    public static String convertTimeToMinSecString(long time){
        return new SimpleDateFormat("mm:ss", Locale.getDefault()).format(time);
    }
    public static void hideNotifPanel(Context context){
        try
        {
            Object service  = context.getSystemService("statusbar");
            Class<?> statusbarManager = Class.forName("android.app.StatusBarManager");
            if (Build.VERSION.SDK_INT <= 16) {
                Method collapse = statusbarManager.getMethod("collapse");
                collapse.setAccessible(true);
                collapse.invoke(service);
            } else {
                Method collapse2 = statusbarManager.getMethod("collapsePanels");
                collapse2.setAccessible(true);
                collapse2.invoke(service);
            }
        } catch (Exception ignored) {
        }
    }

    public static void unlockScreen(Context context) {
        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        assert km != null;
        final KeyguardManager.KeyguardLock kl = km.newKeyguardLock("MyKeyguardLock");
        kl.disableKeyguard();

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        assert pm != null;
        @SuppressLint("InvalidWakeLockTag")
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                | PowerManager.ACQUIRE_CAUSES_WAKEUP
                | PowerManager.ON_AFTER_RELEASE, "MyWakeLock");
        wakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
    }
    public static Bitmap byteArrayToBitmap(byte[] arr){
        return BitmapFactory.decodeByteArray(arr,0,arr.length);
    }
    public static class CmdVar {
        static public final String CMD_NAME = "command";
        static public final String CMD_PAUSE = "pause";
        static public final String CMD_STOP = "pause";
        static public final String CMD_PLAY = "play";

        // Jellybean
        static public String SERVICE_CMD = "com.sec.android.app.music.musicservicecommand";
        static public String PAUSE_SERVICE_CMD = "com.sec.android.app.music.musicservicecommand.pause";
        static public String PLAY_SERVICE_CMD = "com.sec.android.app.music.musicservicecommand.play";

        // Honeycomb
        static {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                SERVICE_CMD = "com.android.music.musicservicecommand";
                PAUSE_SERVICE_CMD = "com.android.music.musicservicecommand.pause";
                PLAY_SERVICE_CMD = "com.android.music.musicservicecommand.play";
            }
        }
    }
    public static class Convertions {
        private Context context;
        private float scale;
        public Convertions(Context context){
            this.context = context;
            scale = context.getResources().getDisplayMetrics().density;
        }
        public int dpToPx(int dp){
            return (int)(dp*scale+0.5f);
        }
        public int pxToDp(int px){
            return (int)((px-0.5f)/scale);
        }
        static public int dpToPx(int dp, Context context){
            float scale = context.getResources().getDisplayMetrics().density;
            return (int)(dp*scale+0.5f);
        }
    }

    //Interfaces
    public interface OnPreparedListener{
        void onPrepared(ArgAudio audio, int duration);
    }
    public interface OnPausedListener{
        void onPaused();
    }
    public interface OnTimeChangeListener{
        void onTimeChanged(long currentTime);
    }
    public interface OnCompletedListener{
        void onCompleted();
    }
    public interface OnErrorListener{
        void onError(ErrorType errorType, String description);
    }
    public interface OnPlayingListener{
        void onPlaying();
    }
    public interface OnPlaylistAudioChangedListener{
        void onPlaylistAudioChanged(ArgAudioList playlist, int currentAudioIndex);
    }

}
