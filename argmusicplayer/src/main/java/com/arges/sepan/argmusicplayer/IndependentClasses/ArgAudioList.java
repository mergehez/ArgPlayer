package com.arges.sepan.argmusicplayer.IndependentClasses;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ArgAudioList{
    private ArrayList<ArgAudio> list = new ArrayList<>();
    private int currentIndex = -1;
    private boolean repeat = false;
    public ArgAudioList(boolean repeat){ this.repeat = repeat; }

    public boolean hasNext(){ return !isEmpty() && size()>1 && (repeat || size()>currentIndex+1); }
    public int getNextIndex(){return hasNext() ? getProperIndex(currentIndex+1) : getCurrentIndex();}
    public boolean goToNext(){
        if(hasNext()){
            changeCurrentIndex(currentIndex+1);
            return true;
        }
        return false;
    }
    public boolean hasPrev() {  return !isEmpty() && size()>1 && (repeat || currentIndex > 0); }
    public int getPrevIndex(){return hasPrev() ? getProperIndex(currentIndex-1) : getCurrentIndex();}
    public boolean goToPrev(){
        if(hasPrev()){
            changeCurrentIndex(currentIndex-1);
            return true;
        }
        return false;
    }

    private int changeCurrentIndex(int newIndex){
        return currentIndex = (newIndex+size()) % size();
    }
    private int getProperIndex(int i){return  (i+size()) % size();}
    private int idForAudios = 0;
    public ArgAudioList add(@NonNull ArgAudio audio){
        ArgAudio newAudio = audio.cloneAudio();
        newAudio.setId(idForAudios++);
        list.add(newAudio.makePlaylist());
        if(currentIndex == -1) changeCurrentIndex(0);
        return this;
    }

    public void goTo(int index){ changeCurrentIndex(index); }
    public ArgAudio getCurrentAudio(){ return currentIndex >= 0 ? list.get(currentIndex) : null; }

    public boolean isRepeat(){
        return this.repeat;
    }
    public void setRepeat(boolean repeat){
        this.repeat = repeat;
    }
    public int getCurrentIndex(){
        return currentIndex;
    }


    public int size(){
        return list.size();
    }
    public boolean isEmpty() {
        return size() == 0;
    }
    public boolean contains(ArgAudio o) {
        return list.contains(o);
    }
    public int indexOf(ArgAudio o) {
        return list.indexOf(o);
    }
    public void addAll(Collection<? extends ArgAudio> c) { for (ArgAudio a : c) add(a);  }
    public void clear() { list.clear();   idForAudios = 0; }
    public ArgAudio get(int index) {
        return list.get(index);
    }
    @NonNull
    public Iterator<ArgAudio> iterator() {
        return list.iterator();
    }
    public ArgAudio set(int index, ArgAudio element) {
        return list.set(index, element);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        else if (!(obj instanceof ArgAudioList)) return false;
        else {
            ArgAudioList a = (ArgAudioList) obj;
            return !(a.isEmpty() || this.isEmpty())
                    && this.size() == a.size()
                    && this.get(0).equals(a.get(0))
                    && this.get(this.size()-1).equals(a.get(a.size()-1));
        }
    }
}
