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

*/
public class MainActivity extends AppCompatActivity {
    //example: https://github.com/ysimonx/Android_TYPE_SIGNIFICANT_MOTION
    private  SensorManager mSensorManager;
    private  Sensor mSigMotion;
    private  TriggerEventListener mListener;
    private TextView tv,tvX,tvY,tvZ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        mListener = new TriggerListener(this);

        tv = (TextView) findViewById(R.id.tv);
        tvX = (TextView) findViewById(R.id.tvX);
        tvY = (TextView) findViewById(R.id.tvY);
        tvZ = (TextView) findViewById(R.id.tvZ);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mSigMotion = mSensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mSensorManager != null) {
            mSensorManager.requestTriggerSensor(mListener, mSigMotion);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Call disable to ensure that the trigger request has been canceled.
        mSensorManager.cancelTriggerSensor(mListener, mSigMotion);
    }

    class TriggerListener extends TriggerEventListener {
        Context context;

        public TriggerListener(Context pcontext){
            this.context = pcontext;
            Toast.makeText(pcontext, "Trigger Creado",Toast.LENGTH_SHORT).show();
        }

        public void onTrigger(TriggerEvent event) {
            Toast.makeText(this.context, "significant motion detected",Toast.LENGTH_SHORT).show();
            long currentTimeStamp = System.currentTimeMillis();
            tv.setText("Last movement triggered at "+String.valueOf(currentTimeStamp));
            tvX.setText(String.valueOf(event.values[0]));
            tvY.setText(String.valueOf(event.values[1]));
            tvZ.setText(String.valueOf(event.values[2]));
        }
    }
}
