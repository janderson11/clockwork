package app.main.model;

import javafx.collections.transformation.SortedList;

import java.util.*;

public class Player {
    private int id;
    private List<Card> cardsInHand;
    private List<Card> wreckage;
    private int aggro;
    int totalScore;

    public Player(int id) {
        this.id = id;
        cardsInHand = new ArrayList<>();
        wreckage = new ArrayList<>();
        Random r = new Random();
        aggro = r.nextInt(16) - 8;
    }

    public void dealCard(Card card) {
        cardsInHand.add(card);
    }

    public int getNumCards() {
        return cardsInHand.size();
    }

    public Card playLowestCard() {
        Card lowest = null;
        if (cardsInHand.size() > 0) {
            lowest = cardsInHand.get(0);
            cardsInHand.remove(0);
        }
        return lowest;
    }

    public void outputScore() {
        System.out.println("Player " + id + ": " + totalScore + " wreckage");
    }

    public void addWreckage(List<Card> newWreckage) {
        wreckage.addAll(newWreckage);
        Collections.sort(wreckage);
    }

    public List<Integer> getWreckageVals() {
        List<Integer> vals = new ArrayList<>();
        for (int i = 0; i < wreckage.size(); i++) {
            vals.add(wreckage.get(i).getValue());
        }
        return vals;
    }

    public void sortCards() {
        Collections.sort(cardsInHand);
    }

    public List<Card> discardLowerCards(int value) {
        List<Card> cardsToDiscard = new ArrayList<>();
        for (int i = 0; i < cardsInHand.size(); i++) {
            if (cardsInHand.get(i).getValue() < value) {
                cardsToDiscard.add(cardsInHand.get(i));
            }
        }
        cardsInHand.removeAll(cardsToDiscard);
        return cardsToDiscard;
    }

    public Card getCard(int index) {
        return cardsInHand.get(index);
    }

    public int getAggro() {
        return aggro;
    }

    public int getLowestVal() {
        if (cardsInHand.size() < 1) {
            return 0;
        }
        return cardsInHand.get(0).getValue();
    }

    public int getSecondLowestVal() {
        if (cardsInHand.size() < 2) {
            return 99;
        }
        return cardsInHand.get(1).getValue();
    }

    public void clearCards() {
        cardsInHand.clear();
    }

    public int getRoundScore(int currentRound) {
        int roundScore = 0;
        Set<Integer> uniqueGroups = new HashSet<>();
        for (int i = 0; i < wreckage.size(); i++) {
            roundScore++;
            uniqueGroups.add(wreckage.get(i).getGroup());
        }
        roundScore += (uniqueGroups.size() * currentRound);
        return roundScore;
    }

    public void addTotalScore(int score) {
        totalScore += score;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void clearWreckage() {
        wreckage.clear();
    }

    public boolean hasMachineCore() {
        for (int i = 0; i < wreckage.size(); i++) {
            if (wreckage.get(i).equals(new Card(0,0))) {
                wreckage.remove(i);
                return true;
            }
        }
        return false;
    }
}
