import java.util.*;

public class BlackJack {

    private final String[] SUITS = { "C", "D", "H", "S" };
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };

    private final Player player;
    private final Player computer;
    private List<Card> deck;
    private final Scanner in;
    private boolean ongoingTurn = true;
    private int playerScore = 0;
    private int computerScore = 0;
    private int chips = 25;
    private int wager;
    private boolean turn = true;

    public BlackJack() {
        this.player = new Player();
        this.computer = new Player();
        this.in = new Scanner(System.in);
    }

    public void play() {
        turn = true;
        wager();
        shuffleAndDeal();
        takeTurn(false);
        takeTurn(true);
        endRound();
    }

    private void wager() {
        do {
            System.out.println("Your total chips right now: " + chips);
            System.out.println("How many chips would you like to wager(Min: 1, Max: 25)");
            wager = in.nextInt();
            if (wager > chips) {
                System.out.println("Sorry, you only have " + chips + " chips remaining, but you wagered " + wager + "chips.");
            }
        } while (wager < 1 || wager > 25 || wager > chips);
    }

    public void shuffleAndDeal() {
        if (deck == null) {
            initializeDeck();
        }
        Collections.shuffle(deck);  // shuffles the deck
        while (player.getHand(false).size() < 2) {
            player.takeCard(deck.remove(0));    // deal 2 cards to the
            computer.takeCard(deck.remove(0));  // player and the computer
        }

        playerScore = player.calculateScore();
        computerScore = computer.calculateScore();
    }

        ////////// PRIVATE METHODS /////////////////////////////////////////////////////
    private void initializeDeck() {
        deck = new ArrayList<>(52);

        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(rank, suit));     // adds 52 cards to the deck (13 ranks, 4 suits)
            }
        }
    }

    private void takeTurn(boolean cpu) {
        if (!cpu) {
            showHand("initial");
            String action;
            do {
                System.out.println("Hit or Stand(H or S)");
                action = in.nextLine().toUpperCase();
            } while (action.equals("H") == false && action.equals("S") == false);
            if (action.equals("H")) {
                player.takeCard(deck.remove(0));
                playerScore = player.calculateScore();
                showHand("player");
                action = "x";

            } else {
            }
        } else {
            showHand("computer");
            while (computerScore < 17) {
                computer.takeCard(deck.remove(0));
                computerScore = computer.calculateScore();
                showHand("computer");
            }
        }
    }

    private void endRound() {
        showHand("player");
        showHand("computer");

        if (playerScore > 21) {
            System.out.println("Sorry, your score has gone over a 21 and you have lost this round.");
            chips -= (wager);
            if (chips <= 0) {
                endGame();
            }
        } else if(computerScore > 21) {
            System.out.println("The computer has gone over 21 so you have won this round.");
            chips += wager;
        } else if (computerScore == playerScore && playerScore <= 21) {
            System.out.println("You have tied with the computer this round.");
        } else if (playerScore == 21 && player.hand.size() == 2) {
            System.out.println("Congratulations, that is a BlackJack. You have won this round.");
            chips += (wager * 3/2);
        } else if (computerScore > playerScore && computerScore <= 21) {
            System.out.println("You have lost this round " + playerScore + " to " + computerScore + ".");
            chips -= wager;
        } else if (playerScore > computerScore && playerScore <= 21) {
            System.out.println("You have won this round " + playerScore + " to " + computerScore + ".");
            chips += wager;
        }
        String answer = "";
        do {
            System.out.println("Would you like to end the game here or continue playing(E or C)");
            answer = in.nextLine().toUpperCase();
        } while (answer.equals("E") == false && answer.equals("C") == false);
        if (answer.equals("E")) {
            endGame();
        } else {
            play();
        }

    }

    private void showHand(String type) {
        if (type.equals("initial")) {
            System.out.println("\nPLAYER hand: " + player.getHand(false));   // show player's full hand and cpus partial hand
            System.out.println("\nCPU hand: " + computer.getHand(true));
        } else if (type.equals("cpu")) {
            System.out.println("\nCPU hand: " + computer.getHand(false)); //show cpu's full hand
        } else if (type.equals("player")) {
            System.out.println("\nPLAYER hand: " + player.getHand(false)); //shows player's full hand
        }
    }

    private void endGame() {
        if (chips <= 0) {
            System.out.println("You ran out of chips and lost.");
        } else {
            System.out.println("You ended up with " + chips + " chips. Good job!");
        }
        System.exit(0);
    }

        ////////// MAIN METHOD /////////////////////////////////////////////////////////

    public static void main(String[] args) {
        System.out.println("#########################################################");
        System.out.println("#                                                       #");
        System.out.println("#   ####### #######   ####### ####### ####### #     #   #");
        System.out.println("#   #       #     #   #          #    #       #     #   #");
        System.out.println("#   #  #### #     #   #####      #    ####### #######   #");
        System.out.println("#   #     # #     #   #          #          # #     #   #");
        System.out.println("#   ####### #######   #       ####### ####### #     #   #");
        System.out.println("#                                                       #");
        System.out.println("#   A human v. CPU rendition of the classic card game   #");
        System.out.println("#   Go Fish. Play the game, read and modify the code,   #");
        System.out.println("#   and make it your own!                               #");
        System.out.println("#                                                       #");
        System.out.println("#########################################################");

        new BlackJack().play();
    }
}