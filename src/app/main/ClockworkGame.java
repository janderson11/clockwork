package app.main;

import app.main.model.Card;
import app.main.model.Player;
import app.main.util.ComputerAi;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class ClockworkGame {

    private int numPlayers;
    private int numCards;
    private final int TOTAL_CARDS_PER_PLAYER = 24; //This includes the cards that aren't dealt to players; must be higher
    private final int CARDS_PER_PLAYER = 10;
    private int timesPassed;
    private int currentRound;
    private int numRounds;

    private List<Card> allCards; //List of cards never goes away.
    private List<Card> machine;
    private List<Player> players;

    private int currentPlayerTurn;

    public ClockworkGame(int numPlayers, int numRounds) {
        this.numPlayers = numPlayers;
        this.numRounds = numRounds;
        timesPassed = 0;
        numCards = (numPlayers * TOTAL_CARDS_PER_PLAYER);

        this.allCards = new ArrayList<>();
        this.machine = new ArrayList<>();
        this.players = new ArrayList<>();

        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player(i));
        }

        int currentGroup = 0;
        machine.add(new Card(0, currentGroup));
        for (int i = 1; i <= numCards; i++) {
            if((i - 1) % 10 == 0) currentGroup++;
            allCards.add(new Card(i, currentGroup));
        }
        currentRound = 1;
    }

    public void setupGame() {
        machine.clear();
        machine.add(new Card(0, 0));

        Collections.shuffle(allCards);
        int cardCount = 0;
        for(int i = 0; i < CARDS_PER_PLAYER; i++) {
            for (int j = 0; j < numPlayers; j++) {
                players.get(j).dealCard(allCards.get(cardCount));
                cardCount++;
            }
        }
        for (int i = 0; i < numPlayers; i++) {
            players.get(i).sortCards();
        }
        Random rand = new Random();
        if (currentRound == 1) {
            currentPlayerTurn = rand.nextInt(numPlayers);
        } else {
            int highestScore = 0;
            int highestScorePlayer = 0;
            for (int i = 0; i < numPlayers; i++) {
                if (players.get(i).getTotalScore() > highestScore) {
                    highestScore = players.get(i).getTotalScore();
                    highestScorePlayer = i;
                }
            }
            currentPlayerTurn = highestScorePlayer;
        }
    }

    public void playGame() {
        for (int i = 0; i < numRounds; i++) {
            while (!gameOverCheck()) {
                if (currentPlayerTurn == 0) {
                    playerTurn(currentPlayerTurn); //change to computerTurn(...) if you want to watch just ai play
                } else {
                    computerTurn(currentPlayerTurn);
                }
                currentPlayerTurn = (currentPlayerTurn + 1) % numPlayers;
            }
            for (int j = 0; j < numPlayers; j++) {
                players.get(j).clearCards();
            }
            if (currentRound < numRounds) {
                for (int j = 0; j < players.size(); j++) {
                    players.get(j).addTotalScore(players.get(j).getRoundScore(currentRound));
                    players.get(j).clearWreckage();
                }
                outputScores(false);
                currentRound += 1;
                setupGame();
            }
        }
        for (int j = 0; j < players.size(); j++) {
            players.get(j).addTotalScore(players.get(j).getRoundScore(currentRound));
            players.get(j).clearWreckage();
        }
        outputScores(true);
    }


    private void playerTurn(int currentPlayerTurn) {
        System.out.print("\n");
        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Player: " + i + "; Cards: " + players.get(i).getNumCards() + "; Round Score: " + players.get(i).getRoundScore(currentRound) + "; Total Score: " + Math.addExact(players.get(i).getTotalScore(), players.get(i).getRoundScore(currentRound)) + "; Wreckage Vals: " + players.get(i).getWreckageVals() + (i==0 ? " (You)" : ""));
        }
        List<Integer> cardsVals = new ArrayList<>();
        for (int i = 0; i < players.get(currentPlayerTurn).getNumCards(); i++) {
            cardsVals.add(players.get(currentPlayerTurn).getCard(i).getValue());
        }
        boolean play = false;
        if (players.get(currentPlayerTurn).getNumCards() == 0) {
            resolveTurn(currentPlayerTurn,play);
        } else {
            System.out.println("Your Cards: " + cardsVals);
            System.out.println("Card on top of machine: " + machine.get(machine.size() - 1).getValue() + "\n");
            System.out.println("What would you like to do (pass[1] or play[2]): ");

            Scanner s = new Scanner(System.in);
            int input = s.nextInt();
            if (input == 2 && players.get(currentPlayerTurn).getNumCards() > 0) {
                play = true;
            }
            System.out.print("\n");
            resolveTurn(currentPlayerTurn, play);
        }
    }

    private void computerTurn(int currentPlayerTurn) {
        Random r = new Random();
        int randVal = r.nextInt(100);
        int decisionChance = ComputerAi.makeDecision(machine.get(machine.size()-1).getValue(), players.get(currentPlayerTurn).getLowestVal(),
                players.get(currentPlayerTurn).getSecondLowestVal(), (timesPassed <= numPlayers), players.get(currentPlayerTurn).getAggro());
        if (decisionChance > 100) decisionChance = 100;
        if (decisionChance < 0) decisionChance = 0;
        boolean play = randVal < decisionChance;

        if (players.get(currentPlayerTurn).getNumCards() == 0) {
            play = false;
        }
        resolveTurn(currentPlayerTurn, play);
    }

    private void resolveTurn(int playerId, boolean play) {
        if (play) {
            timesPassed = 0;
            Card playedCard = players.get(playerId).playLowestCard();
            System.out.println("[[Player " + playerId + " played " + playedCard.getValue() + ".]]");
            machine.add(playedCard);
            List<Integer> playersWhoDiscarded = new ArrayList<>();
            List<Card> cardsDiscarded = new ArrayList<>();
            for (int i = 0; i < numPlayers; i++) {
                int numCards = cardsDiscarded.size();
                cardsDiscarded.addAll(players.get(i).discardLowerCards( playedCard.getValue()));
                if (cardsDiscarded.size() > numCards) {
                    playersWhoDiscarded.add(i);
                }
            }
            List<Integer> discardedVals = new ArrayList<>();
            for (int i = 0; i < cardsDiscarded.size(); i++) {
                discardedVals.add(cardsDiscarded.get(i).getValue());
            }
            Collections.sort(discardedVals);
            if (cardsDiscarded.size() > 1) {
                int beforeScore = players.get(playerId).getRoundScore(currentRound);
                players.get(playerId).addWreckage(cardsDiscarded);
                int scoreDiscarded = players.get(playerId).getRoundScore(currentRound) - beforeScore;
                System.out.println("Player " + playerId + " got " + scoreDiscarded + " wreckage from player(s) " + playersWhoDiscarded.toString() + " from " + cardsDiscarded.size() + " different cards (" + discardedVals + ").");
            } else if (cardsDiscarded.size() == 1) {
                int beforeScore = players.get(playersWhoDiscarded.get(0)).getRoundScore(currentRound);
                players.get(playersWhoDiscarded.get(0)).addWreckage(cardsDiscarded);
                int scoreDiscarded = players.get(playersWhoDiscarded.get(0)).getRoundScore(currentRound) - beforeScore;
                System.out.println("Player " + playersWhoDiscarded.get(0) + " got " + scoreDiscarded + " wreckage from their [" + cardsDiscarded.get(0).getValue() + "] card.");
            }

        } else {
            System.out.println("[[Player " + playerId + " passed.]]");
            timesPassed += 1;
            if (timesPassed >= numPlayers * 2) {
                timesPassed = 0;
                System.out.println("The machine stalled!");
                stallTheMachine();
            }
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void stallTheMachine() {
        List<Card> cardsDiscarded = new ArrayList<>();
        for(int i = 0; i < numPlayers; i++) {
            cardsDiscarded.add(players.get(i).playLowestCard());
            if (players.get(i).hasMachineCore()) { //also removes Machine Core
                machine.add(new Card(0,0));
            }
        }
        int lowestCard = Integer.MAX_VALUE;
        for(int i = 0; i < cardsDiscarded.size(); i++) {
            if (cardsDiscarded.get(i) != null && cardsDiscarded.get(i).getValue() < lowestCard) {
                lowestCard = cardsDiscarded.get(i).getValue();
            }
        }
        for(int i = 0; i < cardsDiscarded.size(); i++) {
            if (cardsDiscarded.get(i) != null &&
                    cardsDiscarded.get(i).getValue() == lowestCard) {
                int beforeScore = players.get(i).getRoundScore(currentRound);
                players.get(i).addWreckage(machine);
                int machineScore = players.get(i).getRoundScore(currentRound) - beforeScore;
                System.out.println("Player " + i + " got the machine (Lowest Card: " + lowestCard + " Wreckage: " + machineScore + ")!");
                machine.clear();
                machine.add(cardsDiscarded.get(i));
            } else if (cardsDiscarded.get(i) != null) {
                players.get(i).addWreckage(Arrays.asList(cardsDiscarded.get(i)));
            }
        }
    }

    private void outputScores(boolean finalRound) {
        if(finalRound) {
            System.out.println("\n===========================Final Scores================================");
            int lowestScore = 100;
            int winningPlayer = 0;
            for (int i = 0; i < numPlayers; i++) {
                if (players.get(i).getTotalScore() < lowestScore) {
                    lowestScore = players.get(i).getTotalScore();
                    winningPlayer = i;
                }
                players.get(i).outputScore();
            }
            System.out.println("\nPlayer " + winningPlayer + " is the winner!");
        } else {
            if (currentRound <= numRounds - 1) {
                System.out.println("\n===========================Round " + currentRound + " Scores================================");
                int highestScore = 0;
                int losingPlayer = 0;
                for (int i = 0; i < numPlayers; i++) {
                    if (players.get(i).getTotalScore() > highestScore) {
                        highestScore = players.get(i).getTotalScore();
                        losingPlayer = i;
                    }
                    players.get(i).outputScore();
                }
                System.out.println("Player " + losingPlayer + " is the CLUNK and will go first next round!\n");
            }
        }
    }

    private boolean gameOverCheck() {
        int numPlayersWithCards = 0;
        for (int i = 0; i < numPlayers; i++) {
            if (players.get(i).getNumCards() > 0) numPlayersWithCards++;
        }
        return numPlayersWithCards <= 1;
    }
}
