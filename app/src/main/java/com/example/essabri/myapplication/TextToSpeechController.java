package com.example.essabri.myapplication;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Locale;

/**
 * Created by essabri on 16/04/2018.
 */

public class TextToSpeechController extends UtteranceProgressListener implements TextToSpeech.OnInitListener {

    TextToSpeech tts;
    private Context context;
    ToggleButton ttsPlayPause;

    public TextToSpeechController(Context context){
        this.context = context;
        tts = new TextToSpeech(context,this);
    }


    @Override
    public void onInit(int status) {
        tts.setLanguage(Locale.KOREAN);
        tts.setSpeechRate(1);
        tts.setOnUtteranceProgressListener(this);
    }

    public void speak(String text){
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, text);
    }

    @Override
    public void onStart(String utteranceId) {
        ((Phrases)context).runOnUiThread(new Runnable() {
            public void run()
            {
                ttsPlayPause.setChecked(true);
            }
        });
    }


    @Override
    public void onStop(String utteranceId, boolean interrupted) {
        super.onStop(utteranceId, interrupted);
        ((Phrases)context).runOnUiThread(new Runnable() {
            public void run()
            {
                ttsPlayPause.setChecked(false);
            }
        });
    }

    @Override
    public void onDone(String utteranceId) {
        ((Phrases)context).runOnUiThread(new Runnable() {
            public void run()
            {
                ttsPlayPause.setChecked(false);
            }
        });
    }

    @Override
    public void onError(String utteranceId) {
        ((Phrases)context).runOnUiThread(new Runnable() {
            public void run()
            {
                Toast.makeText(context,"Unable to speak!",Toast.LENGTH_LONG).show();
                ttsPlayPause.setChecked(false);
            }
        });
    }
}


