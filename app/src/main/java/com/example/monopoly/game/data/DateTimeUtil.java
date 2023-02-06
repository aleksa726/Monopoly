package com.example.monopoly.game.data;

import java.text.SimpleDateFormat;

public class DateTimeUtil {

    private static final SimpleDateFormat simpleDateFormatDateOnly = new SimpleDateFormat("dd/MM/yyyy");

    private static final SimpleDateFormat simpleDateFormatDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static SimpleDateFormat getSimpleDateFormatDateOnly(){
        return simpleDateFormatDateOnly;
    }

    public static SimpleDateFormat getSimpleDateFormatDateTime(){
        return simpleDateFormatDateTime;
    }
}
