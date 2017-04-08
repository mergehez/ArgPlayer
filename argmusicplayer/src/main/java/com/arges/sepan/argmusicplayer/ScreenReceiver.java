package com.arges.sepan.argmusicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by arges on 4/4/2017.
 */

public class ScreenReceiver extends BroadcastReceiver {

    // THANKS JASON
    public static boolean wasScreenOn = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            wasScreenOn = false;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            wasScreenOn = true;
        }
    }

}
