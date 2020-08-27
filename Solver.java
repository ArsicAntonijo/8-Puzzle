/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
package puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;


public class Solver {
    private int move = 0;
    private Stack<Board> gameTree;
    private boolean solveable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        gameTree = new Stack<Board>();
        if (initial.isGoal()) {
            solveable = true;
            gameTree.push(initial);
            return;
        }
        if(initial.twin().isGoal()) {
            solveable = false;
            return;
        }

        BoardObject tmp;
        Board board = initial;
        BoardObject tmpRoot = new BoardObject(board, 0, null);
        MinPQ<BoardObject> priorityQueue = new MinPQ<BoardObject>();
        priorityQueue.insert(tmpRoot);

        BoardObject tmpTwin;
        Board boardTwin = board.twin();
        BoardObject tmpRootTwin = new BoardObject(boardTwin, 0, null);
        MinPQ<BoardObject> priorityQueueTwin = new MinPQ<BoardObject>();
        priorityQueueTwin.insert(tmpRootTwin);
        int mr = 0;
        while (mr < 950000) {
            tmpRoot = priorityQueue.delMin();
            tmpRootTwin = priorityQueueTwin.delMin();

            if (tmpRoot.isGoal()) {
                solveable = true;
                gameTree.push(tmpRoot.board);
                while (tmpRoot.previous != null) {
                    tmpRoot = tmpRoot.previous;
                    gameTree.push(tmpRoot.board);
                    move++;
                }
                break;
            }
            if(tmpRootTwin.isGoal()) {
                solveable = false;
                break;
            }
            mr++;
            tmpRoot.moves++;
            tmpRootTwin.moves++;
            for (Board obj : tmpRoot.neighbors()) {
                tmp = new BoardObject(obj, tmpRoot.moves, tmpRoot);
                if (tmpRoot.previous != null) {
                    if (!tmp.equals(tmpRoot.previous)) {
                        // ovde da se dodeli tmp , mozda
                        priorityQueue.insert(tmp);
                    }
                } else { priorityQueue.insert(tmp); } }
            for (Board obj : tmpRootTwin.neighbors()) {
                tmpTwin = new BoardObject(obj, tmpRootTwin.moves, tmpRootTwin);
                if (tmpRootTwin.previous != null) {
                    if (!tmpTwin.equals(tmpRootTwin.previous)) {
                        priorityQueueTwin.insert(tmpTwin);
                    }
                } else {
                    priorityQueueTwin.insert(tmpTwin);
                } } }
        if (mr >= 950000) solveable = false;
    }

    private class BoardObject implements Comparable<BoardObject> {
        private Board board;
        private BoardObject previous;
        private int moves;
        private int cachedPriority = -3;

        public BoardObject(Board b, int mv, BoardObject that) {
            board = b;
            moves = mv;
            previous = that;
        }
        public boolean equals(BoardObject y) {
            return board.equals(y.board);
        }
        public boolean isGoal() {
            return board.isGoal();
        }
        public Iterable<Board> neighbors() {
            return board.neighbors();
        }
        private int manhattanPriority()
        {
            if (cachedPriority == -3) {
                cachedPriority = board.manhattan() + moves;
            }
            return cachedPriority;
        }
        public Board getBoard() {
            return board;
        }
        @Override
        public int compareTo(BoardObject that) {
            return Integer.compare(this.manhattanPriority(), that.manhattanPriority());
        }

        @Override
        public String toString() {
            return board.toString();
        }
    }

    /*
        private int manhattanPriority(Board b, int move) {
            return b.manhattan() + move;
        }

        class BoardCompare implements Comparator<Board>
        {
            // Used for sorting in ascending order of
            // roll number
            public int compare(Board a, Board b)
            {
                return Integer.compare(a.manhattan() + move, b.manhattan() + move);
            }
        }
    */
    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        // return move < 50;
        return solveable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return  move;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (!solveable)
            return null;
        else
            return gameTree;
    }
/*
    private class IterableSolution implements Iterable<Board> {
        @Override
        public Iterator<Board> iterator() {
            return new IteratorSolution();
        }
        private class IteratorSolution implements Iterator<Board> {

            @Override
            public boolean hasNext() {
                return !(gameTree.isEmpty());
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public Board next() {
                if (gameTree.isEmpty()) throw new NoSuchElementException();
                else {
                    BoardObject tmp = gameTree.pop();
                    return tmp.getBoard();
                }
            }
        }
    }
*/
    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }


}