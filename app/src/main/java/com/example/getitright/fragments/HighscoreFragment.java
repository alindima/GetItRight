package com.example.getitright.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getitright.R;
import com.example.getitright.adapters.MyHighscoreRecyclerViewAdapter;
import com.example.getitright.models.Highscore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class HighscoreFragment extends Fragment {

    final String HIGHSCORES_KEY = "list_of_highscores";
    private List<Highscore> highscores;

    public HighscoreFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void getHighscores(){
        SharedPreferences sharedPref = getContext().getSharedPreferences(HIGHSCORES_KEY, MODE_PRIVATE);
        Set<String> scores = sharedPref.getStringSet(HIGHSCORES_KEY, new HashSet<>());
        highscores = new ArrayList<>();

        for(String s:scores){
            Highscore hs = new Highscore();
            String[] splitRes = s.split(":");
            hs.name = splitRes[0];
            hs.score = Integer.parseInt(splitRes[1]);
            highscores.add(hs);
        }

        Collections.sort(highscores);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_highscore_list, container, false);

        getHighscores();

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            recyclerView.setAdapter(new MyHighscoreRecyclerViewAdapter(highscores));
        }

        return view;
    }

}
