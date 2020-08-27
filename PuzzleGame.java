package puzzle;

import edu.princeton.cs.algs4.Queue;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.*;

// We are going to create a Game of 15 Puzzle with Java 8 and Swing
// If you have some questions, feel free to ue comments ;)
public class PuzzleGame extends JPanel { // our grid will be drawn in a dedicated Panel

    // Size of our Game of Fifteen instance
    private int size;
    // Number of tiles
    private int nbTiles;
    // Grid UI Dimension
    private int dimension;
    // Foreground Color
    private static final Color FOREGROUND_COLOR = new Color(45, 239, 19); // we use arbitrary color
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
    // solution
    private Queue<Board> queue;
   private final boolean[] start = {false};
   private boolean slv = true;
   private PuzzleGUI parent;
   private int[][] niz;


    public PuzzleGame(int size, int dim, int mar, PuzzleGUI parent) {
        this.parent = parent;
        this.size = size;
        dimension = dim;
        margin = mar;
        doIt();

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
        newGame();
        solve();


        // final boolean[] start = {false};
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                start[0] = true;
            }
        });

        parent.button0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                newGame();
                solve();
                start[0] = false;
                repaint();
                // System.out.println(2);
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

        ActionListener actListner = new ActionListener() {
            @Override

            public void actionPerformed(ActionEvent event) {

                if (!queue.isEmpty() && start[0]) {
                    Board board = queue.dequeue();
                    int newPos = 0;
                    for (int i = 0; i < size; i++)
                        for (int j = 0; j < size; j++){
                            if (board.getInt(i,j) == 0) {
                                newPos = i * size + j;
                            }
                        }
                    int g = tiles[blankPos];
                    tiles[blankPos] = tiles[newPos];
                    tiles[newPos] = g;

                    blankPos = newPos;
                    repaint();
                }
                if(queue.isEmpty())
                     gameOver = true;
            }
        };

        Timer timer = new Timer(1000, actListner);

        timer.start();


    }

    private int manhattan() {
        int count1 = 0;
        int boardPosX;
        int boardPosY;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int x = i * size + j;
                if (tiles[x] != 0) {
                    boardPosX = 1 + (tiles[x] - 1) / size;
                    boardPosY = tiles[x]- size * (boardPosX - 1);
                    count1 += Math.abs(boardPosX - i - 1) + Math.abs(boardPosY - j - 1);
                }
            }
        }
        return count1;
    }

    private void solve() {
        // here we create matrix for puzzle, creating puzzle and solving it :D
        queue = new Queue<>();
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
            for (Board board : solver.solution()) {
                queue.enqueue(board);
            }
            slv = true;
        } else slv = false;
    }

    private void newGame() {
       if (size == 3) {
           int m;
           reset(); // reset in intial state
           do {
               //reset(); // reset in intial state
               shuffle(); // shuffle
               // m = manhattan();
           } while (!isSolvable()); // make it until grid be solvable
           // System.out.println(m);
       } else {
           doShuffle();
       }

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

    private void doShuffle() {
        int m = RANDOM.nextInt(niz.length);
        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = niz[m][i];
            // novo ovo
            if (niz[m][i] == 0) blankPos = i;
        }
    }
    private void doIt() {
        niz = new int[][]{
                {1, 2, 3, 4, 5, 0, 7, 8, 9, 6, 11, 12, 13, 10, 14, 15},
                {1, 6, 2, 4, 5, 0, 3, 8, 9, 10, 7, 11, 13, 14, 15, 12},
                {1, 2, 8, 3, 5, 11, 6, 4, 0, 10, 7, 12, 9, 13, 14, 15},
                {5, 1, 3, 4, 13, 2, 7, 8, 6, 10, 11, 12, 14, 9, 0, 15},
                {5, 1, 2, 4, 6, 0, 10, 7, 13, 11, 3, 8, 14, 9, 15, 12},
                {5, 2, 4, 0, 6, 1, 3, 8, 13, 11, 7, 12, 10, 9, 14, 15},
                {2, 5, 3, 4, 1, 7, 11, 8, 9, 6, 0, 12, 13, 14, 15, 10},
                {3, 7, 2, 4, 1, 5, 10, 8, 6, 0, 11, 12, 9, 13, 14, 15},
                {3, 7, 1, 0, 6, 2, 8, 4, 5, 10, 11, 12, 9, 13, 14, 15},
                {1, 4, 8, 3, 7, 2, 10, 11, 5, 6, 0, 15, 9, 13, 14, 12},
                {1, 2, 3, 4, 5, 6, 14, 8, 13, 0, 9, 11, 10, 12, 15, 7},
                {9, 5, 1, 2, 6, 4, 8, 3, 10, 14, 7, 11, 13, 0, 15, 12},
                {2, 5, 1, 3, 9, 6, 12, 4, 10, 14, 8, 0, 13, 11, 15, 7},
                {1, 10, 6, 4, 5, 9, 2, 8, 13, 12, 0, 7, 14, 11, 3, 15},
                {1, 2, 3, 0, 5, 12, 7, 4, 13, 6, 14, 9, 10, 8, 11, 15},
                {2, 5, 4, 7, 9, 1, 3, 8, 11, 10, 0, 6, 14, 13, 15, 12},
                {1, 8, 3, 0, 5, 7, 4, 12, 14, 6, 2, 15, 9, 13, 10, 11},
                {2, 4, 8, 12, 1, 7, 3, 14, 0, 6, 15, 11, 5, 9, 13, 10}
        };
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
                // novo ovo
                if (tiles[i] == 0) blankPos = i;
            }
        }

        return countInversions % 2 == 0;
    }

    private void drawGrid(Graphics2D g) {
        for (int i = 0; i < tiles.length; i++) {
            // we convert 1D coords to 2D coords given the size of the 2D Array
            int r = i / size;
            int c = i % size;
            // we convert in coords on the UI
            int x = margin + c * tileSize;
            int y = margin + r * tileSize;
            if (size == 2) {
                x += margin;
            }

            // check special case for blank tile
            if (tiles[i] == 0) {
                if (gameOver && slv) {
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

            drawCenteredString(g, String.valueOf(tiles[i]), x, y);
        }
    }


    private void drawStartMessage(Graphics2D g) {
        if (!start[0] && slv) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(new Color(239, 39, 9));
            String s = "Click to start new game";
            g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2,
                    getHeight() - margin);
        }
        if (gameOver && slv) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(new Color(239, 39, 9));
            String s = "Puzzle is solved";
            g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2,
                    getHeight() - margin);
        }
        if (!slv) {
            g.setFont(getFont().deriveFont(Font.BOLD, 18));
            g.setColor(FOREGROUND_COLOR);
            String s = "Unsolvable! Please shuffle";
            g.drawString(s, (getWidth() - g.getFontMetrics().stringWidth(s)) / 2,
                    getHeight() - margin);
        }
    }

    private void drawCenteredString(Graphics2D g, String s, int x, int y) {
        // center string s for the given tile (x,y)
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent();
        int desc = fm.getDescent();
        g.drawString(s, x + (tileSize - fm.stringWidth(s)) / 2,
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

    public static void main(String[] args) {
 /*       SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            // button 0
            JButton button0 = new JButton("0");
            // ..text field
            JTextField displayField = new JTextField(1);
            displayField.setHorizontalAlignment(JTextField.CENTER);
            JPanel p1 = new JPanel();
            GridLayout gl = new GridLayout(1,2);
            p1.setLayout(gl);
            p1.add(button0);
            p1.add(displayField);
            // Engine engine = new Engine();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setTitle("Game of Fifteen");
            frame.setResizable(false);
            frame.add(new GameOfFifteen(3, 350, 30), BorderLayout.CENTER);
           // frame.add(new GameOfFifteen(), BorderLayout.CENTER);
            frame.add("South", p1);

            frame.pack();
            // center on the screen
            frame.setLocationRelativeTo(null);

            // enable the window's close button
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.setVisible(true);
        });*/


    }
}