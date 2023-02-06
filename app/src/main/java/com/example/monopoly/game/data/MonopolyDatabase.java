package com.example.monopoly.game.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;


@TypeConverters(value = {DateConverter.class})
@Database(entities = {GameEntity.class, TurnEntity.class, BuySellEntitiy.class}, version = 1, exportSchema = false)
public abstract class MonopolyDatabase  extends RoomDatabase {

    public abstract GameDao gameDao();
    public abstract TurnDao turnDao();
    public abstract BuySellDao buySellDao();

    private static final String DATABASE_NAME = "monopoly-app.db";
    private static MonopolyDatabase instance = null;

    public static MonopolyDatabase getInstance(Context context){
        if(instance == null){
            synchronized (MonopolyDatabase.class) {
                if(instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), MonopolyDatabase.class, DATABASE_NAME)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return instance;
    }

}
