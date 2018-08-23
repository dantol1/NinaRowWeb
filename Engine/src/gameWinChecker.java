public abstract class gameWinChecker implements Runnable {

    public void setFoundSequence(boolean foundSequence) {
        this.foundSequence = foundSequence;
    }

    private boolean foundSequence = false;
    private int column;

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    private int row;

    public void setColumn(int column) {
        this.column = column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public boolean isFoundSequence() {
        return foundSequence;
    }
}
