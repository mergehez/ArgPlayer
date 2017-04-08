package com.arges.sepan.argmusicplayer;

/**
 * Created by arges on 4/6/2017.
 */

class CmdVar {
    static final String CMD_NAME = "command";
    static final String CMD_PAUSE = "pause";
    static final String CMD_STOP = "pause";
    static final String CMD_PLAY = "play";
    // Jellybean
    static String SERVICE_CMD = "com.sec.android.app.music.musicservicecommand";
    static String PAUSE_SERVICE_CMD = "com.sec.android.app.music.musicservicecommand.pause";
    static String PLAY_SERVICE_CMD = "com.sec.android.app.music.musicservicecommand.play";

    // Honeycomb
    {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            SERVICE_CMD = "com.android.music.musicservicecommand";
            PAUSE_SERVICE_CMD = "com.android.music.musicservicecommand.pause";
            PLAY_SERVICE_CMD = "com.android.music.musicservicecommand.play";
        }
    }
}
