package com.example.mementomori;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NewTask extends Fragment {

    static int entryId = 1500;
    static int saveTaskId = 1001;
    static int hobbyTask = 1000;

    static int eveningToggle = 9997;
    static int morningToggle = 9996;

    static int permanentTask = 2000;
    static int dayTask = 2001;
    static int tomorrowTask = 2002;
    static int weeklyTask = 2003;
    static int oneDayTask = 2004;

    static int DayName = 1999;

    static int MonID = 2500;
    static int TueID = 2501;
    static int WedID = 2502;
    static int ThrID = 2503;
    static int FriID = 2504;
    static int SatID = 2505;
    static int SunID = 2506;

    static int taskLengthGroupOneID = 2560;
    static int taskLengthGroupTwoID = 2561;
    static int dayGroupID = 2562;
    RadioGroup taskLengthGroupTwo, taskLengthGroupOne, dayGroupOne, dayGroupTwo;

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_days, container, false);

        // Import json array
        Settings activity = (Settings) getActivity();

        // width
        assert activity != null;
        int width = activity.width;

        // Gets current theme
        int textColor = activity.textColor;
        int backgroundColor = activity.boxBackgroundColor;
        ColorStateList colorStateList = activity.colorStateList;

        // sets row style
        TableRow.LayoutParams rowPram = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

        TableLayout tableLayout = view.findViewById(R.id.tableFragment);
        // Calling in a layout by ID caused a error which could not be fixed
        TableRow newTaskTitle = new TableRow(activity);
        TableRow newTaskEntry = new TableRow(activity);

        // Edit task
        TextView editText = new TextView(activity);
        editText.setTextColor(textColor);
        editText.setTextSize(15);
        editText.setText(R.string.taskName);

        EditText editEntry = new EditText(activity);
        editEntry.setId(entryId);
        editEntry.setTextColor(textColor);
        editEntry.setBackgroundColor(backgroundColor);
        editEntry.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        editEntry.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editEntry.setLines(3);
        // set 40 char limit (about 3 lines)
        editEntry.setFilters(new InputFilter[] {new InputFilter.LengthFilter(100) });
        //editEntry.setLayoutParams(spanPrams);

        // apply
        newTaskTitle.addView(editText);
        newTaskEntry.addView(editEntry);

        TableRow.LayoutParams spanPrams = (TableRow.LayoutParams) editText.getLayoutParams();
        spanPrams.span = 2;
        editEntry.setLayoutParams(spanPrams);

        editEntry.setWidth(width);

        TableRow EditTextRow = new TableRow(activity);
        EditTextRow.setLayoutParams(rowPram);

        CheckBox dynamicToggle = new CheckBox(activity);
        dynamicToggle.setId(hobbyTask);
        dynamicToggle.setText(R.string.hobbyTask);
        dynamicToggle.setTextColor(textColor);
        CompoundButtonCompat.setButtonTintList(dynamicToggle, colorStateList);
        //dynamicToggle.setBackgroundColor();
        // dynamicToggle.setButtonTintList();

        // apply
        EditTextRow.addView(dynamicToggle);

        TableRow TaskTypeRow = new TableRow(activity);
        TaskTypeRow.setLayoutParams(rowPram);

        TextView selectTaskType = new TextView(activity);
        selectTaskType.setText(R.string.taskType);
        selectTaskType.setPadding(0,10,0,10);
        selectTaskType.setTextColor(textColor);

        RadioGroup taskToggles = new RadioGroup(activity);
        taskToggles.setOrientation(LinearLayout.VERTICAL);

        RadioButton MorningToggle = new RadioButton(activity);
        MorningToggle.setId(morningToggle);
        MorningToggle.setText(R.string.r_mor);
        CompoundButtonCompat.setButtonTintList(MorningToggle, colorStateList);
        MorningToggle.setTextColor(textColor);

        RadioButton EveningToggle = new RadioButton(activity);
        EveningToggle.setId(eveningToggle);
        EveningToggle.setChecked(true);
        EveningToggle.setText(R.string.r_dai);
        CompoundButtonCompat.setButtonTintList(EveningToggle, colorStateList);
        EveningToggle.setTextColor(textColor);

        // Apply
        taskToggles.addView(MorningToggle);
        taskToggles.addView(EveningToggle);

        TaskTypeRow.addView(selectTaskType);
        TaskTypeRow.addView(taskToggles);

        TextView taskLength = new TextView(activity);
        taskLength.setText(R.string.taskLength);
        taskLength.setPadding(0,10,0,10);
        taskLength.setTextColor(textColor);

        // Radio group for tasks
        RadioGroup taskDayGroup = new RadioGroup(activity);
        taskDayGroup.setOrientation(LinearLayout.VERTICAL);

        // PermanentTag
        TextView PermanentTag = new TextView(activity);
        PermanentTag.setText(R.string.taskPermanent);
        PermanentTag.setTextColor(textColor);

        // Permanent
        RadioButton permanentTaskRadio = new RadioButton(activity);
        permanentTaskRadio.setText(R.string.taskPermanent);
        permanentTaskRadio.setId(permanentTask);
        permanentTaskRadio.setTextColor(textColor);
        CompoundButtonCompat.setButtonTintList(permanentTaskRadio, colorStateList);

        // Select day/s will appear when selected
        RadioButton weeklyTaskRadio = new RadioButton(activity);
        weeklyTaskRadio.setId(weeklyTask);
        weeklyTaskRadio.setText(R.string.taskWeekly);
        weeklyTaskRadio.setTextColor(textColor);
        CompoundButtonCompat.setButtonTintList(weeklyTaskRadio, colorStateList);

        // Temporary text view
        TextView TemporaryTag = new TextView(activity);
        TemporaryTag.setText(R.string.taskLengthOneDay);
        TemporaryTag.setTextColor(textColor);

        // Today option
        RadioButton onDayTaskRadio = new RadioButton(activity);
        onDayTaskRadio.setId(dayTask);
        onDayTaskRadio.setText(R.string.taskToday);
        onDayTaskRadio.setTextColor(textColor);
        CompoundButtonCompat.setButtonTintList(onDayTaskRadio, colorStateList);

        // Tomorrow one day option
        RadioButton tomorrowTaskRadio = new RadioButton(activity);
        tomorrowTaskRadio.setId(tomorrowTask);
        tomorrowTaskRadio.setText(R.string.taskTomorrow);
        tomorrowTaskRadio.setTextColor(textColor);
        CompoundButtonCompat.setButtonTintList(tomorrowTaskRadio, colorStateList);

        // One day task
        RadioButton oneDayRadio = new RadioButton(activity);
        oneDayRadio.setId(oneDayTask);
        oneDayRadio.setText(R.string.taskOneDay);
        oneDayRadio.setTextColor(textColor);
        CompoundButtonCompat.setButtonTintList(oneDayRadio, colorStateList);

        taskLengthGroupOne = new RadioGroup(activity);
        taskLengthGroupOne.setOrientation(LinearLayout.VERTICAL);
        taskLengthGroupOne.setId(taskLengthGroupOneID);

        taskLengthGroupTwo = new RadioGroup(activity);
        taskLengthGroupTwo.setOrientation(LinearLayout.VERTICAL);
        taskLengthGroupTwo.setId(taskLengthGroupTwoID);

        // Adds radio buttons to radio group
        taskLengthGroupOne.addView(PermanentTag);
        taskLengthGroupOne.addView(permanentTaskRadio);
        taskLengthGroupOne.addView(weeklyTaskRadio);

        taskLengthGroupTwo.addView(TemporaryTag);
        taskLengthGroupTwo.addView(onDayTaskRadio);
        taskLengthGroupTwo.addView(tomorrowTaskRadio);
        taskLengthGroupTwo.addView(oneDayRadio);

        // Sets on change listener
        taskLengthGroupOne.setOnCheckedChangeListener(dayGroupOneListener);
        taskLengthGroupTwo.setOnCheckedChangeListener(dayGroupTwoListener);

        TableLayout taskLengthTable = new TableLayout(activity);

        TableRow taskLengthRow = new TableRow(activity);

        taskLengthRow.addView(taskLengthGroupOne);
        taskLengthRow.addView(taskLengthGroupTwo);

        taskLengthTable.addView(taskLengthRow);

        TextView textDays = new TextView(activity);
        textDays.setText(R.string.textDay);
        textDays.setId(DayName);
        textDays.setPadding(0,10,0,10);
        textDays.setTextColor(textColor);
        // textDays.setVisibility(View.INVISIBLE);

        CheckBox Mon = new CheckBox(activity);
        Mon.setId(MonID);
        Mon.setText(R.string.Mon);
        Mon.setVisibility(View.INVISIBLE);
        Mon.setTextColor(textColor);
        CompoundButtonCompat.setButtonTintList(Mon, colorStateList);
        CheckBox Tue = new CheckBox(activity);
        Tue.setText(R.string.Tue);
        Tue.setId(TueID);
        Tue.setVisibility(View.INVISIBLE);
        Tue.setTextColor(textColor);
        CompoundButtonCompat.setButtonTintList(Tue, colorStateList);
        CheckBox Wed = new CheckBox(activity);
        Wed.setText(R.string.Wed);
        Wed.setId(WedID);
        Wed.setVisibility(View.INVISIBLE);
        Wed.setTextColor(textColor);
        CompoundButtonCompat.setButtonTintList(Wed, colorStateList);
        CheckBox Thr = new CheckBox(activity);
        Thr.setText(R.string.Thr);
        Thr.setId(ThrID);
        Thr.setVisibility(View.INVISIBLE);
        Thr.setTextColor(textColor);
        CompoundButtonCompat.setButtonTintList(Thr, colorStateList);
        CheckBox Fri = new CheckBox(activity);
        Fri.setText(R.string.Fri);
        Fri.setId(FriID);
        Fri.setVisibility(View.INVISIBLE);
        Fri.setTextColor(textColor);
        CompoundButtonCompat.setButtonTintList(Fri, colorStateList);
        CheckBox Sat = new CheckBox(activity);
        Sat.setText(R.string.Sat);
        Sat.setId(SatID);
        Sat.setVisibility(View.INVISIBLE);
        Sat.setTextColor(textColor);
        CompoundButtonCompat.setButtonTintList(Sat, colorStateList);
        CheckBox Sun = new CheckBox(activity);
        Sun.setText(R.string.Sun);
        Sun.setId(SunID);
        Sun.setVisibility(View.INVISIBLE);
        Sun.setTextColor(textColor);
        CompoundButtonCompat.setButtonTintList(Sun, colorStateList);

        // Adds checkboxes to button group
        dayGroupOne = new RadioGroup(activity);
        dayGroupOne.setOrientation(LinearLayout.VERTICAL);
        dayGroupOne.setId(dayGroupID);

        dayGroupTwo = new RadioGroup(activity);
        dayGroupTwo.setOrientation(LinearLayout.VERTICAL);
        dayGroupTwo.setId(dayGroupID);

        dayGroupOne.addView(Mon);
        dayGroupTwo.addView(Tue);
        dayGroupOne.addView(Wed);
        dayGroupTwo.addView(Thr);
        dayGroupOne.addView(Fri);
        dayGroupTwo.addView(Sat);
        dayGroupOne.addView(Sun);

        TableRow daySelection = new TableRow(activity);

        daySelection.addView(dayGroupOne);
        daySelection.addView(dayGroupTwo);

        // Apply rows
        tableLayout.addView(newTaskTitle);
        tableLayout.addView(newTaskEntry);
        tableLayout.addView(EditTextRow);
        tableLayout.addView(TaskTypeRow);
        tableLayout.addView(taskLength);
        tableLayout.addView(taskLengthTable);
        tableLayout.addView(textDays);
        tableLayout.addView(daySelection);

        return view;
    }

    private RadioGroup.OnCheckedChangeListener dayGroupOneListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                taskLengthGroupTwo.setOnClickListener(null);
                taskLengthGroupTwo.clearCheck();
                taskLengthGroupTwo.setOnCheckedChangeListener(dayGroupTwoListener);
                taskLengthGroupOne.check(checkedId);

                //TextView textDays = activity.findViewById(DayName);

                CheckBox MonId = dayGroupOne.findViewById(MonID);
                CheckBox TueId = dayGroupTwo.findViewById(TueID);
                CheckBox WedId = dayGroupOne.findViewById(WedID);
                CheckBox ThrId = dayGroupTwo.findViewById(ThrID);
                CheckBox FriId = dayGroupOne.findViewById(FriID);
                CheckBox SatId = dayGroupTwo.findViewById(SatID);
                CheckBox SunId = dayGroupOne.findViewById(SunID);


                if (checkedId == weeklyTask) {
                    //textDays.setVisibility(View.VISIBLE);

                    MonId.setVisibility(View.VISIBLE);
                    TueId.setVisibility(View.VISIBLE);
                    WedId.setVisibility(View.VISIBLE);
                    ThrId.setVisibility(View.VISIBLE);
                    FriId.setVisibility(View.VISIBLE);
                    SatId.setVisibility(View.VISIBLE);
                    SunId.setVisibility(View.VISIBLE);
                }
                else
                {
                    MonId.setVisibility(View.INVISIBLE);
                    TueId.setVisibility(View.INVISIBLE);
                    WedId.setVisibility(View.INVISIBLE);
                    ThrId.setVisibility(View.INVISIBLE);
                    FriId.setVisibility(View.INVISIBLE);
                    SatId.setVisibility(View.INVISIBLE);
                    SunId.setVisibility(View.INVISIBLE);
                }
            }
        }
    };


    private RadioGroup.OnCheckedChangeListener dayGroupTwoListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                taskLengthGroupOne.setOnClickListener(null);
                taskLengthGroupOne.clearCheck();
                taskLengthGroupOne.setOnCheckedChangeListener(dayGroupOneListener);
                taskLengthGroupTwo.check(checkedId);

                // TextView textDays = activity.findViewById(DayName);

                CheckBox MonId = dayGroupOne.findViewById(MonID);
                CheckBox TueId = dayGroupTwo.findViewById(TueID);
                CheckBox WedId = dayGroupOne.findViewById(WedID);
                CheckBox ThrId = dayGroupTwo.findViewById(ThrID);
                CheckBox FriId = dayGroupOne.findViewById(FriID);
                CheckBox SatId = dayGroupTwo.findViewById(SatID);
                CheckBox SunId = dayGroupOne.findViewById(SunID);

                if(checkedId == oneDayTask) {
                    //textDays.setVisibility(View.INVISIBLE);

                    MonId.setVisibility(View.VISIBLE);
                    TueId.setVisibility(View.VISIBLE);
                    WedId.setVisibility(View.VISIBLE);
                    ThrId.setVisibility(View.VISIBLE);
                    FriId.setVisibility(View.VISIBLE);
                    SatId.setVisibility(View.VISIBLE);
                    SunId.setVisibility(View.VISIBLE);
                }
                else
                {
                    MonId.setVisibility(View.INVISIBLE);
                    TueId.setVisibility(View.INVISIBLE);
                    WedId.setVisibility(View.INVISIBLE);
                    ThrId.setVisibility(View.INVISIBLE);
                    FriId.setVisibility(View.INVISIBLE);
                    SatId.setVisibility(View.INVISIBLE);
                    SunId.setVisibility(View.INVISIBLE);
                }
            }
        }
    };
}