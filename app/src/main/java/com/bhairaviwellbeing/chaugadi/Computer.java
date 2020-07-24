package com.bhairaviwellbeing.chaugadi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.Integer;


public class Computer {

    //    public static void main(String[] args) {
//        // TODO code application logic here
//
//        Map<String, Object> AIUBottom_Sur = new HashMap<String, Object>();
//        Integer[] AIUBottom_cards = {52, 45, 43, 38, 30, 27, 26, 24, 20, 17, 14, 10, 7};
//        //AIUBottom_Sur = surProcess(0, 0, 0, AIUBottom_cards);
//        // System.out.println(AIUBottom_Sur);
//
//        Integer[] AIUBottom_cards2 = {27, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 13};
//        // AIUBottom_Sur = surProcess(9, 8, 7, AIUBottom_cards2);
//        //System.out.println(AIUBottom_Sur);
//
//        Integer[] AIUBottom_cards3 = {26, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 13, 11};
////        AIUBottom_Sur = surProcess(11, 10, 8, AIUBottom_cards3);
////        System.out.println(AIUBottom_Sur);
//
//        Integer[] AIUBottom_cards4 = {52, 13, 26, 1, 5, 10, 12, 35, 42, 43, 29, 9, 25};
////        AIUBottom_Sur = surProcess(0, 0, 0, AIUBottom_cards4);
////        System.out.println(AIUBottom_Sur);
////                                       spade              club            diamond         heart
//        Integer[] AIUBottom_cards5 = {1, 6, 8, 11, 16, 20, 21, 25, 41, 45, 49, 50, 51};
////        AIUBottom_Sur = surProcess(0, 0, 0, AIUBottom_cards5);
////        System.out.println(AIUBottom_Sur);
//
//        //chal process test
//        int AIUBottom_card = 0;
//        Integer[] unseenCards = {2, 7, 6};
//        // AIUBottom_card = chalProcess(52, 48, 39, AIUBottom_cards5, "Heart", unseenCards);
//        // System.out.println(AIUBottom_card);
//
//        int my_cardsPos = 1;
//        String deckStr = "01020304050607080952111213141516171819202122232425262728293031323334353637383940414243444546474849505110";
//        AIUBottom_Sur = surProcess(0, 0, 0, 4, deckStr);
//        System.out.println(AIUBottom_Sur);
//        AIUBottom_card = chalProcess(3, 9, 0, 1, "Heart", deckStr);
//        System.out.println(AIUBottom_card);
////
////        AIUBottom_card=chalProcess(9, 8, 5, AIUBottom_cards5, "Heart", unseenCards );
////        System.out.println(AIUBottom_card);
////
////        AIUBottom_card=chalProcess(9, 8, 5, AIUBottom_cards5, "Heart", unseenCards );
////        System.out.println(AIUBottom_card);
//    }
    public Map<String, Integer> surProcess(int ul_sur, int uu_sur, int ur_sur, int myCardsPos, String deckStr) {
        Map<String, Integer> result = new HashMap<String, Integer>();

        List<Integer> my_cardsList = new ArrayList<Integer>();
//        List<Integer> unseen_cardsList = new ArrayList<Integer>();
        String tempCardStr = "";
        int my_cardsStartIndex = ((myCardsPos * 13 * 2) - 13 * 2);
        int my_cardsEndIndex = (myCardsPos * 13 * 2) - 1;
        for (int i = my_cardsStartIndex; i < my_cardsEndIndex; i += 2) {
            tempCardStr = deckStr.substring(i, i + 2);
//            if (tempCardStr.equals("00")) {
//                continue;
//            }
            // if (i >= my_cardsStartIndex && i <= my_cardsEndIndex) {
            my_cardsList.add(Integer.parseInt(tempCardStr));

        }
        Integer[] my_cards = my_cardsList.toArray(new Integer[my_cardsList.size()]);
//        Integer[] unseen_cards = unseen_cardsList.toArray(new Integer[unseen_cardsList.size()]);

        int maxSurValue = getMaxNumber(ur_sur, uu_sur, ul_sur);
        Arrays.sort(my_cards, Collections.reverseOrder());

        List<Integer> spades = new ArrayList<Integer>();
        List<Integer> clubs = new ArrayList<Integer>();
        List<Integer> diamonds = new ArrayList<Integer>();
        List<Integer> hearts = new ArrayList<Integer>();

        for (int i = 0; i < my_cards.length; i++) {
            if (my_cards[i] <= 13) {
                spades.add(my_cards[i]);
            } else if (my_cards[i] <= 26) {
                clubs.add(my_cards[i] - 13);
            } else if (my_cards[i] <= 39) {
                diamonds.add(my_cards[i] - 26);
            } else if (my_cards[i] <= 52) {
                hearts.add(my_cards[i] - 39);
            }
        }

        int confirmSurCount = 0;
        int riskySurCount = 0;
        Integer suit = 0;

        int tempSpadeConfirmSurCount = 0;
        int tempClubConfirmSurCount = 0;
        int tempDiamondConfirmSurCount = 0;
        int tempHeartConfirmSurCount = 0;

        tempSpadeConfirmSurCount = getConfirmMaxSurCount(spades);
        tempClubConfirmSurCount = getConfirmMaxSurCount(clubs);
        tempDiamondConfirmSurCount = getConfirmMaxSurCount(diamonds);
        tempHeartConfirmSurCount = getConfirmMaxSurCount(hearts);

        if (tempSpadeConfirmSurCount >= tempClubConfirmSurCount
                && tempSpadeConfirmSurCount >= tempDiamondConfirmSurCount
                && tempSpadeConfirmSurCount >= tempHeartConfirmSurCount) {

            suit = 1;
            confirmSurCount = tempSpadeConfirmSurCount;

            riskySurCount += getMaxSurCount(clubs);
            riskySurCount += getMaxSurCount(diamonds);
            riskySurCount += getMaxSurCount(hearts);

        } else if (tempClubConfirmSurCount >= tempSpadeConfirmSurCount
                && tempClubConfirmSurCount >= tempDiamondConfirmSurCount
                && tempClubConfirmSurCount >= tempHeartConfirmSurCount) {
            suit = 2;
            confirmSurCount = tempClubConfirmSurCount;

            riskySurCount += getMaxSurCount(spades);
            riskySurCount += getMaxSurCount(diamonds);
            riskySurCount += getMaxSurCount(hearts);
        } else if (tempDiamondConfirmSurCount >= tempClubConfirmSurCount
                && tempDiamondConfirmSurCount >= tempSpadeConfirmSurCount
                && tempDiamondConfirmSurCount >= tempHeartConfirmSurCount) {
            suit = 3;
            confirmSurCount = tempDiamondConfirmSurCount;

            riskySurCount += getMaxSurCount(clubs);
            riskySurCount += getMaxSurCount(spades);
            riskySurCount += getMaxSurCount(hearts);
        } else {
            suit = 4;
            confirmSurCount = tempHeartConfirmSurCount;

            riskySurCount += getMaxSurCount(clubs);
            riskySurCount += getMaxSurCount(diamonds);
            riskySurCount += getMaxSurCount(spades);
        }

        //select suit on basis of suit quantity
        //if()
        int totalPossibleSurCount = confirmSurCount + riskySurCount;
        if (totalPossibleSurCount < 8) { //replace to second method in future
            totalPossibleSurCount += 1;
        }
        if (totalPossibleSurCount >= 7 && totalPossibleSurCount > maxSurValue) {
            result.put("Suit", suit);
            result.put("Sur", totalPossibleSurCount);
        } else {
            result.put("Suit", suit);
            result.put("Sur", 0);
        }
        return result;
    }

