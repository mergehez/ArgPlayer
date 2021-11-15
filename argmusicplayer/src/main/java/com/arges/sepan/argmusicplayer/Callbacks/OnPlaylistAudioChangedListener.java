package com.arges.sepan.argmusicplayer.Callbacks;

import com.arges.sepan.argmusicplayer.Models.ArgAudioList;

public interface OnPlaylistAudioChangedListener {
    void onPlaylistAudioChanged(ArgAudioList playlist, int currentAudioIndex);
}
