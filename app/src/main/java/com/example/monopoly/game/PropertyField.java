package com.example.monopoly.game;

import com.example.monopoly.views.FieldView;

public class PropertyField extends OwnableField{


    private int housePrice;
    private int hotelPrice;
    private PropertyColor propertyColor;
    private int propertySetCnt;
    private int rent;
    private int rentWithColorSet;
    private int rentw1House;
    private int rentw2House;
    private int rentw3House;
    private int rentw4House;
    private int rentwHotel;
    private int houseCnt;
    private int hotelCnt;


    public PropertyField(FieldView fieldView, int fieldNumber, String name, int price, int housePrice, int hotelPrice, PropertyColor propertyColor, int propertySetCnt, int rent, int rentw1House, int rentw2House, int rentw3House, int rentw4House, int rentwHotel) {
        super(fieldView, fieldNumber, name, price, price/2, false, null);
        this.housePrice = housePrice;
        this.hotelPrice = hotelPrice;
        this.propertyColor = propertyColor;
        this.propertySetCnt = propertySetCnt;
        this.rent = rent;
        this.rentWithColorSet = rent * 2;
        this.rentw1House = rentw1House;
        this.rentw2House = rentw2House;
        this.rentw3House = rentw3House;
        this.rentw4House = rentw4House;
        this.rentwHotel = rentwHotel;
        this.houseCnt = 0;
        this.hotelCnt = 0;
    }

    public int calculateRent(int diceNumber){
        if(morgaged)
            return 0;
        if(hotelCnt > 0){
            return this.rentwHotel;
        }
        else if(houseCnt > 0){
            switch (houseCnt){
                case 1:
                    return rentw1House;
                case 2:
                    return rentw2House;
                case 3:
                    return rentw3House;
                case 4:
                    return rentw4House;
            }
        }
        else{
            if(owner.getPropertiesCnt(this.propertyColor) == propertySetCnt){
                return rentWithColorSet;
            }
            else
                return rent;
        }
        return -1;
    }

    public void incHouses(){
        this.houseCnt++;
    }

    public void decHouses(){
        this.houseCnt--;
    }

    public void incHotel(){
        this.hotelCnt++;
    }

    public void decHotel(){
        this.hotelCnt--;
    }


    public int getHousePrice() {
        return housePrice;
    }

    public void setHousePrice(int housePrice) {
        this.housePrice = housePrice;
    }

    public int getHotelPrice() {
        return hotelPrice;
    }

    public void setHotelPrice(int hotelPrice) {
        this.hotelPrice = hotelPrice;
    }

    public PropertyColor getPropertyColor() {
        return propertyColor;
    }

    public void setPropertyColor(PropertyColor propertyColor) {
        this.propertyColor = propertyColor;
    }

    public int getRent() {
        return rent;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public int getRentw1House() {
        return rentw1House;
    }

    public void setRentw1House(int rentw1House) {
        this.rentw1House = rentw1House;
    }

    public int getRentw2House() {
        return rentw2House;
    }

    public void setRentw2House(int rentw2House) {
        this.rentw2House = rentw2House;
    }

    public int getRentw3House() {
        return rentw3House;
    }

    public void setRentw3House(int rentw3House) {
        this.rentw3House = rentw3House;
    }

    public int getRentw4House() {
        return rentw4House;
    }

    public void setRentw4House(int rentw4House) {
        this.rentw4House = rentw4House;
    }

    public int getRentwHotel() {
        return rentwHotel;
    }

    public void setRentwHotel(int rentwHotel) {
        this.rentwHotel = rentwHotel;
    }

    public int getHouseCnt() {
        return houseCnt;
    }

    public void setHouseCnt(int houseCnt) {
        this.houseCnt = houseCnt;
    }

    public int getHotelCnt() {
        return hotelCnt;
    }

    public void setHotelCnt(int hotelCnt) {
        this.hotelCnt = hotelCnt;
    }

    public int getRentWithColorSet() {
        return rentWithColorSet;
    }

    public void setRentWithColorSet(int rentWithColorSet) {
        this.rentWithColorSet = rentWithColorSet;
    }

    public int getPropertySetCnt() {
        return propertySetCnt;
    }

    public void setPropertySetCnt(int propertySetCnt) {
        this.propertySetCnt = propertySetCnt;
    }


}
