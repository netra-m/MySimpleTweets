package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.activities.TimelineActivity;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

import fragments.ComposeFragment;
import fragments.TweetsListFragment;

/**
 * Created by netram on 2/8/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context, List<Tweet> tweetsList) {
        super(context, 0, tweetsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Tweet tweet = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet, parent, false);
        }

        ImageView ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        ImageView ivTweetImage = (ImageView) convertView.findViewById(R.id.ivTweetImage);

        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
        TextView tvRetweetCount = (TextView) convertView.findViewById(R.id.tvRetweetCount);
        TextView tvFavoritesCount = (TextView) convertView.findViewById(R.id.tvFavoritesCount);
        ImageButton btnReply = (ImageButton) convertView.findViewById(R.id.btnReply);
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fm = ((TimelineActivity) getContext()).getSupportFragmentManager();
                ComposeFragment composeFragment = ComposeFragment.newInstance("Compose Tweet");
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", TweetsListFragment.getCurrentUser());
                bundle.putString("replyTo", tweet.getUser().getScreenName());
                composeFragment.setArguments(bundle);
                composeFragment.show(fm, "fragment_compose");
            }
        });

        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvRelativeTime.setText(Tweet.getRelativeTimeAgo(tweet.getCreatedAt(),true));
        tvRetweetCount.setText(Integer.toString(tweet.getRetweetCount()));
        tvFavoritesCount.setText(Integer.toString(tweet.getFavoriteCount()));
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        if(tweet.getEmbeddedImageURL() != null) {
            Picasso.with(getContext()).load(tweet.getEmbeddedImageURL()).into(ivTweetImage);
            ivTweetImage.setVisibility(View.VISIBLE);
        }
        else {
            ivTweetImage.setVisibility(View.GONE);
        }

        //retweet related
        ImageView ivRetweeted = (ImageView) convertView.findViewById(R.id.ivRetweeted);
        TextView tvRetweeted = (TextView) convertView.findViewById(R.id.tvRetweeted);

        if(tweet.getRetweetedUser() != null) {
            ivRetweeted.setVisibility(View.VISIBLE);
            tvRetweeted.setVisibility(View.VISIBLE);
            tvRetweeted.setText(tweet.getRetweetedUser().getName()+" retweeted");

        }
        else {
            ivRetweeted.setVisibility(View.GONE);
            tvRetweeted.setVisibility(View.GONE);
        }

        return convertView;


    }
}
