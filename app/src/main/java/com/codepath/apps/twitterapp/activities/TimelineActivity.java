package com.codepath.apps.twitterapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.adapters.TweetsAdapter;
import com.codepath.apps.twitterapp.fragments.CreateTweetFragment;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
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

public class TimelineActivity extends AppCompatActivity implements CreateTweetFragment.CreateTweetFragmentListener {

    TwitterClient client;
    ArrayList<Tweet> tweets;

    @BindView(R.id.rvTweets)
    RecyclerView rvTweets;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    TweetsAdapter adapter;

    User authenticatedUser;

    private static final long DEFAULT_MAX = -1;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupViews();
        setCurrentUser();
        populateTimeline(DEFAULT_MAX);
    }

    public void setCurrentUser() {
        client.getAuthenticatedUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                authenticatedUser = User.fromJSON(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
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
                long maxId = DEFAULT_MAX;
                if (tweets.size() > 0) {
                    maxId = tweets.get(tweets.size() - 1).getUid();
                }
                populateTimeline(maxId);
            }
        });
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                populateTimeline(DEFAULT_MAX);
                swipeContainer.setRefreshing(false);
            }
        });
    }

    // Send an API request to get the timeline json
    // fill the RecyclerView by creating the tweet objects from the json
    public void populateTimeline(long maxId) {
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
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

    // Inflate the menu; this adds items to the action bar if it is present.
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    public void onActionTweet(MenuItem item) {
        FragmentManager fm = getSupportFragmentManager();
        CreateTweetFragment fragment = CreateTweetFragment.newInstance(authenticatedUser);
        fragment.show(fm, "create_tweet_fragment");
    }

    @Override
    public void onCreateNewTweet(String tweet) {
        client.postNewTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                tweets.add(0, Tweet.fromJSON(response));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}