package com.example.mementomori;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.time.MonthDay;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tasks);

        // assigns values (most efficient way found so far)
        UpdatedMorningRoutine = UpdatedDayRoutine = new HashSet<>();
        UpdatedMonMRoutine = UpdatedTueMRoutine = UpdatedWedMRoutine = UpdatedThrMRoutine = UpdatedFriMRoutine = UpdatedSatMRoutine = UpdatedSunMRoutine = new HashSet<>();
        UpdatedMonERoutine = UpdatedTueERoutine = UpdatedWedERoutine = UpdatedThrERoutine = UpdatedFriERoutine = UpdatedSatERoutine = UpdatedSunERoutine = new HashSet<>();
        UpdatedMonDay = UpdatedTueDay = UpdatedWedDay = UpdatedThrDay = UpdatedFriDay = UpdatedSatDay = UpdatedSunDay = new HashSet<>();
        UpdatedDayTask = UpdatedTomorrowTask = new HashSet<>();

        // Gets current day passed though new task intent
        int currentDay = 0;

        Bundle day = getIntent().getExtras();
        if(day != null)
        {
            currentDay = day.getInt("currentDay");
        }
        else
        {
            Calendar calendar = Calendar.getInstance();
            currentDay = calendar.get(Calendar.DAY_OF_WEEK);
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

            mornRoutineLayout.addView(Perm);

            if (MorningRoutine.size() > 0) {
                TextView PermDay = new TextView(this);
                PermDay.setText(R.string.r_mor);
                PermDay.setTextSize(20);

                mornRoutineLayout.addView(PermDay);

                for (String mornPermTask : MorningRoutine) {
                    String[] entry = mornPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : MorningRoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedMorningRoutine.add(e);
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

                dailyRoutineLayout.addView(PermDay);

                for (String dayPermTask : DayRoutine) {
                    String[] entry = dayPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : DayRoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedDayRoutine.add(e);
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

        weeklyTasks.addView(WeeklyRoutines);

        //region weeklyTasks

        if(MonMRoutine.size() > 0 || MonERoutine.size() > 0)
        {
            TextView MonRoutineW = new TextView(this);
            MonRoutineW.setText(R.string.Mon);
            MonRoutineW.setTextSize(20);

            weeklyTasks.addView(MonRoutineW);

            if(MonMRoutine.size() > 0)
            {
                for (String monPermTask : MonMRoutine) {
                    String[] entry = monPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : MonMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedMonMRoutine.add(e);
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

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : MonERoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedMonERoutine.add(e);
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

            weeklyTasks.addView(TueRoutineW);

            if(TueMRoutine.size() > 0)
            {
                for (String tuePermTask : TueMRoutine) {
                    String[] entry = tuePermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : TueMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedTueMRoutine.add(e);
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

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : TueERoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedTueERoutine.add(e);
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

            weeklyTasks.addView(WedRoutineW);

            if(WedMRoutine.size() > 0)
            {
                for (String wedPermTask : WedMRoutine) {
                    String[] entry = wedPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : WedMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedWedMRoutine.add(e);
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

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : WedERoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedWedERoutine.add(e);
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

            weeklyTasks.addView(ThrRoutineW);

            if(ThrMRoutine.size() > 0)
            {
                for (String thrPermTask : ThrMRoutine) {
                    String[] entry = thrPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : ThrMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedThrMRoutine.add(e);
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

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : ThrERoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedThrERoutine.add(e);
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

            weeklyTasks.addView(FriRoutineW);


            if(FriMRoutine.size() > 0)
            {
                for (String friPermTask : FriMRoutine) {
                    String[] entry = friPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : FriMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedFriMRoutine.add(e);
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

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : FriERoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedFriERoutine.add(e);
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

            weeklyTasks.addView(SatRoutineW);

            if(SatMRoutine.size() > 0)
            {
                for (String satPermTask : SatMRoutine) {
                    String[] entry = satPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : SatMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedSatMRoutine.add(e);
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

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : SatERoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedSatERoutine.add(e);
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

            weeklyTasks.addView(SunRoutineW);

            if(SunMRoutine.size() > 0)
            {
                for (String sunPermTask : SunMRoutine) {
                    String[] entry = sunPermTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : SunMRoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedSunMRoutine.add(e);
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

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : SunERoutine)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedSunERoutine.add(e);
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

        dayTasks.addView(oneDay);

        //region dailyTasks
        if(MonDay.size() > 0 || currentDay == 2 || currentDay == 1)
        {
            TextView MonTempRoutine = new TextView(this);
            MonTempRoutine.setText(R.string.Mon);
            MonTempRoutine.setTextSize(20);

            dayTasks.addView(MonTempRoutine);

            if(MonDay.size() > 0)
                {
                // Adding weekly cued day task
                for (String monTempTask : MonDay) {
                    String[] entry = monTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : MonDay)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedMonDay.add(e);
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
            if(currentDay == 2)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if(currentDay == 1)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }

        if(TueDay.size() > 0 || currentDay == 3 || currentDay == 2)
        {
            TextView TueTempRoutine = new TextView(this);
            TueTempRoutine.setText(R.string.Tue);
            TueTempRoutine.setTextSize(20);

            dayTasks.addView(TueTempRoutine);

            if (TueDay.size() > 0)
            {
                for (String tueTempTask : TueDay) {
                    String[] entry = tueTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : TueDay)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedTueDay.add(e);
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
            if (currentDay == 3)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if (currentDay == 2)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }

        if(WedDay.size() > 0 || currentDay == 4 || currentDay == 3)
        {
            TextView WedTempRoutine = new TextView(this);
            WedTempRoutine.setText(R.string.Wed);
            WedTempRoutine.setTextSize(20);

            dayTasks.addView(WedTempRoutine);

            if (WedDay.size() > 0)
            {
                for (String wedTempTask : WedDay) {
                    String[] entry = wedTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : WedDay)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedWedDay.add(e);
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
            if (currentDay == 4)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if (currentDay == 3)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }

        if(ThrDay.size() > 0 || currentDay == 5 || currentDay == 4)
        {
            TextView ThrTempRoutine = new TextView(this);
            ThrTempRoutine.setText(R.string.Thr);
            ThrTempRoutine.setTextSize(20);

            dayTasks.addView(ThrTempRoutine);

            if (ThrDay.size() > 0)
            {
                for (String thrTempTask : ThrDay) {
                    String[] entry = thrTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : ThrDay)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedThrDay.add(e);
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
            if (currentDay == 5)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if (currentDay == 4)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }

        if(FriDay.size() > 0 || currentDay == 6 || currentDay == 5)
        {
            TextView FriTempRoutine = new TextView(this);
            FriTempRoutine.setText(R.string.Fri);
            FriTempRoutine.setTextSize(20);

            dayTasks.addView(FriTempRoutine);

            if (FriDay.size() > 0)
            {
                for (String friTempTask : FriDay) {
                    String[] entry = friTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : FriDay)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedFriDay.add(e);
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
            if (currentDay == 6)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if (currentDay == 5)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }

        if(SatDay.size() > 0 || currentDay == 7 || currentDay == 6) {
            TextView SatTempRoutine = new TextView(this);
            SatTempRoutine.setText(R.string.Sat);
            SatTempRoutine.setTextSize(20);

            dayTasks.addView(SatTempRoutine);

            if (SatDay.size() > 0)
            {
                for (String satTempTask : SatDay) {
                    String[] entry = satTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : SatDay)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedSatDay.add(e);
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
            if (currentDay == 7)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if (currentDay == 6)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }

        if(SunDay.size() > 0 || currentDay == 1 || currentDay == 7) {
            TextView SunTempRoutine = new TextView(this);
            SunTempRoutine.setText(R.string.Sat);
            SunTempRoutine.setTextSize(20);

            dayTasks.addView(SunTempRoutine);
            if (SunDay.size() > 0)
            {
                for (String sunTempTask : SunDay) {
                    String[] entry = sunTempTask.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    checkBox.setId(Integer.parseInt(entry[3]));
                    checkBox.setLayoutParams(CheckParams);

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // Sets onclick listener
                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            for(String e : SunDay)
                            {
                                String[] listV = e.split(",");

                                if(!Objects.equals(entry[3], listV[3]))
                                {
                                    UpdatedSunDay.add(e);
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
            if (currentDay == 1)
            {
                addOneDay(CheckParams, dayTasks);
            }
            else if (currentDay == 7)
            {
                addTomorrowDay(CheckParams, dayTasks);
            }
        }
        //endregion
    }

    private void addOneDay(LinearLayout.LayoutParams CheckParams, LinearLayout dayTasks)
    {
        // Loads day temporary tasks
        Set<String> DayTask = LoadSharedStrArray("DayTask", new HashSet<>(), "sharedPref");

        for (String oneDayTemp : DayTask) {
            String[] entry = oneDayTemp.split(",");

            CheckBox checkBox = new CheckBox(this);

            checkBox.setId(Integer.parseInt(entry[3]));
            checkBox.setLayoutParams(CheckParams);

            checkBox.setText(entry[1]);
            checkBox.setTextSize(20);

            // Sets onclick listener
            checkBox.setOnClickListener(v -> {
                if(((CheckBox) v).isChecked())
                {
                    for(String e : DayTask)
                    {
                        String[] listV = e.split(",");

                        if(!Objects.equals(entry[3], listV[3]))
                        {
                            UpdatedDayTask.add(e);
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
        // Loads day temporary tasks
        Set<String> TomorrowTask = LoadSharedStrArray("TomorrowTask", new HashSet<>(), "sharedPref");

        for (String day : TomorrowTask) {
            String[] entry = day.split(",");


            CheckBox checkBox = new CheckBox(this);

            checkBox.setId(Integer.parseInt(entry[3]));
            checkBox.setLayoutParams(CheckParams);

            checkBox.setText(entry[1]);
            checkBox.setTextSize(20);

            // Sets onclick listener
            checkBox.setOnClickListener(v -> {
                if(((CheckBox) v).isChecked())
                {
                    for(String e : TomorrowTask)
                    {
                        String[] listV = e.split(",");

                        if(!Objects.equals(entry[3], listV[3]))
                        {
                            UpdatedTomorrowTask.add(e);
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
        if(UpdatedMorningRoutine != null)
        {
            SaveSharedStrArray("MorningRoutine", UpdatedMorningRoutine, "sharedPref");
        }
        if(UpdatedDayRoutine != null)
        {
            SaveSharedStrArray("DayRoutine", UpdatedDayRoutine, "sharedPref");
        }

        if(UpdatedMonMRoutine != null)
        {
            SaveSharedStrArray("MornMonTask", UpdatedMonMRoutine, "sharedPref");
        }
        if(UpdatedTueMRoutine != null)
        {
            SaveSharedStrArray("MornTueTask", UpdatedTueMRoutine, "sharedPref");
        }
        if(UpdatedWedMRoutine != null)
        {
            SaveSharedStrArray("MornWedTask", UpdatedWedMRoutine, "sharedPref");
        }
        if(UpdatedThrMRoutine != null)
        {
            SaveSharedStrArray("MornThrTask", UpdatedThrMRoutine, "sharedPref");
        }
        if(UpdatedFriMRoutine != null)
        {
            SaveSharedStrArray("MornFriTask", UpdatedFriMRoutine, "sharedPref");
        }
        if(UpdatedSatMRoutine != null)
        {
            SaveSharedStrArray("MornSatTask", UpdatedSatMRoutine, "sharedPref");
        }
        if(UpdatedSunMRoutine != null)
        {
            SaveSharedStrArray("MornSunTask", UpdatedSunMRoutine, "sharedPref");
        }

        if(UpdatedMonERoutine != null)
        {
            SaveSharedStrArray("EveMonTask", UpdatedMonERoutine, "sharedPref");
        }
        if(UpdatedTueERoutine != null)
        {
            SaveSharedStrArray("EveTueTask", UpdatedTueERoutine, "sharedPref");
        }
        if(UpdatedWedERoutine != null)
        {
            SaveSharedStrArray("EveWedTask", UpdatedWedERoutine, "sharedPref");
        }
        if(UpdatedThrERoutine != null)
        {
            SaveSharedStrArray("EveThrTask", UpdatedThrERoutine, "sharedPref");
        }
        if(UpdatedFriERoutine != null)
        {
            SaveSharedStrArray("EveFriTask", UpdatedFriERoutine, "sharedPref");
        }
        if(UpdatedSatERoutine != null)
        {
            SaveSharedStrArray("EveSatTask", UpdatedSatERoutine, "sharedPref");
        }
        if(UpdatedSunERoutine != null)
        {
            SaveSharedStrArray("EveSunTask", UpdatedSunERoutine, "sharedPref");
        }

        if(UpdatedDayTask != null)
        {
            SaveSharedStrArray("DayTask", UpdatedDayTask, "sharedPref");
        }
        if(UpdatedTomorrowTask != null)
        {
            SaveSharedStrArray("TomorrowTask", UpdatedTomorrowTask, "sharedPref");
        }


        /*
        // Starts and returns new task view (thus enacting deletedChanges)
        Intent intent = new Intent(AllTasks.this, Tasks.class);
        startActivity(intent);
         */
    }
}