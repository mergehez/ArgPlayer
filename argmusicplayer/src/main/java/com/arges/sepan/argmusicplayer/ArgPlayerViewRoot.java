package com.arges.sepan.argmusicplayer;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.arges.sepan.argmusicplayer.Callbacks.OnCompletedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnErrorListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPausedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPlayingListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPlaylistAudioChangedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPreparedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnTimeChangeListener;
import com.arges.sepan.argmusicplayer.Models.ArgAudio;
import com.arges.sepan.argmusicplayer.Models.ArgAudioList;
import com.arges.sepan.argmusicplayer.Models.ArgNotificationOptions;
import com.arges.sepan.argmusicplayer.Views.ArgErrorView;
import com.arges.sepan.argmusicplayer.Views.ArgProgressView;

public abstract class ArgPlayerViewRoot extends RelativeLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    protected Context context;
    protected int defaultLayoutResId = R.layout.player_small_layout;
    private AppCompatTextView tvTimeNow, tvTimeTotal;
    private AppCompatImageButton btnPlayPause, btnPrev, btnNext, btnRepeat;
    private SeekBar seekBar;
    private ArgProgressView progress;
    private ArgErrorView errorView;
    protected ArgMusicPlayer player;

    protected abstract void setEmbeddedImageBitmap(byte[] byteArray);

    protected abstract void onAudioNameChanged(ArgAudio audio);

    protected abstract void onPlaylistAudioChanged(ArgAudioList list);

    public ArgPlayerViewRoot(Context context) {
        super(context);
        init(context, defaultLayoutResId);
    }

    public ArgPlayerViewRoot(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, defaultLayoutResId);
    }

    public ArgPlayerViewRoot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, defaultLayoutResId);
    }

    protected void init(Context context, int layoutResId) {
        inflate(getContext(), layoutResId, this);
        if (isInEditMode()) return;
        this.context = context;
        // RelativeLayout panelLayout = findViewById(R.id.arg_music_panel_layout);
        this.tvTimeNow = findViewById(R.id.tvTimeNow);
        this.tvTimeTotal = findViewById(R.id.tvTimeTotal);
        this.btnPlayPause = findViewById(R.id.btnPlayPause);
        this.btnPrev = findViewById(R.id.btnPrev);
        this.btnNext = findViewById(R.id.btnNext);
        this.btnRepeat = findViewById(R.id.btnRepeat);
        this.progress = findViewById(R.id.arg_music_progress);
        this.errorView = findViewById(R.id.arg_music_error_view);
        this.seekBar = findViewById(R.id.seekBar);

        this.seekBar.setOnSeekBarChangeListener(this);
        this.btnPlayPause.setOnClickListener(this);
        this.btnNext.setOnClickListener(this);
        this.btnPrev.setOnClickListener(this);
        this.btnRepeat.setOnClickListener(this);
        this.errorView.setOnClickListener(this);



        player = new ArgMusicPlayer(context.getApplicationContext()) {
            @Override
            protected void hideErrorView() {
                errorView.hide();
            }

            @Override
            protected void showErrorView() {
                if (!player.service.errorViewCancellation) errorView.show();
            }

            @Override
            protected void changeNextPrevButtons() {
                ArgPlayerViewRoot.this.changeNextPrevButtons();
            }

            @Override
            protected boolean getNextPrevButtonsState() {
                return player.service.nextPrevButtons;
            }

            @Override
            protected void onPlaylistAudioChanged(ArgAudioList audioList) {
                ArgPlayerViewRoot.this.onPlaylistAudioChanged(audioList);
            }

            @Override
            protected void setPlayPauseAsPause() {
                btnPlayPause.setImageResource(player.service.pauseButtonResId);
            }

            @Override
            protected void setPlayPauseAsPlay() {
                btnPlayPause.setImageResource(player.service.playButtonResId);
            }

            @Override
            protected void setSeekBarMax(int max) {
                seekBar.setMax(max);
            }

            @Override
            protected void setSeekBarProgresss(int progress) {
                seekBar.setProgress(progress);
            }

            @Override
            protected void setTimeTotalText(String text) {
                setTvText(false, text);
            }

            @Override
            protected void setTimeNowText(String text) {
                setTvText(true, text);
            }

            @Override
            protected void startProgress() {
                if (!player.service.progressCancellation) {
                    progress.start();
                }
            }

            @Override
            protected void stopProgress() {
                progress.stop();
            }

            @Override
            protected void changeRepeatButton() {
                ArgPlayerViewRoot.this.changeRepeatButton();
            }

            @Override
            protected void setEmbeddedImageBitmap(byte[] byteArray) {
                ArgPlayerViewRoot.this.setEmbeddedImageBitmap(byteArray);
            }

            @Override
            protected void changeAudioName(ArgAudio audio) {
                onAudioNameChanged(audio);
            }
        };
        player.checkIfAppReOpened();
        player.setOnPlaylistAudioChangedListener((list, currIndex) -> onPlaylistAudioChanged(list));

        this.progress.setMessage(player.service.progressMessage);
    }

    private void changeNextPrevButtons() {
        if (player.service.nextPrevButtons) {
            btnNext.setVisibility(VISIBLE);
            btnPrev.setVisibility(VISIBLE);
            btnNext.setEnabled(player.hasNextAudio());
            btnPrev.setEnabled(player.hasPrevAudio());
        } else {
            btnNext.setVisibility(GONE);
            btnPrev.setVisibility(GONE);
        }
    }

    private void setTvText(final boolean tvNow, final String text) {
        TextView tv = (tvNow ? tvTimeNow : tvTimeTotal);
        ((Activity) context).runOnUiThread(() -> tv.setText(String.format("%s", text)));
    }

    private void changeRepeatButton() {
        if (player.getPlaylistRepeat())
            btnRepeat.setImageResource(player.service.repeatButtonResId);
        else btnRepeat.setImageResource(player.service.repeatNotButtonResId);
        changeNextPrevButtons();
    }

    @Override
    public void onClick(View v) {
        player.onClick(v);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) player.seekTo(progress);
    }

    // UI methods
    public void enableNotification(@NonNull ArgNotificationOptions options) {
        player.enableNotification(options);
    }
    public void enableNotification(@NonNull Activity activity) {
        player.enableNotification(new ArgNotificationOptions(activity));
    }
    public void disableNotification() {
        player.disableNotification();
    }


    public void disableProgress() {
        player.service.progressCancellation = true;
    }

    public void enableProgress() {
        player.service.progressCancellation = false;
    }

    public void disableErrorView() {
        player.service.errorViewCancellation = true;
    }

    public void enableErrorView() {
        player.service.errorViewCancellation = false;
    }

    public void disableNextPrevButtons() {

        player.service.nextPrevButtons = false;
        changeNextPrevButtons();
    }

    public void enableNextPrevButtons() {
        player.service.nextPrevButtons = true;
        changeNextPrevButtons();
    }

    public void setProgressMessage(String progressMessage) {
        player.service.progressMessage = progressMessage;
    }

    public void setAllButtonsImageResource(int resIdPlay, int resIdPause, int resIdPrev, int resIdNext, int resIdRepeat, int resIdNotRepeat) {
        setPrevButtonImageResource(resIdPrev);
        setNextButtonImageResource(resIdNext);
        setRepeatButtonImageResource(resIdRepeat, resIdNotRepeat);
        setPlayButtonImageResource(resIdPlay);
        setPauseButtonImageResource(resIdPause);
    }

    public void setPrevButtonImageResource(int resId) {
        this.btnPrev.setImageResource(resId);
    }

    public void setNextButtonImageResource(int resId) {
        this.btnNext.setImageResource(resId);
    }

    public void setRepeatButtonImageResource(int repeatResId, int notRepeatResId) {
        player.service.repeatButtonResId = repeatResId;
        player.service.repeatNotButtonResId = notRepeatResId;
        changeRepeatButton();
    }

    public void setPlayButtonImageResource(int resId) {
        player.service.playButtonResId = resId;
        if (player.isPlaying()) btnPlayPause.setImageResource(resId);
    }

    public void setPauseButtonImageResource(int resId) {
        player.service.pauseButtonResId = resId;
        if (player.isPaused()) btnPlayPause.setImageResource(resId);
    }

    // Listener Set Methods
    public void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        errorView.hide();
        player.setOnPreparedListener(onPreparedListener);
    }



    public void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) {
        player.setOnTimeChangeListener(onTimeChangeListener);
    }

    public void setOnPausedListener(OnPausedListener onPausedListener) {
        player.setOnPausedListener(onPausedListener);
    }

    public void setOnCompletedListener(OnCompletedListener onCompletedListener) {
        player.setOnCompletedListener(onCompletedListener);
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        player.setOnErrorListener(onErrorListener);
    }

    public void setOnPlayingListener(OnPlayingListener onPlayingListener) {
        player.setOnPlayingListener(onPlayingListener);
    }

    public void setOnPlaylistAudioChangedListener(OnPlaylistAudioChangedListener onPlaylistAudioChangedListener) {
        player.setOnPlaylistAudioChangedListener(onPlaylistAudioChangedListener);
    }


    // Player methods
    public boolean seekTo(int progress) {
        return player.seekTo(progress);
    }

    public boolean forward(int milliSec, boolean willPlay) {
        return player.forward(milliSec, willPlay);
    }

    public boolean backward(int milliSec, boolean willPlay) {
        return player.backward(milliSec, willPlay);
    }

    public void playAudioAfterPercent(int percent) {
        player.playAudioAfterPercent(percent);
    }

    public boolean isPlaying() {
        return player.isPlaying();
    }

    public boolean isPaused() {
        return player.isPaused();
    }

    public long getDuration() {
        return player.getDuration();
    }

    public long getCurrentPosition() {
        return player.getCurrentPosition();
    }

    public ArgAudio getCurrentAudio() {
        return player.getCurrentAudio();
    }

    public void setPlaylistRepeat(boolean repeat) {
        player.setPlaylistRepeat(repeat);
    }

    public void loadSingleAudio(@NonNull ArgAudio audio) {
        player.loadSingleAudio(audio);
    }

    public void loadPlaylist(@NonNull ArgAudioList playlist) {
        player.loadPlaylist(playlist);
    }

    public void playLoadedSingleAudio() {
        player.play(getCurrentAudio());
    }

    public void playLoadedPlaylist() {
        player.playLoadedPlaylist();
    }

    public void playPlaylist(@NonNull ArgAudioList argAudioList) {
        player.playPlaylist(argAudioList);
    }

    public void play(ArgAudio audio) {
        player.play(audio);
    }

    public void playPlaylistItem(int index) {
        player.playPlaylistItem(index);
    }

    public void pause() {
        player.pause();
    }

    public void resume() {
        player.continuePlaying();
    }

    public void stop() {
        player.stop();
    }

    public void continuePlaylistWhenError() {
        player.continuePlaylistOnError();
    }

    public void stopPlaylistWhenError() {
        player.stopPlaylistOnError();
    }
}
