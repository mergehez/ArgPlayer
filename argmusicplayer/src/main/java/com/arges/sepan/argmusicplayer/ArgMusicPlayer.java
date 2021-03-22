package com.arges.sepan.argmusicplayer;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.arges.sepan.argmusicplayer.Enums.ErrorType;
import com.arges.sepan.argmusicplayer.IndependentClasses.Arg;
import com.arges.sepan.argmusicplayer.IndependentClasses.Arg.OnCompletedListener;
import com.arges.sepan.argmusicplayer.IndependentClasses.Arg.OnErrorListener;
import com.arges.sepan.argmusicplayer.IndependentClasses.Arg.OnPausedListener;
import com.arges.sepan.argmusicplayer.IndependentClasses.Arg.OnPlayingListener;
import com.arges.sepan.argmusicplayer.IndependentClasses.Arg.OnPlaylistAudioChangedListener;
import com.arges.sepan.argmusicplayer.IndependentClasses.Arg.OnPreparedListener;
import com.arges.sepan.argmusicplayer.IndependentClasses.Arg.OnTimeChangeListener;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudio;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudioList;

import java.util.ArrayList;

import static com.arges.sepan.argmusicplayer.Enums.AudioState.PAUSED;
import static com.arges.sepan.argmusicplayer.Enums.AudioState.PLAYING;

abstract class ArgMusicPlayer{
    private String TAG = ArgMusicPlayer.this.getClass().getSimpleName();
    private ArgMusicService service;
    private Arg.Convertions cv;
    private ArrayList<ArgAudio> playlist = new ArrayList<>();
    private OnPreparedListener onPreparedListener;
    private OnTimeChangeListener onTimeChangeListener;
    private OnPausedListener onPausedListener;
    private OnCompletedListener onCompletedListener;
    private OnErrorListener onErrorListener;
    private OnPlayingListener onPlayingListener;
    private OnPlaylistAudioChangedListener onPlaylistAudioChangedListener;

    public static ArgMusicPlayer instance;
    
    //Abstract Methods
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

