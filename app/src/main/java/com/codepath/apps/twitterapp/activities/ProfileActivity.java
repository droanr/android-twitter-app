package com.codepath.apps.twitterapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.fragments.UserTimelineFragment;
import com.codepath.apps.twitterapp.models.User;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity{

    @BindView(R.id.ivUserImage)
    ImageView ivUserImage;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.tvUserHandle)
    TextView tvUserHandle;

    @BindView(R.id.tvUserDescription)
    TextView tvUserDescription;

    @BindView(R.id.tvFollowersCount)
    TextView tvFollowersCount;

    @BindView(R.id.tvFollowingCount)
    TextView tvFollowingCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        User user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        // Populate the user info
        Glide.with(this).load(user.getProfileImageUrl()).into(ivUserImage);
        tvUserName.setText(user.getName());
        tvUserHandle.setText(user.getScreenName());
        tvUserDescription.setText(user.getDescription());
        tvFollowersCount.setText(Integer.toString(user.getFollowersCount()));
        tvFollowingCount.setText(Integer.toString(user.getFollowingCount()));

        if (savedInstanceState == null) {
            getSupportActionBar().setTitle(user.getScreenName());
            // Create the user timeline fragment
            UserTimelineFragment userTimelineFragment = UserTimelineFragment.newInstance(user);

            // Display fragment within this activity dynamically
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, userTimelineFragment);



            ft.commit(); // changes the fragments
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
