package com.arges.sepan.argmusicplayer;


/**
 * Created by arges on 4/4/2017.
 */

public class ArgListener {
    public interface OnPreparedListener{
        void onPrepared(String audioName, int duration);
    }
    public interface OnPausedListener{
        void onPaused();
    }
    public interface OnTimeChangeListener{
        void onTimeChanged(long currentTime);
    }
    public interface OnCompletedListener{
        void onCompleted();
    }
    public interface OnErrorListener{
        void onError(ArgMusicPlayer.ErrorType errorType, String description);
    }
    public interface OnPlayingListener{
        void onPlaying();
    }
    public interface OnPlaylistAudioChangedListener{
        void onPlaylistAudioChanged(ArgAudioList playlist, int currentAudioIndex);
    }
    public interface OnPlaylistStateChangedListener{
        void onPlaylistStateChanged(boolean isPlaylist, ArgAudioList playlist);
    }
}
