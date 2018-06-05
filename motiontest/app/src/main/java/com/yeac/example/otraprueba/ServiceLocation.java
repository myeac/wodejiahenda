package com.yeac.example.otraprueba;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ServiceLocation extends Service implements LocationListener{

//Objetos Principales
    private static final String TAG = "ServiceLocation";
    Activity mActivity;
    Location mLocation;
    LocationManager mLocationManager;
    float minDistancia;
    long minTiempo;
    boolean isUP;

    TextView tvResultado, tvVelMaxima, tvAcMaxima, tvFreFuert;
    ScrollView scrollView;
    String strResultado;

//Valores Maximos
    float vMaxima, aMaxima, aFreno;

//Datos a guardar
    List<Posiciones> listaPos;
    int listaTamano;

//setters del Service
    public void setLocListener(long ptiempo, float pdistancia){
        this.minDistancia = pdistancia;
        this.minTiempo = ptiempo;
    }
    public void setmActivity(Activity pactivity){
        this.mActivity = pactivity;
    }
    public void setTexvViewResult(TextView ptv, ScrollView psv){
        this.tvResultado = ptv;
        this.scrollView = psv;
    }
    public void setTextViewMotionValues(TextView pVel, TextView pAc, TextView pFreno){
        this.tvVelMaxima = pVel;
        this.tvAcMaxima = pAc;
        this.tvFreFuert = pFreno;
    }

//EventosListener
    @SuppressLint("MissingPermission")
    public void iniciarLocationService(){
        isUP = true;
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER
                                                ,minTiempo
                                                ,minDistancia
                                                ,this);
    }
    public void terminarLocationService(){
        mLocationManager.removeUpdates(this);
        isUP = false;
        stopSelf();
    }
    SensorEventListener selAccelerometer = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    SensorEventListener selLinearAcc = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

//General
    public void valoresGenerales(){
        listaPos = new ArrayList<>();
        strResultado = "";
        listaTamano = 0;
        vMaxima = 0;
        aMaxima = 0;
        aFreno = 0;
    }

//Binder
    public IBinder binder = new LocalBinder();
    public class LocalBinder extends Binder{
        public ServiceLocation getService(){
            return ServiceLocation.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

//Common
    @Override
    public void onCreate() {
        super.onCreate();
        valoresGenerales();
        isUP = true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public float timeDif(Location actual){
        long fin = actual.getTime();
        long inicio = listaPos.get(listaTamano - 1).getLocation().getTime();
        return  (float)(fin - inicio)/1000;
    }
    public float posDif(Location fin){
        float result[] = new float[1];
        Location init = listaPos.get(listaTamano - 1).getLocation();
        Location.distanceBetween(init.getLatitude(),init.getLongitude(),
                                fin.getLatitude(),fin.getLongitude(),
                                result);
        return  result[0];
    }
    public float accDif(Posiciones fin){
        float acc = 0;
        Posiciones ini = listaPos.get(listaTamano - 1);
        float difVel = fin.getLocation().getSpeed() - ini.getLocation().getSpeed();
        acc = difVel / (fin.getTiempoDif());
        return  acc;
    }
    public String parseoTiempo(long tDif){
        long secEmilli = 1000;
        long minEmilli = secEmilli * 60;
        long horEmilli = minEmilli * 60;
        long diaEmilli = horEmilli * 24;

        long dias = tDif/ diaEmilli; tDif = tDif % diaEmilli;
        long horas = tDif/ horEmilli; tDif = tDif % horEmilli;
        long minutos = tDif/ minEmilli; tDif = tDif % minEmilli;
        long segundos = tDif/ secEmilli;

        if(dias == 0)
            return horas +  " H " + minutos +  " m " + segundos + " s";
        else
            return dias + " d " + horas +  " H " + minutos +  " m " + segundos + " s";
    }
    public String getTotalTime(){
        long enviar = 0;
        for(Posiciones x : listaPos){
            enviar += (x.getTiempoDif()*1000);
        }
        return parseoTiempo(enviar);
    }
    public String getTotalDist(){
        float enviar = 0;
        for(Posiciones x : listaPos){
            enviar += x.getDistanciaDif();
        }
        return String.valueOf(enviar);
    }
    public void checkMaxSpeed(Location actual){
        vMaxima = (actual.getSpeed() > vMaxima) ? actual.getSpeed() : vMaxima;
        tvVelMaxima.setText(String.valueOf(vMaxima));
    }
    public void checkMaxMinAcceleration(Posiciones actual){
        aMaxima = (actual.getAceleracion() > aMaxima) ? actual.getAceleracion() : aMaxima;
        tvAcMaxima.setText(String.valueOf(aMaxima));
        aFreno = (actual.getAceleracion() < aFreno) ? actual.getAceleracion() : aFreno;
        tvFreFuert.setText(String.valueOf(aFreno));
    }
    public void updateTableResult(Posiciones p, int ppos){
        int pos = ppos - 1;
        strResultado    += " \t " + pos
                + " \t \t \t \t" + (p.getLocation().getSpeed()*(3600/1000)) //eb metros
                + " \t \t \t \t \t \t" + (p.getTiempoDif())                 //en seg
                + " \t \t \t \t"+ (p.getDistanciaDif()) + "\n";
        tvResultado.setText(strResultado);
        scrollView.scrollTo(0, tvResultado.getBottom());
    }
//LocationListener
    @Override
    public void onLocationChanged(Location plocation){
        Log.i(TAG,"LocationChanged");
        Posiciones enviar = new Posiciones(plocation);
        if(listaPos.isEmpty()){
            enviar.setTiempoDif(0);
            enviar.setDistanciaDif(0);
            enviar.setAceleracion(0);
            enviar.setStatus("pos inicial");
            listaPos.add(enviar);
            listaTamano = listaPos.size();
        }else {
            enviar.setTiempoDif(timeDif(plocation));
            enviar.setDistanciaDif(posDif(plocation));
            enviar.setAceleracion(accDif(enviar));
            enviar.setStatus("por definir");
            checkMaxSpeed(plocation);
            checkMaxMinAcceleration(enviar);
            listaPos.add(enviar);
            listaTamano = listaPos.size();
            updateTableResult(enviar,listaTamano);
        }
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
