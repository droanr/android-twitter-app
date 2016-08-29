package com.codepath.apps.twitterapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.codepath.apps.twitterapp.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

/**
 * Created by drishi on 8/28/16.
 */
public class UserTimelineFragment extends TweetsListFragment {

    TwitterClient client;
    User mUser;

    public static UserTimelineFragment newInstance(User user) {
        UserTimelineFragment userTimelineFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", Parcels.wrap(user));
        userTimelineFragment.setArguments(args);
        return userTimelineFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        mUser = Parcels.unwrap(getArguments().getParcelable("user"));
        populateTimeline(-1);
    }

    // Send an API request to get the timeline json
    // fill the RecyclerView by creating the tweet objects from the json
    public void populateTimeline(long maxId) {
        client.getUserTimeline(maxId, mUser.getScreenName(), new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Deserialize + create models.
                // Add them to the adapter
                tweets.addAll(Tweet.fromJSONArray(response));
                adapter.notifyDataSetChanged();
            }


            // Failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
