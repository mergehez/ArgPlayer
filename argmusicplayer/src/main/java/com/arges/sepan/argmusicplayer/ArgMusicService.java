package com.arges.sepan.argmusicplayer;

import static android.media.AudioManager.AUDIOFOCUS_GAIN;
import static android.media.AudioManager.AUDIOFOCUS_REQUEST_GRANTED;
import static android.media.AudioManager.STREAM_MUSIC;
import static com.arges.sepan.argmusicplayer.Enums.AudioState.NO_ACTION;
import static com.arges.sepan.argmusicplayer.Enums.AudioState.PAUSED;
import static com.arges.sepan.argmusicplayer.Enums.AudioState.PLAYING;
import static com.arges.sepan.argmusicplayer.Enums.AudioState.STOPPED;
import static com.arges.sepan.argmusicplayer.Enums.AudioType.URL;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.arges.sepan.argmusicplayer.Callbacks.OnCompletedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnEmbeddedImageReadyListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnErrorListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPausedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPlayingListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPlaylistAudioChangedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPlaylistStateChangedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPreparedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnTimeChangeListener;
import com.arges.sepan.argmusicplayer.Enums.AudioState;
import com.arges.sepan.argmusicplayer.Enums.AudioType;
import com.arges.sepan.argmusicplayer.Enums.ErrorType;
import com.arges.sepan.argmusicplayer.Models.ArgAudio;
import com.arges.sepan.argmusicplayer.Models.ArgAudioList;
import com.arges.sepan.argmusicplayer.Notification.ArgNotification;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

class ArgMusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener {
    private final Context context;
    private final AudioManager audioManager;
    private final IBinder binder = new ArgMusicServiceBinder();
    protected AudioState audioState = NO_ACTION;
    protected String progressMessage = "Audio is loading..";
    protected boolean progressCancellation = false;
    protected boolean errorViewCancellation = false;
    protected boolean nextPrevButtons = true;
    protected boolean repeatButton = true;
    protected int playButtonResId = R.drawable.arg_play;
    protected int pauseButtonResId = R.drawable.arg_pause;
    protected int repeatButtonResId = R.drawable.arg_repeat;
    protected int repeatNotButtonResId = R.drawable.arg_repeat_not;
    // After an incoming call or a notification, service gains audio focus. If player was paused, it should not play audio.
    // We have to check this situation to prevent unexpected playbacks
    boolean wasPlayingBeforeFocusLoss = false;
    private MediaPlayer mediaPlayer;
    private boolean isPlaylistActive = false;
    private boolean isRepeatPlaylist = false;
    private boolean playlistError = true;
    private final ArgAudioList currentPlaylist = new ArgAudioList(true, "ArgCih FromMusicService");
    private ArgAudio currentAudio;
    private OnPreparedListener onPreparedListener;
    private OnTimeChangeListener onTimeChangeListener;
    private OnPausedListener onPausedListener;
    private OnCompletedListener onCompletedListener;
    private OnErrorListener onErrorListener;
    private OnPlayingListener onPlayingListener;
    private OnPlaylistAudioChangedListener onPlaylistAudioChangedListener;
    private OnPlaylistStateChangedListener onPlaylistStateChangedListener;
    private OnEmbeddedImageReadyListener onEmbeddedImageReadyListener;
    private int playAudioPercent = 50;
    private boolean audioFocusHasRequested = false;
    private final AudioManager.OnAudioFocusChangeListener onAudioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AUDIOFOCUS_GAIN:
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                    mediaPlayer.setVolume(1f, 1f);
                    //long timeDiffSinceLastPause = System.currentTimeMillis() - timeWhenPaused;
                    //if audio has been paused 10 or more minutes ago, do not resume
                    if (wasPlayingBeforeFocusLoss/* && timeDiffSinceLastPause < 10*60*1000*/)
                        continuePlaying();
                    break;

                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    if (audioState == PLAYING) mediaPlayer.setVolume(0.1f, 0.1f);
                    wasPlayingBeforeFocusLoss = audioState == PLAYING;
                    break;

                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    wasPlayingBeforeFocusLoss = audioState == PLAYING;
                    pause();
                    break;

