package com.example.yeac.orueba.Locgps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LocListener extends Service implements LocationListener{

    //https://www.androidhive.info/2015/02/android-location-api-using-google-play-services/
    //https://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android
    //https://www.dev2qa.com/android-update-ui-from-child-thread-example/
    //https://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/

    LocationManager mLocMan;
    Location mLocInicial,mLocFinal;
    Activity mActivity;
    TextView tvInicio,tvFinal,tvVelocidad,tvTiempo;
    List<Location> listaLocation;


    private IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder{
        public LocListener getService(){
            return LocListener.this;
        }
    }

    public void setActivityLayout(Activity pactivity){
        this.mActivity = pactivity;
    }
    public String actualizarPosicion(Location location){
        if(location != null)
            return String.valueOf(location.getLatitude()) + " , " + String.valueOf(location.getLongitude());
        else
            return "error null";
    }
    public void actualizarPosicionUI(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvInicio.setText(actualizarPosicion(mLocInicial));
                tvFinal.setText(actualizarPosicion(mLocFinal));
            }
        });
    }
    public void setLayoutToUpdate(TextView pinicio,TextView pfinal,TextView pvel,TextView ptiempo){
        this.tvInicio = pinicio;
        this.tvFinal = pfinal;
        this.tvVelocidad = pvel;
        this.tvTiempo = ptiempo;
    }


    //Common
    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        listaLocation = new ArrayList<>();
        mLocMan = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        mLocFinal = mLocMan.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        mLocMan.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER
                                        ,1000,10,this);
    }
    @Override
    public void onDestroy() {
        stopSelf();
    }


    //service
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    //LocationListener
    @Override
    public void onLocationChanged(Location location) {
        mLocInicial = new Location(mLocFinal);
        mLocFinal = location;
        listaLocation.add(mLocFinal);
        actualizarPosicionUI();
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
