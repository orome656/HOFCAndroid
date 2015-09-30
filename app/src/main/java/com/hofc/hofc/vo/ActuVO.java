package com.hofc.hofc.vo;

import android.graphics.Bitmap;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class ActuVO {
	/**
	 * Attributes
	 */

	private int postId;
	private String titre;
	private String texte;
	private String url;
	private String imageUrl;
	private Date date;
	private Bitmap bitmapImage;

	/**
	 * Methods
	 */
	public int getPostId() {
		return postId;
	}
	@JsonProperty("postid")
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
	@JsonProperty("image")
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
	@JsonIgnore
	public void setBitmapImage(Bitmap bmp) {
		this.bitmapImage = bmp;
	}
	
}
