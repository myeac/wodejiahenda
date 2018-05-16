package com.example.yeac.orueba.Huella;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yeac.orueba.R;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class HuellaActivity extends AppCompatActivity {

    // https://www.androidauthority.com/how-to-add-fingerprint-authentication-to-your-android-app-747304/

    private static final String KEY = "millave";
    Cipher mCipher;
    KeyStore mKeystore;
    KeyGenerator mKeygenerator;
    TextView mTextview;
    FingerprintManager.CryptoObject mCryptoObject;
    FingerprintManager mFingerPrintManager;
    KeyguardManager mKeyguardManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huella);
        try {
            initFingerScarn();
        } catch (FingerprintException e) {
            e.printStackTrace();
        }
    }

    public void initFingerScarn() throws FingerprintException {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mKeyguardManger = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
            mFingerPrintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
            mTextview = (TextView) findViewById(R.id.textviewMensaje);

            if(!mFingerPrintManager.isHardwareDetected())
                mTextview.setText("No se detecto hardware");
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
                mTextview.setText("Da los permisos a la app");
            if(!mFingerPrintManager.hasEnrolledFingerprints())
                mTextview.setText("No se configuro huella");
            if(!mKeyguardManger.isKeyguardSecure())
                mTextview.setText("Habilitar lockscreen security");
        }else{
            generateKey();
        }
        if(initCypher()){
            mCryptoObject = new FingerprintManager.CryptoObject(mCipher);
            HuellaHandler helper = new HuellaHandler(this);
            helper.startAuth(mFingerPrintManager, mCryptoObject);
        }
    }
    public void generateKey() throws FingerprintException{
        try{
            mKeystore = KeyStore.getInstance("AndroidKeyStore");
            mKeygenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            mKeystore.load(null);
            mKeygenerator.init(new KeyGenParameterSpec.Builder(KEY,KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            mKeygenerator.generateKey();
        }catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException
                | InvalidAlgorithmParameterException | CertificateException | IOException exc){
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    public boolean initCypher(){
        try{
            mCipher = Cipher.getInstance(
                              KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }catch (NoSuchAlgorithmException | NoSuchPaddingException e){
            throw new RuntimeException("Fallo en obtener Cipher", e);
        }
        try{
            mKeystore.load(null);
            SecretKey key = (SecretKey) mKeystore.getKey(KEY,null);
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        }catch (KeyPermanentlyInvalidatedException e){
            return false;
        }catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e){
            throw new RuntimeException("Fallo inicio de Cipher", e);
        }
    }
    private class FingerprintException extends Exception{
        public FingerprintException(Exception e){
            super(e);
        }
    }
}

