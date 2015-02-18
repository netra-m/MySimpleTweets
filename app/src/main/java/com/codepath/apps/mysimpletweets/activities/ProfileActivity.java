package com.codepath.apps.mysimpletweets.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.mysimpletweets.R;
import com.codepath.apps.mysimpletweets.fragments.FollowersListFragment;
import com.codepath.apps.mysimpletweets.fragments.FollowingTimelineFragment;
import com.codepath.apps.mysimpletweets.fragments.UserTimelineFragment;
import com.codepath.apps.mysimpletweets.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends ActionBarActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Get screen name
        user = (User) getIntent().getSerializableExtra("user");

        ViewPager viewPager = (ViewPager) findViewById(R.id.profileViewpager);
        viewPager.setAdapter(new ProfilePagerAdapter(getSupportFragmentManager(), user));

        PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.profileTabs);
        tabs.setViewPager(viewPager);


        populateUserDetails(user);

        getSupportActionBar().hide();


    }

    private void populateUserDetails(User user) {

        ImageView ivProfileImage = (ImageView) findViewById(R.id.ibProfileImage);
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        TextView tvUserTagline = (TextView) findViewById(R.id.tvUserTagline);
        ImageView ivProfileBackground = (ImageView) findViewById(R.id.ivProfileBackground);

        ivProfileImage.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(user.getProfileImageUrl()).into(ivProfileImage);

        ivProfileBackground.setImageResource(android.R.color.transparent);
        Picasso.with(this).load(user.getBackgroundImage()).into(ivProfileBackground);

        tvUserName.setText(user.getName());
        tvScreenName.setText("@" + user.getScreenName());
        tvUserTagline.setText(user.getTagLine());

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

    //Return the order of fragment in the view pager
    public class ProfilePagerAdapter extends FragmentPagerAdapter {

        final private int PAGE_COUNT = 3;

        private List<String> tabTitles;

        public ProfilePagerAdapter(FragmentManager fm, User user) {
            super(fm);
            tabTitles = new ArrayList<String>();
            tabTitles.add(user.getTweetsCount() + " Tweets");
            tabTitles.add(user.getFollowing() + " Following");
            tabTitles.add(user.getFollowers() + " Followers");

        }


        public void setTabTitles(List<String> tabTitles) {
            this.tabTitles = tabTitles;
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 1 : return FollowingTimelineFragment.newInstance(user.getScreenName());

                case 2 : return FollowersListFragment.newInstance(user.getScreenName());

                default:
                    return UserTimelineFragment.newInstance(user.getScreenName());


            }

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }
}
