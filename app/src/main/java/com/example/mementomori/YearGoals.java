package com.example.mementomori;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class YearGoals extends AppCompatActivity {

    public static final String Shared_Pref = "sharedPref";

    PopupWindow popUpWin;

    String IDHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_goals);

        LinearLayout goalHolder = findViewById(R.id.yearlyGoals);

        TableLayout yearlyGoalTable = findViewById(R.id.yearlyGoalHolder);

        ImageButton addGoalButton = findViewById(R.id.addYearGoalButton);
        ImageButton ResetGoalsButton = findViewById(R.id.resetYearGoals);

        LayoutInflater inflater = (LayoutInflater) YearGoals.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        /*
        LayoutInflater inflater = LayoutInflater.from(this);
        */

        ViewGroup rootView = (ViewGroup) findViewById(R.id.yearsGoals);

        View layout = inflater.inflate(R.layout.popupwindow, null);

        EditText yearEntry = layout.findViewById(R.id.yearGoalEdit);

        // popUpWin = new PopupWindow(this);

        // devices screen density
        float density=YearGoals.this.getResources().getDisplayMetrics().density;

        // create focusable popupWindow
        final PopupWindow popUpWin = new PopupWindow(layout, (int)density*240, (int)density*285, true);

        ResetGoalsButton.setOnClickListener(view -> {

            AlertDialog dialog = (AlertDialog) onCreateDialog(savedInstanceState);

            dialog.show();
            // ResetYearGoalsDialog.onCreateDialog();
        });

        addGoalButton.setOnClickListener(view -> {

            // button to close popup window
            ((Button) layout.findViewById(R.id.back)).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    popUpWin.dismiss();
                }
            });

            // button to save yearly goal
            ((Button) layout.findViewById(R.id.save)).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String yearValue = yearEntry.getText().toString();

                    // save new yearly goal
                    Set<String> yearGoals = LoadSharedStrArray("YearGoals", new HashSet<>());

                    // create new ID
                    byte[] array = new byte[8];
                    new Random().nextBytes(array);
                    IDHolder = new String(array, StandardCharsets.UTF_8);

                    String NewEntry = IDHolder + "," + yearValue;

                    yearGoals.add(NewEntry);

                    SaveSharedStrArray("YearGoals", yearGoals);

                    popUpWin.dismiss();
                }
            });

            // "wake,Get Up (before 7.30),0,1"

            popUpWin.setBackgroundDrawable(new ColorDrawable(Color.GRAY));

            popUpWin.setOutsideTouchable(true);

            popUpWin.showAtLocation(layout, Gravity.CENTER, 0, 0);

        });


        Set<String> yearGoals = LoadSharedStrArray("YearGoals", new HashSet<>());
        String[] yearGoalsArray = yearGoals.toArray(new String[0]);

        for(String goal : yearGoalsArray)
        {
            TableRow GoalEntry = new TableRow(this);
            GoalEntry.setGravity(Gravity.CENTER);

            String[] entry = goal.split(",");

            boolean YearGoals = LoadSharedBoolean(entry[1], false);

            TextView GoalREntry = new TextView(this);
            GoalREntry.setText(entry[2]);
            GoalREntry.setGravity(Gravity.CENTER);
            GoalREntry.setTextSize(20);

            CheckBox complete = new CheckBox(this);
            complete.setGravity(Gravity.END);
            complete.setOnClickListener(v -> SaveBoolData(entry[1], ((CheckBox) v).isChecked()));

            complete.setChecked(YearGoals);

            GoalEntry.addView(complete);
            GoalEntry.addView(GoalREntry);
            yearlyGoalTable.addView(GoalEntry);
        }
    }

    /*
    public void saveValue()
    {
        EditText yearEntry = findViewById(R.id.yearGoalEdit);
        String yearValue = yearEntry.getText().toString();

        // save new yearly goal
        Set<String> yearGoals = LoadSharedStrArray("YearGoals", new HashSet<>());

        int number = yearGoals.size() + 1;

        String NewEntry = String.valueOf(number) + "," + "yearValue";

        yearGoals.add(NewEntry);

        // SaveSharedStrArray("YearGoals", yearGoals);
    }
     */

    private Set<String> LoadSharedStrArray(String valueName, Set<String> value) {
        SharedPreferences sharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);

        return sharedPref.getStringSet(valueName, value);
    }

    private boolean LoadSharedBoolean(String valueName, Boolean value) {
        SharedPreferences sharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);

        return sharedPref.getBoolean(valueName, value);
    }

    private void SaveSharedStrArray(String valueName, Set<String> value)
    {
        // Updates daily progression and new checkbox value
        SharedPreferences UpdateSharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);
        SharedPreferences.Editor editor = UpdateSharedPref.edit();

        editor.putStringSet(valueName, value);
        editor.apply();
    }

    private void SaveBoolData(String valueName, Boolean Value) {

        // Updates daily progression and new checkbox value
        SharedPreferences UpdateSharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);
        SharedPreferences.Editor editor = UpdateSharedPref.edit();

        editor.putBoolean(valueName, Value);
        editor.apply();
    }

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(R.string.conformation);

        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                SaveSharedStrArray("YearGoals", new HashSet<>());
        });

        builder.setNegativeButton(R.string.no, (dialogInterface, i) -> {
            // Dialog closes by it self without the need for anything
        });

        return builder.create();
    }
}