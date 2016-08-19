package com.codepath.apps.twitterapp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by drishi on 8/17/16.
 */
/*
    [
        {
             "text": "just another test",
            "contributors": null,
            "id": 240558470661799936,
            "retweet_count": 0,
            "in_reply_to_status_id_str": null,
            "geo": null,
            "retweeted": false,
            "in_reply_to_user_id": null,
            "place": null,
            "source": "<a href="//realitytechnicians.com%5C%22" rel="\"nofollow\"">OAuth Dancer Reborn</a>",
            "user": {
              "name": "OAuth Dancer",
              "profile_sidebar_fill_color": "DDEEF6",
              "profile_background_tile": true,
              "profile_sidebar_border_color": "C0DEED",
              "profile_image_url":"http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
             ...
             }
        },
    ]
*/

@Parcel
public class Tweet {
    // list out the attributes
    private String body;

    public Long getUid() {
        return uid;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    private Long uid;

    public User getUser() {
        return user;
    }

    private User user;
    private String createdAt;

    // Deserialize the JSONObject
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.createdAt = jsonObject.getString("created_at");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // tweet.user
        return tweet;

    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Tweet tweet = Tweet.fromJSON(jsonObject);
                if (tweet != null) {
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                // continue to process other tweets if one fails
                continue;
            }
        }

        return tweets;
    }
}
