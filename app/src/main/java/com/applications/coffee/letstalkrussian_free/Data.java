package com.applications.coffee.letstalkrussian_free;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by essabri on 04/12/2017.
 */

final class Data {

    private final static String CATEGORIES = "categories";
    private final static String PHRASES = "phrases";

    static SQLiteDatabase db;

    private Data(){}
    static class Manager{

        static ArrayList<Category> categories = new ArrayList<>();
        public static ArrayList<Phrase> phrases = new ArrayList<>();

        private Context context;
        private static DbHelper mDbHelper;

        Manager(Context context) {
            this.context = context;
            mDbHelper = new DbHelper(context);
            HashMap<String, JSONArray> data = Util.Json.getJsonFromAssets(context);
            db = mDbHelper.getReadableDatabase();
            Cursor cursor = db.query(CategoryTable.TABLE_NAME,null,null,null,null,null,null);
            if(cursor.getCount() == 0) {
                db = mDbHelper.getWritableDatabase();
                try {
                    ContentValues values = new ContentValues();
                    for (int i = 0; i < data.get(CATEGORIES).length(); i++) {
                        JSONObject categoryObject = data.get(CATEGORIES).getJSONObject(i);
                        //values.put(CategoryTable.NAME,categoryObject.getString("name"));
                        values.put(CategoryTable.TRANSLATEDNAME,categoryObject.getString("translatedName"));
                        values.put(CategoryTable.ID, categoryObject.getInt("id"));
                        db.insert(CategoryTable.TABLE_NAME, null, values);
                    }
                    values = new ContentValues();
                    for (int i = 0; i < data.get(PHRASES).length(); i++) {
                        JSONObject phraseObject = data.get(PHRASES).getJSONObject(i);
                        //values.put(PhraseTable.ORIGIN,Util.getStringFromResourcesByName("phrase_"+phraseObject.getInt("id"),context));
                        values.put(PhraseTable.TARGET, phraseObject.getString("target"));
                        values.put(PhraseTable.PRONUNCIATION, phraseObject.getString("pronunciation"));
                        values.put(PhraseTable.IS_FAVORITE, 0);
                        values.put(PhraseTable.CATEGORY, phraseObject.getInt("category"));
                        values.put(PhraseTable.ID, phraseObject.getInt("id"));
                        db.insert(PhraseTable.TABLE_NAME, null, values);
                    }
                }
                catch (Exception ex){
                    Log.d("mtag",ex.getMessage());
                }
            }
            db = mDbHelper.getReadableDatabase();
            cursor = db.query(CategoryTable.TABLE_NAME,null,null,null,null,null,null);
            phrases.clear();
            categories.clear();
            while(cursor.moveToNext()) {
                Category category = new Category();
                category.id = cursor.getInt(cursor.getColumnIndexOrThrow(CategoryTable.ID));
                //category.name = cursor.getString(cursor.getColumnIndexOrThrow(CategoryTable.NAME));
                category.name = Util.getStringFromResourcesByName("category_"+category.id,context);
                category.translatedName = cursor.getString(cursor.getColumnIndexOrThrow(CategoryTable.TRANSLATEDNAME));
                categories.add(category);
            }
            cursor = db.query(PhraseTable.TABLE_NAME,null,null,null,null,null,null);
            while(cursor.moveToNext()) {
                Phrase phrase = new Phrase();

                phrase.id = cursor.getInt(cursor.getColumnIndexOrThrow(PhraseTable.ID));
                phrase.target = cursor.getString(cursor.getColumnIndexOrThrow(PhraseTable.TARGET));
                phrase.origin = Util.getStringFromResourcesByName("phrase_"+phrase.id,context);
                /*phrase.origin = cursor.getString(cursor.getColumnIndexOrThrow(PhraseTable.ORIGIN));*/
                phrase.pronunciation = cursor.getString(cursor.getColumnIndexOrThrow(PhraseTable.PRONUNCIATION));
                phrase.setIsFavorite(cursor.getInt(cursor.getColumnIndexOrThrow(PhraseTable.IS_FAVORITE)));
                phrase.category =  cursor.getInt(cursor.getColumnIndexOrThrow(PhraseTable.CATEGORY));
                phrases.add(phrase);
            }
            cursor.close();
        }

        static ArrayList<Phrase> getPhrasesWithCategory(int id){
            ArrayList<Phrase> phrases_ = new ArrayList<>();
            for (Phrase phrase:phrases) {
                if(phrase.category == id){
                    phrases_.add(phrase);
                }
            }
            return phrases_;
        }

        public static ArrayList<Phrase> getFavorites(){
            ArrayList<Phrase> phrases_ = new ArrayList<>();
            for (Phrase phrase:phrases) {
                if(phrase.isFavorite){
                    phrases_.add(phrase);
                }
            }
            return phrases_;
        }

        static Category getCategoryById(int id){
            for (Category category:categories) {
                if(category.id == id){
                    return category;
                }
            }
            return null;
        }

        static void clearFavorites(){
            //phrases.forEach(phrase->{if(phrase.isFavorite) {phrase.isFavorite = false;}});
            for (Phrase phrase:getFavorites()) {
                phrase.isFavorite = false;
                phrase.save();
            }
        }

    }

    //---------------------------------- SQLITE --------------------------------------------
    static class PhraseTable implements BaseColumns {
        static final String ID = "id";
        static final String TABLE_NAME = "phrase";
        /*public static final String ORIGIN = "origin";*/
        static final String TARGET = "target";
        static final String PRONUNCIATION = "pronunciation";
        static final String IS_FAVORITE = "isFavorite";
        static final String CATEGORY = "category";
    }
    static class CategoryTable implements BaseColumns {
        static final String TABLE_NAME = "category";
        static final String ID = "id";
        //public static final String NAME = "name";
        static final String TRANSLATEDNAME = "translatedName";
    }
}
