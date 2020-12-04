package com.example.allab;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

public class drug_data {
    public static final String PREFERENCES_NAME = "drug_db";

    private static final String DEFAULT_VALUE_STRING = "";
    private static final boolean DEFAULT_VALUE_BOOLEAN = false;
    private static final int DEFAULT_VALUE_INT = -1;

    private static SharedPreferences getPreferences(Context context)
    {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
    public static void setArray(Context context, String jsondata)
    {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("jsondata",jsondata);
        editor.commit();
    }
    public static String getArray(Context context)
    {
        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString("jsondata","fail");
        return value;
    }
    public static void setString(Context context, String key, String value)
    {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static String getString(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);
        String value = prefs.getString(key, DEFAULT_VALUE_STRING);
        return value;

    }
    public static void setBoolean(Context context, String key, boolean value) {

        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }
    public static boolean getBoolean(Context context, String key) {

        SharedPreferences prefs = getPreferences(context);
        boolean value = prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN);
        return value;

    }

}
