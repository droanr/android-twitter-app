package com.codepath.apps.twitterapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by drishi on 8/25/16.
 */
public class TweetsListFragment extends Fragment{
    TwitterClient client;
    ArrayList<Tweet> tweets;

    @BindView(R.id.rvTweets)
    RecyclerView rvTweets;

    @BindView(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    TweetsAdapter adapter;

    Unbinder unbinder;

    private static final long DEFAULT_MAX = -1;

    // inflation logic
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, parent, false);
        unbinder = ButterKnife.bind(this, v);
        client = TwitterApplication.getRestClient();
        rvTweets.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvTweets.setAdapter(adapter);

        rvTweets.setItemAnimator(new SlideInUpAnimator());
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
        populateTimeline(DEFAULT_MAX);
        return v;
    }


    // creation lifecycle event
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViews();
    }

    public void setupViews() {
        tweets = new ArrayList<>();
        adapter = new TweetsAdapter(getActivity(), tweets);

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

    public void addAll(List<Tweet> tweets) {
        tweets.addAll(tweets);
        adapter.notifyDataSetChanged();
    }

    public void add(int position, Tweet tweet) {
        tweets.add(position, tweet);
        adapter.notifyDataSetChanged();
        rvTweets.scrollToPosition(0);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
