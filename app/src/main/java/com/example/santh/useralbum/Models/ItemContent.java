package com.example.santh.useralbum.Models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.santh.useralbum.R;


/**
 * Created by santh on 7/23/2016.
 */
public class ItemContent extends RecyclerView.ViewHolder{
    public TextView myTitle;
    public TextView myContent;

    public ItemContent(View v){
        super(v);
        myTitle = (TextView) v.findViewById(R.id.itemTitle);

    }
}
