package com.example.monopoly;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.monopoly.databinding.FragmentHomeBinding;
import com.example.monopoly.game.data.MonopolyDatabase;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private NavController navController;
    private MainActivity mainActivity;

    private MonopolyDatabase monopolyDatabase;

    public HomeFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) requireActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        binding.startGame.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToGameConfigureFragment();
            navController.navigate(action);
        });

        binding.settings.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToSettingsFragment();
            navController.navigate(action);
        });

        binding.gameHistory.setOnClickListener(v -> {
            NavDirections action = HomeFragmentDirections.actionHomeFragmentToMatchHistoryFragment();
            navController.navigate(action);
        });

        monopolyDatabase = MonopolyDatabase.getInstance(mainActivity);

        monopolyDatabase.gameDao().deleteUnfinishedGames();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

}