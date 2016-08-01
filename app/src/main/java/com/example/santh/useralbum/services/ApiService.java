package com.example.santh.useralbum.services;



import java.util.List;

import com.example.santh.useralbum.Models.Albums;
import com.example.santh.useralbum.Models.Photos;
import com.example.santh.useralbum.Models.Users;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import rx.Observable;

/**
 * Created by santh on 7/22/16
 * ApiService to make calls to jsonplaceholder.
 */
public class ApiService {

    private static final String API_SERVER_URL = "http://jsonplaceholder.typicode.com";
    private Api mApi;

    public ApiService() {


        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("Accept", "application/json");
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_SERVER_URL)
                .setRequestInterceptor(requestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mApi = restAdapter.create(Api.class);
    }

    public Api getApi() {

        return mApi;
    }

    /**
     * calling the urls to retrieve data using retrofit
     */
    public interface Api {

        @GET("/users")
        public Observable<List<Users>>
            getusers();

        @GET("/posts/{id}")
        public Observable<Users>
            getUser(@Path("id") int userId);

        @GET("/albums")
        public Observable<List<Albums>>
            getAlbums(@Query("userId") int userId);

        @GET("/photos")
        public Observable<List<Photos>>
        getPhotoslist(@Query("albumId")int albumId);

    }



}
