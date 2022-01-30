package com.arges.sepan.argmusicplayer;

import static com.arges.sepan.argmusicplayer.Enums.AudioState.PAUSED;
import static com.arges.sepan.argmusicplayer.Enums.AudioState.PLAYING;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.arges.sepan.argmusicplayer.Callbacks.OnCompletedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnErrorListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPausedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPlayingListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPlaylistAudioChangedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPreparedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnTimeChangeListener;
import com.arges.sepan.argmusicplayer.Enums.ErrorType;
import com.arges.sepan.argmusicplayer.Models.ArgAudio;
import com.arges.sepan.argmusicplayer.Models.ArgAudioList;
import com.arges.sepan.argmusicplayer.Models.ArgNotificationOptions;
import com.arges.sepan.argmusicplayer.Notification.ArgNotification;

import java.text.SimpleDateFormat;
import java.util.Locale;

public abstract class ArgMusicPlayer {
    ArgMusicService service;
    ArgNotification notification;
    private OnPreparedListener onPreparedListener;
    private OnTimeChangeListener onTimeChangeListener;
    private OnPausedListener onPausedListener;
    private OnCompletedListener onCompletedListener;
    private OnErrorListener onErrorListener;
    private OnPlayingListener onPlayingListener;
    private OnPlaylistAudioChangedListener onPlaylistAudioChangedListener;
    public static ArgMusicPlayer instance;

    //region Abstract Methods
    protected abstract void changeAudioName(ArgAudio audio);

    protected abstract void changeNextPrevButtons();

    protected abstract void changeRepeatButton();

    protected abstract void hideErrorView();

    protected abstract boolean getNextPrevButtonsState();

    protected abstract void onPlaylistAudioChanged(ArgAudioList audioList);

    protected abstract void setPlayPauseAsPlay();

    protected abstract void setPlayPauseAsPause();

    protected abstract void setSeekBarMax(int max);

    protected abstract void setSeekBarProgresss(int progress);

    protected abstract void setTimeTotalText(String text);

    protected abstract void setTimeNowText(String text);

    protected abstract void showErrorView();

    protected abstract void startProgress();

    protected abstract void stopProgress();

    protected abstract void setEmbeddedImageBitmap(byte[] byteArray);
    //endregion Abstract Methods

    public static ArgMusicPlayer getInstance() {
        return instance;
    }

    ArgMusicPlayer(Context context) {
        this.service = new ArgMusicService(context);
        service.setOnPreparedListener((audio, duration) -> {
            setSeekBarMax(duration);
            hideErrorView();

            setTimeTotalText(new SimpleDateFormat("mm:ss", Locale.getDefault()).format(duration));
            if (isNotificationEnabled()) {
                boolean hasNext = service.getCurrentPlaylist().hasNext();
                boolean hasPrev = service.getCurrentPlaylist().hasPrev();
                notification.renew(audio.getTitle(), duration, hasNext, hasPrev);
            }
            changeAudioName(audio);
            if (service.isPlaylist())
                onPlaylistAudioChanged(service.getCurrentPlaylist());

            changeNextPrevButtons();
            ArgMusicPlayer.this.onPrepared(audio, duration);
        });
        service.setOnPausedListener(() -> {
            setPlayPauseAsPlay();
            if (isNotificationEnabled()) notification.switchPlayPause(false);
            ArgMusicPlayer.this.onPaused();
        });

        service.setOnTimeChangeListener(currentTime -> {
            setSeekBarProgresss((int) currentTime);
            if (isNotificationEnabled())
                notification.setOnTimeChange((int) currentTime, (int) getDuration());
            setTimeNowText(new SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTime));
            ArgMusicPlayer.this.onTimeChanged(currentTime);
        });
        service.setOnCompletedListener(() -> {
            setPlayPauseAsPlay();
            if (isNotificationEnabled()) notification.close();
            ArgMusicPlayer.this.onCompleted();
        });
        service.setOnErrorListener((errorType, description) -> {
            setPlayPauseAsPlay();
            stopProgress();
            showErrorView();
            if (isNotificationEnabled()) notification.close();
            ArgMusicPlayer.this.onError(errorType, description);
        });
        service.setOnPlayingListener(() -> {
            stopProgress();
            setPlayPauseAsPause();
            if (isNotificationEnabled()) notification.switchPlayPause(true);
            ArgMusicPlayer.this.onPlaying();
        });
        service.setOnPlaylistAudioChangedListener((playlist, currentAudioIndex) -> {
            if (!service.isCurrentAudio(playlist.getCurrentAudio())) {
                startProgress();
                if (isNotificationEnabled())
                    notification.startIsLoading(playlist.hasNext(), playlist.hasPrev());
            }
            service.playAudio(playlist.getCurrentAudio());
            ArgMusicPlayer.this.onPlaylistAudioChanged(playlist, currentAudioIndex);
        });
        service.setOnPlaylistStateChangedListener((isPlaylist, playlist) -> {
            if (getNextPrevButtonsState()) return;
            changeNextPrevButtons();
        });
        service.setOnEmbeddeImageReadyListener(this::setEmbeddedImageBitmap);

