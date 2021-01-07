package kawa.ttehaute;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by NextU on 11/02/2018.
 */

public class GpxLocationManager implements LocationListener {

    private Gpx gpx;
    private TextView high_counter;
    private TextView low_counter;
    private int count = 0;
    private boolean haveBeenOnceEnabled = false ;
    private boolean checkpoint = false ;

    public GpxLocationManager(Gpx gpx,TextView high_counter, TextView low_counter){
        this.gpx = gpx ;
        this.high_counter = high_counter ;
        this.low_counter = low_counter ;
    }

    @Override
    public void onLocationChanged(Location location){
        float speed = location.getSpeed() ;
        low_counter.setText(String.valueOf(speed));
        int kmh = (int) (speed*3600)/1000;
        high_counter.setText(String.valueOf(kmh));
        //if((count%1)==0) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();
            long time = location.getTime();
            if(this.gpx!=null){
                this.gpx.recordTrkPoint(latitude,longitude,altitude,time);
                if(this.checkpoint){
                    this.gpx.recordWayPoint(latitude,longitude,altitude,time);
                    this.checkpoint = false ;
                }
            }else{
                low_counter.setText("null");
            }
        //}
        //count=count+1;
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s){
        if(!this.haveBeenOnceEnabled){
            low_counter.setText("Enabled");
            high_counter.setText("Enabled");
            this.gpx.startGpxFile();
            this.haveBeenOnceEnabled = true;
        }
    }

    @Override
    public void onProviderDisabled(String s){
        low_counter.setText("Disabled");
        high_counter.setText("Disabled");
        if(this.haveBeenOnceEnabled) {
            this.gpx.stopGpxFile();
            this.haveBeenOnceEnabled=false;
        }
    }

    public void rememberCloseGpx(){
        if(this.haveBeenOnceEnabled){
            this.haveBeenOnceEnabled = false;
            this.gpx.stopGpxFile();
        }
    }

    public void setCheckpoint(){
        this.checkpoint = true ;
    }


}
