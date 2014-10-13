package com.hofc.hofc.vo;

import java.util.Date;

import android.graphics.Bitmap;

public class ActuVO {
	/**
	 * Attributes
	 */
	int postId;
	String titre;
	String texte;
	String url;
	String imageUrl;
	Date date;
	Bitmap bitmapImage;

	/**
	 * Methods
	 */
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getTexte() {
		return texte;
	}
	public void setTexte(String texte) {
		this.texte = texte;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public Bitmap getBitmapImage() {
		return bitmapImage;
	}
	public void setBitmapImage(Bitmap bmp) {
		this.bitmapImage = bmp;
	}
	
}
