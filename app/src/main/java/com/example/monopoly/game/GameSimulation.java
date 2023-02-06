package com.example.monopoly.game;

import android.graphics.Color;

import com.example.monopoly.databinding.FragmentGameBinding;
import com.example.monopoly.databinding.FragmentSimulationBinding;
import com.example.monopoly.game.data.GameEntity;
import com.example.monopoly.game.data.MonopolyDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class GameSimulation implements Serializable {

    private int id;

    private static int availableHouses = 32;
    private static int availableHotels = 12;

    public static Field[] fields;

    private ArrayList<PlayerSimulation> players = new ArrayList<>();
    public static PlayerSimulation playingPlayer = null;

    private FragmentSimulationBinding fragmentSimulationBinding;


    private PlayerSimulation winner = null;


    public GameSimulation(FragmentSimulationBinding activityMainBinding, int id){
        fields = new Field[40];
        this.fragmentSimulationBinding = activityMainBinding;
        initFields();
        this.id = id;
    }



    public void setPlayerInformation(){
        this.fragmentSimulationBinding.playerNameTextView.setText(this.playingPlayer.getPlayerName());
        this.fragmentSimulationBinding.moneyTextView.setText(String.valueOf(this.playingPlayer.getMoney()));
    }

    public void setDiceLabel(int dice1, int dice2){
        this.fragmentSimulationBinding.messageTextView.setText("Player rolled " + dice1 + " i " + dice2 + " = " + (dice1+dice2));
    }


    public static void incAvailableHouses(){
        availableHouses++;
    }

    public static void decAvailableHouses(){
        availableHouses--;
    }

    public static void incAvailableHotels(){
        availableHotels++;
    }

    public static void decAvailableHotels(){
        availableHotels--;
    }

    public static int getAvailableHouses() {
        return availableHouses;
    }

    public static void setAvailableHouses(int houses) {
        availableHouses = houses;
    }

    public static int getAvailableHotels() {
        return availableHotels;
    }

    public static void setAvailableHotels(int hotels) {
        availableHotels = hotels;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        GameSimulation.fields = fields;
    }

    public ArrayList<PlayerSimulation> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<PlayerSimulation> players) {
        this.players = players;
        playingPlayer = players.get(0);
    }

    public PlayerSimulation getPlayingPlayer() {
        return playingPlayer;
    }

    public void setPlayingPlayer(PlayerSimulation playingPlayer) {
        this.playingPlayer = playingPlayer;
    }

    public FragmentSimulationBinding getFragmentBinding() {
        return fragmentSimulationBinding;
    }

    public void setFragmentBinding(FragmentSimulationBinding fragmentBinding) {
        this.fragmentSimulationBinding = fragmentBinding;
    }






    public void nextPlayerTurn(){
        generateNextPlayer();

        this.setPlayerInformation();
    }

    private void generateNextPlayer() {
        int currIndex = -1;
        for(int i = 0; i < players.size(); i++){
            if(playingPlayer == players.get(i)) {
                currIndex = i;
                break;
            }
        }

        int bankruptedPlayers = getBukroptedPlayers();
        if(bankruptedPlayers == this.players.size() - 1){
            this.winner = generateWinner();
        }

        int nextIndex = (currIndex + 1) % players.size();
        while(true){
            if(players.get(nextIndex).isBankrupt()){
                nextIndex = (nextIndex + 1) % players.size();
            }
            else if(nextIndex == currIndex){
                this.winner = generateWinner();
                break;
            }
            else{
                break;
            }
        }
        playingPlayer = players.get(nextIndex);
    }

    private PlayerSimulation generateWinner(){
        int index = 0;
        int cnt = 0;
        for(Player p: this.players){
            if(!p.isBankrupt())
                index = cnt;
            cnt++;
        }
        return players.get(index);
    }

    private int getBukroptedPlayers() {
        int cnt = 0;
        for(Player p: this.players){
            if (p.isBankrupt())
                cnt++;
        }
        return cnt;
    }

    public int getPlayingPlayerIndex(){
        switch (playingPlayer.getColor()){
            case Color.BLUE:
                return 0;
            case Color.MAGENTA:
                return 1;
            case Color.CYAN:
                return 2;
            case Color.GREEN:
                return 3;
        }
        return -1;
    }

    private void initFields(){
        fields[0] = new SpecialField(fragmentSimulationBinding.field0,0,"Go");
        fields[1] = new PropertyField(fragmentSimulationBinding.field1,1,"Mediterranean Avenue",60,50,50, PropertyColor.BROWN,2,2,10,30,90,160,250);
        fields[2] = new SpecialField(fragmentSimulationBinding.field2,2,"Community Chest");
        fields[3] = new PropertyField(fragmentSimulationBinding.field3,3,"Baltic Avenue",60,50,50, PropertyColor.BROWN,2,4,20,60,180,320,450);
        fields[4] = new SpecialField(fragmentSimulationBinding.field4,4,"Income Tax");
        fields[5] = new StationFiled(fragmentSimulationBinding.field5,5,"Reading Railroad");
        fields[6] = new PropertyField(fragmentSimulationBinding.field6,6,"Oriental Avenue",100,50,50, PropertyColor.LIGHT_BLUE,3,6,30,90,270,400,550);
        fields[7] = new SpecialField(fragmentSimulationBinding.field7,7,"Chance");
        fields[8] = new PropertyField(fragmentSimulationBinding.field8,8,"Vermont Avenue",100,50,50, PropertyColor.LIGHT_BLUE,3,6,30,90,270,400 ,550);
        fields[9] = new PropertyField(fragmentSimulationBinding.field9,9,"Connecticut Avenue",120,50,50, PropertyColor.LIGHT_BLUE,3,8,40,100,300,450,600);
        fields[10] = new SpecialField(fragmentSimulationBinding.field10,10,"In Jail/Just Visiting");
        fields[11] = new PropertyField(fragmentSimulationBinding.field11,11,"St. Charles Place",140,100,100, PropertyColor.PINK,3,10,50,150,450,625,750);
        fields[12] = new UtilityField(fragmentSimulationBinding.field12,12,"Electric Company");
        fields[13] = new PropertyField(fragmentSimulationBinding.field13,13,"States Avenue",140,100,100, PropertyColor.PINK,3,10,50,150,450,625,750);
        fields[14] = new PropertyField(fragmentSimulationBinding.field14,14,"Virginia Avenue",160,100,100, PropertyColor.PINK,3,12,60,180,500,700,900);
        fields[15] = new StationFiled(fragmentSimulationBinding.field15,15,"Pennsylvania Railroad");
        fields[16] = new PropertyField(fragmentSimulationBinding.field16,16,"St. James Place",180,100,100, PropertyColor.ORANGE,3,14,70,200,550,750,950);
        fields[17] = new SpecialField(fragmentSimulationBinding.field17,17,"Community Chest");
        fields[18] = new PropertyField(fragmentSimulationBinding.field18,18,"Tennessee Avenue",180,100,100, PropertyColor.ORANGE,3,14,70,200,550,750,950);
        fields[19] = new PropertyField(fragmentSimulationBinding.field19,19,"New York Avenue",200,100,100, PropertyColor.ORANGE,3,16,80,220,600,800,1000);
        fields[20] = new SpecialField(fragmentSimulationBinding.field20,20,"Free Parking");
        fields[21] = new PropertyField(fragmentSimulationBinding.field21,21,"Kentucky Avenue",220,150,150, PropertyColor.RED,3,18,90,250,700,875,1050);
        fields[22] = new SpecialField(fragmentSimulationBinding.field22,22,"Chance");
        fields[23] = new PropertyField(fragmentSimulationBinding.field23,23,"Indiana Avenue",220,150,150, PropertyColor.RED,3,18,90,250,700,875,1050);
        fields[24] = new PropertyField(fragmentSimulationBinding.field24,24,"Illinois Avenue",240,150,150, PropertyColor.RED,3,20,100,300,750,925,1100);
        fields[25] = new StationFiled(fragmentSimulationBinding.field25,25,"B. & O. Railroad");
        fields[26] = new PropertyField(fragmentSimulationBinding.field26,26,"Atlantic Avenue",260,150,150, PropertyColor.YELLOW,3,22,110,330,800,975,1150);
        fields[27] = new PropertyField(fragmentSimulationBinding.field27,27,"Ventnor Avenue",260,150,150, PropertyColor.YELLOW,3,22,110,330,800,975,1150);
        fields[28] = new UtilityField(fragmentSimulationBinding.field28,28,"Water Works");
        fields[29] = new PropertyField(fragmentSimulationBinding.field29,29,"Marvin Gardens",280,150,150, PropertyColor.YELLOW,3,24,120,360,850,1025,1200);
        fields[30] = new SpecialField(fragmentSimulationBinding.field30,30,"Go To Jail");
        fields[31] = new PropertyField(fragmentSimulationBinding.field31,31,"Pacific Avenue",300,200,200, PropertyColor.GREEN,3,26,130,390,900,1100,1275);
        fields[32] = new PropertyField(fragmentSimulationBinding.field32,32,"North Carolina Avenue",300,200,200, PropertyColor.GREEN,3,26,130,390,900,1100,1275);
        fields[33] = new SpecialField(fragmentSimulationBinding.field33,33,"Community Chest");
        fields[34] = new PropertyField(fragmentSimulationBinding.field34,34,"Pennsylvania Avenue",320,200,200, PropertyColor.GREEN,3,28,150,450,1000,1200,1400);
        fields[35] = new StationFiled(fragmentSimulationBinding.field35,35,"Short Line");
        fields[36] = new SpecialField(fragmentSimulationBinding.field36,36,"Chance");
        fields[37] = new PropertyField(fragmentSimulationBinding.field37,37,"Park Place",350,200,200, PropertyColor.DARK_BLUE,2,35,175,500,1100,1300,1500);
        fields[38] = new SpecialField(fragmentSimulationBinding.field38,38,"Luxury Tax");
        fields[39] = new PropertyField(fragmentSimulationBinding.field39,39,"Boardwalk",400,200,200, PropertyColor.DARK_BLUE,2,50,200,600,1400,1700,2000);
    }







    public Player getWinner() {
        return winner;
    }

    public void setWinner(PlayerSimulation winner) {
        this.winner = winner;
    }

    public void redrawFields(){
        for(Field f: fields){
            f.getFieldView().invalidate();
        }
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public void reinitializeFields(){
        fields[0].setFieldView(fragmentSimulationBinding.field0);
        fields[1].setFieldView(fragmentSimulationBinding.field1);
        fields[2].setFieldView(fragmentSimulationBinding.field2);
        fields[3].setFieldView(fragmentSimulationBinding.field3);
        fields[4].setFieldView(fragmentSimulationBinding.field4);
        fields[5].setFieldView(fragmentSimulationBinding.field5);
        fields[6].setFieldView(fragmentSimulationBinding.field6);
        fields[7].setFieldView(fragmentSimulationBinding.field7);
        fields[8].setFieldView(fragmentSimulationBinding.field8);
        fields[9].setFieldView(fragmentSimulationBinding.field9);
        fields[10].setFieldView(fragmentSimulationBinding.field10);
        fields[11].setFieldView(fragmentSimulationBinding.field11);
        fields[12].setFieldView(fragmentSimulationBinding.field12);
        fields[13].setFieldView(fragmentSimulationBinding.field13);
        fields[14].setFieldView(fragmentSimulationBinding.field14);
        fields[15].setFieldView(fragmentSimulationBinding.field15);
        fields[16].setFieldView(fragmentSimulationBinding.field16);
        fields[17].setFieldView(fragmentSimulationBinding.field17);
        fields[18].setFieldView(fragmentSimulationBinding.field18);
        fields[19].setFieldView(fragmentSimulationBinding.field19);
        fields[20].setFieldView(fragmentSimulationBinding.field20);
        fields[21].setFieldView(fragmentSimulationBinding.field21);
        fields[22].setFieldView(fragmentSimulationBinding.field22);
        fields[23].setFieldView(fragmentSimulationBinding.field23);
        fields[24].setFieldView(fragmentSimulationBinding.field24);
        fields[25].setFieldView(fragmentSimulationBinding.field25);
        fields[26].setFieldView(fragmentSimulationBinding.field26);
        fields[27].setFieldView(fragmentSimulationBinding.field27);
        fields[28].setFieldView(fragmentSimulationBinding.field28);
        fields[29].setFieldView(fragmentSimulationBinding.field29);
        fields[30].setFieldView(fragmentSimulationBinding.field30);
        fields[31].setFieldView(fragmentSimulationBinding.field31);
        fields[32].setFieldView(fragmentSimulationBinding.field32);
        fields[33].setFieldView(fragmentSimulationBinding.field33);
        fields[34].setFieldView(fragmentSimulationBinding.field34);
        fields[35].setFieldView(fragmentSimulationBinding.field35);
        fields[36].setFieldView(fragmentSimulationBinding.field36);
        fields[37].setFieldView(fragmentSimulationBinding.field37);
        fields[38].setFieldView(fragmentSimulationBinding.field38);
        fields[39].setFieldView(fragmentSimulationBinding.field39);
    }


}
