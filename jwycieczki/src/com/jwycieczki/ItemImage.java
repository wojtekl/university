package com.jwycieczki;

import android.graphics.Bitmap;


public class ItemImage {
	private Bitmap image;
	private String title;

	public ItemImage(Bitmap image) {
		super();
		this.image = image;
		//this.title = title;
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
