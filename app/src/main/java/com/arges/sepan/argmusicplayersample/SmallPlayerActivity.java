package com.arges.sepan.argmusicplayersample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudio;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudioList;
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerSmallView;

import java.util.Locale;

public class SmallPlayerActivity extends AppCompatActivity {
    ArgPlayerSmallView musicPlayer;
    AppCompatTextView tvError, tvMusicType;
    ArgAudio audioUrl,audioUrl2, audioRaw, audioAssets, audioFile;
    ArgAudioList playlist = new ArgAudioList(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_player);

        tvError = (AppCompatTextView) findViewById(R.id.tvError);
        tvMusicType = (AppCompatTextView) findViewById(R.id.tvMusicType);
        musicPlayer = (ArgPlayerSmallView) findViewById(R.id.argmusicplayer);

        audioAssets = ArgAudio.createFromAssets("Nîzamettîn Ariç", "Zînê", "zine.mp3");
        audioFile = ArgAudio.createFromFilePath("Andrea Bocelli", "Caruso", "/storage/emulated/0/Music/Andrea Bocelli Caruso.mp3");
        audioFile = ArgAudio.createFromFilePath("Awaz Baran", "Zara", "/storage/emulated/0/Music/zaragiyan.m4a");
        audioUrl = ArgAudio.createFromURL("Joan Baez", "North Country Blues", MainActivity.Url1);
        audioUrl2 = ArgAudio.createFromURL("Boney M.", "Rasputin", MainActivity.Url2);
        audioRaw = ArgAudio.createFromRaw("Joe Hisaishi", "Castle in the Sky", MainActivity.RawSong);
        ArgAudio audioUrl3 = ArgAudio.createFromURL("Şehîd Argeş", "Dara Jînê", MainActivity.Url3);
        ArgAudio audioUrl4 = ArgAudio.createFromURL("Vicor Jara", "La Partida", MainActivity.Url4);
        ArgAudio audioUrl5 = ArgAudio.createFromURL("Mark Kelly & Soraya", "Under The Jasmine Tree", MainActivity.Url5);
        ArgAudio audioUrl6 = ArgAudio.createFromURL("Koma Wetan", "Filîto Lawo", MainActivity.Url6);
        playlist.add(audioRaw).add(audioUrl).add(audioUrl2).add(audioUrl3).add(audioUrl4).add(audioUrl5).add(audioUrl6);//.add(audioAssets).add(audioFile);

        musicPlayer.enableNotification(SmallPlayerActivity.class);
        musicPlayer.setOnErrorListener((errorType, description)
                -> tvError.setText(String.format("Error:\nType: %s\nDescription: %s", errorType, description)));
        musicPlayer.setOnPlaylistAudioChangedListener((playlist, currentAudioIndex)
                -> tvMusicType.setText(String.format(Locale.getDefault(),"PLAYLIST %d: %s", playlist.getCurrentIndex(), playlist.getCurrentAudio().getTitle())));
        musicPlayer.setOnPlayingListener(()-> tvError.setText(""));
        musicPlayer.play(audioRaw);

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
            tvMusicType.setText(String.format("URL - %s", audioUrl.getTitle()));
            musicPlayer.play(audioUrl);
        });
        findViewById(R.id.btnRaw).setOnClickListener(v -> {
            tvMusicType.setText(String.format("RAW - %s", audioRaw.getTitle()));
            musicPlayer.play(audioRaw);
        });
        findViewById(R.id.btnAssets).setOnClickListener(v -> {
            tvMusicType.setText(String.format("ASSETS - %s", audioAssets.getTitle()));
            musicPlayer.play(audioAssets);
        });
        findViewById(R.id.btnFile).setOnClickListener(v -> {
            tvMusicType.setText(String.format("FILE PATH - %s", audioFile.getTitle()));
            musicPlayer.play(audioFile);
        });
        findViewById(R.id.btnPlaylist).setOnClickListener(v -> {
            musicPlayer.setPlaylistRepeat(true);
            musicPlayer.playPlaylist(playlist);
            tvMusicType.setText(String.format("PLAYLIST - %s", musicPlayer.getCurrentAudio().getTitle()));
        });
    }
}
