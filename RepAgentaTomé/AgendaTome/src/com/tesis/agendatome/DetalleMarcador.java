package com.tesis.agendatome;

import java.util.ArrayList;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DetalleMarcador extends FragmentActivity implements LocationListener {
	String nombre, direccion, descripcion, telefono, email, sitio, categoria;
	double latitud = 0;
	double longitud = 0;
	GoogleMap mGoogleMap;
    ArrayList<LatLng> mMarkerPoints;
    double mLatitude=0;
    double mLongitude=0;
    double latDestino = 0;
    double lngDestino = 0;
	
	protected void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_mapa);
        
        //Título ActionBar
        setTitle(" ");
        
        TextView nom = (TextView) findViewById(R.id.nombre);
        TextView dir = (TextView) findViewById(R.id.direccion);
        TextView des = (TextView) findViewById(R.id.descripcion);
        
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if(bundle!=null){
        	nombre = (String) bundle.getString("mar_nombre");
        	direccion = (String) bundle.getString("mar_direccion");
        	descripcion = (String) bundle.getString("mar_descripcion");
        	telefono = (String) bundle.getString("mar_telefono");
        	email = (String) bundle.getString("mar_email");
        	sitio = (String) bundle.getString("mar_sitio_web");
        	latDestino = (Double) bundle.get("mar_latitud");
        	lngDestino = (Double) bundle.get("mar_longitud");
        	
        	nom.setText(nombre);
        	dir.setText(direccion);
        	des.setText(descripcion);
        }
        
        // Botón que abre Call Dialer, para realizar llamada si es que está disponible.
        Button buttonCall = (Button) findViewById(R.id.botonCall);
        buttonCall.setText(telefono);
        if (!TextUtils.isEmpty(telefono)) {
			buttonCall.setVisibility(View.VISIBLE);
			buttonCall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						Intent callIntent = new Intent(Intent.ACTION_DIAL);
						callIntent.setData(Uri.parse("tel:"+telefono));
						startActivity(callIntent);
					} catch (ActivityNotFoundException activityException) {
						Log.e("Llamando...", "Error en Llamada", activityException);
					}
				}
			});
        } else{
        	buttonCall.setVisibility(View.GONE);
        }
        
        // Botón que abre Navegador, para visitar sitio web si es que está disponible.
        Button buttonWeb = (Button) findViewById(R.id.sitioWeb);
        buttonWeb.setText(sitio);
        if (!TextUtils.isEmpty(sitio)) {
			buttonWeb.setVisibility(View.VISIBLE);
	        buttonWeb.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sitio));
	                startActivity(webIntent);
	            }
	        });
		} else{
			buttonWeb.setVisibility(View.GONE);
		}
        
        // Botón para abrir el gestor de Email por defecto, para enviar email si es que está disponible.
        Button buttonEmail = (Button) findViewById(R.id.email);
        buttonEmail.setText(email);
        if (!TextUtils.isEmpty(email)) {
			buttonEmail.setVisibility(View.VISIBLE);
			buttonEmail.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent emailIntent = new Intent(Intent.ACTION_SEND);
					emailIntent.setType("message/rfc822");
					emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
					emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
					emailIntent.putExtra(Intent.EXTRA_TEXT   , "");
					try {
					    startActivity(Intent.createChooser(emailIntent, "Enviar Email"));
					} catch (android.content.ActivityNotFoundException ex) {
					    Toast.makeText(DetalleMarcador.this, "No existe un cliente de Email instalado.", Toast.LENGTH_SHORT).show();
					}
				}
			});
        }else{
        	buttonEmail.setVisibility(View.GONE);
        }
        
        // Ver disponibilidad de Google Play Services
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
 
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services no está disponible
 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
 
        }else { // Google Play Services disponible
 
            // Inicializar
            mMarkerPoints = new ArrayList<LatLng>();
 
            // Referenciar SupportMapFragment al xml acivity_main
            SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
            
            // Referenciar SupportFragment
            mGoogleMap = fm.getMap();
            
            // Posición Destino
            LatLng finishPoint = new LatLng(latDestino, lngDestino);
                    
            // Dibujar Macador destino
            drawMarker(finishPoint);
            
            // Abrir Mapa en nueva actividad
	        mGoogleMap.setOnMapClickListener(new OnMapClickListener() {
	            public void onMapClick(LatLng point) {
	               Intent intent = new Intent(DetalleMarcador.this, Mapa.class);
	               intent.putExtra("mar_latitud", latDestino);
	               intent.putExtra("mar_longitud", lngDestino);
	               intent.putExtra("mar_nombre", nombre);
	               startActivity(intent);
	            }
	        });
        }
        
	}
	
	// Dibujar Marcador
	private void drawMarker(LatLng point) {
		mMarkerPoints.add(point);
		 
        // Inicializar
        MarkerOptions options = new MarkerOptions();
 
        // Dar posición al marcador
        options.position(point);
 
        // Otras configuraciones
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
 
        // Agregar Marcador al mapa
        mGoogleMap.addMarker(options);
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
}