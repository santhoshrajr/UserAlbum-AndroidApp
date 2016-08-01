package com.example.santh.useralbum.Models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.santh.useralbum.R;
import com.example.santh.useralbum.Views.SquareImageView;


/**
 * Created by santh on 7/23/2016.
 */
public class GridItem extends RecyclerView.ViewHolder{
    public TextView myTitle;
    public TextView myAuthor;
    public TextView myHearts;
    public SquareImageView myImage;
    public int albumId;
    public int id;
    public String title;
    public String url;
    public String thumbnailUrl;

    public GridItem(View v){
        super(v);
        myImage = (SquareImageView) v.findViewById(R.id.gridImage);
        myTitle = (TextView) v.findViewById(R.id.gridItemText);

    }
}
