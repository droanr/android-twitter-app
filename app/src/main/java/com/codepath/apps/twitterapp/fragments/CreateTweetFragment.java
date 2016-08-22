package com.codepath.apps.twitterapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.twitterapp.R;
import com.codepath.apps.twitterapp.models.User;

import org.parceler.Parcels;

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

    @BindView(R.id.ivClose)
    ImageView ivClose;

    @BindView(R.id.ivUserImage)
    ImageView ivUserImage;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.tvUserHandle)
    TextView tvUserHandle;

    @BindView(R.id.tvCharCount)
    TextView tvCharCount;

    TextWatcher textEditorWatcher;

    User authenticatedUser;
    String body;

    Long inReplyToId;

    public CreateTweetFragment() {

    }

    public interface CreateTweetFragmentListener {
        void onCreateNewTweet(String tweet);
        void onReplyToTweet(String tweet, long inReplyTo);
    }

    public static CreateTweetFragment newInstance(User user, String body, long inReplyToId) {
        CreateTweetFragment fragment = new CreateTweetFragment();
        Bundle args = new Bundle();
        args.putParcelable("user", Parcels.wrap(user));
        args.putString("body", body);
        args.putLong("reply_to", inReplyToId);
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
        //getDialog().getWindow().setLayout(500, 700);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setUpViews();
        User user = (User) Parcels.unwrap(getArguments().getParcelable("user"));
        String body = getArguments().getString("body");
        authenticatedUser = user;
        inReplyToId = getArguments().getLong("reply_to");
        body = body;
        if (body.length() > 0) {
            etTweet.setText(body);
            etTweet.setSelection(body.length());
        }
        if (user != null) {
            tvUserName.setText(user.getName());
            tvUserHandle.setText(authenticatedUser.getScreenName());
            Glide.with(this).load(authenticatedUser.getProfileImageUrl()).into(ivUserImage);
        }
    }

    private void setUpViews() {
        textEditorWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                tvCharCount.setText(String.valueOf(s.length()));
            }

            public void afterTextChanged(Editable s) {
            }
        };
        etTweet.addTextChangedListener(textEditorWatcher);
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweet = etTweet.getText().toString().trim();
                if (tweet.length() > 0) {
                    if (inReplyToId != -1) {
                        CreateTweetFragmentListener listener = (CreateTweetFragmentListener) getActivity();
                        listener.onCreateNewTweet(tweet);
                        close();
                    } else {

                    }
                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
            }
        });
    }

    public void close() {
        unbinder.unbind();
        dismiss();
    }

}
