package com.arges.sepan.argmusicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ArgNotificationReceiver extends BroadcastReceiver{
    public ArgNotificationReceiver(){}
    @Override
    public void onReceive(Context context, Intent intent){
        String action = intent.getAction();
        ArgMusicPlayer player = ArgMusicPlayer.getInstance();
        if(player == null){
            ArgNotification.close(context);
            return;
        }
        Log.d("ARGCIH", "NotifHelper onReceive action: "+action);
        switch (action){
            case "com.arges.intent.PLAYPAUSE":
                if(player.isPlaying())player.pause();
                else player.continuePlaying();
                break;
            case "com.arges.intent.CLOSE":
                ArgNotification.close(context);
                player.stop();
                break;
            case "com.arges.intent.NEXT":
                player.playNextAudio();
                break;
            case "com.arges.intent.PREV":
                player.playPrevAudio();
                break;
        }
    }
}
