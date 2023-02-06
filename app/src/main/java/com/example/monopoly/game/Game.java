package com.example.monopoly.game;

import android.graphics.Color;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.monopoly.databinding.FragmentGameBinding;
import com.example.monopoly.game.data.GameEntity;
import com.example.monopoly.game.data.MonopolyDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class Game implements Serializable {

    private int id;

    private Date startDate = new Date();

    public static boolean buyHouseEnabled = false;
    public static boolean morgagePropertyEnabled = false;
    public static boolean sellHouseEnabled = false;

    private static int availableHouses = 32;
    private static int availableHotels = 12;

    public static Field[] fields = null;

    private ArrayList<Player> players = new ArrayList<>();
    public static Player playingPlayer = null;

    private FragmentGameBinding fragmentBinding;

    private boolean turnOver = false;
    private boolean buyEnabled = false;

    private Player winner = null;

    MonopolyDatabase monopolyDatabase;

    public Game(FragmentGameBinding activityMainBinding, MonopolyDatabase monopolyDatabase){
        fields = new Field[40];
        this.fragmentBinding = activityMainBinding;
        initFields();
        this.monopolyDatabase = monopolyDatabase;
        monopolyDatabase.gameDao().insert(new GameEntity(
                new Date(), new Date(), null, 0,null,null,null,null
        ));
        this.id = (int) monopolyDatabase.gameDao().getMaxId();
    }



    public void setPlayerInformation(){

        this.fragmentBinding.playerNameTextView.setText(this.playingPlayer.getPlayerName());
        this.fragmentBinding.moneyTextView.setText(String.valueOf(this.playingPlayer.getMoney()));
        if(!this.turnOver){
            if(playingPlayer.isInJail()) {
                this.fragmentBinding.endTurnRollDiceButton.setEnabled(false);
                fragmentBinding.endTurnRollDiceButton.setText("Roll double");
            }
            else {
                this.fragmentBinding.endTurnRollDiceButton.setEnabled(false);
                this.fragmentBinding.endTurnRollDiceButton.setText("Roll dices");
            }
        }
        else{
            this.fragmentBinding.endTurnRollDiceButton.setEnabled(true);
            this.fragmentBinding.endTurnRollDiceButton.setText("End turn");
        }
    }

    public void setDiceLabel(int dice1, int dice2){
        this.fragmentBinding.messageTextView.setText("Player rolled " + dice1 + " i " + dice2 + " = " + (dice1+dice2));
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
        Game.fields = fields;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
        playingPlayer = players.get(0);
    }

    public Player getPlayingPlayer() {
        return playingPlayer;
    }

    public void setPlayingPlayer(Player playingPlayer) {
        this.playingPlayer = playingPlayer;
    }

    public FragmentGameBinding getFragmentBinding() {
        return fragmentBinding;
    }

    public void setFragmentBinding(FragmentGameBinding fragmentBinding) {
        this.fragmentBinding = fragmentBinding;
    }

    public boolean isTurnOver() {
        return turnOver;
    }

    public void setTurnOver(boolean turnOver) {
        this.turnOver = turnOver;
        if(!this.turnOver){
            this.fragmentBinding.endTurnRollDiceButton.setEnabled(false);
            this.fragmentBinding.endTurnRollDiceButton.setText("Roll dices");
        }
        else{
            this.fragmentBinding.endTurnRollDiceButton.setEnabled(true);
            this.fragmentBinding.endTurnRollDiceButton.setText("End turn");
        }
    }

    public boolean isBuyEnabled() {
        return buyEnabled;
    }

    public void setBuyEnabled(boolean buyEnabled) {
        this.buyEnabled = buyEnabled;
        fragmentBinding.buyPropertyButton.setEnabled(buyEnabled);
    }

    public void nextPlayerTurn(){
        // TODO proveriti da li je u bakrotu sledeci igrac
        this.turnOver = false;
        this.fragmentBinding.endTurnRollDiceButton.setEnabled(false);
        fragmentBinding.endTurnRollDiceButton.setText("Roll dices");
        fragmentBinding.buyPropertyButton.setEnabled(false);

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
                // TODO prelazak na view pobednika
                this.winner = generateWinner();
                break;
            }
            else{
                break;
            }
        }
        playingPlayer = players.get(nextIndex);
    }

    private Player generateWinner(){
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
        fields[0] = new SpecialField(fragmentBinding.field0,0,"Go");
        fields[1] = new PropertyField(fragmentBinding.field1,1,"Mediterranean Avenue",60,50,50, PropertyColor.BROWN,2,2,10,30,90,160,250);
        fields[2] = new SpecialField(fragmentBinding.field2,2,"Community Chest");
        fields[3] = new PropertyField(fragmentBinding.field3,3,"Baltic Avenue",60,50,50, PropertyColor.BROWN,2,4,20,60,180,320,450);
        fields[4] = new SpecialField(fragmentBinding.field4,4,"Income Tax");
        fields[5] = new StationFiled(fragmentBinding.field5,5,"Reading Railroad");
        fields[6] = new PropertyField(fragmentBinding.field6,6,"Oriental Avenue",100,50,50, PropertyColor.LIGHT_BLUE,3,6,30,90,270,400,550);
        fields[7] = new SpecialField(fragmentBinding.field7,7,"Chance");
        fields[8] = new PropertyField(fragmentBinding.field8,8,"Vermont Avenue",100,50,50, PropertyColor.LIGHT_BLUE,3,6,30,90,270,400 ,550);
        fields[9] = new PropertyField(fragmentBinding.field9,9,"Connecticut Avenue",120,50,50, PropertyColor.LIGHT_BLUE,3,8,40,100,300,450,600);
        fields[10] = new SpecialField(fragmentBinding.field10,10,"In Jail/Just Visiting");
        fields[11] = new PropertyField(fragmentBinding.field11,11,"St. Charles Place",140,100,100, PropertyColor.PINK,3,10,50,150,450,625,750);
        fields[12] = new UtilityField(fragmentBinding.field12,12,"Electric Company");
        fields[13] = new PropertyField(fragmentBinding.field13,13,"States Avenue",140,100,100, PropertyColor.PINK,3,10,50,150,450,625,750);
        fields[14] = new PropertyField(fragmentBinding.field14,14,"Virginia Avenue",160,100,100, PropertyColor.PINK,3,12,60,180,500,700,900);
        fields[15] = new StationFiled(fragmentBinding.field15,15,"Pennsylvania Railroad");
        fields[16] = new PropertyField(fragmentBinding.field16,16,"St. James Place",180,100,100, PropertyColor.ORANGE,3,14,70,200,550,750,950);
        fields[17] = new SpecialField(fragmentBinding.field17,17,"Community Chest");
        fields[18] = new PropertyField(fragmentBinding.field18,18,"Tennessee Avenue",180,100,100, PropertyColor.ORANGE,3,14,70,200,550,750,950);
        fields[19] = new PropertyField(fragmentBinding.field19,19,"New York Avenue",200,100,100, PropertyColor.ORANGE,3,16,80,220,600,800,1000);
        fields[20] = new SpecialField(fragmentBinding.field20,20,"Free Parking");
        fields[21] = new PropertyField(fragmentBinding.field21,21,"Kentucky Avenue",220,150,150, PropertyColor.RED,3,18,90,250,700,875,1050);
        fields[22] = new SpecialField(fragmentBinding.field22,22,"Chance");
        fields[23] = new PropertyField(fragmentBinding.field23,23,"Indiana Avenue",220,150,150, PropertyColor.RED,3,18,90,250,700,875,1050);
        fields[24] = new PropertyField(fragmentBinding.field24,24,"Illinois Avenue",240,150,150, PropertyColor.RED,3,20,100,300,750,925,1100);
        fields[25] = new StationFiled(fragmentBinding.field25,25,"B. & O. Railroad");
        fields[26] = new PropertyField(fragmentBinding.field26,26,"Atlantic Avenue",260,150,150, PropertyColor.YELLOW,3,22,110,330,800,975,1150);
        fields[27] = new PropertyField(fragmentBinding.field27,27,"Ventnor Avenue",260,150,150, PropertyColor.YELLOW,3,22,110,330,800,975,1150);
        fields[28] = new UtilityField(fragmentBinding.field28,28,"Water Works");
        fields[29] = new PropertyField(fragmentBinding.field29,29,"Marvin Gardens",280,150,150, PropertyColor.YELLOW,3,24,120,360,850,1025,1200);
        fields[30] = new SpecialField(fragmentBinding.field30,30,"Go To Jail");
        fields[31] = new PropertyField(fragmentBinding.field31,31,"Pacific Avenue",300,200,200, PropertyColor.GREEN,3,26,130,390,900,1100,1275);
        fields[32] = new PropertyField(fragmentBinding.field32,32,"North Carolina Avenue",300,200,200, PropertyColor.GREEN,3,26,130,390,900,1100,1275);
        fields[33] = new SpecialField(fragmentBinding.field33,33,"Community Chest");
        fields[34] = new PropertyField(fragmentBinding.field34,34,"Pennsylvania Avenue",320,200,200, PropertyColor.GREEN,3,28,150,450,1000,1200,1400);
        fields[35] = new StationFiled(fragmentBinding.field35,35,"Short Line");
        fields[36] = new SpecialField(fragmentBinding.field36,36,"Chance");
        fields[37] = new PropertyField(fragmentBinding.field37,37,"Park Place",350,200,200, PropertyColor.DARK_BLUE,2,35,175,500,1100,1300,1500);
        fields[38] = new SpecialField(fragmentBinding.field38,38,"Luxury Tax");
        fields[39] = new PropertyField(fragmentBinding.field39,39,"Boardwalk",400,200,200, PropertyColor.DARK_BLUE,2,50,200,600,1400,1700,2000);
    }

    public static boolean isBuyHouseEnabled() {
        return buyHouseEnabled;
    }

    public static void setBuyHouseEnabled(boolean buyHouseEnabled) {
        Game.buyHouseEnabled = buyHouseEnabled;
    }

    public static boolean isMorgagePropertyEnabled() {
        return morgagePropertyEnabled;
    }

    public static void setMorgagePropertyEnabled(boolean morgagePropertyEnabled) {
        Game.morgagePropertyEnabled = morgagePropertyEnabled;
    }

    public static boolean isSellHouseEnabled() {
        return sellHouseEnabled;
    }

    public static void setSellHouseEnabled(boolean sellHouseEnabled) {
        Game.sellHouseEnabled = sellHouseEnabled;
    }

    public Player getWinner() {
        return winner;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    public void redrawFields(){
        for(Field f: fields){
            f.getFieldView().invalidate();
        }
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MonopolyDatabase getMonopolyDatabase() {
        return monopolyDatabase;
    }

    public void setMonopolyDatabase(MonopolyDatabase monopolyDatabase) {
        this.monopolyDatabase = monopolyDatabase;
    }

    public void reinitializeFields(){
        fields[0].setFieldView(fragmentBinding.field0);
        fields[1].setFieldView(fragmentBinding.field1);
        fields[2].setFieldView(fragmentBinding.field2);
        fields[3].setFieldView(fragmentBinding.field3);
        fields[4].setFieldView(fragmentBinding.field4);
        fields[5].setFieldView(fragmentBinding.field5);
        fields[6].setFieldView(fragmentBinding.field6);
        fields[7].setFieldView(fragmentBinding.field7);
        fields[8].setFieldView(fragmentBinding.field8);
        fields[9].setFieldView(fragmentBinding.field9);
        fields[10].setFieldView(fragmentBinding.field10);
        fields[11].setFieldView(fragmentBinding.field11);
        fields[12].setFieldView(fragmentBinding.field12);
        fields[13].setFieldView(fragmentBinding.field13);
        fields[14].setFieldView(fragmentBinding.field14);
        fields[15].setFieldView(fragmentBinding.field15);
        fields[16].setFieldView(fragmentBinding.field16);
        fields[17].setFieldView(fragmentBinding.field17);
        fields[18].setFieldView(fragmentBinding.field18);
        fields[19].setFieldView(fragmentBinding.field19);
        fields[20].setFieldView(fragmentBinding.field20);
        fields[21].setFieldView(fragmentBinding.field21);
        fields[22].setFieldView(fragmentBinding.field22);
        fields[23].setFieldView(fragmentBinding.field23);
        fields[24].setFieldView(fragmentBinding.field24);
        fields[25].setFieldView(fragmentBinding.field25);
        fields[26].setFieldView(fragmentBinding.field26);
        fields[27].setFieldView(fragmentBinding.field27);
        fields[28].setFieldView(fragmentBinding.field28);
        fields[29].setFieldView(fragmentBinding.field29);
        fields[30].setFieldView(fragmentBinding.field30);
        fields[31].setFieldView(fragmentBinding.field31);
        fields[32].setFieldView(fragmentBinding.field32);
        fields[33].setFieldView(fragmentBinding.field33);
        fields[34].setFieldView(fragmentBinding.field34);
        fields[35].setFieldView(fragmentBinding.field35);
        fields[36].setFieldView(fragmentBinding.field36);
        fields[37].setFieldView(fragmentBinding.field37);
        fields[38].setFieldView(fragmentBinding.field38);
        fields[39].setFieldView(fragmentBinding.field39);
    }


}
