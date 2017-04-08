package com.arges.sepan.argmusicplayer;

import android.app.AlarmManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import com.arges.sepan.argmusicplayer.ArgMusicPlayer.ErrorType;
import com.arges.sepan.argmusicplayer.ArgListener.*;

import static com.arges.sepan.argmusicplayer.ArgMusicPlayer.ErrorType.EMPTY_PLAYLIST;
import static com.arges.sepan.argmusicplayer.ArgMusicPlayer.ErrorType.NO_AUDIO_SETTED;
import static com.arges.sepan.argmusicplayer.AudioState.*;
import static com.arges.sepan.argmusicplayer.CmdVar.*;

public class ArgMusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener,
                                                        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener {
    private MediaPlayer mediaPlayer;
    private AlarmManager alarmManager;
    private AudioManager.OnAudioFocusChangeListener afChangeListener;
    private boolean isRepeatPlaylist = false;
    private ArgAudioList currentPlaylist = new ArgAudioList();
    private ArgAudio currentAudio;
    private OnPreparedListener onPreparedListener;
    private OnTimeChangeListener onTimeChangeListener;
    private OnPausedListener onPausedListener;
    private OnCompletedListener onCompletedListener;
    private OnErrorListener onErrorListener;
    private OnPlayingListener onPlayingListener;
    private OnPlaylistAudioChangedListener onPlaylistAudioChangedListener;
    private OnPlaylistStateChangedListener onPlaylistStateChangedListener;
    private int playAudioPercent = 50;
    private Context context;
    protected AudioState audioState = NO_ACTION;

    public ArgMusicService(){}
    public ArgMusicService(Context context){
        this.context = context;

    }

    // <setters-getters>
    protected void setOnPreparedListener(OnPreparedListener onPreparedListener) { this.onPreparedListener = onPreparedListener; }
    protected void setOnTimeChangeListener(OnTimeChangeListener onTimeChangeListener) { this.onTimeChangeListener = onTimeChangeListener; }
    protected void setOnPausedListener(OnPausedListener onPausedListener) { this.onPausedListener = onPausedListener; }
    protected void setOnCompletedListener(OnCompletedListener onCompletedListener) { this.onCompletedListener = onCompletedListener; }
    protected void setOnErrorListener(OnErrorListener onErrorListener) { this.onErrorListener = onErrorListener; }
    protected void setOnPlayingListener(OnPlayingListener onPlayingListener) { this.onPlayingListener = onPlayingListener; }
    protected void setOnPlaylistAudioChangedListener(OnPlaylistAudioChangedListener onPlaylistAudioChangedListener) { this.onPlaylistAudioChangedListener = onPlaylistAudioChangedListener; }
    protected void setOnPlaylistStateChangedListener(OnPlaylistStateChangedListener onPlaylistStateChangedListener) { this.onPlaylistStateChangedListener = onPlaylistStateChangedListener; }
    protected long getDuration() {  return mediaPlayer.getDuration();  }
    protected ArgAudio getCurrentAudio(){  return currentAudio; }
    protected void setCurrentAudio(@NonNull ArgAudio audio){  this.currentAudio = audio; }
    protected void setCurrentPlaylist(ArgAudioList argAudioList){ this.currentPlaylist = argAudioList;}
    protected ArgAudioList getCurrentPlaylist(){return this.currentPlaylist;}
    protected void setRepeatPlaylist(boolean repeatPlaylist){this.isRepeatPlaylist = repeatPlaylist;}
    protected boolean getRepeatPlaylist(){return isRepeatPlaylist;}
    protected void playAudioAfterPercent(int percent){this.playAudioPercent=percent;}
    protected AudioState getAudioState(){return audioState;}
    protected boolean isPlaylist(){return currentPlaylist!=null;}
    // </setters-getters>

    // <checkers>
    protected boolean isCurrentAudio(ArgAudio audio){return audio.equals(currentAudio);}
    private boolean isAudioValid(String path, AudioType type){
        switch (type){
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
    // </checkers>

    // <ServiceOverrides>
    @Override public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Override public void onDestroy() {
        super.onDestroy();
    }
    // </ServiceOverrides>

    protected boolean preparePlaylistToPlay(@NonNull ArgAudioList playlist){
        ArgAudioList temp = currentPlaylist;
        currentPlaylist = playlist;
        if(currentPlaylist.size()==0){
            killMediaPlayer();
            publishError(EMPTY_PLAYLIST,"Seems you have loaded an empty playlist!");
            return false;
        }
        if(currentPlaylist.equals(temp))return false;
        currentPlaylist.setRepeat(getRepeatPlaylist());
        onPlaylistStateChangedListener.onPlaylistStateChanged(true,currentPlaylist);
        return true;
    }
    protected void playAudio(ArgAudio audio){
        ArgAudio temp = currentAudio;
        currentAudio = audio;
        if(audio==null){
            killMediaPlayer();
            publishError(NO_AUDIO_SETTED,"Seems you did not load an audio!");
        }else{
            if(audio.equals(temp)) return;
            if(isAudioValid(audio.getPath(),audio.getType())){
                try{
                    killMediaPlayer();
                    mediaPlayer = ArgMusicService.getLoadedMediaPlayer(context,audio);
                    mediaPlayer.setOnPreparedListener(this);
                    mediaPlayer.setOnBufferingUpdateListener(this);
                    mediaPlayer.setOnCompletionListener(this);
                    mediaPlayer.setOnErrorListener(this);
                    if(audio.getType()==AudioType.URL)  mediaPlayer.prepareAsync();
                    else    mediaPlayer.prepare();
                    // Other actions will be performed in onBufferingUpdate and OnPrepared methods
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else{
                publishInvalidFileError(audio.getType(),audio.getPath());
            }
        }
    }
    protected void playSingleAudio(ArgAudio audio){   // Use when play new single audio, not for resuming a paused audio
        currentPlaylist = null;
        onPlaylistStateChangedListener.onPlaylistStateChanged(false,null);
        playAudio(audio);
    }
    protected void pause(){
        if (mediaPlayer != null) {
            pauseMediaPlayer();
            onPausedListener.onPaused();
        }
    }
    protected void continuePlaying(){
        if(mediaPlayer!=null){
            startMediaPlayer();
            updateTimeThread();
            onPlayingListener.onPlaying();
        }
    }
    protected void replayAudio(ArgAudio audio){
        if(mediaPlayer!=null){
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        }else{
            playAudio(audio);
        }
    }
    protected void stop(){
        if( mediaPlayer != null ){
            pauseMediaPlayer();
            mediaPlayer.seekTo(0);
        }
    }
    protected void seekTo(int time){
        if(mediaPlayer!=null) mediaPlayer.seekTo(time);
    }

    //<MediaPlayerOverrides>
    @Override @Nullable public IBinder onBind(Intent intent) {
        return null;
    }
    @Override public void onBufferingUpdate(MediaPlayer mp, int percent) {
        if(percent>playAudioPercent && audioState==NO_ACTION){
            startMediaPlayer();
            updateTimeThread();
            onPlayingListener.onPlaying();
        }
    }
    @Override public boolean onError(MediaPlayer mp, int what, int extra) {
        String errDescription = "MediaPlayer.OnError: \nwhat:"+what+",\nextra:"+extra;
        publishError(ErrorType.MEDIAPLAYER_ERROR, errDescription);
        return false;
    }
    @Override public void onPrepared(MediaPlayer mp){
        if(currentAudio==null) return;
        if(currentAudio.getType() != AudioType.URL){
            onPlayingListener.onPlaying();
            startMediaPlayer();
            updateTimeThread();
        }
        onPreparedListener.onPrepared(currentAudio.getTitle(),mediaPlayer.getDuration());
    }
    @Override public void onCompletion(MediaPlayer mp) {
        stopMediaPlayer();
        if(isPlaylist() && currentPlaylist.hasNext()){
            onPlaylistAudioChangedListener.onPlaylistAudioChanged(currentPlaylist,currentPlaylist.getCurrentIndex());
        }else
            onCompletedListener.onCompleted();
    }
    //</MediaPlayerOverrides>

    //<ErrorFunctions>
    private void publishInvalidFileError(AudioType type, String path){
        switch (type){
            case ASSETS:
                publishError(ErrorType.INVALID_AUDIO, "The file is not an assets file. Assets Id:"+path); break;
            case RAW:
                publishError(ErrorType.INVALID_AUDIO, "The raw id is not valid. Raw Id:"+path); break;
            case URL:
                publishError(ErrorType.INVALID_AUDIO, "Url not valid. Url:"+path); break;
            case FILE_PATH:
                publishError(ErrorType.INVALID_AUDIO, "The file path is not valid. File Path:"+path+"\n Have you add File Access Permission to your project?"); break;
            default: break;
        }
    }
    private void publishError(ErrorType type, String description){
        killMediaPlayer();
        currentPlaylist=null; currentAudio=null;
        onErrorListener.onError(type,description);
    }
    //</ErrorFunctions>

    private void updateTimeThread(){
        new Thread(){
            @Override
            public void run() {
                while (audioState==PLAYING && mediaPlayer!=null){
                    try {
                        onTimeChangeListener.onTimeChanged(mediaPlayer.getCurrentPosition());
                        Thread.sleep(1000);     //Will update time info per second
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                        Log.d("UpdateTimeThread:",e.getMessage());
                    }
                }
            }
        }.start();
    }
    private static MediaPlayer getLoadedMediaPlayer(Context context, ArgAudio audio) throws IOException {
        AssetFileDescriptor descriptor;
        MediaPlayer player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        switch (audio.getType()){
            case ASSETS:
                descriptor = context.getAssets().openFd(audio.getPath());
                player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                break;
            case RAW:
                descriptor = context.getResources().openRawResourceFd(Integer.parseInt(audio.getPath()));
                player.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                break;
            case URL:
                player.setDataSource(audio.getPath()); break;
            case FILE_PATH:
                player.setDataSource(context, Uri.parse(audio.getPath()));  break;
            default: break;
        }
        return player;
    }
    private void killMediaPlayer(){
        audioState = NO_ACTION;
        if( mediaPlayer != null ){
            mediaPlayer.stop();
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    private void startMediaPlayer(){// 1. Acquire audio focus
        if (!mAudioFocusGranted && requestAudioFocus()) {
            // 2. Kill off any other play back sources
            forceMusicStop();
            // 3. Register broadcast receiver for player intents
            setupBroadcastReceiver();
        }
        mediaPlayer.start();
        audioState = PLAYING;
    }
    private void pauseMediaPlayer(){
        if (mAudioFocusGranted && audioState==PLAYING) {
            mediaPlayer.pause();
            audioState= PAUSED;
            abandonAudioFocus();
        }
    }
    private void stopMediaPlayer(){
        if (mAudioFocusGranted && audioState==PLAYING) {
            mediaPlayer.stop();
            audioState=STOPPED;
            // 2. Give up audio focus
            abandonAudioFocus();
        }
    }



    //Hangling Changes In Audio Output
    final String AUDIO_FOCUS_TAG = "AudioFocusChange";
    private boolean mAudioFocusGranted = false;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_GAIN:
                    Log.i(AUDIO_FOCUS_TAG, "AUDIOFOCUS_GAIN");
                    if(audioState== PAUSED) continuePlaying();
                    else playAudio(currentAudio);
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                    Log.i(AUDIO_FOCUS_TAG, "AUDIOFOCUS_GAIN_TRANSIENT");
                    break;
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK:
                    Log.i(AUDIO_FOCUS_TAG, "AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK");
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    Log.e(AUDIO_FOCUS_TAG, "AUDIOFOCUS_LOSS");
                    pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    Log.e(AUDIO_FOCUS_TAG, "AUDIOFOCUS_LOSS_TRANSIENT");
                    pause();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    Log.e(AUDIO_FOCUS_TAG, "AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK");
                    break;
                case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                    Log.e(AUDIO_FOCUS_TAG, "AUDIOFOCUS_REQUEST_FAILED");
                    break;
                default:
                    Log.e(AUDIO_FOCUS_TAG, "AUDIOFOCUS default");
            }
        }
    };
    private boolean requestAudioFocus() {
        if (!mAudioFocusGranted) {
            AudioManager am = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            // Request audio focus for play back
            int result = am.requestAudioFocus(mOnAudioFocusChangeListener,
                    // Use the music stream.
                    AudioManager.STREAM_MUSIC,
                    // Request permanent focus.
                    AudioManager.AUDIOFOCUS_GAIN);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mAudioFocusGranted = true;
            } else {
                // FAILED
                Log.e("RequestAudioFocus",
                        ">>>>>>>>>>>>> FAILED TO GET AUDIO FOCUS <<<<<<<<<<<<<<<<<<<<<<<<");
            }
        }
        return mAudioFocusGranted;
    }

    private void abandonAudioFocus() {
        AudioManager am = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        int result = am.abandonAudioFocus(mOnAudioFocusChangeListener);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            mAudioFocusGranted = false;
        } else {
            // FAILED
            Log.e("AbandonAudioFocus",
                    ">>>>>>>>>>>>> FAILED TO ABANDON AUDIO FOCUS <<<<<<<<<<<<<<<<<<<<<<<<");
        }
        mOnAudioFocusChangeListener = null;
    }
    private void checkAudioFocus(){
        // 1. Acquire audio focus
        if (!mAudioFocusGranted && requestAudioFocus()) {
            // 2. Kill off any other play back sources
            forceMusicStop();
            // 3. Register broadcast receiver for player intents
            setupBroadcastReceiver();
        }
    }
    private void forceMusicStop() {
        AudioManager am = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        if (am.isMusicActive()) {
            Intent intentToStop = new Intent("com.sec.android.app.music.musicservicecommand");
            intentToStop.putExtra("command", "pause");
            context.sendBroadcast(intentToStop);
            audioState=NO_ACTION;
        }
    }
    private boolean mReceiverRegistered = false;
    private void setupBroadcastReceiver() {
        // Do the right thing when something else tries to play
        if (!mReceiverRegistered) {
            IntentFilter commandFilter = new IntentFilter();
            commandFilter.addAction(SERVICE_CMD);
            commandFilter.addAction(PAUSE_SERVICE_CMD);
            commandFilter.addAction(PLAY_SERVICE_CMD);
            context.registerReceiver(new BecomingNoisyReceiver(), commandFilter);
            mReceiverRegistered = true;
        }
    }
    private class BecomingNoisyReceiver extends BroadcastReceiver {
        ArgMusicService service = new ArgMusicService();
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String cmd = intent.getStringExtra(CMD_NAME);
            Log.i("BecomingNoisyReceiver", "intent.onReceive " + action + " / " + cmd);
            if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(action)) {
                new ArgMusicService().pause();
            }if (PAUSE_SERVICE_CMD.equals(action)
                    || (SERVICE_CMD.equals(action) && CMD_PAUSE.equals(cmd))) {
                if(audioState == PAUSE_CMD) service.continuePlaying();
            }

            if (PLAY_SERVICE_CMD.equals(action)
                    || (SERVICE_CMD.equals(action) && CMD_PLAY.equals(cmd))) {
                service.pause();
                audioState = PAUSE_CMD;
            }
        }
    }
}
