import java.util.Arrays;

public class Board {

    private int columns;
    private int rows;

    public char[][] getBoard() {
        return board;
    }

    private char[][] board;

    public int[] getNextPlaceInColumn() {
        return nextPlaceInColumn;
    }

    private int nextPlaceInColumn[];

    public boolean dropDisc(int i_column, char i_pieceshape){

        if (nextPlaceInColumn[i_column] != 0) {
            board[nextPlaceInColumn[i_column]][i_column] = i_pieceshape;
            nextPlaceInColumn[i_column]--;

            return true;
        }

        return false;

    }

    public Board (int i_columns, int i_rows){

        nextPlaceInColumn = new int[i_rows];
        Arrays.fill(nextPlaceInColumn,i_rows);
        board = new char[i_rows][i_columns];
        columns = i_columns;
        rows = i_rows;
    }

    public char getCellSymbol(int row, int col) throws ArrayIndexOutOfBoundsException {

        return board[row][col];
    }

}
