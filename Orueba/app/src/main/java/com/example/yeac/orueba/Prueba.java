package com.example.yeac.orueba;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by miguel.yengle on 5/11/2018.
 */

public class Prueba {

    // Location: https://developer.android.com/reference/android/location/Location
    void fCalcularDistancia() {
        //Prueba:  https://www.tutiempo.net/calcular-distancias.html

        Location mLocationA, mLocationB;
        float [] numfloat;

        //a -12.103167, -77.058154
        //b -12.118645, -77.043139
        mLocationA = new Location("posA");
        mLocationB = new Location("posB");

        numfloat = new float[2];

        mLocationA.setLatitude(-12.103167);
        mLocationA.setLongitude(-77.058154);
        mLocationB.setLatitude(-12.118645);
        mLocationB.setLongitude(-77.043139);

        Location.distanceBetween(mLocationA.getLatitude(), mLocationA.getLongitude(),
                mLocationB.getLatitude(), mLocationB.getLongitude(), numfloat);

        Log.i("resultado", String.valueOf(numfloat[0]));
        //app resultado: 2367.1316 m
        //site resultado: 2.37 km
    }

    static void pushNotificationBuilder(Context app) {

        long[] pattern = {100, 500};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Intent intentRefresh = new Intent("ActionRefresh");

        PendingIntent pendingIntent = PendingIntent.getActivity(app.getApplicationContext(), 0, intentRefresh,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(app,"canal2")
                        .setContentTitle("Titulo")
                        .setContentText("Texto")
                        .setContentInfo("informacion")
                        .setAutoCancel(true)
                        .setVibrate(pattern)
                        .setLights(Color.BLUE, 1, 1)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notification.setColor(app.getColor(R.color.colorPrimary));
        }
        notification.setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager notificationManager = (NotificationManager) app.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(123, notification.build());

//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(app.getApplicationContext())
//                        .setContentTitle("Titulo")
//                        .setContentText("Contexto")
//                        .setAutoCancel(true)
//                        .setVibrate(pattern)
//                        .setLights(Color.BLUE, 1, 1)
//                        .setSound(defaultSoundUri)
//                        .setContentIntent(pendingIntent);
//        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            notification.setColor(app.getColor(R.color.colorPrimary));
//        }
//        NotificationManager notificationManager = (NotificationManager) app.getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.cancel(123);
//        notificationManager.notify(123, notificationBuilder.build());
    }

    //Ejemplo: http://maps.google.com/maps/api/directions/xml?origin=-12.103167,-77.058154&destination=-12.118645,-77.043139&sensor=false&units=metric

    //Source: https://stackoverflow.com/questions/22609087/how-to-find-distance-by-road-between-2-geo-points-in-android-application-witho
//    public float getDistance(double lat1, double lon1, double lat2, double lon2) {
//        String result_in_kms = "";
//        String url = "http://maps.google.com/maps/api/directions/xml?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric";
//        String tag[] = {"text"};
//        HttpResponseCache response = null;
//        try {
//            HttpClient httpClient = new DefaultHttpClient();
//            HttpContext localContext = new BasicHttpContext();
//            HttpPost httpPost = new HttpPost(url);
//            response = httpClient.execute(httpPost, localContext);
//            InputStream is = response.getEntity().getContent();
//            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
//            Document doc = builder.parse(is);
//            if (doc != null) {
//                NodeList nl;
//                ArrayList args = new ArrayList();
//                for (String s : tag) {
//                    nl = doc.getElementsByTagName(s);
//                    if (nl.getLength() > 0) {
//                        Node node = nl.item(nl.getLength() - 1);
//                        args.add(node.getTextContent());
//                    } else {
//                        args.add(" - ");
//                    }
//                }
//                result_in_kms =String.valueOf( args.get(0));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Float f=Float.valueOf(result_in_kms);
//        return f*1000;
//    }

}

