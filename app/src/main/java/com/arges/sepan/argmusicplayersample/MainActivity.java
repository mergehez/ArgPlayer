package com.arges.sepan.argmusicplayersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arges.sepan.argmusicplayer.ArgAudio;
import com.arges.sepan.argmusicplayer.ArgAudioList;
import com.arges.sepan.argmusicplayer.ArgListener;
import com.arges.sepan.argmusicplayer.ArgMusicPlayer;
import com.arges.sepan.argmusicplayer.ArgMusicPlayerSmallView;
import com.arges.sepan.argmusicplayer.ArgMusicService;
import com.arges.sepan.argmusicplayer.AudioType;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String url1 = "http://www.noiseaddicts.com/samples_1w72b820/2563.mp3";   // 7.5 mb
    String url2 = "http://mergesoft.org/thesis/argmusicplayer/binkssake.mp3";   // 1.5 mb
    String url3 = "http://www.noiseaddicts.com/samples_1w72b820/4250.mp3";   // 0.5 mb
    int res1 = R.raw.nausicaa_requiem;
    ArgMusicPlayerSmallView musicPlayer;
    TextView tvError, tvMusicType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvError = (TextView)findViewById(R.id.tvError);
        tvMusicType = (TextView)findViewById(R.id.tvMusicType);
        musicPlayer = (ArgMusicPlayerSmallView) findViewById(R.id.argmusicplayer);

        final ArgAudio audioUrl = ArgAudio.createFromURL("Binks Sake",url2);
        final ArgAudio audioRaw = ArgAudio.createFromRaw("Requiem", res1);
        final ArgAudio audioAssets = ArgAudio.createFromAssets("Zînê","zine.mp3");
        final ArgAudio audioFile = ArgAudio.createFromFilePath("Caruso","/storage/emulated/0/Music/Andrea Bocelli Caruso.mp3");

        final ArgAudioList playlist = new ArgAudioList();
        playlist.add(audioRaw)
                .add(audioUrl)
                .add(audioAssets)
                .add(audioFile);

        musicPlayer.playAudioAfterPercent(50);
        musicPlayer.setOnErrorListener(new ArgListener.OnErrorListener() {
            @Override
            public void onError(ArgMusicPlayer.ErrorType errorType, String description) {
                tvError.setText("Error:\nType: "+errorType+"\nDescription: "+description);
            }
        });
        musicPlayer.setOnPlaylistAudioChangedListener(new ArgListener.OnPlaylistAudioChangedListener() {
            @Override
            public void onPlaylistAudioChanged(ArgAudioList playlist, int currentAudioIndex) {
                tvMusicType.setText(String.format("PLAYLIST Current Audio: %s", playlist.getCurrentAudio().getTitle()));
            }
        });

        findViewById(R.id.btnUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMusicType.setText(String.format("URL - %s", audioUrl.getTitle()));
                musicPlayer.play(audioUrl);
            }
        });
        findViewById(R.id.btnRaw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMusicType.setText(String.format("RAW - %s", audioRaw.getTitle()));
                musicPlayer.play(audioRaw);
            }
        });
        findViewById(R.id.btnAssets).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMusicType.setText(String.format("ASSETS - %s", audioAssets.getTitle()));
                musicPlayer.play(audioAssets);
            }
        });
        findViewById(R.id.btnFile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMusicType.setText(String.format("FILE PATH - %s", audioFile.getTitle()));
                musicPlayer.play(audioFile);
            }
        });
        findViewById(R.id.btnPlaylist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicPlayer.setOnPlaylistAudioChangedListener(new ArgListener.OnPlaylistAudioChangedListener() {
                    @Override
                    public void onPlaylistAudioChanged(ArgAudioList playlist, int currentAudioIndex) {
                        tvMusicType.setText(String.format("PLAYLIST Current Audio: %s", playlist.get(0).getTitle()));
                    }
                });
                musicPlayer.repeatingPlaylist(true);
                musicPlayer.playPlaylist(playlist);
            }
        });
        //musicPlayer.play(audioUrl);














        new Button(this).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArgAudio audio = new ArgAudio("Large Mp3",url1, AudioType.URL);
                ArgMusicPlayerSmallView small = new ArgMusicPlayerSmallView(MainActivity.this);
                small.setProgressMessage("MainProgressMsg");
                small.playAudioAfterPercent(80);
                small.disableProgress();
                small.enableProgress();
                small.disableErrorView();
                small.enableErrorView();
                small.disablePlaylistControls();
                small.enablePlaylistControls();
                small.play(audio);
                small.pause();
                small.seekTo(10000);// 10th second
                small.stop();
                small.getCurrentAudio();
                small.isPlaying();
                small.isPaused();
                small.setOnErrorListener(null);
                small.setOnPreparedListener(null);
                small.setOnPausedListener(null);
                small.setOnPlayingListener(null);
                small.setOnCompletedListener(null);
                small.setOnTimeChangeListener(null);
                small.getDuration();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //if(ScreenReceiver.wasScreenOn && musicPlayer!=null) musicPlayer.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(musicPlayer!=null) musicPlayer.stop();
    }
}
