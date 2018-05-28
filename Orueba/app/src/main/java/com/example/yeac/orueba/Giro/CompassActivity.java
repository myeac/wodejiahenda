package com.example.yeac.orueba.Giro;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yeac.orueba.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompassActivity extends AppCompatActivity implements SensorEventListener{
    //https://www.javacodegeeks.com/2013/09/android-compass-code-example.html
    public SensorManager mSensorManager;
    public Sensor mSensor;
    public float currentDegree = 0f;
    public float prevAngulo = 0f;

    @BindView(R.id.ivMain) ImageView ivMain;
    @BindView(R.id.tvHeading) TextView tvHeading;
    @BindView(R.id.chkbxCambio) CheckBox chkbxCambio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);
        ButterKnife.bind(this);
        getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this,mSensor,SensorManager.SENSOR_DELAY_GAME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if(chkbxCambio.isActivated()){
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float angulo = Math.round(event.values[0]);
            tvHeading.setText("Angulo: " + angulo);
            RotateAnimation ra = new RotateAnimation(currentDegree, -angulo,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            ra.setDuration(210);
            ra.setFillAfter(true);
            ivMain.startAnimation(ra);
            currentDegree = -angulo;
        }else{
            float angulo = event.values[0];
            int precision = 2;
            if(prevAngulo - angulo < precision * -1)
                ivMain.setImageResource(R.drawable.derecha);
            if(prevAngulo - angulo > precision)
                ivMain.setImageResource(R.drawable.izquierda);
            prevAngulo = angulo;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
