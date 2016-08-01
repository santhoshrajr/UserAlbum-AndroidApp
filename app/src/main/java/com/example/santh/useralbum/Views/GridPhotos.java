package com.example.santh.useralbum.Views;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.santh.useralbum.Models.Photos;
import com.example.santh.useralbum.Presenters.GridPresenter;
import com.example.santh.useralbum.R;
import com.example.santh.useralbum.adapters.GridAdapter;
import com.example.santh.useralbum.services.ApiService;

import java.util.List;

/**
 * created by santh on 7/22/2016
 * Display photos in recycler view of the respective album
 */
public class GridPhotos extends AppCompatActivity implements ViewGroup.OnClickListener  {

    private RecyclerView myGrid;
    private String[][] gridItems;

    protected int mAlbumId;
    ApiService mApiService;
    GridPresenter mGridPresenter;
    GridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_photos);
       setTitle("Album Photos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAlbumId = getIntent().getIntExtra("albumId", 0);
        myGrid = (RecyclerView)findViewById(R.id.gridView);
        myGrid.addItemDecoration(new MarginDecoration(this));
        myGrid.setHasFixedSize(true);
        myGrid.setLayoutManager(new GridLayoutManager(this, 2));

        mApiService = new ApiService();
        mGridPresenter = new GridPresenter(this, mApiService);
        if(isOnline()) {
            mGridPresenter.loadPhotos();
        }


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
     * Navigate to Album activity
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

    /**
     * Gets the selected albumID
     * @return
     */
    public int getAlbumId() {

        return mAlbumId;
    }


    /**
     * Displays photos in recycler view using grid adapter
     * @param photos
     */
    public void displayPhotos(List<Photos> photos) {


      gridItems=  getMultidimensionalArrayOfStringsFromCursor(photos);

        myGrid.setAdapter(new GridAdapter(this, gridItems, this));
    }





    /**
     * Converts generic list to multi dimensional array
     * @param photos
     * @return
     */
    private String[][] getMultidimensionalArrayOfStringsFromCursor(List<Photos> photos){
        if(photos==null)return null;

        String[][] returnValues = new String[photos.size()][];
        for(int i=0;i<photos.size();i++)
        {
            String[] rowValues = new String[5];


            rowValues[0] = String.valueOf(photos.get(i).albumId);
            rowValues[1]= String.valueOf(photos.get(i).id);
            rowValues[2] = photos.get(i).title;
                rowValues[3] = photos.get(i).url;
            rowValues[4]=photos.get(i).thumbnailUrl;
            returnValues[i]=rowValues;

        }

        return returnValues;


    }

    /**
     * Navigate to particular photo
     * @param v
     */
    @Override
    public void onClick(View v) {
        int itemPosition = myGrid.getChildLayoutPosition(v);
        Intent itemIntent = new Intent(this, ItemActivity.class);
        itemIntent.putExtra("URL", gridItems[itemPosition][3]);
        itemIntent.putExtra("title",gridItems[itemPosition][2]);
        startActivity(itemIntent);
    }
}
