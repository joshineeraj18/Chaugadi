package com.bhairaviwellbeing.chaugadi;

import java.util.Arrays;
import java.util.Collections;

public class GameData {
    private int scoreA;
    private int scoreB;

    private int feesA;
    private int feesB;

    private String player1 ="empty";
    private String player2 ="empty";
    private String player3 ="empty";
    private String player4 ="empty";

    private String id1;
    private String id2;
    private String id3;
    private String id4;

    private int state;
    private int chal_seat;
    private int chal_card_1;
    private int chal_card_2;
    private int chal_card_3;
    private int chal_card_4;
    private int chal_round;
    private int chal_number;
    private int claim_seat;
    private int claim_number;
    private int claim_color;


    private int shufflingSeat;

    private String deck;

    public static final int IDEAL = 0;
    public static final int SHUFFLING = 1;
    public static final int COLLECT_CARDS = 2;
    public static final int CHAL_1 = 3;
    public static final int CHAL_2 = 4;
    public static final int CHAL_3 = 5;
    public static final int CHAL_4 = 6;
    public static final int CHAL_RESULT = 7;
    public static final int CLAIM = 8;
    public static final int CLAIM_COLOR = 9;
    public static final int CLAIM_READY = 10;



    private boolean isGameStarted = false;


    public GameData() {
        this.scoreA = 0;
        this.scoreB = 0;
        this.isGameStarted = false;
        this.player1 = "empty";
        this.player2 = "empty";
        this.player3 = "empty";
        this.player4 = "empty";
        this.id1 = "empty";
        this.id2 = "empty";
        this.id3 = "empty";
        this.id4 = "empty";
        this.shufflingSeat = 0;
        this.state = GameData.IDEAL;
        this.deck = "empty";
        this.chal_seat = 0;
        this.chal_card_1 = 0;
        this.chal_card_2 = 0;
        this.chal_card_3 = 0;
        this.chal_card_4 = 0;
        this.claim_number = 0;
        this.claim_seat = 0;
        this.claim_color = 0;
        this.feesA = 0;
        this.feesB = 0;
    }

    public int getScoreA() {
        return scoreA;
    }

    public void setScoreA(int scoreA) {
        this.scoreA = scoreA;
    }

    public int getScoreB() {
        return scoreB;
    }

