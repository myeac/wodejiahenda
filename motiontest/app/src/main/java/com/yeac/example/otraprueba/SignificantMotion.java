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
    TextView mTextView;
    Context mContext;

    public SignificantMotion(Context pcontext, TextView ptvestado) {
        this.mContext = pcontext;
        this.mTextView = ptvestado;
    }
    public void iniciarSigMotion() {
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mSigMotion = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        mListener = new TriggerListener(mContext, mTextView);
        if (mSigMotion == null) {
            mTextView.setText("Sensor no disponible");
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
