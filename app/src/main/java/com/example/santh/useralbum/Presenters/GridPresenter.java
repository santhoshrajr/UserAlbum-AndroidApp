package com.example.santh.useralbum.Presenters;

import com.example.santh.useralbum.Models.Photos;
import com.example.santh.useralbum.Views.GridPhotos;
import com.example.santh.useralbum.services.ApiService;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by santh on 7/21/2016.
 * retrieves photos for the respective album
 */
public class GridPresenter {

    GridPhotos mView;
    ApiService mForum;

    public GridPresenter(GridPhotos activity, ApiService forum) {

        mView = activity;
        mForum = forum;
    }



    public void loadPhotos() {

        mForum.getApi()
                .getPhotoslist(mView.getAlbumId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Photos>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Photos> photos) {

                        mView.displayPhotos(photos);
                    }
                });
    }

}
