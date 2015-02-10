package com.codepath.apps.mysimpletweets.models;

import android.text.format.DateUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by netram on 2/8/15.
 */
@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {

    @Column(name = "body")
    private String body;

    @Column(name = "uid", unique = true)
    private long uid; //database id for the tweet

    @Column(name = "user")
    private User user;

    @Column(name = "createdAt")
    private String createdAt;

    @Column(name = "embeddedImageURL")
    private String embeddedImageURL;

    @Column(name = "retweetCount")
    private int retweetCount;

    @Column(name = "favoriteCount")
    private int favoriteCount;

    @Column(name="retweetedUser")
    private User retweetedUser;

    public User getRetweetedUser() {
        return retweetedUser;
    }

    public void setRetweetedUser(User retweetedUser) {
        this.retweetedUser = retweetedUser;
    }

    public String getEmbeddedImageURL() {
        return embeddedImageURL;
    }

    public void setEmbeddedImageURL(String embeddedImageURL) {
        this.embeddedImageURL = embeddedImageURL;
    }

    public int getRetweetCount() {
        return retweetCount;
    }

    public void setRetweetCount(int retweetCount) {
        this.retweetCount = retweetCount;
    }

    public int getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(int favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public Tweet() {

    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();

        try {

            if(jsonObject.has("retweeted_status")) {
                tweet.setRetweetedUser(User.fromJSON(jsonObject.getJSONObject("user")));
                jsonObject = jsonObject.getJSONObject("retweeted_status");
            }

            tweet.setBody(jsonObject.getString("text"));
            tweet.setUid(jsonObject.getLong("id"));
            tweet.setCreatedAt(jsonObject.getString("created_at"));
            tweet.setUser(User.fromJSON(jsonObject.getJSONObject("user")));
            tweet.setRetweetCount(jsonObject.getInt("retweet_count"));
            tweet.setFavoriteCount(jsonObject.getInt("favorite_count"));


            JSONObject entity = jsonObject.getJSONObject("entities");
            if (entity != null) {
                JSONArray media = entity.getJSONArray("media");
                if (media != null) {
                    JSONObject image = media.getJSONObject(0);
                    if (image != null) {
                        tweet.setEmbeddedImageURL(image.getString("media_url"));
                    }

                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static List<Tweet> fromJSONArray(JSONArray jsonArray) {

        List<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                Tweet tweet = fromJSON(jsonArray.getJSONObject(i));
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");

    public static String getRelativeTimeAgo(String createdAtTime, boolean truncate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(createdAtTime).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (truncate) {
            if (relativeDate.contains(" seconds ago")) {
                relativeDate = relativeDate.replace(" seconds ago", "s");
            } else if (relativeDate.contains(" minutes ago")) {
                relativeDate = relativeDate.replace(" minutes ago", "m");
            } else if (relativeDate.contains(" hours ago")) {
                relativeDate = relativeDate.replace(" hours ago", "h");
            } else if (relativeDate.contains(" hour ago")) {
                relativeDate = relativeDate.replace(" hour ago", "h");
            }
        }

        return relativeDate;
    }

    public static List<Tweet> getAll() {
        return new Select()
                .from(Tweet.class)
                .orderBy("Name ASC")
                .execute();
    }


}
