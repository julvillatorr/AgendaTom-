package com.tesis.agendatome;

import java.util.ArrayList;

import com.tesis.agendatome.adapter.CustomGridViewAdapterCategoria;
import com.tesis.agendatome.model.Categoria;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

public class MenuCategorias extends Fragment {
	 GridView gridView;
	 ArrayList<Categoria> gridArray = new ArrayList<Categoria>();
	 CustomGridViewAdapterCategoria customGridAdapter;

	 @Override
	 public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
		  View rootView2 = inflater.inflate(R.layout.menu_categorias, container, false);
		  
		  
		  Bitmap hoteleria = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_hoteleria);
		  Bitmap gastronomia = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_gastronomia);
		  Bitmap playas = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_playa);
		  Bitmap pubs = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_bar);
		  Bitmap comercio = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_comercio);
		  Bitmap otros = BitmapFactory.decodeResource(this.getResources(), R.drawable.ico_otros);
		  
		  gridArray.add(new Categoria(hoteleria));
		  gridArray.add(new Categoria(gastronomia));
		  gridArray.add(new Categoria(playas));
		  gridArray.add(new Categoria(pubs));
		  gridArray.add(new Categoria(comercio));
		  gridArray.add(new Categoria(otros));
	
		  gridView = (GridView) rootView2.findViewById(R.id.gridViewCategorias);
		  customGridAdapter = new CustomGridViewAdapterCategoria(this.getActivity(), R.layout.fila_categoria, gridArray);
		  gridView.setAdapter(customGridAdapter);
		  
		  gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					
					Intent i = new Intent(getActivity(), ListaMarcadores.class);
					
					//HOTELERIA
					if(position == 0){
						i.putExtra("categoria", "hoteleria");
						startActivity(i);
					}
					//GASTRONOMIA
					if(position == 1){
						i.putExtra("categoria", "gastronomia");
						startActivity(i);
					}
					//PLAYAS Y CALETAS
					if(position == 2){
						i.putExtra("categoria", "playas");
						startActivity(i);
					}
					//PUBS Y BARES
					if(position == 3){
						i.putExtra("categoria", "pub_bar");
						startActivity(i);
					}
					//COMERCIO
					if(position == 4){
						i.putExtra("categoria", "comercio");
						startActivity(i);
					}
					//OTROS SERVICIOS
					if(position == 5){
						i.putExtra("categoria", "otros");
						startActivity(i);
					}
				}
			});
		  
		  return rootView2;
	 }

}