package com.arges.sepan.argmusicplayersample;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.arges.sepan.argmusicplayer.Enums.ErrorType;
import com.arges.sepan.argmusicplayer.IndependentClasses.Arg;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudio;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudioList;
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerFullScreenView;


public class FullScreenPlayerActivity extends AppCompatActivity {
    String url1 = "http://www.noiseaddicts.com/samples_1w72b820/2563.mp3";   // 7.5 mb
    String url2 = "http://mergesoft.org/thesis/argmusicplayer/binkssake.mp3";   // 1.5 mb
    String url3 = "http://www.noiseaddicts.com/samples_1w72b820/4250.mp3";   // 0.5 mb
    int res1 = R.raw.nausicaa_requiem;
    ArgPlayerFullScreenView musicPlayer;
    //TextView tvError, tvMusicType;

    ArgAudio audioUrl,audioRaw,audioAssets,audioFile,audioFile2;
    ArgAudioList playlist = new ArgAudioList(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_player);

        //tvError = (TextView)findViewById(R.id.tvError);
        //tvMusicType = (TextView)findViewById(R.id.tvMusicType);
        musicPlayer = (ArgPlayerFullScreenView) findViewById(R.id.argmusicplayer);

        audioUrl = ArgAudio.createFromURL("One Piece","Binks Sake",url1);
        audioRaw = ArgAudio.createFromRaw("Joe Hisaishi","Requiem", res1);
        audioAssets = ArgAudio.createFromAssets("Nîzamettîn Ariç","Zînê","zine.mp3");
        audioFile2 = ArgAudio.createFromFilePath("Andrea Bocelli","Caruso","/storage/emulated/0/Music/Andrea Bocelli Caruso.mp3");
        audioFile = ArgAudio.createFromFilePath("Awaz Baran","Zara","/storage/emulated/0/Music/zaragiyan.m4a");
        playlist
                .add(audioRaw) .add(audioAssets) .add(audioFile)
                .add(audioRaw) .add(audioUrl)   .add(audioAssets) .add(audioFile2)
                .add(audioRaw) .add(audioFile2) .add(audioAssets) .add(audioFile)
                .add(audioRaw) .add(audioFile2) .add(audioAssets) .add(audioFile)
                .add(audioRaw) .add(audioFile2) .add(audioAssets) .add(audioFile)
                .add(audioRaw) .add(audioFile2) .add(audioAssets) .add(audioFile);
        musicPlayer.continuePlaylistWhenError();
        musicPlayer.playAudioAfterPercent(50);
        musicPlayer.setPlaylistRepeat(true);
        musicPlayer.enableNotification(FullScreenPlayerActivity.class);
        musicPlayer.setOnErrorListener(new Arg.OnErrorListener() {
            @Override
            public void onError(ErrorType errorType, String description) {
                Toast.makeText(FullScreenPlayerActivity.this,"Error:\nType: "+errorType+"\nDescription: "+description,Toast.LENGTH_LONG).show();
            }
        });
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
