package com.example.essabri.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

/**
 * Created by essabri on 11/12/2017.
 */

public class PhrasesListAdapter extends BaseExpandableListAdapter {

    private Context context;

    ArrayList<Phrase> phrases = new ArrayList<>();
    private ArrayList<Phrase> backup = new ArrayList<>();
    ToggleButton t;

    PhrasesListAdapter(Context context,ArrayList<Phrase> _phrases){
        this.context = context;
        phrases = _phrases;
        backup.addAll(phrases);
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.phrase_origin_list_item,parent,false);
        }
        ((TextView)convertView.findViewById(R.id.originTextView)).setText(Util.toUpperCaseSentence(phrases.get(groupPosition).origin));

        ToggleButton isFavoriteToggleButton = (ToggleButton)convertView.findViewById(R.id.isFavoriteToggleButton);
        isFavoriteToggleButton.setChecked(phrases.get(groupPosition).isFavorite);
        isFavoriteToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phrases.get(groupPosition).toggleIsFavorite();
                phrases.get(groupPosition).save();
                if (((Phrases)context).getIntent().hasExtra("categoryId_")){
                    notifyDataSetChanged();
                }
                else {
                    backup.removeAll(phrases);
                    phrases = Data.Manager.getFavorites();
                    backup.addAll(phrases);
                    ((Phrases)context).onDataChanged(R.drawable.ic_favorite_black_24dp,"You have no favorite phrases yet!");
                    ((Phrases)context).onCreateOptionsMenu(((Phrases)context).mainMenu);
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Phrase phrase = phrases.get(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.phrase_target_list_item,parent,false);
        }
        ((TextView)convertView.findViewById(R.id.targetTextView)).setText(Util.toUpperCaseSentence(phrase.target));
        ((TextView)convertView.findViewById(R.id.pronunciationTextView)).setText(Util.toUpperCaseSentence(phrase.pronunciation));
        final ImageButton copyBtn = (ImageButton) convertView.findViewById(R.id.copyBtn);

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Phrases)context).onCopyToClipboardClicked(phrase);
            }
        });

        final ImageButton shareBtn = (ImageButton) convertView.findViewById(R.id.shareBtn);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Phrases)context).onShareBtnClicked(phrase);
            }
        });

        final ToggleButton playPauseBtn = (ToggleButton)convertView.findViewById(R.id.playPauseBtn);

        ((Phrases)context).ttsController.ttsPlayPause = playPauseBtn;

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Phrases)context).ttsController.tts.isSpeaking()){
                    ((Phrases)context).ttsController.tts.stop();
                }
                else{
                    ((Phrases) context).ttsController.speak(phrase.target);
                }
            }
        });
        return convertView;
    }

    public void filter(String text){
        phrases.clear();
        if(!text.isEmpty()) {
            for (Phrase phrase : backup) {
                if (phrase.origin.toLowerCase().contains(text.toLowerCase())
                       || phrase.target.toLowerCase().contains(text.toLowerCase())
                        || phrase.pronunciation.toLowerCase().contains(text.toLowerCase())) {
                    phrases.add(phrase);
                }
            }
        }
        else {
            phrases.addAll(backup);
        }
        notifyDataSetChanged();
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public int getGroupCount() {
        return phrases.size();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return phrases.get(groupPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }



    @Override
    public long getGroupId(int groupPosition) {
        return phrases.get(groupPosition).id;
    }



    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }
}
