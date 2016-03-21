import javax.swing.*;

/**
 * Created by Sam on 01/12/2015.
 */
public class ButtonPanel extends JPanel {

    private NCModel model;

    public ButtonPanel(NCModel model) {
        super();
        this.model = model;

        JButton exit = new JButton("Exit");

        exit.addActionListener(e -> quitPressed());

        add(exit);
    }

    private void quitPressed() {
        if (!model.isGameFinished()) {
            model.getNC().sendToServer(model.getNC().getOpponent(), "PlayerDidWin");
            model.setGameFinished(true);
        }
        model.getNC().sendQuitCommand();
    }

}
