package com.fei.repeatplay.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.fei.repeatplay.RepeatApplication;

public class SharesPreUtil {

    private static String name = "file";

    private static void putString(Context context ,String str,String value){
        SharedPreferences preferences = context.getSharedPreferences(name,0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(str,value);
        editor.commit();
    }

    private static String getString(Context context,String key){
        SharedPreferences preferences = context.getSharedPreferences(name,0);
        return preferences.getString(key,"");
    }


    public static void setPath(String path){
        putString(RepeatApplication.getApp(),"path",path);
    }

    public static String getPath(){
       return getString(RepeatApplication.getApp(),"path");
    }



}
