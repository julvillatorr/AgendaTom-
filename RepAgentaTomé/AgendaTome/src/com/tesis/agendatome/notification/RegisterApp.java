package com.tesis.agendatome.notification;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tesis.agendatome.MainActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

@SuppressWarnings("deprecation")
public class RegisterApp extends AsyncTask<Void, Void, String> {
	 
	 private static final String TAG = "GCMRelated";
	 Context ctx;
	 GoogleCloudMessaging gcm;
	 String SENDER_ID = "400376074682";
	 String regid = null; 
	 private int appVersion;
	 
	 public RegisterApp(Context ctx, GoogleCloudMessaging gcm, int appVersion){
		  this.ctx = ctx;
		  this.gcm = gcm;
		  this.appVersion = appVersion;
	 }	  
	  
	 @Override
	 protected void onPreExecute() {
		 super.onPreExecute();
	 }
	 	 
	 @Override
	 protected String doInBackground(Void... arg0) {
		 String msg = "";
	        try {
	            if (gcm == null) {
	                gcm = GoogleCloudMessaging.getInstance(ctx);
	            }
	            regid = gcm.register(SENDER_ID);
	            msg = "Dispositivo Registrado";
	 
	            sendRegistrationIdToBackend();
	 
	            storeRegistrationId(ctx, regid);
	        } catch (IOException ex) {
	            msg = "Error :" + ex.getMessage();
	            }
	        return msg;
	 }
	 
	 private void storeRegistrationId(Context ctx, String regid) {
	  final SharedPreferences prefs = ctx.getSharedPreferences(MainActivity.class.getSimpleName(),
	             Context.MODE_PRIVATE);
	     Log.i(TAG, "Saving regId on app version " + appVersion);
	     SharedPreferences.Editor editor = prefs.edit();
	     editor.putString("registration_id", regid);
	     editor.putInt("appVersion", appVersion);
	     editor.commit();
	 }
	 
	 private void sendRegistrationIdToBackend() {
		 URI url = null;
		 try {
			 url = new URI("http://146.83.198.59/~agendatome/tesis/rsc/notifications/register.php?regId=" + regid);
		  } catch (URISyntaxException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } 
		  HttpClient httpclient = new DefaultHttpClient();
		  HttpGet request = new HttpGet();
		  request.setURI(url);
		  try {
			  httpclient.execute(request);
		  } catch (ClientProtocolException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  } catch (IOException e) {
			  // TODO Auto-generated catch block
			  e.printStackTrace();
		  }
	 }
	 
	 @Override
	 protected void onPostExecute(String result) {
		 super.onPostExecute(result);
		 Log.v(TAG, result);
	 }
}