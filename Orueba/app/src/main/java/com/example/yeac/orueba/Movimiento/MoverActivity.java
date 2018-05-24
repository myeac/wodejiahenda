package com.example.yeac.orueba.Movimiento;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.DetectedActivity;

import com.example.yeac.orueba.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoverActivity extends AppCompatActivity {

    public final String TAG = MoverActivity.class.getSimpleName();

    BroadcastReceiver mBroadcastReceiver;

    @BindView(R.id.txt_confidence) TextView tvConfianza;
    @BindView(R.id.txt_activity) TextView tvActividad;
    @BindView(R.id.img_activity) TextView imgActividad;
    @BindView(R.id.btn_start_tracking) Button btnStart;
    @BindView(R.id.btn_stop_tracking) Button btnEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mover);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comenzarTracking();
            }
        });
        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminarTracking();
            }
        });

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(Constantes.detect_activity)){
                    int type = intent.getIntExtra("type",-1);
                    int confidence = intent.getIntExtra("confidence",0);
                    handleUserActivity(type,confidence);
                }
            }
        };
        comenzarTracking();
    }

    public void handleUserActivity(int ptype, int pconfidence){
        String enviar = "desconocido";
        int icon = -1;
        switch (ptype){
            case DetectedActivity.IN_VEHICLE:
                enviar = "Carro";
                icon = DetectedActivity.IN_VEHICLE;
                break;
            case DetectedActivity.ON_BICYCLE:
                enviar = "Bicicleta";
                icon = DetectedActivity.ON_BICYCLE;
                break;
            case DetectedActivity.ON_FOOT:
                enviar = "A pie";
                icon = DetectedActivity.ON_FOOT;
                break;
            case DetectedActivity.STILL:
                enviar = "Detenido";
                icon = DetectedActivity.STILL;
                break;
            case DetectedActivity.WALKING:
                enviar = "Caminando";
                icon = DetectedActivity.WALKING;
                break;
            case DetectedActivity.RUNNING:
                enviar = "Corriento";
                icon = DetectedActivity.RUNNING;
                break;
            case DetectedActivity.TILTING:
                enviar = "Inclinado";
                icon = DetectedActivity.TILTING;
                break;
            case DetectedActivity.UNKNOWN:
                enviar = "Desconocido";
                icon = DetectedActivity.UNKNOWN;
                break;
        }
        if(pconfidence > Constantes.confianza){
            tvActividad.setText(enviar);
            tvConfianza.setText("Confianza: " + pconfidence);
            imgActividad.setText(String.valueOf(icon));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mBroadcastReceiver, new IntentFilter(Constantes.detect_activity));
        //codigo del Broadcast para detectar cualquier envio de datos entre actividades
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    public void comenzarTracking(){
        startService(new Intent(MoverActivity.this,ServicioDeteccion.class));
    }
    public void terminarTracking(){
        stopService(new Intent(MoverActivity.this,ServicioDeteccion.class));
    }

}
