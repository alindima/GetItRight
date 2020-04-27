package com.example.getitright.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.getitright.R;
import com.example.getitright.fragments.HighscoreFragment;
import com.example.getitright.fragments.NameInputFragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.HashSet;
import java.util.Set;

public class HighscoreActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Integer playerScore;
    final String HIGHSCORES_KEY = "list_of_highscores";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        fragmentManager = getSupportFragmentManager();

        Bundle b = getIntent().getExtras();
        if(b != null){
            playerScore = b.getInt("playerScore");
        }
        actionBarSetup();

        if(playerScore > 0){
            showNameInputFragment();
        }

    }

    protected void actionBarSetup() {
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setTitle("Highscores");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    protected void showNameInputFragment(){
        NameInputFragment nameInputFragment = new NameInputFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.highscorePageContainer, nameInputFragment , "fragment-name-input");

        fragmentTransaction.runOnCommit(() -> {
            addNameInputListener(nameInputFragment);
        });

        fragmentTransaction.commit();

    }

    protected void addNameInputListener(NameInputFragment fragment){
        Button confirmNameButton = this.findViewById(R.id.confirmNameButton);
        EditText nameInputEditText = this.findViewById(R.id.nameInputEditText);
        confirmNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInputEditText.getText().toString();
                // AICI TREBUIE SA VEDEM SA NU AIBA : SAU ALTE SMECHERII
                saveScoreEntry(name);
                hideNameInputFragment(fragment);
                showScoresFragment();
            }
        });
    }

    protected void saveScoreEntry(String name){
        SharedPreferences sharedPref = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        Set<String> scores = sharedPref.getStringSet(HIGHSCORES_KEY, new HashSet<>());
        scores.add(name + ":" + playerScore.toString());

        editor.putStringSet(HIGHSCORES_KEY, scores);
        editor.commit();
    }

    protected void hideNameInputFragment(NameInputFragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment);
        fragmentTransaction.commitNow();
    }

    protected void showScoresFragment(){
        HighscoreFragment highscoreFragment = new HighscoreFragment();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.highscorePageContainer, highscoreFragment , "fragment-highscores-list");
        fragmentTransaction.commitNow();

        System.out.println("SHOWING ALL SCORES NICE JOB");
    }
}
