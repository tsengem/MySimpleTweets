package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class ReplyActivity extends AppCompatActivity {

    // instance fields

    EditText et_tweet;
    TextView et_counter;

    TwitterClient client;
    Tweet tweet;
    Button b_reply;

    TweetAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        b_reply = findViewById(R.id.b_reply_tweet);

        client = TwitterApp.getRestClient(this);

        et_tweet = findViewById(R.id.et_reply_tweet);
        et_counter = findViewById(R.id.et_reply_counter);
    }

    public void composeTweet(View view) {
        TwitterClient c = TwitterApp.getRestClient(this);
        c.sendTweet(et_tweet.getText().toString(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    tweet = Tweet.fromJSON(response);

                    // prepare data intent
                    Intent intent = new Intent();

                    // pass relevant data back as a result
                    intent.putExtra("tweet", Parcels.wrap(tweet));

                    // activity finished without errors, return data
                    setResult(RESULT_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("SendTweet", responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("SendTweet", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("SendTweet", errorResponse.toString());
            }
        });
    }
}
