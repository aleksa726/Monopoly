package com.example.monopoly.game;

import com.example.monopoly.views.FieldView;

public abstract class OwnableField extends Field{

    protected int price;
    protected int morgagePrice;
    protected boolean morgaged;
    protected Player owner;

    public OwnableField(FieldView fieldView, int fieldNumber, String name, int price, int morgagePrice, boolean morgaged, Player owner) {
        super(fieldView, fieldNumber, name);
        this.price = price;
        this.morgagePrice = morgagePrice;
        this.morgaged = morgaged;
        this.owner = owner;
    }

    public abstract int calculateRent(int diceNumber);

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getMorgagePrice() {
        return morgagePrice;
    }

    public void setMorgagePrice(int morgagePrice) {
        this.morgagePrice = morgagePrice;
    }

    public boolean isMorgaged() {
        return morgaged;
    }

    public void setMorgaged(boolean morgaged) {
        this.morgaged = morgaged;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
