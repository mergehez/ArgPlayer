package com.arges.sepan.argmusicplayer.Callbacks;

import android.app.Notification;
import android.app.NotificationChannel;

public interface OnBuildNotificationListener {
    void onBuildNotification(Notification.Builder builder);
    void onBuildNotificationChannel(NotificationChannel notificationChannel);
}