    public static ArgMusicPlayer getInstance(){
        return instance;
    }
    ArgMusicPlayer(Context context) {
        this.service = new ArgMusicService(context);
        this.cv = new Arg.Convertions(context);
        instance = this;
        service.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(ArgAudio audio, int duration) {
                setSeekBarMax(duration);
                setTimeTotalText(Arg.convertTimeToMinSecString(duration));
                if(ArgNotification.getNotif()) {
                    boolean hasNext = service.isPlaylist() && service.getCurrentPlaylist().hasNext();
                    boolean hasPrev = service.isPlaylist() && service.getCurrentPlaylist().hasPrev();
                    ArgNotification.renew(audio.getTitle(), duration, hasNext, hasPrev);
                }
                changeAudioName(audio);
                if(service.isPlaylist()) onPlaylistAudioChanged(service.getCurrentPlaylist());
                changeNextPrevButtons();
                ArgMusicPlayer.this.onPrepared(audio,duration);
            }
        });
        service.setOnPausedListener(new Arg.OnPausedListener() {
            @Override
            public void onPaused() {
                setPlayPauseAsPlay();
                if(ArgNotification.getNotif()) ArgNotification.switchPlayPause(false);
                ArgMusicPlayer.this.onPaused();
            }
        });
        service.setOnTimeChangeListener(new Arg.OnTimeChangeListener() {
            @Override
            public void onTimeChanged(long currentTime) {
                setSeekBarProgresss((int)currentTime);
                if(ArgNotification.getNotif()) ArgNotification.setOnTimeChange((int)currentTime, (int)getDuration());
                setTimeNowText(Arg.convertTimeToMinSecString(currentTime));
                ArgMusicPlayer.this.onTimeChanged(currentTime);
            }
        });
        service.setOnCompletedListener(new Arg.OnCompletedListener() {
            @Override
            public void onCompleted() {
                setPlayPauseAsPlay();
                if(ArgNotification.getNotif()) ArgNotification.close();
                ArgMusicPlayer.this.onCompleted();
            }
        });
        service.setOnErrorListener(new Arg.OnErrorListener() {
            @Override
            public void onError(ErrorType errorType, String description) {
                setPlayPauseAsPlay();
                stopProgress();
                showErrorView();
                if(ArgNotification.getNotif()) ArgNotification.close();
                ArgMusicPlayer.this.onError(errorType,description);
            }
        });
        service.setOnPlayingListener(new Arg.OnPlayingListener() {
            @Override
            public void onPlaying() {
                stopProgress();
                setPlayPauseAsPause();
                if(ArgNotification.getNotif()) ArgNotification.switchPlayPause(true);
                ArgMusicPlayer.this.onPlaying();
            }
        });
        service.setOnPlaylistAudioChangedListener(new OnPlaylistAudioChangedListener() {
            @Override
            public void onPlaylistAudioChanged(ArgAudioList playlist, int currentAudioIndex) {
                if(!service.isCurrentAudio(playlist.getCurrentAudio())){
                    startProgress();
                    if(ArgNotification.getNotif()) ArgNotification.startIsLoading(playlist.hasNext(),playlist.hasPrev());
                }
                service.playAudio(playlist.getCurrentAudio());
                ArgMusicPlayer.this.onPlaylistAudioChanged(playlist,currentAudioIndex);
            }
        });
        service.setOnPlaylistStateChangedListener(new ArgMusicService.OnPlaylistStateChangedListener() {
            @Override
            public void onPlaylistStateChanged(boolean isPlaylist, ArgAudioList playlist) {
                if(getNextPrevButtonsState()) return;
                changeNextPrevButtons();
            }
        });
        service.setOnEmbeddeImageReadyListener(new ArgMusicService.OnEmbeddedImageReadyListener() {
            @Override
            public void onEmbeddedImageReady(byte[] byteArray) {
                setEmbeddedImageBitmap(byteArray);
            }
        });
    }
    protected void checkIfAppReOpened(){
        if(ArgMusicService.audioState== PLAYING){
            setPlayPauseAsPause();
            service.updateTimeThread();
            setSeekBarMax((int)getDuration());
        }
    }

    protected void continuePlaylistOnError(){ service.setPlaylistError(false); }
    protected void stopPlaylistOnError()    { service.setPlaylistError(true);  }
    protected void setNotif(boolean notif)  { ArgNotification.setNotif(notif); }
    protected void onClick(View v){
        if(v!=null){
            int i = v.getId();
            if (i == R.id.btnPlayPause) {
                switch (service.getAudioState()){
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
            } else if(i == R.id.btnNext){
                playNextAudio();
            } else if(i == R.id.btnPrev){
                playPrevAudio();
            } else if(i == R.id.arg_music_error_view){
                stopProgress();
                hideErrorView();
            }
        }
    }
    protected void setPlaylistRepeat(boolean repeat){service.setRepeatPlaylist(repeat);changeRepeatButton();}
    protected boolean getPlaylistRepeat(){return service.getRepeatPlaylist();}
    protected void loadSingleAudio(@NonNull ArgAudio audio){service.setCurrentAudio(audio);}
    protected void loadPlaylist(@NonNull ArgAudioList playlist){service.setCurrentPlaylist(playlist);}
    protected void playLoadedSingleAudio(){play(getCurrentAudio());}
    protected void playLoadedPlaylist(){service.preparePlaylistToPlay(service.getCurrentPlaylist());}
    protected void playPlaylist(@NonNull ArgAudioList argAudioList){
        if(service.preparePlaylistToPlay(argAudioList)){
            hideErrorView();
            ArgAudioList list = service.getCurrentPlaylist();
            if(!service.isCurrentAudio(list.getCurrentAudio())) startProgress();
            service.playAudio(list.getCurrentAudio());
        }
    }
    protected void playPlaylistItem(int index){
        hideErrorView();
        service.playPlaylistItem(index);
    }
    protected void play(ArgAudio audio){
        hideErrorView();
        if(!service.isCurrentAudio(audio)) startProgress();
        service.playSingleAudio(audio);
    }
    protected void continuePlaying(){service.continuePlaying();}
    protected void playNextAudio(){service.playNextAudio();}
    protected void playPrevAudio(){service.playPrevAudio();}
    protected void pause(){ service.pause(); }
    protected void stop(){
        service.stop();
        setPlayPauseAsPlay();
        setSeekBarMax(0);
        setSeekBarProgresss(0);
        setTimeNowText("00:00");
    }
    protected boolean seekTo(int progress){ return service.seekTo(progress); }
    protected boolean forward(int milliSec, boolean willPlay){ return service.forward(milliSec,willPlay);}
    protected boolean backward(int milliSec, boolean willPlay){ return service.backward(milliSec,willPlay);}
    protected void playAudioAfterPercent(int percent){service.playAudioAfterPercent(percent);}
    protected boolean isPlaying(){ return service.getAudioState() == PLAYING; }
    protected boolean isPaused(){ return service.getAudioState() == PAUSED; }
    protected long getDuration(){return service.getDuration();}
    protected long getCurrentPosition(){return service.getCurrentPosition();}
    protected ArgAudio getCurrentAudio() { return service.getCurrentAudio(); }
    protected boolean hasNextAudio(){return service.isPlaylist() && service.getCurrentPlaylist().hasNext();}
    protected boolean hasPrevAudio(){return service.isPlaylist() && service.getCurrentPlaylist().hasPrev();}
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
    private void onPrepared(ArgAudio audio, int duration){
        if(onPreparedListener!=null) onPreparedListener.onPrepared(audio,duration);
    }
    private void onTimeChanged(long currentTime){
        if(onTimeChangeListener!=null) onTimeChangeListener.onTimeChanged(currentTime);
    }
    private void onPaused(){
        if(onPausedListener!=null) onPausedListener.onPaused();
    }
    private void onCompleted(){
        if(onCompletedListener!=null) onCompletedListener.onCompleted();
    }
    private void onError(ErrorType errorType, String description){
        if(onErrorListener!=null)  onErrorListener.onError(errorType,description);
    }
    private void onPlaying(){
        if(onPlayingListener!=null) onPlayingListener.onPlaying();
    }
    private void onPlaylistAudioChanged(ArgAudioList playlist, int currentAudioIndex){
        if(onPlaylistAudioChangedListener!=null) onPlaylistAudioChangedListener.onPlaylistAudioChanged(playlist,currentAudioIndex);
    }
}
