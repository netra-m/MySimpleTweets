package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.Tweet;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by netram on 2/8/15.
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet> {

    public TweetsArrayAdapter(Context context,List<Tweet> tweetsList) {
        super(context,0,tweetsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Tweet tweet = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
        }

        ImageView ivProfileImage = (ImageView ) convertView.findViewById(R.id.ivProfileImage);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
        TextView tvRelativeTime = (TextView) convertView.findViewById(R.id.tvRelativeTime);

        tvUserName.setText(tweet.getUser().getName());
        tvScreenName.setText("@"+ tweet.getUser().getScreenName());
        tvBody.setText(tweet.getBody());
        tvRelativeTime.setText(Tweet.getRelativeTimeAgo(tweet.getCreatedAt()));
        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(ivProfileImage);

        return convertView;


    }
}
