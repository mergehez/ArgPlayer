package com.arges.sepan.argmusicplayer.Callbacks;

import com.arges.sepan.argmusicplayer.Models.ArgAudioList;

public interface OnPlaylistStateChangedListener {
    void onPlaylistStateChanged(boolean isPlaylist, ArgAudioList playlist);
}
