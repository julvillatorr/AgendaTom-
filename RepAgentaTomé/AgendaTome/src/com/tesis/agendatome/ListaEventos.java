package com.tesis.agendatome;
 
import com.tesis.agendatome.adapter.CustomListViewAdapterEvento;
import com.tesis.agendatome.app.AppController;
import com.tesis.agendatome.model.Evento;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
 
public class ListaEventos extends Fragment {
    private static final String TAG = ListaEventos.class.getSimpleName();
    private ListView listView;
    private CustomListViewAdapterEvento adapter;
    private ProgressDialog pDialog;
    private List<Evento> listaEvento = new ArrayList<Evento>();
    private static final String url = "http://146.83.198.59/~agendatome/tesis/rsc/eventos.php";
    int count = 0;
    public String eveNombre, eveInicio, eveTermino, eveDescripcion, eveDireccion, eveCategoria, eveImagen;
	Integer eveAdhesion;
    Double eveLatitud, eveLongitud;
    
    @SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.lista_eventos, container, false);
        
        listView = (ListView) rootView.findViewById(R.id.listaEvento);
        adapter = new CustomListViewAdapterEvento(this.getActivity(), listaEvento);
        listView.setAdapter(adapter);      
        
        pDialog = new ProgressDialog(this.getActivity());
        pDialog.setMessage("Cargando...");
        pDialog.show();
        pDialog.setCancelable(false);
        
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Entry entry = cache.get(url);
        JsonArrayRequest eventoReq = null;
        
        // Verificar conexión a wifi o datos
        if (verificaConexion(this.getActivity())) {
	        eventoReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
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
            	Toast.makeText(getActivity(),
                        "Comprueba tu conexión a Internet. Saliendo ... ", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
        
        
        //SwipeRefresh ListView
        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh);
        swipeView.setEnabled(false); 
        swipeView.setColorScheme(R.color.base);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);
                        
                        JsonArrayRequest eventoReq = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            		        public void onResponse(JSONArray response) {
            		        	
            		        	Log.d(TAG, response.toString());
            		            VolleyLog.d(TAG, "Response: " + response.toString());
            					  if (response != null) {
            						  for (int i = 0; i < response.length(); i++) {
										listaEvento.removeAll(listaEvento);
									}
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
                    }
                }, 3000);
            }
        });
     
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
     
            }
     
            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            	if (firstVisibleItem == 0)
                    swipeView.setEnabled(true);
                else
                    swipeView.setEnabled(false);
            }
        });
	        
        // Abrir nueva activity con detalle al hacer click
        listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				Evento eve = listaEvento.get(position);
				eveNombre = eve.getEve_nombre();
				eveInicio = eve.getEve_fecha_ini();
				eveTermino = eve.getEve_fecha_ter();
				eveDireccion = eve.getEve_direccion();
				eveDescripcion = eve.getEve_descripcion();
				eveCategoria = eve.getEve_categoria();
				eveAdhesion = eve.getEve_adhesion();
				eveImagen = eve.getEve_imagen();
				eveLatitud = eve.getEve_latitud();
				eveLongitud = eve.getEve_longitud();
								
				System.out.println(eveImagen);
				
				Intent i = new Intent(getActivity(), DetalleEvento.class);
				
				i.putExtra("eve_nombre", eveNombre);
				i.putExtra("eve_fecha_ini", eveInicio);
				i.putExtra("eve_fecha_ter", eveTermino);
				i.putExtra("eve_direccion", eveDireccion);
				i.putExtra("eve_descripcion", eveDescripcion);
				i.putExtra("eve_categoria", eveCategoria);
				i.putExtra("eve_adhesion", eveAdhesion);
				i.putExtra("eve_imagen", eveImagen);
				i.putExtra("eve_latitud", eveLatitud);
				i.putExtra("eve_longitud", eveLongitud);
				
			    startActivity(i);
			}
		});
        return rootView;
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
 
    // Parsear datos del archivo json
    public void parseJsonEvento(JSONArray response) {
        try {
        	for (int i = 0; i < response.length(); i++) {
        		JSONObject obj = (JSONObject) response.get(i);
                Evento evento = new Evento();
                
                evento.setEve_imagen(obj.getString("eve_imagen"));
	            evento.setEve_nombre(obj.getString("eve_nombre"));
	            evento.setEve_fecha_ini(obj.getString("eve_fecha_ini"));
	            evento.setEve_fecha_ter(obj.getString("eve_fecha_ter"));
	            evento.setEve_categoria(obj.getString("eve_categoria"));
	            evento.setEve_adhesion(obj.getInt("eve_adhesion"));
	            evento.setEve_descripcion(obj.getString("eve_descripcion"));
	            evento.setEve_direccion(obj.getString("eve_direccion"));
	            evento.setEve_latitud(obj.getDouble("eve_latitud"));
	            evento.setEve_longitud(obj.getDouble("eve_longitud"));
	                
                listaEvento.add(evento);
            }
        	if(listaEvento.isEmpty()){
                Toast.makeText(getActivity(), "No hay Eventos que mostrar.", Toast.LENGTH_SHORT).show();
        	}
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}