/**
 * Wrapper for 3 strings that make up a command; a sender, a recipient and a command itself
 *
 * Created by Sam on 03/02/2016.
 */
public class Command {

    private final String sender, recipient, command;

    /**
     * New command with specified sender recipient and command
     *
     * @param sender
     * @param recipient
     * @param command
     */
    public Command(String sender, String recipient, String command) {
        this.sender = sender;
        this.recipient = recipient;
        this.command = command;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getCommand() {
        return command;
    }

    public String toString() {
        return sender + " -> " + command + " -> " + recipient;
    }

}
