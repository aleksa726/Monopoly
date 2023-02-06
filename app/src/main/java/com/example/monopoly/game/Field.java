package com.example.monopoly.game;

import com.example.monopoly.views.FieldView;

public abstract class Field {

    protected FieldView fieldView;
    protected int fieldNumber;
    protected String name;

    public Field(FieldView fieldView, int fieldNumber, String name) {
        this.fieldView = fieldView;
        this.fieldNumber = fieldNumber;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFieldNumber() {
        return fieldNumber;
    }

    public void setFieldNumber(int fieldNumber) {
        this.fieldNumber = fieldNumber;
    }

    public FieldView getFieldView() {
        return fieldView;
    }

    public void setFieldView(FieldView fieldView) {
        this.fieldView = fieldView;
    }
}
