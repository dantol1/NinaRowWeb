package WebLogic;

import GameLogic.GamePlayer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserManager {

    private final Map<String, User> users;

    public UserManager() {
        users = new HashMap<>();
    }

    public void addUser(String username, boolean isComputer) {

        users.put(username, new User(username, idGenerator++, isComputer));
    }

    private static short idGenerator = 1;

    public void removeUser(String username) {
        users.remove(username);
    }

    public List<User> getUsers() {
        return users.values().stream().collect(Collectors.toList());
    }

    public boolean isUserExists(String username) {
        return users.containsKey(username);
    }

    public User getUser(String username){
        return users.get(username);
    }
}

