package com.tesis.agendatome.adapter;

import com.tesis.agendatome.R;
import com.tesis.agendatome.model.Marcador;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

 
@SuppressLint("InflateParams")
public class CustomListViewAdapterMarcador extends BaseAdapter implements Filterable{
    private Activity activity;
    private LayoutInflater inflater;
    private List<Marcador> itemMarcador;
    List<Marcador> filtro;
    ValueFilter valueFilter;
 
    public CustomListViewAdapterMarcador(Activity activity, List<Marcador> itemMarcador) {
        this.activity = activity;
        this.itemMarcador = itemMarcador;
        filtro = itemMarcador;
    }
 
    @Override
    public int getCount() {
        return itemMarcador.size();
    }
 
    @Override
    public Object getItem(int location) {
        return itemMarcador.get(location);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @SuppressLint("InflateParams")
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.fila_marcador, null);
        
        TextView nombre = (TextView) convertView.findViewById(R.id.nombreMarcador);
        TextView direccion = (TextView) convertView.findViewById(R.id.direccionMarcador);
        TextView distancia = (TextView) convertView.findViewById(R.id.distanciaMarcador);
        
 
        Marcador m = itemMarcador.get(position);

        nombre.setText(m.getMar_nombre());
        direccion.setText(m.getMar_direccion());
        if (m.getMar_distancia() < 6000) {
			distancia.setText(m.getMar_distancia()+" Km.");
		}
        else {
			distancia.setVisibility(View.GONE);
		}
        return convertView;
    }

	@Override
	public Filter getFilter() {
		if (valueFilter == null) {
			valueFilter = new ValueFilter();
		}
		return valueFilter;
	}
    
	private class ValueFilter extends Filter{

		@SuppressLint("DefaultLocale")
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			
			if (constraint != null && constraint.length() > 0) {
				List<Marcador> filterList = new ArrayList<Marcador>();
				for (int i = 0; i < filtro.size(); i++) {
					if ((filtro.get(i).getMar_nombre().toUpperCase()).contains(constraint.toString().toUpperCase())) {
						Marcador marcador = new Marcador(
								filtro.get(i).getMar_nombre(),null,
								null, null, null, null, null, null, 0);
						filterList.add(marcador);
					}
				}
				results.count = filterList.size();
				results.values = filterList;
			}else{
				results.count = filtro.size();
				results.values = filtro;
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			itemMarcador = (ArrayList<Marcador>) results.values;
			notifyDataSetChanged();
		}
		
	}
    
 
}