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
	private static class ViewHolder {
		ImageView imgPhoto;
		CircularImageView imgAvatar;
		TextView tvUsername;
		TextView tvCreatedTime;
		TextView tvLikes;
		TextView tvCaption;
		TextView tvComment1;
		TextView tvComment2;
	}

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

		ViewHolder viewHolder; // view lookup cache stored in tag
		// Check if recycled view
		if (convertView == null) {
			viewHolder = new InstagramPhotosAdapter.ViewHolder();
			convertView = LayoutInflater.from(getContext()).inflate(
					R.layout.item_photo, parent, false);
			viewHolder.imgPhoto = (ImageView) convertView
					.findViewById(R.id.imgPhoto);
			viewHolder.imgAvatar = (CircularImageView) convertView
					.findViewById(R.id.imgAvatar);
			viewHolder.tvUsername = (TextView) convertView
					.findViewById(R.id.tvUsername);
			viewHolder.tvCreatedTime = (TextView) convertView
					.findViewById(R.id.tvCreatedTime);
			viewHolder.tvLikes = (TextView) convertView
					.findViewById(R.id.tvLikes);
			viewHolder.tvCaption = (TextView) convertView
					.findViewById(R.id.tvCaption);
			viewHolder.tvComment1 = (TextView) convertView
					.findViewById(R.id.tvComment1);
			viewHolder.tvComment2 = (TextView) convertView
					.findViewById(R.id.tvComment2);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// Initialize caption and comments to INVISIBLE
		viewHolder.tvCaption.setVisibility(4);
		viewHolder.tvComment1.setVisibility(4);
		viewHolder.tvComment2.setVisibility(4);
		
		viewHolder.tvUsername.setText(Html
				.fromHtml("<font color=\"#206199\"><b>" + photo.username
						+ "</b></font>"));
		viewHolder.tvCreatedTime.setText(Html.fromHtml("<i>"
				+ DateUtils.getRelativeTimeSpanString(
						photo.created_time * 1000, System.currentTimeMillis(),
						DateUtils.SECOND_IN_MILLIS) + "</i>"));

		String formattedLikesCount = formatter.format(photo.likes_count);
		viewHolder.tvLikes.setText(Html.fromHtml("<font color=\"#206199\"><b>"
				+ formattedLikesCount + " likes" + "</b></font>"));
		
		if (photo.caption != null) {
			viewHolder.tvCaption.setText(Html.fromHtml("<i>" + photo.caption
					+ "</i>"));
			viewHolder.tvCaption.setVisibility(0);
		}

		if (photo.comments != null) {
			viewHolder.tvComment1
					.setText(Html.fromHtml("<font color=\"#206199\"><b>"
							+ photo.comments.get(photo.comments.size() - 1).username
							+ "</b></font> "
							+ photo.comments.get(photo.comments.size() - 1).text));
			viewHolder.tvComment1.setVisibility(0);
			if (photo.comments.size() > 1) {
				viewHolder.tvComment2
						.setText(Html.fromHtml("<font color=\"#206199\"><b>"
								+ photo.comments.get(photo.comments.size() - 2).username
								+ "</b></font> "
								+ photo.comments.get(photo.comments.size() - 2).text));
				viewHolder.tvComment2.setVisibility(0);
			}
		}

		// Reset image from recycled view
		viewHolder.imgAvatar.setImageResource(0);
		viewHolder.imgAvatar.addShadow();
		viewHolder.imgPhoto.setImageResource(0);

		// Load photo into image view
		Picasso.with(getContext()).load(photo.profileImgUrl)
				.into(viewHolder.imgAvatar);
		// vHeight = vWidth * oHeight / oWidth
		viewHolder.imgPhoto.getLayoutParams().width = screenWidth;
		viewHolder.imgPhoto.getLayoutParams().height = screenWidth
				* photo.imageHeight / photo.imageWidth;
		Log.i("DEBUGG",
				"width = "
						+ String.valueOf(viewHolder.imgPhoto.getLayoutParams().width));
		Log.i("DEBUGG",
				"height = "
						+ String.valueOf(viewHolder.imgPhoto.getLayoutParams().height));
		Picasso.with(getContext()).load(photo.imageUrl)
				.into(viewHolder.imgPhoto);

		return convertView;
	}
}
