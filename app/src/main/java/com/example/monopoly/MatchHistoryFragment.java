package com.example.monopoly;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.monopoly.databinding.FragmentMatchHistoryBinding;
import com.example.monopoly.game.data.GameEntity;
import com.example.monopoly.game.data.MonopolyDatabase;

import java.util.Date;
import java.util.List;


public class MatchHistoryFragment extends Fragment {

    private FragmentMatchHistoryBinding binding;
    private MatchHistoryViewModel matchHistoryViewModel;
    private NavController navController;
    private MainActivity mainActivity;

    public MatchHistoryFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainActivity = (MainActivity) requireActivity();
        matchHistoryViewModel = new ViewModelProvider(mainActivity).get(MatchHistoryViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMatchHistoryBinding.inflate(inflater, container, false);

        MonopolyDatabase monopolyDatabase = MonopolyDatabase.getInstance(mainActivity);



        MatchHistoryAdapter matchHistoryAdapter = new MatchHistoryAdapter(
                mainActivity,
                gameIndex -> {
                    MatchHistoryFragmentDirections.ActionMatchHistoryFragmentToSimulationFragment action =
                            MatchHistoryFragmentDirections.actionMatchHistoryFragmentToSimulationFragment(gameIndex);

                    navController.navigate(action);
                }

        );


        monopolyDatabase.gameDao().getAllLiveData().observe(getViewLifecycleOwner(), gameEntities -> {
            matchHistoryAdapter.setGameEntities(gameEntities);
        });

        monopolyDatabase.gameDao().getAllLiveData().observe(getViewLifecycleOwner(), gameEntities -> {
            matchHistoryViewModel.setGameEntities(gameEntities);
        });


        binding.recyclerView.setAdapter(matchHistoryAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));




        binding.clearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(mainActivity)
                        .setTitle("Quit")
                        .setMessage("Are you sure you want to delete match history?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                monopolyDatabase.gameDao().deleteAll();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });


        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }
}