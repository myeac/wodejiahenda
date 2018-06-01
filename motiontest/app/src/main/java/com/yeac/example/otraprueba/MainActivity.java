package com.yeac.example.otraprueba;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*otros:
    https://stackoverflow.com/questions/29030943/
    https://android.googlesource.com/platform/development/+/master/samples/ApiDemos/src/com/example/android/apis/os/TriggerSensors.java
    SignificantMotion: https://github.com/ysimonx/Android_TYPE_SIGNIFICANT_MOTION
    https://github.com/kesenhoo/AndroidApiDemo/blob/master/app/src/main/java/com/example/android/apis/os/TriggerSensors.java
    https://stackoverflow.com/questions/17679312/android-detect-turn-from-phone
*/

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity" ;
    @BindView(R.id.btnRecorrido) Button btnRecorrido;
    @BindView(R.id.tvDistancia) TextView tvDistancia;
    @BindView(R.id.tvTiempo) TextView tvTiempo;
    @BindView(R.id.tvEstado) TextView tvEstado;
    @BindView(R.id.tvTOP) TextView tvTOP;
    @BindView(R.id.tvResultados) TextView tvResultado;
    @BindView(R.id.etMinDist) EditText etMinDist;
    @BindView(R.id.etMinTiempo) EditText etMinTiempo;
    @BindView(R.id.svResultado) ScrollView svResult;
    @BindView(R.id.tvVmax) TextView tvVmaxima;
    @BindView(R.id.tvAmax) TextView tvAmaxima;
    @BindView(R.id.tvFreno) TextView tvUsoFreno;

    SignificantMotion mSigMotion;

    Intent mIntentServ;
    ServiceConnection mServConn;
    ServiceLocation mService;

    List<Location> mListaLocation;
    List<Long> mListaTiempo;
    List<Float> mListaDistancia;

//datos service:
    public void valoresIniciales(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        tvTOP.setText("\t Pos \t \t Vel(km/h) \t \t Tiempo \t \t Distancia\n");
        btnRecorrido.setTag(0);
        tvResultado.setMovementMethod(new ScrollingMovementMethod());
        mSigMotion = new SignificantMotion(this);
    }
    public void iniciarService(){
        mIntentServ = new Intent(this, ServiceLocation.class);
        mServConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = ((ServiceLocation.LocalBinder) service).getService();
                mService.setmActivity(MainActivity.this);
                mService.setLocListener(Long.valueOf(etMinTiempo.getText().toString())
                                        ,Float.valueOf(etMinDist.getText().toString()));
                mService.iniciarLocationService();
                mService.setTexvViewResult(tvResultado,svResult);
                mService.setTextViewMotionValues(tvVmaxima,tvAmaxima,tvUsoFreno);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        };
        bindService(mIntentServ,mServConn,BIND_AUTO_CREATE);
        startService(mIntentServ);
    }
    public void terminarservice(){
        mService.terminarLocationService();
//        mListaLocation = mService.getListaLocation();
//        mListaDistancia = mService.getListaDistancias();
//        mListaTiempo = mService.getListaTiempo();
        tvDistancia.setText(String.valueOf(mService.recorridoTotal()));
        tvTiempo.setText(mService.calcularTiempo());
        Log.i(TAG,"Recorrido Total: " + mService.recorridoTotal());
        stopService(mIntentServ);
        mIntentServ = null;
        mService = null;
        mServConn = null;
    }

//UI
    public void actualizarUI(){
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        valoresIniciales();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    public void comenzarRecorrido(View view) {
        if((int) btnRecorrido.getTag() == 0){
            btnRecorrido.setText("Fin Recorrido");
            btnRecorrido.setTag(1);
            etMinDist.setEnabled(false);
            etMinTiempo.setEnabled(false);
            iniciarService();
        }else{
            btnRecorrido.setText("Iniciar Recorrido");
            btnRecorrido.setTag(0);
            etMinDist.setEnabled(true);
            etMinTiempo.setEnabled(true);
            terminarservice();
        }
    }

}

