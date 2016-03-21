import java.util.Observable;

/**
 * Main game logic class. Is also observed by BoardView in order to allow updating when the server sends information
 * about the other player's move.
 */
public class NoughtsCrosses extends Observable {

    public static final int BLANK = 0;
    public static final int CROSS = 1;
    public static final int NOUGHT = 2;
    private final int playerNumber;
    private final String opponent;
    private final ClientSender sender;
    private boolean isTurn;
    private int[][] board;

    /**
     * Create a new game with empty board
     */
    public NoughtsCrosses(int playerNumber, String opponent, ClientSender sender) {
        this.playerNumber = playerNumber;
        board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = BLANK;
            }
        }
        isTurn = false;
        this.opponent = opponent;
        this.sender = sender;
        setChanged();
        notifyObservers();
    }

    /**
     * Get symbol at given location
     *
     * @param i the row
     * @param j the column
     * @return the symbol at that location
     */
    public int get(int i, int j) {
        return board[i][j];
    }

    /**
     * Is it this player's turn?
     *
     * @return true if it is this player's turn false if not
     */
    public boolean isTurn() {
        return isTurn;
    }

    /**
     * Set if it is this player's turn
     *
     * @param isTurn is it this player's turn?
     */
    public void setTurn(boolean isTurn) {
        this.isTurn = isTurn;
        setChanged();
        notifyObservers();
    }

    /**
     * Let the player play at a particular location
     *
     * @param i the row
     * @param j the column
     */
    public void turn(int i, int j) {
        if (board[i][j] == BLANK) {
            if (isTurn) {
                board[i][j] = playerNumber; // if player 1 then cross if player 2 then nought
                sender.sendToServer(sender.getNickname(), opponent, turnCommand(i, j));
                setChanged();
                notifyObservers();
            }
        } else {
            throw new IllegalArgumentException("Board not empty at (" + i + ", " + j + ")");
        }
        isTurn = false;
    }

    /**
     * Converts a turn taken at cell numbers i, j into a string of the form "i j"
     *
     * @param i row
     * @param j column
     * @return string representation of turn taken
     */
    private String turnCommand(int i, int j) {
        return i + " " + j;
    }

    public void updateWithTurn(int i, int j) {
        if (playerNumber == CROSS) {
            board[i][j] = NOUGHT;
        } else if (playerNumber == NOUGHT) {
            board[i][j] = CROSS;
        } else {
            System.out.println("Something went wrong with the turn");
        }
        setChanged();
        notifyObservers();
    }

    private boolean winner(int player) {
        return
                (board[0][0] == player && board[0][1] == player && board[0][2] == player) ||
                        (board[1][0] == player && board[1][1] == player && board[1][2] == player) ||
                        (board[2][0] == player && board[2][1] == player && board[2][2] == player) ||
                        (board[0][0] == player && board[1][0] == player && board[2][0] == player) ||
                        (board[0][1] == player && board[1][1] == player && board[2][1] == player) ||
                        (board[0][2] == player && board[1][2] == player && board[2][2] == player) ||
                        (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                        (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    /**
     * Determine who (if anyone) has won
     *
     * @return CROSS if cross has won, NOUGHT if nought has won, otherwise BLANK
     */
    public int whoWon() {
        if (winner(CROSS)) {
            return CROSS;
        } else if (winner(NOUGHT)) {
            return NOUGHT;
        } else {
            return BLANK;
        }
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void sendQuitCommand() {
        sender.sendToServer(opponent, sender.getNickname(), "GameEnd");
    }

    public void sendToServer(String recipient, String command) {
        sender.sendToServer("Game", recipient, command);
    }

    public ClientSender getSender() {
        return sender;
    }

    public String getOpponent() {
        return opponent;
    }
}