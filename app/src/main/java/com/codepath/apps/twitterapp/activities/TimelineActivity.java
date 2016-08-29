package com.codepath.apps.twitterapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.fragments.CreateTweetFragment;
import com.codepath.apps.twitterapp.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterapp.fragments.MentionsTimelineFragment;
import com.codepath.apps.twitterapp.fragments.TweetsListFragment;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.codepath.apps.twitterapp.utils.SmartFragmentStatePagerAdapter;
import com.codepath.apps.twitterapp.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity implements CreateTweetFragment.CreateTweetFragmentListener {

    TwitterClient client;
    User authenticatedUser;
    TweetsListFragment fragmentTweetsList;

    @BindView(R.id.viewpager)
    ViewPager vpPager;

    @BindView(R.id.tabs)
    PagerSlidingTabStrip tabStrip;

    TweetsPagerAdapter adapter;

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
        setCurrentUser();
        adapter = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapter);
        tabStrip.setViewPager(vpPager);
        if (savedInstanceState == null) {
            fragmentTweetsList = (HomeTimelineFragment) adapter.getRegisteredFragment(0);
        }

    }

    public void setCurrentUser() {
        ButterKnife.bind(this);
        client = TwitterApplication.getRestClient(); // singleton client
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
/*
    public void setupViews() {
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
*/
    // Send an API request to get the timeline json
    // fill the RecyclerView by creating the tweet objects from the json
    public void populateTimeline(long maxId) {
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler() {
            // Success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Deserialize + create models.
                // Add them to the adapter
                fragmentTweetsList.addAll(Tweet.fromJSONArray(response));
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
        CreateTweetFragment fragment = CreateTweetFragment.newInstance(authenticatedUser, "", -1);
        fragment.show(fm, "create_tweet_fragment");
    }

    @Override
    public void onCreateNewTweet(String tweet) {
        client.postNewTweet(tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                fragmentTweetsList.add(0, Tweet.fromJSON(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    @Override
    public void onReplyToTweet(String tweet, long inReplyTo) {
        client.postReplyToTweet(inReplyTo, tweet, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                fragmentTweetsList.add(0, Tweet.fromJSON(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    public void onProfileView(MenuItem item) {
        // Launch the profile view
        Intent i = new Intent(this, ProfileActivity.class);
        i.putExtra("user", Parcels.wrap(authenticatedUser));
        startActivity(i);
    }

    // Return the order of fragments in the view pager
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter {

        private String tabTitles[] = {"Home", "Mentions"};

        public TweetsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new HomeTimelineFragment();
            } else if (position == 1) {
                return new MentionsTimelineFragment();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }
}