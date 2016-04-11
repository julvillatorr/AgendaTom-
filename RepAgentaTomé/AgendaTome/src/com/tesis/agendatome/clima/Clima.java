package com.tesis.agendatome.clima;

import java.util.Calendar;

import org.json.JSONException;

import com.tesis.agendatome.R;
import com.tesis.agendatome.model.Weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Clima extends Activity{
	
	private TextView temp;
	private TextView press;
	private TextView windSpeed;
	private TextView date;
	private TextView detail;
	
	private TextView hum;
	private ImageView imgView;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clima);
        
        String city = "tome,cl";
		
		temp = (TextView) findViewById(R.id.temp);
		hum = (TextView) findViewById(R.id.hum);
		press = (TextView) findViewById(R.id.press);
		windSpeed = (TextView) findViewById(R.id.windSpeed);
		imgView = (ImageView) findViewById(R.id.condIcon);
		date = (TextView) findViewById(R.id.date);
		detail = (TextView) findViewById(R.id.detail);
		
		detail.setText("Ver detalle de la semana");
        detail.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://openweathermap.org/city/3869657"));
                startActivity(webIntent);
            }
        });
		
		if (!verificaConexion(this)) {
        	Toast.makeText(this,"Comprueba tu conexión a Internet. Saliendo ... ", Toast.LENGTH_SHORT).show();
            this.finish();
        }
        else{
		
			date.setText(java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime()));
			
			JSONWeatherTask task = new JSONWeatherTask();
			task.execute(new String[]{city});
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

private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
		
		@Override
		protected Weather doInBackground(String... params) {
			Weather weather = new Weather();
			String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

			try {
				weather = JSONWeatherParser.getWeather(data);
				weather.iconData = ( (new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));
				
			} catch (JSONException e) {				
				e.printStackTrace();
			}
			return weather;
	}
		
	@Override
		protected void onPostExecute(Weather weather) {			
			super.onPostExecute(weather);
			
			if (weather.iconData != null && weather.iconData.length > 0) {
				Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length); 
				imgView.setImageBitmap(img);
			}
			
			temp.setText("" + Math.round((weather.temperature.getTemp() - 273.15)) + "°C");
			hum.setText("" + weather.currentCondition.getHumidity() + "%");
			press.setText("" + weather.currentCondition.getPressure() + " hPa");
			windSpeed.setText("" + weather.wind.getSpeed() + " mps");
				
		}
  }
}