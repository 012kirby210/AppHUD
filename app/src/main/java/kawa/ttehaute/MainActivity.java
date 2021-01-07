package kawa.ttehaute;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import kawa.ttehaute.Graph.AutomobileGraph;
import kawa.ttehaute.Graph.Graph;
import kawa.ttehaute.XMLParser.XMLDataParser;


public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    static final int TETE_HAUTE_PERMISSION_GPS = 1 ;
    static final int TETE_HAUTE_RECORD_AUDIO = 2 ;
    public static final String ACTIVATE_RECOGNITION = "active reconnaissance vocale";
    public static SpeechRecognizer recognizer ;
    LocationManager locationManager;
    GpxLocationManager gpxLocationManager;
    TextView high_counter;
    TextView low_counter;
    ImageButton add_waypoint_button ;
    boolean isMetric, externalStorageAvailable;
    File fileToWriteInto;
    FileOutputStream outputStream;
    int count;
    Gpx gpx ;
    public static final int VOICE_RECOGNITION_TETE_HAUTE = 1001;
    MediaPlayer checkpointMediaPlayer;
    MediaPlayer networkErrorMediaPlayer;
    MediaPlayer timeoutErrorMediaPlayer;
    VocalCommand vocalCommand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Main","does it restart ?");
        setContentView(R.layout.activity_main);
        count = 0;
        PreferenceManager.setDefaultValues(this,R.xml.preferences, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String high_counter_font_path = preferences.getString(getResources().getString(R.string.pref_key_high_counter_font),"font/Orbitron.ttf");
        String low_counter_font_path = preferences.getString(getResources().getString(R.string.pref_key_low_counter_font),"font/Orbitron.ttf");
        String high_counter_color_string = preferences.getString(getResources().getString(R.string.pref_key_high_counter_color),"#FF99CC00");
        String low_counter_color_string = preferences.getString(getResources().getString(R.string.pref_key_low_counter_color),"#FF00DFF");

        boolean isMetric = Integer.parseInt(preferences.getString(
                getResources().getString(R.string.pref_key_high_counter_unit),"1")) == 1?true:false;

        high_counter = (TextView) this.findViewById(R.id.afficheur_km);
        low_counter = (TextView) this.findViewById(R.id.afficheur_ms);
        add_waypoint_button = (ImageButton) this.findViewById((R.id.add_waypoint_button));
        checkpointMediaPlayer = MediaPlayer.create(this,R.raw.checkpoint);
        networkErrorMediaPlayer = MediaPlayer.create(this,R.raw.tete_haute_no_network);
        timeoutErrorMediaPlayer = MediaPlayer.create(this,R.raw.tete_haute_timeup);

        setFont(high_counter,high_counter_font_path);
        setFont(low_counter,low_counter_font_path);
        high_counter.setTextColor(Color.parseColor(high_counter_color_string));
        low_counter.setTextColor(Color.parseColor(low_counter_color_string));

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        high_counter.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //display settings activity
                Intent settings_display_intent = new Intent(v.getContext(),SettingsActivity.class);
                startActivity(settings_display_intent);
            }
        });

        low_counter.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //display settings activity
                Intent settings_display_intent = new Intent(v.getContext(),SettingsActivity.class);
                startActivity(settings_display_intent);
            }
        });

        this.gpx = new Gpx(getResources().getString(R.string.trackfilename),getResources().getString(R.string.waypointsfilename),this);

        this.gpxLocationManager = new GpxLocationManager(this.gpx,this.high_counter,this.low_counter);

        vocalCommand = new VocalCommand(this.gpxLocationManager,this);
        recognizer = SpeechRecognizer.createSpeechRecognizer(this);
        recognizer.setRecognitionListener(vocalCommand);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){

            }else{
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},MainActivity.TETE_HAUTE_PERMISSION_GPS);
            }
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECORD_AUDIO)){

            }else{
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.RECORD_AUDIO},MainActivity.TETE_HAUTE_RECORD_AUDIO);
            }
        }

        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

        SensorManager sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> listSensor = sManager.getSensorList(Sensor.TYPE_ALL);
        for(int i=0; i<listSensor.size(); i++){
            Log.i("sensor",listSensor.get(i).getName()+","+listSensor.get(i).getVendor());
        }

        /*XMLDataParser dataParser = new XMLDataParser("sermaize-les-bains.osm",this);
        dataParser.parse();
        Log.i("XMLParser",dataParser.toString());
        dataParser.test();
        AutomobileGraph graph = new AutomobileGraph();
        graph.populateGraph(dataParser.listeDeWays);
        Log.i("graph",graph.toString());
        graph.test();*/
    }

    @Override
    protected void onStart(){
        super.onStart();
        checkLocationManagerPermission();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Log.i("onNewIntent","is called");
        //this.speak(this.add_waypoint_button);
        this.recognizer.startListening(new Intent(RecognizerIntent.ACTION_VOICE_SEARCH_HANDS_FREE));
    }

    @Override
    protected void onDestroy(){
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        this.gpxLocationManager.rememberCloseGpx();
        super.onDestroy();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus){
            this.findViewById(R.id.afficheur_ms).getRootView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY );
        }
    }

    private void checkLocationManagerPermission(){
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,250,0,this.gpxLocationManager);
        }
    }

    // permission callback :
    public void onRequestPermissionResult(int requestCode,String permissions[],int[] grantResults){
        switch(requestCode){
            case MainActivity.TETE_HAUTE_PERMISSION_GPS:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    checkLocationManagerPermission();
                }else{
                    locationManager=null;
                }
                return;
        }
    }

    // hide and show UI :
    private void hideSystemUI(){
        this.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

    }

    private void showSystemUI(){
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void setFont(TextView counter, String font_path){
        if(counter!=null){
            try {
                Typeface counter_typeface = Typeface.createFromAsset(getAssets(), font_path);
                counter.setTypeface(counter_typeface);
            }catch(RuntimeException e){
                // assets font is not found
                counter.setTypeface(Typeface.DEFAULT);
            }
        }
    }

    private void setColor(TextView counter,String color_string){
        if(counter!=null){
            try{
                counter.setTextColor(Color.parseColor(color_string));
            }catch(RuntimeException e){
                // color resolution trouble
                counter.setTextColor(Color.parseColor("#FFFFFF"));
            }
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }


    // on Preferences changes :
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,String key){
        if(key.equals(getResources().getString(R.string.pref_key_high_counter_color))){
            String high_counter_color_string = sharedPreferences.getString(key,getResources().getString(R.string.pref_dialog_default_value_high_counter_color));
            setColor(high_counter,high_counter_color_string);
        }else if(key.equals(getResources().getString(R.string.pref_key_high_counter_font))){
            String high_counter_font_path = sharedPreferences.getString(key,getResources().getString(R.string.pref_dialog_default_value_high_counter_font));
            setFont(high_counter,high_counter_font_path);
        }else if(key.equals(getResources().getString(R.string.pref_key_high_counter_unit))){
            if(high_counter!=null){
                try{
                    isMetric = (sharedPreferences.getString(key,"1")).equals("1")? true: false;
                }catch( RuntimeException e){
                    isMetric = true;
                }
            }
        }else if(key.equals(getResources().getString(R.string.pref_key_low_counter_color))){
            String low_counter_color_string = sharedPreferences.getString(key,getResources().getString(R.string.pref_dialog_default_value_low_counter_color));
            setColor(low_counter, low_counter_color_string);
        }else if(key.equals(getResources().getString(R.string.pref_key_low_counter_font))){
            if(low_counter!=null){
                String low_counter_font_path = sharedPreferences.getString(key,getResources().getString(R.string.pref_dialog_default_value_low_counter_font));
                setFont(low_counter,low_counter_font_path);
            }
        }
    }

    // Voice recognition :
    private void checkVoiceRecognition(){
        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0) ;
        if(activities.size()==0){
            // pas de service de reconnaissance :
            this.add_waypoint_button.setEnabled(false);
        }else{
            this.add_waypoint_button.setEnabled(true);
        }
    }

    public void speak(View view){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,1);
        startActivityForResult(intent,this.VOICE_RECOGNITION_TETE_HAUTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if( requestCode == this.VOICE_RECOGNITION_TETE_HAUTE ){
            if( resultCode == RESULT_OK ){
                ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) ;
                for (int i=0; i<matches.size();i++){
                    Log.i("headerd", matches.get(i));
                    if((matches.get(i).equals("checkpoint") || matches.get(i).equals("check point"))&& checkpointMediaPlayer !=null){
                        checkpointMediaPlayer.start();
                        this.gpxLocationManager.setCheckpoint();
                    }
                }
            }
        }
    }

    public void playSuccess(){
        if(checkpointMediaPlayer !=null){
            checkpointMediaPlayer.start();
        }
    }

    public void playNetworkError(){
        if(networkErrorMediaPlayer!=null){
            networkErrorMediaPlayer.start();
        }
    }

    public void playTimeoutError(){
        if(timeoutErrorMediaPlayer!=null){
            timeoutErrorMediaPlayer.start();
        }
    }
}
