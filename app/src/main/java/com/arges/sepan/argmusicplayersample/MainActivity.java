package com.arges.sepan.argmusicplayersample;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.arges.sepan.argmusicplayer.ArgPlayerSmallViewRoot;
import com.arges.sepan.argmusicplayer.Callbacks.OnCompletedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnErrorListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPausedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPlayingListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPlaylistAudioChangedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnPreparedListener;
import com.arges.sepan.argmusicplayer.Callbacks.OnTimeChangeListener;
import com.arges.sepan.argmusicplayer.Enums.AudioType;
import com.arges.sepan.argmusicplayer.Models.ArgAudio;
import com.arges.sepan.argmusicplayer.Models.ArgAudioList;
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerSmallView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String Url1 = "https://www.gotinenstranan.com/songs/joan-baez-north-country-blues.mp3";   // 2.3 mb
    public static final String Url2 = "https://www.gotinenstranan.com/songs/boney-m-rasputin.mp3";   // 4.2 mb
    public static final String Url3 = "https://www.gotinenstranan.com/songs/s%CC%A7ehi%CC%82d%20arges%CC%A7%20-%20dara%20ji%CC%82ne%CC%82.mp3";   // 1.7 mb
    public static final String Url4 = "https://www.gotinenstranan.com/songs/victor%20jara%20-%20la%20partida.mp3";   // 1.5 mb
    public static final String Url5 = "https://www.gotinenstranan.com/songs/mark%20kelly%20%26%20soraya%20-%20under%20the%20jasmine%20tree.mp3";   // 1.5 mb
    public static final String Url6 = "https://www.gotinenstranan.com/songs/koma%20wetan%20-%20fili%CC%82to%20lao.mp3";   // 2.1 mb
    public static final int RawSong = R.raw.castle_in_the_sky;
    public static final String AssetSong = "koma gulên xerzan carek.mp3";

    public static ArgAudio audioUrl1 = ArgAudio.createFromURL("Joan Baez", "North Country Blues", Url1);
    public static ArgAudio audioUrl2 = ArgAudio.createFromURL("Boney M.", "Rasputin", MainActivity.Url2);
    public static ArgAudio audioUrl3 = ArgAudio.createFromURL("Şehîd Argeş", "Dara Jînê", MainActivity.Url3);
    public static ArgAudio audioUrl4 = ArgAudio.createFromURL("Vicor Jara", "La Partida", MainActivity.Url4);
    public static ArgAudio audioUrl5 = ArgAudio.createFromURL("Mark Kelly & Soraya", "Under The Jasmine Tree", MainActivity.Url5);
    public static ArgAudio audioUrl6 = ArgAudio.createFromURL("Koma Wetan", "Filîto Lawo", MainActivity.Url6);
    public static ArgAudio audioRaw = ArgAudio.createFromRaw("Joe Hisaishi", "Castle in the Sky", MainActivity.RawSong);
    public static ArgAudio audioAsset = ArgAudio.createFromAssets("Koma Gulên Xerzan", "Carek", MainActivity.AssetSong);
    public static ArgAudio audioFile = ArgAudio.createFromFilePath("Andrea Bocelli", "Caruso", "/storage/emulated/0/Music/Andrea Bocelli Caruso.mp3");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        findViewById(R.id.btnSmall).setOnClickListener(this);
        findViewById(R.id.btnLarge).setOnClickListener(this);
        findViewById(R.id.btnFullScreen).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v!=null){
            int id = v.getId();
            if(id== R.id.btnSmall)
                startActivity(new Intent(MainActivity.this,SmallPlayerActivity.class));
            else if(id == R.id.btnLarge)
                startActivity(new Intent(MainActivity.this,LargePlayerActivity.class));
            else if(id == R.id.btnFullScreen)
                startActivity(new Intent(MainActivity.this,FullScreenPlayerActivity.class));
        }
    }


    private void allFunctions(){
        final ArgAudio myAudio = new ArgAudio("Singer","My Audio","symphony9",AudioType.RAW);
        final ArgAudioList myPlaylist = new ArgAudioList(true);
        final OnErrorListener listener1=null;
        final OnPreparedListener listener2=null;
        final OnPausedListener listener3=null;
        final OnPlayingListener listener4=null;
        final OnCompletedListener listener5=null;
        final OnTimeChangeListener listener6=null;
        final OnPlaylistAudioChangedListener listener7=null;
        final ArgAudio audio = new ArgAudio("Singer","Large Mp3","http://www.url.of/music.mp3(m4a/wav etc..)", AudioType.URL);

        ArgPlayerSmallViewRoot small = new ArgPlayerSmallView(MainActivity.this);
        //-----------Methods-----------     ---Default---   ----------------------Description-----------------------
        small.playAudioAfterPercent(80);    //   %50        Audio will plays after %80 buffered. Only when audio type is Url
        small.disableProgress();            //    -         Disabling 'Loading' Progress View
        small.enableProgress();             //    +         Enabling 'Loading' Progress View
        small.setProgressMessage("...");    //   ...        Setting progress message (default: "Audio is loading..")
        small.disableErrorView();           //    -         Disabling Error View
        small.enableErrorView();            //    +         Enabling Error View
        small.enableNotification(this);  //    -         Enabling notification
        small.disableNotification();        //    +         Disabling notification
        small.enableNextPrevButtons();      //    +         Show prev/next buttons
        small.disableNextPrevButtons();     //    -         Do not show prev/next buttons
        small.continuePlaylistWhenError();  //    -         If an error occures, player will play next audio
        small.stopPlaylistWhenError();      //    +         If an error occures, player will stop playing and publish error
        small.setPlaylistRepeat(true);      //   true       Set repetition of the playlist

        //-----------Methods-----------     -----------Description----------------
        small.loadSingleAudio(myAudio);     // Load an audio to play later
        small.playLoadedSingleAudio();      // Play the loaded audio if exists
        small.loadPlaylist(myPlaylist);     // Load a playlist to play later
        small.playLoadedPlaylist();         // Play the loaded playlist if exists
        small.play(audio);                  // Directly play an audio
        small.playPlaylist(myPlaylist);     // Directly play a playlist
        small.pause();                      // Pause a playing audio
        small.seekTo(10000);                // Seek audio to 10th second
        small.stop();                       // Stop audio
        small.getCurrentAudio();            // Get the current audio if available
        small.isPlaying();                  // Check if an audio is playing
        small.isPaused();                   // Check if an audio has paused
        small.getDuration();                // Get the current duration of the audio

        //-----------Methods-----------                 -----------Description----------------
        small.setAllButtonsImageResource(0,0,0,0,0,0);  // Change Image of playback controls
        small.setNextButtonImageResource(0);            // Change Image of the next audio button
        small.setPrevButtonImageResource(0);            // Change image of the previous audio button
        small.setPauseButtonImageResource(0);           // Change image of the pause audio button
        small.setPlayButtonImageResource(0);            // Change image of the play audio button
        small.setRepeatButtonImageResource(0,0);        // Change image of the stop audio button

        //-----------Methods-----------                     -----------Description----------------
        small.setOnErrorListener(listener1);                // Gets broadcast when an error occured
        small.setOnPreparedListener(listener2);             // Gets broadcast when an audio is ready for playing
        small.setOnPausedListener(listener3);               // Gets broadcast when an audio paused
        small.setOnPlayingListener(listener4);              // Gets broadcast when an audio started playing
        small.setOnCompletedListener(listener5);            // Gets broadcast when an audio complete playing
        small.setOnTimeChangeListener(listener6);           // Gets broadcast when an audio's time is changed
        small.setOnPlaylistAudioChangedListener(listener7); // Gets broadcast when the playing audio of playlist is changed

    }

}