    public int getConfirmMaxSurCount(List<Integer> suitsRef) {

        List<Integer> suits = new ArrayList<Integer>();//suitsRef.toArray(new Integer[suitsRef.size()]).;
        for (int item : suitsRef) {
            suits.add(item);
        }
//        List<Integer> suits =  suitsRef.stream().collect(Collectors.toList());
        // List<Integer> suits =  suitsRef.stream().collect(Collectors.toList());

//new ArrayList<Integer>();
        //suits.addAll(Collections.co)
        //      (Arrays.asList(suitsRef));
        // suits.addAll(Collections., elements)suitsRef.toArray().clone());
        // List<Integer> suits= new ArrayList<Integer>(Arrays.asList(suitsRef.));
        //  Collections.copy(suits, suitsRef);
        //suits=(suitsRef.subList(0, suitsRef.))
        //suits=suitsRef;
        int result = 0;
        if (suits.isEmpty()) {
            return result;
        }
        int topHighRank = 13;
        int count = 0;
        while (suits.size() > 0) {
            count++;
            if (suits.get(0) >= topHighRank) {
                result++;
                suits.remove(0);
                topHighRank--;
            } else {
                topHighRank--;
                suits.remove(suits.size() - 1);
            }
            if (count == 4) {
                break;
            }
        }
        result += suits.size();
        return result;
    }

    public int getMaxSurCount(List<Integer> suitsRef) {

        List<Integer> suits = new ArrayList<Integer>();//suitsRef.toArray(new Integer[suitsRef.size()]).;
        for (int item : suitsRef) {
            suits.add(item);
        }

//        List<Integer> suits = suitsRef.stream().collect(Collectors.toList());
        int result = 0;
        if (suits.isEmpty() || suits.get(0) <= 11) {
            return result;
        }
        int topHighRank = 13;
        while (suits.size() > 0 && topHighRank >= 11) {
            if (suits.get(0) >= topHighRank) {
                result++;
                suits.remove(0);
                topHighRank--;
            } else {
                topHighRank--;
                suits.remove(suits.size() - 1);
            }
        }
        return result;
    }

    public int getMaxNumber(int... numericArgs) {
        int result = 0;
        int max = 0;
        for (int num : numericArgs) {
            if (num > max) {
                max = num;
            }
        }
        result = max;
        return result;
    }

