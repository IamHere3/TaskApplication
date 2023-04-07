package com.example.mementomori;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputFilter;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
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

    static int dayGroupOneID = 2550;
    static int dayGroupTwoID = 2551;
    static int taskLengthGroupOneID = 2560;
    static int taskLengthGroupTwoID = 2561;

    RadioGroup dayGroupTwo, dayGroupOne;
    RadioGroup taskLengthGroupTwo, taskLengthGroupOne;

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

        TableRow.LayoutParams rowPram = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

        TableLayout tableLayout = view.findViewById(R.id.tableFragment);
        // Calling in a layout by ID caused a error which could not be fixed
        TableRow BaseLayout = new TableRow(activity);

        // Edit task
        TextView editText = new TextView(activity);
        editText.setText(R.string.taskName);

        EditText editEntry = new EditText(activity);
        editEntry.setId(entryId);
        editEntry.setSingleLine(false);
        editEntry.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        editEntry.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editEntry.setLines(2);

        // apply
        BaseLayout.addView(editText);
        BaseLayout.addView(editEntry);

        TableRow selectDynamicTaskRow = new TableRow(activity);
        selectDynamicTaskRow.setLayoutParams(rowPram);

        TextView selectTaskTitle = new TextView(activity);
        selectTaskTitle.setText(R.string.hobbyCheckBox);
        selectTaskTitle.setTextColor(getResources().getColor(R.color.white));

        CheckBox dynamicToggle = new CheckBox(activity);
        dynamicToggle.setId(hobbyTask);
        dynamicToggle.setText(R.string.hobbyTask);
        dynamicToggle.setTextColor(getResources().getColor(R.color.white));

        // apply
        selectDynamicTaskRow.addView(selectTaskTitle);
        selectDynamicTaskRow.addView(dynamicToggle);

        TableRow TaskTypeRow = new TableRow(activity);
        TaskTypeRow.setLayoutParams(rowPram);

        TextView selectTaskType = new TextView(activity);
        selectTaskType.setText(R.string.taskType);
        selectTaskType.setPadding(0,10,0,10);
        selectTaskType.setTextColor(getResources().getColor(R.color.white));

        RadioGroup taskToggles = new RadioGroup(activity);
        taskToggles.setOrientation(LinearLayout.VERTICAL);

        RadioButton MorningToggle = new RadioButton(activity);
        MorningToggle.setId(morningToggle);
        MorningToggle.setText(R.string.r_mor);
        MorningToggle.setTextColor(getResources().getColor(R.color.white));

        RadioButton EveningToggle = new RadioButton(activity);
        EveningToggle.setId(eveningToggle);
        EveningToggle.setChecked(true);
        EveningToggle.setText(R.string.r_dai);
        EveningToggle.setTextColor(getResources().getColor(R.color.white));

        // Apply
        taskToggles.addView(MorningToggle);
        taskToggles.addView(EveningToggle);

        TaskTypeRow.addView(selectTaskType);
        TaskTypeRow.addView(taskToggles);

        TextView taskLength = new TextView(activity);
        taskLength.setText(R.string.taskLength);
        taskLength.setPadding(0,10,0,10);
        taskLength.setTextColor(getResources().getColor(R.color.white));

        // Radio group for tasks
        RadioGroup taskDayGroup = new RadioGroup(activity);
        taskDayGroup.setOrientation(LinearLayout.VERTICAL);

        // PermanentTag
        TextView PermanentTag = new TextView(activity);
        PermanentTag.setText(R.string.taskPermanent);
        PermanentTag.setTextColor(getResources().getColor(R.color.white));

        // Permanent
        RadioButton permanentTaskRadio = new RadioButton(activity);
        permanentTaskRadio.setText(R.string.taskPermanent);
        permanentTaskRadio.setId(permanentTask);
        permanentTaskRadio.setTextColor(getResources().getColor(R.color.white));

        // Once day a week - popup? set day
        RadioButton weeklyTaskRadio = new RadioButton(activity);
        weeklyTaskRadio.setId(weeklyTask);
        weeklyTaskRadio.setText(R.string.taskWeekly);
        weeklyTaskRadio.setTextColor(getResources().getColor(R.color.white));

        // Temporary text view
        TextView TemporaryTag = new TextView(activity);
        TemporaryTag.setText(R.string.taskLengthOneDay);
        TemporaryTag.setTextColor(getResources().getColor(R.color.white));

        // Today option
        RadioButton onDayTaskRadio = new RadioButton(activity);
        onDayTaskRadio.setId(dayTask);
        onDayTaskRadio.setText(R.string.taskToday);
        onDayTaskRadio.setTextColor(getResources().getColor(R.color.white));

        // Tomorrow one day option
        RadioButton tomorrowTaskRadio = new RadioButton(activity);
        tomorrowTaskRadio.setId(tomorrowTask);
        tomorrowTaskRadio.setText(R.string.taskTomorrow);
        tomorrowTaskRadio.setTextColor(getResources().getColor(R.color.white));

        // One day task
        RadioButton oneDayRadio = new RadioButton(activity);
        oneDayRadio.setId(oneDayTask);
        oneDayRadio.setText(R.string.taskOneDay);
        oneDayRadio.setTextColor(getResources().getColor(R.color.white));

        taskLengthGroupOne = new RadioGroup(activity);
        taskLengthGroupOne.setOrientation(LinearLayout.VERTICAL);
        taskLengthGroupOne.setId(taskLengthGroupOneID);

        taskLengthGroupTwo = new RadioGroup(activity);
        taskLengthGroupTwo.setOrientation(LinearLayout.VERTICAL);
        taskLengthGroupTwo.setId(taskLengthGroupTwoID);

        // apply radio buttons to radio group
        taskLengthGroupOne.addView(PermanentTag);
        taskLengthGroupOne.addView(permanentTaskRadio);
        taskLengthGroupOne.addView(weeklyTaskRadio);

        taskLengthGroupTwo.addView(TemporaryTag);
        taskLengthGroupTwo.addView(onDayTaskRadio);
        taskLengthGroupTwo.addView(tomorrowTaskRadio);
        taskLengthGroupTwo.addView(oneDayRadio);

        // sets on change listener
        taskLengthGroupOne.setOnCheckedChangeListener(taskLengthGroupOneListener);
        taskLengthGroupTwo.setOnCheckedChangeListener(taskLengthGroupTwoListener);

        TableLayout taskLengthTable = new TableLayout(activity);

        TableRow taskLengthRow = new TableRow(activity);

        taskLengthRow.addView(taskLengthGroupOne);
        taskLengthRow.addView(taskLengthGroupTwo);

        taskLengthTable.addView(taskLengthRow);

        TextView textDays = new TextView(activity);
        textDays.setText(R.string.textDay);
        textDays.setId(DayName);
        textDays.setPadding(0,10,0,10);
        textDays.setTextColor(getResources().getColor(R.color.white));
        // textDays.setVisibility(View.INVISIBLE);

        CheckBox Mon = new CheckBox(activity);
        Mon.setId(MonID);
        Mon.setText(R.string.Mon);
        //Mon.setVisibility(View.INVISIBLE);
        CheckBox Tue = new CheckBox(activity);
        Tue.setText(R.string.Tue);
        Tue.setId(TueID);
        //Tue.setVisibility(View.INVISIBLE);
        CheckBox Wed = new CheckBox(activity);
        Wed.setText(R.string.Wed);
        Wed.setId(WedID);
        //Wed.setVisibility(View.INVISIBLE);
        CheckBox Thr = new CheckBox(activity);
        Thr.setText(R.string.Thr);
        Thr.setId(ThrID);
        //Thr.setVisibility(View.INVISIBLE);
        CheckBox Fri = new CheckBox(activity);
        Fri.setText(R.string.Fri);
        Fri.setId(FriID);
        //Fri.setVisibility(View.INVISIBLE);
        CheckBox Sat = new CheckBox(activity);
        Sat.setText(R.string.Sat);
        Sat.setId(SatID);
        //Sat.setVisibility(View.INVISIBLE);
        CheckBox Sun = new CheckBox(activity);
        Sun.setText(R.string.Sun);
        Sun.setId(SunID);
        //Sun.setVisibility(View.INVISIBLE);

        TableRow DaysOne = new TableRow(activity);

        DaysOne.addView(Mon);
        DaysOne.addView(Tue);

        TableRow DaysTwo = new TableRow(activity);

        DaysTwo.addView(Wed);
        DaysTwo.addView(Thr);

        TableRow DaysThree = new TableRow(activity);

        DaysThree.addView(Fri);
        DaysThree.addView(Sat);

        TableRow DaysFour = new TableRow(activity);

        DaysFour.addView(Sun);

        // apply rows
        tableLayout.addView(BaseLayout);
        tableLayout.addView(selectDynamicTaskRow);
        tableLayout.addView(TaskTypeRow);
        tableLayout.addView(taskLength);
        tableLayout.addView(taskLengthTable);
        tableLayout.addView(textDays);
        tableLayout.addView(DaysOne);
        tableLayout.addView(DaysTwo);
        tableLayout.addView(DaysThree);
        tableLayout.addView(DaysFour);

        return view;
    }

    private RadioGroup.OnCheckedChangeListener taskLengthGroupOneListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                taskLengthGroupTwo.setOnClickListener(null);
                taskLengthGroupTwo.clearCheck();
                taskLengthGroupTwo.setOnCheckedChangeListener(taskLengthGroupTwoListener);
                taskLengthGroupOne.check(checkedId);

                //  TextView textDays = activity.findViewById(DayName);


                /*
                CheckBox TueId = dayGroupOne.findViewById(TueID);
                CheckBox WedId = dayGroupOne.findViewById(WedID);
                CheckBox ThrId = dayGroupOne.findViewById(ThrID);
                CheckBox FriId = dayGroupTwo.findViewById(FriID);
                CheckBox SatId = dayGroupTwo.findViewById(SatID);
                CheckBox SunId = dayGroupTwo.findViewById(SunID);

                if (checkedId == weeklyTask) {
                    // textDays.setVisibility(View.VISIBLE);

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

                 */
            }
        }
    };


    private RadioGroup.OnCheckedChangeListener taskLengthGroupTwoListener = new RadioGroup.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId != -1) {
                taskLengthGroupOne.setOnClickListener(null);
                taskLengthGroupOne.clearCheck();
                taskLengthGroupOne.setOnCheckedChangeListener(taskLengthGroupOneListener);
                taskLengthGroupTwo.check(checkedId);

                // TextView textDays = activity.findViewById(DayName);

                /*
                CheckBox MonId = dayGroupOne.findViewById(MonID);
                CheckBox TueId = dayGroupOne.findViewById(TueID);
                CheckBox WedId = dayGroupOne.findViewById(WedID);
                CheckBox ThrId = dayGroupOne.findViewById(ThrID);
                CheckBox FriId = dayGroupTwo.findViewById(FriID);
                CheckBox SatId = dayGroupTwo.findViewById(SatID);
                CheckBox SunId = dayGroupTwo.findViewById(SunID);

                // textDays.setVisibility(View.INVISIBLE);

                MonId.setVisibility(View.INVISIBLE);
                TueId.setVisibility(View.INVISIBLE);
                WedId.setVisibility(View.INVISIBLE);
                ThrId.setVisibility(View.INVISIBLE);
                FriId.setVisibility(View.INVISIBLE);
                SatId.setVisibility(View.INVISIBLE);
                SunId.setVisibility(View.INVISIBLE);

                 */
            }
        }
    };
}