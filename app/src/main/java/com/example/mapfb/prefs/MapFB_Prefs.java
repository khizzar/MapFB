package com.example.mapfb.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mapfb.models.UserModel;
import com.google.gson.Gson;

public class MapFB_Prefs {
    public final String MapFB_Prefs = "MapFB_Prefs" ;
    SharedPreferences preference;

    public void saveUserModel(Context context, String key, UserModel userModel){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MapFB_Prefs,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String mapData = gson.toJson(userModel);
        editor.putString(key,mapData);
        editor.apply();
        editor.commit();
    }

    public UserModel loadDataObject(Context context,String key){

        SharedPreferences sharedPreferences = context.getSharedPreferences(MapFB_Prefs,Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(key, "");
        UserModel userModel = gson.fromJson(json, UserModel.class);
        return userModel;

    }

}
