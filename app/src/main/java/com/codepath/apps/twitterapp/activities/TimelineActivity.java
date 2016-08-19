package com.codepath.apps.twitterapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;

import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.adapters.TweetsAdapter;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.utils.EndlessRecyclerViewScrollListener;
import com.codepath.apps.twitterapp.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class TimelineActivity extends AppCompatActivity {

    TwitterClient client;
    ArrayList<Tweet> tweets;

    @BindView(R.id.rvTweets)
    RecyclerView rvTweets;

    TweetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        setupViews();
        populateTimeline();
    }

    public void setupViews() {
        ButterKnife.bind(this);
        client = TwitterApplication.getRestClient(); // singleton client
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(this, tweets);
        rvTweets.setAdapter(adapter);
        rvTweets.setItemAnimator(new SlideInUpAnimator());
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) rvTweets.getLayoutManager()) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                // TODO add infinte scroll here
            }
        });
    }

    // Send an API request to get the timeline json
    // fill the RecyclerView by creating the tweet objects from the json
    public void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
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

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }
}
