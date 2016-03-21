import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Class which is a thread of a sender to a specific client
 *
 * Created by Sam on 03/02/2016.
 */
public class ServerSender extends Thread {

    private PrintStream toClient;
    private CommandQueue queue;
    private ClientData data;
    private String clientName;

    /**
     * Create new ServerSender thread for a new connected client
     *
     * @param toClient   print stream to client
     * @param queue      queue of commands directed to this client
     * @param data       overall client data
     * @param clientName name of this client
     */
    public ServerSender(PrintStream toClient, CommandQueue queue, ClientData data, String clientName) {
        this.toClient = toClient;
        this.queue = queue;
        this.data = data;
        this.clientName = clientName;
    }

    /**
     * Main run method loop for the sever sender thread
     */
    public void run() {
        BufferedReader server = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            Command c = queue.dequeue();
            String com = c.getCommand();
            if (com.matches("\\d\\s\\d")) {
                System.out.println("Board change received");
                toClient.println(com);
            } else {
                switch (com) {
                    case "list":
                        toClient.println(data.getClients() + "\n");
                        break;
                    case "scores":
                        toClient.println(data.getScores());
                        break;
                    case "play":
                        if (!data.getPlaying(clientName)) {
                            toClient.println("Play request received from " + c.getSender() +
                                    ".\nUse 'accept/decline " + c.getSender() +
                                    "' to accept or decline the request.");
                            data.gameRequestSent(new Pair<>(c.getSender(), c.getRecipient()));
                        } else {
                            toClient.println(c.getRecipient() + " is already in a game.");
                        }
                        break;
                    case "accept":
                        data.requestAccepted(new Pair<>(c.getRecipient(), c.getSender()));
                        toClient.println("Request accepted\n");

                        break;
                    case "decline":
                        data.requestDeclined(new Pair<>(c.getRecipient(), c.getSender()));
                        toClient.println("Request declined\n");
                        break;
                    case "GameStart 1":
                        toClient.println("GameStart 1");
                        toClient.println(c.getSender());
                        break;
                    case "GameStart 2":
                        toClient.println("GameStart 2");
                        toClient.println(c.getSender());
                        break;
                    case "GameEnd":
                        toClient.println("GameEnd");
                        data.setPlaying(clientName, false);
                        break;
                    case "StartTurn":
                        toClient.println("StartTurn");
                    case "UserDidQuit":
                        data.remove(clientName);
                        break;
                    case "PlayerDidWin":
                        data.incrementScore(clientName);
                    case "RecipientUserNotFound":
                        toClient.println("The specified user is not connected to the server.\n");
                        break;
                }
            }
        }
    }

}
