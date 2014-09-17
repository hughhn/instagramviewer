package com.codepath.hughhn.instagramviewer;

import java.util.ArrayList;

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
}
