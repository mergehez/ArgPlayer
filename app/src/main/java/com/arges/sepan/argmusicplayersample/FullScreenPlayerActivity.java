package com.arges.sepan.argmusicplayersample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudio;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudioList;
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerFullScreenView;


public class FullScreenPlayerActivity extends AppCompatActivity {
    ArgPlayerFullScreenView musicPlayer;
    ArgAudio audioUrl,audioUrl2,audioRaw,audioAssets,audioFile,audioFile2;
    ArgAudioList playlist = new ArgAudioList(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_player);

        //tvError = (TextView)findViewById(R.id.tvError);
        //tvMusicType = (TextView)findViewById(R.id.tvMusicType);
        musicPlayer = (ArgPlayerFullScreenView) findViewById(R.id.argmusicplayer);

        audioAssets = ArgAudio.createFromAssets("Nîzamettîn Ariç", "Zînê", "zine.mp3");
        audioFile = ArgAudio.createFromFilePath("Andrea Bocelli", "Caruso", "/storage/emulated/0/Music/Andrea Bocelli Caruso.mp3");
        audioFile2 = ArgAudio.createFromFilePath("Awaz Baran", "Zara", "/storage/emulated/0/Music/zaragiyan.m4a");
        audioUrl = ArgAudio.createFromURL("Joan Baez", "North Country Blues", MainActivity.Url1);
        audioUrl2 = ArgAudio.createFromURL("Boney M.", "Rasputin", MainActivity.Url2);
        ArgAudio audioUrl3 = ArgAudio.createFromURL("Şehîd Argeş", "Dara Jînê", MainActivity.Url3);
        ArgAudio audioUrl4 = ArgAudio.createFromURL("Vicor Jara", "La Partida", MainActivity.Url4);
        ArgAudio audioUrl5 = ArgAudio.createFromURL("Mark Kelly & Soraya", "Under The Jasmine Tree", MainActivity.Url5);
        ArgAudio audioUrl6 = ArgAudio.createFromURL("Koma Wetan", "Filîto Lawo", MainActivity.Url6);
        audioRaw = ArgAudio.createFromRaw("Joe Hisaishi", "Castle in the Sky", MainActivity.RawSong);
        playlist
                .add(audioRaw).add(audioUrl3).add(audioUrl).add(audioUrl2).add(audioUrl4)
                .add(audioRaw).add(audioUrl3).add(audioUrl5).add(audioUrl6)
                .add(audioUrl).add(audioUrl2).add(audioUrl4).add(audioUrl3)
                .add(audioUrl5).add(audioRaw).add(audioUrl6).add(audioUrl)
                .add(audioUrl2).add(audioRaw).add(audioUrl3).add(audioUrl4);
        musicPlayer.continuePlaylistWhenError();
        musicPlayer.playAudioAfterPercent(50);
        musicPlayer.setPlaylistRepeat(true);
        musicPlayer.enableNotification(FullScreenPlayerActivity.class);
        musicPlayer.setOnErrorListener((errorType, description) -> Toast.makeText(FullScreenPlayerActivity.this,"Error:\nType: "+errorType+"\nDescription: "+description,Toast.LENGTH_LONG).show());
        musicPlayer.playPlaylist(playlist);

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
}
