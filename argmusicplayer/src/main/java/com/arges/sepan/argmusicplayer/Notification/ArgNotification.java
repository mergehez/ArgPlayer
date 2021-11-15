package com.arges.sepan.argmusicplayer.Notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;

import com.arges.sepan.argmusicplayer.Models.ArgNotificationOptions;
import com.arges.sepan.argmusicplayer.R;

public class ArgNotification extends Notification {
    private static int notificationId;
    protected final Notification notification;
    private final ArgRemoteView contentView, bigContentView;
    private final NotificationManager mNotificationManager;
    protected ArgNotificationOptions options;

    public ArgNotification(Context context, @NonNull ArgNotificationOptions options) {
        super();

        this.options = options;
        notificationId = options.getNotificationId();
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //region create builder with min attributes
        Builder builder = new Builder(context)
                .setWhen(System.currentTimeMillis())
                .setTicker("ArgPlayer")
                .setOnlyAlertOnce(true)
                .setSmallIcon(options.getImageResoureId());
        //endregion

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //region create notification channel
            NotificationChannel channel = new NotificationChannel("ArgPlayer-" + context.getPackageName(), "ArgPlayer", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Notification from ArgPlayer");
            channel.enableLights(false);
            channel.enableVibration(false);

            if (options.getListener() != null)
                options.getListener().onBuildNotificationChannel(channel);

            mNotificationManager.createNotificationChannel(channel);
            builder.setChannelId(channel.getId());
            //endregion
        }

        //region create homeIntent
        Intent homeIntent = null;
        if (options.getActivity() != null) {
            homeIntent = new Intent(context, options.getActivity().getClass()).setAction("com.arges.intent.HOME");
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        }
        //endregion

        //region initialize notification's contentViews and build notification
        contentView = initializeContentView(context, homeIntent);
        bigContentView = initializeBigContentView(context, homeIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(contentView);
            if (bigContentView != null)
                builder.setCustomBigContentView(bigContentView);
        }
        if (options.getListener() != null)
            options.getListener().onBuildNotification(builder);

        notification = builder.build();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            notification.contentView = contentView;
            if (bigContentView != null)
                notification.bigContentView = bigContentView;
        }
        //endregion

        notification.flags |= Notification.FLAG_NO_CLEAR;   //FLAG_ONGOING_EVENT
    }

    public static void close(Context context) {
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(ArgNotification.notificationId);
    }

    public void show() {
        mNotificationManager.notify(options.getNotificationId(), notification);
    }

    public void close() {
        if (contentView != null)
            mNotificationManager.cancel(options.getNotificationId());
    }

    public void startIsLoading(boolean hasNext, boolean hasPrev) {
        renew("Audio is loading...", 1, hasNext, hasPrev);
    }

    public boolean isEnabled() {
        return options.isEnabled();
    }

    public void setEnabled(boolean enabled) {
        options.setEnabled(enabled);
        if (!enabled && isActive())
            close();
    }

    public boolean isActive() {
        return contentView != null;
    }

    private ArgRemoteView initializeContentView(Context context, Intent homeIntent) {
        ArgRemoteView contentView = new ArgRemoteView(context.getPackageName(), R.layout.notification_layout);

        contentView.setImageViewResource(R.id.iViewNotif, options.getImageResoureId());
        contentView.setImageResource(R.id.btnPlayPauseNotif, R.drawable.arg_pause);

        contentView.setButtonClickIntent(context, homeIntent, R.id.iViewNotif, 1);
        contentView.setButtonClickIntent(context, "PLAYPAUSE", R.id.btnPlayPauseNotif, 2);
        contentView.setButtonClickIntent(context, "NEXT", R.id.btnNextNotif, 4);
        contentView.setButtonClickIntent(context, "CLOSE", R.id.btnCloseNotif, 7);


        if (options.isProgressEnabled()) {
            contentView.setText(R.id.tvTimeNowNotif, "00:00");
        } else {
            contentView.setVisibility(R.id.relLayProgressNotif, false);
        }
        return contentView;
    }

    private ArgRemoteView initializeBigContentView(Context context, Intent homeIntent) {
        if (!options.isProgressEnabled())
            return null;

        ArgRemoteView bigContentView = new ArgRemoteView(context.getPackageName(), R.layout.notification_big_layout);

        bigContentView.setImageViewResource(R.id.iViewBigNotif, options.getImageResoureId());
        bigContentView.setImageResource(R.id.btnPlayPauseBigNotif, R.drawable.arg_pause);

        bigContentView.setButtonClickIntent(context, homeIntent, R.id.iViewBigNotif, 9);
        bigContentView.setButtonClickIntent(context, "PREV", R.id.btnPrevBigNotif, 3);
        bigContentView.setButtonClickIntent(context, "PLAYPAUSE", R.id.btnPlayPauseBigNotif, 5);
        bigContentView.setButtonClickIntent(context, "NEXT", R.id.btnNextBigNotif, 6);
        bigContentView.setButtonClickIntent(context, "CLOSE", R.id.btnCloseBigNotif, 8);

        return bigContentView;
    }


    public void setOnTimeChange(int currentTime, int max) {
        if (!options.isEnabled() || !options.isProgressEnabled())
            return;

        contentView.setProgressBar(R.id.seekBarNotif, max, currentTime, false);
        contentView.setTimeText(R.id.tvTimeNowNotif, currentTime);
        bigContentView.setProgressBar(R.id.seekBarBigNotif, max, currentTime, false);
        bigContentView.setTimeText(R.id.tvTimeNowBigNotif, currentTime);
        show();
    }

    public void renew(String name, int duration, boolean hasNext, boolean hasPrev) {
        if (options.isProgressEnabled()) {
            contentView.setTimeText(R.id.tvTimeTotalNotif, duration);
            contentView.setText(R.id.tvTimeNowNotif, "00:00");
            bigContentView.setTimeText(R.id.tvTimeTotalBigNotif, duration);
            bigContentView.setText(R.id.tvTimeNowBigNotif, "00:00");
            bigContentView.setText(R.id.tvAudioNameBigNotif, name);
            bigContentView.setImageResource(R.id.btnPlayPauseBigNotif, R.drawable.arg_pause);
            bigContentView.setVisibility(R.id.btnNextBigNotif, hasNext);
            bigContentView.setVisibility(R.id.btnPrevBigNotif, hasPrev);
        }

        contentView.setVisibility(R.id.btnNextNotif, hasNext);
        contentView.setText(R.id.tvAudioNameNotif, name);

        contentView.setImageResource(R.id.btnPlayPauseNotif, R.drawable.arg_pause);
        show();
    }

    public void switchPlayPause(boolean playing) {
        int imgRes = playing ? R.drawable.arg_pause : R.drawable.arg_play;
        contentView.setImageResource(R.id.btnPlayPauseNotif, imgRes);

        if (options.isProgressEnabled())
            bigContentView.setImageResource(R.id.btnPlayPauseBigNotif, imgRes);

        show();
    }
}
