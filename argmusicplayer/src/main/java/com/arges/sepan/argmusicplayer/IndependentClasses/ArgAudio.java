package com.arges.sepan.argmusicplayer.IndependentClasses;

import androidx.annotation.RawRes;

import com.arges.sepan.argmusicplayer.Enums.AudioType;

public class ArgAudio{
    private String singer, audioName, path;
    private boolean isPlaylist = false;
    private AudioType type;
    private int id = -1;

    public ArgAudio(String singer, String audioName, String path, AudioType type){
        this.singer = singer;
        this.audioName = audioName;
        this.path = path;
        this.type = type;
    }

    public ArgAudio(int id, String singer, String audioName, String path, AudioType type){
        this.singer = singer;
        this.audioName = audioName;
        this.path = path;
        this.type = type;
        this.id = id;
    }

    public static ArgAudio createFromRaw(@RawRes int rawId){
        return new ArgAudio(String.valueOf(rawId),String.valueOf(rawId), String.valueOf(rawId), AudioType.RAW);
    }
    public static ArgAudio createFromRaw(String singer, String audioName, @RawRes int rawId){
        return new ArgAudio(singer,audioName, String.valueOf(rawId), AudioType.RAW);
    }
    public static ArgAudio createFromAssets(String assetName){
        return new ArgAudio(assetName, assetName, assetName, AudioType.ASSETS);
    }
    public static ArgAudio createFromAssets(String singer, String audioName, String assetName){
        return new ArgAudio(singer,audioName, assetName, AudioType.ASSETS);
    }
    public static ArgAudio createFromURL(String url) {
        return new ArgAudio(url, url, url, AudioType.URL);
    }
    public static ArgAudio createFromURL(String singer, String audioName,  String url) {
        return new ArgAudio(singer,audioName, url, AudioType.URL);
    }
    public static ArgAudio createFromFilePath(String filePath) {
        return new ArgAudio(filePath, filePath, filePath, AudioType.FILE_PATH);
    }
    public static ArgAudio createFromFilePath(String singer, String audioName, String filePath) {
        return new ArgAudio(singer,audioName, filePath, AudioType.FILE_PATH);
    }
    public ArgAudio cloneAudio(){
        return new ArgAudio(id,singer,audioName,path,type);
    }

    public void setId(int id)                 { this.id = id;                   }
    public int getId()                        { return id;                      }
    public String getTitle()                  { return singer+" - "+audioName;  }
    public void setSinger(String singer)      { this.singer = singer;           }
    public String getSinger()                 { return singer;                  }
    public void setAudioName(String name)     { this.audioName = name;          }
    public String getAudioName()              { return path;                    }
    public String getPath()                   { return path;                    }
    public void setPath(String path)          { this.path = path;               }
    public AudioType getType()                { return type;                    }
    public void setType(AudioType type)       { this.type = type;               }
    public boolean isPlaylist()               { return isPlaylist;              }
    public ArgAudio makePlaylist()            { isPlaylist=true; return this;   }
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        else if (!(obj instanceof ArgAudio)) return false;
        else{
            ArgAudio a = (ArgAudio)obj;
            return this.getTitle().equals(a.getTitle())
                    && this.getType() == a.getType()
                    && this.getPath().equals(a.getPath())
                    && this.getId() == a.getId();
        }
    }
}
