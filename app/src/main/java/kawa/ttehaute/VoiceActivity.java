package kawa.ttehaute;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created by NextU on 12/02/2018.
 */

public class VoiceActivity extends Activity {

    @Override
    protected void onResume(){
        super.onResume();
        Log.i("voice","resumed");
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcessList = am.getRunningAppProcesses();

        for(int i=0; i<runningProcessList.size();i++){
            Log.i("echantillonnage",runningProcessList.get(i).processName);
        }
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}
