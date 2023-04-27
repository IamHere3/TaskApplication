package com.example.mementomori;

import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.CompoundButtonCompat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class YearGoals extends SaveClass {

    ColorStateList colorStateList;
    int textColor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_goals);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        int width = displayMetrics.widthPixels;

        TableLayout yearlyGoalTable = findViewById(R.id.yearlyGoalHolder);

        ImageButton addGoalButton = findViewById(R.id.addYearGoalButton);
        ImageButton ResetGoalsButton = findViewById(R.id.resetYearGoals);

        // Gets theme
        Bundle day = getIntent().getExtras();
        if(day != null) {
            colorStateList = day.getParcelable("colorState");
            textColor = day.getInt("textColor");
        }
        else
        {
            int [][] states = {{}};
            int [] colors = {getResources().getColor(R.color.white)};
            colorStateList = new ColorStateList(states, colors);

            textColor = R.color.white;
        }

        LayoutInflater inflater = (LayoutInflater) YearGoals.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.popup_add_year_goal, null);

        // Finds popup box items and changes their colour
        TextView Title = layout.findViewById(R.id.addYearGoalTitle);
        Title.setTextColor(textColor);

        EditText yearEntry = layout.findViewById(R.id.yearGoalEdit);
        yearEntry.setTextColor(textColor);

        // Devices screen density
        float density=YearGoals.this.getResources().getDisplayMetrics().density;

        // Gets popup box




        // Create focusable popupWindow
        final PopupWindow popUpWin = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        ResetGoalsButton.setOnClickListener(view -> {

            AlertDialog dialog = (AlertDialog) onCreateDialog(savedInstanceState);

            //AlertDialog.Builder resetDialog = new AlertDialog.Builder(YearGoals.this);// , R.style.AlertDialogNight);

            //resetDialog.create();
            dialog.show();

            // sets text color

            // gets by id
            TextView title = (TextView) dialog.findViewById(android.R.id.message);
            Button yes = (Button) dialog.findViewById(DialogInterface.BUTTON_POSITIVE);
            Button no = (Button) dialog.findViewById(DialogInterface.BUTTON_NEGATIVE);

            // sets colour
            if(title != null)
            {
                title.setTextColor(textColor);
            }
            if(yes != null)
            {
                yes.setTextColor(textColor);
            }
            if(no != null)
            {
                no.setTextColor(textColor);
            }
        });

        addGoalButton.setOnClickListener(view -> {

            // Button to close popup window
            ((Button) layout.findViewById(R.id.back)).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    popUpWin.dismiss();
                }
            });

            // Button to save yearly goal
            ((Button) layout.findViewById(R.id.save)).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    String yearValue = yearEntry.getText().toString();

                    // Save new yearly goal
                    Set<String> yearGoals = new HashSet<>(LoadSharedStrArray("LongTermGoals", new HashSet<>(), "sharedPref"));

                    // Create new ID
                    String IDHolder = RandomStringGeneration();

                    String NewEntry = IDHolder + "," + yearValue;

                    yearGoals.add(NewEntry);

                    SaveSharedStrArray("LongTermGoals", yearGoals, "sharedPref");

                    popUpWin.dismiss();

                    Intent intent = new Intent(YearGoals.this, YearGoals.class);
                    intent.putExtra("textColor", textColor);
                    intent.putExtra("colorState", colorStateList);

                    startActivity(intent);
                    finish();
                    // Stops page slide in when restarting activity
                    overridePendingTransition(0, 0);
                }
            });

            popUpWin.setBackgroundDrawable(new ColorDrawable(Color.GRAY));

            popUpWin.setOutsideTouchable(true);

            popUpWin.showAtLocation(layout, Gravity.CENTER, 0, 0);

        });

        Set<String> yearGoals = new HashSet<>(LoadSharedStrArray("LongTermGoals", new HashSet<>(), "sharedPref"));
        String[] yearGoalsArray = yearGoals.toArray(new String[0]);

        for(String goal : yearGoalsArray)
        {
            TableRow GoalEntry = new TableRow(this);
            GoalEntry.setGravity(Gravity.CENTER);

            String[] entry = goal.split(",");

            boolean GoalsValue = LoadSharedBoolean(entry[0], false);

            String GoalText = entry[1];

            StringBuilder holder = new StringBuilder();

            int LineCount = 0;

            // Custom new line creator
            if(GoalText.length() > 25)
            {
                int i = 0;
                int n = 0;
                int t = 0;
                while(i < 1)
                {
                    t = t + 25;
                    if(GoalText.length() < t)
                    {
                        t = GoalText.length();
                        i++;
                    }

                    holder.append(GoalText.substring(n, t)).append("\n");
                    n = n + 25;
                    LineCount++;
                }
                GoalText = holder.toString();
            }
            else
            {
                LineCount++;
            }

            TextView GoalREntry = new TextView(this);
            GoalREntry.setText(GoalText);
            GoalREntry.setTextColor(textColor);
            GoalREntry.setGravity(Gravity.CENTER);
            GoalREntry.setTextSize(20);
            GoalREntry.setSingleLine(false);
            GoalREntry.setEllipsize(TextUtils.TruncateAt.END);
            GoalREntry.setLines(LineCount);

            CheckBox complete = new CheckBox(this);
            complete.setGravity(Gravity.END);
            complete.setTextColor(textColor);
            CompoundButtonCompat.setButtonTintList(complete, colorStateList);
            complete.setOnClickListener(v -> SaveBoolData(entry[0], ((CheckBox) v).isChecked()));

            complete.setChecked(GoalsValue);

            GoalEntry.addView(complete);
            GoalEntry.addView(GoalREntry);
            yearlyGoalTable.addView(GoalEntry);
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); // , R.style.AlertDialogNight);

        builder.setMessage(R.string.conformation);

        builder.setPositiveButton(R.string.yes, (dialogInterface, i) -> {
            SaveSharedStrArray("LongTermGoals", new HashSet<>(), "sharedPref");

            Intent intent = new Intent(YearGoals.this, YearGoals.class);
            startActivity(intent);
            finish();
            overridePendingTransition(0, 0);
        });

        builder.setNegativeButton(R.string.no, (dialogInterface, i) -> {
            // Dialog closes by it self without the need for anything
        });

        return builder.create();
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