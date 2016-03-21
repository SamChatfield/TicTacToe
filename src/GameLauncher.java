import javax.swing.*;

/**
 * Created by Sam on 06/02/2016.
 */
public class GameLauncher extends Thread {

    private JFrame frame;
    private NoughtsCrosses nc;

    public GameLauncher(String clientName, int playerNumber, String opponent, ClientSender sender) {
        this.frame = new JFrame("TicTacToe - " + clientName);
        // if player presses the close button it will dispose of the frame on close
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 500);
        nc = new NoughtsCrosses(playerNumber, opponent, sender);
    }

    public void run() {
        NCComponent ncComponent = new NCComponent(nc);

        frame.add(ncComponent);
        frame.setVisible(true);
    }

    public void terminate() {
        frame.dispose();
    }

    public NoughtsCrosses getNoughtsCrosses() {
        return nc;
    }

}
