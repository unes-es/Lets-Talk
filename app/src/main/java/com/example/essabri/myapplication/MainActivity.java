package com.example.essabri.myapplication;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

    public static int c = 0;
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txt =((TextView)findViewById(R.id.textView));
        txt.setText(c+"");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                ((ToggleButton)findViewById(R.id.toggleButton3)).toggle();
            }
        });

        ((ToggleButton)findViewById(R.id.toggleButton3)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Snackbar.make(buttonView, "isChecked :"+isChecked, Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c++;
                txt.setText(c+"");
            }
        });

        Log.d("mtag","onCreate");

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("mtag","onResume");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("mtag","onRestart");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("mtag","onStart");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("mtag","onStop");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("mtag","onDestroy");

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("mtag","onPause");

    }


    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        Log.d("mtag","onSaveInstanceState");


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("mtag",c+"");
        outState.putInt("c",c);
        Log.d("mtag",outState.getInt("c")+"");
        Log.d("mtag","onSaveInstanceState");
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("mtag",savedInstanceState.getInt("c")+"");
        txt.setText(savedInstanceState.getInt("c")+"");
        Log.d("mtag","onRestoreInstanceState");
    }
}
