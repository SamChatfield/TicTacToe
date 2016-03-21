import java.io.BufferedReader;
import java.io.IOException;

/**
 * Thread that handles input from client and packages them into commands, also handles accept events telling the clients to start a game
 *
 * Created by Sam on 03/02/2016.
 */
public class ServerReceiver extends Thread {

    private BufferedReader fromClient;
    private ClientData clientData;

    public ServerReceiver(BufferedReader fromClient, ClientData clientData) {
        this.fromClient = fromClient;
        this.clientData = clientData;
    }

    /**
     * Main run method for the thread, contains infinite loop until client exits. See class doc
     */
    public void run() {
        try {
            while (true) {
                String sender = fromClient.readLine();
                String recipient = fromClient.readLine();
                String command = fromClient.readLine();
                if (sender != null && recipient != null && command != null) {
                    Command cmd = new Command(sender, recipient, command);
                    CommandQueue recipientQueue = clientData.getCommandQueue(recipient);
                    CommandQueue senderQueue = clientData.getCommandQueue(sender);
                    if (recipientQueue != null) {
                        recipientQueue.queue(cmd);
                        System.out.println(cmd); // Print formatted command object toString into server command line
                        if (command.equals("accept")) {
                            Command gameStartChallenger = new Command(sender, recipient, "GameStart 1"); // Opponent, Player, Command
                            Command gameStartRecipient = new Command(recipient, sender, "GameStart 2"); // Opponent, Player, Command
                            recipientQueue.queue(gameStartChallenger);
                            senderQueue.queue(gameStartRecipient);
                        }
                    } else {
                        senderQueue.queue(new Command("Server", sender, "RecipientUserNotFound"));
                    }
                } else {
                    fromClient.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Client disconnected");
        }
    }

}