    public int getMinNumber(int... numericArgs) {
        int result = 0;
        int min = 999;
        for (int num : numericArgs) {
            if (num < min) {
                min = num;
            }
        }
        result = min;
        return result;
    }

    public int chalProcess(int ul_card, int uu_card, int ur_card, int myCardsPos, int color, String deckStr) {
        int result = 0;
        String color_suit = "";
        switch (color) {
            case 1:
                color_suit = "Spade";
                break;
            case 2:
                color_suit = "Club";
                break;
            case 3:
                color_suit = "Diamond";
                break;
            case 4:
                color_suit = "Heart";
                break;
        }
        List<Integer> my_cardsList = new ArrayList<Integer>();
        List<Integer> unseen_cardsList = new ArrayList<Integer>();
        String tempCardStr = "";
        int my_cardsStartIndex = ((myCardsPos * 13 * 2) - 13 * 2);
        int my_cardsEndIndex = (myCardsPos * 13 * 2) - 1;

        for (int i = 0; i < deckStr.length(); i += 2) {
            tempCardStr = deckStr.substring(i, i + 2);
            if (tempCardStr.equals("00")) {
                continue;
            }
            if (i >= my_cardsStartIndex && i <= my_cardsEndIndex) {
                my_cardsList.add(Integer.parseInt(tempCardStr));
            } else {
                unseen_cardsList.add(Integer.parseInt(tempCardStr));
            }
        }
        Integer[] my_cards = my_cardsList.toArray(new Integer[my_cardsList.size()]);
        Integer[] unseen_cards = unseen_cardsList.toArray(new Integer[unseen_cardsList.size()]);

        if (my_cards.length == 1) {
            return my_cards[0];
        }
        String firstChalSuit = "";
        int afterMeCount = 0;
        int oppRMaxCardRank = 0;
        int oppLMaxCardRank = 0;
        int oppMaxCardRank = 0;
        int frndMaxCardRank = 0;
        boolean isColorChal = false;
        boolean isColorSurpassedByOpp = false;
        String tempSuite = "";

        int tempCard = 0;
        int tempRank = 0;
        if (ur_card == 0) {
            afterMeCount++;
        } else {
            oppRMaxCardRank = getRankByCard(ur_card);
            tempSuite = getSuitByCard(ur_card);
            if (firstChalSuit == "") {
                firstChalSuit = tempSuite;
            }
            if (tempSuite != firstChalSuit && tempSuite != color_suit) {
                oppRMaxCardRank -= 100;
            }
            if (tempSuite == color_suit) {
                oppRMaxCardRank += 100;
            }
        }
        if (uu_card == 0) {
            afterMeCount++;
        } else {
            frndMaxCardRank = getRankByCard(uu_card);
            tempSuite = getSuitByCard(uu_card);
            if (firstChalSuit == "") {
                firstChalSuit = tempSuite;
            }
            if (tempSuite != firstChalSuit && tempSuite != color_suit) {
                frndMaxCardRank -= 100;
            }
            if (tempSuite == color_suit) {
                frndMaxCardRank += 100;
            }
        }
        if (ul_card == 0) {
            afterMeCount++;
        } else {
            //firstChalSuit = (firstChalSuit == "") ? getSuitByCard(ul_card) : firstChalSuit;
            oppLMaxCardRank = getRankByCard(ul_card);
            tempSuite = getSuitByCard(ul_card);
            if (firstChalSuit == "") {
                firstChalSuit = tempSuite;
            }
            if (tempSuite != firstChalSuit && tempSuite != color_suit) {
                oppLMaxCardRank -= 100;
            }
            if (tempSuite == color_suit) {
                oppLMaxCardRank += 100;
            }
        }
        oppMaxCardRank = getMaxNumber(oppLMaxCardRank, oppRMaxCardRank);
        if (firstChalSuit == color_suit) {
            isColorChal = true;
        }

        if (firstChalSuit != color_suit && oppMaxCardRank > 100) {
            isColorSurpassedByOpp = true;
        }
        Arrays.sort(my_cards, Collections.reverseOrder());
        List<Integer> spades = new ArrayList<Integer>();
        List<Integer> clubs = new ArrayList<Integer>();
        List<Integer> diamonds = new ArrayList<Integer>();
        List<Integer> hearts = new ArrayList<Integer>();

        for (int i = 0; i < my_cards.length; i++) {
            if (my_cards[i] <= 13) {
                spades.add(my_cards[i]);
            } else if (my_cards[i] <= 26) {
                clubs.add(my_cards[i] - 13);
            } else if (my_cards[i] <= 39) {
                diamonds.add(my_cards[i] - 26);
            } else if (my_cards[i] <= 52) {
                hearts.add(my_cards[i] - 39);
            }
        }
        int chal = 0;
        switch (afterMeCount) {
            case 0:
                chal = 4;
                break;
            case 1:
                chal = 3;
                break;
            case 2:
                chal = 2;
                break;
            case 3:
                chal = 1;
                break;
        }
        if (chal == 4) { //last chal
            if (frndMaxCardRank > oppMaxCardRank) {
                if (!isSelectedSuitEmpty(firstChalSuit, spades, clubs, diamonds, hearts)) {
                    return getSelectedSuitLowestCard(firstChalSuit, spades, clubs, diamonds, hearts);
                } else {
                    return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts); //to do improve for compare less
                }
            } else {//need more clear aspects
                if (isColorChal) {
                    if (!isSelectedSuitEmpty(color_suit, spades, clubs, diamonds, hearts)) {
                        tempCard = getSelectedSuitHigherRankCard(color_suit, oppMaxCardRank - 100, spades, clubs, diamonds, hearts);
                        if (tempCard != 0) {
                            return tempCard;
                        } else {
                            return getSelectedSuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                        }
                    } else {
                        return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                    }
                } else {
                    if (isColorSurpassedByOpp) {
                        if (!isSelectedSuitEmpty(firstChalSuit, spades, clubs, diamonds, hearts)) {
                            return getSelectedSuitLowestCard(firstChalSuit, spades, clubs, diamonds, hearts);
                        } else { //check for any higher color card or any lower suit card
                            tempCard = getSelectedSuitHigherRankCard(color_suit, oppMaxCardRank - 100, spades, clubs, diamonds, hearts);
                            if (tempCard != 0) {
                                return tempCard;
                            } else {//replace with anylower
                                return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                            }
                        }
                    } else { //to do
                        if (!isSelectedSuitEmpty(firstChalSuit, spades, clubs, diamonds, hearts)) {

                            tempCard = getSelectedSuitHigherRankCard(firstChalSuit, (oppMaxCardRank > 100) ? oppMaxCardRank - 100 : (oppMaxCardRank < 0) ? oppMaxCardRank + 100 : oppMaxCardRank, spades, clubs, diamonds, hearts);
                            if (tempCard != 0) {
                                return tempCard;
                            } else {
                                return getSelectedSuitLowestCard(firstChalSuit, spades, clubs, diamonds, hearts);
                            }
                        } else { //check for lower color else lower any suite
                            if (!isSelectedSuitEmpty(color_suit, spades, clubs, diamonds, hearts)) {
                                return getSelectedSuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                            } else {
                                return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                            }
                        }
                    }
                }
            }
        } //Last chal end
        //new logic start
        else if (chal == 2) { // only left player played
            Arrays.sort(unseen_cards, Collections.reverseOrder());
            List<Integer> unseenSpades = new ArrayList<Integer>();
            List<Integer> unseenClubs = new ArrayList<Integer>();
            List<Integer> unseenDiamonds = new ArrayList<Integer>();
            List<Integer> unseenHearts = new ArrayList<Integer>();

            for (int i = 0; i < unseen_cards.length; i++) {
                if (unseen_cards[i] <= 13) {
                    unseenSpades.add(unseen_cards[i]);
                } else if (unseen_cards[i] <= 26) {
                    unseenClubs.add(unseen_cards[i] - 13);
                } else if (unseen_cards[i] <= 39) {
                    unseenDiamonds.add(unseen_cards[i] - 26);
                } else if (unseen_cards[i] <= 52) {
                    unseenHearts.add(unseen_cards[i] - 39);
                }
            }

            if (!isSelectedSuitEmpty(firstChalSuit, spades, clubs, diamonds, hearts)) {
                tempCard = getSelectedSuitHighestCardWithUnseenInfoAndOppCard(firstChalSuit, oppMaxCardRank, spades, clubs, diamonds, hearts, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);
                if (tempCard != 0) {
                    return tempCard;
                } else {
                    return getSelectedSuitLowestCard(firstChalSuit, spades, clubs, diamonds, hearts);
                }
            } else {
                //neeraj start
                if (!isSelectedSuitEmpty(color_suit, spades, clubs, diamonds, hearts)) {
                    return getSelectedSuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                } else {
                    return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                }
                //neeraj end
            }
        } //new logic end
        else if (chal == 3) { // only right player left
            Arrays.sort(unseen_cards, Collections.reverseOrder());
            List<Integer> unseenSpades = new ArrayList<Integer>();
            List<Integer> unseenClubs = new ArrayList<Integer>();
            List<Integer> unseenDiamonds = new ArrayList<Integer>();
            List<Integer> unseenHearts = new ArrayList<Integer>();

            for (int i = 0; i < unseen_cards.length; i++) {
                if (unseen_cards[i] <= 13) {
                    unseenSpades.add(unseen_cards[i]);
                } else if (unseen_cards[i] <= 26) {
                    unseenClubs.add(unseen_cards[i] - 13);
                } else if (unseen_cards[i] <= 39) {
                    unseenDiamonds.add(unseen_cards[i] - 26);
                } else if (unseen_cards[i] <= 52) {
                    unseenHearts.add(unseen_cards[i] - 39);
                }
            }

            if (frndMaxCardRank > oppLMaxCardRank) { //only right user left behind
                if (!isSelectedSuitEmpty(firstChalSuit, spades, clubs, diamonds, hearts)) {
                    tempCard = getSelectedSuitOptimumHighCardWithUnseenInfoAndFrndCard(firstChalSuit, uu_card, spades, clubs, diamonds, hearts, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);
                    if (tempCard != 0) {
                        return tempCard;
                    } else {
                        return getSelectedSuitLowestCard(firstChalSuit, spades, clubs, diamonds, hearts);
                    }
                } else {
                    if (isColorChal) {//no color left
                        return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                    } else { //you can surpass here
                        //check here if shuld i play color card or any other lower card
                        if (IsThisCardIsHighestCardInUnseenCards(uu_card, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts)) {
                            if (getSelectedSuitCountInUnseenCards(color_suit, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts) == 0) {
                                return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                            } else {
                                if (getSelectedSuitCountInUnseenCards(firstChalSuit, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts) > 0) {
                                    return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                                } else {
                                    if (isSelectedSuitEmpty(color_suit, spades, clubs, diamonds, hearts)) {
                                        return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                                    } else {//i have color cards
                                        tempCard = getSelectedSuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                                        if (tempCard != 0 && frndMaxCardRank < 10 && !IsThisCardIsHighestCardInUnseenCards(tempCard, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts)) {
                                            return tempCard;
                                        } else {
                                            return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                                        }
                                    }
                                }
                            }
                        } else {
                            if (!isSelectedSuitEmpty(color_suit, spades, clubs, diamonds, hearts)) {
                                return getSelectedSuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                            } else {
                                return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                            }
                        }

                        //old
//                        if (!isSelectedSuitEmpty(color_suit, spades, clubs, diamonds, hearts)) {
//                            if (isColorSurpassedByOpp) {
//                                tempCard = getSelectedSuitHigherRankCard(color_suit, oppMaxCardRank - 100, spades, clubs, diamonds, hearts);
//                                if (tempCard != 0) {
//                                    return tempCard;
//                                } else {
//                                    return getSelectedSuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
//                                }
//                            } else {
//                                return getSelectedSuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
//                            }
//                        } else {
//                            return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
//                        }
                    }
                }
            } else {
                if (!isColorSurpassedByOpp) {
                    if (!isSelectedSuitEmpty(firstChalSuit, spades, clubs, diamonds, hearts)) {
                        tempCard = getSelectedSuitHighestCardWithUnseenInfoAndOppCard(firstChalSuit, oppMaxCardRank, spades, clubs, diamonds, hearts, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);
                        if (tempCard != 0) {
                            return tempCard;
                        } else {
                            tempCard = getSelectedSuitHigherRankCard(firstChalSuit, oppMaxCardRank, spades, clubs, diamonds, hearts);
                            if (tempCard == 0) {
                                return getSelectedSuitLowestCard(firstChalSuit, spades, clubs, diamonds, hearts);
                            } else {//should i play higher than opponent or lowest
                                if (getRankByCard(tempCard) < 10) {
                                    return tempCard;
                                } else {
                                    return getSelectedSuitLowestCard(firstChalSuit, spades, clubs, diamonds, hearts);
                                }
                            }
                        }
                    } else {//i don't have firstChalSuit cards
                        if (isColorChal) {
                            return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                        } else {
                            if (!isSelectedSuitEmpty(color_suit, spades, clubs, diamonds, hearts)) {
                                return getSelectedSuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                            } else {
                                return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                            }
                        }
                    }
                } else {// frndcard is lesser, and surpassed by left opp.
                    if (!isSelectedSuitEmpty(firstChalSuit, spades, clubs, diamonds, hearts)) {
                        return getSelectedSuitLowestCard(firstChalSuit, spades, clubs, diamonds, hearts);
                    } else {
                        if (!isSelectedSuitEmpty(color_suit, spades, clubs, diamonds, hearts)) {
                            tempCard = getSelectedSuitHigherRankCard(color_suit, (oppMaxCardRank > 100) ? oppMaxCardRank - 100 : oppMaxCardRank, spades, clubs, diamonds, hearts);
                            if (tempCard == 0) {
                                return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                            } else {//decide should i play higher card or highest card of color suit
                                return tempCard; //to do in FUTURE
                            }
                        } else {
                            return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                        }

                    }
                }
            }

        } // neither first chal nor last chal end
        else if (chal == 1) { //first chal

            Arrays.sort(unseen_cards, Collections.reverseOrder());
            List<Integer> unseenSpades = new ArrayList<Integer>();
            List<Integer> unseenClubs = new ArrayList<Integer>();
            List<Integer> unseenDiamonds = new ArrayList<Integer>();
            List<Integer> unseenHearts = new ArrayList<Integer>();

            for (int i = 0; i < unseen_cards.length; i++) {
                if (unseen_cards[i] <= 13) {
                    unseenSpades.add(unseen_cards[i]);
                } else if (unseen_cards[i] <= 26) {
                    unseenClubs.add(unseen_cards[i] - 13);
                } else if (unseen_cards[i] <= 39) {
                    unseenDiamonds.add(unseen_cards[i] - 26);
                } else if (unseen_cards[i] <= 52) {
                    unseenHearts.add(unseen_cards[i] - 39);
                }
            }
            boolean shouldGoWithHighCardOrShortageCard = true;//(Math.random()>0.5)?true:false;
            if (shouldGoWithHighCardOrShortageCard) {
                tempCard = getRandomHighestCardWithUnseenInfo(spades, clubs, diamonds, hearts, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);
                if (tempCard != 0) {
                    return tempCard;
                } else {//get any lowest + replace in future with shortage card logic
                    return getAnySuitLowestCard(color_suit, spades, clubs, diamonds, hearts);
                }
            } else {//shortage card logic

            }
        } //First chal end

