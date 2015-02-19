package com.codepath.apps.mysimpletweets.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.utils.TwitterApplication;
import com.codepath.apps.mysimpletweets.activities.TimelineActivity;
import com.codepath.apps.mysimpletweets.activities.TweetDetailActivity;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.utils.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.utils.TwitterClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by netram on 2/17/15.
 */
public abstract class TweetsListFragment  extends android.support.v4.app.DialogFragment{

    protected TwitterClient twitterClient;
    protected TweetsArrayAdapter tweetsArrayAdapter;
    private List<Tweet> tweets;
    private ListView lvTweets;
    private SwipeRefreshLayout swipeContainer;
    protected ProgressBar progressBar;
    protected View menuCustomView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tweets_list,container,false);

        ActionBar mActionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099FF")));
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(getActivity());
        menuCustomView = mInflater.inflate(R.layout.timeline_action_bar, null);

        mActionBar.setCustomView(menuCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        progressBar = (ProgressBar) view.findViewById(R.id.pbLoading);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeline();
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lvTweets = (ListView) view.findViewById(R.id.lvTweets);

        tweets = new ArrayList<Tweet>();
        tweetsArrayAdapter = new TweetsArrayAdapter(getActivity(), tweets);
        lvTweets.setAdapter(tweetsArrayAdapter);
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount, long lastVisibleUid) {

                if (page > 10) {
                    return;
                }

                populateTimeLine(lastVisibleUid);

            }
        });

        lvTweets.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Tweet tweet = tweets.get(position);

                Intent intent = new Intent(getActivity(),TweetDetailActivity.class);
                intent.putExtra("tweet",tweet);
                startActivityForResult(intent,TimelineActivity.REQUEST_CODE_DETAILED_ACTIVITY);

            }
        });

        if(isNetworkAvailable()) {
            populateTimeLine(Long.MAX_VALUE);
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
        tweetsArrayAdapter.clear();
        populateTimeLine(Long.MAX_VALUE);
    }


    public void addToTimeLine(Tweet tweet) {

        tweetsArrayAdapter.insert(tweet,0);
        lvTweets.setSelectionAfterHeaderView();
    }



    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public void addAll(List<Tweet> tweets) {
        tweetsArrayAdapter.addAll(tweets);
    }

    public TwitterClient getTwitterClient() {
        return twitterClient;
    }

    abstract protected void populateTimeLine(long lastVisibleUid);
}
