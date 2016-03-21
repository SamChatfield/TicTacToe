import javax.swing.*;
import java.awt.*;

/**
 * Created by Sam on 01/12/2015.
 */
public class NCComponent extends JPanel {

    public NCComponent(NoughtsCrosses nc) {
        super();

        NCModel model = new NCModel(nc);
        BoardView boardView = new BoardView(model);
        ButtonPanel buttonPanel = new ButtonPanel(model);

        model.addObserver(boardView);
        nc.addObserver(boardView);

        setLayout(new BorderLayout());
        add(boardView, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

}
