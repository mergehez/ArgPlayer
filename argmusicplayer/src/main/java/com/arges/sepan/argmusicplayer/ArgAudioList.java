package com.arges.sepan.argmusicplayer;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class ArgAudioList{
    ArrayList<ArgAudio> list = new ArrayList<>();
    private int currentIndex = -1;
    private boolean repeat = false;
    public ArgAudioList(){ }

    public boolean hasNext(){
        if(repeat) return !isEmpty();
        return !isEmpty() && size() != currentIndex + 1;
    }
    protected ArgAudio getNext(){
        if(repeat && !isEmpty()) return get(currentIndex = getProperIndex(currentIndex+1));
        return (isEmpty() || size()==currentIndex+1) ? null : get(++currentIndex);
    }
    protected void goToNext(){
        if(hasNext())   currentIndex = getProperIndex(currentIndex+1);
    }
    public boolean hasPrev(){
        if(repeat) return !isEmpty();
        return !isEmpty() && currentIndex != 0;
    }
    protected ArgAudio getPrev(){
        if(repeat && !isEmpty()) return get(currentIndex = getProperIndex(currentIndex-1));
        return (isEmpty() || currentIndex==0) ? null : get(--currentIndex);
    }
    protected void goToPrev(){
        if(hasPrev())   currentIndex = getProperIndex(currentIndex-1);
    }
    public int getCurrentIndex(){
        return currentIndex;
    }
    public ArgAudio getCurrentAudio(){
        return getCurrentIndex() >= 0 ? list.get(getCurrentIndex()) : null;
    }
    protected void setRepeat(boolean repeat){
        this.repeat = repeat;
    }
    public boolean isRepeat(){
        return this.repeat;
    }
    public void goToStart(){
        currentIndex = 0;
    }
    private int getProperIndex(int index){
        return (index % size());
    }
    public ArgAudioList add(@NonNull ArgAudio audio){
        if(currentIndex==-1) currentIndex=0;
        list.add(audio.makePlaylist());
        return this;
    }
    public void add(int index, @NonNull ArgAudio element) {
        list.add(index, element);
    }
    public boolean removeAll(Collection<ArgAudio> c) {
        return list.removeAll(c);
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
    public ArgAudio remove(int index) {
        return list.remove(index);
    }
    public boolean remove(ArgAudio o) {
        return list.remove(o);
    }
    public boolean addAll(Collection<? extends ArgAudio> c) {
        return list.addAll(c);
    }
    public void clear() {
        list.clear();
    }
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
    public boolean addAll(int index, Collection<? extends ArgAudio> c) {
        return list.addAll(index, c);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        else if (!(obj instanceof ArgAudioList)) return false;
        else {
            ArgAudioList a = (ArgAudioList) obj;
            return !(a.isEmpty() || this.isEmpty()) && this.size() == a.size() && this.get(0).equals(a.get(0));
        }
    }
}
