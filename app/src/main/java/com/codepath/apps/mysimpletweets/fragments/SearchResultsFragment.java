package com.codepath.apps.mysimpletweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.util.Log;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by netram on 2/18/15.
 */
public class SearchResultsFragment extends TweetsListFragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static SearchResultsFragment newInstance(String query) {
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        Bundle args = new Bundle();
        args.putString("query", query);
        searchResultsFragment.setArguments(args);

        return searchResultsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        ((TextView) menuCustomView.findViewById(R.id.title_text)).setText("Search Results");
        return view;
    }

    protected void populateTimeLine(long lastVisibleUid) {

        String query = getArguments().getString("query");

        progressBar.setVisibility(View.VISIBLE);

        twitterClient.getSearchResults(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                Log.d("DEBUG", jsonObject.toString());

                JSONArray jsonArray = null;
                try  {
                    jsonArray = jsonObject.getJSONArray("statuses");
                }
                catch(JSONException e) {
                    Log.e("Error", "Failure in json conversion" + e.toString());
                }
                tweetsArrayAdapter.addAll(Tweet.fromJSONArray(jsonArray));
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("Error", "Failure in twitter call" + errorResponse.toString());
                progressBar.setVisibility(View.INVISIBLE);
            }

        }, lastVisibleUid, query);
    }
}
