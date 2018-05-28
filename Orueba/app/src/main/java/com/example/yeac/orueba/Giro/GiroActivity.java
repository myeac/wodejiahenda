package com.example.yeac.orueba.Giro;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yeac.orueba.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GiroActivity extends AppCompatActivity implements SensorEventListener {

    //https://stackoverflow.com/questions/17679312/android-detect-turn-from-phone

    float accX,accY,accZ;
    float last_x,last_y,last_z;

    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;

    @BindView(R.id.x_axis) TextView tvX;
    @BindView(R.id.y_axis) TextView tvY;
    @BindView(R.id.z_axis) TextView tvZ;
    @BindView(R.id.image) ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giro);
        ButterKnife.bind(this);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this,
                mAccelerometer ,  SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long tiempo;

        accX = event.values[0];      //  m/s2
        accY = event.values[1];      //  m/s2
        accZ = event.values[2];      //  m/s2

        tiempo = System.currentTimeMillis();

        tvX.setText(Float.toString(accX));
        tvY.setText(Float.toString(accY));
        tvZ.setText(Float.toString(accZ));

        if (accX > accY) {
            iv.setImageResource(R.drawable.derecha);
        } else if (accY > accX) {
            iv.setImageResource(R.drawable.izquierda);
        } else{
            iv.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
