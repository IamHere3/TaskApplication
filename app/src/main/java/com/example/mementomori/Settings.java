package com.example.mementomori;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Settings extends SaveClass {

    private String[] arrayMorningRoutine, arrayDayRoutine, arrayTotalRoutine, arrayHobby, arrayDailyRoutine;

    public String taskSpinTextSelected, hobbySpinTextSelected;

    int taskSpinSelected;

    // ID for text entry
    static int spinId = 1499;
    static int deleteCheckboxID = 1498;

    // New task ID values
    static int entryId = 1500;
    static int saveTaskId = 1001;
    static int hobbyTaskId = 1000;
    static int eveningToggle = 9997;
    static int morningToggle = 9996;

    // General option ID
    static int themeID = 15001;
    static int deleteThemeID = 15002;
    static int newHobbyID = 15003;
    static int hobbySpinID = 15004;

    // Task length variables
    static int permanentTask = 2000;
    static int dayTask = 2001;
    static int tomorrowTask = 2002;
    static int weeklyTask = 2003;
    static int oneDayTask = 2004;

    // Radio button IDs
    static int MonID = 2500;
    static int TueID = 2501;
    static int WedID = 2502;
    static int ThrID = 2503;
    static int FriID = 2504;
    static int SatID = 2505;
    static int SunID = 2506;

    // theme colors
    int textColor;
    int backgroundColor;
    ColorStateList colorStateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hides title action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_settings);

        // loads theme
        // if shared pref theme == dark else (light
        // Assigns colour to the check boxes (outline and color of the checkbox)
        int [][] states = {{}};
        int [] colors = {getResources().getColor(R.color.white)};
        colorStateList = new ColorStateList(states, colors);

        // sets background color
        backgroundColor = getResources().getColor(R.color.dark_grey);

        // sets text color
        textColor = getResources().getColor(R.color.white);

        // Initial fragment selection
        FragmentManager EditTasks = getSupportFragmentManager();
        FragmentTransaction ft = EditTasks.beginTransaction();

        // Updates frame holder with a fragment
        ft.replace(R.id.FragmentHolder, new NewTask(), "NewTask");
        ft.commit();
    }

    //region requiredFragmentFunctions

    public String[] importMorningData()
    {
        // Loads tasks
        Set<String> setMorningRoutine = LoadSharedStrArray("MorningRoutine", new HashSet<>(), "sharedPref");

        // Converts sets to string arrays
        arrayMorningRoutine = setMorningRoutine.toArray(new String[0]);

        return arrayMorningRoutine;
    }

    public String[] importDayData()
    {
        Set<String> setDayRoutine = new HashSet<>(LoadSharedStrArray("DayRoutine", new HashSet<>(), "sharedPref"));

        arrayDayRoutine = setDayRoutine.toArray(new String[0]);

        return arrayDayRoutine;
    }

    public String[] importDailyData()
    {
        Set<String> setDayRoutine = new HashSet<>(LoadSharedStrArray("DayTask", new HashSet<>(), "sharedPref"));

        arrayDailyRoutine = setDayRoutine.toArray(new String[0]);

        return arrayDailyRoutine;
    }

    public String[] importHobbies()
    {
        Set<String> setDayRoutine = new HashSet<>(LoadSharedStrArray("HobbyOptions", new HashSet<>(), "sharedPref"));

        arrayHobby = setDayRoutine.toArray(new String[0]);

        return arrayHobby;
    }

    public String importTheme()
    {
        return LoadSharedStr("currentTheme", "App of quotes");
    }

    public String[] importTotalData()
    {
        // Loads tasks
        Set<String> setDailyRoutine = new HashSet<>(LoadSharedStrArray("DayTask", new HashSet<>(), "sharedPref"));

        // Gets current day
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        Set<String> holder = weekDay(day);

        String[] setDailyRoutineArray = setDailyRoutine.toArray(new String[0]);
        assert holder != null;
        String[] currentDayArray = holder.toArray(new String[0]);

        // Combine all tasks
        arrayTotalRoutine = new String[arrayMorningRoutine.length + arrayDayRoutine.length + setDailyRoutineArray.length + currentDayArray.length];

        // Join morning and day permanent routines
        System.arraycopy(arrayMorningRoutine, 0, arrayTotalRoutine, 0, arrayMorningRoutine.length);
        System.arraycopy(arrayDayRoutine, 0, arrayTotalRoutine, arrayMorningRoutine.length, arrayDayRoutine.length);

        // Join daily and weekly tasks
        int ArrayHolder = arrayMorningRoutine.length + arrayDayRoutine.length;

        System.arraycopy(setDailyRoutineArray, 0, arrayTotalRoutine, ArrayHolder, setDailyRoutineArray.length);

        // Joins day, morning and daily routines together
        ArrayHolder = arrayMorningRoutine.length + arrayDayRoutine.length + setDailyRoutineArray.length;

        System.arraycopy(currentDayArray, 0, arrayTotalRoutine, ArrayHolder, currentDayArray.length);

        return arrayTotalRoutine;
    }

    protected void updateTaskInfo(int positionSelected)
    {
        final TextView dailyP = findViewById(entryId);

        int countedEntries = 0;
        taskSpinSelected = positionSelected;

        taskSpinTextSelected = "holder";

        for (String counter : arrayTotalRoutine)
        {
            String[] entry = counter.split(",");

            if(positionSelected == countedEntries)
            {
                taskSpinTextSelected = entry[1];
                break;
            }
            else
            {
                countedEntries = countedEntries + 1;
            }
        }

        int i = 0;
        int lines = 1;
        while (taskSpinTextSelected.length() > i)
        {
            lines++;
            i = i + 18;
        }

        dailyP.setText(taskSpinTextSelected);
        dailyP.setLines(lines);
    }

    protected void updateHobbySelected(int spinPositionSelected)
    {
        int countedEntries = 0;

        for (String counter : arrayHobby)
        {
            if(countedEntries == spinPositionSelected)
            {
                hobbySpinTextSelected = counter;
                break;
            }
            countedEntries = countedEntries + 1;
        }
    }
    //endregion

    //region $saveButton
    public void taskSettings(View view) {
        // get fragments by tag -- tag set when changing fragment (see fragment call)
        Fragment editTask = getSupportFragmentManager().findFragmentByTag("EditTask");
        Fragment newTask = getSupportFragmentManager().findFragmentByTag("NewTask");
        Fragment genSettings = getSupportFragmentManager().findFragmentByTag("GeneralSettings");

        // Checks which fragment is visible
        if (editTask != null && editTask.isVisible())
        {
            EditTasksSave(view);
        }
        else if (newTask != null && newTask.isVisible())
        {
            newTaskCreation();
        }
        else if (genSettings != null && genSettings.isVisible())
        {
            GeneralSettingsSave(view);
        }
        else
        {
            Toast toast = Toast.makeText(this, "Nothing detected", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
    //endregion

    //region $executeFunctions

    private void newTaskCreation()
    {
        // Loads tasks
        Set<String> setMorningRoutine = new HashSet<>(LoadSharedStrArray("MorningRoutine", new HashSet<>(), "sharedPref"));
        Set<String> setDayRoutine = new HashSet<>(LoadSharedStrArray("DayRoutine", new HashSet<>(), "sharedPref"));

        String newStringTask;
        String newTaskNameID;

        // Basic task data retrieving
        final TextView textEntry = findViewById(entryId);
        final CheckBox hobbyTask = findViewById(hobbyTaskId);

        String TaskText = textEntry.getText().toString();
        boolean hobbyValue = hobbyTask.isChecked();

        // Task group retrieving
        final RadioButton eveningValue = findViewById(eveningToggle);
        final RadioButton morningValue = findViewById(morningToggle);

        // Task length retrieving
        final RadioButton permanentTaskR = findViewById(permanentTask);
        final RadioButton dayTaskR = findViewById(dayTask);
        final RadioButton tomorrowTaskR = findViewById(tomorrowTask);
        final RadioButton weeklyTaskR = findViewById(weeklyTask);
        final RadioButton oneDayTaskR = findViewById(oneDayTask);

        boolean permanentTaskValue = permanentTaskR.isChecked();
        boolean dayTaskValue = dayTaskR.isChecked();
        boolean tomorrowTaskValue = tomorrowTaskR.isChecked();
        boolean weeklyTaskValue = weeklyTaskR.isChecked();
        boolean oneDayTaskValue = oneDayTaskR.isChecked();

        // Error catching
        if (TaskText.contains(",")) {
            Toast toast = Toast.makeText(this, "Please don't use commas (,)", Toast.LENGTH_SHORT);
            toast.show();

            return;
        }

        // Setting task ID
        newTaskNameID = RandomStringGeneration();

        // Setting item ID
        Random random = new Random();
        int randomTop = 10000;
        int totalCount = random.nextInt(randomTop);

        // Sets new task values
        if (hobbyValue) {
            newStringTask = newTaskNameID + ",hobbies,100," + totalCount;
        } else {
            newStringTask = newTaskNameID + "," + TaskText + "," + "0" + "," + totalCount;
        }

        //region TaskLengthConfiguring
        if(permanentTaskValue)
        {
            if(eveningValue.isChecked())
            {
                Set<String> eveningArray = new HashSet<>();

                for (String counter : setDayRoutine) {
                    counter = counter.replace("[", "");
                    counter = counter.replace("]", "");

                    String[] entry = counter.split(",");

                    if (Objects.equals(newTaskNameID, entry[0])) {
                        Toast toast = Toast.makeText(this, "saveID is already taken please try again", Toast.LENGTH_SHORT);
                        toast.show();

                        return;
                    }
                    eveningArray.add(counter);
                }
                eveningArray.add(newStringTask);

                SaveSharedStrArray("DayRoutine", eveningArray, "sharedPref");

                Toast toast = Toast.makeText(this, "New task added", Toast.LENGTH_SHORT);
                toast.show();
            }
            else if(morningValue.isChecked())
            {
                Set<String> morningArray = new HashSet<>();

                for(String counter : setMorningRoutine)
                {
                    counter = counter.replace("[", "");
                    counter = counter.replace("]", "");

                    morningArray.add(counter);

                    String[] entry = counter.split(",");

                    if (Objects.equals(newTaskNameID, entry[0])) {
                        Toast toast = Toast.makeText(this, "saveID is already taken please try again", Toast.LENGTH_SHORT);
                        toast.show();

                        return;
                    }
                }
                morningArray.add(newStringTask);

                SaveSharedStrArray("MorningRoutine", morningArray, "sharedPref");

                Toast toast = Toast.makeText(this, "New permanent task added", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if (dayTaskValue)
        {
            Set<String> dayTaskHolder = new HashSet<>(LoadSharedStrArray("DayTask", new HashSet<>(), "sharedPref"));

            dayTaskHolder.add(newStringTask);

            SaveSharedStrArray("DayTask", dayTaskHolder, "sharedPref");

            Toast toast = Toast.makeText(this, "New one day task added", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (tomorrowTaskValue)
        {
            Set<String> tomorrowTask = new HashSet<>(LoadSharedStrArray("TomorrowTask", new HashSet<>(), "sharedPref"));

            tomorrowTask.add(newStringTask);

            SaveSharedStrArray("TomorrowTask", tomorrowTask, "sharedPref");

            Toast toast = Toast.makeText(this, "Task added for tomorrow", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (oneDayTaskValue)
        {
            boolean daySelect = false;

            CheckBox Mon = findViewById(MonID);
            CheckBox Tue = findViewById(TueID);
            CheckBox Wed = findViewById(WedID);
            CheckBox Thr = findViewById(ThrID);
            CheckBox Fri = findViewById(FriID);
            CheckBox Sat = findViewById(SatID);
            CheckBox Sun = findViewById(SunID);

            boolean MonValue = Mon.isChecked();
            boolean TueValue = Tue.isChecked();
            boolean WedValue = Wed.isChecked();
            boolean ThrValue = Thr.isChecked();
            boolean FriValue = Fri.isChecked();
            boolean SatValue = Sat.isChecked();
            boolean SunValue = Sun.isChecked();

            if(MonValue)
            {
                Set<String> MonDayTask = new HashSet<>(LoadSharedStrArray("DayMonTask", new HashSet<>(), "sharedPref"));

                MonDayTask.add(newStringTask);

                SaveSharedStrArray("DayMonTask", MonDayTask, "sharedPref");

                Toast toast = Toast.makeText(this, "One day task saved for Monday", Toast.LENGTH_SHORT);
                toast.show();

                daySelect = true;
            }
            if(TueValue)
            {
                Set<String> TueDayTask = new HashSet<>(LoadSharedStrArray("DayTueTask", new HashSet<>(), "sharedPref"));

                TueDayTask.add(newStringTask);

                SaveSharedStrArray("DayTueTask", TueDayTask, "sharedPref");

                Toast toast = Toast.makeText(this, "One day task saved for Tuesday", Toast.LENGTH_SHORT);
                toast.show();

                daySelect = true;
            }
            if(WedValue)
            {
                Set<String> WedDayTask = new HashSet<>(LoadSharedStrArray("DayWedTask", new HashSet<>(), "sharedPref"));

                WedDayTask.add(newStringTask);

                SaveSharedStrArray("DayWedTask", WedDayTask, "sharedPref");

                Toast toast = Toast.makeText(this, "One day task saved for Wednesday", Toast.LENGTH_SHORT);
                toast.show();

                daySelect = true;
            }
            if(ThrValue)
            {
                Set<String> ThrDayTask = new HashSet<>(LoadSharedStrArray("DayThrTask", new HashSet<>(), "sharedPref"));

                ThrDayTask.add(newStringTask);

                SaveSharedStrArray("DayThrTask", ThrDayTask, "sharedPref");

                Toast toast = Toast.makeText(this, "One day task saved for Thursday", Toast.LENGTH_SHORT);
                toast.show();

                daySelect = true;
            }
            if(FriValue)
            {
                Set<String> FriDayTask = new HashSet<>(LoadSharedStrArray("DayFriTask", new HashSet<>(), "sharedPref"));

                FriDayTask.add(newStringTask);

                SaveSharedStrArray("DayFriTask", FriDayTask, "sharedPref");

                Toast toast = Toast.makeText(this, "One day task saved for Friday", Toast.LENGTH_SHORT);
                toast.show();

                daySelect = true;
            }
            if(SatValue)
            {
                Set<String> SatDayTask = new HashSet<>(LoadSharedStrArray("DaySatTask", new HashSet<>(), "sharedPref"));

                SatDayTask.add(newStringTask);

                SaveSharedStrArray("DaySatTask", SatDayTask, "sharedPref");

                Toast toast = Toast.makeText(this, "One day task saved for Saturday", Toast.LENGTH_SHORT);
                toast.show();

                daySelect = true;
            }
            if(SunValue)
            {
                Set<String> SunDayTask = new HashSet<>(LoadSharedStrArray("DaySunTask", new HashSet<>(), "sharedPref"));

                SunDayTask.add(newStringTask);

                SaveSharedStrArray("DaySunTask", SunDayTask, "sharedPref");

                Toast toast = Toast.makeText(this, "One day task saved for Sunday", Toast.LENGTH_SHORT);
                toast.show();

                daySelect = true;
            }

            if(!daySelect)
            {
                Toast toast = Toast.makeText(this, "Please select day", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if (weeklyTaskValue)
        {
            boolean daySelect = false;

            CheckBox Mon = findViewById(MonID);
            CheckBox Tue = findViewById(TueID);
            CheckBox Wed = findViewById(WedID);
            CheckBox Thr = findViewById(ThrID);
            CheckBox Fri = findViewById(FriID);
            CheckBox Sat = findViewById(SatID);
            CheckBox Sun = findViewById(SunID);

            boolean MonValue = Mon.isChecked();
            boolean TueValue = Tue.isChecked();
            boolean WedValue = Wed.isChecked();
            boolean ThrValue = Thr.isChecked();
            boolean FriValue = Fri.isChecked();
            boolean SatValue = Sat.isChecked();
            boolean SunValue = Sun.isChecked();

            if(MonValue)
            {
                if(morningValue.isChecked())
                {
                    Set<String> MonTasks = new HashSet<>(LoadSharedStrArray("MornMonTask", new HashSet<>(), "WeekT"));

                    MonTasks.add(newStringTask);

                    SaveSharedStrArray("MornMonTask", MonTasks, "WeekT");
                }
                else
                {
                    Set<String> MonTasks = new HashSet<>(LoadSharedStrArray("EveMonTask", new HashSet<>(), "WeekT"));

                    MonTasks.add(newStringTask);

                    SaveSharedStrArray("EveMonTask", MonTasks, "WeekT");
                }

                daySelect = true;

                Toast toast = Toast.makeText(this, "Weekly task saved for Monday", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (TueValue)
            {
                if(morningValue.isChecked())
                {
                    Set<String> TueTasks = new HashSet<>(LoadSharedStrArray("MornTueTask", new HashSet<>(), "WeekT"));

                    TueTasks.add(newStringTask);

                    SaveSharedStrArray("MornTueTask", TueTasks, "WeekT");
                }
                else if(eveningValue.isChecked())
                {
                    Set<String> TueTasks = new HashSet<>(LoadSharedStrArray("EveTueTask", new HashSet<>(), "WeekT"));

                    TueTasks.add(newStringTask);

                    SaveSharedStrArray("EveTueTask", TueTasks, "WeekT");
                }

                daySelect = true;

                Toast toast = Toast.makeText(this, "Weekly task saved for Tuesday", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (WedValue)
            {
                if(morningValue.isChecked())
                {
                    Set<String> WedTasks = new HashSet<>(LoadSharedStrArray("MornWedTask", new HashSet<>(), "WeekT"));

                    WedTasks.add(newStringTask);

                    SaveSharedStrArray("MornWedTask", WedTasks, "WeekT");
                }
                else if(eveningValue.isChecked())
                {
                    Set<String> WedTasks = new HashSet<>(LoadSharedStrArray("EveWedTask", new HashSet<>(), "WeekT"));

                    WedTasks.add(newStringTask);

                    SaveSharedStrArray("EveWedTask", WedTasks, "WeekT");
                }

                daySelect = true;

                Toast toast = Toast.makeText(this, "Weekly task saved for Wednesday", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (ThrValue)
            {
                if(morningValue.isChecked())
                {
                    Set<String> ThrTasks = new HashSet<>(LoadSharedStrArray("MornThrTask", new HashSet<>(), "WeekT"));

                    ThrTasks.add(newStringTask);

                    SaveSharedStrArray("MornThrTask", ThrTasks, "WeekT");
                }
                else if(eveningValue.isChecked())
                {
                    Set<String> ThrTasks = new HashSet<>(LoadSharedStrArray("EveThrTask", new HashSet<>(), "WeekT"));

                    ThrTasks.add(newStringTask);

                    SaveSharedStrArray("EveThrTask", ThrTasks, "WeekT");
                }

                daySelect = true;

                Toast toast = Toast.makeText(this, "Weekly task saved for Thursday", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (FriValue)
            {
                if(morningValue.isChecked())
                {
                    Set<String> FriTasks = new HashSet<>(LoadSharedStrArray("MornFriTask", new HashSet<>(), "WeekT"));

                    FriTasks.add(newStringTask);

                    SaveSharedStrArray("MornFriTask", FriTasks, "WeekT");
                }
                else if(eveningValue.isChecked())
                {
                    Set<String> FriTasks = new HashSet<>(LoadSharedStrArray("EveFriTask", new HashSet<>(), "WeekT"));

                    FriTasks.add(newStringTask);

                    SaveSharedStrArray("EveFriTask", FriTasks, "WeekT");
                }

                daySelect = true;

                Toast toast = Toast.makeText(this, "Weekly task saved for Friday", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (SatValue)
            {
                if(morningValue.isChecked())
                {
                    Set<String> SatTasks = new HashSet<>(LoadSharedStrArray("MornSatTask", new HashSet<>(), "WeekT"));

                    SatTasks.add(newStringTask);

                    SaveSharedStrArray("MornSatTask", SatTasks, "WeekT");
                }
                else if(eveningValue.isChecked())
                {
                    Set<String> SatTasks = new HashSet<>(LoadSharedStrArray("EveSatTask", new HashSet<>(), "WeekT"));

                    SatTasks.add(newStringTask);

                    SaveSharedStrArray("EveSatTask", SatTasks, "WeekT");
                }

                daySelect = true;

                Toast toast = Toast.makeText(this, "Weekly task saved for Saturday", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (SunValue)
            {
                if(morningValue.isChecked())
                {
                    Set<String> SunTasks = new HashSet<>(LoadSharedStrArray("MornSunTask", new HashSet<>(), "WeekT"));

                    SunTasks.add(newStringTask);

                    SaveSharedStrArray("MornSunTask", SunTasks, "WeekT");
                }
                else if(eveningValue.isChecked())
                {
                    Set<String> SunTasks = new HashSet<>(LoadSharedStrArray("EveSunTask", new HashSet<>(), "WeekT"));

                    SunTasks.add(newStringTask);

                    SaveSharedStrArray("EveSunTask", SunTasks, "WeekT");
                }

                daySelect = true;

                Toast toast = Toast.makeText(this, "Weekly task saved for Sunday", Toast.LENGTH_SHORT);
                toast.show();
            }

            if(!daySelect)
            {
                Toast toast = Toast.makeText(this, "Please select day", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else
        {
            Toast toast = Toast.makeText(this, "Please select a task length", Toast.LENGTH_SHORT);
            toast.show();
        }
        //endregion
    }
    //endregion

    private void EditTasksSave(View view)
    {
        final TextView textEntry = findViewById(entryId);
        final CheckBox deleteTask = findViewById(deleteCheckboxID);

        String newText = textEntry.getText().toString();
        boolean deleteValue = deleteTask.isChecked();

        // Error catching
        if (newText.contains(",")) {
            Toast toast = Toast.makeText(this, "Please don't use commas (,)", Toast.LENGTH_SHORT);
            toast.show();

            return;
        }

        // New string arrays
        Set<String> newMorningArray = new HashSet<>();
        Set<String> newEveningArray = new HashSet<>();
        Set<String> newDailyArray = new HashSet<>();
        Set<String> newWeekArray = new HashSet<>();

        // Updated variable
        boolean update = false;

        //region MorningRoutineUpdating

        ArrayList<String> morningArrayHolder = new ArrayList<>();
        Collections.addAll(morningArrayHolder, arrayMorningRoutine);

        for (String counter : morningArrayHolder) {
            counter = counter.replace("[", "");
            counter = counter.replace("]", "");

            String[] entry = counter.split(",");

            if (Objects.equals(taskSpinTextSelected, entry[1])) {
                if (!deleteValue) {
                    String newEntry = entry[0] + "," + newText + "," + entry[2] + "," + entry[3];

                    newMorningArray.add(newEntry);
                }
                else
                {
                    boolean CheckBoxValue = LoadSharedBoolean(entry[0], false);

                    if(CheckBoxValue)
                    {
                        updateDayProgress(false);
                    }
                }

                update = true;

            } else {
                newMorningArray.add(counter);
            }
        }

        if (update) {
            SaveSharedStrArray("MorningRoutine", newMorningArray, "sharedPref");

            Toast toast = Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT);
            toast.show();

            EditTaskFragment(view);
        }
        //endregion

        //region EveningRoutineUpdating

        ArrayList<String> dayArrayHolder = new ArrayList<>();
        Collections.addAll(dayArrayHolder, arrayDayRoutine);

        for (String counter : dayArrayHolder) {
            counter = counter.replace("[", "");
            counter = counter.replace("]", "");

            String[] entry = counter.split(",");

            if (Objects.equals(taskSpinTextSelected, entry[1])) {
                if (!deleteValue) {
                    String newEntry = entry[0] + "," + newText + "," + entry[2] + "," + entry[3];

                    newEveningArray.add(newEntry);
                }
                else
                {
                    boolean CheckBoxValue = LoadSharedBoolean(entry[0], false);

                    if(CheckBoxValue)
                    {
                        updateDayProgress(false);
                    }
                }
                update = true;
            } else {
                newEveningArray.add(counter);
            }
        }

        if (update) {
            SaveSharedStrArray("DayRoutine", newEveningArray, "sharedPref");

            Toast toast = Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT);
            toast.show();

            EditTaskFragment(view);
        }
        //endregion

        //region DayRoutineUpdating

        ArrayList<String> newDailyArrayHolder = new ArrayList<>();
        arrayDailyRoutine = importDailyData();
        Collections.addAll(newDailyArrayHolder, arrayDailyRoutine);

        for (String counter : newDailyArrayHolder) {
            // Removes old array holders start + end
            counter = counter.replace("[", "");
            counter = counter.replace("]", "");

            String[] entry = counter.split(",");

            // If spinner string matches, string array passed
            if (Objects.equals(taskSpinTextSelected, entry[1])) {
                // Delete selected task
                if (!deleteValue) {
                    // Create new update entry
                    String newEntry = entry[0] + "," + newText + "," + entry[2] + "," + entry[3];

                    newDailyArray.add(newEntry);
                }
                else
                {
                    boolean CheckBoxValue = LoadSharedBoolean(entry[0], false);

                    if(CheckBoxValue)
                    {
                        updateDayProgress(false);
                    }
                }
                update = true;
            } else {
                newDailyArray.add(counter);
            }
        }

        if (update) {
            SaveSharedStrArray("DayTask", newDailyArray, "sharedPref");

            Toast toast = Toast.makeText(this, "Day task updated", Toast.LENGTH_SHORT);
            toast.show();

            EditTaskFragment(view);
        }
        //endregion

        //region WeeklyRoutineUpdating

        // Gets day
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        // Gets weekly repeatable tasks
        Set<String> currentDay = weekDay(day);

        // Convert to routine
        assert currentDay != null;
        String[] currentDayRoutine = currentDay.toArray(new String[0]);

        ArrayList<String> newWeekArrayHolder = new ArrayList<>();
        Collections.addAll(newWeekArrayHolder, currentDayRoutine);

        for (String counter : newWeekArrayHolder) {
            // Removes old array holders start + end
            counter = counter.replace("[", "");
            counter = counter.replace("]", "");

            String[] entry = counter.split(",");

            // If spinner string matches, string array passed
            if (Objects.equals(taskSpinTextSelected, entry[1])) {
                if (!deleteValue) {
                    // Create new update entry
                    String newEntry = entry[0] + "," + newText + "," + entry[2] + "," + entry[3];

                    newWeekArray.add(newEntry);
                }
                else
                {
                    // Removes checked value of task
                    boolean CheckBoxValue = LoadSharedBoolean(entry[0], false);

                    if(CheckBoxValue)
                    {
                        updateDayProgress(false);
                    }
                }
                update = true;
            } else {
                newWeekArray.add(counter);
            }
        }

        if (update) {
            String dayString = weekDaySaveName(day);

            SaveSharedStrArray(dayString, newWeekArray, "sharedPref");

            Toast toast = Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT);
            toast.show();

            EditTaskFragment(view);
        }
        //endregion
    }

    // General settings fragment save
    private void GeneralSettingsSave(View view)
    {
        final TextView themeText = findViewById(themeID);
        final CheckBox deleteValue = findViewById(deleteThemeID);
        final TextView hobbyText = findViewById(newHobbyID);

        boolean deleteOption = deleteValue.isChecked();
        String newTheme = themeText.getText().toString();
        String hobbyValue = hobbyText.getText().toString();

        // Update the theme
        if (!newTheme.equals("")) {
            String currentTheme = LoadSharedStr("currentTheme", "App of quotes");

            if (!currentTheme.equals(newTheme)) {
                SaveSharedStr("currentTheme", newTheme);

                Toast toast = Toast.makeText(this, "Theme updated", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        // Deletes the hobby
        if (deleteOption) {
            Set<String> newHobbyArray = new HashSet<>();

            Set<String> hobbyList = new HashSet<>(LoadSharedStrArray("HobbyOptions", new HashSet<>(), "sharedPref"));

            for (String hobbyItem : hobbyList) {
                if (!hobbyItem.equals(hobbySpinTextSelected)) {
                    newHobbyArray.add(hobbyItem);
                } else {
                    Toast toast = Toast.makeText(this, "Hobby deleted", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
            SaveSharedStrArray("HobbyOptions", newHobbyArray, "sharedPref");

            GeneralFragment(view);
        }

        // Adds the new hobby
        if (!hobbyValue.equals("")) {
            Set<String> hobbyList = new HashSet<>(LoadSharedStrArray("HobbyOptions", new HashSet<>(), "sharedPref"));

            Set<String> hobbyArray = new HashSet<>(hobbyList);

            hobbyArray.add(hobbyValue);

            SaveSharedStrArray("HobbyOptions", hobbyArray, "sharedPref");

            Toast toast = Toast.makeText(this, "new hobby added", Toast.LENGTH_SHORT);
            toast.show();

            GeneralFragment(view);
        }
    }

    // Updates total checked task if checked when deleted
    private void updateDayProgress(Boolean NewTask)
    {
        int TasksCompleted = LoadSharedInt("DailyProgression", 0);

        if(!NewTask) {
            TasksCompleted = TasksCompleted - 1;
        }

        SaveIntData("DailyProgression", TasksCompleted);
    }

    //region Fragment updating

    public void NewTaskFragment(View view) {
        FragmentManager NewTasks = getSupportFragmentManager();
        FragmentTransaction ft = NewTasks.beginTransaction();

        ft.replace(R.id.FragmentHolder, new NewTask(), "NewTask");
        ft.commit();
    }

    public void EditTaskFragment(View view){
        FragmentManager EditTasks = getSupportFragmentManager();
        FragmentTransaction ft = EditTasks.beginTransaction();

        ft.replace(R.id.FragmentHolder, new TaskDays(), "EditTask");
        ft.commit();
    }

    public void GeneralFragment(View view) {
        FragmentManager GeneralSettings = getSupportFragmentManager();
        FragmentTransaction ft = GeneralSettings.beginTransaction();

        ft.replace(R.id.FragmentHolder, new general_settings(), "GeneralSettings");
        ft.commit();
    }

    //endregion

    // Loads relevant day
    private Set<String> weekDay(int day)
    {
        switch (day)
        {
            case(1) :
                return new HashSet<>(LoadSharedStrArray("SunTask", new HashSet<>(), "sharedPref"));

            case(2) :
                return new HashSet<>(LoadSharedStrArray("MonTask", new HashSet<>(), "sharedPref"));

            case(3) :
                return new HashSet<>(LoadSharedStrArray("TueTask", new HashSet<>(), "sharedPref"));

            case(4) :
                return new HashSet<>(LoadSharedStrArray("WedTask", new HashSet<>(), "sharedPref"));

            case(5) :
                return new HashSet<>(LoadSharedStrArray("ThrTask", new HashSet<>(), "sharedPref"));

            case(6) :
                return new HashSet<>(LoadSharedStrArray("FriTask", new HashSet<>(), "sharedPref"));

            case(7) :
                return new HashSet<>(LoadSharedStrArray("SatTask", new HashSet<>(), "sharedPref"));
        }
        return null;
    }

    // Retries day task
    private String weekDaySaveName(int day)
    {
        switch (day)
        {
            case(1) :
                return "SunTask";

            case(2) :
                return "MonTask";

            case(3) :
                return "TueTask";

            case(4) :
                return "WedTask";

            case(5) :
                return "ThrTask";

            case(6) :
                return "FriTask";

            case(7) :
                return "SatTask";
        }
        return null;
    }

    // Random string generation
    private String RandomStringGeneration()
    {
        StringBuilder randomString = new StringBuilder();
        int topNumber = 27;
        int rand;

        int i = 0;
        while(i < 8)
        {
            i++;

            Random Random = new Random();
            rand = Random.nextInt(topNumber);

            switch (rand)
            {
                case(1):
                    randomString.append("a");
                    break;
                case(2):
                    randomString.append("b");
                    break;
                case(3):
                    randomString.append("c");
                    break;
                case(4):
                    randomString.append("d");
                    break;
                case(5):
                    randomString.append("e");
                    break;
                case(6):
                    randomString.append("f");
                    break;
                case(7):
                    randomString.append("g");
                    break;
                case(8):
                    randomString.append("h");
                    break;
                case(9):
                    randomString.append("i");
                    break;
                case(10):
                    randomString.append("j");
                    break;
                case(11):
                    randomString.append("k");
                    break;
                case(12):
                    randomString.append("l");
                    break;
                case(13):
                    randomString.append("m");
                    break;
                case(14):
                    randomString.append("n");
                    break;
                case(15):
                    randomString.append("o");
                    break;
                case(16):
                    randomString.append("p");
                    break;
                case(17):
                    randomString.append("q");
                    break;
                case(18):
                    randomString.append("r");
                    break;
                case(19):
                    randomString.append("s");
                    break;
                case(20):
                    randomString.append("t");
                    break;
                case(21):
                    randomString.append("u");
                    break;
                case(22):
                    randomString.append("v");
                    break;
                case(23):
                    randomString.append("w");
                    break;
                case(24):
                    randomString.append("x");
                    break;
                case(25):
                    randomString.append("y");
                    break;
                case(26):
                    randomString.append("z");
                    break;
                default:
                    randomString.append("U");
            }
        }
        return randomString.toString();
    }
}
