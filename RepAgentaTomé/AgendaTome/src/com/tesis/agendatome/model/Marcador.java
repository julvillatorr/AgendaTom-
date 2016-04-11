package com.tesis.agendatome.model;

public class Marcador {
	String mar_nombre, mar_descripcion, mar_direccion, mar_telefono, mar_email, mar_sitio_web;
	Double mar_latitud, mar_longitud;
	int mar_distancia;
	public Marcador(){
		
	}
	
	public Marcador(String mar_nombre, String mar_descripcion, String mar_direccion, Double mar_latitud, Double mar_longitud, String mar_telefono, String mar_email, String mar_sitio_web, int mar_distancia){
		this.mar_nombre = mar_nombre;
		this.mar_descripcion = mar_descripcion;
		this.mar_direccion = mar_direccion;
		this.mar_latitud = mar_latitud;
		this.mar_longitud = mar_longitud;
		this.mar_telefono = mar_telefono;
		this.mar_email = mar_email;
		this.mar_sitio_web = mar_sitio_web;
		this.mar_distancia = mar_distancia;
	}

	public int getMar_distancia() {
		return mar_distancia;
	}

	public void setMar_distancia(int mar_distancia) {
		this.mar_distancia = mar_distancia;
	}

	public String getMar_nombre() {
		return mar_nombre;
	}

	public void setMar_nombre(String mar_nombre) {
		this.mar_nombre = mar_nombre;
	}

	public String getMar_descripcion() {
		return mar_descripcion;
	}

	public void setMar_descripcion(String mar_descripcion) {
		this.mar_descripcion = mar_descripcion;
	}

	public String getMar_direccion() {
		return mar_direccion;
	}

	public void setMar_direccion(String mar_direccion) {
		this.mar_direccion = mar_direccion;
	}

	public String getMar_telefono() {
		return mar_telefono;
	}

	public void setMar_telefono(String mar_telefono) {
		this.mar_telefono = mar_telefono;
	}

	public String getMar_email() {
		return mar_email;
	}

	public void setMar_email(String mar_email) {
		this.mar_email = mar_email;
	}

	public String getMar_sitio_web() {
		return mar_sitio_web;
	}

	public void setMar_sitio_web(String mar_sitio_web) {
		this.mar_sitio_web = mar_sitio_web;
	}

	public Double getMar_latitud() {
		return mar_latitud;
	}

	public void setMar_latitud(Double mar_latitud) {
		this.mar_latitud = mar_latitud;
	}

	public Double getMar_longitud() {
		return mar_longitud;
	}

	public void setMar_longitud(Double mar_longitud) {
		this.mar_longitud = mar_longitud;
	}

}
