package com.arges.sepan.argmusicplayer.Views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.arges.sepan.argmusicplayer.Models.ArgAudio;
import com.arges.sepan.argmusicplayer.Models.ArgAudioList;
import com.arges.sepan.argmusicplayer.R;

public class ArgPlaylistViewAdapter extends ArrayAdapter<ArgAudio> {
    private final ArgPlaylistListView argPlaylistListView;
    private final Context context;
    ArgAudioList audioList;

    public ArgPlaylistViewAdapter(ArgPlaylistListView argPlaylistListView, @NonNull Context context, ArgAudioList argAudioList) {
        super(context, -1);
        this.argPlaylistListView = argPlaylistListView;
        this.audioList = argAudioList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return audioList.size();
    }

    @Override
    public ArgAudio getItem(int position) {
        return audioList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ArgPlaylistViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.player_playlist_item, parent, false);

            holder = new ArgPlaylistViewHolder();
            holder.tvListAudioName = convertView.findViewById(R.id.tvListAudioName);
            holder.imgViewListPlaying = convertView.findViewById(R.id.imgViewListPlaying);
            convertView.setTag(holder);
        } else {
            holder = (ArgPlaylistViewHolder) convertView.getTag();
        }
        ArgAudio audio = audioList.get(position);
        if (audio != null) {
            String audioTitle = audio.getTitle();
            //if (audioTitle.length() > 23) audioTitle = audioTitle.substring(0, 21) + "..";
            holder.tvListAudioName.setText(audioTitle);
            if (argPlaylistListView.getSelectedPosition() != position)
                holder.imgViewListPlaying.setVisibility(View.INVISIBLE);

            if (audioList.getCurrentIndex() == position)
                holder.imgViewListPlaying.setVisibility(View.VISIBLE);

        }
        return convertView;
    }
}
