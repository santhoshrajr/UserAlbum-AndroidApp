package com.example.santh.useralbum.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.santh.useralbum.Models.Users;
import com.example.santh.useralbum.R;
//import com.kmangutov.restexample.models.Post;

import java.util.ArrayList;

/**
 * Created by santh on 7/23/2016.
 */
public class UserAdapter extends ArrayAdapter<Users> {

    public UserAdapter(Context ctx, ArrayList<Users> posts) {

        super(ctx, 0, posts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Users users = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_user, parent, false);

        TextView title = (TextView) convertView.findViewById(R.id.textViewUser);
        title.setText(users.name);

        return convertView;
    }
}
