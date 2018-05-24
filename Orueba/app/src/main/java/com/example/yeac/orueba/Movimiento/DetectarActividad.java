package com.example.yeac.orueba.Movimiento;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class DetectarActividad extends IntentService {

    public static final String TAG = "DetectarActividad";
    public DetectarActividad(){
        super(TAG);
    }
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
        ArrayList<DetectedActivity> listaActvidades = (ArrayList) result.getProbableActivities();
        for(DetectedActivity x : listaActvidades){
            Log.i(TAG,"Actividad: " + x.getType() + ", Confianza: " + x.getConfidence());
            broadcastActividad(x);
        }
    }

    public void broadcastActividad(DetectedActivity pvalue){
        Intent intent = new Intent(Constantes.detect_activity);
        intent.putExtra("type", pvalue.getType());
        intent.putExtra("confidence",pvalue.getConfidence());
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
