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
import android.widget.TextView;
import android.widget.Toast;

/*otros:
    https://stackoverflow.com/questions/29030943/
    https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/os/TriggerSensors.java
    example: https://github.com/ysimonx/Android_TYPE_SIGNIFICANT_MOTION
*/
public class MainActivity extends AppCompatActivity {
    //https://github.com/kesenhoo/AndroidApiDemo/blob/master/app/src/main/java/com/example/android/apis/os/TriggerSensors.java

    SensorManager mSensorManager;
    Sensor mSigMotion;
    TriggerListener mListener;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSigMotion = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
        mTextView = (TextView)findViewById(R.id.text);
        mListener = new TriggerListener(this, mTextView);
        if (mSigMotion == null) {
            mTextView.setText("Sensor no disponible");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSigMotion != null && mSensorManager.requestTriggerSensor(mListener, mSigMotion))
            mTextView.setText("Esperando");
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mSigMotion != null) mSensorManager.cancelTriggerSensor(mListener, mSigMotion);
    }

    class TriggerListener extends TriggerEventListener {
        Context context;
        TextView tvTexto;
        public TriggerListener(Context pcontext, TextView ptext){
            this.context = pcontext;
            this.tvTexto = ptext;
        }
        @Override
        public void onTrigger(TriggerEvent event) {
            if(event.values[0] == 1){
                tvTexto.setText("Movimiento");
                tvTexto.setText("Desactivado");
            }
            //sensor se desactiva automaticamente
        }
    }

}
