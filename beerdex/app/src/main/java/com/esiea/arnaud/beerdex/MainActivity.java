package com.esiea.arnaud.beerdex;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.esiea.arnaud.service.GetBeerServices;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static final String BIERS_SERVICE = "com.esiea.arnaud.beerdex.GETBEERSPAGE";
    private static final int SPEED = 20;

    //View
    private ImageView beerdexImg;

    //Accelerometer
    private SensorManager sensorManager;
    private Sensor accelerometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        //Find View
        beerdexImg = (ImageView)findViewById(R.id.beerdex_img);
        //Broadcasting
        IntentFilter intentFilter = new IntentFilter(BIERS_SERVICE);
        LocalBroadcastManager.getInstance(this).registerReceiver(new GetBeerInfo(), intentFilter);
        //SensorInit
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.toaster:
                toastMe("You've been toasted!");
                return true;
            case R.id.menu_param:
                startActivity(new Intent(this, Param.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    //Handle the sensor
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Sensor mSensor = sensorEvent.sensor;
        //If it's accelerometer
        if(mSensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //Change position of the title
            if(sensorEvent.values[0]>0.5 && beerdexImg.getX()>-30){
                beerdexImg.setX(beerdexImg.getX()-SPEED);
            }
            if(sensorEvent.values[0]<-0.5 && beerdexImg.getX()<270){
                beerdexImg.setX(beerdexImg.getX()+SPEED);
            }
            if(sensorEvent.values[1]>0.5 && beerdexImg.getY()<650){
                beerdexImg.setY(beerdexImg.getY()+SPEED);
            }
            if(sensorEvent.values[1]<-0.5 && beerdexImg.getY()>0){
                beerdexImg.setY(beerdexImg.getY()-SPEED);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void scanClick(View v){
        IntentIntegrator scanIntegrator = new IntentIntegrator(this);
        scanIntegrator.initiateScan();
    }

    public void recyclerClick(View v){
        Intent intent = new Intent(this, Beerdex.class);
        startActivity(intent);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if(scanningResult != null){
            GetBeerServices.startActionBeers(this, "http://world.openfoodfacts.org/api/v0/product/"+scanningResult.getContents()+".json");
        }
    }

    //After the scan, toast the result
    public void afterScan(File file) {
        try {
            InputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            //Missing a "[]" to be a JSONArray, so we add them
            JSONArray jsonArray = new JSONArray("[" + new String(buffer, "UTF-8") + "]");
            if(isProductFound(jsonArray)) {
                if(isBeer(jsonArray)){
                    toastMe(
                                    jsonArray.getJSONObject(0).getJSONObject("product").getString("product_name") +
                                    jsonArray.getJSONObject(0).getJSONObject("product").getString("brands")
                    );
                } else {
                    toastMe("Dude... that's not even a beer!");
                }
            } else {
                toastMe("not found");
            }
            file.delete();
        } catch (IOException e) {e.printStackTrace();}
        catch (JSONException e) {e.printStackTrace();}
    }

    //Toast a string
    private void toastMe(String text){
        Toast myToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        myToast.show();
    }

    //Is the product found?
    private boolean isProductFound(JSONArray jsonArray){
        try {
            if(jsonArray.getJSONObject(0).getString("status_verbose").equals("product found")) {
                return true;
            }
        } catch (JSONException e) {e.printStackTrace();}
        return false;
    }

    //Is it a beer?
    private boolean isBeer(JSONArray jsonArray){
        try {
            if (jsonArray.getJSONObject(0).getJSONObject("product").getString("categories").contains("beer")){
                return true;
            }
        } catch (JSONException e) {e.printStackTrace();}
        return false;
    }

    //Broadcast Receiver
    public class GetBeerInfo extends BroadcastReceiver {
        public GetBeerInfo() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            //On receive get the file and pass it to afterScan
            File file = (File) intent.getExtras().get("FILE_DOWNLOADED");
            String logMessage;

            if (file != null) {
                logMessage = "ile successfully passed to the Second intent";
                afterScan(file);
            } else {
                logMessage = "File not found...";
            }

            Log.d("[BIERS-SERVICES]", logMessage);
        }
    }
}
