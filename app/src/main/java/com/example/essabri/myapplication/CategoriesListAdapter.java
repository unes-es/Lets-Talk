package com.example.essabri.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by essabri on 08/12/2017.
 */

public class CategoriesListAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<Category> categories = new ArrayList<>();
    private ArrayList<Category> backup = new ArrayList<>();

    CategoriesListAdapter(Context context){
        this.context = context;
        categories = Data.Manager.categories;
        backup.addAll(categories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Category category = categories.get(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.categoy_list_item,parent,false);
        }


        if(context.getResources().getIdentifier("c"+category.id, "drawable", context.getPackageName())>0) {
            ((ImageView) convertView.findViewById(R.id.categoryImageView)).setImageDrawable(context.getDrawable(context.getResources().getIdentifier("c" + category.id, "drawable", context.getPackageName())));
        }
        ((TextView)convertView.findViewById(R.id.categoryTextView)).setText(Util.toUpperCaseSentence(category.name));
        ((TextView)convertView.findViewById(R.id.translatedName)).setText(Util.toUpperCaseSentence(category.translatedName));

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
                if (category.name.toLowerCase().contains(text.toLowerCase())
                        || category.translatedName.toLowerCase().contains(text.toLowerCase())) {
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
