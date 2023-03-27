package com.example.mementomori;

import static java.lang.String.valueOf;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Tasks extends SaveClass {

    int TotalTasksComplete, DayTotalTasks;

    int CheckedTasks;

    double NumberOfTasks = 0;

    int UniqueHobbies = 0;

    int currentDayMS, currentDayES, oneDayS, day;

    Integer[] UniqueHobbiesID = new Integer[10];

    Button YearGoalsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hides title action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_tasks);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        // height adjusted for existing elements
        int height = displayMetrics.heightPixels - 40;

        // Gets saved and current day
        int savedDay = LoadSharedInt("Day", 0);

        // Gets current day
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);

        // Retrieves weekly repeatable tasks
        String[] currentDayM = weekDayM(day);
        String[] currentDayE = weekDayE(day);

        // Checks for new day
        if (savedDay != day) {
            updateTempTasks(day);
        }

        // Loads current tasks
        Set<String> setMorningRoutine = LoadSharedStrArray("MorningRoutine", new HashSet<>(), "sharedPref");
        Set<String> setDayRoutine = LoadSharedStrArray("DayRoutine", new HashSet<>(), "sharedPref");
        Set<String> setTempRoutine = LoadSharedStrArray("DayTask", new HashSet<>(), "sharedPref");

        // Converts sets to string arrays
        String[] arrayMorningRoutine = setMorningRoutine.toArray(new String[0]);
        String[] arrayDayRoutine = setDayRoutine.toArray(new String[0]);
        String[] arrayTempRoutine = setTempRoutine.toArray(new String[0]);

        // Retrieves containers
        LinearLayout morningCheckboxes = findViewById(R.id.morningRoutine);
        LinearLayout dailyCheckboxes = findViewById(R.id.dailyRoutine);
        LinearLayout temporaryCheckboxes = findViewById(R.id.temporaryRoutine);

        // Sets onclick listener
        YearGoalsBtn = findViewById(R.id.yearsGoals);

        YearGoalsBtn.setOnClickListener(view -> {
            Intent intent = new Intent(Tasks.this, YearGoals.class);
            try {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e)
            {
                Toast toast = Toast.makeText(Tasks.this, "no activity found", Toast.LENGTH_LONG);
                toast.show();
            }
        });

        //region $checkbox creation

        // Creates Layout parameters
        // Checkboxes
        LinearLayout.LayoutParams CheckParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        CheckParams.leftMargin = 60;
        CheckParams.rightMargin = 40;
        CheckParams.gravity = Gravity.START;

        // TextView
        LinearLayout.LayoutParams TitleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TitleParams.leftMargin = 120;
        TitleParams.bottomMargin = 20;
        TitleParams.topMargin = 20;
        TitleParams.gravity = Gravity.START;

        // Morning title
        TextView MorningTitle = new TextView(this);
        MorningTitle.setText(R.string.r_mor);
        MorningTitle.setTextSize(20);
        MorningTitle.setLayoutParams(TitleParams);

        morningCheckboxes.addView(MorningTitle);

        for (String mR : arrayMorningRoutine) {

            NumberOfTasks++;
            String[] entry = mR.split(",");

            CheckBox checkBox = new CheckBox(this);

            if (Objects.equals(entry[2], "100")) {
                // Add to array
                UniqueHobbiesID[UniqueHobbies] = Integer.parseInt(entry[3]);
                UniqueHobbies = UniqueHobbies + 1;
            }

            checkBox.setId(Integer.parseInt(entry[3]));

            // Sets Location on screen
            checkBox.setLayoutParams(CheckParams);

            // Sets text
            checkBox.setText(entry[1]);
            checkBox.setTextSize(20);

            // Sets onclick listener
            checkBox.setOnClickListener(v -> {
                if(((CheckBox) v).isChecked())
                {
                    CheckedTasks  = CheckedTasks + 1;
                }
                else
                {
                    CheckedTasks = CheckedTasks - 1;
                }
                SaveBoolData(entry[0], ((CheckBox) v).isChecked());
                updateProgress();
            });
            morningCheckboxes.addView(checkBox);
        }

        // Day title
        TextView DailyTitle = new TextView(this);
        DailyTitle.setText(R.string.r_dai);
        DailyTitle.setTextSize(20);
        DailyTitle.setLayoutParams(TitleParams);

        dailyCheckboxes.addView(DailyTitle);

        for (String dR : arrayDayRoutine) {
            NumberOfTasks++;
            String[] entry = dR.split(",");

            CheckBox checkBox = new CheckBox(this);

            if (Objects.equals(entry[2], "100")) {
                // Add to array
                UniqueHobbiesID[UniqueHobbies] = Integer.parseInt(entry[3]);
                UniqueHobbies = UniqueHobbies + 1;
            }

            checkBox.setId(Integer.parseInt(entry[3]));

            // Sets Location on screen
            checkBox.setLayoutParams(CheckParams);

            // Sets text
            checkBox.setText(entry[1]);
            checkBox.setTextSize(20);

            //checkBox.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.4f));

            checkBox.setOnClickListener(v -> {
                if(((CheckBox) v).isChecked())
                {
                    CheckedTasks  = CheckedTasks + 1;
                }
                else
                {
                    CheckedTasks = CheckedTasks - 1;
                }
                SaveBoolData(entry[0], ((CheckBox) v).isChecked());
                updateProgress();
            });
            dailyCheckboxes.addView(checkBox);
        }

        assert currentDayM != null;
        assert currentDayE != null;

        // TempSize = DayTask CurrentDay = WeekdayTask
        if (arrayTempRoutine.length > 0 || currentDayMS > 0 || currentDayES > 0) {

            // If weekly task
            if (currentDayMS > 0) {
                // Checkbox creation for weekly tasks
                for (String WeekTaskM : currentDayM) {
                    NumberOfTasks++;
                    String[] entry = WeekTaskM.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    if (Objects.equals(entry[2], "100")) {
                        // Add to array
                        UniqueHobbiesID[UniqueHobbies] = Integer.parseInt(entry[3]);
                        UniqueHobbies = UniqueHobbies + 1;
                    }

                    checkBox.setId(Integer.parseInt(entry[3]));

                    // Sets Location on screen
                    checkBox.setLayoutParams(CheckParams);

                    // Sets text
                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    //checkBox.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.4f));

                    checkBox.setOnClickListener(v -> {
                        if (((CheckBox) v).isChecked()) {
                            CheckedTasks = CheckedTasks + 1;
                        } else {
                            CheckedTasks = CheckedTasks - 1;
                        }
                        SaveBoolData(entry[0], ((CheckBox) v).isChecked());
                        updateProgress();
                    });
                    morningCheckboxes.addView(checkBox);
                }
            }

            if (currentDayES > 0) {
                // Checkbox creation for weekly tasks
                for (String WeekTaskE : currentDayE) {
                    NumberOfTasks++;
                    String[] entry = WeekTaskE.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    if (Objects.equals(entry[2], "100")) {
                        // Add to array
                        UniqueHobbiesID[UniqueHobbies] = Integer.parseInt(entry[3]);
                        UniqueHobbies = UniqueHobbies + 1;
                    }

                    checkBox.setId(Integer.parseInt(entry[3]));

                    // Sets Location on screen
                    checkBox.setLayoutParams(CheckParams);

                    // Sets text
                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    //checkBox.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.4f));

                    checkBox.setOnClickListener(v -> {
                        if (((CheckBox) v).isChecked()) {
                            CheckedTasks = CheckedTasks + 1;
                        } else {
                            CheckedTasks = CheckedTasks - 1;
                        }
                        SaveBoolData(entry[0], ((CheckBox) v).isChecked());
                        updateProgress();
                    });
                    dailyCheckboxes.addView(checkBox);
                }
            }

            // Checkbox creation for daily tasks
            if(arrayTempRoutine.length > 0)
            {
                // Temporary tasks title
                TextView TempTaskTitle = new TextView(this);
                TempTaskTitle.setText(R.string.r_day);
                TempTaskTitle.setTextSize(20);
                TempTaskTitle.setLayoutParams(TitleParams);

                temporaryCheckboxes.addView(TempTaskTitle);

                // Checkbox creation for daily tasks
                for (String TempEnt : arrayTempRoutine) {
                    NumberOfTasks++;
                    String[] entry = TempEnt.split(",");

                    CheckBox checkBox = new CheckBox(this);

                    if (Objects.equals(entry[2], "100")) {
                        // Add to array
                        UniqueHobbiesID[UniqueHobbies] = Integer.parseInt(entry[3]);
                        UniqueHobbies = UniqueHobbies + 1;
                    }

                    checkBox.setId(Integer.parseInt(entry[3]));

                    checkBox.setText(entry[1]);
                    checkBox.setTextSize(20);

                    // location on screen
                    checkBox.setLayoutParams(CheckParams);

                    //checkBox.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.4f));

                    checkBox.setOnClickListener(v -> {
                        if(((CheckBox) v).isChecked())
                        {
                            CheckedTasks  = CheckedTasks + 1;
                        }
                        else
                        {
                            CheckedTasks = CheckedTasks - 1;
                        }
                        SaveBoolData(entry[0], ((CheckBox) v).isChecked());
                        updateProgress();
                    });

                    temporaryCheckboxes.addView(checkBox);
                }
            }
        }

        //endregion

        //region $NewDayActions

        // Combine permanent tasks
        String[] allTasks = new String[arrayMorningRoutine.length + arrayDayRoutine.length];

        System.arraycopy(arrayMorningRoutine, 0, allTasks, 0, arrayMorningRoutine.length);
        System.arraycopy(arrayDayRoutine, 0, allTasks, arrayMorningRoutine.length, arrayDayRoutine.length);

        // Updates progression, tasks and hobbies based on day
        if (savedDay == day) {
            // Sets up if box was ticked
            savedDailyTasks(false, day, allTasks, arrayTempRoutine, currentDayM, currentDayE);
        } else {
            // Reads progression
            int oldTotalTask = LoadSharedInt("TotalTasks", 0);
            int oldTotalTasksComplete = LoadSharedInt("TotalComplete", 0);

            // Counts total old daily tasks + completed tasks + resets tasks
            savedDailyTasks(true, day, allTasks, arrayTempRoutine, currentDayM, currentDayE);

            // Works out overall percentage of yesterday
            double dailyTasksPD = Math.round((float) TotalTasksComplete / (float) DayTotalTasks * 100d);

            // New Overall percentage total
            double newOverallTasksD = Math.round(((float) oldTotalTasksComplete + (float) TotalTasksComplete) / ((float) oldTotalTask + (float) DayTotalTasks) * 100d);

            // New Overall tasks
            int totalTask = oldTotalTask + DayTotalTasks;

            // New Total completed
            int newTotalTasksComplete = oldTotalTasksComplete + TotalTasksComplete;

            // Updates progression
            SharedPreferences UpdateSharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);
            SharedPreferences.Editor editor = UpdateSharedPref.edit();

            editor.putInt("DailyProgression", 0);
            editor.putInt("Day", day);

            editor.putInt("OverallProgression", (int) newOverallTasksD);
            editor.putInt("TotalTasks", totalTask);
            editor.putInt("TotalComplete", newTotalTasksComplete);
            editor.putInt("YesterdayProgression", (int) dailyTasksPD);
            editor.apply();
        }

        // endregion

        setProgress();

        // load hobbies
        Set<String> hobbies = LoadSharedStrArray("HobbyOptions", new HashSet<>(), "sharedPref");
        UpdateText(hobbies, savedDay != day);
    }

    //region $randomHobby

    // Updates hobby text
    private void UpdateText(Set<String> setHobbies, boolean newDay) {
        int o = 0;
        int t = 0;
        int z = 0;
        int x = 0;
        int rnd;

        int uniqueHobbiesCount = UniqueHobbies;
        int uniqueHobbiesCountLoad = UniqueHobbies;

        // Error handling
        if (uniqueHobbiesCount == 0) {
            return;
        }

        // Random hobby generation / loading
        if (newDay) {
            while (uniqueHobbiesCount >= 1) {

                uniqueHobbiesCount = uniqueHobbiesCount - 1;

                Random rnd1 = new Random();

                rnd = rnd1.nextInt(setHobbies.size());

                switch (uniqueHobbiesCount) {
                    case 0:
                        SaveIntData("HobbiesOne", rnd);
                        break;

                    case 1:
                        SaveIntData("HobbiesTwo", rnd);
                        break;

                    case 2:
                        SaveIntData("HobbiesThree", rnd);
                        break;

                    case 3:
                        SaveIntData("HobbiesFour", rnd);
                        break;

                    default:
                        Toast toast = Toast.makeText(this, "Their is only a max of four random hobbies", Toast.LENGTH_SHORT);
                        toast.show();
                        break;
                }
            }
        } else {
            while (uniqueHobbiesCount >= 1) {
                uniqueHobbiesCount = uniqueHobbiesCount - 1;

                switch (uniqueHobbiesCount) {
                    case 0:
                        o = LoadSharedInt("HobbiesOne", 1);
                        break;
                    case 1:
                        t = LoadSharedInt("HobbiesTwo", 1);
                        break;
                    case 2:
                        z = LoadSharedInt("HobbiesThree", 1);
                        break;
                    case 3:
                        x = LoadSharedInt("HobbiesFour", 1);
                        break;
                    default:
                        break;
                }
            }
        }

        // Converts Set to array
        String[] arrayHobbies = setHobbies.toArray(new String[0]);

        // Set text
        int counter = 0;
        while (uniqueHobbiesCountLoad >= 1) {
            final CheckBox rHobby = findViewById(UniqueHobbiesID[counter]);

            uniqueHobbiesCountLoad = uniqueHobbiesCountLoad - 1;

            switch (counter) {
                case 0:
                    rHobby.setText(arrayHobbies[o]);
                    break;
                case 1:
                    rHobby.setText(arrayHobbies[t]);
                    break;
                case 2:
                    rHobby.setText(arrayHobbies[z]);
                    break;
                case 3:
                    rHobby.setText(arrayHobbies[x]);
                    break;
                default:
                    break;
            }
            counter = counter + 1;
        }
    }

    //endregion

    //region $UpdateProgress

    // Updates visual elements
    private void updateProgress() {
        // Loads recorded progression
        SaveIntData("DailyProgression", CheckedTasks);

        // Error handling -- resets daily progression to 0 if negative
        if (CheckedTasks < 0) {
            SaveIntData("DailyProgression", 0);
        }

        double NewDailyProgress = ((CheckedTasks / NumberOfTasks) * 100d);
        double p = Math.round(NewDailyProgress);

        // Updates frontend
        final TextView dailyP = findViewById(R.id.DailyProgress);
        dailyP.setText(valueOf(p));
    }

    private void resetDayProgress(){
        SaveIntData("DailyProgression", 0);

        // Updates frontend
        final TextView dailyP = findViewById(R.id.DailyProgress);
        dailyP.setText(valueOf(0));
    }

    private void setProgress() {
        // Loads recorded progression
        double DailyProgress = LoadSharedInt("DailyProgression", 0);
        double YesterdayProgress = LoadSharedInt("YesterdayProgression", 0);
        double OverallProgress = LoadSharedInt("OverallProgression", 0);

        // Gets elements by ID
        final TextView yesterdayP = findViewById(R.id.YesterdayProgress);
        final TextView totalP = findViewById(R.id.TotalProgress);
        final TextView dayP = findViewById(R.id.DailyProgress);

        double NewDailyProgress = ((DailyProgress / NumberOfTasks) * 100d);
        double p = Math.round(NewDailyProgress);

        // Updates page
        yesterdayP.setText(valueOf(YesterdayProgress));
        totalP.setText(valueOf(OverallProgress));
        dayP.setText(valueOf(p));
    }

    //endregion

    private void savedDailyTasks(boolean reset, int day, String[] allTasks, String[] DayTempTasks, String[]  WeeklyTemporaryTasksM, String[] WeeklyTemporaryTasksE) {

        // Resets daily task value if no checkboxes are checked
        boolean ErrorHandling = true;

        // Main tasks
        for (String CheckBox : allTasks) {

            String[] entry = CheckBox.split(",");

            DayTotalTasks++;

            boolean CheckBoxValue = LoadSharedBoolean(entry[0], false);
            int resID = getResources().getIdentifier(entry[3], "id", getPackageName());

            if (CheckBoxValue) {
                // If new day
                if (reset) {
                    TotalTasksComplete++;
                    SaveBoolData(entry[0], false);
                } else {
                    CheckedTasks++;
                    final CheckBox cBox = findViewById(resID);
                    cBox.setChecked(true);

                    ErrorHandling = false;
                }
            } else {
                final CheckBox cBox = findViewById(resID);
                cBox.setChecked(false);
            }
        }

        // Temporary Tasks
        for (String daily : DayTempTasks) {

            String[] entry = daily.split(",");

            DayTotalTasks++;

            boolean CheckBoxValue = LoadSharedBoolean(entry[0], false);
            int resID = getResources().getIdentifier(entry[3], "id", getPackageName());

            if (CheckBoxValue) {
                // If new day
                if (reset) {
                    TotalTasksComplete++;

                    // Removes entry
                    SharedPreferences oldRemoval = getSharedPreferences(Shared_Pref, MODE_PRIVATE);
                    oldRemoval.edit().remove(entry[0]).apply();
                } else {
                    CheckedTasks++;
                    final CheckBox cBox = findViewById(resID);
                    cBox.setChecked(true);

                    ErrorHandling = false;
                }
            } else {
                final CheckBox cBox = findViewById(resID);
                cBox.setChecked(false);
            }
        }

        if (reset) {
            day = day - 1;
            String[] yesterdayM = weekDayM(day);
            String[] yesterdayE = weekDayE(day);

            // Yesterdays tasks
            if (yesterdayM != null) {
                for (String oldWeekly : yesterdayM) {

                    String[] entry = oldWeekly.split(",");

                    DayTotalTasks++;

                    boolean CheckBoxValue = LoadSharedBoolean(entry[0], false);

                    if (CheckBoxValue) {
                        TotalTasksComplete++;
                        SaveBoolData(entry[0], false);
                    } else {
                        CheckedTasks++;
                    }
                }
            }

            if (yesterdayE != null) {
                for (String oldWeekly : yesterdayE) {

                    String[] entry = oldWeekly.split(",");

                    DayTotalTasks++;

                    boolean CheckBoxValue = LoadSharedBoolean(entry[0], false);

                    if (CheckBoxValue) {
                        TotalTasksComplete++;
                        SaveBoolData(entry[0], false);
                    } else {
                        CheckedTasks++;
                    }
                }
            }

        } else {
            // Main tasks
            for (String currentWeekly : WeeklyTemporaryTasksM) {

                String[] entry = currentWeekly.split(",");
                DayTotalTasks++;

                boolean CheckBoxValue = LoadSharedBoolean(entry[0], false);
                int resID = getResources().getIdentifier(entry[3], "id", getPackageName());

                if (CheckBoxValue) {
                        CheckedTasks++;
                        final CheckBox cBox = findViewById(resID);
                        cBox.setChecked(true);

                        ErrorHandling = false;
                    } else {
                        final CheckBox cBox = findViewById(resID);
                        cBox.setChecked(false);
                }
            }

            for (String currentWeekly : WeeklyTemporaryTasksE) {

                String[] entry = currentWeekly.split(",");
                DayTotalTasks++;

                boolean CheckBoxValue = LoadSharedBoolean(entry[0], false);
                int resID = getResources().getIdentifier(entry[3], "id", getPackageName());

                if (CheckBoxValue) {
                    CheckedTasks++;
                    final CheckBox cBox = findViewById(resID);
                    cBox.setChecked(true);

                    ErrorHandling = false;
                } else {
                    final CheckBox cBox = findViewById(resID);
                    cBox.setChecked(false);
                }
            }
        }

        if(ErrorHandling)
        {
            resetDayProgress();
        }
    }

    private void updateTempTasks(int day)
    {
        // Gets tomorrow (now today's tasks)
        Set<String> NewDayTasks = LoadSharedStrArray("TomorrowTask", new HashSet<>(), "sharedPref");

        // Gets old day tasks
        Set<String> DayTasks = LoadSharedStrArray("DayTask", new HashSet<>(), "sharedPref");

        // Counts total day tasks and number of them completed
        for (String countChecks : DayTasks)
        {
            String[] entry = countChecks.split(",");

            DayTotalTasks++;

            boolean CheckBoxValue = LoadSharedBoolean(entry[0], false);

            if(CheckBoxValue)
            {
                TotalTasksComplete++;
            }
        }

        SharedPreferences oldRemoval = getSharedPreferences(Shared_Pref, MODE_PRIVATE);
        // Clears day
        oldRemoval.edit().remove("DayTask").apply();

        // Clears tomorrow
        oldRemoval.edit().remove("TomorrowTask").apply();

        // Gets week string array
        Set<String> dayTemp = dayWeek(day);

        // Ensures day temp is not empty
        if(oneDayS > 0)
        {
            assert dayTemp != null;
            NewDayTasks.addAll(dayTemp);
        }

        // Updates day tasks with new day
        SaveSharedStrArray("DayTask", NewDayTasks, "sharedPref");
    }

    private String[] weekDayM(int day)
    {
        switch (day)
        {
            case(1) :
                 Set<String> Sun = LoadSharedStrArray("MornSunTask", new HashSet<>(), "WeekT");
                 currentDayMS = Sun.size();
                 return Sun.toArray(new String[0]);

            case(2) :
                Set<String> Mon = LoadSharedStrArray("MornMonTask", new HashSet<>(), "WeekT");
                currentDayMS = Mon.size();
                return Mon.toArray(new String[0]);

            case(3) :
                Set<String> Tue = LoadSharedStrArray("MornTueTask", new HashSet<>(), "WeekT");
                currentDayMS = Tue.size();
                return Tue.toArray(new String[0]);

            case(4) :
                Set<String> Wed = LoadSharedStrArray("MornWedTask", new HashSet<>(), "WeekT");
                currentDayMS = Wed.size();
                return Wed.toArray(new String[0]);

            case(5) :
                Set<String> Thr = LoadSharedStrArray("MornThrTask", new HashSet<>(), "WeekT");
                currentDayMS = Thr.size();
                return Thr.toArray(new String[0]);

            case(6) :
                Set<String> Fri = LoadSharedStrArray("MornFriTask", new HashSet<>(), "WeekT");
                currentDayMS = Fri.size();
                return Fri.toArray(new String[0]);

            case(7) :
                Set<String> Sat =  LoadSharedStrArray("MornSatTask", new HashSet<>(), "WeekT");
                currentDayMS = Sat.size();
                return Sat.toArray(new String[0]);
        }
        return null;
    }

    private String[] weekDayE(int day)
    {
        switch (day)
        {
            case(1) :
                Set<String> Sun = LoadSharedStrArray("EveSunTask", new HashSet<>(), "WeekT");
                currentDayES = Sun.size();
                return Sun.toArray(new String[0]);

            case(2) :
                Set<String> Mon = LoadSharedStrArray("EveMonTask", new HashSet<>(), "WeekT");
                currentDayES = Mon.size();
                return Mon.toArray(new String[0]);

            case(3) :
                Set<String> Tue = LoadSharedStrArray("EveTueTask", new HashSet<>(), "WeekT");
                currentDayES = Tue.size();
                return Tue.toArray(new String[0]);

            case(4) :
                Set<String> Wed = LoadSharedStrArray("EveWedTask", new HashSet<>(), "WeekT");
                currentDayES = Wed.size();
                return Wed.toArray(new String[0]);

            case(5) :
                Set<String> Thr = LoadSharedStrArray("EveThrTask", new HashSet<>(), "WeekT");
                currentDayES = Thr.size();
                return Thr.toArray(new String[0]);

            case(6) :
                Set<String> Fri = LoadSharedStrArray("EveFriTask", new HashSet<>(), "WeekT");
                currentDayES = Fri.size();
                return Fri.toArray(new String[0]);

            case(7) :
                Set<String> Sat =  LoadSharedStrArray("EveSatTask", new HashSet<>(), "WeekT");
                currentDayES = Sat.size();
                return Sat.toArray(new String[0]);
        }
        return null;
    }

    private Set<String> dayWeek(int day)
    {
        SharedPreferences oldRemoval = getSharedPreferences(Shared_Pref, MODE_PRIVATE);
        switch (day)
        {
            case(1) :
                Set<String> Mon = LoadSharedStrArray("DayMonTask", new HashSet<>(), "sharedPref");
                oneDayS = Mon.size();
                oldRemoval.edit().remove("DayMonTask").apply();
                return Mon;

            case(2) :
                Set<String> Tue = LoadSharedStrArray("DayTueTask", new HashSet<>(), "sharedPref");
                oneDayS = Tue.size();
                oldRemoval.edit().remove("DayTueTask").apply();
                return Tue;

            case(3) :
                Set<String> Wed = LoadSharedStrArray("DayWedTask", new HashSet<>(), "sharedPref");
                oneDayS = Wed.size();
                oldRemoval.edit().remove("DayWedTask").apply();
                return Wed;

            case(4) :
                Set<String> Thr = LoadSharedStrArray("DayThrTask", new HashSet<>(), "sharedPref");
                oneDayS = Thr.size();
                oldRemoval.edit().remove("DayThrTask").apply();
                return Thr;

            case(5) :
                Set<String> Fri = LoadSharedStrArray("DayFriTask", new HashSet<>(), "sharedPref");
                oneDayS = Fri.size();
                oldRemoval.edit().remove("DayFriTask").apply();
                return Fri;

            case(6) :
                Set<String> Sat = LoadSharedStrArray("DaySatTask", new HashSet<>(), "sharedPref");
                oneDayS = Sat.size();
                oldRemoval.edit().remove("DaySatTask").apply();
                return Sat;

            case(7) :
                Set<String> Sun = LoadSharedStrArray("DaySunTask", new HashSet<>(), "sharedPref");
                oneDayS = Sun.size();
                oldRemoval.edit().remove("DaySunTask").apply();
                return Sun;
        }
        return null;
    }

    public void AllTasks(View view) {
        Intent intent = new Intent(Tasks.this, AllTasks.class);
        intent.putExtra("currentDay", day);
        startActivity(intent);
    }
}