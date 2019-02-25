package com.example.quotescardmaker.image;

import android.graphics.Bitmap;

/**
 * class forbject for every item image
 *
 */
public class ImageItem {
	private Bitmap image;
	private String title;

	public ImageItem(Bitmap image, String title) {
		super();
		setImage(image);
		setTitle(title);
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
