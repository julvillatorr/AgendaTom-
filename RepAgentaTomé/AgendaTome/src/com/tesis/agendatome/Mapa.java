package com.tesis.agendatome;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tesis.agendatome.util.JSONDirectionsParser;
 
public class Mapa extends FragmentActivity implements LocationListener {
    GoogleMap mGoogleMap;
    ArrayList<LatLng> mMarkerPoints;
    String nombre = "";
    String provider;
    double mLatitude=0;
    double mLongitude=0;
    double latDestino = 0;
    double lngDestino = 0;
    TextView distancia;
    TextView duracion;
    
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa_evento);
        
        // Título ActionBar
        setTitle(" ");
        
        TextView nombreMarcador = (TextView) findViewById(R.id.nombreMarcador);
        distancia = (TextView) findViewById(R.id.distanciaMarcador);
        duracion = (TextView)findViewById(R.id.tiempoMarcador);
        
        if(distancia == null){
        	Toast.makeText(this,"Tu ubicación no está disponible. ", Toast.LENGTH_SHORT).show();
        }
        
        // Verificar conexión a wifi o datos
        if (!verificaConexion(this)) {
        	Toast.makeText(this,"Comprueba tu conexión a Internet. Saliendo ... ", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{
	        Intent i = getIntent();
	        Bundle bundle = i.getExtras();
	        
	        if(bundle!=null){
	        	latDestino = (Double) bundle.get("mar_latitud");
	        	lngDestino = (Double) bundle.get("mar_longitud");
	        	nombre = (String) bundle.get("mar_nombre");
	        	
	        	nombreMarcador.setText(nombre);
	        }
	 
	        // Estado de Google Play Services
	        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
	 
	        if(status!=ConnectionResult.SUCCESS){ // Google Play Services no está disponible
	 
	            int requestCode = 10;
	            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
	            dialog.show();
	 
	        }else {// Google Play Services está disponible
	 
	            // Inicializar
	            mMarkerPoints = new ArrayList<LatLng>();
	 
	            // Referenciar SupportMaoFragment al activity_main
	            SupportMapFragment fm = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
	 
	            mGoogleMap = fm.getMap();
	 
	            // Disponible Botón Mi posición
	            mGoogleMap.setMyLocationEnabled(true);
	            mGoogleMap.setBuildingsEnabled(true);
	 
	            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	            Criteria criteria = new Criteria();
	            provider = locationManager.getBestProvider(criteria, true);
	            

	            if(provider != null){
	            	Location location = locationManager.getLastKnownLocation(provider);
	            	if(location != null){
	            		onLocationChanged(location);
	            	}
	                locationManager.requestLocationUpdates(provider, 20000, 0, this);
	            }
	 
	            mMarkerPoints.clear();
	            mGoogleMap.clear();
	            //Posición Inicial
	            LatLng startPoint = new LatLng(mLatitude, mLongitude);
	            //Posición Destino
	            LatLng finishPoint = new LatLng(latDestino, lngDestino);
	            // Dibujar Ruta
	            drawMarker(finishPoint);
	 
	            // Solicitar Ruta
	            String url = getDirectionsUrl(startPoint, finishPoint);
	            DownloadTask downloadTask = new DownloadTask();
	 
	            // Comienza descarga de la ruta en archivo json de google maps
	            downloadTask.execute(url);                   
	        }
	    }
    }
 
    private String getDirectionsUrl(LatLng origin,LatLng dest){
 
        // Origen
        String str_origin = "origin="+origin.latitude+","+origin.longitude;
 
        // Destino
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
 
        // Sensor disponible
        String sensor = "sensor=false";
 
        // Construir Parámetros
        String parameters = str_origin+"&"+str_dest+"&"+sensor;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
 
        return url;
    }
 
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
 
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();
            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
 
            data = sb.toString();
 
            br.close();
 
        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
 
    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String>{
 
        @Override
        protected String doInBackground(String... url) {
 
            String data = "";
 
            try{
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }
 
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
 
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }
 
    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{
 
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
 
            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;
 
            try{
                jObject = new JSONObject(jsonData[0]);
                JSONDirectionsParser parser = new JSONDirectionsParser();
 
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }
 
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = new PolylineOptions();
            String distance = "";
            String duration = "";
            
            
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                List<HashMap<String, String>> path = result.get(i);
 
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
                    
                    if(j==0){   
                        distance = (String)point.get("distance");                       
                        continue;
                    }else if(j==1){ 
                        duration = (String)point.get("duration");
                        continue;
                    }
 
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
 
                    points.add(position);
                }
 
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.CYAN);
                lineOptions.geodesic(true);
            }
            
            distancia.setText("Distancia: " +distance);
            duracion.setText("Tiempo Estimado: "+duration);
            
            mGoogleMap.addPolyline(lineOptions);
        }
    }
 
    private void drawMarker(LatLng point){
        mMarkerPoints.add(point);
 
        MarkerOptions options = new MarkerOptions();
 
        options.position(point);
        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
        options.title(nombre);	
        
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        
        CameraPosition cameraPosition = new CameraPosition.Builder()
	        .target(point)     
	        .zoom(14)              
	        .tilt(50)   
	        .build();                   
        mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mGoogleMap.addMarker(options);
    }
 
    @Override
    public void onLocationChanged(Location location) {
    	if (provider != null) {
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
		}
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
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}