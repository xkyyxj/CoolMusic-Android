package com.run.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.run.Bean.LocalMusic;
import com.run.coolmusic.R;

import java.util.List;

/**
 * Created by Albert on 2015/8/27.
 */
public class LocalMusicListAdapter extends BaseAdapter {

    private List<LocalMusic> localMusicList;

    private LayoutInflater layoutInflater;

    public LocalMusicListAdapter(Context _context, List<LocalMusic> _list) {
        layoutInflater = LayoutInflater.from(_context);
        localMusicList = _list;
    }

    @Override
    public int getCount() {
        return localMusicList == null ? 0 : localMusicList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LocalMusic temp = localMusicList.get(position);
        if(convertView == null) {
            convertView = layoutInflater.inflate(R.layout.local_music_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder)convertView.getTag();
        holder.musicname.setText(temp.getMusicNameString());
        holder.musicartist.setText(temp.getArtistString());
        //holder.musictimelength.setText(temp.getMusicTimeLengthInt());
        return convertView;
    }

    public class ViewHolder {
        public final TextView musicname;
        public final TextView musicartist;
        public final TextView musictimelength;
        public final View root;

        public ViewHolder(View root) {
            musicname = (TextView) root.findViewById(R.id.music_name);
            musicartist = (TextView) root.findViewById(R.id.music_artist);
            musictimelength = (TextView) root.findViewById(R.id.music_time_length);
            this.root = root;
        }
    }
}
