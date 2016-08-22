package com.codepath.apps.twitterapp.activities;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.models.Tweet;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TweetDetailActivity extends AppCompatActivity {
    @BindView(R.id.ivUserImage)
    ImageView ivUserImage;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.tvUserHandle)
    TextView tvUserHandle;

    @BindView(R.id.tvTime)
    TextView tvTime;

    @BindView(R.id.ivTweetImage)
    ImageView ivTweetImage;

    @BindView(R.id.tvTweet)
    TextView tvTweet;

    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
        setUpViews();
    }

    private void setUpViews() {
        tweet = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        tvUserName.setText(tweet.getUser().getName());
        tvUserHandle.setText(tweet.getUser().getScreenName());
        tvTime.setText(tweet.getRelativeTime());
        Glide.with(this).load(tweet.getUser().getProfileImageUrl()).into(ivUserImage);
        tvTweet.setText(tweet.getBody());
        if (tweet.getImageUrl() != null) {
            Glide.with(this).load(tweet.getImageUrl()).into(ivTweetImage);
        } else {
            ivTweetImage.setVisibility(View.INVISIBLE);
        }
    }
}
