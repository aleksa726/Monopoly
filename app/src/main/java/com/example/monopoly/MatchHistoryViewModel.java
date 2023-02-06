package com.example.monopoly;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.monopoly.game.data.GameEntity;

import java.util.ArrayList;
import java.util.List;

public class MatchHistoryViewModel extends ViewModel {

    private MutableLiveData<GameEntity> selectedGameEntity = new MutableLiveData<>(null);
    private List<GameEntity> gameEntities = new ArrayList<>();

    public MutableLiveData<GameEntity> getSelectedGameEntity() {
        return selectedGameEntity;
    }

    public void setSelectedGameEntity(GameEntity selectedGameEntity) {
        this.selectedGameEntity.setValue(selectedGameEntity);
    }

    public List<GameEntity> getGameEntities() {
        return gameEntities;
    }

    public void setGameEntities(List<GameEntity> gameEntities) {
        this.gameEntities = gameEntities;
    }
}
