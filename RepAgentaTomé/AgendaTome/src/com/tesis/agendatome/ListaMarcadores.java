package com.tesis.agendatome;
 
import com.tesis.agendatome.adapter.CustomListViewAdapterMarcador;
import com.tesis.agendatome.app.AppController;
import com.tesis.agendatome.model.Marcador;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.model.LatLng;
 
public class ListaMarcadores extends Activity implements LocationListener {
    private static final String TAG = ListaEventos.class.getSimpleName();
    private ListView listView;
    private CustomListViewAdapterMarcador adapter;
    private ProgressDialog pDialog;
    private List<Marcador> listaMarcador = new ArrayList<Marcador>();
    public String nombreCategoria, marNombre, marDireccion, eveTermino, marEmail, marDescripcion, marTelefono, marSitioWeb, eveImagen;
	Integer eveAdhesion, distancia, marDistancia;
    Double marLatitud, marLongitud;
    double mLatitude = 0;
    double mLongitude = 0;
	double dLatiud = 0;
	double dLongitud = 0;
	EditText inputSearch;
	String provider;
    
    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_categoria);
        
        Intent i = getIntent();
	    Bundle bundle = i.getExtras();
	        
	    if(bundle!=null){
	    	nombreCategoria = (String) bundle.get("categoria");
	    }
	    
	    // Título ActionBar
	    if (nombreCategoria.equalsIgnoreCase("hoteleria")) {
	    	setTitle("Hotelería");
		}else if (nombreCategoria.equalsIgnoreCase("gastronomia")) {
			setTitle("Gastronomía");
		}else if (nombreCategoria.equalsIgnoreCase("playas")) {
			setTitle("Playas y Caletas");
		}else if (nombreCategoria.equalsIgnoreCase("pub_bar")) {
			setTitle("Pubs y Bares");
		}else if (nombreCategoria.equalsIgnoreCase("comercio")) {
			setTitle("Comercio");
		}else if (nombreCategoria.equalsIgnoreCase("otros")) {
			setTitle("Otros Servicios");
		}
	    
        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListViewAdapterMarcador(this, listaMarcador);
        listView.setAdapter(adapter);
        
        listView.setTextFilterEnabled(true);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        
        
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
        
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Cargando...");
        pDialog.show();
        pDialog.setCancelable(false);
        
        // Url ubicación archivo json
        String url = "http://146.83.198.59/~agendatome/tesis/rsc/"+nombreCategoria+".php";
        
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Entry entry = cache.get(url);
        
        // Verificar conexión wifi o datos
        if (verificaConexion(this)) {
	        JsonArrayRequest eventoReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
		        public void onResponse(JSONArray response) {
		        	
		        	Log.d(TAG, response.toString());
		            hidePDialog();
		            VolleyLog.d(TAG, "Response: " + response.toString());
					  if (response != null) {
					        parseJsonEvento(response);
					  }
		        }
	        }, new Response.ErrorListener() {
	        	@Override
		        public void onErrorResponse(VolleyError error) {
		            VolleyLog.d(TAG, "Error: " + error.getMessage());
			    }
		    });
	 
	        AppController.getInstance().addToRequestQueue(eventoReq);
        } else{
        	if (entry != null) {
                try {
                    String data = new String(entry.data, "UTF-8");
                    try {
                        parseJsonEvento(new JSONArray(data));
    		            hidePDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else{
            	Toast.makeText(this,
                        "Comprueba tu conexión a Internet. Saliendo ... ", Toast.LENGTH_SHORT).show();
                this.finish();
            }
        }
        
        // Abrir detalle al clickear
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				Marcador mar = listaMarcador.get(position);
				marNombre = mar.getMar_nombre();
				marDireccion = mar.getMar_direccion();
				marDescripcion = mar.getMar_descripcion();
				marEmail = mar.getMar_email();
				marTelefono = mar.getMar_telefono();
				marSitioWeb = mar.getMar_sitio_web();
				marLatitud = mar.getMar_latitud();
				marLongitud = mar.getMar_longitud();
				
				Intent i = new Intent(ListaMarcadores.this, DetalleMarcador.class);
				
				i.putExtra("mar_nombre", marNombre);
				i.putExtra("mar_direccion", marDireccion);
				i.putExtra("mar_descripcion", marDescripcion);
				i.putExtra("mar_email", marEmail);
				i.putExtra("mar_telefono", marTelefono);
				i.putExtra("mar_sitio_web", marSitioWeb);
				i.putExtra("mar_latitud", marLatitud);
				i.putExtra("mar_longitud", marLongitud);
				
			    startActivity(i);
			}
		});
        
        // Búsqueda
        inputSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				ListaMarcadores.this.adapter.getFilter().filter(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
    }

	public static boolean verificaConexion(Context ctx) {
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
 
    // Parsear datos del json
    public void parseJsonEvento(JSONArray response) {
        try {
 
            for (int i = 0; i < response.length(); i++) {
                JSONObject obj = (JSONObject) response.get(i);
                
                Marcador marcador = new Marcador();
                marcador.setMar_nombre(obj.getString("mar_nombre"));
                marcador.setMar_direccion(obj.getString("mar_direccion"));
                marcador.setMar_descripcion(obj.getString("mar_descripcion"));
                marcador.setMar_email(obj.getString("mar_email"));
                marcador.setMar_telefono(obj.getString("mar_telefono"));
                marcador.setMar_sitio_web(obj.getString("mar_sitio_web"));
                marcador.setMar_latitud(obj.getDouble("mar_latitud"));
                marcador.setMar_longitud(obj.getDouble("mar_longitud"));
                //Calcula Distancia
                distancia = (int) CalculationByDistance(new LatLng(mLatitude, mLongitude), new LatLng(marcador.getMar_latitud(), marcador.getMar_longitud()));
                marcador.setMar_distancia(distancia);
                
                listaMarcador.add(marcador);
            }
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    // Calulcar distancia entre posición actual y destino
    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
		int kmInDec = Integer.valueOf(newFormat.format(km));
		return kmInDec;
    }
    
    @SuppressWarnings("unused")
	public void onLocationChanged(Location location) {
    	if (provider != null) {
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
		}
    	LatLng point = new LatLng(mLatitude, mLongitude);
        
    }
    
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
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