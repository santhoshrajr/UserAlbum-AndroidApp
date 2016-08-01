package com.example.santh.useralbum.Views;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.santh.useralbum.Models.Albums;
import com.example.santh.useralbum.Presenters.AlbumPresenter;
import com.example.santh.useralbum.R;
import com.example.santh.useralbum.adapters.AlbumAdapter;
import com.example.santh.useralbum.services.ApiService;

import java.util.ArrayList;
import java.util.List;

/**
 * create by santh  on 7/22/2016
 * Dispaly Albums for the selected user
 */

public class AlbumsActivity extends AppCompatActivity {

    ListView mListViewAlbum;

    AlbumAdapter mAlbumAdapter;

    AlbumPresenter mAlbumPresenter;
    ApiService mApiService;

    protected int mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);
        setTitle("User Album");

        mUserId = getIntent().getIntExtra("userId", 0);
        mListViewAlbum=(ListView)findViewById(R.id.listAlbums);
        ArrayList<Albums> dummyAlbums = new ArrayList<Albums>();
        mAlbumAdapter = new AlbumAdapter(this, dummyAlbums);
        mListViewAlbum.setAdapter(mAlbumAdapter);

        mApiService = new ApiService();
        mAlbumPresenter = new AlbumPresenter(this, mApiService);
        if(isOnline()) {
            mAlbumPresenter.loadAlbums();
        }
        else {
            Toast.makeText(getApplicationContext(), "Network unavailable! Try agaian", Toast.LENGTH_SHORT).show();
        }
        //display back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mListViewAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {onAlbumSelect(position);}
        });

    }
    public void onResume(){
        super.onResume();
        if(isOnline()) {
            mAlbumPresenter.loadAlbums();
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Redirects to grid photo activity for the selected album
     * @param position
     */
    private void onAlbumSelect(int position) {
        Albums album = mAlbumAdapter.getItem(position);
        int albumId = album.id;

        Intent GridIntent = new Intent(this, GridPhotos.class);
        GridIntent.putExtra("albumId", albumId);
        startActivity(GridIntent);
    }

    /**
     * returns userId selected
     * @return
     */
    public int getUserId() {

        return mUserId;
    }

    /**
     * display albums in list view using adapter
     * @param albums
     */
    public void displayAlbums(List<Albums> albums) {

        mAlbumAdapter.clear();
        mAlbumAdapter.addAll(albums);
        mAlbumAdapter.notifyDataSetInvalidated();
    }

}
