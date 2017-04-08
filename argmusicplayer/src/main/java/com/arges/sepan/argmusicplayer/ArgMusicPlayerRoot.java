package com.arges.sepan.argmusicplayer;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import com.arges.sepan.argmusicplayer.ArgListener.*;

import static com.arges.sepan.argmusicplayer.AudioState.*;

abstract class ArgMusicPlayerRoot extends RelativeLayout implements View.OnClickListener,SeekBar.OnSeekBarChangeListener {
    private Context context;
    private RelativeLayout panelLayout;
    private TextView tvTimeNow, tvTimeTotal;
    private ImageButton btnPlayPause, btnPrev, btnNext;
    private SeekBar seekBar;
    private ArgProgress progress;
    private ArgErrorView errorView;
    private ArgMusicService service;
    private OnPreparedListener onPreparedListener;
    private OnTimeChangeListener onTimeChangeListener;
    private OnPausedListener onPausedListener;
    private OnCompletedListener onCompletedListener;
    private OnErrorListener onErrorListener;
    private OnPlayingListener onPlayingListener;
    private OnPlaylistAudioChangedListener onPlaylistAudioChangedListener;
    private Convertions cv;
    private String progressMessage = "Audio is loading..";
    private boolean progressCancellation = false;
    private boolean errorViewCancellation = false;
    private boolean playlistControlsCancellation = false;
    private int defaultLayoutResId = R.layout.musicplayerpanel;
    private ArrayList<ArgAudio> playlist = new ArrayList<>();

