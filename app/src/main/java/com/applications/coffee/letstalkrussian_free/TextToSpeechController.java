package com.applications.coffee.letstalkrussian_free;

import android.content.Context;
import android.media.AudioManager;
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
        tts.setLanguage(new Locale("ru","RU"));
        tts.setSpeechRate(1);
        tts.setOnUtteranceProgressListener(this);
    }

    public static void audioVolumeTest(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)<1){
            Toast.makeText(context, "Turn up the volume please", Toast.LENGTH_SHORT).show();
        }
    }

    public void speak(String text){
        audioVolumeTest(context);
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


