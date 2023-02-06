package com.example.monopoly.game.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TurnDao {

    @Insert
    long insert(TurnEntity turnEntity);

    @Query("SELECT * FROM TurnEntity")
    List<TurnEntity> getAll();

    @Query("SELECT MAX(id) FROM TurnEntity")
    long getMaxId();


    @Query("SELECT * FROM TurnEntity WHERE gameId = :gameId")
    List<TurnEntity> getAll(long gameId);

}
