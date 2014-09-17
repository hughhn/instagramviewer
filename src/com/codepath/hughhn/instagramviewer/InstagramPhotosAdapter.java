package com.codepath.hughhn.instagramviewer;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Point;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {
	private int screenWidth;
	private DecimalFormat formatter;

	public InstagramPhotosAdapter(Context context,
			ArrayList<InstagramPhoto> photos) {
		super(context, R.layout.item_photo, photos);
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		screenWidth = size.x;
		formatter = new DecimalFormat("#,###,###");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get data item at position
		InstagramPhoto photo = getItem(position);

		// Check if recycled view
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_photo, parent, false);
		}

		// Look up subviews within template
		CircularImageView imgAvatar = (CircularImageView) convertView
				.findViewById(R.id.imgAvatar);
		TextView tvUsername = (TextView) convertView
				.findViewById(R.id.tvUsername);
		TextView tvCreatedTime = (TextView) convertView
				.findViewById(R.id.tvCreatedTime);
		ImageView imgPhoto = (ImageView) convertView
				.findViewById(R.id.imgPhoto);
		TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
		TextView tvCaption = (TextView) convertView
				.findViewById(R.id.tvCaption);
		TextView tvComment1 = (TextView) convertView
				.findViewById(R.id.tvComment1);
		TextView tvComment2 = (TextView) convertView
				.findViewById(R.id.tvComment2);
		tvCaption.setVisibility(4);
		tvComment1.setVisibility(4);
		tvComment2.setVisibility(4);

		tvUsername.setText(Html.fromHtml("<font color=\"#206199\"><b>"
				+ photo.username + "</b></font>"));
		tvCreatedTime.setText(Html.fromHtml("<i>"
				+ DateUtils.getRelativeTimeSpanString(photo.created_time * 1000,
						System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS) + "</i>"));

		String formattedLikesCount = formatter.format(photo.likes_count);
		tvLikes.setText(Html.fromHtml("<font color=\"#206199\"><b>"
				+ formattedLikesCount + " likes" + "</b></font>"));
		if (photo.caption != null) {
			tvCaption.setText(Html.fromHtml("<i>" + photo.caption + "</i>"));
			tvCaption.setVisibility(0);
		}

		if (photo.comments != null) {
			tvComment1.setText(Html.fromHtml("<font color=\"#206199\"><b>"
					+ photo.comments.get(photo.comments.size() - 1).username
					+ "</b></font> "
					+ photo.comments.get(photo.comments.size() - 1).text));
			tvComment1.setVisibility(0);
			if (photo.comments.size() > 1) {
				tvComment2
						.setText(Html.fromHtml("<font color=\"#206199\"><b>"
								+ photo.comments.get(photo.comments.size() - 2).username
								+ "</b></font> "
								+ photo.comments.get(photo.comments.size() - 2).text));
				tvComment2.setVisibility(0);
			}
		}

		// Reset image from recycled view
		imgAvatar.setImageResource(0);
		imgAvatar.addShadow();
		imgPhoto.setImageResource(0);

		// Load photo into image view
		Picasso.with(getContext()).load(photo.profileImgUrl).into(imgAvatar);
		// vHeight = vWidth * oHeight / oWidth
		imgPhoto.getLayoutParams().width = screenWidth;
		imgPhoto.getLayoutParams().height = screenWidth * photo.imageHeight
				/ photo.imageWidth;
		Log.i("DEBUGG",
				"width = " + String.valueOf(imgPhoto.getLayoutParams().width));
		Log.i("DEBUGG",
				"height = " + String.valueOf(imgPhoto.getLayoutParams().height));
		Picasso.with(getContext()).load(photo.imageUrl).into(imgPhoto);

		return convertView;
	}
}
