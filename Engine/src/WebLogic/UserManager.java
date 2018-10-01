package WebLogic;
import GameLogic.GameController;
import GameLogic.GamePlayer;

import java.util.*;
import java.util.stream.Collectors;

public class UserManager {

    private final HashMap<String, User> users;
    private static short idGenerator = 1;
    public UserManager() {
        users = new HashMap<>();
    }

    public synchronized void addUser(String username, boolean isComputer) {
        users.put(username, new User(username, idGenerator++, isComputer));
    }

    public synchronized void removeUser(String username) {
        users.remove(username);
    }

    public synchronized Set<String> getUsers() {
        return Collections.unmodifiableSet(users.keySet());
    }

    public boolean isUserExists(String username) {
        return users.containsKey(username);
    }

    public synchronized User getUser(String username){
        return users.get(username);
    }

    public void JoinUserToTheGame(String userName, String gameTitle) {
        User user = users.get(userName);
        user.setGameRegisteredTo(gameTitle);
    }

    public boolean canUserJoinTheGame(String username) {
        User user = users.get(username);
        String gameRegisteredTo = user.getGameRegisteredTo();
        return gameRegisteredTo.equals("");
    }
}