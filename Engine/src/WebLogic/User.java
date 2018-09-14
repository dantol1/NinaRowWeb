package WebLogic;

public class User {

    private String name;
    private short id;

    public User(String name, short id, boolean isComputer) {
        this.name = name;
        this.id = id;
        this.isComputer = isComputer;
    }

    public String getName() {
        return name;
    }

    public short getId() {
        return id;
    }

    public boolean isComputer() {
        return isComputer;
    }

    private boolean isComputer;
}