    public ArgMusicPlayerRoot(Context context) {
        super(context);
        init(context,defaultLayoutResId);
    }
    public ArgMusicPlayerRoot(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,defaultLayoutResId);
    }
    public ArgMusicPlayerRoot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,defaultLayoutResId);
    }
    protected void init(Context context, int layoutResId){
        this.context = context;
        inflate(getContext(), layoutResId, this);
        this.service = new ArgMusicService(context);
        this.panelLayout = (RelativeLayout) findViewById(R.id.arg_music_panel_layout);
        this.tvTimeNow = (TextView)findViewById(R.id.tvTimeNow);
        this.tvTimeTotal = (TextView)findViewById(R.id.tvTimeTotal);
        this.btnPlayPause = (ImageButton)findViewById(R.id.btnPlayPause);
        this.btnPrev = (ImageButton)findViewById(R.id.btnPrev);
        this.btnNext = (ImageButton)findViewById(R.id.btnNext);
        this.progress = (ArgProgress) findViewById(R.id.arg_music_progress);
        this.errorView = (ArgErrorView)findViewById(R.id.arg_music_error_view);
        this.seekBar = (SeekBar) findViewById(R.id.seekBar);

        this.progress.setMessage(progressMessage);
        this.seekBar.setOnSeekBarChangeListener(this);
        this.btnPlayPause.setOnClickListener(this);
        findViewById(R.id.btnStop).setOnClickListener(this);
        this.cv = new Convertions(context);
        this.errorView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopProgress();
                if(!errorViewCancellation)  hideErrorView();
            }
        });
        service.setOnPreparedListener(new ArgListener.OnPreparedListener() {
            @Override
            public void onPrepared(String audioName, int duration) {
                seekBar.setMax(duration);
                setText(tvTimeTotal,convertTimeToString(duration));
                ArgMusicPlayerRoot.this.onPrepared(audioName,duration);
            }
        });
        service.setOnPausedListener(new ArgListener.OnPausedListener() {
            @Override
            public void onPaused() {
                btnPlayPause.setImageResource(R.drawable.arg_music_play);
                ArgMusicPlayerRoot.this.onPaused();
            }
        });
        service.setOnTimeChangeListener(new ArgListener.OnTimeChangeListener() {
            @Override
            public void onTimeChanged(long currentTime) {
                seekBarSetProgresss((int) currentTime);
                setText(tvTimeNow,convertTimeToString(currentTime));
                ArgMusicPlayerRoot.this.onTimeChanged(currentTime);
            }
        });
        service.setOnCompletedListener(new ArgListener.OnCompletedListener() {
            @Override
            public void onCompleted() {
                btnPlayPause.setImageResource(R.drawable.arg_music_play);
                ArgMusicPlayerRoot.this.onCompleted();
            }
        });
        service.setOnErrorListener(new ArgListener.OnErrorListener() {
            @Override
            public void onError(ArgMusicPlayer.ErrorType errorType, String description) {
                btnPlayPause.setImageResource(R.drawable.arg_music_play);
                stopProgress();
                if(!errorViewCancellation)  showErrorView();
                ArgMusicPlayerRoot.this.onError(errorType,description);
            }
        });
        service.setOnPlayingListener(new ArgListener.OnPlayingListener() {
            @Override
            public void onPlaying() {
                btnPlayPause.setImageResource(R.drawable.arg_music_pause);
                stopProgress();
                ArgMusicPlayerRoot.this.onPlaying();
            }
        });
        service.setOnPlaylistAudioChangedListener(new OnPlaylistAudioChangedListener() {
            @Override
            public void onPlaylistAudioChanged(ArgAudioList playlist, int currentAudioIndex) {
                startProgress(playlist.getCurrentAudio());
                service.playAudio(playlist.getNext());
                ArgMusicPlayerRoot.this.onPlaylistAudioChanged(playlist,currentAudioIndex);
            }
        });
        service.setOnPlaylistStateChangedListener(new OnPlaylistStateChangedListener() {
            @Override
            public void onPlaylistStateChanged(boolean isPlaylist, ArgAudioList playlist) {
                if(playlistControlsCancellation) return;
                if(isPlaylist){
                    showPlaylistButtons();
                } else
                    hidePlaylistButtons();
            }
        });
    }
    @Override public void onClick(View v){
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
            } else if (i == R.id.btnStop) {
                stop();
            }
        }
    }
    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) { if(fromUser) seekTo(progress); }
    @Override public void onStartTrackingTouch(SeekBar seekBar) { }
    @Override public void onStopTrackingTouch(SeekBar seekBar) { }
    public void disableProgress(){this.progressCancellation = true;}
    public void enableProgress(){this.progressCancellation = false;}
    public void disableErrorView(){this.errorViewCancellation = true;}
    public void enableErrorView(){this.errorViewCancellation = false;}
    public void disablePlaylistControls(){this.playlistControlsCancellation = true;}
    public void enablePlaylistControls(){this.playlistControlsCancellation = false;}
    public void setProgressMessage(String progressMessage){this.progressMessage = progressMessage;}
    public void repeatingPlaylist(boolean repeat){service.setRepeatPlaylist(repeat);}
    public void loadSingleAudio(@NonNull ArgAudio audio){service.setCurrentAudio(audio);}
    public void loadPlaylist(@NonNull ArgAudioList playlist){service.setCurrentPlaylist(playlist);}
    public void playLoadedSingleAudio(){play(getCurrentAudio());}
    public void playLoadedPlaylist(){service.preparePlaylistToPlay(service.getCurrentPlaylist());}
    public void playPlaylist(@NonNull ArgAudioList argAudioList){
        if(service.preparePlaylistToPlay(argAudioList)){
            ArgAudioList list = service.getCurrentPlaylist();
            startProgress(list.getCurrentAudio());
            service.playAudio(list.getCurrentAudio());
        }
    }
    public void play(ArgAudio audio){
        startProgress(audio);
        service.playSingleAudio(audio);
    }
    public void pause(){ service.pause(); }
    public void stop(){
        service.stop();
        btnPlayPause.setImageResource(R.drawable.arg_music_play);
        seekBar.setMax(0);
        seekBarSetProgresss(0);
        setText(tvTimeNow,"00:00");
    }
    public void seekTo(int progress){ service.seekTo(progress); }
    public void playAudioAfterPercent(int percent){service.playAudioAfterPercent(percent);}
    public boolean isPlaying(){ return service.getAudioState() == PLAYING; }
    public boolean isPaused(){ return service.getAudioState() == PAUSED; }
    public long getDuration(){return service.getDuration();}
    public ArgAudio getCurrentAudio() { return service.getCurrentAudio(); }
    private void setText(final TextView tv, final String text){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(text);
            }
        });
    }
    private void seekBarSetProgresss(final int progress){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(progress);
            }
        });
    }
    private void startProgress(ArgAudio audio){
        if(!progressCancellation && !service.isCurrentAudio(audio)){
            progress.start();
            Arg.disableContent(this.panelLayout);
        }
    }
    private void stopProgress(){progress.stop();    Arg.enableContent(this.panelLayout);}
    private void showErrorView(){Arg.disableContent(this.panelLayout);  this.errorView.show(); }
    private void hideErrorView(){Arg.enableContent(this.panelLayout);   this.errorView.hide(); }
    private String convertTimeToString(long time){
        return new SimpleDateFormat("mm:ss", Locale.getDefault()).format(time);
    }
    private void showPlaylistButtons(){
        if(!playlistControlsCancellation){
            this.btnNext.setEnabled(true);
            this.btnPrev.setEnabled(true);
            this.btnNext.setVisibility(VISIBLE);
            this.btnPrev.setVisibility(VISIBLE);
        }
    }
    private void hidePlaylistButtons(){
        this.btnNext.setVisibility(GONE);
        this.btnPrev.setVisibility(GONE);
    }




    // Listeners
    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.onPreparedListener = onPreparedListener;
    }
    public void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
        this.onTimeChangeListener = onTimeChangeListener;
    }
    public void setOnPausedListener(OnPausedListener onPausedListener) {
        this.onPausedListener = onPausedListener;
    }
    public void setOnCompletedListener(OnCompletedListener onCompletedListener) {
        this.onCompletedListener = onCompletedListener;
    }
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }
    public void setOnPlayingListener(OnPlayingListener onPlayingListener) {
        this.onPlayingListener = onPlayingListener;
    }
    public void setOnPlaylistAudioChangedListener(OnPlaylistAudioChangedListener onPlaylistAudioChangedListener) {
        this.onPlaylistAudioChangedListener = onPlaylistAudioChangedListener;
    }
    private void onPrepared(String audioName, int duration){
        if(onPreparedListener!=null) onPreparedListener.onPrepared(audioName,duration);
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
    private void onError(ArgMusicPlayer.ErrorType errorType, String description){
        if(onErrorListener!=null)  onErrorListener.onError(errorType,description);
    }
    private void onPlaying(){
        if(onPlayingListener!=null) onPlayingListener.onPlaying();
    }
    private void onPlaylistAudioChanged(ArgAudioList playlist, int currentAudioIndex){
        if(onPlaylistAudioChangedListener!=null) onPlaylistAudioChangedListener.onPlaylistAudioChanged(playlist,currentAudioIndex);
    }
}