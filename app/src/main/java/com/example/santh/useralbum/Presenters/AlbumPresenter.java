package com.example.santh.useralbum.Presenters;

import com.example.santh.useralbum.Models.Albums;

import com.example.santh.useralbum.Views.AlbumsActivity;
import com.example.santh.useralbum.services.ApiService;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by santh on 7/20/2016.
 *  retrieves albums for the respective user
 */
public class AlbumPresenter {
    AlbumsActivity mView;
    ApiService mApi;

    public AlbumPresenter(AlbumsActivity activity, ApiService api) {

        mView = activity;
        mApi = api;
    }



    public void loadAlbums() {

        mApi.getApi()
                .getAlbums(mView.getUserId())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Albums>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Albums> comments) {

                        mView.displayAlbums(comments);
                    }
                });
    }


}
