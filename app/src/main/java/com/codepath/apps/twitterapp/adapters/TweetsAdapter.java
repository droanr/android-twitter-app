package com.codepath.apps.twitterapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.TwitterApplication;
import com.codepath.apps.twitterapp.activities.TweetDetailActivity;
import com.codepath.apps.twitterapp.fragments.CreateTweetFragment;
import com.codepath.apps.twitterapp.models.Tweet;
import com.codepath.apps.twitterapp.models.User;
import com.codepath.apps.twitterapp.utils.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * Created by drishi on 8/18/16.
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {
    private ArrayList<Tweet> mTweets;

    public Context getContext() {
        return mContext;
    }

    private Context mContext;

    private User mAuthenticatedUser;

    public User getmAuthenticatedUser() { return mAuthenticatedUser; }

    public TweetsAdapter(Context context, ArrayList<Tweet> tweets) {
        mContext = context;
        mTweets = tweets;
        TwitterClient client = TwitterApplication.getRestClient();
        client.getAuthenticatedUser(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                mAuthenticatedUser = User.fromJSON(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG", errorResponse.toString());
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, parent, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.ivTweetImage.setImageDrawable(null);
        final Tweet tweet = mTweets.get(position);
        holder.tvUserHandle.setText(tweet.getUser().getScreenName().toString());
        holder.tvUserName.setText(tweet.getUser().getName().toString());
        holder.tvTweet.setText(tweet.getBody().toString());
        holder.tvTime.setText(getRelativeTimeAgo(tweet.getCreatedAt()));
        holder.tvRetweetCount.setText(String.valueOf(tweet.getRetweetCount()));
        holder.tvFavoritesCount.setText(String.valueOf(tweet.getFavoritesCount()));

        if (tweet.isRetweeted()) {
            holder.ivRetweet.setBackgroundResource(R.drawable.retweeted);
        }

        if (tweet.getImageUrl() != null) {
            Glide.with(getContext()).load(tweet.getImageUrl()).into(holder.ivTweetImage);
        }

        holder.ivRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterClient client = TwitterApplication.getRestClient();
                client.postRetweet(tweet.getUid(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Log.d("DEBUG", response.toString());
                        Tweet t = Tweet.fromJSON(response);
                        mTweets.add(0, t);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Log.d("DEBUG", errorResponse.toString());
                        try {
                            if (errorResponse.getJSONArray("errors").getJSONObject(0).getInt("code") == 327) {
                                Toast.makeText(getContext(), "You have already retweeted this tweet", Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        holder.ivReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = ((AppCompatActivity)getContext()).getSupportFragmentManager();
                CreateTweetFragment fragment = CreateTweetFragment.newInstance(getmAuthenticatedUser(), tweet.getBody(), tweet.getUid());
                fragment.show(fm, "create_tweet_fragment");
            }
        });

        Glide.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(holder.ivUserImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.ivUserImage)
        ImageView ivUserImage;

        @BindView(R.id.tvUserHandle)
        TextView tvUserHandle;

        @BindView(R.id.tvUserName)
        TextView tvUserName;

        @BindView(R.id.tvTweet)
        TextView tvTweet;

        @BindView(R.id.tvTime)
        TextView tvTime;

        @BindView(R.id.ivReply)
        ImageView ivReply;

        @BindView(R.id.ivRetweet)
        ImageView ivRetweet;

        @BindView(R.id.tvRetweetCount)
        TextView tvRetweetCount;

        @BindView(R.id.ivLike)
        ImageView ivLike;

        @BindView(R.id.tvFavoritesCount)
        TextView tvFavoritesCount;

        @BindView(R.id.ivTweetImage)
        ImageView ivTweetImage;

        public ViewHolder (View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent i = new Intent(getContext(), TweetDetailActivity.class);
            int position = getLayoutPosition();
            Tweet tweet = mTweets.get(position);
            i.putExtra("tweet", Parcels.wrap(tweet));
            getContext().startActivity(i);
        }
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