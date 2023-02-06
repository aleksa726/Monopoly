package com.example.monopoly;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.monopoly.R;
import com.example.monopoly.databinding.FragmentGameBinding;
import com.example.monopoly.databinding.FragmentWinnerBinding;


public class WinnerFragment extends Fragment {

    public FragmentWinnerBinding binding;

    private NavController navController;

    private String playerName;

    public WinnerFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWinnerBinding.inflate(inflater, container, false);

        if(savedInstanceState != null){
            playerName = savedInstanceState.getString("label");
        }
        else {
            playerName = WinnerFragmentArgs.fromBundle(requireArguments()).getPlayerName();
        }

        binding.label2.setText(playerName);
        binding.endGameButton.setOnClickListener(v -> {
            NavDirections action = WinnerFragmentDirections.actionWinnerFragmentToHomeFragment();
            navController.navigate(action);
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("label", playerName);
    }
}