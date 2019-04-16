package com.applications.coffee.letstalkrussian_free;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class Phrases extends AppCompatActivity {

    ExpandableListView phrasesListView;
    PhrasesListAdapter adapter;
    boolean isFavoriteView = false;
    ConstraintLayout noDataView;
    TextView noDataMsg;
    ImageView noDataImg;
    Menu mainMenu;
    SharedPreferences sharedPref;
    Context context;
    TextToSpeechController ttsController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phrases);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        ttsController = new TextToSpeechController(this);


        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(getDrawable(R.drawable.ic_arrow_back_white_24dp));
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        context = this;

        AdView mAdView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        if (!BuildConfig.DEBUG) {
            mAdView.loadAd(adRequest);
        }

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
            onDataChanged(R.drawable.ic_favorite_black_24dp,getResources().getString(R.string.no_favorite_msg));
            setTitle(R.string.favorites);
            isFavoriteView = true;
        }
        phrasesListView.setAdapter(adapter);


        phrasesListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int groupPosition)
            {
                if (ttsController.tts.isSpeaking()){
                    ttsController.tts.stop();
                }
                if (sharedPref.getBoolean("pref_auto_play",true)) {
                    //ttsController.tts.setSpeechRate(sharedPref.getInt("pref_auto_play",1));
                    ttsController.speak(adapter.phrases.get(groupPosition).target);
                }
                if(groupPosition != previousGroup)
                    phrasesListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

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
        if (ttsController.tts.isSpeaking()){
            ttsController.tts.stop();
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        View list = inflater.inflate(R.layout.phrase_to_copy_list,null);
        String[] values = new String[] {
                phrase.origin,
                phrase.target,
                phrase.pronunciation
        };

        SimpleListAdapter adapter = new SimpleListAdapter(this,values);
        final ListView listView = (ListView)list.findViewById(R.id.listCopy);
        listView.setAdapter(adapter);
        final PopupWindow listWindow = new PopupWindow(list, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copy", (String) listView.getItemAtPosition(position));
                clipboard.setPrimaryClip(clip);
                listWindow.dismiss();
                Toast.makeText(Phrases.this, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
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
        final String pn = getPackageName();
        if (ttsController.tts.isSpeaking()){
            ttsController.tts.stop();
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        //TODO: translate hard coded strings
        sendIntent.putExtra(Intent.EXTRA_TEXT, phrase.origin+": "+phrase.target + "\nYou read it : "+phrase.pronunciation+"\n Find more in our app:http://play.google.com/store/apps/details?id=" + pn);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ttsController.tts.isSpeaking()){
            ttsController.tts.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (ttsController.tts.isSpeaking()){
            ttsController.tts.stop();
        }
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
                    onDataChanged(R.drawable.ic_search_blue_24dp,getResources().getString(R.string.no_result_msg)+"\"" + newText + "\"");
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
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
}
