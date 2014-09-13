package com.codepath.hughhn.instagramviewer;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
	public InstagramPhotosAdapter(Context context, ArrayList<InstagramPhoto> photos) {
		super(context, R.layout.item_photo, photos);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get data item at position
		InstagramPhoto photo = getItem(position);
		
		// Check if recycled view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
		}
		
		// Look up subviews within template
		TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
		ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
		
		tvCaption.setText(photo.caption);
		imgPhoto.getLayoutParams().height = photo.imageHeight;
		
		// Reset image from recycled view
		imgPhoto.setImageResource(0);
		
		// Load photo into image view
		Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);
		
		return convertView;
	}
}