        instance = this;
    }

    protected void enableNotification(@NonNull ArgNotificationOptions options) {
        notification = new ArgNotification(options.getActivity(), options);
    }
    protected void disableNotification() {//TODO clear notification service and set to null
        if(notification != null)
            notification.setEnabled(false);
    }
    private boolean isNotificationEnabled(){
        return notification != null && notification.isEnabled();
    }

    protected void checkIfAppReOpened() {
        if (service.audioState == PLAYING) {
            setPlayPauseAsPause();
            service.updateTimeThread();
            setSeekBarMax((int) getDuration());
        }
    }

    protected void continuePlaylistOnError() {
        service.setPlaylistError(false);
    }

    protected void stopPlaylistOnError() {
        service.setPlaylistError(true);
    }

    protected void onClick(View v) {
        if (v != null) {
            int i = v.getId();
            if (i == R.id.btnPlayPause) {
                switch (service.getAudioState()) {
                    case NO_ACTION:
                        play(getCurrentAudio());
                        break;
                    case PAUSED:
                        service.continuePlaying();
                        break;
                    case STOPPED:
                        service.replayAudio(getCurrentAudio());
                        break;
                    case PLAYING:
                        pause();
                        break;
                }
            } else if (i == R.id.btnRepeat) {
                setPlaylistRepeat(!service.getRepeatPlaylist());
                changeRepeatButton();
            } else if (i == R.id.btnNext) {
                playNextAudio();
            } else if (i == R.id.btnPrev) {
                playPreviousAudio();
            } else if (i == R.id.arg_music_error_view) {
                stopProgress();
                hideErrorView();
            }
        }
    }

    protected void setPlaylistRepeat(boolean repeat) {
        service.setRepeatPlaylist(repeat);
        changeRepeatButton();
    }

    protected boolean getPlaylistRepeat() {
        return service.getRepeatPlaylist();
    }

    protected void loadSingleAudio(@NonNull ArgAudio audio) {
        service.setCurrentAudio(audio);
    }

    protected void loadPlaylist(@NonNull ArgAudioList playlist) {
        service.setCurrentPlaylist(playlist);
    }

    protected void playLoadedPlaylist() {
        service.preparePlaylistToPlay(service.getCurrentPlaylist());
    }

    protected void playPlaylist(@NonNull ArgAudioList argAudioList) {
        if (service.preparePlaylistToPlay(argAudioList)) {
            hideErrorView();
            ArgAudioList list = service.getCurrentPlaylist();
            if (!service.isCurrentAudio(list.getCurrentAudio())) startProgress();
            service.playAudio(list.getCurrentAudio());
        }
    }

    protected void playPlaylistItem(int index) {
        hideErrorView();
        service.playPlaylistItem(index);
    }

    protected void play(ArgAudio audio) {
        hideErrorView();
        if (!service.isCurrentAudio(audio)) startProgress();
        service.playSingleAudio(audio);
    }

    public void continuePlaying() {
        service.continuePlaying();
    }

    public void playNextAudio() {
        service.playNextAudio();
    }

    public void playPreviousAudio() {
        service.playPrevAudio();
    }

    public void pause() {
        service.pause();
    }

    public void stop() {
        service.stop();
        setPlayPauseAsPlay();
        setSeekBarMax(0);
        setSeekBarProgresss(0);
        setTimeNowText("00:00");
    }

    protected boolean seekTo(int progress) {
        return service.seekTo(progress);
    }

    protected boolean forward(int milliSec, boolean willPlay) {
        return service.forward(milliSec, willPlay);
    }

    protected boolean backward(int milliSec, boolean willPlay) {
        return service.backward(milliSec, willPlay);
    }

    protected void playAudioAfterPercent(int percent) {
        service.playAudioAfterPercent(percent);
    }

    public boolean isPlaying() {
        return service.getAudioState() == PLAYING;
    }

    protected boolean isPaused() {
        return service.getAudioState() == PAUSED;
    }

    protected long getDuration() {
        return service.getDuration();
    }

    protected long getCurrentPosition() {
        return service.getCurrentPosition();
    }

    protected ArgAudio getCurrentAudio() {
        return service.getCurrentAudio();
    }

    protected boolean hasNextAudio() {
        return service.getCurrentPlaylist().hasNext();
    }

    protected boolean hasPrevAudio() {
        return service.getCurrentPlaylist().hasPrev();
    }

    // setting listeners
    protected void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }

    protected void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
        this.onTimeChangeListener = onTimeChangeListener;
    }

    protected void setOnPausedListener(OnPausedListener onPausedListener) {
        this.onPausedListener = onPausedListener;
    }

    protected void setOnCompletedListener(OnCompletedListener onCompletedListener) {
        this.onCompletedListener = onCompletedListener;
    }

    protected void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    protected void setOnPlayingListener(OnPlayingListener onPlayingListener) {
        this.onPlayingListener = onPlayingListener;
    }

    protected void setOnPlaylistAudioChangedListener(OnPlaylistAudioChangedListener onPlaylistAudioChangedListener) {
        this.onPlaylistAudioChangedListener = onPlaylistAudioChangedListener;
    }

    // listener publishing
    private void onPrepared(ArgAudio audio, int duration) {
        if (onPreparedListener != null) onPreparedListener.onPrepared(audio, duration);
    }

    private void onTimeChanged(long currentTime) {
        if (onTimeChangeListener != null) onTimeChangeListener.onTimeChanged(currentTime);
    }

    private void onPaused() {
        if (onPausedListener != null) onPausedListener.onPaused();
    }

    private void onCompleted() {
        if (onCompletedListener != null) onCompletedListener.onCompleted();
    }

    private void onError(ErrorType errorType, String description) {
        if (onErrorListener != null) onErrorListener.onError(errorType, description);
    }

    private void onPlaying() {
        if (onPlayingListener != null) onPlayingListener.onPlaying();
    }

    private void onPlaylistAudioChanged(ArgAudioList playlist, int currentAudioIndex) {
        if (onPlaylistAudioChangedListener != null)
            onPlaylistAudioChangedListener.onPlaylistAudioChanged(playlist, currentAudioIndex);
    }
}
