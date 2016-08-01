package com.example.santh.useralbum.Views;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.santh.useralbum.services.ImageLoader;
import com.example.santh.useralbum.R;
import com.example.santh.useralbum.adapters.ItemContentAdapter;

/**
 * Created by santh on 7/22/2016.
 * Load Selected photo in the album
 */

public class ItemActivity extends AppCompatActivity {

    private RecyclerView myItemContent;
    protected String mURL;
    protected String title;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        setTitle("photo");
        mURL = getIntent().getStringExtra("URL");
        title=getIntent().getStringExtra("title");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ImageView imageView = (ImageView) findViewById(R.id.itemImage);
        if(isOnline()) {
            new ImageLoader(imageView, this, 600, 600,"Please wait. Image is being downloaded").execute(mURL);
        }else {
            Toast.makeText(getApplicationContext(), "Network unavailable! Try agaian", Toast.LENGTH_SHORT).show();
        }
       myItemContent = (RecyclerView) findViewById(R.id.contentScrollView);

        myItemContent.addItemDecoration(new MarginDecoration(this));
        myItemContent.setLayoutManager(new LinearLayoutManager(this));
        myItemContent.setAdapter(new ItemContentAdapter(title));


    }
    public void onResume(){
        super.onResume();

    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Navigate back to album activity
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }






}
