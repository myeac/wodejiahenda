package com.yeac.example.otraprueba;

import android.location.Location;

public class Posiciones {

    //se tomara como referencia esos puntos, mas no en un intervalo de fecha
    //dependiendo de los datos obtenidos del listener

    public Location location;
    public float distanciaDif;
    public float aceleracion;
    public String status;
    public float tiempoDif;

    public Posiciones(Location p){
        this.location = p;
    }
    public Posiciones(float distanciaDif, float aceleracion, String status) {
        this.distanciaDif = distanciaDif;
        this.aceleracion = aceleracion;
        this.status = status;
    }

    public float getDistanciaDif() {
        return distanciaDif;
    }
    public void setDistanciaDif(float distanciaDif) {
        this.distanciaDif = distanciaDif;
    }
    public float getAceleracion() {
        return aceleracion;
    }
    public void setAceleracion(float aceleracion) {
        this.aceleracion = aceleracion;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public float getTiempoDif() {
        return tiempoDif;
    }
    public void setTiempoDif(float tiempoDif) {
        this.tiempoDif = tiempoDif;
    }
    public void setLocation(Location location) {
        this.location = location;
    }
    public Location getLocation() {
        return location;
    }
}

