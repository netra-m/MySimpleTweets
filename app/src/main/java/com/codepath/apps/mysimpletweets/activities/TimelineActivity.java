package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.util.Log;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.TwitterApplication;
import com.codepath.apps.mysimpletweets.adapters.TweetsArrayAdapter;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.utils.EndlessScrollListener;
import com.codepath.apps.mysimpletweets.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimelineActivity extends ActionBarActivity implements ComposeCallBack {

    private static final int REQUEST_CODE = 333;
    private TwitterClient twitterClient;
    private TweetsArrayAdapter tweetsArrayAdapter;
    private List<Tweet> tweets;
    private ListView lvTweets;
    private User currentUser;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

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

        lvTweets = (ListView) findViewById(R.id.lvTweets);

        tweets = new ArrayList<Tweet>();
        tweetsArrayAdapter = new TweetsArrayAdapter(this, tweets);
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

        twitterClient = TwitterApplication.getRestClient();
        populateTimeLine(Long.MAX_VALUE);
        populateCurrentUser();
    }

    private void populateTimeLine(long lastVisibleUid) {

        twitterClient.getHomeTimeline(new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray jsonArray) {
                Log.d("DEBUG", jsonArray.toString());
                tweetsArrayAdapter.addAll(Tweet.fromJSONArray(jsonArray));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e("Error", "Failure in twitter call" + errorResponse.toString());
            }

        }, lastVisibleUid);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.compose) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            ComposeFragment settingsFragment = ComposeFragment.newInstance("Compose Tweet");
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", currentUser);
            settingsFragment.setArguments(bundle);
            settingsFragment.show(fm,"fragment_compose");
        }

        return super.onOptionsItemSelected(item);
    }

    private void populateCurrentUser() {

        if (currentUser == null) {

            twitterClient.getCurrentUser(new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {

                    currentUser = User.fromJSON(jsonObject);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.e("Error", "Failure in twitter call" + errorResponse.toString());
                }

            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract name value from result extras
            String tweetText = data.getExtras().getString("tweet");

            twitterClient.postTweet(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    refreshTimeline();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {

                    Toast.makeText(TimelineActivity.this, "Unable to post your Tweet", Toast.LENGTH_SHORT).show();
                }
            }, tweetText);


        }
    }

    private void refreshTimeline() {
        tweetsArrayAdapter.clear();
        populateTimeLine(Long.MAX_VALUE);
    }

    @Override
    public void onCompose(String tweetBody) {

        twitterClient.postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                refreshTimeline();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {

                Toast.makeText(TimelineActivity.this, "Unable to post your Tweet", Toast.LENGTH_SHORT).show();
            }
        }, tweetBody);

    }
}
