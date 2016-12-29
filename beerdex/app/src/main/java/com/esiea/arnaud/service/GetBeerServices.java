package com.esiea.arnaud.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.esiea.arnaud.beerdex.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by alabyre on 16/12/2016.
 */

public class GetBeerServices extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String GETBEERINFO = "com.esiea.arnaud.beerdex.request";

    private static String address;

    public GetBeerServices(){
        super("GetBeerServices");
    }

    public static void startActionBeers(Context context, String link)
    {
        address = link;
        Intent intent = new Intent(context, GetBeerServices.class);
        intent.setAction(GETBEERINFO);
        context.startService(intent);
    }

    protected void onHandleIntent(Intent intent)
    {
        if(intent != null){
            final String action = intent.getAction();
            if(GETBEERINFO.equals(action)){
                handleActionBeers();
            }
        }
    }

    private void handleActionBeers(){
        Log.d("[BIERS-SERVICES]", "Theard service name: "+ Thread.currentThread().getName());
        Intent beersIntent = new Intent(MainActivity.BIERS_SERVICE);
        URL url = null;
        try{
            url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try{
                conn.connect();
            } catch (UnknownHostException e){
                Log.d("[BIERS-SERVICES]","No internet services");
            }
            if(HttpURLConnection.HTTP_OK == conn.getResponseCode()){
                File file = new File(getCacheDir(), "bieres.txt");
                copyInputStreamToFile(conn.getInputStream(), file);
                Log.d("[BIERS-SERVICES]","Beer page has been download.");
                beersIntent.putExtra("FILE_DOWNLOADED", file);
                LocalBroadcastManager.getInstance(this).sendBroadcast(beersIntent);
            }
        }
        catch (MalformedURLException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}
    }

    private void copyInputStreamToFile(InputStream in, File file){
        try{
            OutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while((len=in.read(buffer))>0)
            {
                out.write(buffer, 0, len);
            }
            out.close();
            in.close();
        } catch(Exception e){e.printStackTrace();}
    }


}