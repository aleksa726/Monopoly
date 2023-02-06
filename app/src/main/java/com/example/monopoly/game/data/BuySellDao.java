package com.example.monopoly.game.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BuySellDao {

    @Insert
    long insert(BuySellEntitiy buySellEntitiy);

    @Query("SELECT * FROM BuySellEntitiy WHERE gameId = :gameId AND turnId = :turnId")
    List<BuySellEntitiy> getAll(long gameId, long turnId);

}