                case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                    break;
                default:
            }
        }
    };

    public ArgMusicService(Context context) {
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);

        currentPlaylist.setOnAudioAddedToPlaylistListener((audio, wasRemoved) -> onPlaylistAudioChangedListener.onPlaylistAudioChanged(currentPlaylist, currentPlaylist.getCurrentIndex()));
    }

    // region <setters-getters>
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

    protected void setOnPlaylistStateChangedListener(OnPlaylistStateChangedListener onPlaylistStateChangedListener) {
        this.onPlaylistStateChangedListener = onPlaylistStateChangedListener;
    }

    protected void setOnEmbeddeImageReadyListener(OnEmbeddedImageReadyListener onEmbeddedImageReadyListener) {
        this.onEmbeddedImageReadyListener = onEmbeddedImageReadyListener;
    }

    protected long getDuration() {
        return mediaPlayer != null && audioState != NO_ACTION ? mediaPlayer.getDuration() : -1;
    }

    protected long getCurrentPosition() {
        return mediaPlayer != null ? mediaPlayer.getCurrentPosition() : -1;
    }

    protected ArgAudio getCurrentAudio() {
        return currentAudio;
    }

    protected void setCurrentAudio(@NonNull ArgAudio audio) {
        this.currentAudio = audio;
    }

    protected ArgAudioList getCurrentPlaylist() {
        return this.currentPlaylist;
    }

    protected void setCurrentPlaylist(ArgAudioList argAudioList) {
        currentPlaylist.clear();
        currentPlaylist.addAll(argAudioList.getAll());
    }

    protected boolean getRepeatPlaylist() {
        return isRepeatPlaylist;
    }

    protected void setRepeatPlaylist(boolean repeatPlaylist) {
        this.isRepeatPlaylist = repeatPlaylist;
        currentPlaylist.setRepeat(repeatPlaylist);
    }

    protected boolean getPlaylistError() {
        return playlistError;
    }

    protected void setPlaylistError(boolean playlistError) {
        this.playlistError = playlistError;
    }
    //endregion </setters-getters>

    protected void playAudioAfterPercent(int percent) {
        this.playAudioPercent = percent;
    }

    protected AudioState getAudioState() {
        return audioState;
    }
    // </checkers>

    public void setAudioState(AudioState audioState) {
        this.audioState = audioState;
    }

    protected boolean isPlaylist() {
        return isPlaylistActive;
    }
    //endregion </ServiceOverrides>

    protected boolean isCurrentAudio(ArgAudio audio) {
        return audio != null && audio.equals(currentAudio);
    }

    private boolean isAudioValid(String path, AudioType type) {
        switch (type) {
            case ASSETS:
                try {
                    return context.getAssets().openFd(path) != null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            case RAW:
                return context.getResources().getIdentifier(path, "raw", context.getPackageName()) != 0;
            case URL:
                return path.startsWith("http") || path.startsWith("https");
            case FILE_PATH:
                return new File(path).exists();
            default:
                return false;
        }
    }

    //region  <ServiceOverrides>
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null)
            switch (action) {
                case "com.arges.intent.service.PLAYPAUSE":
                    if (audioState == PLAYING)
                        pause();
                    else
                        continuePlaying();
                    break;
                case "com.arges.intent.service.STOP":
                    stop();
                    break;
                case "com.arges.intent.service.NEXT":
                    playNextAudio();
                    break;
                case "com.arges.intent.service.PREV":
                    playPrevAudio();
                    break;
                case "com.arges.intent.service.CONTINUE":
                    continuePlaying();
                    break;
            }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    protected boolean preparePlaylistToPlay(@NonNull ArgAudioList playlist) {
        final String temp = currentPlaylist.getStringForComparison();

        playlist.setOnAudioAddedToPlaylistListener((audio, wasRemoved) -> currentPlaylist.add(audio));

        currentPlaylist.clear();
        currentPlaylist.addAll(playlist.getAll());

        if (currentPlaylist.size() == 0) {
            killMediaPlayer();
            publishError(ErrorType.EMPTY_PLAYLIST, "Seems you have loaded an empty playlist!");
            return false;
        }

        if (currentPlaylist.getStringForComparison().equals(temp))
            return false;

        isPlaylistActive = true;
        currentPlaylist.setRepeat(getRepeatPlaylist());
        onPlaylistStateChangedListener.onPlaylistStateChanged(true, currentPlaylist);
        return true;
    }

    protected void playAudio(ArgAudio audio) {
        ArgAudio temp = currentAudio;
        currentAudio = audio;
        if (audio == null) {
            killMediaPlayer();
            publishError(ErrorType.NO_AUDIO_SET, "Seems you haven't not loaded an audio yet!");
        } else {
            if (audio.equals(temp)) return;
            if (isAudioValid(audio.getPath(), audio.getType())) {
                try {
                    killMediaPlayer();
                    mediaPlayer = getLoadedMediaPlayer(context, audio);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.setOnBufferingUpdateListener(this);
                    mediaPlayer.setOnCompletionListener(this);
                    mediaPlayer.setOnErrorListener(this);

                    if (audio.getType() == URL)
                        mediaPlayer.prepareAsync();
                    else
                        mediaPlayer.prepare();

                    mediaPlayerTimeOutCheck();
                    // Other actions will be performed in onBufferingUpdate and OnPrepared methods
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                publishInvalidFileError(audio.getType(), audio.getPath());
            }
        }
    }

    protected void playPlaylistItem(int index) {
        if (index == currentPlaylist.getCurrentIndex())
            return;
        if (isPlaylist() && !(index < 0 || index >= currentPlaylist.size())) {
            pauseMediaPlayer();
            //setAudioState(NO_ACTION);
            currentPlaylist.goTo(index);
            onPlaylistAudioChangedListener.onPlaylistAudioChanged(currentPlaylist, currentPlaylist.getCurrentIndex());
        } else {
            publishError(ErrorType.NO_AUDIO_SET, "Invalid index or Empty Playlist");
        }
    }

    protected void playSingleAudio(ArgAudio audio) {   // Use when play new single audio, not for resuming a paused audio
        isPlaylistActive = false;
        currentPlaylist.clear();
        onPlaylistStateChangedListener.onPlaylistStateChanged(false, null);
        playAudio(audio);
    }

    protected void pause() {
        if (mediaPlayer != null) {
            pauseMediaPlayer();
            onPausedListener.onPaused();
        }
    }

    protected void continuePlaying() {
        if (mediaPlayer != null) {
            startMediaPlayer();
            updateTimeThread();
            onPlayingListener.onPlaying();
        }
    }

    protected void replayAudio(ArgAudio audio) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } else {
            playAudio(audio);
        }
    }

    protected void playNextAudio() {
        playPlaylistItem(currentPlaylist.getNextIndex());
    }

    protected void playPrevAudio() {
        playPlaylistItem(currentPlaylist.getPrevIndex());
    }

    protected void stop() {
        if (mediaPlayer != null) {
            pauseMediaPlayer();
            mediaPlayer.seekTo(0);
        }
    }

    protected boolean seekTo(int time) {
        if (mediaPlayer != null && time <= getDuration()) {
            mediaPlayer.seekTo(time);
            return true;
        }
        return false;
    }

    protected boolean forward(int milliSec, boolean willPlay) {
        if (mediaPlayer != null) {
            int seekTime = mediaPlayer.getCurrentPosition() + milliSec;
            if (seekTime > getDuration())
                return false;

            seekTo(seekTime);
            if (willPlay) continuePlaying();
            return true;
        }
        return false;
    }

    protected boolean backward(int milliSec, boolean willPlay) {
        if (mediaPlayer != null) {
            int seekTime = mediaPlayer.getCurrentPosition() - milliSec;
            if (seekTime < 0) return false;
            seekTo(seekTime);
            if (willPlay) continuePlaying();
            return true;
        }
        return false;
    }

    private void mediaPlayerTimeOutCheck() {
        new Handler().postDelayed(() -> {
            if (audioState == NO_ACTION)
                if (playlistError)
                    publishError(ErrorType.MEDIAPLAYER_TIMEOUT, "Url resource has not been prepared in 30 seconds");
                else
                    playNextAudio();
        }, 30000);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    //region <MediaPlayerOverrides>
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if (percent > playAudioPercent && audioState == NO_ACTION) {
            startMediaPlayer();
            updateTimeThread();
            onPlayingListener.onPlaying();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        String errDescription = "MediaPlayer.OnError: \nwhat:" + what + ",\nextra:" + extra;
        publishError(ErrorType.MEDIAPLAYER_ERROR, errDescription);
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (currentAudio == null)
            return;

        if (currentAudio.getType() != URL) {
            onPlayingListener.onPlaying();
            startMediaPlayer();
            updateTimeThread();
        }
        onPreparedListener.onPrepared(currentAudio, mediaPlayer.getDuration());
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopMediaPlayer();
        if (isPlaylist() && currentPlaylist.hasNext()) {
            currentPlaylist.goToNext();
            onPlaylistAudioChangedListener.onPlaylistAudioChanged(currentPlaylist, currentPlaylist.getCurrentIndex());
        } else
            onCompletedListener.onCompleted();
    }
    //endregion </MediaPlayerOverrides>

    private void publishInvalidFileError(AudioType type, String path) {
        switch (type) {
            case ASSETS:
                publishError(ErrorType.INVALID_AUDIO, "The file is not an assets file. Assets Id:" + path);
                break;
            case RAW:
                publishError(ErrorType.INVALID_AUDIO, "The raw id is not valid. Raw Id:" + path);
                break;
            case URL:
                publishError(ErrorType.INVALID_AUDIO, "Url not valid. Url:" + path);
                break;
            case FILE_PATH:
                publishError(ErrorType.INVALID_AUDIO, "The file path is not valid. File Path:" + path + "\n Have you add File Access Permission to your project?");
                break;
            default:
                break;
        }
    }

    private void publishError(ErrorType type, String description) {
        killMediaPlayer();
        onErrorListener.onError(type, description);
    }

    protected void updateTimeThread() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(
                () -> {
                    if(audioState == PAUSED)
                        executor.shutdown();
                    else
                        onTimeChangeListener.onTimeChanged(mediaPlayer.getCurrentPosition());
                },
                0,
                1000,
                TimeUnit.MILLISECONDS
        );
    }

    private MediaPlayer getLoadedMediaPlayer(Context context, ArgAudio audio) throws IOException {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        AssetFileDescriptor descriptor;
        MediaPlayer player = new MediaPlayer();
        player.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build());
        switch (audio.getType()) {
            case ASSETS:
                descriptor = context.getAssets().openFd(audio.getPath());
                player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                retriever.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                break;
            case RAW:
                descriptor = context.getResources().openRawResourceFd(Integer.parseInt(audio.getPath()));
                player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                retriever.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                break;
            case URL:
                player.setDataSource(audio.getPath());
                break;
            case FILE_PATH:
                player.setDataSource(context, Uri.parse(audio.getPath()));
                retriever.setDataSource(context, Uri.parse(audio.getPath()));
                break;
            default:
                break;
        }

        onEmbeddedImageReadyListener.onEmbeddedImageReady(audio.getType() != AudioType.URL ? retriever.getEmbeddedPicture() : null);

        return player;
    }

    private void startMediaPlayer() {
        requestAudioFocus();
        if (audioFocusHasRequested) {
            mediaPlayer.start();
            setAudioState(PLAYING);
        }
    }

    private void pauseMediaPlayer() {
        if (audioState == PLAYING) {
            mediaPlayer.pause();
            setAudioState(PAUSED);
        }
    }

    private void stopMediaPlayer() {
        if (audioState == PLAYING) {
            mediaPlayer.stop();
            setAudioState(STOPPED);
            abandonAudioFocus();
        }
    }

    private void killMediaPlayer() {
        setAudioState(NO_ACTION);
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes attributes = new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).build();
            AudioFocusRequest afr = new AudioFocusRequest.Builder(AUDIOFOCUS_GAIN)
                    .setOnAudioFocusChangeListener(onAudioFocusListener)
                    .setAudioAttributes(attributes)
                    .build();
            audioFocusHasRequested = audioManager.requestAudioFocus(afr) == AUDIOFOCUS_REQUEST_GRANTED;
        } else
            audioFocusHasRequested = audioManager.requestAudioFocus(onAudioFocusListener, STREAM_MUSIC, AUDIOFOCUS_GAIN) == AUDIOFOCUS_REQUEST_GRANTED;
    }

    private void abandonAudioFocus() {
        audioFocusHasRequested = audioManager.abandonAudioFocus(onAudioFocusListener) != AUDIOFOCUS_REQUEST_GRANTED;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        ArgNotification.close(context);
        super.onTaskRemoved(rootIntent);
    }

    public class ArgMusicServiceBinder extends Binder {
        ArgMusicService getService() {
            return ArgMusicService.this;
        }
    }
}
