import java.util.Observable;

/**
 * Created by Sam on 01/12/2015.
 */
public class NCModel extends Observable {

    private NoughtsCrosses nc;
    private boolean gameFinished;

    public NCModel(NoughtsCrosses nc) {
        super();
        this.nc = nc;
        gameFinished = false;
    }

    public int get(int i, int j) {
        return nc.get(i, j);
    }

    public boolean isTurn() {
        return nc.isTurn();
    }

    public void turn(int i, int j) {
        nc.turn(i, j);
        setChanged();
        notifyObservers();
    }

    public int whoWon() {
        return nc.whoWon();
    }

    public int getPlayerNumber() {
        return nc.getPlayerNumber();
    }

    public NoughtsCrosses getNC() {
        return nc;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public void setGameFinished(boolean gameFinished) {
        this.gameFinished = gameFinished;
    }

    // No longer needed as new game button is disabled
//    public void newGame() {
//        nc.newGame();
//        setChanged();
//        notifyObservers();
//    }

}
