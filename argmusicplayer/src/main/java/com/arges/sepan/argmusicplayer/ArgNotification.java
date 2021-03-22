package com.arges.sepan.argmusicplayer;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.arges.sepan.argmusicplayer.IndependentClasses.Arg;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

@SuppressLint("ParcelCreator")
public class ArgNotification extends Notification {
    private static RemoteViews contentView, bigContentView;
    private static NotificationManager mNotificationManager;
    private static Notification notification;
    protected static int notifId = 548853;
    private static boolean notif = false;
    private static boolean progress = false;
    protected static Class mainActivityClass;
    protected static int notifImgResId = R.drawable.mergesoftlogo;

    @SuppressLint("NewApi")
    public ArgNotification(Context context, String audio, int duration){
        super();
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Builder(context).setWhen(System.currentTimeMillis()).setTicker("ArgPlayer").setSmallIcon(notifImgResId).build();

        contentView=new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        Intent homeIntent = null;
        if(mainActivityClass != null){
            homeIntent = new Intent(context, mainActivityClass).setAction("com.arges.intent.HOME");
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            contentView.setOnClickPendingIntent(R.id.iViewNotif, PendingIntent.getActivity(context, 1, homeIntent, PendingIntent.FLAG_CANCEL_CURRENT));
        }

        contentView.setImageViewResource(R.id.iViewNotif,notifImgResId);
        Intent playIntent = new Intent(context,ArgNotificationReceiver.class).setAction("com.arges.intent.PLAYPAUSE");
        contentView.setOnClickPendingIntent(R.id.btnPlayPauseNotif, PendingIntent.getBroadcast(context,2,playIntent,PendingIntent.FLAG_CANCEL_CURRENT));
        Intent nextIntent = new Intent(context,ArgNotificationReceiver.class).setAction("com.arges.intent.NEXT");
        contentView.setOnClickPendingIntent(R.id.btnNextNotif, PendingIntent.getBroadcast(context,4,nextIntent,PendingIntent.FLAG_CANCEL_CURRENT));
        Intent closeIntent = new Intent(context,ArgNotificationReceiver.class).setAction("com.arges.intent.CLOSE");
        contentView.setOnClickPendingIntent(R.id.btnCloseNotif, PendingIntent.getBroadcast(context,7,closeIntent,PendingIntent.FLAG_CANCEL_CURRENT));
        contentView.setInt(R.id.btnPlayPauseNotif, "setImageResource", R.drawable.arg_notif_pause);
        contentView.setTextViewText(R.id.tvAudioNameNotif, audio);

        if(progress){
            contentView.setTextViewText(R.id.tvTimeTotalNotif, Arg.convertTimeToMinSecString(duration));
            contentView.setTextViewText(R.id.tvTimeNowNotif, "00:00");
            bigContentView=new RemoteViews(context.getPackageName(), R.layout.notification_big_layout);

            bigContentView.setImageViewResource(R.id.iViewBigNotif,notifImgResId);
            if(homeIntent != null)
                bigContentView.setOnClickPendingIntent(R.id.iViewBigNotif, PendingIntent.getActivity(context, 9, homeIntent, PendingIntent.FLAG_CANCEL_CURRENT));
            Intent prevBigIntent = new Intent(context,ArgNotificationReceiver.class).setAction("com.arges.intent.PREV");
            bigContentView.setOnClickPendingIntent(R.id.btnPrevBigNotif, PendingIntent.getBroadcast(context, 3, prevBigIntent, PendingIntent.FLAG_CANCEL_CURRENT));
            bigContentView.setOnClickPendingIntent(R.id.btnPlayPauseBigNotif, PendingIntent.getBroadcast(context,5,playIntent,PendingIntent.FLAG_CANCEL_CURRENT));
            bigContentView.setOnClickPendingIntent(R.id.btnNextBigNotif, PendingIntent.getBroadcast(context,6,nextIntent,PendingIntent.FLAG_CANCEL_CURRENT));
            bigContentView.setOnClickPendingIntent(R.id.btnCloseBigNotif, PendingIntent.getBroadcast(context,8,closeIntent,PendingIntent.FLAG_CANCEL_CURRENT));
            bigContentView.setInt(R.id.btnPlayPauseBigNotif, "setImageResource", R.drawable.arg_notif_pause);
            bigContentView.setTextViewText(R.id.tvAudioNameBigNotif, audio);
            notification.bigContentView = bigContentView;
        }else{
            contentView.setInt(R.id.relLayProgressNotif,"setVisibility", GONE);
        }

        notification.contentView = contentView;
        notification.flags |= Notification.FLAG_NO_CLEAR;   //FLAG_ONGOING_EVENT
    }
    public static void show(){
        mNotificationManager.notify(notifId, notification);
    }
    public static void renew(String name, int duration, boolean hasNext, boolean hasPrev){
        if(progress){
            contentView.setTextViewText(R.id.tvTimeTotalNotif, Arg.convertTimeToMinSecString(duration));
            contentView.setTextViewText(R.id.tvTimeNowNotif, "00:00");
            bigContentView.setTextViewText(R.id.tvTimeTotalBigNotif, Arg.convertTimeToMinSecString(duration));
            bigContentView.setTextViewText(R.id.tvTimeNowBigNotif, "00:00");
            bigContentView.setTextViewText(R.id.tvAudioNameBigNotif, name);
            bigContentView.setInt(R.id.btnPlayPauseBigNotif, "setImageResource", R.drawable.arg_notif_pause);
            if(hasNext) bigContentView.setInt(R.id.btnNextBigNotif,"setVisibility", VISIBLE);
            else        bigContentView.setInt(R.id.btnNextBigNotif,"setVisibility", GONE);
            if(hasPrev) bigContentView.setInt(R.id.btnPrevBigNotif, "setVisibility", VISIBLE);
            else        bigContentView.setInt(R.id.btnPrevBigNotif,"setVisibility", GONE);
        }
        if(!hasNext) contentView.setInt(R.id.btnNextNotif,"setVisibility", GONE);
        else         contentView.setInt(R.id.btnNextNotif,"setVisibility", VISIBLE);
        contentView.setTextViewText(R.id.tvAudioNameNotif, name);
        contentView.setInt(R.id.btnPlayPauseNotif, "setImageResource", R.drawable.arg_notif_pause);
        show();
    }
    public static void close(){
        if(contentView!=null)
            mNotificationManager.cancel(notifId);
    }
    public static void close(Context context){
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).cancel(ArgNotification.notifId);
    }
    public static void startIsLoading(boolean hasNext, boolean hasPrev){
        renew("Audio is loading...",1,hasNext,hasPrev);
    }
    public static void setOnTimeChange(int currentTime, int max){
        if(!notif || !progress) return;
        contentView.setProgressBar(R.id.seekBarNotif,max,currentTime,false);
        contentView.setTextViewText(R.id.tvTimeNowNotif, Arg.convertTimeToMinSecString(currentTime));
        bigContentView.setProgressBar(R.id.seekBarBigNotif,max,currentTime,false);
        bigContentView.setTextViewText(R.id.tvTimeNowBigNotif, Arg.convertTimeToMinSecString(currentTime));
        show();
    }
    public static boolean getNotif(){return notif;}
    public static void setNotif(boolean notif){  ArgNotification.notif = notif; if(!notif && isActive()) close();}
    public static boolean isActive(){return contentView != null;}
    public static void switchPlayPause(boolean playing){
        if(playing)contentView.setInt(R.id.btnPlayPauseNotif, "setImageResource", R.drawable.arg_notif_pause);
        else contentView.setInt(R.id.btnPlayPauseNotif, "setImageResource", R.drawable.arg_notif_play);
        if(progress)
            if(playing)bigContentView.setInt(R.id.btnPlayPauseBigNotif, "setImageResource", R.drawable.arg_notif_pause);
            else bigContentView.setInt(R.id.btnPlayPauseBigNotif, "setImageResource", R.drawable.arg_notif_play);
        show();
    }
}
