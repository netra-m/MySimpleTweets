package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.view.View;

import com.activeandroid.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.codepath.apps.mysimpletweets.models.User.fromJSONArray;

/**
 * Created by netram on 2/18/15.
 */
public class FollowersListFragment extends UsersListFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static FollowersListFragment newInstance(String screenName) {
        FollowersListFragment followersListFragment = new FollowersListFragment();
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        followersListFragment.setArguments(args);
        return followersListFragment;
    }

    protected void populateTimeLine() {

        String screenName = getArguments().getString("screenName");

        progressBar.setVisibility(View.VISIBLE);

        twitterClient.getFollowers(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d("DEBUG", jsonObject.toString());
                JSONArray jsonArray = null;
                try  {
                    jsonArray = jsonObject.getJSONArray("users");
                }
                catch(JSONException e) {
                    Log.e("Error", "Failure in json conversion" + e.toString());
                }
                usersArrayAdapter.addAll(fromJSONArray(jsonArray));
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("Error", "Failure in twitter call" + errorResponse.toString());
                progressBar.setVisibility(View.INVISIBLE);
            }

        },screenName);
    }
}

