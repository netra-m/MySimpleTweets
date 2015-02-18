package com.codepath.apps.mysimpletweets.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends ActionBarActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Get screen name
        user = (User) getIntent().getSerializableExtra("user");

        if (savedInstanceState == null) {

            //create user timeline fragment
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(user.getScreenName());

            //display the user fragment within this activity dynamically
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.flContainer, userTimelineFragment);

            fragmentTransaction.commit();
        }

        populateUserDetails(user);
    }

    private void populateUserDetails(User user) {

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ibProfileImage);
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        TextView tvUserTagline = (TextView) findViewById(R.id.tvUserTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);

        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);
        tvUserName.setText(user.getName());
        tvScreenName.setText("@" + user.getScreenName());
        tvUserTagline.setText(user.getTagLine());
        tvFollowers.setText(user.getFollowers()+" FOLLOWERS");
        tvFollowing.setText(user.getFollowing()+" FOLLOWING");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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
}
