import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main server class that is started when the server is run and when clients connect it allocates new threads for each of them
 *
 * Created by Sam on 02/02/2016.
 */
public class Server {

    /**
     * Main method with functionality described as per class description
     *
     * @param args command line arguments to the server, must be 1 or server will exit arg being the port number
     */
    public static void main(String[] args) {
        ClientData clientData = new ClientData();
        ServerSocket serverSocket = null;
        int port = 0;

        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        } else {
            System.err.println("Incorrect arguments for Server.\nUsage: java Server <port number>");
            System.exit(1);
        }

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Couldn't listen on port " + port);
            System.exit(1);
        }

        try {
            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintStream toClient = new PrintStream(socket.getOutputStream());

                String desiredNickname = fromClient.readLine();
                String nickname = clientData.assignNickname(desiredNickname);
                clientData.add(nickname);
                System.out.println(nickname + " connected");

                ServerReceiver receiver = new ServerReceiver(fromClient, clientData);
                ServerSender sender = new ServerSender(toClient, clientData.getCommandQueue(nickname), clientData, nickname);
                receiver.start();
                sender.start();
            }
        } catch (IOException e) {
            System.err.println("IO Error " + e.getMessage());
        }
    }

}
