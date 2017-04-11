package com.arges.sepan.argmusicplayersample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.arges.sepan.argmusicplayer.IndependentClasses.Arg;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudio;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudioList;
import com.arges.sepan.argmusicplayer.ArgPlayerLargeViewRoot;
import com.arges.sepan.argmusicplayer.Enums.ErrorType;
import com.arges.sepan.argmusicplayer.ArgPlayerSmallViewRoot;
import com.arges.sepan.argmusicplayer.Enums.AudioType;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
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
        if(v!=null)
            switch (v.getId()){
                case R.id.btnSmall: startActivity(new Intent(MainActivity.this,SmallPlayerActivity.class)); break;
                case R.id.btnLarge: startActivity(new Intent(MainActivity.this,LargePlayerActivity.class)); break;
                case R.id.btnFullScreen: startActivity(new Intent(MainActivity.this,FullScreenPlayerActivity.class)); break;
            }
    }






    private void allFunctions(){
        final ArgAudio myAudio = new ArgAudio("Singer","My Audio","symphony9",AudioType.RAW);
        final ArgAudioList myPlaylist = new ArgAudioList(true);
        final Arg.OnErrorListener listener1=null;
        final Arg.OnPreparedListener listener2=null;
        final Arg.OnPausedListener listener3=null;
        final Arg.OnPlayingListener listener4=null;
        final Arg.OnCompletedListener listener5=null;
        final Arg.OnTimeChangeListener listener6=null;
        final Arg.OnPlaylistAudioChangedListener listener7=null;
        final Class myClass = this.getClass();
        final ArgAudio audio = new ArgAudio("Singer","Large Mp3","http://www.url.of/music.mp3(m4a/wav etc..)", AudioType.URL);

        ArgPlayerSmallViewRoot small = new ArgPlayerSmallViewRoot(MainActivity.this);
        //-----------Methods-----------     ---Default---   ----------------------Description-----------------------
        small.playAudioAfterPercent(80);    //   %50        Audio will plays after %80 buffered. Only when audio type is Url
        small.disableProgress();            //    -         Disabling 'Loading' Progress View
        small.enableProgress();             //    +         Enabling 'Loading' Progress View
        small.setProgressMessage("...");    //   ...        Setting progress message (default: "Audio is loading..")
        small.disableErrorView();           //    -         Disabling Error View
        small.enableErrorView();            //    +         Enabling Error View
        small.enableNotification(myClass);  //    -         Enabling notification
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
