import java.util.Arrays;

public class Board {

    private int columns;
    private int rows;
    private char[][] board;
    private int nextPlaceInColumn[];

    public void dropDisc(int i_column, char i_pieceshape){

        board[nextPlaceInColumn[i_column]][i_column] = i_pieceshape;
        nextPlaceInColumn[i_column]--;

    }

    public Board (int i_columns, int i_rows){

        nextPlaceInColumn = new int[i_rows];
        Arrays.fill(nextPlaceInColumn,i_rows);
        board = new char[i_rows][i_columns];
        columns = i_columns;
        rows = i_rows;
    }

}
