package com.codepath.apps.twitterapp.utils;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
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
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "qFIop5Itn47DKNfFFWN3QpqW3";       // Change this
	public static final String REST_CONSUMER_SECRET = "FuG0pSTIcl3MDpbWarUpHnIkO6LWdHwvPRI9TMlQufuDqE8Ksx"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cptwitterapp"; // Change this (here and in manifest)

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

	/*
	  - GET the home timeline for the user
  	    uri = /statuses/home_timeline.json
		count=25
		since_id=1
		max_id=
	 */
    public void getHomeTimeline(long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/home_timeline.json");

        // Params
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);

        if (maxId != -1) {
            params.put("max_id", maxId);
        }

        // Execute the request
        getClient().get(apiUrl, params, handler);
    }

    public void postNewTweet(String tweet, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");

        // Params
        RequestParams params = new RequestParams();
        params.put("status", tweet);

        getClient().post(apiUrl, params, handler);
    }

    public void getAuthenticatedUser(AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("account/verify_credentials.json");

        getClient().get(apiUrl, handler);
    }

    public void postRetweet(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/retweet/" + String.valueOf(tweetId) + ".json");

        getClient().post(apiUrl, handler);
    }

    public void postReplyToTweet(long tweetId, String tweet, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/update.json");

        // Params
        RequestParams params = new RequestParams();
        params.put("in_reply_to_status_id", tweetId);
        params.put("status", tweet);

        getClient().post(apiUrl, params, handler);
    }

    public void postFavorite(long tweetId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("favorites/create.json");

        RequestParams params = new RequestParams();
        params.put("id", tweetId);

        getClient().post(apiUrl, params, handler);
    }

    public void getMentionsTimeline(long maxId, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/mentions_timeline.json");

        // Params
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);

        if (maxId != -1) {
            params.put("max_id", maxId);
        }

        // Execute the request
        getClient().get(apiUrl, params, handler);
    }

    public void getUserTimeline(long maxId, String screenName, AsyncHttpResponseHandler handler) {
        String apiUrl = getApiUrl("statuses/user_timeline.json");

        // Params
        RequestParams params = new RequestParams();
        params.put("count", 25);
        params.put("since_id", 1);
        params.put("screen_name", screenName);

        if (maxId != -1) {
            params.put("max_id", maxId);
        }

        // Execute the request
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