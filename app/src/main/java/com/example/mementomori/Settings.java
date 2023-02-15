package com.example.mementomori;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.os.Bundle;
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

    // Radio button IDs
    static int MonID = 2500;
    static int TueID = 2501;
    static int WedID = 2502;
    static int ThrID = 2503;
    static int FriID = 2504;
    static int SatID = 2505;
    static int SunID = 2506;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hides title action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_settings);

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
        Set<String> setMorningRoutine = LoadSharedStrArray("MorningRoutine", new HashSet<>());

        // Converts sets to string arrays
        arrayMorningRoutine = setMorningRoutine.toArray(new String[0]);

        return arrayMorningRoutine;
    }

    public String[] importDayData()
    {
        Set<String> setDayRoutine = LoadSharedStrArray("DayRoutine", new HashSet<>());

        arrayDayRoutine = setDayRoutine.toArray(new String[0]);

        return arrayDayRoutine;
    }

    public String[] importDailyData()
    {
        Set<String> setDayRoutine = LoadSharedStrArray("DayTask", new HashSet<>());

        arrayDailyRoutine = setDayRoutine.toArray(new String[0]);

        return arrayDailyRoutine;
    }

    public String[] importHobbies()
    {
        Set<String> setDayRoutine = LoadSharedStrArray("HobbyOptions", new HashSet<>());

        arrayHobby = setDayRoutine.toArray(new String[0]);

        return arrayHobby;
    }

    public String importTheme()
    {
        return LoadSharedStr("currentTheme", "Holder");
    }

    public String[] importTotalData()
    {
        // Loads tasks
        Set<String> setDailyRoutine = LoadSharedStrArray("DayTask", new HashSet<>());

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

        dailyP.setText(taskSpinTextSelected);
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
            EditTasksSave();
        }
        else if (newTask != null && newTask.isVisible())
        {
            newTaskCreation();
        }
        else if (genSettings != null && genSettings.isVisible())
        {
            GeneralSettingsSave();
        }
        else
        {
            Toast toast = Toast.makeText(this, "Nothing detected", Toast.LENGTH_LONG);
            toast.show();
        }
    }
    //endregion

    //region $executeFunctions

    private void newTaskCreation()
    {
        // Loads tasks
        Set<String> setMorningRoutine = LoadSharedStrArray("MorningRoutine", new HashSet<>());
        Set<String> setDayRoutine = LoadSharedStrArray("DayRoutine", new HashSet<>());

        String newStringTask;
        String newTaskNameID = "";

        int morningCount = (setMorningRoutine.size());
        int dayCount = (setDayRoutine.size());
        int totalCount = morningCount + dayCount;

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

        boolean permanentTaskValue = permanentTaskR.isChecked();
        boolean dayTaskValue = dayTaskR.isChecked();
        boolean tomorrowTaskValue = tomorrowTaskR.isChecked();
        boolean weeklyTaskValue = weeklyTaskR.isChecked();

        // Gets spare ID (if one is available)
        Set<String> SpareIDs = LoadSharedStrArray("SpareID", new HashSet<>());

        int nID = SpareIDs.size();

        String holderID = "";

        // Error catching
        if (TaskText.contains(",")) {
            Toast toast = Toast.makeText(this, "Please don't use commas (,)", Toast.LENGTH_LONG);
            toast.show();

            return;
        }

        //region settingTaskID
        if(nID > 0)
        {
            // Depending on API level one of which will run
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holderID = SpareIDs.stream().findFirst().get();
            }
            else
            {
                boolean firstID = false;
                for(String ID : SpareIDs)
                {
                    if(!firstID)
                    {
                        holderID = ID;
                        firstID = true;
                    }
                }
            }

            // Updates ID string
            HashSet<String> newSpareID = new HashSet<>();
            boolean firstID = true;

            for(String ID : SpareIDs)
            {
                if(firstID)
                {
                    firstID = false;
                }
                else
                {
                    newSpareID.add(ID);
                }
            }

            // Saves new spareID string
            if(nID < 2)
            {
                SaveSharedStrArray("SpareID", new HashSet<>());
            }
            else
            {
                SaveSharedStrArray("SpareID", newSpareID);
            }
        }
        else
        {
            newTaskNameID = RandomStringGeneration();
            Toast toast = Toast.makeText(this, "new ID system = " + newTaskNameID, Toast.LENGTH_SHORT);
            toast.show();
        }

        //endregion

        // Checks if their is a old ID which can be used
        if(!holderID.equals(""))
        {
            totalCount = Integer.parseInt(holderID);
        }
        else
        {
            totalCount = totalCount + 1;
        }

        // Sets new task values
        if (hobbyValue) {
            newStringTask = newTaskNameID + ",hobbies,100," + totalCount;
        } else {
            newStringTask = newTaskNameID + "," + TaskText + "," + "0" + "," + totalCount;
        }

        //region TaskLengthConfiguring
        if(permanentTaskValue)
        {
            if(eveningValue.isChecked()) {

                Set<String> newEveningArray = LoadSharedStrArray("DayRoutine", new HashSet<>());

                for (String counter : setDayRoutine) {
                    counter = counter.replace("[", "");
                    counter = counter.replace("]", "");

                    String[] entry = counter.split(",");

                    if (Objects.equals(newTaskNameID, entry[0])) {
                        Toast toast = Toast.makeText(this, "saveID is already taken please try again", Toast.LENGTH_LONG);
                        toast.show();

                        return;
                    }
                }
                newEveningArray.add(newStringTask);

                SaveSharedStrArray("DayRoutine", newEveningArray);

                Toast toast = Toast.makeText(this, "New task added", Toast.LENGTH_LONG);
                toast.show();
            }
            else if(morningValue.isChecked())
            {
                Set<String> newMorningArray = LoadSharedStrArray("MorningRoutine", new HashSet<>());

                for(String counter : setMorningRoutine)
                {
                    counter = counter.replace("[", "");
                    counter = counter.replace("]", "");

                    String[] entry = counter.split(",");

                    if (Objects.equals(newTaskNameID, entry[0])) {
                        Toast toast = Toast.makeText(this, "saveID is already taken please try again", Toast.LENGTH_LONG);
                        toast.show();

                        return;
                    }
                }
                newMorningArray.add(newStringTask);

                SaveSharedStrArray("MorningRoutine", newMorningArray);

                Toast toast = Toast.makeText(this, "New permanent task added", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        else if (dayTaskValue)
        {
            Set<String> dayTask = LoadSharedStrArray("DayTask", new HashSet<>());

            dayTask.add(newStringTask);

            SaveSharedStrArray("DayTask", dayTask);

            Toast toast = Toast.makeText(this, "New one day task added", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (tomorrowTaskValue)
        {
            Set<String> tomorrowTask = LoadSharedStrArray("TomorrowTask", new HashSet<>());

            tomorrowTask.add(newStringTask);

            SaveSharedStrArray("TomorrowTask", tomorrowTask);

            Toast toast = Toast.makeText(this, "Task added for tomorrow", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if (weeklyTaskValue)
        {
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
                Set<String> MonTasks = LoadSharedStrArray("MonTask", new HashSet<>());

                MonTasks.add(newStringTask);

                SaveSharedStrArray("MonTask", MonTasks);

                Toast toast = Toast.makeText(this, "Weekly task saved for Monday", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (TueValue)
            {
                Set<String> MonTasks = LoadSharedStrArray("TueTask", new HashSet<>());

                MonTasks.add(newStringTask);

                SaveSharedStrArray("TueTask", MonTasks);

                Toast toast = Toast.makeText(this, "Weekly task saved for Tuesday", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (WedValue)
            {
                Set<String> MonTasks = LoadSharedStrArray("WedTask", new HashSet<>());

                MonTasks.add(newStringTask);

                SaveSharedStrArray("WedTask", MonTasks);

                Toast toast = Toast.makeText(this, "Weekly task saved for Wednesday", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (ThrValue)
            {
                Set<String> MonTasks = LoadSharedStrArray("ThrTask", new HashSet<>());

                MonTasks.add(newStringTask);

                SaveSharedStrArray("ThrTask", MonTasks);

                Toast toast = Toast.makeText(this, "Weekly task saved for Thursday", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (FriValue)
            {
                Set<String> MonTasks = LoadSharedStrArray("FriTask", new HashSet<>());

                MonTasks.add(newStringTask);

                SaveSharedStrArray("FriTask", MonTasks);

                Toast toast = Toast.makeText(this, "Weekly task saved for Friday", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (SatValue)
            {
                Set<String> MonTasks = LoadSharedStrArray("SatTask", new HashSet<>());

                MonTasks.add(newStringTask);

                SaveSharedStrArray("SatTask", MonTasks);

                Toast toast = Toast.makeText(this, "Weekly task saved for Saturday", Toast.LENGTH_SHORT);
                toast.show();
            }
            if (SunValue)
            {
                Set<String> MonTasks = LoadSharedStrArray("SunTask", new HashSet<>());

                MonTasks.add(newStringTask);

                SaveSharedStrArray("SunTask", MonTasks);

                Toast toast = Toast.makeText(this, "Weekly task saved for Sunday", Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                Toast toast = Toast.makeText(this, "Please select day", Toast.LENGTH_LONG);
                toast.show();
            }
        }
        else
        {
            Toast toast = Toast.makeText(this, "Please select a task length", Toast.LENGTH_LONG);
            toast.show();
        }
        //endregion
    }
    //endregion

    private void EditTasksSave()
    {
        final TextView textEntry = findViewById(entryId);
        final CheckBox deleteTask = findViewById(deleteCheckboxID);

        String newText = textEntry.getText().toString();
        boolean deleteValue = deleteTask.isChecked();

        // Error catching
        if (newText.contains(",")) {
            Toast toast = Toast.makeText(this, "Please don't use commas (,)", Toast.LENGTH_LONG);
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
                    SharedPreferences StringSharedPref = getSharedPreferences(entry[0], MODE_PRIVATE);
                    StringSharedPref.edit().clear().apply();

                    Set<String> IDHolder = new HashSet<>();

                    Set<String> SpareID = LoadSharedStrArray("SpareID", new HashSet<>());

                    int totalID = SpareID.size();

                    if(totalID > 0)
                    {
                        IDHolder.addAll(SpareID);
                    }

                    IDHolder.add(entry[3]);

                    SaveSharedStrArray("SpareID", IDHolder);

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
            SaveSharedStrArray("MorningRoutine", newMorningArray);

            Toast toast = Toast.makeText(this, "Task updated", Toast.LENGTH_LONG);
            toast.show();

            return;
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
                    SharedPreferences StringSharedPref = getSharedPreferences(entry[0], MODE_PRIVATE);
                    StringSharedPref.edit().clear().apply();

                    Set<String> IDHolder = new HashSet<>();

                    Set<String> SpareID = LoadSharedStrArray("SpareID", new HashSet<>());

                    int totalID = SpareID.size();

                    if(totalID > 0)
                    {
                        IDHolder.addAll(SpareID);
                    }

                    IDHolder.add(entry[3]);

                    SaveSharedStrArray("SpareID", IDHolder);

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
            SaveSharedStrArray("DayRoutine", newEveningArray);

            Toast toast = Toast.makeText(this, "Task updated", Toast.LENGTH_LONG);
            toast.show();

            return;
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
                    SharedPreferences StringSharedPref = getSharedPreferences(entry[0], MODE_PRIVATE);
                    StringSharedPref.edit().clear().apply();

                    Set<String> IDHolder = new HashSet<>();

                    Set<String> SpareID = LoadSharedStrArray("SpareID", new HashSet<>());

                    int totalID = SpareID.size();

                    if(totalID > 0)
                    {
                        IDHolder.addAll(SpareID);
                    }

                    IDHolder.add(entry[3]);

                    SaveSharedStrArray("SpareID", IDHolder);

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
            SaveSharedStrArray("DayTask", newDailyArray);

            Toast toast = Toast.makeText(this, "Day task updated", Toast.LENGTH_LONG);
            toast.show();

            return;
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
                    SharedPreferences StringSharedPref = getSharedPreferences(entry[0], MODE_PRIVATE);
                    StringSharedPref.edit().clear().apply();

                    // Saves task deleted ID to be used in the future
                    Set<String> IDHolder = new HashSet<>();

                    Set<String> SpareID = LoadSharedStrArray("SpareID", new HashSet<>());

                    int totalID = SpareID.size();

                    if(totalID > 0)
                    {
                        IDHolder.addAll(SpareID);
                    }

                    IDHolder.add(entry[3]);

                    SaveSharedStrArray("SpareID", IDHolder);

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

            SaveSharedStrArray(dayString, newWeekArray);

            Toast toast = Toast.makeText(this, "Task updated", Toast.LENGTH_LONG);
            toast.show();
        }
        //endregion
    }

    // General settings fragment save
    private void GeneralSettingsSave()
    {
        final TextView themeText = findViewById(themeID);
        final CheckBox deleteValue = findViewById(deleteThemeID);
        final TextView hobbyText = findViewById(newHobbyID);

        boolean deleteOption = deleteValue.isChecked();
        String newTheme = themeText.getText().toString();
        String hobbyValue = hobbyText.getText().toString();

        // Update the theme
        if (!newTheme.equals("")) {
            String currentTheme = LoadSharedStr("currentTheme", newTheme);

            if (!currentTheme.equals(newTheme)) {
                SaveSharedStr("currentTheme", newTheme);

                Toast toast = Toast.makeText(this, "Theme updated", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        // Deletes the hobby
        if (deleteOption) {
            Set<String> newHobbyArray = new HashSet<>();

            Set<String> hobbyList = LoadSharedStrArray("HobbyOptions", new HashSet<>());

            for (String hobbyItem : hobbyList) {
                if (!hobbyItem.equals(hobbySpinTextSelected)) {
                    newHobbyArray.add(hobbyItem);
                } else {
                    Toast toast = Toast.makeText(this, "Hobby deleted", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
            SaveSharedStrArray("HobbyOptions", newHobbyArray);
            return;
        }

        // Adds the new hobby
        if (!hobbyValue.equals("")) {
            Set<String> hobbyList = LoadSharedStrArray("HobbyOptions", new HashSet<>());

            Set<String> hobbyArray = new HashSet<>(hobbyList);

            hobbyArray.add(hobbyValue);

            SaveSharedStrArray("HobbyOptions", hobbyArray);

            Toast toast = Toast.makeText(this, "new hobby added", Toast.LENGTH_LONG);
            toast.show();
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
        FragmentManager EditTasks = getSupportFragmentManager();
        FragmentTransaction ft = EditTasks.beginTransaction();

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
                return LoadSharedStrArray("SunTask", new HashSet<>());

            case(2) :
                return LoadSharedStrArray("MonTask", new HashSet<>());

            case(3) :
                return LoadSharedStrArray("TueTask", new HashSet<>());

            case(4) :
                return LoadSharedStrArray("WedTask", new HashSet<>());

            case(5) :
                return LoadSharedStrArray("ThrTask", new HashSet<>());

            case(6) :
                return LoadSharedStrArray("FriTask", new HashSet<>());

            case(7) :
                return LoadSharedStrArray("SatTask", new HashSet<>());
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
