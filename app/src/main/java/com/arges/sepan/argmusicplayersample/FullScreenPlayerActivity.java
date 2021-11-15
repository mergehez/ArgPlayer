package com.arges.sepan.argmusicplayersample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arges.sepan.argmusicplayer.Models.ArgAudioList;
import com.arges.sepan.argmusicplayer.Models.ArgNotificationOptions;
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerFullScreenView;


public class FullScreenPlayerActivity extends AppCompatActivity {
    ArgPlayerFullScreenView musicPlayer;
    ArgAudioList playlist = new ArgAudioList(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_player);

        //tvError = (TextView)findViewById(R.id.tvError);
        //tvMusicType = (TextView)findViewById(R.id.tvMusicType);
        musicPlayer = (ArgPlayerFullScreenView) findViewById(R.id.argmusicplayer);
        playlist
                .add(MainActivity.audioRaw).add(MainActivity.audioAsset)
                .add(MainActivity.audioUrl3).add(MainActivity.audioUrl1)
                .add(MainActivity.audioUrl2).add(MainActivity.audioUrl4)
                .add(MainActivity.audioRaw).add(MainActivity.audioAsset)
                .add(MainActivity.audioUrl3).add(MainActivity.audioUrl5)
                .add(MainActivity.audioUrl6).add(MainActivity.audioAsset)
                .add(MainActivity.audioUrl1).add(MainActivity.audioUrl2)
                .add(MainActivity.audioUrl4).add(MainActivity.audioUrl3)
                .add(MainActivity.audioUrl5).add(MainActivity.audioRaw)
                .add(MainActivity.audioUrl6).add(MainActivity.audioUrl1)
                .add(MainActivity.audioUrl2).add(MainActivity.audioRaw)
                .add(MainActivity.audioUrl3).add(MainActivity.audioUrl4);

        musicPlayer.continuePlaylistWhenError();
        musicPlayer.playAudioAfterPercent(50);
        musicPlayer.setPlaylistRepeat(true);
        musicPlayer.enableNotification(new ArgNotificationOptions(this).setProgressEnabled(true));
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
        if(musicPlayer!=null)
            musicPlayer.pause();
    }
}