    public void setScoreB(int scoreB) {
        this.scoreB = scoreB;
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void setGameStarted(boolean isGameStarted) {
        this.isGameStarted = isGameStarted;
    }

    public int getShufflingSeat() {
        return shufflingSeat;
    }

    public void setShufflingSeat(int shufflingSeat) {
        this.shufflingSeat = shufflingSeat;
    }

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getPlayer3() {
        return player3;
    }

    public void setPlayer3(String player3) {
        this.player3 = player3;
    }

    public String getPlayer4() {
        return player4;
    }

    public void setPlayer4(String player4) {
        this.player4 = player4;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public String getId2() {
        return id2;
    }

    public void setId2(String id2) {
        this.id2 = id2;
    }

    public String getId3() {
        return id3;
    }

    public void setId3(String id3) {
        this.id3 = id3;
    }

    public String getId4() {
        return id4;
    }

    public void setId4(String id4) {
        this.id4 = id4;
    }

    public int addPlayer(String name, String id){
        if(this.player1.equals("empty")){
            this.player1 = name;
            this.id1 = id;
            return 1;
        }else {
            if(this.player2.equals("empty")){
                this.player2 = name;
                this.id2 = id;
                return 2;
            }else {
                if(this.player3.equals("empty")){
                    this.player3 = name;
                    this.id3 = id;
                    return 3;
                }else {
                    if(this.player4.equals("empty")){
                        this.player4 = name;
                        this.id4 = id;
                        return 4;
                    }else {
                        return 0;
                    }
                }
            }
        }

    }

    public int getMyPosition(String userid){
        if(userid.equals(this.id1)){
            return 1;
        }else if(userid.equals(this.id2)){
            return 2;
        }else if(userid.equals(this.id3)){
            return 3;
        }else if(userid.equals(this.id4)){
            return 4;
        }else {
            return 0;
        }

    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getDeck() {
        return deck;
    }

    public void setDeck(String deck) {
        this.deck = deck;
    }

    public int getChal_seat() {
        return chal_seat;
    }

    public void setChal_seat(int chal_seat) {
        this.chal_seat = chal_seat;
    }

    public int getChal_round() {
        return chal_round;
    }

    public void setChal_round(int chal_round) {
        this.chal_round = chal_round;
    }

    public int getChal_number() {
        return chal_number;
    }

    public void setChal_number(int chal_number) {
        this.chal_number = chal_number;
    }

    public void setNextChalData(){


        if(this.chal_seat == 4){
            this.chal_seat = 1;
        }else {
            this.chal_seat++;
        }

        this.chal_number++;


        if(this.state == 3){
            this.state = CHAL_2;
        }else if(this.state == 4){
            this.state = CHAL_3;
        }else if(this.state == 5){
            this.state = CHAL_4;
        }else if(this.state == 6){
            this.state = CHAL_RESULT;
        }

    }

    public int getChal_card_1() {
        return chal_card_1;
    }

    public void setChal_card_1(int chal_card_1) {
        this.chal_card_1 = chal_card_1;
    }

    public int getChal_card_2() {
        return chal_card_2;
    }

    public void setChal_card_2(int chal_card_2) {
        this.chal_card_2 = chal_card_2;
    }

    public int getChal_card_3() {
        return chal_card_3;
    }

    public void setChal_card_3(int chal_card_3) {
        this.chal_card_3 = chal_card_3;
    }

    public int getChal_card_4() {
        return chal_card_4;
    }

    public void setChal_card_4(int chal_card_4) {
        this.chal_card_4 = chal_card_4;
    }

    public void setNextChalCard(int cardNum){
        if(this.getChal_number() == 1){
            this.chal_card_1 = cardNum;
        }
        if(this.getChal_number() == 2){
            this.chal_card_2 = cardNum;
        }
        if(this.getChal_number() == 3){
            this.chal_card_3 = cardNum;
        }
        if(this.getChal_number() == 4){
            this.chal_card_4 = cardNum;
        }

    }

    public Card getLastThrownCard(){

        switch (this.state){
            case CHAL_1:
                return null;
            case CHAL_2:
                return new Card(getChal_card_1());
            case CHAL_3:
                return new Card(getChal_card_2());
            case CHAL_4:
                return new Card(getChal_card_3());
            case CHAL_RESULT:
                return new Card(getChal_card_4());
            default:
                return null;

        }
    }

    public String getPlayerAtSeat(int seat){

        switch (seat){
            case 1:
                return player1;
            case 2:
                return player2;
            case 3:
                return player3;
            case 4:
                return player4;
            default:
                return null;
        }
    }


    public String getPlayerIDAtSeat(int seat){

        switch (seat){
            case 1:
                return id1;
            case 2:
                return id2;
            case 3:
                return id3;
            case 4:
                return id4;
            default:
                return null;
        }
    }

    public int getClaim_seat() {
        return claim_seat;
    }

    public void setClaim_seat(int claim_seat) {
        this.claim_seat = claim_seat;
    }

    public int getClaim_number() {
        return claim_number;
    }

    public void setClaim_number(int claim_number) {
        this.claim_number = claim_number;
    }

    public void setNextClaimNumber(int claim_number){
        if(this.getClaim_number() == 1){
            this.chal_card_1 = claim_number;
        }
        if(this.getClaim_number() == 2){
            this.chal_card_2 = claim_number;
        }
        if(this.getClaim_number() == 3){
            this.chal_card_3 = claim_number;
        }
        if(this.getClaim_number() == 4){
            this.chal_card_4 = claim_number;
        }

    }

    public void setNextClaimData(){

            if(this.claim_number == 4){

            Integer[] claimNm = {this.chal_card_1,this.chal_card_2,this.chal_card_3,this.chal_card_4};
            int max = Collections.max(Arrays.asList(claimNm));
            int index;

            if(max == this.chal_card_1){
                index = 0;
                this.claim_number = max;
            }else if(max == this.chal_card_2){
                index = 1;
                this.claim_number = max;
            }else if(max == this.chal_card_3){
                index = 2;
                this.claim_number = max;
            }else {
                index = 3;
                this.claim_number = max;
            }

            for(int i=0; i<=index; i++){
                this.claim_seat = getnextSeat(this.claim_seat);
            }
            this.chal_seat = this.claim_seat;
            this.chal_round = 1;
            this.chal_number = 1;

        }else {
            this.claim_number++;
            this.claim_seat = this.getnextSeat(this.claim_seat);
        }
    }

    public  int getPrvSeat(int seat)
    {
        if(seat == 1){
            seat = 4;
        }else {
            seat--;
        }
        return seat;
    }

    public  int getnextSeat(int seat)
    {
        if(seat == 4){
            seat = 1;
        }else {
            seat++;
        }
        return seat;
    }

    public int getClaim_color() {
        return claim_color;
    }

    public void setClaim_color(int claim_color) {
        this.claim_color = claim_color;
    }

    public int getChalof(int seatNo){
        if (seatNo == 1){
            return chal_card_1;
        }else if(seatNo == 2){
            return chal_card_2;
        }else if(seatNo == 3){
            return chal_card_3;
        }else if(seatNo == 4){
            return chal_card_4;
        }else {
            return 0;
        }
    }

    public void setChalof(int seatNo, int chal_data){
        if (seatNo == 1){
            this.chal_card_1 = chal_data;
        }else if(seatNo == 2){
            this.chal_card_2 = chal_data;
        }else if(seatNo == 3){
            this.chal_card_3 = chal_data;
        }else if(seatNo == 4){
            this.chal_card_4 = chal_data;
        }
    }

    public void incScoreA(){
        this.scoreA++;
    }
    public void incScoreB(){
        this.scoreB++;
    }

    public void calculate_chal_result(){


        Card[] th_cards = new Card[4];
        int index = 0;

        th_cards[0] = new Card(this.getChal_card_1());
        th_cards[1] = new Card(this.getChal_card_2());
        th_cards[2] = new Card(this.getChal_card_3());
        th_cards[3] = new Card(this.getChal_card_4());

        if(th_cards[0].getColor() == th_cards[1].getColor() &&
                th_cards[0].getColor() == th_cards[2].getColor() &&
                th_cards[0].getColor() == th_cards[3].getColor() )
        {
            int maxValue = th_cards[0].getRank();
            index = 0;
            for(int a = 0; a < th_cards.length; a++)
            {
                if(maxValue < th_cards[a].getRank())
                {
                    maxValue = th_cards[a].getRank();
                    index = a;
                }
            }
        }else if(th_cards[0].getColor() == this.getClaim_color() ||
                th_cards[1].getColor() == this.getClaim_color() ||
                th_cards[2].getColor() == this.getClaim_color() ||
                th_cards[3].getColor() == this.getClaim_color() ){

            int maxValue = 0;
            index = 0;

            for(int a = 0; a < th_cards.length; a++)
            {
                if (th_cards[a].getColor() == this.getClaim_color()) {
                    if(maxValue < th_cards[a].getRank())
                    {
                        maxValue = th_cards[a].getRank();
                        index = a;
                    }
                }
            }
        }else {

            int maxValue = th_cards[0].getRank();
            index = 0;

            for(int a = 1; a < th_cards.length; a++)
            {
                if (th_cards[a].getColor() == th_cards[0].getColor()) {
                    if(maxValue < th_cards[a].getRank())
                    {
                        maxValue = th_cards[a].getRank();
                        index = a;
                    }
                }
            }
        }

        int i;
        for(i=0; i< index; i++){
            this.chal_seat = getnextSeat(this.chal_seat);
        }
        if(this.chal_seat == 1 || this.chal_seat == 3){
            this.feesA++;
        }else {
            this.feesB++;
        }

        this.chal_round++;
        this.chal_number = 1;



    }

    public int getFeesA() {
        return feesA;
    }

    public void setFeesA(int feesA) {
        this.feesA = feesA;
    }

    public int getFeesB() {
        return feesB;
    }

    public void setFeesB(int feesB) {
        this.feesB = feesB;
    }
}
