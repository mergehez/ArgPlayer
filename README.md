# ArgPlayer
An android music player ui library

Builds: [![](https://jitpack.io/v/mergehez/ArgPlayer.svg)](https://jitpack.io/#mergehez/ArgPlayer)

## Table of Contents
* [Gradle](#gradle)
* [How to Use](#how-to-use)
	* [XML Codes](#xml-codes)
		* [Small View](#for-small-view)
		* [Large View](#for-large-view)
		* [Full Screen View](#for-full-screen-view)
	* [Simplest Usage](#simplest-usage)
		* [Play Simple Audio](#play-simple-audio)
		* [Play Playlist](#play-playlist)
	* [Player Methods](#player-methods)
	* [ArgAudio Methods](#argaudio)
	* [ArgAudioList Methods](#argaudiolist)
* [ScreenShots](#screenshots)

## Gradle
To always build from the latest commit with all updates. Add the JitPack repository:

(path:\to\your\projects\MainFolderOfYourProject\build.gradle)
```

allprojects {
    repositories {
    	...
	maven { url "https://jitpack.io" }
    }
}
```
Make minSdkVersion 14 and add the following dependency:

```
android{
  ...
  defaultConfig{
    ...
    minSdkVersion 14
    ...
  }
}
dependencies {
    compile 'com.github.mergehez:ArgPlayer:master-SNAPSHOT'
}
```


## How to use

### XML Codes
#### for small view:
```xml
<com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerSmallView
	android:id="@+id/argmusicplayer"
	android:layout_width="match_parent"
	android:layout_height="wrap_content"/>
```
#### for large view:
```xml
<com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerLargeView
	android:id="@+id/argmusicplayer"
	android:layout_width="match_parent"
	android:layout_height="wrap_content"/>
```
#### for full screen view:
```xml
<com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerFullScreenView
	android:id="@+id/argmusicplayer"
	android:layout_width="match_parent"
	android:layout_height="wrap_content"/>
```

----
### Simplest Usage
#### Play Simple Audio
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
	//... other codes ...
	String url = "https://mergesoft.org/caruso.mp3";
	ArgAudio audio = new ArgAudio("Bocelli","Caruso",url,AudioType.URL);
	ArgPlayerSmallView argMusicPlayer = (ArgPlayerSmallView) findViewById(R.id.argmusicplayer);
	argMusicPlayer.play(audio);
	//... other codes ...
}
```
####  Play Playlist

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
	//... other codes ...
	ArgAudio audio1 = new ArgAudio("Singer1","Audio1",url,AudioType.URL);
	//Define audio2, audio3, audio4 ......
	ArgAudioList playlist = new ArgAudioList(true).add(audio1)
						.add(audio2)
						.add(audio3)
						.add(audio4);
	ArgPlayerSmallView argMusicPlayer = (ArgPlayerSmallView) findViewById(R.id.argmusicplayer);
	argMusicPlayer.playPlaylist(playlist );
	//... other codes ...
}
```
----



#### Player Methods
| Return | Method/Description |
| :-- |:--|
| void |`play(ArgAudio audio)` <br>Directly play an audio|
| void |`playPlaylist(ArgAudioList list)` <br>Directly play a playlist|
| void |`loadSingleAudio(ArgAudio audio)` <br>Load an audio to play later|
| void |`playLoadedSingleAudio()` <br>Play the loaded audio if exists|
| void |`loadPlaylist(ArgAudioList list)` <br>Load a playlist to play later|
| void |`playLoadedPlaylist()` <br>Play the loaded playlist if exists|
| void |`pause()` <br>Pause a playing audio|
| void |`resume()` <br>Resume the playing audio|
| void |`stop()` <br>Stop audio|
| boolean |`seekTo(int millisec)` <br>Seek audio to *milliSec*<br> **return**: if seeking position is greater than duration or less than 0, it returns *false*|
| boolean |`forward(int milliSec, boolean willPlay)` <br>Forward audio as long as *milliSec*<br>*`willPlay:`* if audio will play after forwarding, set this *true*<br> **return:** If forwarding position is greater than duration, it returns *false*|
| boolean |`backward(int milliSec, boolean willPlay)` <br>Backward audio as long as *milliSec*<br>*`willPlay:`* if audio will play after backwarding, set this *true*<br> **return:** If backwarding position is less than 0, it returns *false*|
| ArgAudio |`getCurrentAudio()` <br>Get the current audio if available|
| long|`getDuration()` <br>Get duration of current audio|
| boolean|`isPlaying()` <br>Check if an audio is playing|
| boolean|`isPaused()` <br>Check if an audio is paused|
| void |`playAudioAfterPercent(int percent)` <br>Audio will plays after %`percent` buffered. Only when audio type is Url. *Default percent is **%50**.* | 
| void |`enableProgress()` and `disableProgress()` <br> Enable/Disable Progress View. *Defaultly is **enabled***|
| void |`setProgressMessage(String message)` <br> Change Progress View message. *Default message is **'Audio is Loading..'***|
| void |`enableErrorView()` and `disableErrorView()` <br>Enable/Disable Error View. Error view appears when an error has occured. *Defaultly is **enabled*** |
| void |`enableNextPrevButtons()`and `disableNextPrevButtons()` <br> Use can disable next/previous playback control but *Defaultly is **enabled***|
| void |`enableNotification(Class activityClass)` <br>Enable notification. With this option users will be able to control music player on notification panel. Look at screenshots.. |
| void |`enableNotification(Class homeClass, int notifImgResId)` <br>Enable notification. With this option users will be able to control music player on notification panel. Look at screenshots..<br>*`homeClass:`* When click on notification home will open.<br>*`notifImgResId:`* Resource id for ImageView on the notification layout.  |
| void |`disableNotification()` <br> Disable notification option. If notification was enabled before, it will be cancelled. *Notification defaultly is **disabled***|
| void |`continuePlaylistWhenError()` <br>If an error occures, player won't publish error and will play next audio. |
| void |`stopPlaylistWhenError()` <br>If an error occures, player will stop playing and publish error. *Defaultly **player publishes errors***.|
| void |`setPlaylistRepeat(boolean repeat)` <br>Set repetition of the playlist. *Default value is **true***|
| void |`setAllButtonsImageResource(int resIdPlay,int resIdPause,int resIdPrev,int resIdNext,int resIdRepeat,int resIdNotRepeat)` <br> You can change image resources of all control buttons by this method.|
| void |`setPrevButtonImageResource(int resId)` <br> Change only image resource of the *previous* control button|
| void |`setNextButtonImageResource(int resId)` <br>Change only image resource of the *next* control button|
| void |`setPlayButtonImageResource(int resId)` <br>Change only image resource of the *play* control button|
| void |`setPauseButtonImageResource(int resId)` <br>Change only image resource of the *pause* control button|
| void |`setRepeatButtonImageResource(int repeatResId, int notRepeatResId)` <br>Change only image resource of the *repeat* control button.<br> *`repeatResId`*: when repeatition is enabled <br> *`notRepeatResId`*: when repeatition is disabled|
| void |`setOnErrorListener(Arg.OnErrorListener onErrorListener)` <br> `setOnPreparedListener(Arg.OnPreparedListener onPreparedListener)` <br> `setOnPausedListener(Arg.OnPausedListener onPausedListener)` <br> `setOnPlayingListener(Arg.OnPlayingListener onPlayingListener)` <br>`setOnCompletedListener(Arg.OnCompletedListener onCompletedListener)` <br>`setOnTimeChangeListener(Arg.OnTimeChangeListener onTimeChangeListener)` <br>`setOnPlaylistAudioChangedListener(Arg.OnPlaylistAudioChangedListener onPlaylistAudioChangedListener)` <br> Set listeners to handle broadcasts|
|||


#### ArgAudio
| Return | Method/Description |
| :-- |:--|
| ArgAudio |*`static`*`createFromRaw(@RawRes int rawId)` <br> Short way to create an audio from raw resources. Singer an audio name will be defined as *rawId*.|
| ArgAudio |*`static`*`createFromRaw(String singer, String audioName, @RawRes int rawId)` <br>Create a raw type audio.|
| ArgAudio|*`static`*`createFromAssets(String assetName)` <br>Short way to create an audio from raw resources. Singer an audio name will be defined as *assetName*.|
| ArgAudio|*`static`*`createFromAssets(String singer, String audioName, String assetName)` <br>Create an assets type audio.|
| ArgAudio|*`static`*`createFromURL(String url)` <br>Short way to create an audio from raw resources. Singer an audio name will be defined as *url*.|
| ArgAudio|*`static`*`createFromURL(String singer, String audioName,  String url)` <br>Create an url type audio.|
| ArgAudio|*`static`*`createFromFilePath(String filePath)` <br>Create a filepath type audio.|
| ArgAudio|*`static`*`createFromFilePath(String singer, String audioName, String filePath)` <br>Short way to create an audio from raw resources. Singer an audio name will be defined as *filePath*.|
| ArgAudio|`cloneAudio()` <br>Clone the audio.|
| ArgAudio|`makePlaylist()` <br> Make audio as a playlist audio.|
| boolean|`isPlaylist()` <br> Check if the audio is a playlist audio.|
|||
#### ArgAudioList
| Return | Method/Description |
| :-- |:--|
| ArgAudioList|`add(@NonNull ArgAudio audio)` <br>Add an ArgAudio|
| ArgAudio |`getCurrentAudio()` <br> Get current(playing) audio.|
| int|`getCurrentIndex()` <br>Get index of current audio.|
| boolean|`equals(Object obj)` <br> Check if another ArgAudioList object is equals current one. Method checks emptyness, equalness of first and last childs of objects and sizes. |
| void |`goTo(int index)` <br> Change playing audio of playlist.|
| boolean|`hasNext()` <br>Check if any next audio|
| int|`getNextIndex()` <br> Get index of next audio.|
| boolean|`goToNext()` <br> Set next audio as playing audio. If this action is not possible, method returns false.|
| boolean|`hasPrev()` <br>Check if any previous audio|
| int|`getPrevIndex()` <br> Get index of previous audio.|
| boolean|`goToPrev()` <br> Set previous audio as playing audio. If this action is not possible, method returns false.|
|||






## ScreenShots
### Progress View
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/progressview.png "")
### Error View
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/errorview.png "")
### Notification
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/notification.png "")
### Small Player View
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/smallview.png "")
### Player Views ScreenShots
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/s.png "")
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/largeview.png "")
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/fullscreen1.png "")
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/fullscreen2.png "")
