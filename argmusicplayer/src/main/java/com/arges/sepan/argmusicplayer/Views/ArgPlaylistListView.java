package com.arges.sepan.argmusicplayer.Views;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.ListView;

import com.arges.sepan.argmusicplayer.Models.ArgAudioList;

public class ArgPlaylistListView extends ListView {
    private Context context;
    private int selectedPosition = 0;
    private ArgPlaylistViewAdapter adapter;

    public ArgPlaylistListView(Context context) {
        super(context);
        init(context);
    }

    public ArgPlaylistListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ArgPlaylistListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
    }

    public void setAdapter(final ArgAudioList argAudioList) {
        adapter = new ArgPlaylistViewAdapter(this, context, argAudioList);
        setAdapter(adapter);
    }

    public ArgPlaylistViewAdapter getAdapter() {
        return adapter;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void scrollToSelected() {
        new Handler().post(() -> setSelection(getSelectedItemPosition()));
    }
}
