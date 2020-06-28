package com.bhairaviwellbeing.chaugadi;

public class Deck {

    public Card[] deck;
    private int current_card = 0;

    public Deck() {
        deck = new Card[52];
        for(int i = 0; i <= 51; i++){
            deck[i] = new Card(i+1);
        }
        current_card = 52;
    }

    public Deck(String str){
        deck = new Card[52];
        for(int i = 1; i <= 52; i++){
            deck[i-1] = new Card(str.substring(0,2));
            str=str.substring(2);
        }
    }



    public Card[] getCards(int startIndex,int endIndex){

        Card subdeck[] = new Card[endIndex-startIndex+1];

        for (int i = 0;startIndex <= endIndex; startIndex++,i++){
            subdeck[i] = deck[startIndex];
        }
        return subdeck;
    }

    public void shuffle(int n){
        int i,j,k;

        for(k=0;k<=n;k++){
            i = (int) (52 * Math.random());
            j = (int) (52 * Math.random());

            Card temp = deck[i];
            deck[i] = deck[j];
            deck[j] = temp;
        }

    }

    public String toString() {
        String tmp = new String();

        for(int i = 0; i <= 51; i++){
            tmp = tmp + deck[i].toString();
        }
        return tmp;
    }

    public void removeCard(int index){
        this.deck[index -1].setCard(0,0);
    }

    public Card getCard(int index){
        return deck[index];
    }

}
