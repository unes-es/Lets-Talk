package com.example.essabri.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by essabri on 08/12/2017.
 */

public class CategoriesListAdapter extends BaseAdapter {

    Context context;

    ArrayList<Category> categories = new ArrayList<>();
    ArrayList<Category> backup = new ArrayList<>();

    CategoriesListAdapter(Context context){
        this.context = context;
        categories = Data.Manager.categories;
        backup.addAll(categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.categoy_list_item,parent,false);
        }
        ((TextView)convertView.findViewById(R.id.categoryTextView)).setText(Util.toUpperCaseSentence(categories.get(position).name));
        ((TextView)convertView.findViewById(R.id.translatedName)).setText(Util.toUpperCaseSentence(categories.get(position).translatedName));

        return convertView;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public long getItemId(int position) {
        return categories.get(position).id;
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    public void filter(String text){

        categories.clear();
        if(!text.isEmpty()) {
            for (Category category : backup) {
                /*ArrayList<Phrase> phrases = Data.Manager.getPhrasesWithCategory(category.id);
                boolean exist = false;
                for (Phrase phrase:phrases) {
                    if (phrase.origin.contains(text.toLowerCase())
                            || phrase.target.contains(text.toLowerCase())
                            || phrase.pronunciation.contains(text.toLowerCase()) ){
                        exist = true;
                    }
                }
                if (category.name.contains(text.toLowerCase()) || exist) {*/
                if (category.name.toLowerCase().contains(text.toLowerCase()) || category.translatedName.toLowerCase().contains(text.toLowerCase())) {
                    categories.add(category);
                }
            }
        }
        else {
            categories.addAll(backup);
        }
        notifyDataSetChanged();

    }
}
