package ca.ualberta.cs.lonelyTweet;

import java.util.List;

import ca.ualberta.cs.lonelytwitter.ImportantLonelyTweet;
import ca.ualberta.cs.lonelytwitter.LonelyTweet;
import ca.ualberta.cs.lonelytwitter.NormalLonelyTweet;
import ca.ualberta.cs.lonelytwitter.R;
import ca.ualberta.cs.lonelytwitter.R.id;
import ca.ualberta.cs.lonelytwitter.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class LonelyTwitterActivity extends Activity {

	private EditText bodyText;
	private ListView oldTweetsList;

	private List<LonelyTweet> tweets;
	private ArrayAdapter<LonelyTweet> adapter;
	private TweetsFileManager tweetsProvider;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		bodyText = (EditText) findViewById(R.id.body);
		oldTweetsList = (ListView) findViewById(R.id.oldTweetsList);
	}

	@Override
	protected void onStart() {
		super.onStart();

		tweetsProvider = new TweetsFileManager(this);
		tweets = tweetsProvider.loadTweets();
		adapter = new ArrayAdapter<LonelyTweet>(this, R.layout.list_item,
				tweets);
		oldTweetsList.setAdapter(adapter);
	}

	public void save(View v) {
//		String text = bodyText.getText().toString();
//
//		NormalLonelyTweet tweet;
//
//		tweet = new NormalLonelyTweet(text);

		String text = bodyText.getText().toString();

		LonelyTweet tweet = determineImportance(text);
		
		if (tweet.isValid()) {
			tweets.add(tweet);
			adapter.notifyDataSetChanged();

			bodyText.setText("");
			tweetsProvider.saveTweets(tweets);
		} else {
			Toast.makeText(this, "Invalid tweet", Toast.LENGTH_SHORT).show();
		}
	}

	private LonelyTweet determineImportance(String text) {
		LonelyTweet tweet;

		if (text.contains("*")) {
			tweet = new ImportantLonelyTweet(text);
		} else {
			tweet = new NormalLonelyTweet(text);
		}
		return tweet;
	}

	public void clear(View v) {
		tweets.clear();
		adapter.notifyDataSetChanged();
		tweetsProvider.saveTweets(tweets);
	}

}