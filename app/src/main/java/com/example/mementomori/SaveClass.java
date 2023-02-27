package com.example.mementomori;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Set;

public class SaveClass extends AppCompatActivity {
    public static final String Shared_Pref = "sharedPref";
    // ------------------------------------------------------------------------ Shared preferences -------------------------------------------------------

    //region saveVariables

    protected void SaveBoolData(String valueName, Boolean Value) //, String Shared_Pref)
    {
        SharedPreferences UpdateSharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);
        SharedPreferences.Editor editor = UpdateSharedPref.edit();

        editor.remove(valueName);
        editor.putBoolean(valueName, Value);
        editor.apply();
    }

    protected void SaveIntData(String valueName, Integer Value) // , String Shared_Pref)
    {
        SharedPreferences UpdateSharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);
        SharedPreferences.Editor editor = UpdateSharedPref.edit();

        editor.remove(valueName);
        editor.putInt(valueName, Value);
        editor.apply();
    }

    protected void SaveSharedStr(String valueName, String Value) //, String Shared_Pref)
    {
        SharedPreferences UpdateSharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);
        SharedPreferences.Editor editor = UpdateSharedPref.edit();

        editor.remove(valueName);
        editor.putString(valueName, Value);
        editor.apply();
    }

    protected void SaveSharedStrArray(String valueName, Set<String> value, String Shared_Pref)
    {
        SharedPreferences UpdateSharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);
        SharedPreferences.Editor editor = UpdateSharedPref.edit();

        editor.remove(valueName);
        editor.putStringSet(valueName, value);
        editor.apply();
    }

    //endregion

    //region loadVariables
    protected boolean LoadSharedBoolean(String valueName, Boolean value) // , String Shared_Pref)
    {
        SharedPreferences sharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);

        return sharedPref.getBoolean(valueName, value);
    }

    protected int LoadSharedInt(String valueName, Integer value) //, String Shared_Pref)
    {
        SharedPreferences sharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);

        return sharedPref.getInt(valueName, value);
    }

    protected String LoadSharedStr(String valueName, String value) // , String Shared_Pref)
    {
        SharedPreferences sharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);

        return sharedPref.getString(valueName, value);
    }

    protected Set<String> LoadSharedStrArray(String valueName, Set<String> value, String Shared_Pref)
    {
        SharedPreferences sharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);

        return sharedPref.getStringSet(valueName, value);
    }
    //endregion
}