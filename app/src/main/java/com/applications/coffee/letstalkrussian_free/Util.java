package com.applications.coffee.letstalkrussian_free;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by essabri on 04/12/2017.
 */

public final class Util {

    public static class Json{
        private static final String DATAFILE = "data.json";
        private static final String[] ARRAYSNAMES = {"phrases","categories"};

        public static HashMap<String,JSONArray> getJsonFromAssets(Context context){
            HashMap<String,JSONArray> jsonArray = new HashMap<>();
            try {
                InputStream is = context.getAssets().open(DATAFILE);
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                is.close();
                for (String arrayName:ARRAYSNAMES) {
                    jsonArray.put(arrayName,new JSONObject(new String(buffer, "UTF-8")).getJSONArray(arrayName));
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return jsonArray;
        }
    }

    public static String toUpperCaseSentence(String text){
        return text.substring(0,1).toUpperCase()+text.substring(1);
    }

    public static String getStringFromResourcesByName(String resourceName,Context c){
        int resourceId = c.getResources().getIdentifier(resourceName,"string",c.getPackageName());
        return c.getString(resourceId);
    }
}
