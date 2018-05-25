package com.example.yeac.orueba.Locgps;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
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
    @BindView(R.id.etMintiempo) EditText etTiempo;
    @BindView(R.id.etMinDist) EditText etDistancia;
    @BindView(R.id.btnStartService) Button btnStartService;

    Location mLocInicial;
    LocListener mService;
    LocationManager mLocationManager;
    ServiceConnection mServiceConn;
    Intent mIntentServ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locgps);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        btnStartService.setTag(0);  //apagado
        //http://abhiandroid.com/ui/chronometer#Attributes_of_Chronometer_In_Android
        chroReloj.start();

        mIntentServ = new Intent(this,LocListener.class);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        //obtener posicion incial
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocInicial = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        tvPosPrimera.setText(LocListener.actualizarPosicion(mLocInicial));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(mIntentServ);
        mLocationManager = null;
        mLocInicial = null;
    }

    //servicio de actualizacion de posicion: posinicial y posfinal
    public void comenzarServicio(View view){
        if(btnStartService.getTag().equals(0)){
            mServiceConn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    mService = ((LocListener.LocalBinder) service).getService();
                    mService.setActivityLayout(LocgpsActivity.this);
                    mService.setServiceParameters(  Long.valueOf(etTiempo.getText().toString()),
                                                    Float.valueOf(etDistancia.getText().toString()));
                    mService.setLayoutToUpdate(tvPosInicial,tvPosFinal,tvVelocidad,tvTiempo,tvDistancia);
                }
                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            bindService(mIntentServ,mServiceConn,BIND_AUTO_CREATE);
            startService(mIntentServ);

            btnStartService.setTag(1);
            btnStartService.setText("Detener");
            etDistancia.setEnabled(false);
            etTiempo.setEnabled(false);
        }else{
            stopService(mIntentServ);
            mServiceConn = null;
            mLocationManager = null;
            mLocInicial = null;

            btnStartService.setTag(0);
            btnStartService.setText("Iniciar");
            etDistancia.setEnabled(true);
            etTiempo.setEnabled(true);
        }

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

