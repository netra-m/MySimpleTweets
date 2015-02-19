package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.utils.TwitterApplication;
import com.codepath.apps.mysimpletweets.activities.ProfileActivity;
import com.codepath.apps.mysimpletweets.activities.TimelineActivity;
import com.codepath.apps.mysimpletweets.fragments.ComposeFragment;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.List;

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

        ImageButton ibProfileImage = (ImageButton) convertView.findViewById(R.id.ibProfileImage);
        ImageView ivTweetImage = (ImageView) convertView.findViewById(R.id.ivTweetImage);

        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);
        final TextView tvRetweetCount = (TextView) convertView.findViewById(R.id.tvRetweetCount);
        final TextView tvFavoritesCount = (TextView) convertView.findViewById(R.id.tvFavoritesCount);
        ImageButton btnReply = (ImageButton) convertView.findViewById(R.id.btnReply);

        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText("@" + tweet.getUser().getScreenName());
        tvBody.setText(Html.fromHtml(tweet.getBody()));
        tvRelativeTime.setText(Tweet.getRelativeTimeAgo(tweet.getCreatedAt(), true));
        tvRetweetCount.setText(Integer.toString(tweet.getRetweetCount()));
        tvFavoritesCount.setText(Integer.toString(tweet.getFavoriteCount()));
        ibProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ibProfileImage);

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.support.v4.app.FragmentManager fm = ((TimelineActivity) getContext()).getSupportFragmentManager();
                ComposeFragment composeFragment = ComposeFragment.newInstance("Compose Tweet");
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", TimelineActivity.getCurrentUser());
                bundle.putString("replyTo", tweet.getUser().getScreenName());
                composeFragment.setArguments(bundle);
                composeFragment.show(fm, "fragment_compose");
            }
        });

        final ImageButton btnRetweet = (ImageButton) convertView.findViewById(R.id.reTweetIcon);
        if (tweet.isRetweetedByUser()) {
            btnRetweet.setImageResource(R.drawable.ic_retweeted);
        } else {
            btnRetweet.setImageResource(R.drawable.ic_retweet);
        }
        btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tweet.isRetweetedByUser()) {

                    TwitterApplication.getRestClient().undoRetweet(tweet.getUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {

                            btnRetweet.setImageResource(R.drawable.ic_retweet);
                            tvRetweetCount.setText(Integer.toString(tweet.getRetweetCount() - 1));

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            com.activeandroid.util.Log.e("Error", "Failure in twitter call" + errorResponse.toString());
                        }
                    });

                } else {

                    TwitterApplication.getRestClient().retweet(tweet.getUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {

                            btnRetweet.setImageResource(R.drawable.ic_retweeted);
                            tvRetweetCount.setText(Integer.toString(tweet.getRetweetCount() + 1));

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            com.activeandroid.util.Log.e("Error", "Failure in twitter call" + errorResponse.toString());
                        }
                    });

                }

            }
        });

        final ImageButton btnFavorite = (ImageButton) convertView.findViewById(R.id.favoritesIcon);

        if (tweet.isFavoritedByUser()) {

            btnFavorite.setImageResource(R.drawable.ic_favorited);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorites);
        }
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tweet.isFavoritedByUser()) {

                    TwitterApplication.getRestClient().unFavorite(tweet.getUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                            btnFavorite.setImageResource(R.drawable.ic_favorites);
                            tvFavoritesCount.setText(Integer.toString(tweet.getFavoriteCount() - 1));

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            com.activeandroid.util.Log.e("Error", "Failure in twitter call" + errorResponse.toString());
                        }
                    });

                } else {
                    TwitterApplication.getRestClient().favorite(tweet.getUid(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                            btnFavorite.setImageResource(R.drawable.ic_favorited);
                            tvFavoritesCount.setText(Integer.toString(tweet.getFavoriteCount() + 1));

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            com.activeandroid.util.Log.e("Error", "Failure in twitter call" + errorResponse.toString());
                        }
                    });
                }

            }
        });


        ibProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("user", tweet.getUser());
                getContext().startActivity(intent);
            }
        });

        if (tweet.getEmbeddedImageURL() != null) {
            Picasso.with(getContext()).load(tweet.getEmbeddedImageURL()).into(ivTweetImage);
            ivTweetImage.setVisibility(View.VISIBLE);
        } else {
            ivTweetImage.setVisibility(View.GONE);
        }

        //retweet related
        ImageView ivRetweeted = (ImageView) convertView.findViewById(R.id.ivRetweeted);
        TextView tvRetweeted = (TextView) convertView.findViewById(R.id.tvRetweeted);

        if (tweet.getRetweetedUser() != null) {
            ivRetweeted.setVisibility(View.VISIBLE);
            tvRetweeted.setVisibility(View.VISIBLE);
            tvRetweeted.setText(tweet.getRetweetedUser().getName() + " retweeted");

        } else {
            ivRetweeted.setVisibility(View.GONE);
            tvRetweeted.setVisibility(View.GONE);
        }

        return convertView;


    }
}
