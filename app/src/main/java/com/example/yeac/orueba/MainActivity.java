package com.example.yeac.orueba;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.yeac.orueba.Huella.HuellaActivity;
import com.example.yeac.orueba.Locgps.LocgpsActivity;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_CODE = 10;



    public static String[] listPermisos = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_WIFI_STATE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //permisos
        if(!hasPermissions(this,listPermisos))
            ActivityCompat.requestPermissions(this,listPermisos,PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Toast.makeText(this,"Permisos Brindados",Toast.LENGTH_SHORT).show();
    }
    public boolean hasPermissions(Context context, String... listperm){
        if(context != null && listperm != null){
            for(String permiso: listperm){
                if(ActivityCompat.checkSelfPermission(context,permiso) != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }



    public void comenzarHuellaActivity(View view) {
        startActivity(new Intent(this,HuellaActivity.class));
    }
    public void startLocgpsActivity(View view){
        startActivity( new Intent(this, LocgpsActivity.class));
    }

}

