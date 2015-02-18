package com.codepath.apps.mysimpletweets.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by netram on 2/8/15.
 */
@Table(name="Users")
public class User extends Model implements Serializable{

    @Column(name="name")
    private String name;

    @Column(name="uid", unique = true)
    private long uid;

    @Column(name = "screenName")
    private String screenName;

    @Column(name = "profileImageUrl")
    private String profileImageUrl;

    private String tagLine;

    private int followers;

    private String backgroundImage;

    private int tweetsCount;

    public int getTweetsCount() {
        return tweetsCount;
    }

    public void setTweetsCount(int tweetsCount) {
        this.tweetsCount = tweetsCount;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    private int following;

    public User() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public static User fromJSON(JSONObject jsonObject) {
        User user = new User();

        try {
            user.setName(jsonObject.getString("name"));
            user.setUid(jsonObject.getLong("id"));
            user.setScreenName(jsonObject.getString("screen_name"));
            user.setProfileImageUrl(jsonObject.getString("profile_image_url"));
            user.setFollowers(jsonObject.getInt("followers_count"));
            user.setFollowing(jsonObject.getInt("friends_count"));
            user.setTagLine(jsonObject.getString("description"));
            user.setBackgroundImage(jsonObject.getString("profile_banner_url"));
            user.setTweetsCount(jsonObject.getInt("statuses_count"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static List<User> fromJSONArray(JSONArray jsonArray) {

        List<User> users = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                User user = fromJSON(jsonArray.getJSONObject(i));
                if (user != null) {
                    users.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return users;
    }
}
