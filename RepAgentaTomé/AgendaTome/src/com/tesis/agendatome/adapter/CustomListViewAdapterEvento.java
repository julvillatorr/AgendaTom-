package com.tesis.agendatome.adapter;

import com.tesis.agendatome.R;
import com.tesis.agendatome.model.Evento;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
@SuppressLint("InflateParams")
public class CustomListViewAdapterEvento extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Evento> itemEvento;
    String categoria = "";
    public CustomListViewAdapterEvento(Activity activity, List<Evento> itemEvento) {
        this.activity = activity;
        this.itemEvento = itemEvento;
    }
 
    @Override
    public int getCount() {
        return itemEvento.size();
    }
 
    @Override
    public Object getItem(int location) {
        return itemEvento.get(location);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
 
    @SuppressLint({ "InflateParams", "SimpleDateFormat" })
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
 
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.fila_evento, null);
        
        ImageView imageView = (ImageView) convertView.findViewById(R.id.iconoEvento);
        TextView nombre = (TextView) convertView.findViewById(R.id.nombreEvento);
        TextView fecha = (TextView) convertView.findViewById(R.id.fechaEvento);
        TextView direccion = (TextView) convertView.findViewById(R.id.direccionEvento);
        
        Evento eve = itemEvento.get(position);
 
        // Icono Evento
        categoria = eve.getEve_categoria();
        if (categoria.equalsIgnoreCase("arte_cultura")) {
        	imageView.setImageResource(R.drawable.ico_arte);
		}
        else if (categoria.equalsIgnoreCase("ciencia_tecnologia")) {
        	imageView.setImageResource(R.drawable.ico_ciencia);
		}
        else if (categoria.equalsIgnoreCase("deporte")) {
        	imageView.setImageResource(R.drawable.ico_deporte);
		}
        else if (categoria.equalsIgnoreCase("fiesta_costumbrista")) {
        	imageView.setImageResource(R.drawable.ico_costumbrista);
		}
        else if (categoria.equalsIgnoreCase("actividad_musical")) {
        	imageView.setImageResource(R.drawable.ico_musical);
		}
        
        // Nombre Evento
        nombre.setText(eve.getEve_nombre());
         
        // Fecha y Hora Evento
        String s = eve.getEve_fecha_ini();
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
        fecha.setText(fmt + " hrs");
         
        // Direccion y Estado Evento
        direccion.setText(eve.getEve_direccion());
 
        return convertView;
    }
 
}