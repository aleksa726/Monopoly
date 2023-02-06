package com.example.monopoly;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.monopoly.databinding.FragmentGameBinding;
import com.example.monopoly.databinding.FragmentSimulationBinding;
import com.example.monopoly.game.Game;
import com.example.monopoly.game.GameSimulation;
import com.example.monopoly.game.Player;
import com.example.monopoly.game.PlayerSimulation;
import com.example.monopoly.game.data.GameEntity;
import com.example.monopoly.game.data.MonopolyDatabase;
import com.example.monopoly.game.data.TurnEntity;

import java.util.ArrayList;
import java.util.List;


public class SimulationFragment extends Fragment {

    private FragmentSimulationBinding binding;
    private MatchHistoryViewModel matchHistoryViewModel;
    private GameEntity gameEntity;

    private GameSimulation game;

    private MainActivity mainActivity;
    private MonopolyDatabase monopolyDatabase;

    private List<TurnEntity> turnEntityList = new ArrayList<>();

    private int countTurn = 0;

    public static TurnEntity currentTurn = null;

    private NavController navController;


    public SimulationFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) requireActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSimulationBinding.inflate(inflater, container, false);

        MainActivity parentActivity = (MainActivity) getParentFragment().getActivity();

        matchHistoryViewModel = new ViewModelProvider(parentActivity).get(MatchHistoryViewModel.class);

        monopolyDatabase = MonopolyDatabase.getInstance(mainActivity);


        int index = SimulationFragmentArgs.fromBundle(requireArguments()).getIndex();
        gameEntity = matchHistoryViewModel.getGameEntities().get(index);


        if(savedInstanceState != null){
            this.game = (GameSimulation) savedInstanceState.get("Game");
            reinitializeFields();
            game.setFragmentBinding(binding);
            game.reinitializeFields();

            game.redrawFields();

            countTurn = savedInstanceState.getInt("index");

            game.setPlayerInformation();
        }
        else {


            game = new GameSimulation(binding, (int) gameEntity.getId());

            String[] playersNames = {
                    gameEntity.getPlayer1(),
                    gameEntity.getPlayer2(),
                    gameEntity.getPlayer3(),
                    gameEntity.getPlayer4()
            };

            int[] colors = {
                    Color.BLUE,
                    Color.MAGENTA,
                    Color.CYAN,
                    Color.GREEN
            };

            ArrayList<PlayerSimulation> players = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                if (playersNames[i] != null) {
                    players.add(new PlayerSimulation(playersNames[i], colors[i], game, monopolyDatabase));
                }
            }

            game.setPlayers(players);

            for (PlayerSimulation p : players) {
                binding.field0.addPlayer(p);
            }
            game.setPlayerInformation();
            binding.endTurnRollDiceButton.setText("Start simulation");
        }


        turnEntityList = monopolyDatabase.turnDao().getAll(gameEntity.getId());


        binding.endTurnRollDiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.endTurnRollDiceButton.getText().equals("End simulation")){
                    NavDirections action = SimulationFragmentDirections.actionSimulationFragmentToMatchHistoryFragment();
                    navController.navigate(action);
                }
                else {
                    currentTurn = turnEntityList.get(countTurn);
                    if (countTurn++ == turnEntityList.size()) {
                        binding.endTurnRollDiceButton.setText("End simulation");

                    } else {
                        PlayerSimulation player = game.getPlayers().get(currentTurn.getPlayerIndex());
                        player.setDice(currentTurn.getDice1(), currentTurn.getDice2());
                        game.setDiceLabel(currentTurn.getDice1(), currentTurn.getDice2());
                        player.rollDice();


                        if (countTurn == turnEntityList.size()) {
                            binding.endTurnRollDiceButton.setText("End simulation");
                        } else {
                            game.setPlayerInformation();
                            game.nextPlayerTurn();
                            binding.endTurnRollDiceButton.setText("Next turn");
                        }
                    }
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Game", this.game);
        outState.putInt("index", this.countTurn);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    public void reinitializeFields(){
        FragmentSimulationBinding fragmentGameBinding = game.getFragmentBinding();
        binding.field0.setInfo(fragmentGameBinding.field0.getPlayers(), fragmentGameBinding.field0.getOwner(), fragmentGameBinding.field0.isPainted());
        binding.field1.setInfo(fragmentGameBinding.field1.getPlayers(), fragmentGameBinding.field1.getOwner(), fragmentGameBinding.field1.isPainted());
        binding.field2.setInfo(fragmentGameBinding.field2.getPlayers(), fragmentGameBinding.field2.getOwner(), fragmentGameBinding.field2.isPainted());
        binding.field3.setInfo(fragmentGameBinding.field3.getPlayers(), fragmentGameBinding.field3.getOwner(), fragmentGameBinding.field3.isPainted());
        binding.field4.setInfo(fragmentGameBinding.field4.getPlayers(), fragmentGameBinding.field4.getOwner(), fragmentGameBinding.field4.isPainted());
        binding.field5.setInfo(fragmentGameBinding.field5.getPlayers(), fragmentGameBinding.field5.getOwner(), fragmentGameBinding.field5.isPainted());
        binding.field6.setInfo(fragmentGameBinding.field6.getPlayers(), fragmentGameBinding.field6.getOwner(), fragmentGameBinding.field6.isPainted());
        binding.field7.setInfo(fragmentGameBinding.field7.getPlayers(), fragmentGameBinding.field7.getOwner(), fragmentGameBinding.field7.isPainted());
        binding.field8.setInfo(fragmentGameBinding.field8.getPlayers(), fragmentGameBinding.field8.getOwner(), fragmentGameBinding.field8.isPainted());
        binding.field9.setInfo(fragmentGameBinding.field9.getPlayers(), fragmentGameBinding.field9.getOwner(), fragmentGameBinding.field9.isPainted());
        binding.field10.setInfo(fragmentGameBinding.field10.getPlayers(), fragmentGameBinding.field10.getOwner(), fragmentGameBinding.field10.isPainted());
        binding.field11.setInfo(fragmentGameBinding.field11.getPlayers(), fragmentGameBinding.field11.getOwner(), fragmentGameBinding.field11.isPainted());
        binding.field12.setInfo(fragmentGameBinding.field12.getPlayers(), fragmentGameBinding.field12.getOwner(), fragmentGameBinding.field12.isPainted());
        binding.field13.setInfo(fragmentGameBinding.field13.getPlayers(), fragmentGameBinding.field13.getOwner(), fragmentGameBinding.field13.isPainted());
        binding.field14.setInfo(fragmentGameBinding.field14.getPlayers(), fragmentGameBinding.field14.getOwner(), fragmentGameBinding.field14.isPainted());
        binding.field15.setInfo(fragmentGameBinding.field15.getPlayers(), fragmentGameBinding.field15.getOwner(), fragmentGameBinding.field15.isPainted());
        binding.field16.setInfo(fragmentGameBinding.field16.getPlayers(), fragmentGameBinding.field16.getOwner(), fragmentGameBinding.field16.isPainted());
        binding.field17.setInfo(fragmentGameBinding.field17.getPlayers(), fragmentGameBinding.field17.getOwner(), fragmentGameBinding.field17.isPainted());
        binding.field18.setInfo(fragmentGameBinding.field18.getPlayers(), fragmentGameBinding.field18.getOwner(), fragmentGameBinding.field18.isPainted());
        binding.field19.setInfo(fragmentGameBinding.field19.getPlayers(), fragmentGameBinding.field19.getOwner(), fragmentGameBinding.field19.isPainted());
        binding.field20.setInfo(fragmentGameBinding.field20.getPlayers(), fragmentGameBinding.field20.getOwner(), fragmentGameBinding.field20.isPainted());
        binding.field21.setInfo(fragmentGameBinding.field21.getPlayers(), fragmentGameBinding.field21.getOwner(), fragmentGameBinding.field21.isPainted());
        binding.field22.setInfo(fragmentGameBinding.field22.getPlayers(), fragmentGameBinding.field22.getOwner(), fragmentGameBinding.field22.isPainted());
        binding.field23.setInfo(fragmentGameBinding.field23.getPlayers(), fragmentGameBinding.field23.getOwner(), fragmentGameBinding.field23.isPainted());
        binding.field24.setInfo(fragmentGameBinding.field24.getPlayers(), fragmentGameBinding.field24.getOwner(), fragmentGameBinding.field24.isPainted());
        binding.field25.setInfo(fragmentGameBinding.field25.getPlayers(), fragmentGameBinding.field25.getOwner(), fragmentGameBinding.field25.isPainted());
        binding.field26.setInfo(fragmentGameBinding.field26.getPlayers(), fragmentGameBinding.field26.getOwner(), fragmentGameBinding.field26.isPainted());
        binding.field27.setInfo(fragmentGameBinding.field27.getPlayers(), fragmentGameBinding.field27.getOwner(), fragmentGameBinding.field27.isPainted());
        binding.field28.setInfo(fragmentGameBinding.field28.getPlayers(), fragmentGameBinding.field28.getOwner(), fragmentGameBinding.field28.isPainted());
        binding.field29.setInfo(fragmentGameBinding.field29.getPlayers(), fragmentGameBinding.field29.getOwner(), fragmentGameBinding.field29.isPainted());
        binding.field30.setInfo(fragmentGameBinding.field30.getPlayers(), fragmentGameBinding.field30.getOwner(), fragmentGameBinding.field30.isPainted());
        binding.field31.setInfo(fragmentGameBinding.field31.getPlayers(), fragmentGameBinding.field31.getOwner(), fragmentGameBinding.field31.isPainted());
        binding.field32.setInfo(fragmentGameBinding.field32.getPlayers(), fragmentGameBinding.field32.getOwner(), fragmentGameBinding.field32.isPainted());
        binding.field33.setInfo(fragmentGameBinding.field33.getPlayers(), fragmentGameBinding.field33.getOwner(), fragmentGameBinding.field33.isPainted());
        binding.field34.setInfo(fragmentGameBinding.field34.getPlayers(), fragmentGameBinding.field34.getOwner(), fragmentGameBinding.field34.isPainted());
        binding.field35.setInfo(fragmentGameBinding.field35.getPlayers(), fragmentGameBinding.field35.getOwner(), fragmentGameBinding.field35.isPainted());
        binding.field36.setInfo(fragmentGameBinding.field36.getPlayers(), fragmentGameBinding.field36.getOwner(), fragmentGameBinding.field36.isPainted());
        binding.field37.setInfo(fragmentGameBinding.field37.getPlayers(), fragmentGameBinding.field37.getOwner(), fragmentGameBinding.field37.isPainted());
        binding.field38.setInfo(fragmentGameBinding.field38.getPlayers(), fragmentGameBinding.field38.getOwner(), fragmentGameBinding.field38.isPainted());
        binding.field39.setInfo(fragmentGameBinding.field39.getPlayers(), fragmentGameBinding.field39.getOwner(), fragmentGameBinding.field39.isPainted());
    }

}