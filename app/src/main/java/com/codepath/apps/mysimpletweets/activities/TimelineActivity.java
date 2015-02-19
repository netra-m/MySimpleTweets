package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.ComposeFragment;
import com.codepath.apps.mysimpletweets.fragments.HomeTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.TweetsListFragment;
import com.codepath.apps.mysimpletweets.models.ComposeCallBack;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.codepath.apps.mysimpletweets.models.User;
import com.codepath.apps.mysimpletweets.utils.TwitterApplication;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;

public class TimelineActivity extends ActionBarActivity implements ComposeCallBack {

    public static final int REQUEST_CODE_DETAILED_ACTIVITY = 333;

    private TweetsListFragment tweetsListFragment;

    private static User currentUser;
    private ViewPager viewPager;
    private TweetsPagerAdapter tweetsPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_timeline);

        populateCurrentUser();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tweetsPagerAdapter = new TweetsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tweetsPagerAdapter);

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        tabs.setViewPager(viewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(TimelineActivity.this, SearchResultsActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Log.e("ERROR", "Current user is :" + currentUser);

        //noinspection SimplifiableIfStatement
        if (id == R.id.compose) {
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            ComposeFragment composeFragment = ComposeFragment.newInstance("Compose Tweet");
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", currentUser);
            composeFragment.setArguments(bundle);
            composeFragment.show(fm, "fragment_compose");
        } else if (id == R.id.profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("user", currentUser);
            startActivity(intent);

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
    public void onCompose(final String tweetBody) {

        tweetsListFragment.getTwitterClient().postTweet(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                viewPager.setCurrentItem(0);

                ((TweetsListFragment) tweetsPagerAdapter.getItem(0)).addToTimeLine(Tweet.fromJSON(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject jsonObject) {

                Toast.makeText(TimelineActivity.this, "Unable to post your Tweet", Toast.LENGTH_SHORT).show();
            }
        }, tweetBody);

    }


    //Return the order of fragment in the view pager
    public class TweetsPagerAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        final private int PAGE_COUNT = 2;

        private String tabTitles[] = {"Home", "Mentions"};

        private HomeTimelineFragment homeTimelineFragment;
        private MentionsTimelineFragment mentionsTimelineFragment;

        private int tabIcons[] = {R.drawable.ic_home, R.drawable.ic_mentions};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
            mentionsTimelineFragment = new MentionsTimelineFragment();
            homeTimelineFragment = new HomeTimelineFragment();
        }

        @Override
        public TweetsListFragment getItem(int position) {

            switch (position) {

                case 1:
                    tweetsListFragment = mentionsTimelineFragment;
                    return mentionsTimelineFragment;


                default:
                    tweetsListFragment = homeTimelineFragment;
                    return homeTimelineFragment;

            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public int getPageIconResId(int i) {
            return tabIcons[i];
        }
       
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    private void populateCurrentUser() {

        if (currentUser == null) {

            TwitterApplication.getRestClient().getCurrentUser(new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {

                    currentUser = User.fromJSON(jsonObject);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    com.activeandroid.util.Log.e("Error", "Failure in twitter call" + errorResponse.toString());
                }

            });
        }

    }


}
