package com.arges.sepan.argmusicplayer.Models;

import static android.os.Build.VERSION_CODES.O;

import android.app.Activity;

import androidx.annotation.RequiresApi;

import com.arges.sepan.argmusicplayer.Callbacks.OnBuildNotificationListener;
import com.arges.sepan.argmusicplayer.R;

public class ArgNotificationOptions {

    private boolean enabled = true;
    private boolean progressEnabled = false;
    private String channelId;
    private CharSequence channelName;
    private int notificationId = 548853;
    private int imageResoureId = R.drawable.mergesoftlogo;
    private OnBuildNotificationListener listener = null;
    private final Activity activity;

    public ArgNotificationOptions(Activity activity){
        this.activity = activity;
    }

    //region getter setters
    public OnBuildNotificationListener getListener() {
        return listener;
    }

    public ArgNotificationOptions setListener(OnBuildNotificationListener listener) {
        this.listener = listener;
        return this;
    }

    public boolean isProgressEnabled() {
        return progressEnabled;
    }

    public ArgNotificationOptions setProgressEnabled(boolean progressEnabled) {
        this.progressEnabled = progressEnabled;
        return this;
    }

    @RequiresApi(O)
    public String getChannelId() {
        return channelId;
    }
    @RequiresApi(O)
    public ArgNotificationOptions setChannelId(String channelId) {
        this.channelId = channelId;
        return this;
    }

    @RequiresApi(O)
    public CharSequence getChannelName() {
        return channelName;
    }

    @RequiresApi(O)
    public ArgNotificationOptions setChannelName(CharSequence channelName) {
        this.channelName = channelName;
        return this;
    }

    public int getImageResoureId() {
        return imageResoureId;
    }

    public ArgNotificationOptions setImageResoureId(int imageResoureId) {
        this.imageResoureId = imageResoureId;
        return this;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public ArgNotificationOptions setNotificationId(int notificationId) {
        this.notificationId = notificationId;
        return this;
    }

    public Activity getActivity() {
        return activity;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public ArgNotificationOptions setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }
    //endregion getter setters
}
