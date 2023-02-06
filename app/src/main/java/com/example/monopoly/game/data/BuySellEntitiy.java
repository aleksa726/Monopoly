package com.example.monopoly.game.data;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = {
        @ForeignKey(
                entity = GameEntity.class,
                parentColumns = "id",
                childColumns = "gameId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE
        ),
        @ForeignKey(
                entity = TurnEntity.class,
                parentColumns = "id",
                childColumns = "turnId",
                onDelete = ForeignKey.CASCADE,
                onUpdate = ForeignKey.CASCADE)
})
public class BuySellEntitiy {

    @PrimaryKey(autoGenerate = true)
    long id;

    long gameId;
    long turnId;
    int fieldToBuyIndex;
    boolean buyField;
    boolean mortgageField;
    boolean buyHouse;
    boolean sellHouse;

    public BuySellEntitiy(long gameId, long turnId, int fieldToBuyIndex, boolean buyField, boolean mortgageField, boolean buyHouse, boolean sellHouse) {
        this.gameId = gameId;
        this.turnId = turnId;
        this.fieldToBuyIndex = fieldToBuyIndex;
        this.buyField = buyField;
        this.mortgageField = mortgageField;
        this.buyHouse = buyHouse;
        this.sellHouse = sellHouse;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGameId() {
        return gameId;
    }

    public void setGameId(long gameId) {
        this.gameId = gameId;
    }

    public long getTurnId() {
        return turnId;
    }

    public void setTurnId(long turnId) {
        this.turnId = turnId;
    }

    public int getFieldToBuyIndex() {
        return fieldToBuyIndex;
    }

    public void setFieldToBuyIndex(int fieldToBuyIndex) {
        this.fieldToBuyIndex = fieldToBuyIndex;
    }

    public boolean isBuyField() {
        return buyField;
    }

    public void setBuyField(boolean buyField) {
        this.buyField = buyField;
    }

    public boolean isMortgageField() {
        return mortgageField;
    }

    public void setMortgageField(boolean mortgageField) {
        this.mortgageField = mortgageField;
    }

    public boolean isBuyHouse() {
        return buyHouse;
    }

    public void setBuyHouse(boolean buyHouse) {
        this.buyHouse = buyHouse;
    }

    public boolean isSellHouse() {
        return sellHouse;
    }

    public void setSellHouse(boolean sellHouse) {
        this.sellHouse = sellHouse;
    }
}
