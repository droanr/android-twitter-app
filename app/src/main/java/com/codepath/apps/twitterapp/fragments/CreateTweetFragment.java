package com.codepath.apps.twitterapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.apps.twitterapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by drishi on 8/18/16.
 */
public class CreateTweetFragment extends DialogFragment {
    @BindView(R.id.etTweet)
    EditText etTweet;

    @BindView(R.id.btnTweet)
    Button btnTweet;

    Unbinder unbinder;

    public CreateTweetFragment() {

    }

    public interface CreateTweetFragmentListener {
        void onCreateNewTweet(String tweet);
    }

    public static CreateTweetFragment newInstance() {
        CreateTweetFragment fragment = new CreateTweetFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_tweet_fragment, container);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDialog().getWindow().setLayout(500, 700);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setUpViews();
    }

    private void setUpViews() {
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweet = etTweet.getText().toString().trim();
                if (tweet.length() > 0) {
                    CreateTweetFragmentListener listener = (CreateTweetFragmentListener) getActivity();
                    listener.onCreateNewTweet(tweet);
                    unbinder.unbind();
                    dismiss();
                }
            }
        });
    }

}
