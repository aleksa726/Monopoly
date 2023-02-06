package com.example.monopoly.game;

public class CommunityChest {

    private int money = 0;
    private String message;
    private int fieldToGo = -1;
    private int fieldsToAdvance = 0;

    public CommunityChest(String message, int money, int fieldToGo) {
        this.money = money;
        this.message = message;
        this.fieldToGo = fieldToGo;
    }

    public CommunityChest(String message, int money, int fieldToGo, int fieldsToAdvance) {
        this.money = money;
        this.message = message;
        this.fieldToGo = fieldToGo;
        this.fieldsToAdvance = fieldsToAdvance;
    }

    public CommunityChest(String message, int money) {
        this.money = money;
        this.message = message;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFieldToGo() {
        return fieldToGo;
    }

    public void setFieldToGo(int fieldToGo) {
        this.fieldToGo = fieldToGo;
    }

    public int getFieldsToAdvance() {
        return fieldsToAdvance;
    }

    public void setFieldsToAdvance(int fieldsToAdvance) {
        this.fieldsToAdvance = fieldsToAdvance;
    }

}
