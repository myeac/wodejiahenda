package com.example.yeac.orueba.Locgps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yeac.orueba.R;
import com.example.yeac.orueba.Util;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocgpsActivity extends AppCompatActivity {


    @BindView(R.id.tvPrimera) TextView tvPosPrimera;
    @BindView(R.id.tvInicial) TextView tvPosInicial;
    @BindView(R.id.tvfinal) TextView tvPosFinal;
    @BindView(R.id.tvDistancia) TextView tvDistancia;
    @BindView(R.id.tvVelocidad) TextView tvVelocidad;
    @BindView(R.id.tvTiempo) TextView tvTiempo;
    @BindView(R.id.chroReloj) Chronometer chroReloj;

    Location mLocInicial;
    LocationManager mLocationManager;
    LocListener mService;
    ServiceConnection mServiceConn;
    Intent mIntentServ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locgps);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ButterKnife.bind(this);
        //http://abhiandroid.com/ui/chronometer#Attributes_of_Chronometer_In_Android
        chroReloj.start();

        mIntentServ = new Intent(this,LocListener.class);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        //obtener posicion incial
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocInicial = mLocationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        tvPosPrimera.setText(LocListener.actualizarPosicion(mLocInicial));

        //servicio de actualizacion de posicion: posinicial y posfinal
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = ((LocListener.LocalBinder) service).getService();
                mService.setActivityLayout(LocgpsActivity.this);
                mService.setLayoutToUpdate(tvPosInicial,tvPosFinal,tvVelocidad,tvTiempo,tvDistancia);
//                Thread t = new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            while (!isInterrupted()) {
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        tvPosFinal.setText(mService.enviarLocation());
//                                    }
//                                });
//                                Thread.sleep(10);
//                            }
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                t.start();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(mIntentServ,mServiceConn,BIND_AUTO_CREATE);
        startService(mIntentServ);
    }


    public void actualizarConThread() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }



}

