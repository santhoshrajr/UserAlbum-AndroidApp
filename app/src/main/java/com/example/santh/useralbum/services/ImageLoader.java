package com.example.santh.useralbum.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.santh.useralbum.R;
import com.example.santh.useralbum.adapters.GridAdapter;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by santh on 7/22/2016.
 *
 * Loads an image from a URL
 */
public class ImageLoader extends AsyncTask<String, Void, Bitmap> {



    private final WeakReference<ImageView> imageViewReference;
    private Context myContext;
    private int width, height;
    String message; // for dialog message
    ProgressDialog progress;


    /**
     * Async Task constructor with parameters
     * @param imageView
     * @param myContext
     * @param width
     * @param height
     *
     */
    public ImageLoader(ImageView imageView, Context myContext, int width, int height,String message) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.myContext = myContext;
        this.width=width;
        this.height=height;
        this.message=message;
        progress = new ProgressDialog(myContext);

    }

    @Override
    protected void onPreExecute() {
        // Runs on the UI thread
        progress.setMessage(message);
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }



    protected Bitmap doInBackground(String... params) {
        return decodeSampledBitmapFromURL(params[0], width, height);
    }



    /**
     *
     * Sets decoded bitmap image to Image view
     * @param bitmap
     */
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
        if(progress.isShowing())
            progress.dismiss();

    }





    /**
     * Resizes the image as per user specification
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
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

            return BitmapFactory.decodeStream(secondURL.openConnection().getInputStream(), null, options);
        } catch (MalformedURLException e) {

            return BitmapFactory.decodeResource(myContext.getResources(), R.drawable.dog);
        } catch (IOException e) {

            return BitmapFactory.decodeResource(myContext.getResources(), R.drawable.dog);

        }
    }


}
