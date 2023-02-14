package com.example.mementomori;

import static java.lang.String.valueOf;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class TaskDays extends Fragment {

    public static final String Shared_Pref = "sharedPref";

    // id for text entry
    static int entryId = 1500;
    static int spinId = 1499;
    static int deleteCheckboxID = 1498;

    // radio button IDs
    static int MonID = 3000;
    static int TueID = 3001;
    static int WedID = 3002;
    static int ThrID = 3003;
    static int FriID = 3004;
    static int SatID = 3005;
    static int SunID = 3006;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_days, container, false);

        // import json array
        Settings activity = (Settings) getActivity();
        assert activity != null;
        String[] importedMorningData = activity.importMorningData();
        String[] importedDailyData = activity.importDayData();
        String[] importedTotalData = activity.importTotalData();

        LinearLayout layout = (LinearLayout) view.findViewById(R.id.taskFragment);

        TableRow.LayoutParams rowPram = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        TableLayout.LayoutParams tablePram = new TableLayout.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

        TableLayout tableLayout = (TableLayout) view.findViewById(R.id.tableFragment);

        TableRow selectTaskRow = new TableRow(activity);
        selectTaskRow.setLayoutParams(rowPram);

        TextView selectTaskTitle = new TextView(activity);
        selectTaskTitle.setText(R.string.editTask);

        ArrayList<String> spinnerArray = new ArrayList<String>();


        for(String entryTotal : importedTotalData)
        {
            String[] entry = entryTotal.split(",");

            spinnerArray.add(entry[1]);
            // spinnerArray.add(Integer.parseInt(entry[3]), entry[1]);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_dropdown_item, spinnerArray);


        Spinner selectTaskDropdown = new Spinner(activity);
        selectTaskDropdown.setAdapter(spinnerArrayAdapter);
        selectTaskDropdown.setId(spinId);

        selectTaskDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
               //update edit data
               activity.updateTaskInfo(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // do nothing
            }
        });

        // apply
        selectTaskRow.addView(selectTaskTitle);
        selectTaskRow.addView(selectTaskDropdown);

        // edit task
        TableRow editTaskRow = new TableRow(activity);
        editTaskRow.setLayoutParams(rowPram);

        TextView editText = new TextView(activity);
        editText.setText(R.string.editTask);

        EditText editEntry = new EditText(activity);
        editEntry.setText(R.string.holderText);
        editEntry.setId(entryId);

        // apply
        editTaskRow.addView(editText);
        editTaskRow.addView(editEntry);

        // edit task
        TableRow deleteTaskRow = new TableRow(activity);
        deleteTaskRow.setLayoutParams(rowPram);

        TextView deleteTask = new TextView(activity);
        deleteTask.setText(R.string.deleteTask);

        CheckBox deleteCheckbox = new CheckBox(activity);
        deleteCheckbox.setText(R.string.removeTask);
        deleteCheckbox.setId(deleteCheckboxID);

        deleteTaskRow.addView(deleteTask);
        deleteTaskRow.addView(deleteCheckbox);

        tableLayout.addView(selectTaskRow);
        tableLayout.addView(editTaskRow);
        tableLayout.addView(deleteTaskRow);

        return view;
    }
}