package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

import static com.codepath.apps.restclienttemplate.TwitterClient.REST_CONSUMER_KEY;

public class ComposeActivity extends AppCompatActivity {

    // instance fields

    Button bTweet;
    EditText et_tweet;
    TextView et_counter;

    TwitterClient client;
    Tweet tweet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);

        bTweet = findViewById(R.id.bTweet);
        et_tweet = findViewById(R.id.et_tweet);
        et_counter = findViewById(R.id.et_counter);

        final TextWatcher mEditTextWatcher = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                et_counter.setText(String.valueOf(140 - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        et_tweet.addTextChangedListener(mEditTextWatcher);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.compose, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
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

                    Intent i = new Intent(ComposeActivity.this, TimelineActivity.class);
                    startActivity(i);
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