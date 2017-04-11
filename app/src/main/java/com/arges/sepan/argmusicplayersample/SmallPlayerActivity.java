package com.arges.sepan.argmusicplayersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.arges.sepan.argmusicplayer.Enums.ErrorType;
import com.arges.sepan.argmusicplayer.IndependentClasses.Arg;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudio;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudioList;
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerSmallView;

public class SmallPlayerActivity extends AppCompatActivity {
    String url1 = "http://www.noiseaddicts.com/samples_1w72b820/2563.mp3";   // 7.5 mb
    String url2 = "http://mergesoft.org/thesis/argmusicplayer/binkssake.mp3";   // 1.5 mb
    String url3 = "http://www.noiseaddicts.com/samples_1w72b820/4250.mp3";   // 0.5 mb
    int res1 = R.raw.nausicaa_requiem;
    ArgPlayerSmallView musicPlayer;
    TextView tvError, tvMusicType;

    ArgAudio audioUrl,audioRaw,audioAssets,audioFile;
    ArgAudioList playlist = new ArgAudioList(true);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_player);

        tvError = (TextView)findViewById(R.id.tvError);
        tvMusicType = (TextView)findViewById(R.id.tvMusicType);
        musicPlayer = (ArgPlayerSmallView) findViewById(R.id.argmusicplayer);

        audioUrl = ArgAudio.createFromURL("One Piece","Binks Sake",url1);
        audioRaw = ArgAudio.createFromRaw("Joe Hisaishi","Requiem", res1);
        audioAssets = ArgAudio.createFromAssets("Nîzamettîn Ariç","Zînê","zine.mp3");
        audioFile = ArgAudio.createFromFilePath("Andrea Bocelli","Caruso","/storage/emulated/0/Music/Andrea Bocelli Caruso.mp3");
        audioFile = ArgAudio.createFromFilePath("Awaz Baran","Zara","/storage/emulated/0/Music/zaragiyan.m4a");
        playlist .add(audioRaw) .add(audioUrl) .add(audioAssets) .add(audioFile);

        musicPlayer.enableNotification(SmallPlayerActivity.class);
        musicPlayer.setOnErrorListener(new Arg.OnErrorListener() {
            @Override
            public void onError(ErrorType errorType, String description) {
                tvError.setText("Error:\nType: "+errorType+"\nDescription: "+description);
            }
        });
        musicPlayer.setOnPlaylistAudioChangedListener(new Arg.OnPlaylistAudioChangedListener() {
            @Override
            public void onPlaylistAudioChanged(ArgAudioList playlist, int currentAudioIndex) {
                tvMusicType.setText(String.format("PLAYLIST Audio%d: %s", playlist.getCurrentIndex(), playlist.getCurrentAudio().getTitle()));
            }
        });
        musicPlayer.play(audioFile);

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
                musicPlayer.setPlaylistRepeat(true);
                musicPlayer.playPlaylist(playlist);
            }
        });
    }
}
