package puzzle;

import edu.princeton.cs.algs4.Queue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import javax.swing.JPanel;


public class TryYourself extends JPanel { // our grid will be drawn in a dedicated Panel

    // Size of our Game of Fifteen instance
    private int size;
    // Number of tiles
    private int nbTiles;
    // Grid UI Dimension
    private int dimension;
    // Foreground Color
    private static final Color FOREGROUND_COLOR = new Color(154, 239, 191); // we use arbitrary color
    // Random object to shuffle tiles
    private static final Random RANDOM = new Random();
    // Storing the tiles in a 1D Array of integers
    private int[] tiles;
    // Size of tile on UI
    private int tileSize;
    // Position of the blank tile
    private int blankPos;
    // Margin for the grid on the frame
    private int margin;
    // Grid UI Size
    private int gridSize;
    private boolean gameOver; // true if game over, false otherwise
    private Queue<Integer> queue = new Queue<>(); // solution for hint
    private boolean getHint = false;

    public TryYourself(int size, int dim, int mar, PuzzleGUI parent) {
        this.size = size;
        dimension = dim;
        margin = mar;
        

        // init tiles
        nbTiles = size * size - 1; // -1 because we don't count blank tile
        tiles = new int[size * size];

        // calculate grid size and tile size
        gridSize = (dim - 2 * margin);
        tileSize = gridSize / size;

        setPreferredSize(new Dimension(dimension, dimension + margin));
        setBackground(Color.WHITE);
        setForeground(FOREGROUND_COLOR);
        setFont(new Font("SansSerif", Font.BOLD, 60));

        gameOver = true;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // used to let users to interact on the grid by clicking
                // it's time to implement interaction with users to move tiles to solve the game !
                if (gameOver) {
                    newGame();
                } else {
                    // get position of the click
                    int ex = e.getX() - margin;
                    int ey = e.getY() - margin;

                    // click in the grid ?
                    if (ex < 0 || ex > gridSize  || ey < 0  || ey > gridSize)
                        return;

                    // get position in the grid
                    int c1 = ex / tileSize;
                    int r1 = ey / tileSize;

                    // get position of the blank cell
                    int c2 = blankPos % size;
                    int r2 = blankPos / size;

                    // we convert in the 1D coord
                    int clickPos = r1 * size + c1;

                    int dir = 0;

                    // we search direction for multiple tile moves at once
                    if (c1 == c2  &&  Math.abs(r1 - r2) > 0)
                        dir = (r1 - r2) > 0 ? size : -size;
                    else if (r1 == r2 && Math.abs(c1 - c2) > 0)
                        dir = (c1 - c2) > 0 ? 1 : -1;

                    if (dir != 0) {
                        // we move tiles in the direction
                        do {
                            int newBlankPos = blankPos + dir;
                            tiles[blankPos] = tiles[newBlankPos];
                            blankPos = newBlankPos;
                        } while(blankPos != clickPos);

                        tiles[blankPos] = 0;
                    }

                    // we check if game is solved
                    gameOver = isSolved();
                }

                // we repaint panel
                repaint();
            }
        });

        parent.button0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                solve();
                getHint = true;
                repaint();
            }
        });

        parent.button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                parent.frame.setVisible(false);
                new StartGUI();
                // System.out.println(3);
            }
        });

        newGame();
    }

    private void newGame() {
        do {
            reset(); // reset in intial state
            shuffle(); // shuffle
        } while(!isSolvable()); // make it until grid be solvable

        gameOver = false;
    }

    private void reset() {
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = (i + 1) % tiles.length;
        }

        // we set blank cell at the last
        blankPos = tiles.length - 1;
    }

    private void shuffle() {
        // don't include the blank tile in the shuffle, leave in the solved position
        int n = nbTiles;

        while (n > 1) {
            int r = RANDOM.nextInt(n--);
            int tmp = tiles[r];
            tiles[r] = tiles[n];
            tiles[n] = tmp;
        }
    }

    private void solve() {
        // here we create matrix for puzzle, creating puzzle and solving it :D
        int[][] arr = new int[size][size];
        for (int i = 0; i < tiles.length; i++) {
            // we convert 1D coords to 2D coords given the size of the 2D Array
            int r = i / size;
            int c = i % size;
            arr[r][c] = tiles[i];
        }

        // int m = manhattan(arr);
        // System.out.println(m);
        Solver solver = new Solver(new Board(arr));

        if (solver.isSolvable() ) {
            int z = 0, newPos = 0;
            for (Board board : solver.solution()) {
                if (z < 4 && z > 0) {
                    for (int i = 0; i < size; i++)
                        for (int j = 0; j < size; j++){
                            if (board.getInt(i,j) == 0) {
                                newPos = i * size + j;
                            }
                        }
                    queue.enqueue(newPos);
                }
                z++;
            }
        }
    }

    // Only half permutations o the puzzle are solvable
    // Whenever a tile is preceded by a tile with higher value it counts
    // as an inversion. In our case, with the blank tile in the solved position,
    // the number of inversions must be even for the puzzle to be solvable
    private boolean isSolvable() {
        int countInversions = 0;

        for (int i = 0; i < nbTiles; i++) {
            for (int j = 0; j < i; j++) {
                if (tiles[j] > tiles[i])
                    countInversions++;
            }
        }

        return countInversions % 2 == 0;
    }

    private boolean isSolved() {
        if (tiles[tiles.length - 1] != 0) // if blank tile is not in the solved position ==> not solved
            return false;

        for (int i = nbTiles - 1; i >= 0; i--) {
            if (tiles[i] != i + 1)
                return false;
        }

        return true;
    }

    private void drawGrid(Graphics2D g) {
        for (int i = 0; i < tiles.length; i++) {
            // we convert 1D coords to 2D coords given the size of the 2D Array
            int r = i / size;
            int c = i % size;
            // we convert in coords on the UI
            int x = margin + c * tileSize;
            int y = margin + r * tileSize;

            // check special case for blank tile
            if(tiles[i] == 0) {
                if (gameOver) {
                    // g.setColor(FOREGROUND_COLOR);
                    g.setColor(new Color(239, 39, 9));
                    drawCenteredString(g, "\u2713", x, y);
                }

                continue;
            }

            // for other tiles
            g.setColor(getForeground());
            g.fillRoundRect(x, y, tileSize, tileSize, 25, 25);
            g.setColor(Color.BLACK);
            g.drawRoundRect(x, y, tileSize, tileSize, 25, 25);
            g.setColor(Color.WHITE);

            drawCenteredString(g, String.valueOf(tiles[i]), x , y);
        }
    }

    private void drawStartMessage(Graphics2D g) {
        Color TEMP_COLOR = new Color(239, 39, 9);
        if (gameOver) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(TEMP_COLOR);
            String s = "Click to start new game";
            g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2,
                    getHeight() - margin);
        }
        if (getHint && !gameOver) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(TEMP_COLOR);
            String s = "";
            while (!queue.isEmpty()){
                s += tiles[queue.dequeue()] + " -> ";
            }
            s += "...";
            g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2,
                    getHeight() - margin);
            getHint = false;
        }
    }

    private void drawCenteredString(Graphics2D g, String s, int x, int y) {
        // center string s for the given tile (x,y)
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int desc = fm.getDescent();
        g.drawString(s,  x + (tileSize - fm.stringWidth(s)) / 2,
                y + (asc + (tileSize - (asc + desc)) / 2));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g2D);
        drawStartMessage(g2D);
    }
}
