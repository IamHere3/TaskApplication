package com.example.mementomori;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.widget.CompoundButtonCompat;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class AllTasks extends SaveClass {

    // For storing updated Lists
    Set<String> UpdatedMorningRoutine, UpdatedDayRoutine;
    Set<String> UpdatedMonMRoutine, UpdatedTueMRoutine, UpdatedWedMRoutine, UpdatedThrMRoutine, UpdatedFriMRoutine, UpdatedSatMRoutine, UpdatedSunMRoutine;
    Set<String> UpdatedMonERoutine, UpdatedTueERoutine, UpdatedWedERoutine, UpdatedThrERoutine, UpdatedFriERoutine, UpdatedSatERoutine, UpdatedSunERoutine;
    Set<String> UpdatedMonDay, UpdatedTueDay, UpdatedWedDay, UpdatedThrDay, UpdatedFriDay, UpdatedSatDay, UpdatedSunDay;
    Set<String> UpdatedDayTask, UpdatedTomorrowTask;
    Set<String> OneDayTask, TomorrowTask;
    int textColor;
    ColorStateList colorStateList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        // assigns values (most efficient way found so far)
        UpdatedMorningRoutine = UpdatedDayRoutine = null;
        UpdatedMonMRoutine = UpdatedTueMRoutine = UpdatedWedMRoutine = UpdatedThrMRoutine = UpdatedFriMRoutine = UpdatedSatMRoutine = UpdatedSunMRoutine = null;
        UpdatedMonERoutine = UpdatedTueERoutine = UpdatedWedERoutine = UpdatedThrERoutine = UpdatedFriERoutine = UpdatedSatERoutine = UpdatedSunERoutine = null;
        UpdatedMonDay = UpdatedTueDay = UpdatedWedDay = UpdatedThrDay = UpdatedFriDay = UpdatedSatDay = UpdatedSunDay = null;
        UpdatedDayTask = UpdatedTomorrowTask = null;

        // Gets current day passed though new task intent
        int currentDay = 0;

        Bundle day = getIntent().getExtras();
        if(day != null)
        {
            currentDay = day.getInt("currentDay");
        }
        else
        {
            // Sunday == 1
            Calendar calendar = Calendar.getInstance();
            currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        }

        // Gets theme
        if(day != null)
        {
            textColor = day.getInt("textColor");
            colorStateList = day.getParcelable("colorState");
        }

        // Loads all permanent tasks
        Set<String> DayRoutine = LoadSharedStrArray("DayRoutine", new HashSet<>(), "sharedPref");
        Set<String> MorningRoutine = LoadSharedStrArray("MorningRoutine", new HashSet<>(), "sharedPref");

        // Loads all weekly tasks
        Set<String> MonMRoutine = LoadSharedStrArray("MornMonTask", new HashSet<>(), "WeekT");
        Set<String> TueMRoutine = LoadSharedStrArray("MornTueTask", new HashSet<>(), "WeekT");
        Set<String> WedMRoutine = LoadSharedStrArray("MornWedTask", new HashSet<>(), "WeekT");
        Set<String> ThrMRoutine = LoadSharedStrArray("MornThrTask", new HashSet<>(), "WeekT");
        Set<String> FriMRoutine = LoadSharedStrArray("MornFriTask", new HashSet<>(), "WeekT");
        Set<String> SatMRoutine = LoadSharedStrArray("MornSatTask", new HashSet<>(), "WeekT");
        Set<String> SunMRoutine = LoadSharedStrArray("MornSunTask", new HashSet<>(), "WeekT");

        Set<String> MonERoutine = LoadSharedStrArray("EveMonTask", new HashSet<>(), "WeekT");
        Set<String> TueERoutine = LoadSharedStrArray("EveTueTask", new HashSet<>(), "WeekT");
        Set<String> WedERoutine = LoadSharedStrArray("EveWedTask", new HashSet<>(), "WeekT");
        Set<String> ThrERoutine = LoadSharedStrArray("EveThrTask", new HashSet<>(), "WeekT");
        Set<String> FriERoutine = LoadSharedStrArray("EveFriTask", new HashSet<>(), "WeekT");
        Set<String> SatERoutine = LoadSharedStrArray("EveSatTask", new HashSet<>(), "WeekT");
        Set<String> SunERoutine = LoadSharedStrArray("EveSunTask", new HashSet<>(), "WeekT");

        // Loads weekday temporary day tasks
        Set<String> MonDay = LoadSharedStrArray("DayMonTask", new HashSet<>(), "sharedPref");
        Set<String> TueDay = LoadSharedStrArray("DayTueTask", new HashSet<>(), "sharedPref");
        Set<String> WedDay = LoadSharedStrArray("DayWedTask", new HashSet<>(), "sharedPref");
        Set<String> ThrDay = LoadSharedStrArray("DayThrTask", new HashSet<>(), "sharedPref");
        Set<String> FriDay = LoadSharedStrArray("DayFriTask", new HashSet<>(), "sharedPref");
        Set<String> SatDay = LoadSharedStrArray("DaySatTask", new HashSet<>(), "sharedPref");
        Set<String> SunDay = LoadSharedStrArray("DaySunTask", new HashSet<>(), "sharedPref");

        // Loads today and tomorrow temporary tasks (loads here as is checked before loading day tasks)
        OneDayTask = LoadSharedStrArray("DayTask", new HashSet<>(), "sharedPref");
        TomorrowTask = LoadSharedStrArray("TomorrowTask", new HashSet<>(), "sharedPref");


        LinearLayout mornRoutineLayout = findViewById(R.id.morningRoutine);
        LinearLayout dailyRoutineLayout = findViewById(R.id.dailyRoutine);
        LinearLayout weeklyTasks = findViewById(R.id.weeklyTasks);
        LinearLayout dayTasks = findViewById(R.id.dayTasks);

        Button button = findViewById(R.id.ButtonID);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteMethod();
            }
        });

        // Creates Layout parameters
        // Checkboxes
        LinearLayout.LayoutParams CheckParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        CheckParams.leftMargin = 60;
        CheckParams.rightMargin = 40;
        CheckParams.gravity = Gravity.START;

        //region permanentTasks
        if(DayRoutine.size() > 0 || MorningRoutine.size() > 0)
        {
            TextView Perm = new TextView(this);
            Perm.setText(R.string.taskLengthPermanent);
            Perm.setTextSize(20);
            Perm.setTextColor(textColor);

            mornRoutineLayout.addView(Perm);

            if (MorningRoutine.size() > 0) {
                TextView PermDay = new TextView(this);
                PermDay.setText(R.string.r_mor);
                PermDay.setTextSize(20);
                PermDay.setTextColor(textColor);

                mornRoutineLayout.addView(PermDay);

                for (String mornPermTask : MorningRoutine) {
                    String[] entry = mornPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedMorningRoutine == null)
                            {
                                UpdatedMorningRoutine = new HashSet<>();

                                for(String e : MorningRoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedMorningRoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedMorningRoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedMorningRoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedMorningRoutine.add(ReAdd);
                        }
                    });
                    mornRoutineLayout.addView(checkBox);
                }
            }

            if (DayRoutine.size() > 0) {
                TextView PermDay = new TextView(this);
                PermDay.setText(R.string.r_dai);
                PermDay.setTextSize(20);
                PermDay.setTextColor(textColor);

                dailyRoutineLayout.addView(PermDay);

                for (String dayPermTask : DayRoutine) {
                    String[] entry = dayPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedDayRoutine == null)
                            {
                                UpdatedDayRoutine = new HashSet<>();

                                for(String e : DayRoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedDayRoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedDayRoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedDayRoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedDayRoutine.add(ReAdd);
                        }
                    });
                    dailyRoutineLayout.addView(checkBox);
                }
            }
        }
        //endregion


        TextView WeeklyRoutines = new TextView(this);
        WeeklyRoutines.setText(R.string.taskWeekly);
        WeeklyRoutines.setTextSize(20);
        WeeklyRoutines.setTextColor(textColor);

        weeklyTasks.addView(WeeklyRoutines);

        //region weeklyTasks

        if(MonMRoutine.size() > 0 || MonERoutine.size() > 0)
        {
            TextView MonRoutineW = new TextView(this);
            MonRoutineW.setText(R.string.Mon);
            MonRoutineW.setTextSize(20);
            MonRoutineW.setTextColor(textColor);

            weeklyTasks.addView(MonRoutineW);

            if(MonMRoutine.size() > 0)
            {
                for (String monPermTask : MonMRoutine) {
                    String[] entry = monPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedMonMRoutine == null)
                            {
                                UpdatedMonMRoutine = new HashSet<>();

                                for(String e : MonMRoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedMonMRoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedMonMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedMonMRoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedMonMRoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }

            if(MonERoutine.size() > 0)
            {
                for (String monPermTask : MonERoutine) {
                    String[] entry = monPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedMonERoutine == null)
                            {
                                UpdatedMonERoutine = new HashSet<>();

                                for(String e : MonERoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedMonERoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedMonERoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedMonERoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : UpdatedMonERoutine)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedMonERoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }
        }

        if(TueMRoutine.size() > 0 || TueERoutine.size() > 0)
        {
            TextView TueRoutineW = new TextView(this);
            TueRoutineW.setText(R.string.Tue);
            TueRoutineW.setTextSize(20);
            TueRoutineW.setTextColor(textColor);

            weeklyTasks.addView(TueRoutineW);

            if(TueMRoutine.size() > 0)
            {
                for (String tuePermTask : TueMRoutine) {
                    String[] entry = tuePermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedTueMRoutine == null)
                            {
                                UpdatedTueMRoutine = new HashSet<>();

                                for(String e : TueMRoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedTueMRoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedTueMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedTueMRoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedTueMRoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }

            if(TueERoutine.size() > 0)
            {
                for (String tuePermTask : TueERoutine) {
                    String[] entry = tuePermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedTueERoutine == null)
                            {
                                UpdatedTueERoutine = new HashSet<>();

                                for(String e : TueERoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedTueERoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedTueERoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedTueERoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedTueERoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }
        }

        if(WedMRoutine.size() > 0 || WedERoutine.size() > 0)
        {
            TextView WedRoutineW = new TextView(this);
            WedRoutineW.setText(R.string.Wed);
            WedRoutineW.setTextSize(20);
            WedRoutineW.setTextColor(textColor);

            weeklyTasks.addView(WedRoutineW);

            if(WedMRoutine.size() > 0)
            {
                for (String wedPermTask : WedMRoutine) {
                    String[] entry = wedPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedWedMRoutine == null)
                            {
                                UpdatedWedMRoutine = new HashSet<>();

                                for(String e : WedMRoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedWedMRoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedWedMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedWedMRoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedWedMRoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }

            if(WedERoutine.size() > 0)
            {
                for (String wedPermTask : WedERoutine) {
                    String[] entry = wedPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedWedERoutine == null)
                            {
                                UpdatedWedERoutine = new HashSet<>();

                                for(String e : WedERoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedWedERoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedWedERoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedWedERoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedWedERoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }
        }

        if(ThrMRoutine.size() > 0 || ThrERoutine.size() > 0)
        {
            TextView ThrRoutineW = new TextView(this);
            ThrRoutineW.setText(R.string.Thr);
            ThrRoutineW.setTextSize(20);
            ThrRoutineW.setTextColor(textColor);

            weeklyTasks.addView(ThrRoutineW);

            if(ThrMRoutine.size() > 0)
            {
                for (String thrPermTask : ThrMRoutine) {
                    String[] entry = thrPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedThrMRoutine == null)
                            {
                                UpdatedThrMRoutine = new HashSet<>();

                                for(String e : ThrMRoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedThrMRoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedThrMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedThrMRoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedThrMRoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }

            if(ThrERoutine.size() > 0)
            {
                for (String thrPermTask : ThrERoutine) {
                    String[] entry = thrPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedThrERoutine == null)
                            {
                                UpdatedThrERoutine = new HashSet<>();

                                for(String e : ThrERoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedThrERoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedThrERoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedThrERoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedThrERoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }
        }

        if(FriMRoutine.size() > 0 || FriERoutine.size() > 0)
        {
            TextView FriRoutineW = new TextView(this);
            FriRoutineW.setText(R.string.Fri);
            FriRoutineW.setTextSize(20);
            FriRoutineW.setTextColor(textColor);

            weeklyTasks.addView(FriRoutineW);


            if(FriMRoutine.size() > 0)
            {
                for (String friPermTask : FriMRoutine) {
                    String[] entry = friPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedFriMRoutine == null)
                            {
                                UpdatedFriMRoutine = new HashSet<>();

                                for(String e : FriMRoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedFriMRoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedFriMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedFriMRoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedFriMRoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }

            if(FriERoutine.size() > 0)
            {
                for (String friPermTask : FriERoutine) {
                    String[] entry = friPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedFriERoutine == null)
                            {
                                UpdatedFriERoutine = new HashSet<>();

                                for(String e : FriERoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedFriERoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedFriERoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedFriERoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedFriERoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }
        }

        if(SatERoutine.size() > 0 || SatMRoutine.size() > 0)
        {
            TextView SatRoutineW = new TextView(this);
            SatRoutineW.setText(R.string.Sat);
            SatRoutineW.setTextSize(20);
            SatRoutineW.setTextColor(textColor);

            weeklyTasks.addView(SatRoutineW);

            if(SatMRoutine.size() > 0)
            {
                for (String satPermTask : SatMRoutine) {
                    String[] entry = satPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedSatMRoutine == null)
                            {
                                UpdatedSatMRoutine = new HashSet<>();

                                for(String e : SatMRoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedSatMRoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedSatMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedSatMRoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedSatMRoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }

            if(SatERoutine.size() > 0)
            {
                for (String satPermTask : SatERoutine) {
                    String[] entry = satPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedSatERoutine == null)
                            {
                                UpdatedSatERoutine = new HashSet<>();

                                for(String e : SatERoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedSatERoutine.add(e);
                                    }
                                }
                            }

                            for(String e : UpdatedSatERoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedSatERoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedSatERoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }
        }

        if(SunMRoutine.size() > 0 || SatERoutine.size() > 0)
        {
            TextView SunRoutineW = new TextView(this);
            SunRoutineW.setText(R.string.Sun);
            SunRoutineW.setTextSize(20);
            SunRoutineW.setTextColor(textColor);

            weeklyTasks.addView(SunRoutineW);

            if(SunMRoutine.size() > 0)
            {
                for (String sunPermTask : SunMRoutine) {
                    String[] entry = sunPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedSunMRoutine == null)
                            {
                                UpdatedSunMRoutine = new HashSet<>();

                                for(String e : SunMRoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedSunMRoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedSunMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedSunMRoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedSunMRoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }

            if(SunERoutine.size() > 0)
            {
                for (String sunPermTask : SunERoutine) {
                    String[] entry = sunPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedSunERoutine == null)
                            {
                                UpdatedSunERoutine = new HashSet<>();

                                for(String e : SunERoutine)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedSunERoutine.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedSunERoutine)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedSunERoutine.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedSunERoutine.add(ReAdd);
                        }
                    });

                    weeklyTasks.addView(checkBox);
                }
            }
        }
        //endregion

        TextView oneDay = new TextView(this);
        oneDay.setText(R.string.taskOneDay);
        oneDay.setTextSize(20);
        oneDay.setTextColor(textColor);

        dayTasks.addView(oneDay);

        //region dailyTasks
        if(MonDay.size() > 0 || currentDay == 2 && OneDayTask.size() != 0 || currentDay == 3 && TomorrowTask.size() != 0)
        {
            TextView MonTempRoutine = new TextView(this);
            MonTempRoutine.setText(R.string.Mon);
            MonTempRoutine.setTextSize(20);
            MonTempRoutine.setTextColor(textColor);

            dayTasks.addView(MonTempRoutine);

            if(MonDay.size() > 0)
                {
                // Adding weekly cued day task
                for (String monTempTask : MonDay) {
                    String[] entry = monTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedMonDay == null)
                            {
                                UpdatedMonDay = new HashSet<>();

                                for(String e : MonDay)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedMonDay.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedMonDay)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedMonDay.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedMonDay.add(ReAdd);
                        }
                    });

                    dayTasks.addView(checkBox);
                }
            }
            // Adding one day and tomorrow task
            if(currentDay == 2 && OneDayTask.size() != 0)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if(currentDay == 3 && TomorrowTask.size() != 0)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }

        if(TueDay.size() > 0 || currentDay == 3 && OneDayTask.size() != 0 || currentDay == 4 && TomorrowTask.size() != 0)
        {
            TextView TueTempRoutine = new TextView(this);
            TueTempRoutine.setText(R.string.Tue);
            TueTempRoutine.setTextSize(20);
            TueTempRoutine.setTextColor(textColor);

            dayTasks.addView(TueTempRoutine);

            if (TueDay.size() > 0)
            {
                for (String tueTempTask : TueDay) {
                    String[] entry = tueTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedTueDay == null)
                            {
                                UpdatedTueDay = new HashSet<>();

                                for(String e : TueDay)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedTueDay.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedTueDay)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedTueDay.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedTueDay.add(ReAdd);
                        }
                    });

                    dayTasks.addView(checkBox);
                }
            }
            // Adding one day and tomorrow task
            if (currentDay == 3 && OneDayTask.size() != 0)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if (currentDay == 4 && TomorrowTask.size() != 0)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }

        if(WedDay.size() > 0 || currentDay == 4 && OneDayTask.size() != 0 || currentDay == 5 && TomorrowTask.size() != 0)
        {
            TextView WedTempRoutine = new TextView(this);
            WedTempRoutine.setText(R.string.Wed);
            WedTempRoutine.setTextSize(20);
            WedTempRoutine.setTextColor(textColor);

            dayTasks.addView(WedTempRoutine);

            if (WedDay.size() > 0)
            {
                for (String wedTempTask : WedDay) {
                    String[] entry = wedTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedWedDay == null)
                            {
                                UpdatedWedDay = new HashSet<>();

                                for(String e : WedDay)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedWedDay.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedWedDay)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedWedDay.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedWedDay.add(ReAdd);
                        }
                    });

                    dayTasks.addView(checkBox);
                }
            }
            // Adding one day and tomorrow task
            if (currentDay == 4 && OneDayTask.size() != 0)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if (currentDay == 5 && TomorrowTask.size() != 0)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }

        if(ThrDay.size() > 0 || currentDay == 5 && OneDayTask.size() != 0 || currentDay == 6 && TomorrowTask.size() != 0)
        {
            TextView ThrTempRoutine = new TextView(this);
            ThrTempRoutine.setText(R.string.Thr);
            ThrTempRoutine.setTextSize(20);
            ThrTempRoutine.setTextColor(textColor);

            dayTasks.addView(ThrTempRoutine);

            if (ThrDay.size() > 0)
            {
                for (String thrTempTask : ThrDay) {
                    String[] entry = thrTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedThrDay == null)
                            {
                                UpdatedThrDay = new HashSet<>();

                                for(String e : ThrDay)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedThrDay.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedThrDay)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedThrDay.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedThrDay.add(ReAdd);
                        }
                    });

                    dayTasks.addView(checkBox);
                }
            }
            // Adding one day and tomorrow task
            if (currentDay == 5 && OneDayTask.size() != 0)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if (currentDay == 6 && TomorrowTask.size() != 0)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }

        if(FriDay.size() > 0 || currentDay == 6 && OneDayTask.size() != 0 || currentDay == 7 && TomorrowTask.size() != 0)
        {
            TextView FriTempRoutine = new TextView(this);
            FriTempRoutine.setText(R.string.Fri);
            FriTempRoutine.setTextSize(20);
            FriTempRoutine.setTextColor(textColor);

            dayTasks.addView(FriTempRoutine);

            if (FriDay.size() > 0)
            {
                for (String friTempTask : FriDay) {
                    String[] entry = friTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedFriDay == null)
                            {
                                UpdatedFriDay = new HashSet<>();

                                for(String e : FriDay)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedFriDay.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedFriDay)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedFriDay.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedFriDay.add(ReAdd);
                        }
                    });

                    dayTasks.addView(checkBox);
                }
            }
            // Adding one day and tomorrow task
            if (currentDay == 6 && OneDayTask.size() != 0)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if (currentDay == 7 && TomorrowTask.size() != 0)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }

        if(SatDay.size() > 0 || currentDay == 7 && OneDayTask.size() != 0 || currentDay == 1 && TomorrowTask.size() != 0) {
            TextView SatTempRoutine = new TextView(this);
            SatTempRoutine.setText(R.string.Sat);
            SatTempRoutine.setTextSize(20);
            SatTempRoutine.setTextColor(textColor);

            dayTasks.addView(SatTempRoutine);

            if (SatDay.size() > 0)
            {
                for (String satTempTask : SatDay) {
                    String[] entry = satTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedSatDay == null)
                            {
                                UpdatedSatDay = new HashSet<>();

                                for(String e : SatDay)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedSatDay.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedSatDay)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedSatDay.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedSatDay.add(ReAdd);
                        }
                    });

                    dayTasks.addView(checkBox);
                }
            }
            // Adding one day and tomorrow task
            if (currentDay == 7 && OneDayTask.size() != 0)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if (currentDay == 1 && TomorrowTask.size() != 0)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }

        if(SunDay.size() > 0 || currentDay == 1 && OneDayTask.size() != 0 || currentDay == 2 && TomorrowTask.size() != 0) {
            TextView SunTempRoutine = new TextView(this);
            SunTempRoutine.setText(R.string.Sat);
            SunTempRoutine.setTextSize(20);
            SunTempRoutine.setTextColor(textColor);

            dayTasks.addView(SunTempRoutine);
            if (SunDay.size() > 0)
            {
                for (String sunTempTask : SunDay) {
                    String[] entry = sunTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
                    checkBox.setTextColor(textColor);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            if (UpdatedSunDay == null)
                            {
                                UpdatedSunDay = new HashSet<>();

                                for(String e : SunDay)
                                {
                                    String[] listV = e.split(",");

                                    if(!Objects.equals(entry[3], listV[3]))
                                    {
                                        UpdatedSunDay.add(e);
                                    }
                                }
                            }
                            for(String e : UpdatedSunDay)
                            {
                                String[] listV = e.split(",");

                                if(Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedSunDay.remove(e);
                                }
                            }
                        }
                        else
                        {
                            StringBuilder reAdd = new StringBuilder();

                            for(String e : entry)
                            {
                                reAdd.append(e);
                                reAdd.append(",");
                            }

                            String ReAdd = reAdd.toString();

                            UpdatedSunDay.add(ReAdd);
                        }
                    });

                    dayTasks.addView(checkBox);
                }
            }
            // Adding one day and tomorrow task
            if (currentDay == 1 && OneDayTask.size() != 0)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if (currentDay == 2 && TomorrowTask.size() != 0)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }
        //endregion
    }

    private void addOneDay(LinearLayout.LayoutParams CheckParams, LinearLayout dayTasks)
    {
        for (String oneDayTemp : OneDayTask) {
            String[] entry = oneDayTemp.split(",");

            CheckBox checkBox = new CheckBox(this);

            checkBox.setId(Integer.parseInt(entry[3]));
            checkBox.setLayoutParams(CheckParams);

            CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
            checkBox.setTextColor(textColor);

            checkBox.setText(entry[1]);
            checkBox.setTextSize(20);

            // Sets onclick listener
            checkBox.setOnClickListener(v -> {
                if(((CheckBox) v).isChecked())
                {
                    if (UpdatedDayTask == null)
                    {
                        UpdatedDayTask = new HashSet<>();

                        for(String e : OneDayTask)
                        {
                            String[] listV = e.split(",");

                            if(!Objects.equals(entry[3], listV[3]))
                            {
                                UpdatedDayTask.add(e);
                            }
                        }
                    }
                    for(String e : UpdatedDayTask)
                    {
                        String[] listV = e.split(",");

                        if(Objects.equals(entry[3], listV[3]))
                        {
                            UpdatedDayTask.remove(e);
                        }
                    }
                }
                else
                {
                    StringBuilder reAdd = new StringBuilder();

                    for(String e : entry)
                    {
                        reAdd.append(e);
                        reAdd.append(",");
                    }

                    String ReAdd = reAdd.toString();

                    UpdatedDayTask.add(ReAdd);
                }
            });

            dayTasks.addView(checkBox);
        }
    }

    private void addTomorrowDay(LinearLayout.LayoutParams CheckParams, LinearLayout dayTasks)
    {
        for (String day : TomorrowTask) {
            String[] entry = day.split(",");


            CheckBox checkBox = new CheckBox(this);

            checkBox.setId(Integer.parseInt(entry[3]));
            checkBox.setLayoutParams(CheckParams);

            CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);
            checkBox.setTextColor(textColor);

            checkBox.setText(entry[1]);
            checkBox.setTextSize(20);

            // Sets onclick listener
            checkBox.setOnClickListener(v -> {
                if(((CheckBox) v).isChecked())
                {
                    if (UpdatedTomorrowTask == null)
                    {
                        UpdatedTomorrowTask = new HashSet<>();

                        for(String e : TomorrowTask)
                        {
                            String[] listV = e.split(",");

                            if(!Objects.equals(entry[3], listV[3]))
                            {
                                UpdatedTomorrowTask.add(e);
                            }
                        }
                    }
                    for(String e : UpdatedTomorrowTask)
                    {
                        String[] listV = e.split(",");

                        if(Objects.equals(entry[3], listV[3]))
                        {
                            UpdatedTomorrowTask.remove(e);
                        }
                    }
                }
                else
                {
                    StringBuilder reAdd = new StringBuilder();

                    for(String e : entry)
                    {
                        reAdd.append(e);
                        reAdd.append(",");
                    }

                    String ReAdd = reAdd.toString();

                    UpdatedTomorrowTask.add(ReAdd);
                }
            });

            dayTasks.addView(checkBox);
        }
    }

    private void deleteMethod()
    {
        // Permanent
        if(UpdatedMorningRoutine != null)
        {
            SaveSharedStrArray("MorningRoutine", UpdatedMorningRoutine, "sharedPref");
        }
        if(UpdatedDayRoutine != null)
        {
            SaveSharedStrArray("DayRoutine", UpdatedDayRoutine, "sharedPref");
        }

        // Weekly
        if(UpdatedMonMRoutine != null)
        {
            SaveSharedStrArray("MornMonTask", UpdatedMonMRoutine, "WeekT");
        }
        if(UpdatedTueMRoutine != null)
        {
            SaveSharedStrArray("MornTueTask", UpdatedTueMRoutine, "WeekT");
        }
        if(UpdatedWedMRoutine != null)
        {
            SaveSharedStrArray("MornWedTask", UpdatedWedMRoutine, "WeekT");
        }
        if(UpdatedThrMRoutine != null)
        {
            SaveSharedStrArray("MornThrTask", UpdatedThrMRoutine, "WeekT");
        }
        if(UpdatedFriMRoutine != null)
        {
            SaveSharedStrArray("MornFriTask", UpdatedFriMRoutine, "WeekT");
        }
        if(UpdatedSatMRoutine != null)
        {
            SaveSharedStrArray("MornSatTask", UpdatedSatMRoutine, "WeekT");
        }
        if(UpdatedSunMRoutine != null)
        {
            SaveSharedStrArray("MornSunTask", UpdatedSunMRoutine, "WeekT");
        }

        if(UpdatedMonERoutine != null)
        {
            SaveSharedStrArray("EveMonTask", UpdatedMonERoutine, "WeekT");
        }
        if(UpdatedTueERoutine != null)
        {
            SaveSharedStrArray("EveTueTask", UpdatedTueERoutine, "WeekT");
        }
        if(UpdatedWedERoutine != null)
        {
            SaveSharedStrArray("EveWedTask", UpdatedWedERoutine, "WeekT");
        }
        if(UpdatedThrERoutine != null)
        {
            SaveSharedStrArray("EveThrTask", UpdatedThrERoutine, "WeekT");
        }
        if(UpdatedFriERoutine != null)
        {
            SaveSharedStrArray("EveFriTask", UpdatedFriERoutine, "WeekT");
        }
        if(UpdatedSatERoutine != null)
        {
            SaveSharedStrArray("EveSatTask", UpdatedSatERoutine, "WeekT");
        }
        if(UpdatedSunERoutine != null)
        {
            SaveSharedStrArray("EveSunTask", UpdatedSunERoutine, "WeekT");
        }

        // Day
        if(UpdatedMonDay != null)
        {
            SaveSharedStrArray("DayMonTask", UpdatedMonDay, "sharedPref");
        }
        if(UpdatedTueDay != null)
        {
            SaveSharedStrArray("DayTueTask", UpdatedTueDay, "sharedPref");
        }
        if(UpdatedWedDay != null)
        {
            SaveSharedStrArray("DayWedTask", UpdatedWedDay, "sharedPref");
        }
        if(UpdatedThrDay != null)
        {
            SaveSharedStrArray("DayThrTask", UpdatedThrDay, "sharedPref");
        }
        if(UpdatedFriDay != null)
        {
            SaveSharedStrArray("DayFriTask", UpdatedFriDay, "sharedPref");
        }
        if(UpdatedSatDay != null)
        {
            SaveSharedStrArray("DaySatTask", UpdatedSatDay, "sharedPref");
        }
        if(UpdatedSunDay != null)
        {
            SaveSharedStrArray("DaySunTask", UpdatedSunDay, "sharedPref");
        }

        if(UpdatedDayTask != null)
        {
            SaveSharedStrArray("DayTask", UpdatedDayTask, "sharedPref");
        }
        if(UpdatedTomorrowTask != null)
        {
            SaveSharedStrArray("TomorrowTask", UpdatedTomorrowTask, "sharedPref");
        }

        // Starts and returns new task view (thus enacting deletedChanges)
        Intent intent = new Intent(AllTasks.this, Tasks.class);
        startActivity(intent);
    }
}