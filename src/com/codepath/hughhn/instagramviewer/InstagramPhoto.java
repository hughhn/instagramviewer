package com.codepath.hughhn.instagramviewer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InstagramPhoto {
	// username, caption, image_url, height, likes_count
	public String username;
	public String profileImgUrl;
	public String caption;
	public String imageUrl;
	public int imageHeight;
	public int imageWidth;
	public int likes_count;
	public long created_time;
	public ArrayList<PhotoComment> comments;

	public InstagramPhoto(JSONObject photoJSON) {
		try {
			JSONArray commentsJSON = null;

			username = photoJSON.getJSONObject("user").getString("username");
			profileImgUrl = photoJSON.getJSONObject("user").getString(
					"profile_picture");
			if (photoJSON.optJSONObject("caption") != null) {
				caption = photoJSON.getJSONObject("caption").getString("text");
			}
			imageUrl = photoJSON.getJSONObject("images")
					.getJSONObject("standard_resolution").getString("url");
			imageHeight = photoJSON.getJSONObject("images")
					.getJSONObject("standard_resolution").getInt("height");
			imageWidth = photoJSON.getJSONObject("images")
					.getJSONObject("standard_resolution").getInt("width");
			likes_count = photoJSON.getJSONObject("likes").getInt("count");
			created_time = photoJSON.getLong("created_time");

			commentsJSON = photoJSON.getJSONObject("comments").getJSONArray(
					"data");
			if (commentsJSON != null) {
				comments = new ArrayList<PhotoComment>();

				for (int j = 0; j < commentsJSON.length(); j++) {
					JSONObject comment = commentsJSON.getJSONObject(j);
					PhotoComment cmt = new PhotoComment();
					cmt.text = comment.getString("text");
					cmt.username = comment.getJSONObject("from").getString(
							"username");
					cmt.profileImgUrl = comment.getJSONObject("from")
							.getString("profile_picture");
					comments.add(cmt);
				}
			}
		} 
		catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
