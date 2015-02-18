package com.codepath.apps.mysimpletweets.utils;

import android.content.Context;
import android.util.Log;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class;
    public static final String REST_URL = "https://api.twitter.com/1.1/";
    public static final String REST_CONSUMER_KEY = "G9mcb0I60a4YMfjLgRfb8FKxT";
    public static final String REST_CONSUMER_SECRET = "VVEckqg6iF82JUjnHYZe1ylYbiAOycFxq3uinBoE4cwNOGyQqF";
    public static final String REST_CALLBACK_URL = "oauth://nmsimpletweets";

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }


    public void getHomeTimeline(AsyncHttpResponseHandler handler, long lastVisibleUid) {
        Log.i("INFO", "Twitter API called with lastVisibleUid=" + lastVisibleUid);

        String apiUrl = getApiUrl("statuses/home_timeline.json");

        RequestParams params = new RequestParams();
        params.put("count", "25");
        params.put("since_id", "1");
        if (lastVisibleUid != Long.MAX_VALUE) {
            params.put("max_id", Long.toString(lastVisibleUid - 1));
        }
        getClient().get(apiUrl, params, handler);
    }

    public void getCurrentUser(AsyncHttpResponseHandler handler) {

        String apiUrl = getApiUrl("account/verify_credentials.json");

        getClient().get(apiUrl, null, handler);

    }

    public void postTweet(AsyncHttpResponseHandler handler, String status) {

        String apiUrl = getApiUrl("statuses/update.json");

        RequestParams params = new RequestParams();
        params.put("status", status);

        getClient().post(apiUrl, params, handler);

    }

    public void getMentionsTimeline(JsonHttpResponseHandler jsonHttpResponseHandler, long lastVisibleUid) {

        Log.i("INFO", "Twitter Mentions API called with lastVisibleUid=" + lastVisibleUid);

        String apiUrl = getApiUrl("statuses/mentions_timeline.json");

        RequestParams params = new RequestParams();
        params.put("count", "25");
        if (lastVisibleUid != Long.MAX_VALUE) {
            params.put("max_id", Long.toString(lastVisibleUid - 1));
        }
        getClient().get(apiUrl, params, jsonHttpResponseHandler);
    }

    public void getUserTimeline(JsonHttpResponseHandler jsonHttpResponseHandler, long lastVisibleUid, String screenName) {

        String apiUrl = getApiUrl("statuses/user_timeline.json");

        RequestParams params = new RequestParams();
        params.put("screen_name",screenName);
        params.put("count", "25");
        if (lastVisibleUid != Long.MAX_VALUE) {
            params.put("max_id", Long.toString(lastVisibleUid - 1));
        }
        getClient().get(apiUrl, params, jsonHttpResponseHandler);
    }

    public void retweet(long uid, AsyncHttpResponseHandler handler) {

        String apiUrl = getApiUrl("statuses/retweet/"+Long.toString(uid)+".json");

        getClient().post(apiUrl, null, handler);

    }

    public void favorite(long uid, AsyncHttpResponseHandler handler) {

        String apiUrl = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id",Long.toString(uid));

        getClient().post(apiUrl, params, handler);

    }

    public void getFollowers(AsyncHttpResponseHandler handler, String screenName) {

        String apiUrl = getApiUrl("followers/list.json");
        RequestParams params = new RequestParams();
        params.put("count","200");
        params.put("screen_name",screenName);

        getClient().get(apiUrl, params, handler);

    }

    public void getFollowing(AsyncHttpResponseHandler handler, String screenName) {

        String apiUrl = getApiUrl("friends/list.json");
        RequestParams params = new RequestParams();
        params.put("count","200");
        params.put("screen_name",screenName);

        getClient().get(apiUrl, params, handler);

    }







	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
     * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}