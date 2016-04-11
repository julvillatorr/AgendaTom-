package com.tesis.agendatome.model;

public class Evento {
	private String eve_nombre, eve_direccion, eve_fecha_ini, eve_fecha_ter, eve_descripcion, eve_categoria, eve_imagen;
	private int eve_adhesion;
	private double eve_latitud, eve_longitud;
	public Evento(){

	}
	
	public Evento(String eve_nombre, String eve_direccion, String eve_fecha_ini, String eve_fecha_ter, String eve_descripcion, String eve_categoria, String eve_imagen, int eve_adhesion, double mar_latitud, double mar_longitud){
		this.eve_nombre = eve_nombre;
		this.eve_direccion = eve_direccion;
		this.eve_fecha_ini = eve_fecha_ini;
		this.eve_fecha_ter = eve_fecha_ter;
		this.eve_descripcion = eve_descripcion;
		this.eve_categoria = eve_categoria;
		this.eve_imagen = eve_imagen;
		this.eve_adhesion = eve_adhesion;
		this.eve_latitud = mar_latitud;
		this.eve_longitud = mar_longitud;
	}

	public String getEve_fecha_ter() {
		return eve_fecha_ter;
	}

	public void setEve_fecha_ter(String eve_fecha_ter) {
		this.eve_fecha_ter = eve_fecha_ter;
	}

	public double getEve_latitud() {
		return eve_latitud;
	}

	public void setEve_latitud(double eve_latitud) {
		this.eve_latitud = eve_latitud;
	}

	public double getEve_longitud() {
		return eve_longitud;
	}

	public void setEve_longitud(double eve_longitud) {
		this.eve_longitud = eve_longitud;
	}

	public String getEve_fecha_ini() {
		return eve_fecha_ini;
	}

	public void setEve_fecha_ini(String eve_fecha_ini) {
		this.eve_fecha_ini = eve_fecha_ini;
	}

	public String getEve_nombre() {
		return eve_nombre;
	}

	public void setEve_nombre(String eve_nombre) {
		this.eve_nombre = eve_nombre;
	}

	public String getEve_direccion() {
		return eve_direccion;
	}

	public void setEve_direccion(String eve_direccion) {
		this.eve_direccion = eve_direccion;
	}

	public String getEve_descripcion() {
		return eve_descripcion;
	}

	public void setEve_descripcion(String eve_descripcion) {
		this.eve_descripcion = eve_descripcion;
	}

	public String getEve_categoria() {
		return eve_categoria;
	}

	public void setEve_categoria(String eve_categoria) {
		this.eve_categoria = eve_categoria;
	}

	public String getEve_imagen() {
		return eve_imagen;
	}

	public void setEve_imagen(String eve_imagen) {
		this.eve_imagen = eve_imagen;
	}

	public int getEve_adhesion() {
		return eve_adhesion;
	}

	public void setEve_adhesion(int eve_adhesion) {
		this.eve_adhesion = eve_adhesion;
	}	
}