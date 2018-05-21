package com.example.yeac.orueba.Accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yeac.orueba.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener{

    SensorManager mSensorManager, mLinearManager;
    Sensor sAcelerometer,sLineal;

    long lastUpdate = 0;
    float last_x,last_y,last_z;
    static final int shake_treshhold = 600;

    @BindView(R.id.tvX) TextView tvXacel;
    @BindView(R.id.tvY) TextView tvYacel;
    @BindView(R.id.tvZ) TextView tvZacel;
    @BindView(R.id.tvVel) TextView tvVel;
    @BindView(R.id.tvlinX) TextView tvlinX;
    @BindView(R.id.tvlinY) TextView tvlinY;
    @BindView(R.id.tvlinZ) TextView tvlinZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);
        ButterKnife.bind(this);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sAcelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sLineal = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mSensorManager.registerListener(this,sAcelerometer,SensorManager.SENSOR_DELAY_NORMAL);

        mLinearManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mLinearManager.registerListener(this,sLineal,SensorManager.SENSOR_DELAY_NORMAL);

    }


    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        mLinearManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorManager.unregisterListener(this);
        mLinearManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,sAcelerometer,SensorManager.SENSOR_DELAY_NORMAL);
        mLinearManager.registerListener(this,sLineal,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor miSensor = event.sensor;
        float accX,accY,accz,linX,linY,linZ;
        long tiempoactual;

        if(miSensor.getType() == Sensor.TYPE_ACCELEROMETER){
            accX = event.values[0];      //  m/s2
            accY = event.values[1];      //  m/s2
            accz = event.values[2];      //  m/s2
            tiempoactual = System.currentTimeMillis();

            if(tiempoactual - lastUpdate > 100){
                long diferencia = (tiempoactual - lastUpdate);
                lastUpdate = tiempoactual;
                float velocidad = Math.abs(accX + accY + accz - last_x - last_y - last_z)/ diferencia * 10000;

                if(velocidad > shake_treshhold){
                    tvXacel.setText(String.valueOf(accX));
                    tvYacel.setText(String.valueOf(accY));
                    tvZacel.setText(String.valueOf(accz));
                    tvVel.setText(String.valueOf(velocidad));
                }
                last_x = accX;
                last_y = accY;
                last_z = accz;
            }
        }
        if(miSensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
            linX = event.values[0];      //  m/s2
            linY = event.values[1];      //  m/s2
            linZ = event.values[2];      //  m/s2

            tiempoactual = System.currentTimeMillis();

            if(tiempoactual - lastUpdate > 100){
                long diferencia = (tiempoactual - lastUpdate);
                lastUpdate = tiempoactual;
                float velocidad = Math.abs(linX + linY + linZ - last_x - last_y - last_z)/ diferencia * 10000;

                if(velocidad > shake_treshhold){
                    tvlinX.setText(String.valueOf(linX));
                    tvlinY.setText(String.valueOf(linY));
                    tvlinZ.setText(String.valueOf(linZ));
                }
                last_x = linX;
                last_y = linY;
                last_z = linZ;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
