package Exceptions;

public class NoMovesMadeException extends Exception {
    String errorMessage = "No moves have been made yet";

    @Override
    public String toString()
    {
        return errorMessage;
    }
}
