import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Client thread for sending input to the server
 *
 * Created by Sam on 03/02/2016.
 */
public class ClientSender extends Thread {

    private final String nickname;
    private final PrintStream toServer;
    private boolean running;

    /**
     * Create new object with nickname and printstream of the server
     *
     * @param nickname name of this client
     * @param toServer printstream of the server
     */
    public ClientSender(String nickname, PrintStream toServer) {
        this.nickname = nickname;
        this.toServer = toServer;
    }

    /**
     * Main run method of this thread loops until client exits or server is stopped
     */
    public void run() {
        running = true;
        BufferedReader user = new BufferedReader(new InputStreamReader(System.in));

        try {
            toServer.println(nickname);
            while (running) {
                // Command recognition and passing on a recipient and a command
                String command = user.readLine();
                if (command.equals("list") || command.equals("scores")) {
                    sendToServer(nickname, nickname, command);
                } else if (command.split("\\s")[0].equals("play") || command.charAt(0) == '?') {
                    String[] c = command.split("\\s");
                    if (!nickname.equals(c[1])) {
                        sendToServer(nickname, c[1], c[0]);
                    } else {
                        System.out.println("Invalid usage of play. Cannot play yourself.\n");
                    }
                } else if ((command.split("\\s")[0].equals("accept")
                        || command.split("\\s")[0].equals("decline"))
                        && command.split("\\s")[1] != null) {
                    String[] c = command.split("\\s");
                    if (!nickname.equals(c[1])) {
                        sendToServer(nickname, c[1], c[0]);
                    } else {
                        System.out.println("Invalid usage of accept/decline. Cannot accept/decline yourself.\n");
                    }
                } else if (command.equals("help")) {
                    System.out.println(listCommands());
                } else if (command.equals("quit")) {
                    sendToServer(nickname, nickname, "UserDidQuit");
                    running = false;
                } else {
                    System.out.println("Command not recognised.\n" + listCommands());
                }
            }
            System.exit(0);
        } catch (IOException e) {
            System.err.println("Communication broke to the server.");
            System.exit(1);
        }
    }

    /**
     * Send strings to server in the order of sender, recipient and command to be constructed into a Command object at the server end
     *
     * @param sender    sender of the command
     * @param recipient recipient of the command
     * @param command   the command itself
     */
    public void sendToServer(String sender, String recipient, String command) {
        toServer.println(sender);
        toServer.println(recipient);
        toServer.println(command);
    }

    /**
     * List the commands available on the server
     * @return string of the commands
     */
    private String listCommands() {
        return "Valid commands:\n" +
                "-list\n" +
                "-scores\n" +
                "-play <user>\n" +
                "-accept <user>\n" +
                "-decline <user>" +
                "-quit\n" +
                "-help\n";
    }

    public String getNickname() {
        return nickname;
    }
}
