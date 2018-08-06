import java.io.Serializable;
import java.util.Arrays;

public class Board implements Serializable {
    private static final char EMPTY_CELL = ' ';
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

        if (i_column < 0 || i_column >= columns) {

            return false;
        }
        if (nextPlaceInColumn[i_column] >= 0) {
            board[nextPlaceInColumn[i_column]][i_column] = i_pieceshape;
            nextPlaceInColumn[i_column]--;

            return true;
        }

        return false;

    }

    public Board (int i_columns, int i_rows){

        nextPlaceInColumn = new int[i_columns];
        Arrays.fill(nextPlaceInColumn,i_rows-1);
        board = new char[i_rows][i_columns];
        columns = i_columns;
        rows = i_rows;
        initializeEmptyBoard();
    }

    private void initializeEmptyBoard()
    {
        for(int i = 0; i < rows; i++)
        {
            for(int j = 0; j < columns; j++)
            {
                board[i][j] = EMPTY_CELL;
            }
        }
    }

    public char getCellSymbol(int row, int col) throws ArrayIndexOutOfBoundsException {

        return board[row][col];
    }

    public void removeDisc(int colToRemoveFrom, int rowToRemoveFrom, char pieceShape)
    {
        //to plan for the future where we will have to remove a disc from the bottom
        //or remove all the player discs, this will know where to remove the disc/s from.

        board[rowToRemoveFrom][colToRemoveFrom] = ' ';
        collapseBoard(colToRemoveFrom, rowToRemoveFrom);
    }

    private void collapseBoard(int colToRemoveFrom, int rowToRemoveFrom)
    {
        for(int i  = rowToRemoveFrom; i > 0; i--)
        {
            board[i][colToRemoveFrom] = board[i-1][colToRemoveFrom];
        }

        board[0][colToRemoveFrom] = EMPTY_CELL;
        updateNextPlaceInCol(colToRemoveFrom);
    }

    private void updateNextPlaceInCol(int colToRemoveFrom)
    {
        for(int i = 0; i < rows; i++)
        {
            if(board[i][colToRemoveFrom] == EMPTY_CELL)
            {
                nextPlaceInColumn[colToRemoveFrom] = i;
            }
        }
    }

}
