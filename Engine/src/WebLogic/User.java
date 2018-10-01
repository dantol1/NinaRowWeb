package WebLogic;

public class User {

    private String name;
    private short id;
    private String gameRegisteredTo;
    private boolean isComputer;

    public User(String name, short id, boolean isComputer) {
        this.name = name;
        this.id = id;
        this.isComputer = isComputer;
        this.gameRegisteredTo = "";
    }

    public String getGameRegisteredTo(){
        return this.gameRegisteredTo;
    }
    public void setGameRegisteredTo(String gameName) {
        this.gameRegisteredTo = gameName;
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


}
