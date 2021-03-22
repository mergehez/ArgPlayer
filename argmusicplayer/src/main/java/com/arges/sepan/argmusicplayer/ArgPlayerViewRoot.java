package com.arges.sepan.argmusicplayer;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.arges.sepan.argmusicplayer.IndependentClasses.Arg;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudio;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudioList;
import com.arges.sepan.argmusicplayer.Views.ArgErrorView;
import com.arges.sepan.argmusicplayer.Views.ArgProgressView;

abstract class ArgPlayerViewRoot extends RelativeLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    protected Context context;
    protected int defaultLayoutResId = R.layout.player_small_layout;
    private RelativeLayout panelLayout;
    private AppCompatTextView tvTimeNow, tvTimeTotal;
    private AppCompatImageButton btnPlayPause, btnPrev, btnNext, btnRepeat;
    private SeekBar seekBar;
    private ArgProgressView progress;
    private ArgErrorView errorView;
    ArgMusicPlayer player;

    abstract void setEmbeddedImageBitmap(byte[] byteArray);

    abstract void onAudioNameChanged(ArgAudio audio);

    abstract void onPlaylistAudioChanged(ArgAudioList list);

    public ArgPlayerViewRoot(Context context) {
        super(context);
        init(context,defaultLayoutResId);
    }
    public ArgPlayerViewRoot(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,defaultLayoutResId);
    }
    public ArgPlayerViewRoot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,defaultLayoutResId);
    }

    protected void init(Context context, int layoutResId) {
        inflate(getContext(), layoutResId, this);
        if (isInEditMode()) return;
        this.context = context;
        this.panelLayout = (RelativeLayout) findViewById(R.id.arg_music_panel_layout);
        this.tvTimeNow = (AppCompatTextView) findViewById(R.id.tvTimeNow);
        this.tvTimeTotal = (AppCompatTextView) findViewById(R.id.tvTimeTotal);
        this.btnPlayPause = findViewById(R.id.btnPlayPause);
        this.btnPrev = findViewById(R.id.btnPrev);
        this.btnNext = findViewById(R.id.btnNext);
        this.btnRepeat = findViewById(R.id.btnRepeat);
        this.progress = (ArgProgressView) findViewById(R.id.arg_music_progress);
        this.errorView = (ArgErrorView) findViewById(R.id.arg_music_error_view);
        this.seekBar = (SeekBar) findViewById(R.id.seekBar);

        this.seekBar.setOnSeekBarChangeListener(this);
        this.btnPlayPause.setOnClickListener(this);
        this.btnNext.setOnClickListener(this);
        this.btnPrev.setOnClickListener(this);
        this.btnRepeat.setOnClickListener(this);
        this.errorView.setOnClickListener(this);



        this.progress.setMessage(ArgMusicService.progressMessage);

        player = new ArgMusicPlayer(context.getApplicationContext()) {
            @Override protected void hideErrorView() {  errorView.hide(); }
            @Override protected void showErrorView() { if(!ArgMusicService.errorViewCancellation) errorView.show(); }
            @Override protected void changeNextPrevButtons() { ArgPlayerViewRoot.this.changeNextPrevButtons(); }
            @Override protected boolean getNextPrevButtonsState() { return ArgMusicService.nextPrevButtons; }
            @Override protected void onPlaylistAudioChanged(ArgAudioList audioList) {ArgPlayerViewRoot.this.onPlaylistAudioChanged(audioList); }
            @Override protected void setPlayPauseAsPause() { btnPlayPause.setImageResource(ArgMusicService.pauseButtonResId); }
            @Override protected void setPlayPauseAsPlay() { btnPlayPause.setImageResource(ArgMusicService.playButtonResId); }
            @Override protected void setSeekBarMax(int max) { seekBar.setMax(max); }
            @Override protected void setSeekBarProgresss(int progress) { seekBar.setProgress(progress); }
            @Override protected void setTimeTotalText(String text) { setTvText(false, text); }
            @Override protected void setTimeNowText(String text) { setTvText(true, text); }
            @Override protected void startProgress() { if(!ArgMusicService.progressCancellation){ progress.start();}}
            @Override protected void stopProgress() { progress.stop();}
            @Override protected void changeRepeatButton() { ArgPlayerViewRoot.this.changeRepeatButton(); }
            @Override protected void setEmbeddedImageBitmap(byte[] byteArray) { ArgPlayerViewRoot.this.setEmbeddedImageBitmap(byteArray);}
            @Override protected void changeAudioName(ArgAudio audio) { onAudioNameChanged(audio);}
        };
        player.checkIfAppReOpened();
    }

    private void changeNextPrevButtons(){
        if(ArgMusicService.nextPrevButtons){
            btnNext.setVisibility(VISIBLE);
            btnPrev.setVisibility(VISIBLE);
            btnNext.setEnabled(player.hasNextAudio());
            btnPrev.setEnabled(player.hasPrevAudio());
        }else{
            btnNext.setVisibility(GONE);
            btnPrev.setVisibility(GONE);
        }
    }
    private void setTvText(final boolean tvNow, final String text){
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                (tvNow? tvTimeNow : tvTimeTotal).setText(String.format("%s", text));
            }
        });
    }
    private void changeRepeatButton(){
        if(player.getPlaylistRepeat()) btnRepeat.setImageResource(ArgMusicService.repeatButtonResId);
        else  btnRepeat.setImageResource(ArgMusicService.repeatNotButtonResId);
        changeNextPrevButtons();
    }

    @Override public void onClick(View v){ player.onClick(v); }
    @Override public void onStartTrackingTouch(SeekBar seekBar) {}
    @Override public void onStopTrackingTouch(SeekBar seekBar) {}
    @Override public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if(fromUser) player.seekTo(progress);
    }

    // UI methods
    public void enableNotification(@NonNull Class mainActivityClass){
        if(mainActivityClass != null){
            ArgNotification.mainActivityClass = mainActivityClass;
            player.setNotif(true);
        }
    }
    public void enableNotification(@NonNull Class mainActivityClass, int notificationImageResourceId){
        enableNotification(mainActivityClass);
        ArgNotification.notifImgResId = notificationImageResourceId;
    }
    public void disableNotification(){player.setNotif(false);}
    public void disableProgress(){ArgMusicService.progressCancellation = true;}
    public void enableProgress(){ArgMusicService.progressCancellation = false;}
    public void disableErrorView(){ArgMusicService.errorViewCancellation = true;}
    public void enableErrorView(){ArgMusicService.errorViewCancellation = false;}
    public void disableNextPrevButtons(){ArgMusicService.nextPrevButtons = false;  changeNextPrevButtons();}
    public void enableNextPrevButtons(){ArgMusicService.nextPrevButtons = true;  changeNextPrevButtons();}
    public void setProgressMessage(String progressMessage){ArgMusicService.progressMessage = progressMessage;}
    public void setAllButtonsImageResource(int resIdPlay, int resIdPause, int resIdPrev, int resIdNext, int resIdRepeat, int resIdNotRepeat){
        setPrevButtonImageResource(resIdPrev);
        setNextButtonImageResource(resIdNext);
        setRepeatButtonImageResource(resIdRepeat, resIdNotRepeat);
        setPlayButtonImageResource(resIdPlay);
        setPauseButtonImageResource(resIdPause);
    }
    public void setPrevButtonImageResource(int resId){ this.btnPrev.setImageResource(resId); }
    public void setNextButtonImageResource(int resId){ this.btnNext.setImageResource(resId); }
    public void setRepeatButtonImageResource(int repeatResId, int notRepeatResId){
        ArgMusicService.repeatButtonResId = repeatResId;
        ArgMusicService.repeatNotButtonResId = notRepeatResId;
        changeRepeatButton();
    }
    public void setPlayButtonImageResource(int resId){
        ArgMusicService.playButtonResId = resId;
        if(player.isPlaying()) btnPlayPause.setImageResource(resId);
    }
    public void setPauseButtonImageResource(int resId){
        ArgMusicService.pauseButtonResId = resId;
        if(player.isPaused()) btnPlayPause.setImageResource(resId);
    }

    // Listener Set Methods
    public void setOnPreparedListener(Arg.OnPreparedListener onPreparedListener) {
        errorView.hide();
        player.setOnPreparedListener(onPreparedListener);
    }
    public void setOnTimeChangeListener(Arg.OnTimeChangeListener onTimeChangeListener) {
        player.setOnTimeChangeListener(onTimeChangeListener);
    }
    public void setOnPausedListener(Arg.OnPausedListener onPausedListener) {
        player.setOnPausedListener(onPausedListener);
    }
    public void setOnCompletedListener(Arg.OnCompletedListener onCompletedListener) {
        player.setOnCompletedListener(onCompletedListener);
    }
    public void setOnErrorListener(Arg.OnErrorListener onErrorListener) {
        player.setOnErrorListener(onErrorListener);
    }
    public void setOnPlayingListener(Arg.OnPlayingListener onPlayingListener) {
        player.setOnPlayingListener(onPlayingListener);
    }
    public void setOnPlaylistAudioChangedListener(Arg.OnPlaylistAudioChangedListener onPlaylistAudioChangedListener) {
        player.setOnPlaylistAudioChangedListener(onPlaylistAudioChangedListener);
    }


    // Player methods
    public boolean seekTo(int progress){ return player.seekTo(progress); }
    public boolean forward(int milliSec, boolean willPlay){ return player.forward(milliSec,willPlay);}
    public boolean backward(int milliSec, boolean willPlay){ return player.backward(milliSec,willPlay);}
    public void playAudioAfterPercent(int percent){player.playAudioAfterPercent(percent);}
    public boolean isPlaying(){ return player.isPlaying(); }
    public boolean isPaused(){ return player.isPaused(); }
    public long getDuration(){return player.getDuration();}
    public long getCurrentPosition(){return player.getCurrentPosition();}
    public ArgAudio getCurrentAudio() { return player.getCurrentAudio(); }
    public void setPlaylistRepeat(boolean repeat){player.setPlaylistRepeat(repeat);}
    public void loadSingleAudio(@NonNull ArgAudio audio){player.loadSingleAudio(audio);}
    public void loadPlaylist(@NonNull ArgAudioList playlist){player.loadPlaylist(playlist);}
    public void playLoadedSingleAudio(){player.play(getCurrentAudio());}
    public void playLoadedPlaylist(){player.playLoadedPlaylist();}
    public void playPlaylist(@NonNull ArgAudioList argAudioList){ player.playPlaylist(argAudioList); }
    public void play(ArgAudio audio){   player.play(audio); }
    public void playPlaylistItem(int index){player.playPlaylistItem(index);}
    public void pause(){ player.pause(); }
    public void resume(){ player.continuePlaying(); }
    public void stop(){ player.stop(); }
    public void continuePlaylistWhenError(){ player.continuePlaylistOnError();}
    public void stopPlaylistWhenError(){ player.stopPlaylistOnError(); }
}
