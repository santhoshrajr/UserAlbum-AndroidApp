package com.example.santh.useralbum.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.santh.useralbum.R;
import com.example.santh.useralbum.Models.ItemContent;


/**
 * Created by sant on 7/23/2016.
 */
public class ItemContentAdapter extends RecyclerView.Adapter<ItemContent>{

    private String myContent;
    public ItemContentAdapter(String content){
        myContent=content;
    }

    public ItemContent onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_content, parent, false);
        return new ItemContent(view);
    }

    public void onBindViewHolder(ItemContent holder, int position) {
        holder.myTitle.setText(Html.fromHtml(myContent));

    }


    public int getItemCount() {
        return 1;

    }
}
