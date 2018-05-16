package com.example.yeac.orueba.Locgps;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class LocListener extends Service implements LocationListener {

    //https://www.androidhive.info/2015/02/android-location-api-using-google-play-services/
    //https://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android
    //https://www.dev2qa.com/android-update-ui-from-child-thread-example/
    //https://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/

    TextView tvInicio, tvFinal;
    ArrayList<String> listPos;
    Activity mActivity;

    //flags
    boolean isGPSenabled = false;
    boolean isNetworkEnabled = false;
    boolean isCanGetLocation = false;

    LocationManager mLocationManager;
    Location mLocation;
    double mLat, mLong;

    private static final long MIN_DIST_FOR_UPDATE = 10; //meters
    private static final long MIN_TIME_FOR_UPDATE = 2000; //miliseconds (2 sec)

    //Constructor
    public LocListener(Activity pActivity, TextView pinicio, TextView pfinal, LocationManager pLocationMan) {
        this.tvInicio = pinicio;
        this.tvFinal = pfinal;
        this.mActivity = pActivity;
        this.mLocationManager = pLocationMan;
        listPos = new ArrayList<>();
    }


    public Location getLocation() {
        Location enviar = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mActivity.getApplicationContext(), "Habilite GPS", Toast.LENGTH_SHORT).show();
            return null;
        }
        try {
            //gpsStatus
            isGPSenabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            //network status
            isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSenabled && !isNetworkEnabled) {
                Toast.makeText(mActivity.getApplicationContext(), "No hay proveedores", Toast.LENGTH_SHORT).show();
            } else {
                isCanGetLocation = true;
                if (isNetworkEnabled) {

                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_FOR_UPDATE,
                            MIN_DIST_FOR_UPDATE,
                            this);
                    if(mLocationManager != null){
                        enviar = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(enviar != null){
                            mLat = enviar.getLatitude();
                            mLong = enviar.getLongitude();
                        }else{
                            Toast.makeText(mActivity.getApplicationContext(), "Location es NULL", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(mActivity.getApplicationContext(), "LocationManager es NULL", Toast.LENGTH_SHORT).show();
                    }
                }
                if(isGPSenabled){
                    if(enviar == null){
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_FOR_UPDATE,
                                MIN_DIST_FOR_UPDATE,
                                this);
                        if(mLocationManager != null){
                            enviar = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(enviar != null){
                                mLat = enviar.getLatitude();
                                mLong = enviar.getLongitude();
                            }else{
                                Toast.makeText(mActivity.getApplicationContext(), "Location es NULL", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(mActivity.getApplicationContext(), "LocationManager es NULL", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }
        return enviar;
    }
    public void actualizarConThread() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //update
                            }
                        });
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }


    //service
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    //http://rdcworld-android.blogspot.pe/2012/01/get-current-location-coordinates-city.html

    //locationlistener
    @Override
    public void onLocationChanged(Location nuevo) {
        tvInicio.setText(nuevo.getLatitude() + " , " + nuevo.getLongitude());
        listPos.add(tvInicio.getText().toString());
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onProviderDisabled(String provider) {

    }



}
