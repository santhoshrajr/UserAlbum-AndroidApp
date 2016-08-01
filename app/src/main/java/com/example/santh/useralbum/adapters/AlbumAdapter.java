package com.example.santh.useralbum.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.santh.useralbum.Models.Albums;
import com.example.santh.useralbum.Models.Users;
import com.example.santh.useralbum.R;

import java.util.ArrayList;

/**
 * Created by santh on 7/20/2016.
 */
public class AlbumAdapter extends ArrayAdapter<Albums> {
    public AlbumAdapter(Context context, ArrayList<Albums> albums ) {
        super(context, 0, albums);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Albums album = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_album, parent, false);

        TextView title = (TextView) convertView.findViewById(R.id.textViewAlbum);
        title.setText(album.title);

        return convertView;
    }
}
