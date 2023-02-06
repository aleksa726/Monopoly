package com.example.monopoly.game.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.monopoly.game.Game;

import java.util.Date;
import java.util.List;

@Dao
public interface GameDao {

    @Insert
    long insert(GameEntity gameEntity);

    @Query("SELECT * FROM GameEntity")
    List<GameEntity> getAll();

    @Query("SELECT * FROM GameEntity")
    LiveData<List<GameEntity>> getAllLiveData();

    @Query("DELETE FROM GameEntity")
    void deleteAll();

    @Query("SELECT MAX(id) FROM GameEntity")
    long getMaxId();

    @Query("UPDATE GameEntity " +
            "SET dateEnd = :dateEnd, winner = :winner,  numberOfPlayers = :numOfPlayers, " +
            "player1 = :player1, player2 = :player2, player3 = :player3, player4 = :player4 " +
            "WHERE id = :id")
    void update(long id, Date dateEnd, String winner, int numOfPlayers, String player1, String player2, String player3, String player4);

    @Query("DELETE FROM GameEntity WHERE numberOfPlayers = 0")
    void deleteUnfinishedGames();

}
