package com.codepath.hughhn.instagramviewer;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class PhotosActivity extends Activity {
	public static final String CLIENT_ID = "627ff804fd224a5a9c2d7996c664164a";
	private ArrayList<InstagramPhoto> mPhotos;
	private InstagramPhotosAdapter aPhotos;
	private SwipeRefreshLayout swipeContainer;
	private ListView lvPhotos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photos);

		// Initialize photos and adapter
		mPhotos = new ArrayList<InstagramPhoto>();
		aPhotos = new InstagramPhotosAdapter(this, mPhotos);

		// Populate data to list view (adapter -> list view)
		lvPhotos = (ListView) findViewById(R.id.lvPhotos);
		lvPhotos.setAdapter(aPhotos);

		fetchPopularPhotos();

		swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
		// Setup refresh listener which triggers new data loading
		swipeContainer.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list here.
				// Make sure you call swipeContainer.setRefreshing(false)
				// once the network request has completed successfully.
				fetchPopularPhotos();
			}
		});
		// Configure the refreshing colors
		swipeContainer.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
	}

	private void fetchPopularPhotos() {
		// https://api.instagram.com/v1/media/popular?client_id=627ff804fd224a5a9c2d7996c664164a
		mPhotos.clear();
		aPhotos.clear();

		// Set up popular URL endpoint
		String popularUrl = "https://api.instagram.com/v1/media/popular?client_id="
				+ CLIENT_ID;

		// Create network client
		AsyncHttpClient client = new AsyncHttpClient();

		// Trigger network request
		client.get(popularUrl, new JsonHttpResponseHandler() {
			// define success & failure callbacks
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// data -> [x] -> user -> username
				// data -> [x] -> caption -> text
				// data -> [x] -> images -> standard_resolution -> url
				// data -> [x] -> images -> standard_resolution -> height
				// data -> [x] -> likes -> count
				JSONArray photosJSON = null;

				try {
					photosJSON = response.getJSONArray("data");
					for (int i = 0; i < photosJSON.length(); i++) {
						JSONObject photoJSON = photosJSON.getJSONObject(i);
						InstagramPhoto photo = new InstagramPhoto();
						photo.username = photoJSON.getJSONObject("user")
								.getString("username");
						photo.profileImgUrl = photoJSON.getJSONObject("user")
								.getString("profile_picture");
						if (photoJSON.optJSONObject("caption") != null) {
							photo.caption = photoJSON.getJSONObject("caption")
									.getString("text");
						}
						photo.imageUrl = photoJSON.getJSONObject("images")
								.getJSONObject("standard_resolution")
								.getString("url");
						photo.imageHeight = photoJSON.getJSONObject("images")
								.getJSONObject("standard_resolution")
								.getInt("height");
						photo.imageWidth = photoJSON.getJSONObject("images")
								.getJSONObject("standard_resolution")
								.getInt("width");
						photo.likes_count = photoJSON.getJSONObject("likes")
								.getInt("count");
						mPhotos.add(photo);
					}

					// Notify adapter of data changes
					aPhotos.notifyDataSetChanged();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					String responseString, Throwable throwable) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photos, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
