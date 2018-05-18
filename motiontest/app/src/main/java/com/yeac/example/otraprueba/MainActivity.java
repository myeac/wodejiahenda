package com.yeac.example.otraprueba;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    SensorManager mManager;
    Sensor  sAcelerometro, sMomento;
    TriggerEventListener mTriggerListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        mManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sMomento = mManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        mTriggerListener = new TriggerEventListener() {
            @Override
            public void onTrigger(TriggerEvent event) {
                Log.i("EventoSensor","Activado");
            }
        };
        mManager.requestTriggerSensor(mTriggerListener,sMomento);
    }


}
