package com.yeac.example.otraprueba;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class ServiceLocation extends Service {


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
