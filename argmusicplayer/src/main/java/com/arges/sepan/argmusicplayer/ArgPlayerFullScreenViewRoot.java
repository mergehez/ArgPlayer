package com.arges.sepan.argmusicplayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.arges.sepan.argmusicplayer.IndependentClasses.Arg;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudioList;
import com.arges.sepan.argmusicplayer.Views.ArgPlaylistListView;


public class ArgPlayerFullScreenViewRoot extends ArgPlayerLargeViewRoot {
    LinearLayout layPanelPlaylist;
    ArgPlaylistListView listView;
    AppCompatImageButton btnViewFlipper;
    byte[] byteArray;
    AppCompatTextView tvAudioCount, tvAudioPosition;
    boolean isFlipped = false;
    ArgAudioList currentAudioList;

    public ArgPlayerFullScreenViewRoot(Context context) {
        super(context);
    }

    public ArgPlayerFullScreenViewRoot(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArgPlayerFullScreenViewRoot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context, int layoutResId) {
        super.init(context, layoutResId);
        layPanelPlaylist = (LinearLayout) findViewById(R.id.layPanelPlaylist);
        listView = (ArgPlaylistListView) findViewById(R.id.listViewPlaylist);
        btnViewFlipper = (AppCompatImageButton) findViewById(R.id.btnViewFlipper);
        tvAudioCount = (AppCompatTextView) findViewById(R.id.tvAudioCountPlaylist);
        tvAudioPosition = (AppCompatTextView) findViewById(R.id.tvPositonPlaylist);

        btnViewFlipper.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isFlipped = !isFlipped;
                if (isFlipped) {
                    listView.scrollToSelected();
                    layPanelPlaylist.setVisibility(VISIBLE);
                    imageView.setVisibility(INVISIBLE);
                    setBtnViewFlipperImage(byteArray);
                } else {
                    layPanelPlaylist.setVisibility(INVISIBLE);
                    imageView.setVisibility(VISIBLE);
                    btnViewFlipper.setImageResource(R.drawable.arg_music_playlist);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                player.playPlaylistItem(position);
            }
        });
    }
    private void setBtnViewFlipperImage(byte[] byteArray){
        if(byteArray!=null)
            btnViewFlipper.setImageBitmap(Arg.byteArrayToBitmap(byteArray));
        else
            btnViewFlipper.setImageResource(R.drawable.mergesoftlogo);
    }
    @Override
    void setEmbeddedImageBitmap(byte[] byteArray) {
        super.setEmbeddedImageBitmap(byteArray);
        this.byteArray = byteArray;
        setBtnViewFlipperImage(byteArray);
    }

    @SuppressLint("DefaultLocale")
    @Override
    void onPlaylistAudioChanged(ArgAudioList list){
        if(!list.equals(currentAudioList)){
            currentAudioList = list;
            listView.setAdapter(list);
            tvAudioCount.setText(String.format("%d songs",list.size()));
        }else{
            listView.setSelectedPosition(list.getCurrentIndex());
            listView.getAdapter().notifyDataSetChanged();
        }
        tvAudioPosition.setText(String.format("%d / %d", list.getCurrentIndex()+1, list.size()));
    }
}
