package com.example.yeac.orueba.Locgps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LocListener extends Service implements LocationListener{

    /* https://www.androidhive.info/2015/02/android-location-api-using-google-play-services/
    * https://stackoverflow.com/questions/1513485/how-do-i-get-the-current-gps-location-programmatically-in-android
    * https://www.dev2qa.com/android-update-ui-from-child-thread-example/
    * https://www.androidhive.info/2012/07/android-gps-location-manager-tutorial/
    * Accelerometer
    *   https://examples.javacodegeeks.com/android/core/hardware/sensor/android-accelerometer-example/
    *   https://code.tutsplus.com/tutorials/using-the-accelerometer-on-android--mobile-22125
    * */


    //lcation updates: https://developer.android.com/training/location/receive-location-updates

    //service variables
    LocationManager mLocMan;
    Location mLocInicial, mLocFinal;
    Activity mActivity;
    TextView tvInicio, tvFinal, tvVelocidad, tvTiempo, tvDistancia;
    List<Location> listaLocation;
    List<Float> listaDistancias;
    float distRecorrida = 0;

    //significant motion    One-shot (trigger)
    //step counter          on-change (sensorlistener)
    //Linear Acceleration   continuous (sensorlistener)

    Sensor sAcelerador, sMotion;
    SensorManager mAcelerador, mMotion;
    TriggerEventListener tlMotion;

    //Binder - comunicaicon con activity
    private IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder{
        public LocListener getService(){
            return LocListener.this;
        }
    }

    //tiempo
    public static String getTimeFormat(long ptiempo){
        Date enviar = new Date(ptiempo);
        DateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return sdf.format(enviar);
    }

    //Motion
    public String calcularVelocidad(){
        return String.valueOf(listaLocation.get(listaLocation.size() - 1).getSpeed()*3600/1000);
    }

    //distancias
    public static String actualizarPosicion(Location location){
        if(location != null)
            return  String.valueOf(location.getLatitude()) + " , " +
                    String.valueOf(location.getLongitude() + " - \n" +
                    String.valueOf(getTimeFormat(location.getTime())));
        else
            return "error null";
    }
    public String calcularDistancia(){
        if(!listaLocation.isEmpty()){
            float [] resultados = new float[1];
            Location ini = listaLocation.get(listaLocation.size() - 1);
            Location fin = listaLocation.get(listaLocation.size() - 2);
            Location.distanceBetween(ini.getLatitude(),ini.getLongitude(),
                                     fin.getLatitude(),fin.getLongitude(), resultados);
            listaDistancias.add(resultados[0]); //en metros
        }else{
            return "no suficientes hay datos";
        }
        return String.valueOf(distRecorrida);
    }
    public String calcularRecorrido(){
        calcularDistancia();
        //valores en metros
        float enviar = 0;
        for (int i = 0; i < listaDistancias.size(); i++)
            enviar += listaDistancias.get(i);
        return String.valueOf(enviar) + "\n" + "Puntos: " + String.valueOf(listaDistancias.size());
    }

    //imprimir y otors extra
    public void imprimirLista(List<Float> lista){
        for(int i = 0; i < lista.size();i++)
            Log.i("Distancia de " + i,"es ---" + lista.get(i));
    }
    public void setActivityLayout(Activity pactivity){
        this.mActivity = pactivity;
    }

    //update UI
    public void actualizarPosicionUI(){
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvInicio.setText(actualizarPosicion(mLocInicial));
                tvFinal.setText(actualizarPosicion(mLocFinal));
                tvVelocidad.setText(calcularVelocidad());
                tvDistancia.setText(calcularRecorrido());
                imprimirLista(listaDistancias);
            }
        });
    }
    public void setLayoutToUpdate(TextView pinicio,TextView pfinal,TextView pvel,TextView ptiempo,TextView pdistancia){
        this.tvInicio = pinicio;
        this.tvFinal = pfinal;
        this.tvVelocidad = pvel;
        this.tvTiempo = ptiempo;
        this.tvDistancia = pdistancia;
    }

    //Common
    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        listaLocation = new ArrayList<>();
        listaDistancias = new ArrayList<>();

        //LOCATION
        mLocMan = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        mLocFinal = mLocMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mLocMan.requestLocationUpdates(LocationManager.GPS_PROVIDER
                                        ,100,1,this);   //proveedor,millis,metros,listener
        listaLocation.add(mLocFinal);

        //MOTION - necesita implementar un triggerlistener,
        mMotion = (SensorManager) getApplicationContext().getSystemService(Context.SENSOR_SERVICE);
        sMotion = mMotion.getDefaultSensor(sMotion.TYPE_SIGNIFICANT_MOTION);
        tlMotion = new TriggerEventListener() {
            @Override
            public void onTrigger(TriggerEvent event) {
                Log.i("sensor:","Sensor de Momento cambio");
            }
        };
        mMotion.requestTriggerSensor(tlMotion,sMotion);

    }

    @Override
    public void onDestroy() {
        mLocMan.removeUpdates(this);
        stopSelf();
    }

    //EVENTOS DE SENSORES


    //service
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    //LocationListener
    @Override
    public void onLocationChanged(Location location) {
        mLocInicial = new Location(mLocFinal);
        mLocFinal = location;
        listaLocation.add(mLocFinal);
        actualizarPosicionUI();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(String provider) {

    }
    @Override
    public void onProviderDisabled(String provider) {

    }
}
