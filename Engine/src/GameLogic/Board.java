package GameLogic;

import java.io.Serializable;
import java.util.Arrays;

public class Board implements Serializable {
    public static final char EMPTY_CELL = ' ';
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

    public DropDiscComplex dropDisc(int i_column, char i_pieceshape){


        if (i_column < 0 || i_column >= columns) {

            return new DropDiscComplex(false,0,0);
        }
        if (nextPlaceInColumn[i_column] >= 0) {
           DropDiscComplex complex = new DropDiscComplex(true,i_column,nextPlaceInColumn[i_column]);
            board[nextPlaceInColumn[i_column]][i_column] = i_pieceshape;
            nextPlaceInColumn[i_column]--;

            return complex;
        }

        return new DropDiscComplex(false,0,0);

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

    public PopoutMoveComplex popoutDisc(int column) {

        PopoutMoveComplex result = null;

        char pieceshape = '`';
        boolean popoutSucceeded = false;

        if(nextPlaceInColumn[column] < rows -1)
        {
            pieceshape = board[rows-1][column];
            collapseBoard(column, rows - 1);
            popoutSucceeded = true;
        }

        result = new PopoutMoveComplex(pieceshape,popoutSucceeded);
        return result;

    }

    public void collapseSpaces() {
        for(int column = 0; column < columns; column++)
        {
            for(int row = rows - 2; row >= 0; row--)
            {
                int currRow = row;
                while(currRow + 1 < rows && board[currRow + 1][column] == EMPTY_CELL)
                {
                    board[currRow + 1][column] = board[currRow][column];
                    board[currRow][column] = EMPTY_CELL;
                    currRow++;
                }
            }
        }

        updateNextPlacesInColumns();
    }

    private void updateNextPlacesInColumns()
    {
        for(int column = 0; column < columns; column++)
        {
            for(int row = 0; row < rows; row++)
            {
                if(board[row][column]== Board.EMPTY_CELL)
                {
                    nextPlaceInColumn[column] = row;
                }
            }
        }
    }

    public void insertDiscAtBottom(int columnToAddTo, char pieceShape) {
        bumpUpDiscs(columnToAddTo);
        board[rows-1][columnToAddTo] = pieceShape;
    }

    private void bumpUpDiscs(int columnToAddTo) {
        for(int i = 1; i < rows; i++)
        {
            board[i-1][columnToAddTo] = board[i][columnToAddTo];
        }
    }
}
