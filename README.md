# ArgPlayer
An android music player ui library

Builds: [![](https://jitpack.io/v/mergehez/ArgPlayer.svg)](https://jitpack.io/#mergehez/ArgPlayer)

## Table of Contents
* [Installation](#installation)
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
	* [ArgNotificationOptions Methods](#argnotificationoptions)
* [ScreenShots](#screenshots)

## Installation
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
Set minSdkVersion to 21 and add the following dependency:

```
android{
  ...
  defaultConfig{
    ...
    minSdkVersion 21
    ...
  }
}
dependencies {
    implementation 'com.github.mergehez:ArgPlayer:v3.1'
}
```

> Note: You may encounter the error below. See this stackoverflow question: [link](https://stackoverflow.com/questions/69163511/build-was-configured-to-prefer-settings-repositories-over-project-repositories-b) <br/>
> `Build was configured to prefer settings repositories over project repositories but repository 'maven' was added by build file 'build.gradle'`

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
	String url = "https://www.gotinenstranan.com/songs/joan-baez-north-country-blues.mp3";
	ArgAudio audio = ArgAudio.createFromURL("Joan Baez", "North Country Blues", url);

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
	ArgAudio audio1 = ArgAudio.createFromURL("Joan Baez", "North Country Blues", url1)
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
| void |`play(ArgAudio audio)` <br>Directly plays an audio|
| void |`playPlaylist(ArgAudioList list)` <br>Directly plays a playlist|
| void |`loadSingleAudio(ArgAudio audio)` <br>Loads an audio to play later|
| void |`playLoadedSingleAudio()` <br>Plays the loaded audio if exists|
| void |`loadPlaylist(ArgAudioList list)` <br>Loads a playlist to play later|
| void |`playLoadedPlaylist()` <br>Plays the loaded playlist if exists|
| void |`pause()` <br>Pauses a playing audio|
| void |`resume()` <br>Resumes the playing audio|
| void |`stop()` <br>Stops audio|
| boolean |`seekTo(int millisec)` <br>Seeks audio to *milliSec*<br> **return**: if seeking position is greater than duration or less than 0, it returns *false*|
| boolean |`forward(int milliSec, boolean willPlay)` <br>Forward audio as long as *milliSec*<br>*`willPlay:`* if audio will play after forwarding, set this *true*<br> **return:** If forwarding position is greater than duration, it returns *false*|
| boolean |`backward(int milliSec, boolean willPlay)` <br>Backward audio as long as *milliSec*<br>*`willPlay:`* if audio will play after backwarding, set this *true*<br> **return:** If backwarding position is less than 0, it returns *false*|
| ArgAudio |`getCurrentAudio()` <br>Gets the current audio if available|
| long|`getDuration()` <br>Gets duration of current audio|
| boolean|`isPlaying()` <br>Checks if an audio is playing|
| boolean|`isPaused()` <br>Checks if an audio is paused|
| void |`playAudioAfterPercent(int percent)` <br>Audio plays as soon as %`percent` is buffered. Only when audio type is Url. *Default percent is **%50**.* |
| void |`enableProgress()` and `disableProgress()` <br> Enable/Disable Progress View. *As default is **enabled***|
| void |`setProgressMessage(String message)` <br> Change Progress View message. *Default message is **'Audio is Loading..'***|
| void |`enableErrorView()` and `disableErrorView()` <br>Enables/Disables Error View. Error view appears when an error occurs. *Default value is **enabled*** |
| void |`enableNextPrevButtons()`and `disableNextPrevButtons()` <br> Enables/disables next/previous playback control. *Default behavior is **enabled***|
| void |`enableNotification(Activity activity)` <br>Enables notification.  Look at screenshots..<br>*`activity:`* Current activity (You'd probably pass **this** keyword). |
| void |`enableNotification(ArgNotificationOptions options)` <br>Enables notification with custom options such as image, channel name etc. <br>*`options:`* An ArgNotificationOptions instance.  |
| void |`disableNotification()` <br> Disable notification option. If notification was enabled before, it will be cancelled. *Default value for Notification is **disabled***|
| void |`continuePlaylistWhenError()` <br>If an error occures, player won't publish error and will play next audio. |
| void |`stopPlaylistWhenError()` <br>If an error occures, player will stop playing and publish error. *As default **player publishes errors***.|
| void |`setPlaylistRepeat(boolean repeat)` <br>Sets repetition of the playlist. *Default value is **true***|
| void |`setAllButtonsImageResource(int resIdPlay,int resIdPause,int resIdPrev,int resIdNext,int resIdRepeat,int resIdNotRepeat)` <br> You can change image resources of all control buttons by this method.|
| void |`setPrevButtonImageResource(int resId)` <br> Sets image resource of the *previous* control button|
| void |`setNextButtonImageResource(int resId)` <br>Changes image resource of the *next* control button|
| void |`setPlayButtonImageResource(int resId)` <br>Sets image resource of the *play* control button|
| void |`setPauseButtonImageResource(int resId)` <br>Sets image resource of the *pause* control button|
| void |`setRepeatButtonImageResource(int repeatResId, int notRepeatResId)` <br>Sets image resource of the *repeat* control button.<br> *`repeatResId`*: when repetition is enabled <br> *`notRepeatResId`*: when repetition is disabled|
| void |`setOnErrorListener(Arg.OnErrorListener onErrorListener)` <br> `setOnPreparedListener(Arg.OnPreparedListener onPreparedListener)` <br> `setOnPausedListener(Arg.OnPausedListener onPausedListener)` <br> `setOnPlayingListener(Arg.OnPlayingListener onPlayingListener)` <br>`setOnCompletedListener(Arg.OnCompletedListener onCompletedListener)` <br>`setOnTimeChangeListener(Arg.OnTimeChangeListener onTimeChangeListener)` <br>`setOnPlaylistAudioChangedListener(Arg.OnPlaylistAudioChangedListener onPlaylistAudioChangedListener)` <br> Sets listeners to handle broadcasts|

<br>

#### ArgAudio
| Return | Method/Description |
| :-- |:--|
| ArgAudio |*`static`*`createFromRaw(@RawRes int rawId)` <br> Short way to create an audio from raw resources. Singer and audio name will be set to *rawId*.|
| ArgAudio |*`static`*`createFromRaw(String singer, String audioName, @RawRes int rawId)` <br>Creates a raw type audio.|
| ArgAudio|*`static`*`createFromAssets(String assetName)` <br>Short way to create an audio from raw resources. Singer and audio name will be set to *assetName*.|
| ArgAudio|*`static`*`createFromAssets(String singer, String audioName, String assetName)` <br>Creates an assets type audio.|
| ArgAudio|*`static`*`createFromURL(String url)` <br>Short way to create an audio from raw resources. Singer and audio name will be set to *url*.|
| ArgAudio|*`static`*`createFromURL(String singer, String audioName,  String url)` <br>Creates an url type audio.|
| ArgAudio|*`static`*`createFromFilePath(String filePath)` <br>Creates a filepath type audio.|
| ArgAudio|*`static`*`createFromFilePath(String singer, String audioName, String filePath)` <br>Short way to create an audio from raw resources. Singer and audio name will be set to *filePath*.|
| ArgAudio|`cloneAudio()` <br>Clones the audio.|
| ArgAudio|`convertToPlaylist()` <br> Makes audio as a playlist audio.|
| boolean|`isPlaylist()` <br> Checks if the audio is a playlist audio.|

<br>

#### ArgAudioList
| Return | Method/Description |
| :-- |:--|
| ArgAudioList|`add(@NonNull ArgAudio audio)` <br>Add an ArgAudio|
| ArgAudio |`getCurrentAudio()` <br> Gets current(playing) audio.|
| int|`getCurrentIndex()` <br>Gets index of current audio.|
| boolean|`equals(Object obj)` <br> Checks if another ArgAudioList object is equal to the current one. Checks emptyness, equalness of first and last childs of objects and sizes. |
| void |`goTo(int index)` <br> Changes playing audio of playlist.|
| boolean|`hasNext()` <br>Checks if any next audio|
| int|`getNextIndex()` <br> Gets index of next audio.|
| boolean|`goToNext()` <br> Sets next audio as playing audio. If this action is not possible, method returns false.|
| boolean|`hasPrev()` <br>Checks if any previous audio|
| int|`getPrevIndex()` <br> Gets index of previous audio.|
| boolean|`goToPrev()` <br> Sets previous audio as playing audio. If this action is not possible, method returns false.|

<br>

#### ArgNotificationOptions
| Return | Method/Description |
| :-- |:--|
| OnBuildNotificationListener |`getListener()` <br> Gets the listener.|
| ArgNotificationOptions |`setListener()` <br> Sets the listener.|
| bool|`isProgressEnabled()` <br>Checks whether progress is enabled. |
| ArgNotificationOptions|`setProgressEnabled(boolean progressEnabled)` <br>Checks whether progress is enabled. (Current time and a progress view is visible if set to true)|
| int |`getNotificationId()` <br> Gets notification id. |
| ArgNotificationOptions |`setNotificationId(int notificationId)` <br> Sets notification id.|
| String |`getChannelId()` <br> Gets channel id. <br>Requires MinSDK of 26 (O)|
| ArgNotificationOptions |`setChannelId(CharSequence channelId)` <br> Sets channel id. <br>Requires MinSDK of 26 (O)|
| String |`getChannelName()` <br> Gets channel name. <br>Requires MinSDK of 26 (O)|
| ArgNotificationOptions |`setChannelName(CharSequence channelName)` <br> Sets channel name. <br>Requires MinSDK of 26 (O)|

<br>


## ScreenShots
### Progress View
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/progressx.png "")
---
### Error View
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/errorviewx.png "")
---
### Notification
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/notification-with-progress.png "")
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/notification-without-progress.png "")
---
### Small Player View
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/smallview.png "")

### Large Player View
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/largeview.png "")
---
### Fullscreen Player View
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/fullscreenview.png "")
![](https://raw.githubusercontent.com/mergehez/ArgPlayer/master/ScreenShots/fullscreenview-playlist.png "")
