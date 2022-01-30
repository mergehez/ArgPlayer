package com.arges.sepan.argmusicplayer;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.arges.sepan.argmusicplayer.Models.ArgAudioList;
import com.arges.sepan.argmusicplayer.Views.ArgPlaylistListView;

import java.util.Locale;


public abstract class ArgPlayerFullScreenViewRoot extends ArgPlayerLargeViewRoot {
    LinearLayout layPanelPlaylist;
    ArgPlaylistListView listView;
    AppCompatImageButton btnViewFlipper;
    byte[] byteArray;
    AppCompatTextView tvAudioCount, tvAudioPosition;
    boolean isFlipped = false;
    String currentAudioListStrToCompare;
    private Locale currentLocale;

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
        currentLocale = Locale.getDefault();
        layPanelPlaylist = findViewById(R.id.layPanelPlaylist);
        listView = findViewById(R.id.listViewPlaylist);
        btnViewFlipper = findViewById(R.id.btnViewFlipper);
        tvAudioCount = findViewById(R.id.tvAudioCountPlaylist);
        tvAudioPosition = findViewById(R.id.tvPositonPlaylist);

        btnViewFlipper.setOnClickListener(v -> {
            isFlipped = !isFlipped;
            if (isFlipped) {
                listView.scrollToSelected();
                layPanelPlaylist.setVisibility(VISIBLE);
                imageView.setVisibility(INVISIBLE);
                setBtnViewFlipperImage(byteArray);
            } else {
                layPanelPlaylist.setVisibility(INVISIBLE);
                imageView.setVisibility(VISIBLE);
                btnViewFlipper.setImageResource(R.drawable.arg_playlist);
            }
        });
        listView.setOnItemClickListener((parent, view, position, id) -> player.playPlaylistItem(position));
    }

    private void setBtnViewFlipperImage(byte[] byteArray) {
        if (byteArray != null)
            btnViewFlipper.setImageBitmap(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
        else
            btnViewFlipper.setImageResource(R.drawable.mergesoftlogo);
    }

    @Override
    protected void setEmbeddedImageBitmap(byte[] byteArray) {
        super.setEmbeddedImageBitmap(byteArray);
        if(byteArray != null){
            this.byteArray = byteArray;
            setBtnViewFlipperImage(byteArray);
        }
    }

    @Override
    protected void onPlaylistAudioChanged(ArgAudioList list) {
        if (!list.getStringForComparison().equals(currentAudioListStrToCompare)) {
            currentAudioListStrToCompare = list.getStringForComparison();
            listView.setAdapter(list);
            tvAudioCount.setText(String.format(currentLocale, "%d songs", list.size()));
        } else {
            listView.setSelectedPosition(list.getCurrentIndex());
            listView.getAdapter().notifyDataSetChanged();
        }
        tvAudioPosition.setText(String.format(currentLocale, "%d / %d", list.getCurrentIndex() + 1, list.size()));
    }
}
