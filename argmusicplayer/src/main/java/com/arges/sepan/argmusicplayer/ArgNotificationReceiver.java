package com.arges.sepan.argmusicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.arges.sepan.argmusicplayer.IndependentClasses.Arg;


public class ArgNotificationReceiver extends BroadcastReceiver{
    public ArgNotificationReceiver(){}
    @Override
    public void onReceive(Context context, Intent intent){
        String action = intent.getAction();
        ArgMusicPlayer player = ArgMusicPlayer.getInstance();
        Intent serviceIntent;
        switch (action){
            case "com.arges.intent.PLAYPAUSE":
                Log.d("ARGCIH", "NotifHelper onReceive action: "+action);
                if(player.isPlaying())player.pause();
                else player.continuePlaying();
                break;
            case "com.arges.intent.CLOSE":
                ArgNotification.close();
                try{
                    player.stop();
                }catch (Exception ec){Log.d("Notification Close"," Cannot stop music");}

                break;
            case "com.arges.intent.NEXT":
                Log.d("ARGCIH", "NotifHelper onReceive action: "+action);
                player.playNextAudio();
                break;
            case "com.arges.intent.PREV":
                Log.d("ARGCIH", "NotifHelper onReceive action: "+action);
                player.playPrevAudio();
                break;
            case "com.arges.intent.HOME":
                Log.d("ARGCIH", "NotifHelper onReceive action: "+action);
                Intent i = new Intent(context, ArgPlayerViewRoot.mainActivityClass);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                }
                context.startActivity(i);
                Toast.makeText(context,"Sepan vebû",Toast.LENGTH_SHORT).show();
                Arg.hideNotifPanel(context);
                Arg.unlockScreen(context);
                break;
        }
    }
}
