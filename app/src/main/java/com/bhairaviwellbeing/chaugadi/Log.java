package com.bhairaviwellbeing.chaugadi;

public class Log {

    int ur_card;
    int uu_card;
    int ul_card;
    int chal_seat;
    int claim_color;
    String deck;
    int result;
    int roomNo;
    String name;


    public Log(int roomNo,int result,int ul_card, int uu_card, int ur_card, int chal_seat, int claim_color, String deck) {
        this.ur_card = ur_card;
        this.uu_card = uu_card;
        this.ul_card = ul_card;
        this.chal_seat = chal_seat;
        this.claim_color = claim_color;
        this.deck = deck;
        this.result = result;
        this.name = "("+roomNo+") "+result+" = "+ul_card+"," +uu_card+","+ ur_card+","+ chal_seat+","+ chal_seat+","+ deck;
    }

    public int getUr_card() {
        return ur_card;
    }

    public void setUr_card(int ur_card) {
        this.ur_card = ur_card;
    }

    public int getUu_card() {
        return uu_card;
    }

    public void setUu_card(int uu_card) {
        this.uu_card = uu_card;
    }

    public int getUl_card() {
        return ul_card;
    }

    public void setUl_card(int ul_card) {
        this.ul_card = ul_card;
    }

    public int getChal_seat() {
        return chal_seat;
    }

    public void setChal_seat(int chal_seat) {
        this.chal_seat = chal_seat;
    }

    public int getClaim_color() {
        return claim_color;
    }

    public void setClaim_color(int claim_color) {
        this.claim_color = claim_color;
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(int roomNo) {
        this.roomNo = roomNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
