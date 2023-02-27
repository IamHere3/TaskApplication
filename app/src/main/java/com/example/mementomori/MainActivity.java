package com.example.mementomori;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class MainActivity extends SaveClass {

    Button Tasks, Settings;

    public static final String Shared_Pref = "sharedPref";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initial setup checker
        boolean FirstRun = LoadSharedBoolean("Setup", false);

        if (!FirstRun) {
            compileStartSetArrays();
        }

        // Strings for quote displaying
        String[] Phrases = PhraseArray();
        String[] EnglishPhrases = EnglishPhraseArray();
        String[] Author = Author();
        UpdateThemeStr();

        UpdateText(Phrases, EnglishPhrases, Author);

        //region SettingOnClickListeners

        // Setting button activity
        Tasks = findViewById(R.id.PageTransition);

        Tasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Tasks.class);
                try {
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast toast = Toast.makeText(MainActivity.this, "no activity found", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        Settings = findViewById(R.id.SettingTransition);

        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(MainActivity.this, Settings.class);
                try {
                    startActivity(intent);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast toast = Toast.makeText(MainActivity.this, "no activity found", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        //endregion
    }

    @Override
    public void onResume()
    {
        // Calls a super class method
        super.onResume();

        UpdateThemeStr();
    }

    //region $JSONPopulation

    // New application population
    protected void compileStartSetArrays() {
        SharedPreferences StringSharedPref = getSharedPreferences(Shared_Pref, MODE_PRIVATE);
        SharedPreferences.Editor setUp = StringSharedPref.edit();

        /*
        SharedPreferences MorningR = getSharedPreferences("MorningR", MODE_PRIVATE);
        SharedPreferences.Editor MorningRe = MorningR.edit();

         */

        Set<String> mRSetup = new HashSet<>();

        mRSetup.add("wake,Get Up (before 7.30),0,1");
        mRSetup.add("breakfast,Breakfast,0,2");

        setUp.putStringSet("MorningRoutine", mRSetup);

        Set<String> dRSetup = new HashSet<>();

        dRSetup.add("hobbyOne,hobbies,100,3");
        dRSetup.add("hobbyTwo,hobbies,100,4");
        dRSetup.add("diary,Write in Diary,0,5");
        dRSetup.add("sleep,Go to bed by 11.00,0,6");

        /*
        SharedPreferences DayR = getSharedPreferences("DayR", MODE_PRIVATE);
        SharedPreferences.Editor DayRe = DayR.edit();

         */

        setUp.putStringSet("DayRoutine", dRSetup);

        Set<String> hSetup = new HashSet<>();

        hSetup.add("Calligraphy");
        hSetup.add("Do something good / new");
        hSetup.add("Latin");
        hSetup.add("Math");
        hSetup.add("Paint");
        hSetup.add("Read");
        hSetup.add("Sign language");
        hSetup.add("Ukulele");

        setUp.putStringSet("HobbyOptions", hSetup);

        setUp.putBoolean("Setup", true);

        setUp.apply();
        /*
        DayRe.apply();
        MorningRe.apply();

         */
    }

    // Initial quotes
    protected static String[] PhraseArray(){
        return new String[]{"Memento Mori", "Vincit Qui Se Vincit", "Tempus Edax Rerum", "Valar Monghulis", "", "", "", "Carpe diem", "Acta, non verba", "Memento vivere", "", "", "", "", "", "", "", "", ""};
    }

    // English translation if one is needed
    protected static String[] EnglishPhraseArray(){
        return new String[]{"Remember you are mortal", "He conquers who conquers himself", "time, devourer of all things", "All men must die", "You only live once, but if you do it right, once is enough", "Believe you can and you're halfway there", "You miss 100% of the shots you don't take", "Seize the day", "Deeds, not words", "Remember to live", "In three words I can sum up everything I've learned about life: it goes on", "I have not failed. I've just found 10,000 ways that won't work", "We don't see things as they are, we see them as we are", "Sometimes the questions are complicated and the answers are simple", "Do one thing every day that scares you", "Pain is temporary, quitting lasts forever", "Don’t wait for things to happen, Make them happen", "Do not let the roles you play in life make you forget who you are", "Find a purpose to serve, not a lifestyle to live"};
    }

    // Author of the quotes
    protected static String[] Author(){
        return new String[]{"Socrates","Publilius Syrus","Ovid","Unknown","Mae West", "Theodore Roosevelt", "Wayne Gretzky", "Horace's Odes", "Emmeline Pankhurst", "Unknown", "Robert Frost", "Thomas Edison", "Anaïs Nin", "Dr. Seuss", "Eleanor Roosevelt", "Lance Armstrong", "Roy Bennett", "Roy Bennett", "Criss Jami"};
    }

    //endregion

    //region TextUpdating

    // Quote picking and updating
    private void UpdateText(String[] Phrases, String[] EnglishPhrases, String[] Author) {
        Random rand = new Random();
        int n = rand.nextInt(Phrases.length);

        if(!Phrases[n].equals(""))
        {
            final TextView mPhrase = findViewById(R.id.Phrase);
            mPhrase.setText(Phrases[n]);

            final TextView mEPhrase = findViewById(R.id.Translation);
            mEPhrase.setText(EnglishPhrases[n]);

            final TextView mAPhrase = findViewById(R.id.Author);
            mAPhrase.setText(Author[n]);
        }
        else
        {
            final TextView mEPhrase = findViewById(R.id.translationTwo);
            mEPhrase.setText(EnglishPhrases[n]);

            final TextView mAPhrase = findViewById(R.id.AuthorTwo);
            mAPhrase.setText(Author[n]);

            // Clears primary quote holders
            final TextView ClearPhrase = findViewById(R.id.Phrase);
            ClearPhrase.setText("");

            final TextView ClearAuthor = findViewById(R.id.Author);
            ClearAuthor.setText("");

            final TextView ClearTranslation = findViewById(R.id.Translation);
            ClearTranslation.setText("");
        }
    }

    // Theme displaying
    private void UpdateThemeStr()
    {
        String Theme = LoadSharedStr("currentTheme", "App of quotes");

        // theme system https://www.youtube.com/watch?v=NVGuFdX5guE
        if(!Objects.equals(Theme, "App of quotes"))
        {
            Theme = "Season of " + Theme;
        }

        final TextView cTheme = findViewById(R.id.SeasonalTheme);
        cTheme.setText(Theme);
    }
    //endregion
}