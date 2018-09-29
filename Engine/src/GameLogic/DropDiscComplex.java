package GameLogic;

public class DropDiscComplex {


    private boolean dropSucceded;
    private int column;
    private int row;

    DropDiscComplex(boolean isSucceeded, int col, int row) {

        this.dropSucceded = isSucceeded;
        this.column = col;
        this.row = row;
    }

    public boolean isDropSucceded() {
        return dropSucceded;
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
