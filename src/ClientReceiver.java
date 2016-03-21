import java.io.BufferedReader;
import java.io.IOException;

/**
 * Client thread to receive data from the server
 *
 * Created by Sam on 03/02/2016.
 */
public class ClientReceiver extends Thread {

    private BufferedReader fromServer;
    private ClientSender sender;
    private String clientName;

    /**
     * create new object with reader from the server, client sender to send back to server and a client name
     *
     * @param fromServer buffered reader of the server
     * @param sender     sender to send to the server
     * @param clientName name of the client
     */
    public ClientReceiver(BufferedReader fromServer, ClientSender sender, String clientName) {
        this.fromServer = fromServer;
        this.sender = sender;
        this.clientName = clientName;
    }

    /**
     * Main run method of the thread, runs until communication to server ceases or client exits
     */
    public void run() {
        GameLauncher game = null;
        try {
            while (true) {
                String message = fromServer.readLine();
                if (message != null) {
                    if (message.matches("\\d\\s\\d")) {
                        int i = Integer.parseInt(message.split("\\s")[0]);
                        int j = Integer.parseInt(message.split("\\s")[1]);
                        if (game != null) {
                            game.getNoughtsCrosses().setTurn(true);
                            game.getNoughtsCrosses().updateWithTurn(i, j);
                        }
                    } else {
                        switch (message) {
                            case "GameStart 1":
                                String player2 = fromServer.readLine();
                                game = new GameLauncher(clientName, 1, player2, sender);
                                game.start();
                                game.getNoughtsCrosses().setTurn(true);
                                break;
                            case "GameStart 2":
                                String player1 = fromServer.readLine();
                                game = new GameLauncher(clientName, 2, player1, sender);
                                game.start();
                                break;
                            case "StartTurn":
                                if (game != null) {
                                    game.getNoughtsCrosses().setTurn(true);
                                }
                                break;
                            case "GameEnd":
                                if (game != null) {
                                    game.terminate();
                                    game.interrupt();
                                }
                                break;
                            default:
                                System.out.println(message);
                                break;
                        }
                    }
                } else {
                    fromServer.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Something went wrong in ClientReceiver.\n" + e.getMessage());
        }
    }

    public ClientSender getSender() {
        return sender;
    }
}
