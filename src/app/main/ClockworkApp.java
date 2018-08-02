package app.main;

import java.util.InputMismatchException;
import java.util.Scanner;

public class ClockworkApp {

    public static void main(String[] args) {
        try {
            Scanner s = new Scanner(System.in);
            boolean incorrectPlayers = true;
            boolean incorrectRounds = true;
            int numPlayers = 0, numRounds = 0;

            while(incorrectPlayers) {
                System.out.println("How many players would you like to play with? ");
                numPlayers = s.nextInt();
                if (numPlayers > 10 || numPlayers < 2) {
                    System.out.println("numPlayers must be between 2 and 10 inclusive.");
                    incorrectPlayers = true;
                } else {
                    incorrectPlayers = false;
                }
            }

            while(incorrectRounds) {
                System.out.println("\nHow many rounds would you like to play? ");
                numRounds = s.nextInt();
                if (numRounds < 1 || numRounds > 10) {
                    System.out.println("numRounds must be between 1 and 10 inclusive.");
                    incorrectRounds = true;
                } else {
                    incorrectRounds = false;
                }
            }

            ClockworkGame game = new ClockworkGame(numPlayers, numRounds);

            game.setupGame();
            game.playGame();

        } catch(InputMismatchException e) {
            System.out.println("Players and rounds must be integers.");
            e.printStackTrace();
        }
    }
}
