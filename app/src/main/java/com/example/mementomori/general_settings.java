package com.example.mementomori;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.widget.CompoundButtonCompat;
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
        int width = displayMetrics.widthPixels - 40;

        String currentTheme = activity.importTheme();
        String[] hobby = activity.importHobbies();


        // loads theme
        // if shared pref theme == dark else (light
        // Assigns colour to the check boxes (outline and color of the checkbox)
        int [][] states = {{}};
        int [] colors = {getResources().getColor(R.color.white)};
        ColorStateList colorStateList = new ColorStateList(states, colors);

        // theme text colour
        int textColor = activity.textColor;
        // theme text entry background colour
        int backgroundColour = activity.backgroundColor;

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.FragmentHolder);

        TableLayout optionHolder = (TableLayout) view.findViewById(R.id.optionHolder);

        // Theme section
        TableRow themeTitle = new TableRow(activity);
        TableRow themeEdit = new TableRow(activity);

        TextView editTheme = new TextView(activity);
        editTheme.setText(R.string.themeName);
        editTheme.setTextColor(textColor);

        // Edit hobby
        EditText editCurrentTheme = new EditText(activity);
        editCurrentTheme.setText(currentTheme);
        editCurrentTheme.setId(themeID);
        editCurrentTheme.setLines(2);
        editCurrentTheme.setBackgroundColor(backgroundColour);
        editCurrentTheme.setTextColor(textColor);

        themeTitle.addView(editTheme);
        themeEdit.addView(editCurrentTheme);


        TableRow.LayoutParams newThemeEdit = (TableRow.LayoutParams) editCurrentTheme.getLayoutParams();
        newThemeEdit.span = 2;
        editCurrentTheme.setLayoutParams(newThemeEdit);


        // spinner and delete option
        TableRow hobbyRow = new TableRow(activity);

        // Hobbies List
        Spinner hobbySpinner = new Spinner(activity);
        hobbySpinner.setMinimumWidth(width - 400);

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

        hobbySpinner.setAdapter(spinnerArrayAdapter);
        hobbySpinner.setId(hobbySpinID);
        hobbySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        deleteHobby.setTextColor(textColor);
        deleteHobby.setId(deleteThemeID);
        CompoundButtonCompat.setButtonTintList(deleteHobby, colorStateList);

        //hobbySpinnerArray.addView(editCurrentTheme);
        hobbyRow.addView(hobbySpinner);
        hobbyRow.addView(deleteHobby);

        TableRow hobbyNew = new TableRow(activity);

        // Create hobby
        TextView newHobby = new TextView(activity);
        newHobby.setTextColor(textColor);
        newHobby.setText(R.string.newHobby);

        TableRow hobbyNewEntry = new TableRow(activity);

        EditText newHobbyText = new EditText(activity);
        newHobbyText.setId(newHobbyID);
        newHobbyText.setSingleLine(false);
        newHobbyText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        newHobbyText.setLines(3);
        newHobbyText.setWidth(width);
        newHobbyText.setBackgroundColor(backgroundColour);
        newHobbyText.setTextColor(textColor);

        hobbyNewEntry.addView(newHobbyText);

        TableRow.LayoutParams newHobbyParams = (TableRow.LayoutParams) newHobbyText.getLayoutParams();
        newHobbyParams.span = 2;
        newHobbyText.setLayoutParams(newHobbyParams);

        hobbyNew.addView(newHobby);


        optionHolder.addView(themeTitle);
        optionHolder.addView(themeEdit);

        optionHolder.addView(hobbyRow);

        optionHolder.addView(hobbyNew);
        optionHolder.addView(hobbyNewEntry);

        return view;
    }
}