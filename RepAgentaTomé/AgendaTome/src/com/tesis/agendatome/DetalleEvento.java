package com.tesis.agendatome;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tesis.agendatome.app.AppController;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class DetalleEvento extends FragmentActivity implements LocationListener {
 
    GoogleMap mGoogleMap;
    ArrayList<LatLng> mMarkerPoints;
	
	String img, nom, ini, dir, des, est, fin;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	double lat =0;
	double lng = 0;
	
	@SuppressLint("SimpleDateFormat")
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalle_evento);
        
        if (!verificaConexion(this)) {
        	Toast.makeText(this,"Comprueba tu conexión a Internet. Saliendo ... ", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{
        	NetworkImageView imagen = (NetworkImageView) findViewById(R.id.imagenEvento);
	        TextView nombre = (TextView) findViewById(R.id.nombreEvento);
	        TextView fecha = (TextView) findViewById(R.id.fechaEvento);
	        TextView direccion = (TextView) findViewById(R.id.direccionEvento);
	        TextView descripcion = (TextView) findViewById(R.id.descripcionEvento);
	        
	        Intent i = getIntent();
	        Bundle bundle = i.getExtras();
	        
	        if(bundle!=null){
	        	//Capturar Atributos
	        	img = (String) bundle.get("eve_imagen");
	            nom = (String) bundle.get("eve_nombre");
	            ini = (String) bundle.get("eve_fecha_ini");
	            fin = (String) bundle.get("eve_fecha_ter");
	            dir = (String) bundle.get("eve_direccion");
	            //Integer adh = (Integer) bundle.get("eve_adhesion");
	            des = (String) bundle.get("eve_descripcion");
	            lat = (Double) bundle.get("eve_latitud");
	            lng = (Double) bundle.get("eve_longitud");
	            
	         // Fecha y Hora Evento
	            String s = ini;
	            int selectedYear = Integer.parseInt(s.substring(0, 4));
	            int selectedDay = Integer.parseInt(s.substring(8, 10));
	            int selectedMonth = Integer.parseInt(s.substring(5, 7));
	            int selectedHour = Integer.parseInt(s.substring(11, 13));
	            int selectedMin = Integer.parseInt(s.substring(14,16));

	            Calendar cal = Calendar.getInstance();
	            cal.setTimeZone(TimeZone.getTimeZone("GMT-03:00"));
	            cal.set(Calendar.YEAR, selectedYear);
	            cal.set(Calendar.DAY_OF_MONTH, selectedDay);
	            cal.set(Calendar.MONTH, selectedMonth -1);
	            cal.set(Calendar.HOUR_OF_DAY, selectedHour);
	            cal.set(Calendar.MINUTE, selectedMin);
	            
	            String fmt = new SimpleDateFormat("EEE dd MMM yyyy, HH:mm").format(cal.getTime());
	            
	            //Set Atributos
	            imagen.setImageUrl(img, imageLoader);
	            nombre.setText(nom);
	            fecha.setText(fmt + " hrs");            
	            direccion.setText(dir);
	            descripcion.setText(des);
	        }
	        
	        //Título actionBar
	        setTitle(" ");
	        
	        // Estado de Google Play Services
	        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
	 
	        if(status!=ConnectionResult.SUCCESS){ // Google Play Services no disponible
	 
	            int requestCode = 10;
	            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
	            dialog.show();
	 
	        }else { // Google Play Services está disponible
	 
	            // Inicializar
	            mMarkerPoints = new ArrayList<LatLng>();
	 
	            // Referenciar fragment
	            SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
	            
	            // Referenciar SupportFragment
	            mGoogleMap = fm.getMap();
	            
	            //Posición Destino
	            LatLng finishPoint = new LatLng(lat, lng);
	                    
	            // Dibujar Marcador Destino
	            drawMarker(finishPoint);
	            
	            // Abrir en nueva Activity Mapa
	            mGoogleMap.setOnMapClickListener(new OnMapClickListener() {
	            	public void onMapClick(LatLng point) {
	            		Intent intent = new Intent(DetalleEvento.this, Mapa.class);
	            		intent.putExtra("mar_latitud", lat);
	            		intent.putExtra("mar_longitud", lng);
	            		intent.putExtra("mar_nombre", nom);
	            		startActivity(intent);
	            	}
	            });
	            
	            // Abrir Calendario con datos del evento
	            Button agregarEventoCalendario = (Button) findViewById(R.id.addEventButton);
	            agregarEventoCalendario.setOnClickListener(new OnClickListener() {
	
	            	@Override
	            	public void onClick(View v) {
	            		Intent intent = new Intent(Intent.ACTION_EDIT);
	            		intent.setType("vnd.android.cursor.item/event");
			        
	            		Calendar cal = Calendar.getInstance();
			        
				        Date inicio = null;
				        Date finicio = null;
				        try {
							inicio = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(ini);
							finicio = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(fin);
							
							Calendar beginTime = Calendar.getInstance();
							cal.setTime(inicio);
		
							beginTime.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
							cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY),
							cal.get(Calendar.MINUTE));
							
							Calendar endTime = Calendar.getInstance();
							cal.setTime(finicio);
		
							endTime.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
							cal.get(Calendar.DATE), cal.get(Calendar.HOUR_OF_DAY),
							cal.get(Calendar.MINUTE));
							
					        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
					        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
					        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
					        intent.putExtra(Events.TITLE, nom);
					        intent.putExtra(Events.DESCRIPTION,  des);
					        intent.putExtra(Events.EVENT_LOCATION, dir);
					        intent.putExtra(Events.RRULE, "FREQ=YEARLY;UNTIL="+cal.get(Calendar.YEAR)+1231);
			
					        startActivity(intent);
				        } catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            	}
	            });
	        }
        }
	}

	private void drawMarker(LatLng point) {
		mMarkerPoints.add(point);
		 
        // Inicializar
        MarkerOptions options = new MarkerOptions();
 
        // Configurar la posición del marcador y otros
        options.position(point);
 
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
 
        // Agregar Marcador al mapa
        mGoogleMap.addMarker(options);
	}

	private boolean verificaConexion(Context ctx) {
		boolean bConectado = false;
        ConnectivityManager connec = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        // No sólo wifi, también GPRS
        NetworkInfo[] redes = connec.getAllNetworkInfo();
        // este bucle debería no ser tan ñapa
        for (int i = 0; i < 2; i++) {
            // ¿Tenemos conexión? ponemos a true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                bConectado = true;
            }
        }
        return bConectado;
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
