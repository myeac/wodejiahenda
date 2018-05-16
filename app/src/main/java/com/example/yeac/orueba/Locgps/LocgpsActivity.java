package com.example.yeac.orueba.Locgps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yeac.orueba.R;
import com.example.yeac.orueba.Util;

import java.util.ArrayList;

import butterknife.BindView;

public class LocgpsActivity extends AppCompatActivity {

    Location mLocation;
    LocationManager mLocationManager;
    LocListener mLocationListener;
    ArrayList<Location> mListLocation;

    @BindView(R.id.tvInicial) TextView tvPosInicial;
    @BindView(R.id.tvfinal) TextView tvPosFinal;
    @BindView(R.id.tvDistancia) TextView tvDistancia;
    @BindView(R.id.tvVelocidad) TextView tvVelocidad;
    @BindView(R.id.tvTiempo) TextView tvTiempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locgps);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        localizacionActual();

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(mLocationManager.GPS_PROVIDER      //proveedor
                , 1000          //Miliseconds
                , 10         //Meters
                , mLocationListener);   //Listener

    }

    public void localizacionActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        Location enviar = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

    }


    @Override
    protected void onStart() {
        super.onStart();

    }


}

