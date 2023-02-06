package com.example.monopoly.game;

public class Chance {

    private int money = 0;
    private String message;
    private int fieldToGo = -1;

    public Chance(String message, int money, int fieldToGo) {
        this.money = money;
        this.message = message;
        this.fieldToGo = fieldToGo;
    }

    public Chance(String message, int money) {
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
}
