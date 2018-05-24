package com.yeac.example.otraprueba;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.widget.TextView;

public class SignificantMotion{

    SensorManager mSensorManager;
    Sensor mSigMotion;
    TriggerListener mListener;
    Context mContext;
    boolean activo;


    public SignificantMotion(Context pcontext) {
        this.mContext = pcontext;
    }
    public void iniciarSigMotion(TextView ptxt) {
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mSigMotion = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        mListener = new TriggerListener(mContext, ptxt);
        if (mSigMotion == null) {
            ptxt.setText("Sensor no disponible");
        }
    }
    class TriggerListener extends TriggerEventListener {
        Context context;
        TextView tvTexto;

        public TriggerListener(Context pcontext, TextView ptext) {
            this.context = pcontext;
            this.tvTexto = ptext;
        }
        @Override
        public void onTrigger(TriggerEvent event) {
            if (event.values[0] == 1) {
                tvTexto.setText("Movimiento");
                tvTexto.setText("Desactivado");
            }
            //sensor se desactiva automaticamente
        }
    }
}
