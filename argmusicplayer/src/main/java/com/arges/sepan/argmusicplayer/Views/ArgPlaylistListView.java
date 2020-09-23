package com.arges.sepan.argmusicplayer.Views;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudio;
import com.arges.sepan.argmusicplayer.IndependentClasses.ArgAudioList;
import com.arges.sepan.argmusicplayer.R;

public class ArgPlaylistListView extends ListView {
    private Context context;
    private int selectedPosition = 0;
    private ArgPlaylistListViewAdapter adapter;
    public ArgPlaylistListView(Context context) {
        super(context); init(context);
    }

    public ArgPlaylistListView(Context context, AttributeSet attrs) {
        super(context, attrs); init(context);
    }

    public ArgPlaylistListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr); init(context);
    }
    private void init(Context context){
        this.context = context;
    }
    public void setAdapter(final ArgAudioList argAudioList){
        adapter = new ArgPlaylistListViewAdapter(context,argAudioList);
        setAdapter(adapter);
    }
    public ArgPlaylistListViewAdapter getAdapter(){return adapter;}
    public void setSelectedPosition(int position){selectedPosition = position;}
    public class ArgPlaylistListViewAdapter extends ArrayAdapter<ArgAudio> {
        private Context context;
        ArgAudioList audioList = new ArgAudioList(true);
        public ArgPlaylistListViewAdapter(@NonNull Context context, ArgAudioList argAudioList) {
            super(context, -1);
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
            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = inflater.inflate(R.layout.player_playlist_item, parent, false);

                holder = new ViewHolder();
                holder.tvListAudioName = (AppCompatTextView) convertView.findViewById(R.id.tvListAudioName);
                holder.imgViewListPlaying = (AppCompatImageView) convertView.findViewById(R.id.imgViewListPlaying);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ArgAudio audio = audioList.get(position);
            if(audio!=null){
                String audioTitle = audio.getTitle();
                if(audioTitle.length()>23) audioTitle = audioTitle.substring(0,21) + "..";
                holder.tvListAudioName.setText(audioTitle.length()<24 ? audioTitle : audioTitle.substring(0,21) + "..");
                if(selectedPosition != position)
                    holder.imgViewListPlaying.setVisibility(INVISIBLE);

                if (audioList.getCurrentIndex() == position)
                    holder.imgViewListPlaying.setVisibility(VISIBLE);

            }
            return convertView;
        }
    }

    static class ViewHolder {
        AppCompatTextView tvListAudioName;
        AppCompatImageView imgViewListPlaying;
    }
    public void scrollToSelected(){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                setSelection(getSelectedItemPosition());
            }
        });
    }
}
