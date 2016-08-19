package com.codepath.apps.twitterapp.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by drishi on 8/17/16.
 */
/*
            "user": {
              "name": "OAuth Dancer",
              "profile_sidebar_fill_color": "DDEEF6",
              "profile_background_tile": true,
              "profile_sidebar_border_color": "C0DEED",
              "profile_image_url":"http://a0.twimg.com/profile_images/730275945/oauth-dancer_normal.jpg",
             ...
             }
 */

@Parcel
public class User {
    // list the attributes
    String name;
    Long uid;

    public String getName() {
        return name;
    }

    public Long getUid() {
        return uid;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    String screenName;
    String profileImageUrl;

    //deserialize user JSON --> User
    public static User fromJSON(JSONObject json) {
        User u = new User();

        try {
            u.name = json.getString("name");
            u.uid = json.getLong("id");
            u.screenName = json.getString("screen_name");
            u.profileImageUrl = json.getString("profile_image_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }



        return u;
    }
}
