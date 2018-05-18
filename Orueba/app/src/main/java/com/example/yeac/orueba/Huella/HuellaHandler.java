package com.example.yeac.orueba.Huella;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class HuellaHandler extends FingerprintManager.AuthenticationCallback {

    CancellationSignal mCancellationSignal;
    Context mContext;

    public HuellaHandler(Context pcontext){
        this.mContext = pcontext;
    }

    void startAuth(FingerprintManager pFingerMan, FingerprintManager.CryptoObject pCrypto){
        mCancellationSignal = new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(mContext,"No hay permisos",Toast.LENGTH_SHORT).show();
            return;
        }
        pFingerMan.authenticate(pCrypto,mCancellationSignal,0,this,null);
    }

    void onAuthError(int mID, CharSequence errString){
        Toast.makeText(mContext,"Error Auth: " + mID, Toast.LENGTH_SHORT).show();
    }

    void onAuthFailed(int mID, CharSequence errString){
        Toast.makeText(mContext,"Fail Auth: "+ mID, Toast.LENGTH_SHORT).show();
    }

    void onAuthSuccess(){
        Toast.makeText(mContext,"Success Auth",Toast.LENGTH_SHORT).show();
    }




}
