package com.arges.sepan.argmusicplayer.Notification;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.IdRes;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ArgRemoteView extends RemoteViews {
    public ArgRemoteView(String packageName, int layoutId) {
        super(packageName, layoutId);
    }


    protected void setButtonClickIntent(Context context, String action, int btnViewId, int requestCode) {
        Intent intent = new Intent(context, ArgNotificationReceiver.class).setAction("com.arges.intent." + action);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setOnClickPendingIntent(btnViewId, PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT));
        } else {
            setOnClickPendingIntent(btnViewId, PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT));
        }
    }

    protected void setButtonClickIntent(Context context, Intent homeIntent, int btnViewId, int requestCode) {
        if (homeIntent != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setOnClickPendingIntent(btnViewId, PendingIntent.getActivity(context, requestCode, homeIntent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_CANCEL_CURRENT));
            } else {
                setOnClickPendingIntent(btnViewId, PendingIntent.getActivity(context, requestCode, homeIntent, PendingIntent.FLAG_CANCEL_CURRENT));
            }
        }
    }

    protected void setImageResource(@IdRes int btnViewId, int drawableId) {
        setInt(btnViewId, "setImageResource", drawableId);
    }

    protected void setVisibility(@IdRes int btnViewId, boolean visible) {
        setInt(btnViewId, "setVisibility", visible ? VISIBLE : GONE);
    }

    protected void setText(@IdRes int btnViewId, String text) {
        setTextViewText(btnViewId, text);
    }

    protected void setTimeText(@IdRes int btnViewId, int currentTime) {
        setTextViewText(btnViewId, new SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTime));
    }
}
