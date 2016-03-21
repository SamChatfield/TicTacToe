import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Main client class which connects to the server and creates threads to talk to it
 *
 * Created by Sam on 02/02/2016.
 */
public class Client {

    private final String machineName;
    private final int port;
    private String nickname;
    private Socket server;
    private PrintStream toServer;
    private BufferedReader fromServer;

    /**
     * Create new client with nickname, port number and machine name
     *
     * @param nickname
     * @param port
     * @param machineName
     */
    public Client(String nickname, int port, String machineName) {
        this.nickname = nickname;
        this.machineName = machineName;
        this.port = port;
        run();
    }

    /**
     * Main method entered into when Client is run
     * @param args command line args nickname, port and machine
     */
    public static void main(String[] args) {
        if (args.length == 3) {
            Client client = new Client(args[0], Integer.parseInt(args[1]), args[2]);
            client.run();
        } else {
            System.err.println("Incorrect arguments for Client.\nUsage: java Client <nickname> <port number> <machine name>");
            System.exit(1);
        }
    }

    /**
     * Main run method (not a thread) which starts the threads and runs until client decides to exit
     */
    public void run() {
        try {
            server = new Socket(machineName, port);
            toServer = new PrintStream(server.getOutputStream());
            fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
        } catch (IOException e) {
            System.err.println("The server isn't running.\n" + e.getMessage());
            System.exit(1);
        }

        ClientSender sender = new ClientSender(nickname, toServer);
        ClientReceiver receiver = new ClientReceiver(fromServer, sender, nickname);

        sender.start();
        receiver.start();

        try {
            sender.join();
            receiver.join();
            toServer.close();
            fromServer.close();
            server.close();
        } catch (InterruptedException e) {
            System.err.println("Interruption, closing.\n" + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.err.println("IO Exception.\n" + e.getMessage());
        }
    }

}
