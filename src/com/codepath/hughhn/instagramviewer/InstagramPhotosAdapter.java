package com.codepath.hughhn.instagramviewer;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
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
		CircularImageView imgAvatar = (CircularImageView) convertView.findViewById(R.id.imgAvatar);
		TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
		ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.imgPhoto);
		TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
		TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
		
		tvUsername.setText(Html.fromHtml("<font color=\"#206199\"><b>" + photo.username + "</b></font>"));
		tvLikes.setText(Html.fromHtml("<font color=\"#206199\"><b>" + String.valueOf(photo.likes_count) + " likes" + "</b></font>"));
		tvCaption.setText(photo.caption);
		
		// Reset image from recycled view
		imgAvatar.setImageResource(0);
		imgPhoto.setImageResource(0);
//		imgAvatar.setBorderColor(getContext().getResources().getColor(R.color.GrayLight));
//		imgAvatar.setBorderWidth(10);
//		imgAvatar.setSelectorColor(getContext().getResources().getColor(R.color.BlueLightTransparent));
//		imgAvatar.setSelectorStrokeColor(getContext().getResources().getColor(R.color.BlueDark));
//		imgAvatar.setSelectorStrokeWidth(10);
		imgAvatar.addShadow();
		
		// Load photo into image view
		Picasso.with(getContext()).load(photo.profileImgUrl).into(imgAvatar);
		// vHeight = vWidth * oHeight / oWidth
		imgPhoto.getLayoutParams().width = convertView.getWidth();
		imgPhoto.getLayoutParams().height = imgPhoto.getLayoutParams().width * photo.imageHeight / photo.imageWidth;
		Log.i("DEBUG", String.valueOf(imgPhoto.getLayoutParams().width));
		Log.i("DEBUG", String.valueOf(imgPhoto.getLayoutParams().height));
		Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);
		
		return convertView;
	}
}
