package com.example.monopoly.game;

import com.example.monopoly.views.FieldView;

public class SpecialField extends Field{


    public static final Chance[] chances = {
        new Chance("Advance to Boardwalk",0,39),
        new Chance("Advance to Go (Collect $200)",200,0),
        new Chance("Advance to Illinois Avenue",0,24),
        new Chance("Advance to St. Charles Place",0,11),
        new Chance("Bank pays you dividend of $50",50),
        new Chance("Go to Jail",0,10),
        new Chance("Speeding fine $15",-15),
        new Chance("Take a trip to Reading Railroad",0,5),
        new Chance("Your building loan matures. Collect $150",150),
        new Chance("Holiday fund matures. Receive $100",100),
        new Chance("Income tax refund. Collect $20",20),
        new Chance("Make general repairs on all your property. For each house pay $25. For each hotel pay $100",0),
        new Chance("Get Out of Jail Free",20),
        new Chance("You have been elected Chairman of the Board. Pay each player $50",0)
    };

    public static final CommunityChest[] communityChests = {
        new CommunityChest("Bank error in your favor. Collect $200",200),
        new CommunityChest("Advance to Go (Collect $200)",200,0),
        new CommunityChest("Doctor’s fee. Pay $50",-50),
        new CommunityChest("From sale of stock you get $50",50),
        new CommunityChest("Bank pays you dividend of $50",50),
        new CommunityChest("Go to Jail",0,10),
        new CommunityChest("Holiday fund matures. Receive $100",100),
        new CommunityChest("Income tax refund. Collect $20",20),
        new CommunityChest("Life insurance matures. Collect $100",100),
        new CommunityChest("Pay hospital fees of $100",-100),
        new CommunityChest("Pay school fees of $50",-50),
        new CommunityChest("Receive $25 consultancy fee",25),
        new CommunityChest("You inherit $100",100),
        new CommunityChest("You have won second prize in a beauty contest. Collect $10",10)
    };



    public SpecialField(FieldView fieldView, int fieldNumber, String name){
        super(fieldView, fieldNumber, name);
    }


    public static Chance getRandomChanceCard(){
        int index = (int) (Math.random() * 10);
        return chances[index];
    }

    public static CommunityChest getRandomCommunityChestCard(){
        int index = (int) (Math.random() * 13);
        return communityChests[index];
    }



}
