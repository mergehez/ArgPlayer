package com.arges.sepan.argmusicplayersample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.arges.sepan.argmusicplayer.Models.ArgAudioList;
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerSmallView;

import java.util.Locale;

public class SmallPlayerActivity extends AppCompatActivity {
    ArgPlayerSmallView musicPlayer;
    AppCompatTextView tvError, tvMusicType;
    ArgAudioList playlist = new ArgAudioList(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_player);

        tvError = (AppCompatTextView) findViewById(R.id.tvError);
        tvMusicType = (AppCompatTextView) findViewById(R.id.tvMusicType);
        musicPlayer = (ArgPlayerSmallView) findViewById(R.id.argmusicplayer);

        playlist.add(MainActivity.audioRaw)
                .add(MainActivity.audioAsset)
                .add(MainActivity.audioUrl1)
                .add(MainActivity.audioUrl2)
                .add(MainActivity.audioUrl3)
                .add(MainActivity.audioUrl4)
                .add(MainActivity.audioUrl5)
                .add(MainActivity.audioUrl6);//.add(audioFile);

        musicPlayer.enableNotification(this);
        musicPlayer.setOnErrorListener((errorType, description)
                -> tvError.setText(String.format("Error:\nType: %s\nDescription: %s", errorType, description)));
        musicPlayer.setOnPlaylistAudioChangedListener((playlist, currentAudioIndex)
                -> tvMusicType.setText(String.format(Locale.getDefault(),"PLAYLIST %d: %s", playlist.getCurrentIndex(), playlist.getCurrentAudio().getTitle())));
        musicPlayer.setOnPlayingListener(()-> tvError.setText(""));
        musicPlayer.play(MainActivity.audioRaw);

        initBtns();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //if(musicPlayer!=null) musicPlayer.pause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(musicPlayer!=null) musicPlayer.pause();
    }

    public void initBtns(){

        findViewById(R.id.btnUrl).setOnClickListener(v -> {
            tvMusicType.setText(String.format("URL - %s", MainActivity.audioUrl1.getTitle()));
            musicPlayer.play(MainActivity.audioUrl1);
        });
        findViewById(R.id.btnRaw).setOnClickListener(v -> {
            tvMusicType.setText(String.format("RAW - %s", MainActivity.audioRaw.getTitle()));
            musicPlayer.play(MainActivity.audioRaw);
        });
        findViewById(R.id.btnAssets).setOnClickListener(v -> {
            tvMusicType.setText(String.format("ASSETS - %s", MainActivity.audioAsset.getTitle()));
            musicPlayer.play(MainActivity.audioAsset);
        });
        findViewById(R.id.btnFile).setOnClickListener(v -> {
            tvMusicType.setText(String.format("FILE PATH - %s", MainActivity.audioFile.getTitle()));
            musicPlayer.play(MainActivity.audioFile);
        });
        findViewById(R.id.btnPlaylist).setOnClickListener(v -> {
            musicPlayer.setPlaylistRepeat(true);
            musicPlayer.playPlaylist(playlist);
            tvMusicType.setText(String.format("PLAYLIST - %s", musicPlayer.getCurrentAudio().getTitle()));
        });
    }
}
