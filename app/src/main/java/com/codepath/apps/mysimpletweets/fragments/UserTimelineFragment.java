package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.view.View;

import com.activeandroid.util.Log;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by netram on 2/17/15.
 */
public class UserTimelineFragment  extends TweetsListFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static UserTimelineFragment newInstance(String screenName) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screenName", screenName);
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    protected void populateTimeLine(long lastVisibleUid) {

        String screenName = getArguments().getString("screenName");

        progressBar.setVisibility(View.VISIBLE);

        twitterClient.getUserTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("DEBUG", jsonArray.toString());
                tweetsArrayAdapter.addAll(Tweet.fromJSONArray(jsonArray));
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("Error", "Failure in twitter call" + errorResponse.toString());
                progressBar.setVisibility(View.INVISIBLE);
            }

        }, lastVisibleUid, screenName);
    }
}
