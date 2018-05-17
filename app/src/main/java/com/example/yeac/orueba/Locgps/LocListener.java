package com.example.yeac.orueba.Locgps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import butterknife.BindView;

public class LocListener extends Service implements LocationListener{

    //https://www.androidhive.info/2015/02/android-location-api-using-google-play-services/
    //https://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android
    //https://www.dev2qa.com/android-update-ui-from-child-thread-example/
    //https://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/

    LocationManager mLocMan;
    Location mLocationActual;
    Activity mActivity;
    TextView tvInicio,tvFinal,tvVelocidad,tvTiempo;


    private IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder{
        public LocListener getService(){
            return LocListener.this;
        }
    }

    public String enviarLocation(){
        if(mLocationActual != null)
            return String.valueOf(mLocationActual.getLatitude()) + " , " + String.valueOf(mLocationActual.getLongitude());
        else
            return "error null";
    }
    public void setActivityLayout(Activity pactivity){
        this.mActivity = pactivity;
    }
    public void actualziarUI(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvFinal.setText(enviarLocation());
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
        mLocMan = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        mLocationActual = mLocMan.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        mLocMan.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER
                                        ,10,0,this);
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
        this.mLocationActual = location;
        actualziarUI();
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
