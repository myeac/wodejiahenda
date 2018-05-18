package com.example.yeac.orueba;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Util {

    public static final int LOCATION_PERMISSIONS_CODE = 10;
    Context mContext;

    public static String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WAKE_LOCK};

    public static boolean fCheckPermissions(Context pcontext){
        int result;
        List<String> listPermisos = new ArrayList<>();
        for(String p:permissions){
            result = ContextCompat.checkSelfPermission(pcontext.getApplicationContext(),p);
            if(result != PackageManager.PERMISSION_GRANTED){
                listPermisos.add(p);
            }
        }
        if(!listPermisos.isEmpty()){
            ActivityCompat.requestPermissions((Activity) pcontext,
                    listPermisos.toArray(new String[listPermisos.size()]),
                    LOCATION_PERMISSIONS_CODE);
            return false;
        }
        return true;
    }
}
