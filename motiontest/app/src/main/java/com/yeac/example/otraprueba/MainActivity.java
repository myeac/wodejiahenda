package com.yeac.example.otraprueba;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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


    @BindView(R.id.btnService) Button btnService;
    @BindView(R.id.btnResult) Button btnResult;
    @BindView(R.id.btnRecorrido) Button btnRecorrido;
    @BindView(R.id.tvDistancia) TextView tvDistancia;
    @BindView(R.id.tvTiempo) TextView tvTiempo;
    @BindView(R.id.tvEstado) TextView tvEstado;

    SignificantMotion mSigMotion;

    void valoresIniciales(){
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        btnService.setTag(0);
        btnRecorrido.setTag(0);
        mSigMotion = new SignificantMotion(this,this.tvEstado);
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

    public void comenzarService(View view) {
        if((Integer) btnService.getTag() == 0){
            btnService.setText("Detener Service");
            btnService.setTag(1);
            Toast.makeText(this,"Se detuvo el Servicio",Toast.LENGTH_SHORT).show();
        }else{
            btnService.setText("Comenzar Service");
            btnService.setTag(0);
            Toast.makeText(this,"Inicio el Servicio",Toast.LENGTH_SHORT).show();
        }
    }
    public void comenzarRecorrido(View view) {
        if((Integer) btnRecorrido.getTag() == 0){
            btnRecorrido.setText("Detener Recorrido");
            btnRecorrido.setTag(1);
            Toast.makeText(this,"Se detuvo el Recorrido",Toast.LENGTH_SHORT).show();
        }else{
            btnRecorrido.setText("Comenzar Recorrido");
            btnRecorrido.setTag(0);
            Toast.makeText(this,"Inicio el Recorrido",Toast.LENGTH_SHORT).show();
        }
    }

    public void verResultados(View view) {

    }
}

