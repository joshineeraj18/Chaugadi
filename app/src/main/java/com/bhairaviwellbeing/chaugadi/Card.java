package com.bhairaviwellbeing.chaugadi;

public class Card {

    private int color;
    private int number;
    private int res;

    public static final int SPADE = 1;
    public static final int CLUB = 2;
    public static final int DIAMOND = 3;
    public static final int HEART = 4;


    public static final int[] CARD_RES = {
            R.drawable.card_2s,
            R.drawable.card_3s,
            R.drawable.card_4s,
            R.drawable.card_5s,
            R.drawable.card_6s,
            R.drawable.card_7s,
            R.drawable.card_8s,
            R.drawable.card_9s,
            R.drawable.card_10s,
            R.drawable.card_js,
            R.drawable.card_qs,
            R.drawable.card_ks,
            R.drawable.card_as,

            R.drawable.card_2c,
            R.drawable.card_3c,
            R.drawable.card_4c,
            R.drawable.card_5c,
            R.drawable.card_6c,
            R.drawable.card_7c,
            R.drawable.card_8c,
            R.drawable.card_9c,
            R.drawable.card_10c,
            R.drawable.card_jc,
            R.drawable.card_qc,
            R.drawable.card_kc,
            R.drawable.card_ac,

            R.drawable.card_2d,
            R.drawable.card_3d,
            R.drawable.card_4d,
            R.drawable.card_5d,
            R.drawable.card_6d,
            R.drawable.card_7d,
            R.drawable.card_8d,
            R.drawable.card_9d,
            R.drawable.card_10d,
            R.drawable.card_jd,
            R.drawable.card_qd,
            R.drawable.card_kd,
            R.drawable.card_ad,

            R.drawable.card_2h,
            R.drawable.card_3h,
            R.drawable.card_4h,
            R.drawable.card_5h,
            R.drawable.card_6h,
            R.drawable.card_7h,
            R.drawable.card_8h,
            R.drawable.card_9h,
            R.drawable.card_10h,
            R.drawable.card_jh,
            R.drawable.card_qh,
            R.drawable.card_kh,
            R.drawable.card_ah,
    };

    public Card() {
        this.color = 0;
        this.number = 0;
        this.res = 0;
    }

    public Card(String str) {
        this(Integer.parseInt(str));
/*        int num = Integer.parseInt(str);
        num--;
        this.color = 1+num/13;
        this.number = 1+num%13;
        this.res = CARD_RES[color*number-1];*/
    }

    public Card(int num) {
        num--;
        this.color = 1+(num/13);
        this.number = 1+(num%13);
        this.res = CARD_RES[(this.color-1)*13+this.number-1];
    }

    public Card(int color, int number) {
        this.color = color;
        this.number = number;
        this.res = CARD_RES[color*number-1];
    }

    public boolean isEmpty(){
        if(this.number == 0 && this.color  == 0){
            return true;
        }
        else {
            return false;
        }
    }

    public void setCard(int str,int num){
        this.color = str;
        this.number = num;
    }

    public int getRank(){
        return number;
    }
    public int getColor(){
        return color;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public int getCardNo(){
        return (((this.color-1)*13)+this.number);
    }

    public String toString(){
        return String.format("%02d",this.getCardNo());
    }
}
