package com.example.santh.useralbum.Views;
import com.example.santh.useralbum.adapters.UserAdapter;
import com.example.santh.useralbum.Models.Users;
import com.example.santh.useralbum.services.ApiService;
import com.example.santh.useralbum.Presenters.UserPresenter;
import com.example.santh.useralbum.R;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by santh on 7/22/2016.
 * Displays Users in listview
 */
public class UserActivity extends AppCompatActivity {


    ListView mListViewUsers;

    UserAdapter mUserAdapter;

    UserPresenter mUserPresenter;
    ApiService mApiService;
    private static final int MY_PERMISSIONS_INTERNET = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        setTitle("Users");
        mListViewUsers =(ListView)findViewById(R.id.listUsers);
        ArrayList<Users> dummyUsers = new ArrayList<Users>();
        //set the adapter
        mUserAdapter = new UserAdapter(this, dummyUsers);
        mListViewUsers.setAdapter(mUserAdapter);

        mApiService = new ApiService();
        mUserPresenter = new UserPresenter(this, mApiService);
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED && isOnline()){
            //Call service to load users

            mUserPresenter.loadUsers();
        }else {
            Toast.makeText(getApplicationContext(), "Network unavailable! Try agaian", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, MY_PERMISSIONS_INTERNET);
        }

        mListViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {onUserSelect(position);}
       });
    }

    public void onResume(){
        super.onResume();
if(isOnline()) {
    mUserPresenter.loadUsers();
}
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * Ask user permission for internet
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_INTERNET: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && isOnline()) {
                    mUserPresenter.loadUsers();
                }
            }
        }
    }

    /**
     * Redirect to Album activity of selected user
     * @param position
     */
    public void onUserSelect(int position) {

        Users u = mUserAdapter.getItem(position);
        int userId = u.id;

        Intent albumIntent = new Intent(this, AlbumsActivity.class);
        albumIntent.putExtra("userId", userId);
        startActivity(albumIntent);
    }



    /**
     * Display Users in List view using adapter
     * @param users
     */
    public void displayUsers(List<Users> users) {

        mUserAdapter.clear();
        mUserAdapter.addAll(users);
        mUserAdapter.notifyDataSetInvalidated();

    }




}
