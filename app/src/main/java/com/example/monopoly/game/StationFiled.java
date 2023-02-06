package com.example.monopoly.game;

import com.example.monopoly.views.FieldView;

public class StationFiled extends OwnableField{

    public StationFiled(FieldView fieldView, int fieldNumber, String name){
        super(fieldView, fieldNumber, name, 200, 100, false, null);
    }

    public int calculateRent(int diceNumber){
        if(morgaged)
            return 0;
        int cnt = this.owner.getStationPropertiesCnt();
        switch (cnt){
            case 1:
                return 25;
            case 2:
                return 50;
            case 3:
                return 100;
            case 4:
                return 200;
        }
        return -1;
    }

}
