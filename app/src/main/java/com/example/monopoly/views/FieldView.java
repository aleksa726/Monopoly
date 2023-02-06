package com.example.monopoly.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.monopoly.GameFragment;
import com.example.monopoly.MainActivity;
import com.example.monopoly.R;
import com.example.monopoly.game.Field;
import com.example.monopoly.game.Game;
import com.example.monopoly.game.GameSimulation;
import com.example.monopoly.game.OwnableField;
import com.example.monopoly.game.Player;
import com.example.monopoly.game.PropertyField;

import java.util.ArrayList;
import java.util.List;

public class FieldView extends View {

    private int playerColor;
    private Paint playerPaint;

    private RectF rectangle;
    private Drawable houseImage;
    private Drawable hotelImage;

    private List<Player> players = new ArrayList<>();
    private Player owner;

    private int fieldId;

    private boolean painted = true;




    public FieldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        playerPaint = new Paint();
        houseImage = context.getResources().getDrawable(R.drawable.house);
        hotelImage = context.getResources().getDrawable(R.drawable.hotel);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.FieldView, 0, 0);
        try {
            fieldId = a.getInteger(R.styleable.FieldView_fieldId, 0);
        } finally {
            a.recycle();
        }
    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(Game.buyHouseEnabled){
            if(owner != null)
                if(owner == Game.playingPlayer)
                    owner.buyHouse(fieldId);
        }
        else if(Game.sellHouseEnabled){
            if(owner != null)
                if(owner == Game.playingPlayer)
                    owner.sellHouse(fieldId);
        }
        else if(Game.morgagePropertyEnabled){
            if(owner != null)
                if(owner == Game.playingPlayer)
                    owner.morgageProperty(fieldId);
        }
        else{
            if(!painted) {

                ImageView dice1Image = GameFragment.dice1ImageView;
                int image1 = getResources().getIdentifier("dice" + 7, "drawable", getContext().getPackageName());
                dice1Image.setImageResource(image1);

                ImageView dice2Image = GameFragment.dice2ImageView;
                int image2 = getResources().getIdentifier("dice" + 7, "drawable", getContext().getPackageName());
                dice2Image.setImageResource(image2);

                ImageView linearLayout = GameFragment.cardImageView;
                int image = getResources().getIdentifier("field" + fieldId, "drawable", getContext().getPackageName());
                linearLayout.setImageResource(image);
                painted = true;
            }
            else{
                ImageView linearLayout = GameFragment.cardImageView;
                int image = getResources().getIdentifier("field" + 0, "drawable", getContext().getPackageName());
                linearLayout.setImageResource(image);
                painted = false;
            }
        }
        return super.dispatchTouchEvent(event);

    }

    @Override
    protected void onDraw(Canvas canvas) {

        LinearLayout parent = (LinearLayout) this.getParent();

        String str = parent.toString();
        boolean desno = str.contains("desno");
        boolean dole = str.contains("dole");

        boolean rotate = false;
        if (desno || dole) {
            rotate = true;
        }


        int viewWidthHalf = (int) (this.getMeasuredWidth() / 3.5);
        int viewHeightHalf = (int) (this.getMeasuredHeight() / 3.5);
        int radius = 0;
        if (viewWidthHalf > viewHeightHalf)
            radius = viewHeightHalf - 10;
        else
            radius = viewWidthHalf - 10;


        if (!rotate) {
            if (viewWidthHalf > viewHeightHalf) {
                canvas.translate(getWidth(), 0);
                canvas.rotate(90);
            } else {
                canvas.translate(getWidth(), getHeight());
                canvas.rotate(180);
            }
        } else {
            if (desno) {
                canvas.translate(0, getHeight());
                canvas.rotate(270);
            }
        }

        this.paintPlayers(canvas);

        if (this.owner != null) {
            this.paintMorgaged(canvas);
            this.paintOwner(canvas);
            this.paintHouses(canvas);
            this.paintHotel(canvas);
        }
    }


    public void paintPlayers(Canvas canvas){

        int viewWidthHalf = (int) (this.getMeasuredWidth()/3.5);
        int viewHeightHalf = (int) (this.getMeasuredHeight()/3.5);
        int radius = 0;
        if(viewWidthHalf>viewHeightHalf)
            radius=viewHeightHalf-10;
        else
            radius=viewWidthHalf-10;

        radius = 13;
        int cnt = 0;
        for(Player p: this.players){

            if(!p.isBankrupt()) {
                playerPaint.setAntiAlias(true);
                playerPaint.setStyle(Paint.Style.FILL);
                playerPaint.setColor(p.getColor());

                if (cnt == 0)
                    canvas.drawCircle(60, 90, radius, playerPaint);
                else if (cnt == 1)
                    canvas.drawCircle(25, 90, radius, playerPaint);
                else if (cnt == 2)
                    canvas.drawCircle(60, 60, radius, playerPaint);
                else if (cnt == 3)
                    canvas.drawCircle(25, 60, radius, playerPaint);

                cnt++;
            }
        }
    }


    public void paintMorgaged(Canvas canvas){
        if(Game.fields != null) {
            if (((OwnableField) Game.fields[this.fieldId]).isMorgaged()) {
                Paint p = new Paint();
                p.setStyle(Paint.Style.FILL);
                p.setColor(Color.RED);
                canvas.drawRect(new Rect(6, 128, 70, 160), p);
            }
        }
        else{
            if (((OwnableField) GameSimulation.fields[this.fieldId]).isMorgaged()) {
                Paint p = new Paint();
                p.setStyle(Paint.Style.FILL);
                p.setColor(Color.RED);
                canvas.drawRect(new Rect(6, 128, 70, 160), p);
            }
        }
    }


    public void paintOwner(Canvas canvas){
        rectangle = new RectF(6, 108, 70, 145);
        playerPaint.setAntiAlias(true);
        playerPaint.setStyle(Paint.Style.FILL);
        playerPaint.setColor(this.owner.getColor());
        canvas.drawArc(rectangle, 0f, 180f, false, playerPaint);
    }


    public void paintHouses(Canvas canvas){
        int width = 14;
        int height = 16;
        int startLeft = 2;
        int startTop = 4;

        OwnableField field = null;
        for(OwnableField f: this.owner.getOwnedFields()){
            if(f.getFieldNumber() == this.fieldId){
                field = f;
            }
        }

        if(field != null && field instanceof PropertyField) {
            for (int i = 0; i < ((PropertyField) field).getHouseCnt(); i++) {
                houseImage.setBounds(startLeft + (startLeft + width) * i, startTop, startLeft + (startLeft + width) * i + width, startTop + height);
                houseImage.draw(canvas);
            }
        }
    }

    public void paintHotel(Canvas canvas){
        int width = 15;
        int height = 16;
        int startLeft = 28;
        int startTop = 4;

        OwnableField field = null;
        for(OwnableField f: this.owner.getOwnedFields()){
            if(f.getFieldNumber() == this.fieldId){
                field = f;
            }
        }

        if(field != null && field instanceof PropertyField) {
            for (int i = 0; i < ((PropertyField) field).getHotelCnt(); i++) {
                hotelImage.setBounds(startLeft + (startLeft + width) * i, startTop, startLeft + (startLeft + width) * i + width, startTop + height);
                hotelImage.draw(canvas);
            }
        }
    }

    public void setInfo(List<Player> players, Player owner, boolean painted){
        this.players = players;
        this.owner = owner;
        this.painted = painted;
    }

    public void addPlayer(Player p){
        this.players.add(p);
    }

    public void removePlayer(Player p){
        this.players.remove(p);
    }

    public int getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(int playerColor) {
        this.playerColor = playerColor;
    }

    public Paint getPlayerPaint() {
        return playerPaint;
    }

    public void setPlayerPaint(Paint playerPaint) {
        this.playerPaint = playerPaint;
    }

    public RectF getRectangle() {
        return rectangle;
    }

    public void setRectangle(RectF rectangle) {
        this.rectangle = rectangle;
    }

    public Drawable getHouseImage() {
        return houseImage;
    }

    public void setHouseImage(Drawable houseImage) {
        this.houseImage = houseImage;
    }

    public Drawable getHotelImage() {
        return hotelImage;
    }

    public void setHotelImage(Drawable hotelImage) {
        this.hotelImage = hotelImage;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public boolean isPainted() {
        return painted;
    }

    public void setPainted(boolean painted) {
        this.painted = painted;
    }
}
