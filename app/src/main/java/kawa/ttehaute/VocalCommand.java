package kawa.ttehaute;

import android.app.Activity;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by NextU on 12/02/2018.
 */

public class VocalCommand implements RecognitionListener {

    GpxLocationManager gpxManager ;
    MainActivity mainActivity ;

    public VocalCommand(GpxLocationManager gpxManager,MainActivity main){
        this.gpxManager = gpxManager ;
        this.mainActivity = main ;
    }

    public void onBeginningOfSpeech(){
        Log.i("voiceListener","onBegin");
    }

    public void onRmsChanged(float rmsdb){

    }

    public void onEndOfSpeech(){
        Log.i("voiceListener","onEnd");
    }

    public void onReadyForSpeech(Bundle params){
        Log.i("voiceListener","onReady");
    }

    public void onResults(Bundle results){
        Log.i("voiceListener","onResult");
        ArrayList<String> stringList = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if(stringList!=null){
            for(int i=0; i<stringList.size(); i++){
                Log.i("result",stringList.get(i));
                if(stringList.get(i).equals("checkpoint") || stringList.get(i).equals("check point")){
                    this.gpxManager.setCheckpoint();
                    if(mainActivity!=null){
                        mainActivity.playSuccess();
                    }
                    return;
                }
            }
        }

    }

    public void onEvent(int eventType, Bundle params){
        Log.i("voiceListener","onevent");
    }

    public void onBufferReceived(byte[] buffer){
        Log.i("voiceListener","onBufferReceived");
    }

    public void onPartialResults(Bundle partialResults){
        Log.i("voiceListener","onPartialResult");
    }

    public void onError(int error){
        Log.i("voiceListener","onError");
        switch(error){
            case SpeechRecognizer.ERROR_AUDIO:
                Log.i("voiceListener","Error audio");
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                Log.i("voiceListener","Error client");
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                Log.i("voiceListener","Error permissions");
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                Log.i("voiceListener","Error network");
                this.mainActivity.playNetworkError();
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                Log.i("voiceListener","Error network timeout");
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                Log.i("voiceListener","Error No match");
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                Log.i("voiceListener","Error Recognizer busy");
                break;
            case SpeechRecognizer.ERROR_SERVER:
                Log.i("voiceListener","Error Server");
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                Log.i("voiceListener","Error Speech timeout");
                this.mainActivity.playTimeoutError();
                break;
            default:
                break;
        }
    }
}