        return result;
    }

    public String getSuitByCard(int card) {
        String result = "";
        if (card == 0) {
            return result;
        } else if (card <= 13) {
            result = "Spade";
        } else if (card <= 26) {
            result = "Club";
        } else if (card <= 39) {
            result = "Diamond";
        } else {
            result = "Heart";
        }
        return result;
    }

    public int getRankByCard(int card) {
        int result = 0;
        if (card == 0) {
            return result;
        } else if (card <= 13) {
            result = card;
        } else if (card <= 26) {
            result = card - 13;
        } else if (card <= 39) {
            result = card - 26;
        } else {
            result = card - 39;
        }
        return result;
    }

    public boolean isSelectedSuitEmpty(String SelectedSuit, List<Integer> spades, List<Integer> clubs, List<Integer> diamonds, List<Integer> hearts) {
        boolean result = true;
        if (SelectedSuit == "Spade" && !spades.isEmpty()) {
            result = false;
        } else if (SelectedSuit == "Club" && !clubs.isEmpty()) {
            result = false;
        } else if (SelectedSuit == "Diamond" && !diamonds.isEmpty()) {
            result = false;
        } else if (SelectedSuit == "Heart" && !hearts.isEmpty()) {
            result = false;
        }
        return result;
    }

    public int getSelectedSuitLowestCard(String selectedSuit, List<Integer> spades, List<Integer> clubs, List<Integer> diamonds, List<Integer> hearts) {
        int result = 0;
        if (selectedSuit == "Spade" && !spades.isEmpty()) {
            result = spades.get(spades.size() - 1);
        } else if (selectedSuit == "Club" && !clubs.isEmpty()) {
            result = clubs.get(clubs.size() - 1) + 13;
        } else if (selectedSuit == "Diamond" && !diamonds.isEmpty()) {
            result = diamonds.get(diamonds.size() - 1) + 26;
        } else if (selectedSuit == "Heart" && !hearts.isEmpty()) {
            result = hearts.get(hearts.size() - 1) + 39;
        }
        return result;
    }

    public int getAnySuitLowestCard(String colorSuit, List<Integer> spades, List<Integer> clubs, List<Integer> diamonds, List<Integer> hearts) {
        int result = 0;
        int minRank = 999;
        int tempRank = 0;
        String minSuit = "";

        if (colorSuit != "Spade" && !spades.isEmpty()) {
            tempRank = spades.get(spades.size() - 1);
            if (tempRank < minRank) {
                minRank = tempRank;
                minSuit = "Spade";
            }
        }
        if (colorSuit != "Club" && !clubs.isEmpty()) {
            tempRank = clubs.get(clubs.size() - 1);
            if (tempRank < minRank) {
                minRank = tempRank;
                minSuit = "Club";
            }
        }
        if (colorSuit != "Diamond" && !diamonds.isEmpty()) {
            tempRank = diamonds.get(diamonds.size() - 1);
            if (tempRank < minRank) {
                minRank = tempRank;
                minSuit = "Diamond";
            }
        }
        if (colorSuit != "Heart" && !hearts.isEmpty()) {
            tempRank = hearts.get(hearts.size() - 1);
            if (tempRank < minRank) {
                minRank = tempRank;
                minSuit = "Heart";
            }
        }
        if (minSuit == "") { //if no any other card found case
            result = getSelectedSuitLowestCard(colorSuit, spades, clubs, diamonds, hearts);
        } else {
            result = getSelectedSuitLowestCard(minSuit, spades, clubs, diamonds, hearts);
        }

        return result;
    }

    public int getSelectedSuitHigherRankCard(String selectedSuit, int TopRank, List<Integer> spades, List<Integer> clubs, List<Integer> diamonds, List<Integer> hearts) {
        int result = 0;
        //neeraj start
        switch (selectedSuit) {
            case "Spade":
                for (int i = spades.size() - 1; i >= 0; i--) {
                    if (spades.get(i) > TopRank) {
                        return spades.get(i);
                    }
                }
                break;
            case "Club":
                for (int i = clubs.size() - 1; i >= 0; i--) {
                    if (clubs.get(i) > TopRank) {
                        return clubs.get(i) + 13;
                    }
                }
                break;
            case "Diamond":
                for (int i = diamonds.size() - 1; i >= 0; i--) {
                    if (diamonds.get(i) > TopRank) {
                        return diamonds.get(i) + 26;
                    }
                }
                break;
            case "Heart":
                for (int i = hearts.size() - 1; i >= 0; i--) {
                    if (hearts.get(i) > TopRank) {
                        return hearts.get(i) + 39;
                    }
                }
                break;
        }

//end-neeraj
        return result;
    }

    public int getSelectedSuitHighestCardWithUnseenInfo(String selectedSuit, List<Integer> spades, List<Integer> clubs, List<Integer> diamonds, List<Integer> hearts, List<Integer> unseenSpades, List<Integer> unseenClubs, List<Integer> unseenDiamonds, List<Integer> unseenHearts) //FUTURE CHANGE TO DO UP LEVEL
    {
        int result = 0;
        switch (selectedSuit) {
            case "Spade":
                if (!spades.isEmpty()) {
                    if (unseenSpades.isEmpty()) {
                        result = spades.get(0);
                    } else {
                        result = (spades.get(0) > unseenSpades.get(0)) ? spades.get(0) : 0;
                    }
                }
                break;
            case "Club":
                if (!clubs.isEmpty()) {
                    if (unseenClubs.isEmpty()) {
                        result = clubs.get(0) + 13;
                        //getSelectedSuitHigherRankCard(selectedSuit, oppMaxCardRank - 100, spades, clubs, diamonds, hearts); FUTURE CHANGE TO DO UP LEVEL
                    } else {
                        result = (clubs.get(0) > unseenClubs.get(0)) ? clubs.get(0) + 13 : 0;
                    }
                }
                break;
            case "Diamond":
                if (!diamonds.isEmpty()) {
                    if (unseenDiamonds.isEmpty()) {
                        result = diamonds.get(0) + 26;
                    } else {
                        result = (diamonds.get(0) > unseenDiamonds.get(0)) ? diamonds.get(0) + 26 : 0;
                    }
                }
                break;
            case "Heart":
                if (!hearts.isEmpty()) {
                    if (unseenHearts.isEmpty()) {
                        result = hearts.get(0) + 39;
                    } else {
                        result = (hearts.get(0) > unseenHearts.get(0)) ? hearts.get(0) + 39 : 0;
                    }
                }
                break;
        }
        return result;
    }

    public int getSelectedSuitCountInUnseenCards(String selectedSuit, List<Integer> unseenSpades, List<Integer> unseenClubs, List<Integer> unseenDiamonds, List<Integer> unseenHearts) {
        int result = 0;
        switch (selectedSuit) {
            case "Spade":
                result = unseenSpades.size();
                break;
            case "Club":
                result = unseenClubs.size();
                break;
            case "Diamond":
                result = unseenDiamonds.size();
                break;
            case "Heart":
                result = unseenHearts.size();
                break;
        }
        return result;
    }

    public boolean IsThisCardIsHighestCardInUnseenCards(int thisCard, List<Integer> unseenSpades, List<Integer> unseenClubs, List<Integer> unseenDiamonds, List<Integer> unseenHearts) {
        boolean result = false;

        if (thisCard <= 13) {
            if (unseenSpades.isEmpty()) {
                return true;
            }
            if (getRankByCard(thisCard) > unseenSpades.get(0)) {
                return true;
            }
        } else if (thisCard <= 26) {
            if (unseenClubs.isEmpty()) {
                return true;
            }
            if (getRankByCard(thisCard) > unseenClubs.get(0)) {
                return true;
            }
        } else if (thisCard <= 39) {

            if (unseenDiamonds.isEmpty()) {
                return true;
            }

            if (getRankByCard(thisCard) > unseenDiamonds.get(0)) {
                return true;
            }
        } else if (thisCard <= 52) {

            if (unseenHearts.isEmpty()) {
                return true;
            }

            if (getRankByCard(thisCard) > unseenHearts.get(0)) {
                return true;
            }
        }
        return result;
    }

    public int getSelectedSuitHighestCardWithUnseenInfoAndOppCard(String selectedSuit, int oppMaxCardRank, List<Integer> spades, List<Integer> clubs, List<Integer> diamonds, List<Integer> hearts, List<Integer> unseenSpades, List<Integer> unseenClubs, List<Integer> unseenDiamonds, List<Integer> unseenHearts) //FUTURE CHANGE TO DO UP LEVEL
    {
        int result = 0;
        switch (selectedSuit) {
            case "Spade":
                if (!spades.isEmpty()) {
                    for (int k = spades.size() - 1; k >= 0; k--) {
                        if (spades.get(k) > oppMaxCardRank) {
                            return getSelectedSuitHighestCardWithUnseenInfo(selectedSuit, spades, clubs, diamonds, hearts, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);
                        }
                    }
                    return 0;
                }
                break;
            case "Club":
                if (!clubs.isEmpty()) {
                    for (int k = clubs.size() - 1; k >= 0; k--) {
                        if (clubs.get(k) > oppMaxCardRank) {
                            return getSelectedSuitHighestCardWithUnseenInfo(selectedSuit, spades, clubs, diamonds, hearts, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);
                        }
                    }
                    return 0;

                }
                break;
            case "Diamond":
                if (!diamonds.isEmpty()) {
                    for (int k = diamonds.size() - 1; k >= 0; k--) {
                        if (diamonds.get(k) > oppMaxCardRank) {
                            return getSelectedSuitHighestCardWithUnseenInfo(selectedSuit, spades, clubs, diamonds, hearts, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);
                        }
                    }
                    return 0;
                }
                break;
            case "Heart":
                if (!hearts.isEmpty()) {
                    for (int k = hearts.size() - 1; k >= 0; k--) {
                        if (hearts.get(k) > oppMaxCardRank) {
                            return getSelectedSuitHighestCardWithUnseenInfo(selectedSuit, spades, clubs, diamonds, hearts, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);
                        }
                    }
                    return 0;
                }
                break;
        }
        return result;
    }

    public int getRandomHighestCardWithUnseenInfo(List<Integer> spades, List<Integer> clubs, List<Integer> diamonds, List<Integer> hearts, List<Integer> unseenSpades, List<Integer> unseenClubs, List<Integer> unseenDiamonds, List<Integer> unseenHearts) {
        int result = 0;
        List<String> suits = new ArrayList<String>();
        suits.add("Spade");
        suits.add("Club");
        suits.add("Diamond");
        suits.add("Heart");

        boolean cardFound = false;
        int rand = 0;
        int tempCard = 0;
        while (cardFound != true && suits.size() > 0) {
            rand = getRandomInteger(0, suits.size());
            tempCard = getSelectedSuitHighestCardWithUnseenInfo(suits.get(rand), spades, clubs, diamonds, hearts, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);
            if (tempCard != 0) {
                cardFound = true;
                return tempCard;
            } else {
                suits.remove(rand);
            }
        }
        return result;
    }

    public int getSelectedSuitOptimumHighCardWithUnseenInfoAndFrndCard(String selectedSuit, int frndCard, List<Integer> spades, List<Integer> clubs, List<Integer> diamonds, List<Integer> hearts, List<Integer> unseenSpades, List<Integer> unseenClubs, List<Integer> unseenDiamonds, List<Integer> unseenHearts) {
        int result = 0;
        int frndRank = 0;
        int myRank = 0;
        int tempCard;
        boolean canGoWithLowerCardThanFrndCard = true;
        boolean isFrndCardIsHighestInUnSeen=IsThisCardIsHighestCardInUnseenCards(frndCard, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);



        switch (selectedSuit) {
            case "Spade":
                if (!spades.isEmpty()) {
                    if (isFrndCardIsHighestInUnSeen) {
                        return spades.get(spades.size() - 1);
                    } else {
                        return getSelectedSuitHighestCardWithUnseenInfo(selectedSuit, spades, clubs, diamonds, hearts, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);
                    }
                }

//                        result = (spades.get(0) > unseenSpades.get(0)) ? spades.get(0) : 0;
//                    }
//                    if (result != 0 && spades.size() > 1) {
//                        frndRank = getRankByCard(frndCard);
//                        myRank = result;
//                        for (int r = frndRank + 1; r < myRank; r++) {
//                            if (spades.contains(r)) {
//                                continue;
//                            }
//                            if (unseenSpades.contains(r)) {
//                                canGoWithLowerCardThanFrndCard = false;
//                                break;
//                            }
//                        }


                break;
            case "Club":
                if (!clubs.isEmpty()) {
                    if (isFrndCardIsHighestInUnSeen) {
                        return clubs.get(clubs.size() - 1) + 13;
                    } else {
                        return getSelectedSuitHighestCardWithUnseenInfo(selectedSuit, spades, clubs, diamonds, hearts, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);
                    }
                }
                break;
            case "Diamond":
                if (!diamonds.isEmpty()) {
                    if (isFrndCardIsHighestInUnSeen) {
                        return diamonds.get(diamonds.size() - 1) + 26;
                    } else {
                        return getSelectedSuitHighestCardWithUnseenInfo(selectedSuit, spades, clubs, diamonds, hearts, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);
                    }
                }
                break;
            case "Heart":
                if (!hearts.isEmpty()) {
                    if (isFrndCardIsHighestInUnSeen) {
                        return hearts.get(hearts.size() - 1) + 39;
                    } else {
                        return getSelectedSuitHighestCardWithUnseenInfo(selectedSuit, spades, clubs, diamonds, hearts, unseenSpades, unseenClubs, unseenDiamonds, unseenHearts);
                    }
                }
                break;
        }
//        if (canGoWithLowerCardThanFrndCard) {
//            result = getSelectedSuitLowestCard(selectedSuit, spades, clubs, diamonds, hearts);
//        }
        return result;
    }

    public int getRandomInteger(int minimum, int maximum) {
        return ((int) (Math.random() * (maximum - minimum))) + minimum;
    }

    public String getPlayerDeckStringByNumber(int... cards) {
        String result = "";
        for (int i = 0; i < 13; i++) {
            if (i < cards.length) {
                if (cards[i] < 10) {
                    result += "0" + cards[i];
                } else {
                    result += cards[i];
                }
            } else {
                result += "00";
            }
        }
        return result;
    }
}