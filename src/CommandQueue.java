import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Contains a queue of commands for a specific user for the server thread in charge of that user to handle in turn
 *
 * Created by Sam on 03/02/2016.
 */
public class CommandQueue {

    private BlockingQueue<Command> queue = new LinkedBlockingQueue<>();

    /**
     * Add command to queue
     *
     * @param c command to add
     */
    public void queue(Command c) {
        queue.offer(c);
    }

    /**
     * Remove command from queue and return it
     * @return return the command that has been dequeued
     */
    public Command dequeue() {
        while (true) {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                // Do nothing since there are no calls to interrupt() in the code
            }
        }
    }

}
