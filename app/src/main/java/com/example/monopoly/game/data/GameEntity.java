package com.example.monopoly.game.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.monopoly.game.Player;

import java.util.Date;

@Entity
public class GameEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private Date dateStart;
    private Date dateEnd;
    private String winner;
    private int numberOfPlayers;
    private String player1;
    private String player2;
    private String player3;
    private String player4;

    public GameEntity(Date dateStart, Date dateEnd, String winner, int numberOfPlayers,
                      String player1, String player2, String player3, String player4) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.winner = winner;
        this.numberOfPlayers = numberOfPlayers;
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getPlayer3() {
        return player3;
    }

    public void setPlayer3(String player3) {
        this.player3 = player3;
    }

    public String getPlayer4() {
        return player4;
    }

    public void setPlayer4(String player4) {
        this.player4 = player4;
    }
}
