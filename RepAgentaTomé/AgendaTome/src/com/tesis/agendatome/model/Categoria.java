package com.tesis.agendatome.model;


import android.graphics.Bitmap;

public class Categoria {
	Bitmap image;
	
	public Categoria(Bitmap image) {
		super();
		this.image = image;
	}
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image) {
		this.image = image;
	}
}
