package com.example.essabri.myapplication;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.annotation.BoolRes;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Locale;

public class Phrases extends AppCompatActivity {

    ExpandableListView phrasesListView;
    PhrasesListAdapter adapter;
    boolean isFavoriteView = false;
    ConstraintLayout noDataView;
    TextView noDataMsg;
    ImageView noDataImg;
    int test;
    Menu mainMenu;
    SharedPreferences sharedPref;
    View targetPhraseView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        noDataView = (ConstraintLayout)findViewById(R.id.emptyListMessage);
        noDataMsg = (TextView) findViewById(R.id.emptyMsg);
        noDataImg = (ImageView) findViewById(R.id.emptyListImg);




        phrasesListView = (ExpandableListView)findViewById(R.id.phrasesListView);
        if (getIntent().hasExtra("categoryId_")) {
            final Category category = Data.Manager.getCategoryById((int) getIntent().getLongExtra("categoryId_", 0));
            setTitle(category != null? Util.toUpperCaseSentence(category.name):"");
            adapter = new PhrasesListAdapter(this, Data.Manager.getPhrasesWithCategory(category != null? category.id:0));
        }
        else{
            adapter = new PhrasesListAdapter(this, Data.Manager.getFavorites());
            onDataChanged(R.drawable.ic_favorite_black_24dp,"You have no favorite phrases yet!");
            setTitle(R.string.favorites);
            isFavoriteView = true;
        }



        phrasesListView.setAdapter(adapter);
        phrasesListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition)
            {
                Log.d("mtag","Expanded");
                test = groupPosition;
                /*if (sharedPref.getBoolean("pref_auto_play",true)) {
                    //Util.tts.setSpeechRate(sharedPref.getInt("pref_auto_play",1));
                    Util.tts.setSpeechRate(1);
                    Util.tts.speak(adapter.phrases.get(groupPosition).target,
                            TextToSpeech.QUEUE_FLUSH,
                            null,
                            adapter.phrases.get(groupPosition).target);
                }*/
                if(groupPosition != previousGroup)
                    phrasesListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        phrasesListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                if(Util.tts.isSpeaking()){
                    Util.tts.stop();
                }
            }
        });
    }




    public void onListViewExpand(View v,int pos){
        /*if (test == pos)
        targetPhraseView = v;
        Log.d("mtag","onListViewExpand");

        Util.tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.d("mtag","start"+" "+utteranceId);
                //((ToggleButton)phrasesListView.findViewWithTag(test)).setChecked(true);
                ((ToggleButton)targetPhraseView.findViewById(R.id.playPauseBtn)).setChecked(true);
            }
            @Override
            public void onDone(String utteranceId) {
                Log.d("mtag","done"+" "+utteranceId);
                //((ToggleButton)phrasesListView.findViewWithTag(test)).setChecked(false);
                ((ToggleButton)targetPhraseView.findViewById(R.id.playPauseBtn)).setChecked(false);
            }
            @Override
            public void onError(String utteranceId) {
                Log.d("mtag","error"+" "+utteranceId);
                //((ToggleButton)phrasesListView.findViewWithTag(test)).setChecked(false);
                ((ToggleButton)targetPhraseView.findViewById(R.id.playPauseBtn)).setChecked(false);
            }
            @Override
            public void onStop(String utteranceId, boolean interrupted) {
                super.onStop(utteranceId, interrupted);
                Log.d("mtag","stop"+" "+utteranceId+" "+interrupted);
                //((ToggleButton)phrasesListView.findViewWithTag(test)).setChecked(false);
                ((ToggleButton)targetPhraseView.findViewById(R.id.playPauseBtn)).setChecked(false);
            }
        });

        if (sharedPref.getBoolean("pref_auto_play",true)) {
            //Util.tts.setSpeechRate(sharedPref.getInt("pref_auto_play",1));
            Util.tts.setSpeechRate(1);
            Util.tts.speak(adapter.phrases.get(pos).target,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    adapter.phrases.get(pos).target);
        }*/
    }

    void onDataChanged(int imgDrawable,String msg){
        if (adapter.phrases.size() == 0) {
            phrasesListView.setVisibility(View.INVISIBLE);
            noDataView.setVisibility(View.VISIBLE);
            noDataImg.setImageDrawable(getDrawable(imgDrawable));
            noDataMsg.setText(msg);
            //onCreateOptionsMenu(mainMenu);
        }
        else {
            phrasesListView.setVisibility(View.VISIBLE);
            noDataView.setVisibility(View.INVISIBLE);
        }
    }


    void onCopyToClipboardClicked(Phrase phrase){
        LayoutInflater inflater = LayoutInflater.from(this);
        View list = inflater.inflate(R.layout.phrase_to_copy_list,null);
        String[] values = new String[] {
                phrase.origin,
                phrase.target,
                phrase.pronunciation
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, android.R.id.text1, values);
        final ListView listView = (ListView)list.findViewById(R.id.listCopy);
        listView.setAdapter(adapter);

        final PopupWindow listWindow = new PopupWindow(list, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copy", (String) listView.getItemAtPosition(position));
                clipboard.setPrimaryClip(clip);
                listWindow.dismiss();
                Toast.makeText(Phrases.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });


        list.findViewById(R.id.dismissBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listWindow.dismiss();
            }
        });
        listWindow.showAtLocation(findViewById(R.id.container), Gravity.CENTER,0,0);
    }

    public void onShareBtnClicked(Phrase phrase){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, phrase.origin+": "+phrase.target);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().hasExtra("categoryId_")) {
            final Category category = Data.Manager.getCategoryById((int) getIntent().getLongExtra("categoryId_", 0));
            setTitle(category != null? Util.toUpperCaseSentence(category.name):"");
            adapter = new PhrasesListAdapter(this, Data.Manager.getPhrasesWithCategory(category != null? category.id:0));
        }
        else{
            adapter = new PhrasesListAdapter(this, Data.Manager.getFavorites());
            onDataChanged(R.drawable.ic_favorite_black_24dp,"You have no favorite phrases yet!");
            setTitle(R.string.favorites);
            isFavoriteView = true;
        }
        adapter.notifyDataSetChanged();
        Log.d("mtag","onResume");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        mainMenu = menu;
        if (isFavoriteView) menu.removeItem(R.id.action_favorite);
        if (adapter.phrases.size() == 0) menu.removeItem(R.id.action_search);
        else {
            MenuItem searchViewItem = menu.findItem(R.id.action_search);
            final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
            searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchViewAndroidActionBar.clearFocus();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    adapter.filter(newText);
                    onDataChanged(R.drawable.ic_search_blue_24dp, "No result for \"" + newText + "\"!!");
                    return false;
                }
            });
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_favorite:
                Intent i = new Intent(this,Phrases.class);
                startActivity(i);
                break;
            case R.id.action_settings:
                Intent s = new Intent(this,SettingsActivity.class);
                startActivity(s);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
