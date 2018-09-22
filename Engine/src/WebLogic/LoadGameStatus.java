package WebLogic;

public class LoadGameStatus {
     boolean isLoaded;
     String errorMessage;
     int testt;

    public LoadGameStatus(boolean isLoaded, String message) {
        this.isLoaded = isLoaded;
        this.errorMessage = message;
    }

    public boolean isLoaded() {
        return this.isLoaded;
    }

    public void setLoaded(boolean loaded) {
        this.isLoaded = loaded;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
