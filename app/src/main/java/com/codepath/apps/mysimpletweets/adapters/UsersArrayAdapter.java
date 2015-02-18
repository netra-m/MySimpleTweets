package com.codepath.apps.mysimpletweets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by netram on 2/18/15.
 */
public class UsersArrayAdapter extends ArrayAdapter<User> {

    public UsersArrayAdapter(Context context, List<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final User user = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }

        ImageView ivUserImage = (ImageView) convertView.findViewById(R.id.ivUserImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);


        tvUserName.setText(user.getName());
        tvScreenName.setText("@" + user.getScreenName());
        Picasso.with(getContext()).load(user.getProfileImageUrl()).into(ivUserImage);

        return convertView;


    }
}
