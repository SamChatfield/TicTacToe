import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sam on 01/12/2015.
 */
public class BoardView extends JPanel implements Observer {

    private NCModel model;
    private JButton[][] tiles;
    private boolean endGameShown;

    public BoardView(NCModel model) {
        super();
        this.model = model;
        endGameShown = false;

        tiles = new JButton[3][3];
        setLayout(new GridLayout(3, 3));

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int fi = i;
                final int fj = j;
                tiles[i][j] = new JButton();
                tiles[i][j].addActionListener(e -> model.turn(fi, fj));
                add(tiles[i][j]);
                if (!model.isTurn()) {
                    tiles[i][j].setEnabled(false);
                }
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        int opponentNumber;
        String won = "Congratulations, you won!\n" +
                "Press exit to go back to the lobby";
        String lost = "You lost.\n" +
                "Better luck next time. :(";
        if (model.getPlayerNumber() == 1) {
            opponentNumber = 2;
        } else {
            opponentNumber = 1;
        }
        if (model.whoWon() == model.getPlayerNumber() && !endGameShown) {
            JOptionPane.showMessageDialog(this, won);
            model.getNC().sendToServer(model.getNC().getSender().getNickname(), "PlayerDidWin");
            model.setGameFinished(true);
            endGameShown = true;
        } else if (model.whoWon() == opponentNumber && !endGameShown) {
            JOptionPane.showMessageDialog(this, lost);
            model.setGameFinished(true);
            endGameShown = true;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tiles[i][j].setEnabled(true);
                if (model.get(i, j) == NoughtsCrosses.NOUGHT) {
                    tiles[i][j].setText("O");
                    tiles[i][j].setEnabled(false);
                } else if (model.get(i, j) == NoughtsCrosses.CROSS) {
                    tiles[i][j].setText("X");
                    tiles[i][j].setEnabled(false);
                } else if (!model.isTurn()) {
                    tiles[i][j].setEnabled(false);
                } else {
                    tiles[i][j].setText(" ");
                    tiles[i][j].setEnabled(model.whoWon() == NoughtsCrosses.BLANK);
                }
            }
        }
        repaint();
    }
}
