package com.example.santh.useralbum.Presenters;

import com.example.santh.useralbum.Models.Users;
import com.example.santh.useralbum.services.ApiService;
import com.example.santh.useralbum.Views.UserActivity;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by santh on 7/21/2016.
 * Retrives users
 */
public class UserPresenter {

    UserActivity mView;
    ApiService mApi;

    public UserPresenter(UserActivity view, ApiService api) {

        mView = view;
        mApi = api;
    }

    public void loadUsers() {

        mApi.getApi()
                .getusers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Users>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<Users> users) {

                        mView.displayUsers(users);
                    }
                });
    }
}
