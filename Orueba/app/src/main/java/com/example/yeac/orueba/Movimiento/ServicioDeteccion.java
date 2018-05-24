package com.example.yeac.orueba.Movimiento;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class ServicioDeteccion extends Service{
    //https://www.androidhive.info/2017/12/android-user-activity-recognition-still-walking-running-driving-etc/
    public static final String TAG = ServicioDeteccion.class.getSimpleName();

    Intent mIntent;
    PendingIntent mPendingIntent;
    ActivityRecognitionClient mActivityRecClient;

    IBinder mBinder = new ServicioDeteccion.LocalBinder();

    public class LocalBinder extends Binder{
        public ServicioDeteccion getInstance(){
            return ServicioDeteccion.this;
        }
    }
    public ServicioDeteccion(){}

    public void actualizarActividadHandler(){
        Task<Void> tarea = mActivityRecClient.requestActivityUpdates(Constantes.intervalo_deteccion_milis,mPendingIntent);
        tarea.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Actividades Actualizadas", Toast.LENGTH_SHORT).show();
            }
        });
        tarea.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Fallo Actualizacion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removerActividadHandler(){
        Task<Void> tarea = mActivityRecClient.removeActivityUpdates(mPendingIntent);
        tarea.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Actualizaciones Removidas", Toast.LENGTH_SHORT).show();
            }
        });
        tarea.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Fallo Remover Actualizaciones", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mActivityRecClient = new ActivityRecognitionClient(this);
        mIntent = new Intent(this,DetectarActividad.class);
        mPendingIntent = PendingIntent.getService(this,1,mIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        actualizarActividadHandler();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removerActividadHandler();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }



}
