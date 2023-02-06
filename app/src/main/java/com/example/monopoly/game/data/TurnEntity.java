package com.example.monopoly.game.data;


import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(
        entity = GameEntity.class,
        parentColumns = "id",
        childColumns = "gameId",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
))
public class TurnEntity {

    @PrimaryKey(autoGenerate = true)
    long id;

    long gameId;
    int playerIndex;
    int currFieldIndex;
    int dice1;
    int dice2;
    int moneyAtStart;


    public TurnEntity(long gameId, int playerIndex, int currFieldIndex, int dice1, int dice2, int moneyAtStart) {
        this.gameId = gameId;
        this.playerIndex = playerIndex;
        this.currFieldIndex = currFieldIndex;
        this.dice1 = dice1;
        this.dice2 = dice2;
        this.moneyAtStart = moneyAtStart;
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

    public int getPlayerIndex() {
        return playerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        this.playerIndex = playerIndex;
    }

    public int getCurrFieldIndex() {
        return currFieldIndex;
    }

    public void setCurrFieldIndex(int currFieldIndex) {
        this.currFieldIndex = currFieldIndex;
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

    public int getMoneyAtStart() {
        return moneyAtStart;
    }

    public void setMoneyAtStart(int moneyAtStart) {
        this.moneyAtStart = moneyAtStart;
    }
}
