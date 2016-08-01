package com.example.santh.useralbum.adapters;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.santh.useralbum.Views.GridPhotos;
import com.example.santh.useralbum.services.ImageLoader;
import com.example.santh.useralbum.Models.GridItem;
import com.example.santh.useralbum.R;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by santh on 7/23/2016.
 */
public  class GridAdapter extends RecyclerView.Adapter<GridItem>{

    private View.OnClickListener myOnClickListener;
    private String[][] gridItems;
    private Context myContext;
    ArrayList<Uri> imageList;
    private LruCache<String, Bitmap> mLruCache;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101;
    public GridAdapter(View.OnClickListener onClickListener, String[][] items, Context myContext){
        myOnClickListener = onClickListener;
        gridItems = items;
        this.myContext = myContext;
        //Find out maximum memory available to application
        //1024 is used because LruCache constructor takes int in kilobytes
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/4th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 3;
        Log.d("useralbum", "max memory " + maxMemory + " cache size " + cacheSize);

        // LruCache takes key-value pair in constructor
        // key is the string to refer bitmap
        // value is the stored bitmap
        mLruCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes
                return bitmap.getByteCount() / 1024;
            }
        };

        imageList  = new ArrayList<Uri>();


            addimage();


    }

    private void addimage() {
        File imageSrcDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        // if directory not present, build it
        if (!imageSrcDir.exists()){
            imageSrcDir.mkdirs();
        }
        ArrayList<File> imagesInDir = getImagesFromDirectory(imageSrcDir);

        for (File file: imagesInDir){
            // imageList will hold Uri of all images
            imageList.add(Uri.fromFile(file));
        }
    }





    private ArrayList<File> getImagesFromDirectory (File parentDirPath){
        ArrayList <File> listOfImages =  new ArrayList<File>();
        File [] fileArray = null;

        if ( parentDirPath.isDirectory() ){//parentDirPath.exists() &&
            //    &&
            //   parentDirPath.canRead()){
            fileArray = parentDirPath.listFiles();
        }

        if (fileArray == null){
            return listOfImages;    // return empty list
        }

        for (File file: fileArray){
            if (file.isDirectory()){
                listOfImages.addAll(getImagesFromDirectory(file));
            }
            else {
                // Only JPEG and PNG formats are included
                // for sake of simplicity
                if (file.getName().endsWith("png") ||
                        file.getName().endsWith("jpg")){
                    listOfImages.add(file);
                }
            }
        }
        return listOfImages;
    }

    public GridItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item, parent, false);
        view.setOnClickListener(myOnClickListener);
        return new GridItem(view);
    }

    public void onBindViewHolder(GridItem holder, int position) {
        ImageView imageView = holder.myImage;
        Bitmap thumbnailImage = null;




        //thumbnailImage is fetched from LRU cache
        // Use the url as the key to LruCache
        thumbnailImage = getBitmapFromMemCache(gridItems[position][4]);

        if (thumbnailImage == null){
            new Image(imageView, myContext, 100, 100).execute(gridItems[position][4]);
       }
        else {

           imageView.setImageBitmap(thumbnailImage);
        }


        holder.myImage.setBackgroundColor(Color.BLACK);

        holder.myTitle.setText(gridItems[position][2]);

    }


    public int getItemCount() {
        if(gridItems!=null)return gridItems.length;
        else return 0;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mLruCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mLruCache.get(key);
    }

    private class Image extends AsyncTask<String, Void, Bitmap> {


        private final WeakReference<ImageView> imageViewReference;
        private Context myContext;
        private int width, height;


        /**
         * Async Task constructor with parameters
         *
         * @param imageView
         * @param myContext
         * @param width
         * @param height
         */
        public Image(ImageView imageView, Context myContext, int width, int height) {
            imageViewReference = new WeakReference<ImageView>(imageView);
            this.myContext = myContext;
            this.width = width;
            this.height = height;

        }


        protected Bitmap doInBackground(String... params) {
            return decodeSampledBitmapFromURL(params[0], width, height);
        }


        /**
         * Sets decoded bitmap image to Image view
         *
         * @param bitmap
         */
        protected void onPostExecute(Bitmap bitmap) {
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(bitmap);
                }
            }

        }


        /**
         * Resizes the image as per user specification
         *
         * @param options
         * @param reqWidth
         * @param reqHeight
         * @return
         */

        public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {

                final int halfHeight = height / 2;
                final int halfWidth = width / 2;
                while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                    inSampleSize *= 2;
                }
            }
            return inSampleSize;
        }

        /**
         * Returns Bitmap image form the URL
         *
         * @param imageURL
         * @param reqWidth
         * @param reqHeight
         * @return
         */
        public Bitmap decodeSampledBitmapFromURL(String imageURL, int reqWidth, int reqHeight) {
            try {
                URL url = new URL(imageURL);
                HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
                ucon.setInstanceFollowRedirects(false);
                URL secondURL = new URL(ucon.getHeaderField("Location"));
                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(secondURL.openConnection().getInputStream(), null, options);
                options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
                options.inJustDecodeBounds = false;
                addBitmapToMemoryCache(imageURL,BitmapFactory.decodeStream(secondURL.openConnection().getInputStream(), null, options));
                return BitmapFactory.decodeStream(secondURL.openConnection().getInputStream(), null, options);
            } catch (MalformedURLException e) {

                return BitmapFactory.decodeResource(myContext.getResources(), R.drawable.dog);
            } catch (IOException e) {

                return BitmapFactory.decodeResource(myContext.getResources(), R.drawable.dog);

            }
        }
    }
}
