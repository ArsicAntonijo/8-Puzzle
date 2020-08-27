/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

//import Board.IteratorNeighbor;

//import Board.IteratorNeighbor;
package puzzle;

import edu.princeton.cs.algs4.Stack;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class Board {
    private int [][] board;
    private Stack<Board> stack = new Stack<Board>();
    private boolean goal;
    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        board = copy(tiles);
    }


    // string representation of this board
    public String toString() {
        String str = "";
        str += board.length + "\n";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[1].length; j++) {
                str += board[i][j] + " ";
            }
            str += "\n";
        }
        return str;
    }

    // board dimension n
    public int dimension() {
        return board.length;
    }

    // number of tiles out of place
    public int hamming() {
        // return CountEmAll(2, null);
        int count2 = 0;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != 0 && ((i * board.length + j +1) != board[i][j])) count2++;
            }
        }
        if (count2 == 0) goal = true;
        else goal = false;
        return count2;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        // return CountEmAll(1, null);

        int count1 = 0;// help, check;
        int boardPosX;
        int boardPosY;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != 0) {
                    boardPosX = 1 + (board[i][j] - 1) / board.length;
                    boardPosY = board[i][j] - board.length * (boardPosX - 1);
                    count1 += Math.abs(boardPosX - i - 1) + Math.abs(boardPosY - j - 1);
                }
            }
        }
        if (count1 == 0) goal = true;
        else goal = false;
        return count1;
    }

    // is this board the goal board?
    public boolean isGoal() {
        /*
        if (hamming() == 0)
            return true;
        else
            return false;

         */
       // return goal;
        return (CountEmAll(2, null) == 0);
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != this.getClass())
            return false;

        Board b = (Board) y;
        return board.length == b.board.length && (Arrays.deepEquals(((Board) y).board, board));
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new IterableNeighbor();
    }
    private class IterableNeighbor implements Iterable<Board> {
        public Iterator<Board> iterator() {
            return new IteratorNeighbor();
        }
    }
    private class IteratorNeighbor implements Iterator<Board> {
        private Board tmp;
        private int count, total, pointer;
        private int[] position;
        private int position1 = CountEmAll(4,null);
        private int i = position1 / 10;
        private int j = position1 % 10;

        public IteratorNeighbor() {
            // putting neighboor boards in stack


            // int[][] neighboarBoard;
            // Board tmpBoard;

            count = 0;
            position = new int[4];
            total = 0;
            pointer = 0;
            if (i == board.length - 1) position[0] = 0;
            else {
                position[0] = 1;
                total++;
            }
            if (i == 0) position[1] = 0;
            else {
                position[1] = 1;
                total++;
            }
            if (j == board.length - 1) position[2] = 0;
            else {
                position[2] = 1;
                total++;
            }
            if (j == 0) position[3] = 0;
            else {
                position[3] = 1;
                total++;
            }

        }

        @Override
        public boolean hasNext() {
            return count < total;
        }

        @Override
        public Board next() {
            int swap;
            while (position[pointer] == 0 && pointer < 4) pointer++;
            if (pointer == 0) {
                // Board x = new Board(makeCopy(board, posX, posY, posX + 1, posY));
                Board x = new Board(copy(board));
                swap = x.board[i][j];
                x.board[i][j] = x.board[i + 1][j];
                x.board[i + 1][j] = swap;
                count++;
                pointer++;
                return x;
            }

            if (pointer == 1) {
                // Board x = new Board(makeCopy(board, posX, posY, posX - 1, posY));
                Board x = new Board(copy(board));
                swap = x.board[i][j];
                x.board[i][j] = x.board[i - 1][j];
                x.board[i - 1][j] = swap;
                count++;
                pointer++;
                return x;
            }
            if (pointer == 2) {
                // Board x = new Board(makeCopy(board, posX, posY, posX, posY + 1));
                Board x = new Board(copy(board));
                swap = x.board[i][j];
                x.board[i][j] = x.board[i][j + 1];
                x.board[i][j + 1] = swap;
                count++;
                pointer++;
                return x;
            }
            if (pointer == 3) {
                // Board x = new Board(makeCopy(board, posX, posY, posX, posY - 1));
                Board x = new Board(copy(board));
                swap = x.board[i][j];
                x.board[i][j] = x.board[i][j - 1];
                x.board[i][j - 1] = swap;
                count++;
                pointer++;
                return x;
            }


            return null;
        }
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board tmpboard = new Board(board);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length - 1; j++) {
                if (board[i][j] != 0 && board[i][j + 1] != 0) {
                    tmpboard.swap(i, j, i, j + 1);
                    return tmpboard;
                }
            }
        }

        return tmpboard;
    }
    private boolean swap(int i, int j, int it, int jt) {
        if (it < 0 || it >= board.length || jt < 0 || jt >= board.length) {
            return false;
        }
        int temp = board[i][j];
        board[i][j] = board[it][jt];
        board[it][jt] = temp;
        return true;
    }

    private int[][] copy(int[][] src) {

        int m = src[1].length;
        int[][] y = new int[m][m];
        for (int i = 0; i < m; i++) {
            System.arraycopy(src[i], 0, y[i], 0, m);
        }
        return y;
    }


    private int CountEmAll(int mode, int[][] array) {
        int count2 = 0;
        int count1 = 0;// help, check;
        int count3 = 1;
        int count4;
        int boardPosX;
        int boardPosY;
        //int diference, move1, move2;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != 0 && ((i * board.length + j +1) != board[i][j])) count2++;

                if (mode == 1) {
                    if (board[i][j] == 0) continue;
                    else {
                        boardPosX = 1 + (board[i][j] - 1) / board.length;
                        boardPosY = board[i][j] - board.length * (boardPosX - 1);
                        count1 += Math.abs(boardPosX - i - 1) + Math.abs(boardPosY - j - 1);
                    }
                }

                if (mode == 3 && board[i][j] != array[i][j]) count3 = 0;
                if (mode == 4 && board[i][j] == 0) return i * 10 + j;
            }
        }
        if (mode == 1) return count1;
        if (mode == 2) return count2;
        if (mode == 3) return count3;
        return 0;
    }

    public int getInt(int x, int y) {
        return board[x][y];
    }



    // unit testing (not graded)
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[][] array = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                array[i][j] = scanner.nextInt();
            }
        }
        Board b = new Board(array);
        System.out.println(b.dimension());
        System.out.println("h: " + b.hamming());
        System.out.println("manhatan: " + b.manhattan());
        System.out.println("is goal:  " + b.isGoal());
        int[][] array2 = {{0,2},{3,1}};
        //array2[0][0] = 0;
        Board b2 = new Board(array2);
        System.out.println("equals: " + b.equals(b2));
        System.out.println(b);
        System.out.println(b.twin());
        Iterable<Board> iterable = b.neighbors();
        for (Board value : iterable) {
            System.out.println(value);
        }
    }

}
// https://github.com/C3rooks/8-Puzzle-/tree/master/src
// https://www.cs.princeton.edu/courses/archive/spring18/cos226/meetings/cm8-8puzzle.pdf