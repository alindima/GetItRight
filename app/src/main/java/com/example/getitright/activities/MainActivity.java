package com.example.getitright.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.getitright.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerEventHandlers();
    }

    protected void launchCategoriesActivity() {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
    }

    protected void launchHighscoresActivity() {
        Intent intent = new Intent(this, HighscoreActivity.class);

        Bundle b = new Bundle();
        b.putInt("playerScore", -1);
        intent.putExtras(b);

        startActivity(intent);
    }

    protected void registerEventHandlers() {
        Button playBtn = findViewById(R.id.playBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCategoriesActivity();
            }
        });

        Button highscoreBtn = findViewById(R.id.highscoresBtn);
        highscoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchHighscoresActivity();
            }
        });

        Button exitBtn = findViewById(R.id.exitBtn);
        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}
