package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.utils.TwitterApplication;
import com.codepath.apps.mysimpletweets.adapters.UsersArrayAdapter;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.utils.TwitterClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by netram on 2/18/15.
 */
public abstract class UsersListFragment  extends Fragment {

    protected TwitterClient twitterClient;
    protected UsersArrayAdapter usersArrayAdapter;
    private List<User> users;
    private ListView lvUsers;
    protected ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users_list,container,false);

        ActionBar mActionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099FF")));
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        View mCustomView = mInflater.inflate(R.layout.timeline_action_bar, null);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        progressBar = (ProgressBar) view.findViewById(R.id.pbLoading);

        lvUsers = (ListView) view.findViewById(R.id.lvUsers);

        users = new ArrayList<User>();
        usersArrayAdapter = new UsersArrayAdapter(getActivity(), users);
        lvUsers.setAdapter(usersArrayAdapter);

        if(isNetworkAvailable()) {
            populateTimeLine();
        }
        else {
            Toast.makeText(getActivity(), "Network unavailable", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        twitterClient = TwitterApplication.getRestClient();
    }


    public void refreshTimeline() {
        usersArrayAdapter.clear();
        populateTimeLine();
    }


    public void addToTimeLine(User user) {
        usersArrayAdapter.insert(user,0);
        usersArrayAdapter.notifyDataSetChanged();
        populateTimeLine();
    }



    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void addAll(List<User> users) {
        usersArrayAdapter.addAll(users);
    }

    public TwitterClient getTwitterClient() {
        return twitterClient;
    }

    abstract protected void populateTimeLine();
}
