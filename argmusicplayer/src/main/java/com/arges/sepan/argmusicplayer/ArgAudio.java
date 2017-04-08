package com.arges.sepan.argmusicplayer;

import android.media.audiofx.Equalizer;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;

public class ArgAudio{
    private String title, path;
    private boolean isPlaylist = false;
    private AudioType type;

    public ArgAudio(String title, String path, AudioType type){
        this.title = title;
        this.path = path;
        this.type = type;
    }
    ArgAudio(String title, String path, AudioType type, boolean isPlaylist){
        this.isPlaylist = isPlaylist;
        this.title = title;
        this.path = path;
        this.type = type;
    }

    public static ArgAudio createFromRaw(@RawRes int rawId){
        return new ArgAudio(String.valueOf(rawId), String.valueOf(rawId), AudioType.RAW);
    }
    public static ArgAudio createFromRaw(String title, @RawRes int rawId){
        return new ArgAudio(title, String.valueOf(rawId), AudioType.RAW);
    }
    public static ArgAudio createFromAssets(String assetName){
        return new ArgAudio(assetName, assetName, AudioType.ASSETS);
    }
    public static ArgAudio createFromAssets(String title, String assetName){
        return new ArgAudio(title, assetName, AudioType.ASSETS);
    }
    public static ArgAudio createFromURL(String url) {
        return new ArgAudio(url, url, AudioType.URL);
    }
    public static ArgAudio createFromURL(String title, String url) {
        return new ArgAudio(title, url, AudioType.URL);
    }
    public static ArgAudio createFromFilePath(String filePath) {
        return new ArgAudio(filePath, filePath, AudioType.FILE_PATH);
    }
    public static ArgAudio createFromFilePath(String title, String filePath) {
        return new ArgAudio(title, filePath, AudioType.FILE_PATH);
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public AudioType getType() {
        return type;
    }
    public void setType(AudioType type) {
        this.type = type;
    }
    boolean isPlaylist(){return isPlaylist;}
    ArgAudio makePlaylist(){ this.isPlaylist=true; return this;}
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        else if (!(obj instanceof ArgAudio)) return false;
        else{
            ArgAudio a = (ArgAudio)obj;
            return this.getTitle().equals(a.getTitle())
                    && this.getType() == a.getType()
                    && this.getPath().equals(a.getPath());
        }
    }
}
