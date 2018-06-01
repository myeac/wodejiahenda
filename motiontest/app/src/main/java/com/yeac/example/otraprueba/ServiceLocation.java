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
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceLocation extends Service implements LocationListener{

//Objetos Principales
    private static final String TAG = "ServiceLocation";
    Activity mActivity;
    Location mLocation;
    LocationManager mLocationManager;
    float mDistanciaRecorrida;
    float minDistancia;
    long minTiempo;
    boolean isUP;
    TextView tvResultado, tvVelMaxima, tvAcMaxima, tvFreFuert;
    ScrollView scrollView;
    String strResultado;

//Valores Maximos
    Location vMaxima;
    float aMaxima;

//Datos a guardar
    List<Location> listaLocation;
    List<Float> listaDistancias;
    List<Long> listaTiempo;

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
    public float getvMaxima(){return vMaxima.getSpeed();}
    public float getaMaxima(){return aMaxima;}
    public List<Location> getListaLocation(){
        return  this.listaLocation;
    }
    public List<Float> getListaDistancias(){
        return  this.listaDistancias;
    }
    public List<Long> getListaTiempo(){
        return  this.listaTiempo;
    }

//Imprimir resultados

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

//velocidad y aceleracion
    public void setVelMaxima(Location p){
        if(p.getSpeed() > vMaxima.getSpeed()){
            vMaxima = p;
            tvVelMaxima.setText(String.valueOf(vMaxima.getSpeed()));
        }
    }


//UI
    public void imprimirLocation(Location p, int pos){
        System.out.println(pos + ": " + p.getSpeed() + " " + p.getLatitude() + " " + p.getLongitude());
        setVelMaxima(p);
        distanciaEntrePuntos();
        tiempoEntrePuntos();
        updateTableResult(p,pos);
    }
    public void updateTableResult(Location p, int ppos){
        int pos = ppos - 1;
        strResultado    += " \t " + pos
                        + " \t \t \t \t" + (p.getSpeed()*3600/1000)
                        + " \t \t \t \t \t \t" + ( listaTiempo.get(pos) / 1000.00)
                        + " \t \t \t \t"+ listaDistancias.get(pos) + "\n";
        tvResultado.setText(strResultado);
        scrollView.scrollTo(0, tvResultado.getBottom());
    }

//Distancia
    public void distanciaEntrePuntos(){
        Location inicio = listaLocation.get(listaLocation.size() - 2);
        Location fin = listaLocation.get(listaLocation.size() - 1);
        float result[] = new float[1];
        Location.distanceBetween(inicio.getLatitude(),inicio.getLongitude()
                                ,fin.getLatitude(),fin.getLongitude()
                                ,result);
        Log.i(TAG,"distancia: " + String.valueOf(result[0]));
        listaDistancias.add(result[0]);
    }
    public float recorridoTotal(){
        float enviar = 0;
        for(float x : listaDistancias){
            enviar += x;
        }
        return enviar;
    }

//Tiempo
    public void tiempoEntrePuntos(){
        long inicio = listaLocation.get(listaLocation.size() - 2).getTime();
        long fin = listaLocation.get(listaLocation.size() - 1).getTime();
        long dif = fin - inicio;
        listaTiempo.add(dif);
    }
    public long sumaTiempos(){
        long enviar = 0;
        for(long x : listaTiempo)
            enviar +=x;
        return enviar;
    }
    public String calcularTiempo(){
        long total = sumaTiempos();

        long secEmilli = 1000;
        long minEmilli = secEmilli * 60;
        long horEmilli = minEmilli * 60;
        long diaEmilli = horEmilli * 24;

        long dias = total/ diaEmilli; total = total % diaEmilli;
        long horas = total/ horEmilli; total = total % horEmilli;
        long minutos = total/ minEmilli; total = total % minEmilli;
        long segundos = total/ secEmilli;

        if(dias == 0)
            return horas +  " H " + minutos +  " m " + segundos + " s";
        else
            return dias + " d " + horas +  " H " + minutos +  " m " + segundos + " s";
    }

//General
    public void valoresGenerales(){
        listaDistancias = new ArrayList<>();
        listaLocation = new ArrayList<>();
        listaTiempo = new ArrayList<>();
        listaDistancias.add((float) 0000.00);
        listaTiempo.add((long) 0);
        strResultado = "";
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

//LocationListener
    @Override
    public void onLocationChanged(Location plocation){
        Log.i(TAG,"LocationChanged");
        Location nuevo = new Location(plocation);
        listaLocation.add(nuevo);
        if(listaLocation.size() >= 2 && isUP){
            Log.i(TAG, "Acccuracy: " + String.valueOf(nuevo.getAccuracy()));
            imprimirLocation(nuevo,listaLocation.size());

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
