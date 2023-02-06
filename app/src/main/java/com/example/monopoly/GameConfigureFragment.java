package com.example.monopoly;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.monopoly.databinding.FragmentGameConfigureBinding;
import com.example.monopoly.databinding.FragmentHomeBinding;

public class GameConfigureFragment extends Fragment {

    private FragmentGameConfigureBinding binding;

    private NavController navController;


    public GameConfigureFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGameConfigureBinding.inflate(inflater, container, false);

        binding.player1.setVisibility(View.INVISIBLE);
        binding.player2.setVisibility(View.INVISIBLE);
        binding.player3.setVisibility(View.INVISIBLE);
        binding.player4.setVisibility(View.INVISIBLE);

        binding.numberOfPlayersEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        binding.numberOfPlayersEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                switch (binding.numberOfPlayersEditText.getText().toString()){
                    case("2"):
                        binding.player1.setVisibility(View.VISIBLE);
                        binding.player2.setVisibility(View.VISIBLE);
                        binding.player3.setVisibility(View.INVISIBLE);
                        binding.player4.setVisibility(View.INVISIBLE);
                        break;
                    case("3"):
                        binding.player1.setVisibility(View.VISIBLE);
                        binding.player2.setVisibility(View.VISIBLE);
                        binding.player3.setVisibility(View.VISIBLE);
                        binding.player4.setVisibility(View.INVISIBLE);
                        break;
                    case("4"):
                        binding.player1.setVisibility(View.VISIBLE);
                        binding.player2.setVisibility(View.VISIBLE);
                        binding.player3.setVisibility(View.VISIBLE);
                        binding.player4.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        binding.startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (binding.numberOfPlayersEditText.getText().toString()){
                    case("2"):
                        if(binding.player1TextView.getText().toString().length() == 0 ||
                                binding.player2TextView.getText().toString().length() == 0){
                        }
                        else{
                            String player1, player2;
                            player1 = binding.player1TextView.getText().toString();
                            player2 = binding.player2TextView.getText().toString();
                            NavDirections action = GameConfigureFragmentDirections.actionGameConfigureFragmentToGameFragment(
                                    player1, player2, "", ""
                            );
                            navController.navigate(action);
                        }
                        break;
                    case("3"):
                        if(binding.player1TextView.getText().toString().length() == 0 ||
                                binding.player2TextView.getText().toString().length() == 0 ||
                                binding.player3TextView.getText().toString().length() == 0
                        ){
                        }
                        else{
                            String player1, player2, player3;
                            player1 = binding.player1TextView.getText().toString();
                            player2 = binding.player2TextView.getText().toString();
                            player3 = binding.player3TextView.getText().toString();
                            NavDirections action = GameConfigureFragmentDirections.actionGameConfigureFragmentToGameFragment(
                                    player1, player2, player3, ""
                            );
                            navController.navigate(action);
                        }
                        break;
                    case("4"):
                        if(binding.player1TextView.getText().toString().length() == 0 ||
                                binding.player2TextView.getText().toString().length() == 0 ||
                                binding.player3TextView.getText().toString().length() == 0 ||
                                binding.player4TextView.getText().toString().length() == 0
                        ){
                        }
                        else{
                            String player1, player2, player3, player4;
                            player1 = binding.player1TextView.getText().toString();
                            player2 = binding.player2TextView.getText().toString();
                            player3 = binding.player3TextView.getText().toString();
                            player4 = binding.player4TextView.getText().toString();
                            NavDirections action = GameConfigureFragmentDirections.actionGameConfigureFragmentToGameFragment(
                                    player1, player2, player3, player4
                            );
                            navController.navigate(action);
                        }
                        break;
                }
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