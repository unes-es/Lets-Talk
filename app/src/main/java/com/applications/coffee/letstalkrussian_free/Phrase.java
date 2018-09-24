package com.applications.coffee.letstalkrussian_free;

import android.content.ContentValues;

/**
 * Created by essabri on 11/12/2017.
 */

public class Phrase{
    public int id;
    public String origin;
    public String target;
    public String pronunciation;
    public boolean isFavorite;
    public int category;

    public void toggleIsFavorite(){
        isFavorite = !isFavorite;
    }

    public void setIsFavorite(int fav){
        isFavorite = fav != 0;
    }

    public int getIsFavorite(){
        return isFavorite ? 1 : 0;
    }

    public void save(){
        ContentValues data = new ContentValues();
        data.put(Data.PhraseTable.IS_FAVORITE,this.getIsFavorite());
        Data.db.update(Data.PhraseTable.TABLE_NAME,data, Data.PhraseTable.ID+" = "+this.id,null);
    }
}