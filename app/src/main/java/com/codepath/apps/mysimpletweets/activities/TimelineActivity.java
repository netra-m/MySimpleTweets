package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.ComposeCallBack;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

import fragments.ComposeFragment;
import fragments.TweetsListFragment;

public class TimelineActivity extends ActionBarActivity implements ComposeCallBack {

    public static final int REQUEST_CODE_DETAILED_ACTIVITY = 333;

    private TweetsListFragment tweetsListFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timeline);

        tweetsListFragment = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.layout.fragment_tweets_list);
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
            ComposeFragment composeFragment = ComposeFragment.newInstance("Compose Tweet");
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", tweetsListFragment.getCurrentUser());
            composeFragment.setArguments(bundle);
            composeFragment.show(fm, "fragment_compose");
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_DETAILED_ACTIVITY) {
            String tweetText = data.getExtras().getString("tweetBody");
            onCompose(tweetText);
        }
    }


    @Override
    public void onCompose(String tweetBody) {

        tweetsListFragment.getTwitterClient().postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                tweetsListFragment.refreshTimeline();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {

                Toast.makeText(TimelineActivity.this, "Unable to post your Tweet", Toast.LENGTH_SHORT).show();
            }
        }, tweetBody);

    }


}
