package com.codepath.apps.mysimpletweets.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.ComposeFragment;
import com.codepath.apps.mysimpletweets.models.ComposeCallBack;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

public class TweetDetailActivity extends ActionBarActivity implements ComposeCallBack {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4099FF")));
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.timeline_action_bar, null);

        ((TextView) mCustomView.findViewById(R.id.title_text)).setText("Tweet");

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        setContentView(R.layout.activity_tweet_detail);

        final Tweet tweet = (Tweet) getIntent().getSerializableExtra("tweet");

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ibProfileImage);
        TextView tvBody = (TextView) findViewById(R.id.tvBody);
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        TextView tvRelativeTime = (TextView) findViewById(R.id.tvRelativeTime);
        TextView tvRetweetCount = (TextView) findViewById(R.id.tvRetweetCount);
        TextView tvFavoritesCount = (TextView) findViewById(R.id.tvFavoritesCount);
        ImageButton btnReply = (ImageButton) findViewById(R.id.btnReply);
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                ComposeFragment composeFragment = ComposeFragment.newInstance("Compose Tweet");
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", TimelineActivity.getCurrentUser());
                bundle.putString("replyTo", tweet.getUser().getScreenName());
                composeFragment.setArguments(bundle);
                composeFragment.show(fm, "fragment_compose");
            }
        });

        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(Html.fromHtml(tweet.getBody()));
        tvRelativeTime.setText(Tweet.getRelativeTimeAgo(tweet.getCreatedAt(),false));
        tvRetweetCount.setText(Integer.toString(tweet.getRetweetCount()));
        tvFavoritesCount.setText(Integer.toString(tweet.getFavoriteCount()));
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        ImageView ivTweetDetail = (ImageView) findViewById(R.id.ivTweetDetail);

        if(tweet.getEmbeddedImageURL() != null) {
            ivTweetDetail.setVisibility(View.VISIBLE);
            Picasso.with(this).load(tweet.getEmbeddedImageURL()).into(ivTweetDetail);
        }
        else {
            ivTweetDetail.setVisibility(View.GONE);
        }

        //retweet related
        ImageView ivRetweeted = (ImageView) findViewById(R.id.ivRetweeted);
        TextView tvRetweeted = (TextView) findViewById(R.id.tvRetweeted);

        if(tweet.getRetweetedUser() != null) {
            ivRetweeted.setVisibility(View.VISIBLE);
            tvRetweeted.setVisibility(View.VISIBLE);
            tvRetweeted.setText(tweet.getRetweetedUser().getName()+" retweeted");

        }
        else {
            ivRetweeted.setVisibility(View.GONE);
            tvRetweeted.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tweet_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCompose(String tweetBody) {

        Intent intent = new Intent();
        intent.putExtra("tweetBody",tweetBody);
        setResult(RESULT_OK, intent);
        finish();

    }
}
