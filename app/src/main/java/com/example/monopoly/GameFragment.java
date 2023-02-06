package com.example.monopoly;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.monopoly.databinding.FragmentGameBinding;
import com.example.monopoly.game.Game;
import com.example.monopoly.game.OwnableField;
import com.example.monopoly.game.Player;
import com.example.monopoly.game.RollDiceSensor;
import com.example.monopoly.game.data.GameEntity;
import com.example.monopoly.game.data.MonopolyDatabase;

import java.util.ArrayList;
import java.util.Date;


public class GameFragment extends Fragment {

    private FragmentGameBinding binding;

    private Game game;

    public static ImageView cardImageView;
    public static ImageView dice1ImageView;
    public static ImageView dice2ImageView;


    private RollDiceSensor rollDiceSensor;

    private NavController navController;

    private MainActivity mainActivity;

    private MonopolyDatabase monopolyDatabase;


    public GameFragment() {}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) requireActivity();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentGameBinding.inflate(inflater, container, false);

        rollDiceSensor = new RollDiceSensor(binding, this);

        getLifecycle().addObserver(rollDiceSensor);

        monopolyDatabase = MonopolyDatabase.getInstance(mainActivity);

        rollDiceSensor.start(getActivity());

        if(savedInstanceState != null){

            this.game = (Game) savedInstanceState.get("Game");
            reinitializeFields();
            game.setFragmentBinding(binding);
            game.reinitializeFields();

            game.redrawFields();

            cardImageView = binding.cardImage;
            dice1ImageView = binding.dice1;
            dice2ImageView = binding.dice2;
            int dice1 = savedInstanceState.getInt("dice1");
            ImageView dice1Image = GameFragment.dice1ImageView;
            int image1 = getResources().getIdentifier("dice" + dice1, "drawable", getContext().getPackageName());
            dice1Image.setImageResource(image1);

            int dice2 = savedInstanceState.getInt("dice2");
            ImageView dice2Image = GameFragment.dice2ImageView;
            int image2 = getResources().getIdentifier("dice" + dice2, "drawable", getContext().getPackageName());
            dice2Image.setImageResource(image2);

            game.setPlayerInformation();
        }
        else{
            game = new Game(binding, monopolyDatabase);

            String[] playersNames = {
                    GameFragmentArgs.fromBundle(requireArguments()).getPlayer1(),
                    GameFragmentArgs.fromBundle(requireArguments()).getPlayer2(),
                    GameFragmentArgs.fromBundle(requireArguments()).getPlayer3(),
                    GameFragmentArgs.fromBundle(requireArguments()).getPlayer4()
            };

            int[] colors = {
                    Color.BLUE,
                    Color.MAGENTA,
                    Color.CYAN,
                    Color.GREEN
            };

            ArrayList<Player> players = new ArrayList<>();
            for(int i = 0; i < 4; i++){
                if(!playersNames[i].equals("")){
                    players.add(new Player(playersNames[i], colors[i], game));
                }
            }

            game.setPlayers(players);

            for(Player p: players) {
                binding.field0.addPlayer(p);
            }
            cardImageView = binding.cardImage;
            dice1ImageView = binding.dice1;
            dice2ImageView = binding.dice2;


            game.setPlayerInformation();
        }





        binding.endTurnRollDiceButton.setOnClickListener(v -> {
            if(game.isTurnOver()){
                binding.messageTextView.setText("");
                if(game.getPlayingPlayer().getOwedMoney() != 0){
                    if(game.getPlayingPlayer().getMoney() - game.getPlayingPlayer().getOwedMoney() >= 0){
                        game.getPlayingPlayer().decMoney(game.getPlayingPlayer().getOwedMoney());
                        game.getPlayingPlayer().getPlayerYouOwe().addMoney(game.getPlayingPlayer().getOwedMoney());
                        game.getPlayingPlayer().setOwedMoney(0);
                        game.getPlayingPlayer().setPlayerYouOwe(null);

                        game.nextPlayerTurn();
                        if(game.getWinner() != null){
                            addGameToDatabase();
                            NavDirections action = GameFragmentDirections.actionGameFragmentToWinnerFragment(game.getWinner().getPlayerName());
                            navController.navigate(action);
                        }

                        setBuyHouse(false);
                        setSellHouse(false);
                        morgageProperty(false);
                        if (game.getPlayingPlayer().isInJail()) {
                            binding.buyPropertyButton.setEnabled(true);
                            binding.buyPropertyButton.setText("Pay $50 to get out");
                            binding.endTurnRollDiceButton.setEnabled(false);
                            binding.endTurnRollDiceButton.setText("Roll double");
                            rollDiceSensor.setStop(false);
                        } else {
                            binding.buyPropertyButton.setText("Buy property");
                            binding.endTurnRollDiceButton.setEnabled(false);
                            binding.endTurnRollDiceButton.setText("Roll dices");
                            rollDiceSensor.setStop(false);
                        }
                    }
                    else if(!game.getPlayingPlayer().hasSomethingToSell()){
                        game.getPlayingPlayer().setBankrupt(true);
                        game.getPlayingPlayer().getPlayerYouOwe().addMoney(game.getPlayingPlayer().getMoney());
                        game.getPlayingPlayer().transferProperties(game.getPlayingPlayer().getPlayerYouOwe());
                        Log.d("player", "bankrupt");

                        game.nextPlayerTurn();
                        if(game.getWinner() != null){
                            addGameToDatabase();
                            NavDirections action = GameFragmentDirections.actionGameFragmentToWinnerFragment(game.getWinner().getPlayerName());
                            navController.navigate(action);
                        }
                        setBuyHouse(false);
                        setSellHouse(false);
                        morgageProperty(false);
                        if (game.getPlayingPlayer().isInJail()) {
                            binding.buyPropertyButton.setEnabled(true);
                            binding.buyPropertyButton.setText("Pay $50 to get out");
                            binding.endTurnRollDiceButton.setEnabled(false);
                            binding.endTurnRollDiceButton.setText("Roll double");
                            rollDiceSensor.setStop(false);
                        } else {
                            binding.buyPropertyButton.setText("Buy property");
                            binding.endTurnRollDiceButton.setEnabled(false);
                            binding.endTurnRollDiceButton.setText("Roll dices");
                            rollDiceSensor.setStop(false);
                        }
                    }
                    else{
                        binding.messageTextView.setText("You need to sell properties!");
                    }
                }
                else {
                    game.nextPlayerTurn();
                    if(game.getWinner() != null){
                        addGameToDatabase();
                        NavDirections action = GameFragmentDirections.actionGameFragmentToWinnerFragment(game.getWinner().getPlayerName());
                        navController.navigate(action);
                    }
                    setBuyHouse(false);
                    setSellHouse(false);
                    morgageProperty(false);
                    if (game.getPlayingPlayer().isInJail()) {
                        binding.buyPropertyButton.setEnabled(true);
                        binding.buyPropertyButton.setText("Pay $50 to get out");
                        binding.endTurnRollDiceButton.setEnabled(false);
                        binding.endTurnRollDiceButton.setText("Roll double");
                        rollDiceSensor.setStop(false);
                    } else {
                        binding.buyPropertyButton.setText("Buy property");
                        binding.endTurnRollDiceButton.setEnabled(false);
                        binding.endTurnRollDiceButton.setText("Roll dices");
                        rollDiceSensor.setStop(false);
                    }
                }
            }
            else{
                Player player = game.getPlayingPlayer();

                player.rollDice();
            }
        });

        binding.buyPropertyButton.setEnabled(false);
        binding.buyPropertyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.buyPropertyButton.getText().equals("Pay $50 to get out")){
                    Player player = game.getPlayingPlayer();
                    player.payToGetOut();
                }
                else if(game.isBuyEnabled()){
                    Player player = game.getPlayingPlayer();
                    player.buyProperty((OwnableField) player.getCurrentField());
                }
            }
        });

        binding.buyHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSellHouse(false);
                morgageProperty(false);
                if (!Game.buyHouseEnabled) {
                    binding.buyHouseButton.setBackgroundResource(R.drawable.roundedbutton);
                    Game.buyHouseEnabled = true;
                } else {
                    binding.buyHouseButton.setBackgroundResource(R.drawable.ovalbutton);
                    Game.buyHouseEnabled = false;
                }
            }
        });

        binding.sellHouseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                morgageProperty(false);
                setBuyHouse(false);
                if (!Game.sellHouseEnabled) {
                    binding.sellHouseButton.setBackgroundResource(R.drawable.roundedbutton);
                    Game.sellHouseEnabled = true;
                } else {
                    binding.sellHouseButton.setBackgroundResource(R.drawable.ovalbutton);
                    Game.sellHouseEnabled = false;
                }
            }
        });

        binding.morgageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSellHouse(false);
                setBuyHouse(false);
                if (!Game.morgagePropertyEnabled) {
                    binding.morgageButton.setBackgroundResource(R.drawable.roundedbutton);
                    Game.morgagePropertyEnabled = true;
                } else {
                    binding.morgageButton.setBackgroundResource(R.drawable.ovalbutton);
                    Game.morgagePropertyEnabled = false;
                }
            }
        });

        return binding.getRoot();

    }



    public void setBuyHouse(boolean flag){
        if (flag) {
            binding.buyHouseButton.setBackgroundResource(R.drawable.roundedbutton);
            Game.buyHouseEnabled = true;
        } else {
            binding.buyHouseButton.setBackgroundResource(R.drawable.ovalbutton);
            Game.buyHouseEnabled = false;
        }
    }

    public void setSellHouse(boolean flag){
        if (flag) {
            binding.sellHouseButton.setBackgroundResource(R.drawable.roundedbutton);
            Game.sellHouseEnabled = true;
        } else {
            binding.sellHouseButton.setBackgroundResource(R.drawable.ovalbutton);
            Game.sellHouseEnabled = false;
        }
    }

    public void morgageProperty(boolean flag){
        if (flag) {
            binding.morgageButton.setBackgroundResource(R.drawable.roundedbutton);
            Game.morgagePropertyEnabled = true;
        } else {
            binding.morgageButton.setBackgroundResource(R.drawable.ovalbutton);
            Game.morgagePropertyEnabled = false;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }


    private MediaPlayer mediaPlayer = null;

    public void startSound(){
        if(mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.dice_sound);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }

    public void stopSound(){
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(game != null) {
            outState.putSerializable("Game", this.game);
            outState.putInt("dice1", game.getPlayingPlayer().getDice1());
            outState.putInt("dice2", game.getPlayingPlayer().getDice2());
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getLifecycle().removeObserver(rollDiceSensor);
    }

    public void addGameToDatabase(){
        switch (game.getPlayers().size()){
            case 2:
                monopolyDatabase.gameDao().update(
                                game.getId(),
                                new Date(),
                                game.getWinner().getPlayerName(),
                                game.getPlayers().size(),
                                game.getPlayers().get(0).getPlayerName(),
                                game.getPlayers().get(1).getPlayerName(),
                                null,
                                null
                        );
                break;
            case 3:
                monopolyDatabase.gameDao().update(
                                game.getId(),
                                new Date(),
                                game.getWinner().getPlayerName(),
                                game.getPlayers().size(),
                                game.getPlayers().get(0).getPlayerName(),
                                game.getPlayers().get(1).getPlayerName(),
                                game.getPlayers().get(2).getPlayerName(),
                                null
                        );
                break;
            case 4:
                monopolyDatabase.gameDao().update(
                                game.getId(),
                                new Date(),
                                game.getWinner().getPlayerName(),
                                game.getPlayers().size(),
                                game.getPlayers().get(0).getPlayerName(),
                                game.getPlayers().get(1).getPlayerName(),
                                game.getPlayers().get(2).getPlayerName(),
                                game.getPlayers().get(3).getPlayerName()
                        );
                break;
        }
    }

    public void reinitializeFields(){
        FragmentGameBinding fragmentGameBinding = game.getFragmentBinding();
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