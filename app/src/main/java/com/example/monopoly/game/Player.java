package com.example.monopoly.game;

import com.example.monopoly.game.data.BuySellEntitiy;
import com.example.monopoly.game.data.TurnEntity;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private int money = 150;
    private String playerName;
    private List<OwnableField> ownedFields = new ArrayList<>();
    private Field currentField;
    private int color;
    private boolean inJail = false;
    private boolean rolledDouble = false;
    private int rolledCntJail = 0;
    private int rolledDoubleCnt = 0;
    private Game game;
    private int dice;
    private boolean bankrupt;
    private int owedMoney = 0;
    private Player playerYouOwe = null;
    private int dice1;
    private int dice2;

    public Player(){

    }

    public Player(String playerName, int color, Game game){
        this.playerName = playerName;
        this.color = color;
        this.game = game;
        currentField = Game.fields[0];
    }


    public int rollDice(){

        game.getMonopolyDatabase().turnDao().insert(
                new TurnEntity(
                        game.getId(),
                        game.getPlayingPlayerIndex(),
                        currentField.getFieldNumber(),
                        dice1,
                        dice2,
                        money)
        );

        if(this.getPlayerName().equals("a")) {
            if(((OwnableField)Game.fields[1]).getOwner() == null) {
                money += 1000;
                buyProperty((OwnableField) Game.fields[1]);
                buyProperty((OwnableField) Game.fields[3]);
            }
        }

        if(this.getPlayerName().equals("b")) {
            if(((OwnableField)Game.fields[19]).getOwner() == null) {
                money+=100;
                buyProperty((OwnableField) Game.fields[19]);
            }
        }
        /*dice1 = 1;
        dice2 = 2;*/




        boolean wasInJail = false;
        if(inJail){
            this.game.getFragmentBinding().buyPropertyButton.setEnabled(false);
            wasInJail = true;
            rolledCntJail++;

            if(rolledDouble){
                inJail = false;
                rolledCntJail = 0;
                rolledDoubleCnt = 0;
                this.game.getFragmentBinding().buyPropertyButton.setText("Buy property");
            }
            else{
                if(rolledCntJail == 3){
                    this.payToGetOut();
                    rolledCntJail = 0;
                }
            }
        }

        if(!inJail) {
            if (rolledDoubleCnt == 3) {
                this.game.getFragmentBinding().messageTextView.setText("3rd double dice\nGo to jail");
                goToJail();
                rolledDoubleCnt = 0;
                rolledDouble = false;
            } else
                this.getToNextField();
            if (rolledDouble && !wasInJail && !inJail)
                game.setTurnOver(false);
            else
                game.setTurnOver(true);
        }
        else{
            game.setTurnOver(true);
        }

        return this.dice;
    }



    public void getToNextField(){
        int currFieldNum = this.currentField.getFieldNumber();

        int nextFieldNumber = (currFieldNum + this.dice) % 40;

        if (nextFieldNumber != 30) {
            if (nextFieldNumber < currFieldNum)
                this.money += 200;
        } else {
            goToJail();
        }

        this.removePlayerFromCurrentField();
        this.currentField = Game.fields[nextFieldNumber];
        this.addPlayerToCurrentField();

        if (currentField instanceof OwnableField) {

            OwnableField ownableField = (OwnableField) currentField;

            if (ownableField.getOwner() == null) {
                game.setBuyEnabled(true);
            } else {
                game.setBuyEnabled(false);
                if (ownableField.getOwner() != this) {
                    int tax = ownableField.calculateRent(this.dice);
                    this.game.getFragmentBinding().messageTextView.setText("You need to pay $"
                            + tax + " to " + ownableField.getOwner().getPlayerName());
                    if ((this.money - tax) < 0) {
                        this.owedMoney = tax;
                        this.playerYouOwe = ownableField.getOwner();
                        this.game.getFragmentBinding().messageTextView.setText(
                                this.game.getFragmentBinding().messageTextView.getText() + "\nYou need to raise $" + (tax - this.money));
                    } else {
                        this.money -= tax;
                        ownableField.getOwner().addMoney(tax);
                    }
                }
            }
        } else if (currentField instanceof SpecialField) {
            game.setBuyEnabled(false);
            switch (currentField.getName()) {
                case ("Go To Jail"):
                    goToJail();
                    break;
                case ("Community Chest"):
                    CommunityChest communityChest = SpecialField.getRandomCommunityChestCard();
                    this.game.getFragmentBinding().messageTextView.setText(communityChest.getMessage());
                    this.money += communityChest.getMoney();
                    if (communityChest.getFieldToGo() != -1) {

                        if (this.currentField.fieldNumber > communityChest.getFieldToGo()) {
                            if (communityChest.getFieldToGo() != 10) {
                                this.money += 200;
                            }
                        }

                        this.removePlayerFromCurrentField();
                        this.currentField = Game.fields[communityChest.getFieldToGo()];
                        this.addPlayerToCurrentField();

                        if (communityChest.getFieldToGo() == 10) {
                            this.inJail = true;
                        }
                    }
                    break;
                case ("Income Tax"):
                    this.money -= 200;
                    break;
                case ("Chance"):
                    Chance chance = SpecialField.getRandomChanceCard();
                    this.game.getFragmentBinding().messageTextView.setText(chance.getMessage());
                    this.money += chance.getMoney();
                    if (chance.getFieldToGo() != -1) {

                        if (this.currentField.fieldNumber > chance.getFieldToGo()) {
                            if (chance.getFieldToGo() != 10) {
                                this.money += 200;
                            }
                        }
                        this.removePlayerFromCurrentField();
                        this.currentField = Game.fields[chance.getFieldToGo()];
                        this.addPlayerToCurrentField();

                        if (chance.getFieldToGo() == 10) {
                            this.inJail = true;
                        }
                    }
                    break;
                case ("Luxury Tax"):
                    this.money -= 100;
                    break;
            }
        } else {
            throw new RuntimeException("ERROR");
        }

        this.game.setPlayerInformation();

    }

    public void addMoney(int tax) {
        this.money += tax;
    }


    public void payToGetOut(){
        this.money -= 50;
        this.inJail = false;
        game.setPlayerInformation();
        this.game.getFragmentBinding().buyPropertyButton.setText("Buy property");
        this.game.getFragmentBinding().endTurnRollDiceButton.setEnabled(false);
        this.game.getFragmentBinding().endTurnRollDiceButton.setText("Roll dices");
        this.game.getFragmentBinding().buyPropertyButton.setEnabled(false);
    }

    public void goToJail(){
        this.removePlayerFromCurrentField();
        this.currentField = Game.fields[10];
        this.addPlayerToCurrentField();
        this.inJail = true;
        this.game.getFragmentBinding().buyPropertyButton.setEnabled(false);
    }

    public void addPlayerToCurrentField(){
        currentField.getFieldView().addPlayer(this);
        currentField.getFieldView().invalidate();
    }

    public void removePlayerFromCurrentField(){
        currentField.getFieldView().removePlayer(this);
        currentField.getFieldView().invalidate();
    }


    public void buyProperty(OwnableField newField){
        if(this.money > newField.getPrice()) {
            this.ownedFields.add(newField);
            this.money -= newField.getPrice();
            newField.setOwner(this);
            newField.getFieldView().setOwner(this);
            newField.getFieldView().invalidate();
            if(currentField instanceof OwnableField) {
                this.game.getFragmentBinding().messageTextView.setText("Bought "
                        + currentField.getName() + " for $" + ((OwnableField) currentField).getPrice());
            }
            this.game.getFragmentBinding().buyPropertyButton.setEnabled(false);
            this.game.setPlayerInformation();

            game.getMonopolyDatabase().buySellDao().insert(new BuySellEntitiy(
                    game.getId(),
                    game.monopolyDatabase.turnDao().getMaxId(),
                    newField.getFieldNumber(),
                    true,
                    false,
                    false,
                    false
            ));
        }
    }

    public void buyHouse(int propertyFieldId){
        Field field = Game.fields[propertyFieldId];
        if(field instanceof PropertyField){
            PropertyField propertyField = (PropertyField) Game.fields[propertyFieldId];

            if(this != propertyField.getOwner())
                return;

            if(Game.getAvailableHouses() == 0)
                return;

            if(propertyField.getHotelCnt() != 0)
                return;

            if(propertyField.isMorgaged())
                return;

            int ownedColorFields = this.getPropertiesCnt(propertyField.getPropertyColor());
            if(ownedColorFields == propertyField.getPropertySetCnt()) {
                int housePrice = propertyField.getHousePrice();
                if (this.money < housePrice)
                    return;
                if (propertyField.getHouseCnt() == 4) {
                    this.buyHotel(propertyFieldId);
                    return;
                }
                if (!allFieldsHaveEqualHouses(propertyField.getPropertyColor(), propertyField))
                    return;
                this.money -= housePrice;
                propertyField.incHouses();
                propertyField.getFieldView().invalidate();
                game.setPlayerInformation();
                Game.decAvailableHouses();

                game.getMonopolyDatabase().buySellDao().insert(new BuySellEntitiy(
                        game.getId(),
                        game.monopolyDatabase.turnDao().getMaxId(),
                        propertyFieldId,
                        false,
                        false,
                        true,
                        false
                ));
            }
        }
    }

    public void sellHouse(int propertyFieldId){
        Field field = Game.fields[propertyFieldId];
        if(field instanceof PropertyField) {
            PropertyField propertyField = (PropertyField) Game.fields[propertyFieldId];

            if (this != propertyField.getOwner())
                return;

            if(propertyField.getHotelCnt() != 0){
                sellHotel(propertyField);
                return;
            }

            if (propertyField.getHouseCnt() == 0)
                return;
            if (!allFieldsHaveEqualHousesSell(propertyField.getPropertyColor(), propertyField))
                return;
            this.money += propertyField.getHousePrice()/2;
            propertyField.decHouses();
            propertyField.getFieldView().invalidate();
            game.setPlayerInformation();
            Game.incAvailableHouses();

            game.getMonopolyDatabase().buySellDao().insert(new BuySellEntitiy(
                    game.getId(),
                    game.monopolyDatabase.turnDao().getMaxId(),
                    propertyFieldId,
                    false,
                    false,
                    false,
                    true
            ));
        }
    }

    public boolean allFieldsHaveEqualHouses(PropertyColor propertyColor, PropertyField propertyFieldToBuyHouse){
        int currHouses = propertyFieldToBuyHouse.getHouseCnt()+1;
        boolean flag = true;
        for(OwnableField p: this.ownedFields){
            if(p instanceof PropertyField){
                PropertyField propertyField = (PropertyField) p;
                if(propertyField.getPropertyColor() == propertyColor) {
                    if(Math.abs(currHouses - propertyField.getHouseCnt()) > 1)
                        flag = false;
                }
            }
        }
        return flag;
    }

    public boolean allFieldsHaveEqualHousesSell(PropertyColor propertyColor, PropertyField propertyFieldToBuyHouse){
        int currHouses = propertyFieldToBuyHouse.getHouseCnt()-1;
        boolean flag = true;
        for(OwnableField p: this.ownedFields){
            if(p instanceof PropertyField){
                PropertyField propertyField = (PropertyField) p;
                if(propertyField.getPropertyColor() == propertyColor) {
                    if(p != propertyFieldToBuyHouse) {
                        int x = Math.abs(currHouses - propertyField.getHouseCnt());
                        if (x > 1)
                            flag = false;

                    }
                }
            }
        }
        return flag;
    }


    public void buyHotel(int propertyFieldId){
        Field field = Game.fields[propertyFieldId];
        if(field instanceof PropertyField) {
            PropertyField propertyField = (PropertyField) Game.fields[propertyFieldId];
            if (Game.getAvailableHotels() == 0)
                return;

            if (propertyField.getHouseCnt() != 4)
                return;
            int ownedColorFields = this.getPropertiesCnt(propertyField.getPropertyColor());
            if (ownedColorFields == propertyField.getPropertySetCnt()) {
                int hotelPrice = propertyField.getHotelPrice();
                if (this.money < hotelPrice)
                    return;
                if (!allFieldsHave4Houses(propertyField.getPropertyColor()))
                    return;
                this.money -= hotelPrice;
                propertyField.incHotel();
                propertyField.setHouseCnt(0);
                propertyField.getFieldView().invalidate();
                game.setPlayerInformation();
                Game.incAvailableHouses();
                Game.incAvailableHouses();
                Game.incAvailableHouses();
                Game.incAvailableHouses();
                Game.decAvailableHotels();

                game.getMonopolyDatabase().buySellDao().insert(new BuySellEntitiy(
                        game.getId(),
                        game.monopolyDatabase.turnDao().getMaxId(),
                        propertyFieldId,
                        false,
                        false,
                        true,
                        false
                ));
            }
        }
    }

    public void sellHotel(PropertyField propertyField){
        if(propertyField.getHotelCnt() != 1)
            return;
        this.money += propertyField.getHotelPrice()/2;
        propertyField.decHotel();
        propertyField.setHouseCnt(4);
        propertyField.getFieldView().invalidate();
        game.setPlayerInformation();
        Game.incAvailableHotels();
        Game.decAvailableHouses();
        Game.decAvailableHouses();
        Game.decAvailableHouses();
        Game.decAvailableHouses();

        game.getMonopolyDatabase().buySellDao().insert(new BuySellEntitiy(
                game.getId(),
                game.monopolyDatabase.turnDao().getMaxId(),
                propertyField.fieldNumber,
                false,
                false,
                false,
                true
        ));
    }

    public boolean allFieldsHave4Houses(PropertyColor propertyColor){
        boolean flag = true;
        for(OwnableField p: this.ownedFields){
            if(p instanceof PropertyField){
                PropertyField propertyField = (PropertyField) p;
                if(propertyField.getPropertyColor() == propertyColor) {
                    if(propertyField.getHouseCnt() != 4)
                        if(propertyField.getHotelCnt() != 1)
                            flag = false;
                }
            }
        }
        return flag;
    }

    public void morgageProperty(int ownableFieldId){
        Field field = Game.fields[ownableFieldId];
        if(field instanceof OwnableField) {
            OwnableField ownableField = (OwnableField) Game.fields[ownableFieldId];

            if(ownableField instanceof PropertyField){
                PropertyField propertyField = (PropertyField) ownableField;
                if(propertyField.getHotelCnt() != 0)
                    return;
                if(propertyField.getHouseCnt() != 0)
                    return;
            }

            if (this != ownableField.getOwner())
                return;
            if (!ownableField.isMorgaged()) {
                ownableField.setMorgaged(true);
                this.money += ownableField.getMorgagePrice();

                game.getMonopolyDatabase().buySellDao().insert(new BuySellEntitiy(
                        game.getId(),
                        game.monopolyDatabase.turnDao().getMaxId(),
                        ownableFieldId,
                        false,
                        true,
                        false,
                        false
                ));
            }
            else{
                unmorgageProperty(ownableField);
            }
            ownableField.getFieldView().invalidate();
            game.setPlayerInformation();
        }
    }

    public void unmorgageProperty(OwnableField ownableField){
        if(ownableField.isMorgaged()) {
            ownableField.setMorgaged(false);
            if ((this.money - ownableField.getMorgagePrice()) >= 0) {
                this.money -= (int) (ownableField.getMorgagePrice() * 1.1);
                game.getMonopolyDatabase().buySellDao().insert(new BuySellEntitiy(
                        game.getId(),
                        game.monopolyDatabase.turnDao().getMaxId(),
                        ownableField.getFieldNumber(),
                        false,
                        true,
                        false,
                        false
                ));
            }
        }
    }



    public int getPropertiesCnt(PropertyColor propertyColor) {
        int cnt = 0;
        for(Field f: this.ownedFields){
            if(f instanceof PropertyField){
                if(((PropertyField) f).getPropertyColor() == propertyColor)
                    cnt++;
            }
        }
        return cnt;
    }

    public int getStationPropertiesCnt() {
        int cnt = 0;
        for(Field f: this.ownedFields){
            if(f instanceof StationFiled){
                cnt++;
            }
        }
        return cnt;
    }

    public int getUtilityPropertiesCnt() {
        int cnt = 0;
        for(Field f: this.ownedFields){
            if(f instanceof UtilityField){
                cnt++;
            }
        }
        return cnt;
    }




    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<OwnableField> getOwnedFields() {
        return ownedFields;
    }

    public void setOwnedFields(List<OwnableField> ownedFields) {
        this.ownedFields = ownedFields;
    }

    public Field getCurrentField() {
        return currentField;
    }

    public void setCurrentField(Field currentField) {
        this.currentField = currentField;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isInJail() {
        return inJail;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }

    public boolean isRolledDouble() {
        return rolledDouble;
    }

    public void setRolledDouble(boolean rolledDouble) {
        this.rolledDouble = rolledDouble;
    }

    public int getRolledCntJail() {
        return rolledCntJail;
    }

    public void setRolledCntJail(int rolledCntJail) {
        this.rolledCntJail = rolledCntJail;
    }

    public int getRolledDoubleCnt() {
        return rolledDoubleCnt;
    }

    public void setRolledDoubleCnt(int rolledDoubleCnt) {
        this.rolledDoubleCnt = rolledDoubleCnt;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public int getDice() {
        return dice;
    }

    public void setDice(int dice1, int dice2) {
        this.dice1 = dice1;
        this.dice2 = dice2;
        if(dice1 == dice2){
            rolledDoubleCnt++;
            rolledDouble = true;
        }
        else{
            rolledDouble = false;
            rolledDoubleCnt = 0;
        }
        this.dice = dice1 + dice2;
    }

    public void setDice(int dice) {
        this.dice = dice;
    }

    public boolean isBankrupt() {
        return bankrupt;
    }

    public void setBankrupt(boolean bankrupt) {
        this.bankrupt = bankrupt;
    }

    public int getOwedMoney() {
        return owedMoney;
    }

    public void setOwedMoney(int ownedMoney) {
        this.owedMoney = ownedMoney;
    }

    public Player getPlayerYouOwe() {
        return playerYouOwe;
    }

    public void setPlayerYouOwe(Player playerYouOwe) {
        this.playerYouOwe = playerYouOwe;
    }

    public void decMoney(int tax){
        this.money -= tax;
    }

    public boolean hasSomethingToSell(){
        for(OwnableField field: this.ownedFields){
            if(!field.isMorgaged())
                return true;
            if(field instanceof PropertyField){
                PropertyField propertyField = (PropertyField) field;
                if(propertyField.getHouseCnt() != 0)
                    return true;
                if(propertyField.getHotelCnt() != 0)
                    return true;
            }
        }
        return false;
    }

    public void transferProperties(Player playerYouOwe) {
        for(OwnableField field: this.ownedFields){
            field.setOwner(playerYouOwe);
            field.getFieldView().setOwner(playerYouOwe);
            playerYouOwe.addProperty(field);
            field.getFieldView().invalidate();
        }
    }

    private void addProperty(OwnableField field) {
        this.ownedFields.add(field);
    }

    public int getDice1() {
        return dice1;
    }

    public void setDice1(int dice1) {
        this.dice1 = dice1;
    }

    public int getDice2() {
        return dice2;
    }

    public void setDice2(int dice2) {
        this.dice2 = dice2;
    }
}
