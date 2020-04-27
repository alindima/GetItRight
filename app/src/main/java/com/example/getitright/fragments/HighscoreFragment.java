package com.example.getitright.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.getitright.R;
import com.example.getitright.adapters.MyHighscoreRecyclerViewAdapter;
import com.example.getitright.fragments.dummy.DummyContent;
import com.example.getitright.fragments.dummy.DummyContent.DummyItem;
import com.example.getitright.models.Highscore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HighscoreFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    final String HIGHSCORES_KEY = "list_of_highscores";
    private List<Highscore> highscores;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HighscoreFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HighscoreFragment newInstance(int columnCount) {
        HighscoreFragment fragment = new HighscoreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

    }

    protected void getHighscores(){
        SharedPreferences sharedPref = getActivity().getPreferences(MODE_PRIVATE);
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


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
