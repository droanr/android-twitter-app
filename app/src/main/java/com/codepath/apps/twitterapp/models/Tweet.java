package com.codepath.apps.twitterapp.models;

import android.text.format.DateUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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

    public String getRelativeTime() {return getRelativeTimeAgo(getCreatedAt());}

    private Long uid;

    public User getUser() {
        return user;
    }

    private User user;
    private String createdAt;

    private String imageUrl;

    private boolean retweeted;

    private int retweetCount;

    private int favoritesCount;

    public int getFavoritesCount() { return favoritesCount; }

    public int getRetweetCount() { return retweetCount; }

    public boolean isRetweeted() { return retweeted; }

    public String getImageUrl() { return imageUrl;}

    // Deserialize the JSONObject
    public static Tweet fromJSON(JSONObject jsonObject) {
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.retweeted = jsonObject.getBoolean("retweeted");
            tweet.retweetCount = jsonObject.getInt("retweet_count");
            //tweet.favoritesCount = jsonObject.getInt("favourites_count");
            JSONObject entities = jsonObject.getJSONObject("entities");
            if (entities.has("media")) {
                JSONArray mediaArray = entities.getJSONArray("media");
                for (int x = 0; x < mediaArray.length(); x++) {
                    JSONObject media = mediaArray.getJSONObject(x);
                    tweet.imageUrl = media.getString("media_url");
                    break;
                }
            } else {
                tweet.imageUrl = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return relativeDate;
    }
}
