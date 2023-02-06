package com.example.monopoly.game;

import com.example.monopoly.views.FieldView;

public class UtilityField extends OwnableField{


    public UtilityField(FieldView fieldView, int fieldNumber, String name){
        super(fieldView, fieldNumber, name, 150, 75, false, null);
    }


    public int calculateRent(int diceNumber){
        if(morgaged)
            return 0;
        int cnt = this.owner.getUtilityPropertiesCnt();
        switch (cnt){
            case 1:
                return diceNumber * 4;
            case 2:
                return diceNumber * 10;
        }
        return -1;
    }
}
