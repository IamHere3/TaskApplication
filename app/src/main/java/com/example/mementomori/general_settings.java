package com.example.mementomori;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class general_settings extends Fragment {

    int themeID = 15001;
    int deleteThemeID = 15002;
    int newHobbyID = 15003;
    int hobbySpinID = 15004;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_general_settings, container, false);

        // load in current theme

        // import json array
        Settings activity = (Settings) getActivity();
        assert activity != null;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // Screen width
        // Adjusted screen width (40px of padding in fragment holder) - textview (200ish)
        int width = displayMetrics.widthPixels - 300;

        String currentTheme = activity.importTheme();
        String[] hobby = activity.importHobbies();

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.FragmentHolder);

        TableLayout optionHolder = (TableLayout) view.findViewById(R.id.optionHolder);

        // Theme section
        TableRow themeRow = new TableRow(activity);

        TextView editTheme = new TextView(activity);
        editTheme.setText(R.string.themeName);

        EditText editCurrentTheme = new EditText(activity);
        editCurrentTheme.setText(currentTheme);
        editCurrentTheme.setId(themeID);
        editCurrentTheme.setLines(2);

        themeRow.addView(editTheme);
        themeRow.addView(editCurrentTheme);

        TableRow hobbyRow = new TableRow(activity);

        // Hobbies List
        Spinner HobbyList = new Spinner(activity);
        HobbyList.setMinimumWidth(width - 50);

        ArrayList<String> hobbySpinnerArray = new ArrayList<String>();

        for(String entryTotal : hobby)
        {
            String entryHolder = entryTotal;

            int StrLength = entryTotal.length();

            if (StrLength > 12)
            {
                entryHolder = entryTotal.substring(0, 12) + "...";
            }

            hobbySpinnerArray.add(entryHolder);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, hobbySpinnerArray);

        HobbyList.setAdapter(spinnerArrayAdapter);
        HobbyList.setId(hobbySpinID);
        HobbyList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //update edit data
                activity.updateHobbySelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }
        });


        // Delete hobby option
        CheckBox deleteHobby = new CheckBox(activity);
        deleteHobby.setText(R.string.deleteHobby);
        deleteHobby.setId(deleteThemeID);

        hobbyRow.addView(HobbyList);
        hobbyRow.addView(deleteHobby);

        TableRow hobbyNew = new TableRow(activity);

        // Create hobby
        TextView newHobby = new TextView(activity);
        newHobby.setText(R.string.newHobby);

        TableRow hobbyNewEntry = new TableRow(activity);

        EditText newHobbyText = new EditText(activity);
        newHobbyText.setId(newHobbyID);
        newHobbyText.setSingleLine(false);
        newHobbyText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        newHobbyText.setLines(3);

        hobbyNewEntry.addView(newHobbyText);

        TableRow.LayoutParams newHobbyParams = (TableRow.LayoutParams) newHobbyText.getLayoutParams();
        newHobbyParams.span = 2;

        newHobbyText.setLayoutParams(newHobbyParams);

        hobbyNew.addView(newHobby);


        optionHolder.addView(themeRow);
        optionHolder.addView(hobbyRow);
        optionHolder.addView(hobbyNew);
        optionHolder.addView(hobbyNewEntry);

        return view;
    }
}