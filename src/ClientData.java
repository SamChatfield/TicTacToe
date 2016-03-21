import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/**
 * Data class stores data about connected users
 *
 * Created by Sam on 03/02/2016.
 */
public class ClientData {

    private ArrayList<String> clients = new ArrayList<>();
    private ConcurrentMap<String, CommandQueue> queueTable = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Integer> scoreTable = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Boolean> playingTable = new ConcurrentHashMap<>();
    private ArrayList<Pair<String, String>> inGameList = new ArrayList<>();
    private ArrayList<Pair<String, String>> gameInviteList = new ArrayList<>();

    public void add(String nickname) {
        clients.add(nickname);
        queueTable.put(nickname, new CommandQueue());
        scoreTable.put(nickname, 0);
        playingTable.put(nickname, false);
    }

    public void remove(String nickname) {
        clients.remove(nickname);
        queueTable.remove(nickname);
        scoreTable.remove(nickname);
        playingTable.remove(nickname);
    }

    public void gameRequestSent(Pair<String, String> challengePair) {
        gameInviteList.add(challengePair);
    }

    public void requestAccepted(Pair<String, String> challengePair) {
        removePairFromList(challengePair, gameInviteList);
        startedPlaying(challengePair);
    }

    public void requestDeclined(Pair challengePair) {
        removePairFromList(challengePair, gameInviteList);
    }

    public void startedPlaying(Pair<String, String> challengePair) {
        inGameList.add(challengePair);
        setPlaying(challengePair.a(), true);
        setPlaying(challengePair.b(), true);
    }

    public void stoppedPlaying(Pair<String, String> challengePair) {
        removePairFromList(challengePair, inGameList);
        setPlaying(challengePair.a(), false);
        setPlaying(challengePair.b(), false);
    }

    private void removePairFromList(Pair pair, ArrayList<Pair<String, String>> list) {
        for (Pair e : list) {
            if (e.equals(pair)) {
                list.remove(e);
                break;
            }
        }
    }

    public String assignNickname(String nickname) {
        if (clients.contains(nickname)) {
            // Count number of occurrences of this nickname or renamed version of it using regex
            int i = 0;
            for (String client : clients) {
                if (Pattern.matches(nickname + "_?\\d*", client)) {
                    i++;
                }
            }
            nickname = nickname.concat("_" + Integer.toString(i));
        }
        return nickname;
    }

    public void setScore(String nickname, int newScore) {
        System.out.println("Score for " + nickname + "updated from " + scoreTable.get(nickname) + " to ");
        scoreTable.put(nickname, newScore);
        System.out.print(newScore);
    }

    public void setPlaying(String nickname, boolean playing) {
        playingTable.put(nickname, playing);
        System.out.println(nickname + " playing set to " + playing);
    }

    public CommandQueue getCommandQueue(String nickname) {
        return queueTable.get(nickname);
    }

    public ArrayList<String> getClients() {
        return clients;
    }

    public boolean getPlaying(String nickname) {
        return playingTable.get(nickname);
    }

    public String getScores() {
        String out = "";
        for (String client : clients) {
            out += client + ": " + scoreTable.get(client) + "\n";
        }
        return out;
    }

    public void incrementScore(String nickname) {
        scoreTable.put(nickname, scoreTable.get(nickname) + 1);
    }

}
