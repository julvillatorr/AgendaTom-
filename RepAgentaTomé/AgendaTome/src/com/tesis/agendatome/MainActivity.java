package com.tesis.agendatome;

import java.util.concurrent.atomic.AtomicInteger;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.tesis.agendatome.adapter.CustomTabsPagerAdapter;
import com.tesis.agendatome.clima.Clima;
import com.tesis.agendatome.notification.RegisterApp;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements ActionBar.TabListener{
	
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
	public static final String EXTRA_MESSAGE = "message";
	public static final String PROPERTY_REG_ID = "registration_id";
	private static final String PROPERTY_APP_VERSION = "appVersion";
	private static final String TAG = "GCMRelated";
	GoogleCloudMessaging gcm;
	AtomicInteger msgId = new AtomicInteger();
	String regid;
	private ViewPager viewPager;
    private CustomTabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Títulos de la tabs
    private String[] tabs = { "Lugares", "Eventos"};
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Título ActionBar
        setTitle(" ");
        
        
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new CustomTabsPagerAdapter(getSupportFragmentManager());
        
        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
 
        // Agregar Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }
        
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
        	viewPager.setCurrentItem(1);
        	actionBar.setSelectedNavigationItem(1);
		}
        
        // Configurar movimiento swipepager seleccionado
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
 
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }
 
            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
        
        
        if (checkPlayServices()) {
	        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
	              regid = getRegistrationId(getApplicationContext());
	               
	              if (regid.isEmpty()) {
	                  new RegisterApp(getApplicationContext(), gcm, getAppVersion(getApplicationContext())).execute();
	              }else{
	            	  
	            	  //Toast.makeText(getApplicationContext(), "Device already Registered", Toast.LENGTH_SHORT).show();
	              }
	       } else {
	              Log.i(TAG, "No valid Google Play Services APK found.");
	       }
    }
 
    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }
 
    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }
 
    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.clima) {
			Intent i = new Intent(this, Clima.class);
			startActivity(i);
		}
		if (id == R.id.acerca_de) {
			Intent i = new Intent(this, AcercaDe.class);
			startActivity(i);
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	  * Check the device to make sure it has the Google Play Services APK. If
	  * it doesn't, display a dialog that allows users to download the APK from
	  * the Google Play Store or enable it in the device's system settings.
	  */
	  
	 private boolean checkPlayServices() {
	     int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
	     if (resultCode != ConnectionResult.SUCCESS) {
	         if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
	             GooglePlayServicesUtil.getErrorDialog(resultCode, this,
	                     PLAY_SERVICES_RESOLUTION_REQUEST).show();
	         } else {
	             Log.i(TAG, "Este dispositivo no es compatible.");
	             finish();
	         }
	         return false;
	     }
	     return true;
	 }
	  
	 /**
	  * Gets the current registration ID for application on GCM service.
	  * <p>
	  * If result is empty, the app needs to register.
	  *
	  * @return registration ID, or empty string if there is no existing
	  *         registration ID.
	  */
	 private String getRegistrationId(Context context) {
	     final SharedPreferences prefs = getGCMPreferences(context);
	     String registrationId = prefs.getString(PROPERTY_REG_ID, "");
	     if (registrationId.isEmpty()) {
	         Log.i(TAG, "Registration not found.");
	         return "";
	     }
	     // Check if app was updated; if so, it must clear the registration ID
	     // since the existing regID is not guaranteed to work with the new
	     // app version.
	     int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
	     int currentVersion = getAppVersion(getApplicationContext());
	     if (registeredVersion != currentVersion) {
	         Log.i(TAG, "App version changed.");
	         return "";
	     }
	     return registrationId;
	 }
	  
	 /**
	  * @return Application's {@code SharedPreferences}.
	  */
	 private SharedPreferences getGCMPreferences(Context context) {
		 return getSharedPreferences(MainActivity.class.getSimpleName(),
	             Context.MODE_PRIVATE);
	 }
	  
	 /**
	  * @return Application's version code from the {@code PackageManager}.
	  */
	 private static int getAppVersion(Context context) {
	     try {
	         PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
	         return packageInfo.versionCode;
	     } catch (NameNotFoundException e) {
	         throw new RuntimeException("Could not get package name: " + e);
	     }
	 }
 
}