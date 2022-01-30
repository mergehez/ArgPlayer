package com.arges.sepan.argmusicplayer.Callbacks;

import com.arges.sepan.argmusicplayer.Models.ArgAudio;

public interface OnPlaylistUpdateListener {
    void onUpdate(ArgAudio audio, boolean wasRemoved);
}
