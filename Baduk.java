import java.awt.Font;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

public class Baduk {
    // Sprinkle -> stopwatch implementing rendering it differently
    public static Timer t;

    public static void CountDownTimer(int seconds) {
        t = new Timer();
        // schedule the timer
        t.schedule(new b(), seconds * 1000);
    }

    private static class b extends TimerTask {
        public void run() {
            System.out.println("TIME OVER : YOU LOST");
            t.cancel();
        }
    }

    // method that draw the Go board
    public static void drawBoard() {

        StdDraw.setXscale(-1, 21);
        StdDraw.setYscale(-1, 21);

        StdDraw.setPenRadius(0.003);
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);

        Font font = new Font("Dialog", Font.BOLD, 9);
        StdDraw.setFont(font);


        for (int i = 1; i <= 19; i++) {
            StdDraw.line(i, 1, i, 19);
            StdDraw.line(1, i, 19, i);
        }

        // Drawing the "flower dots"
        double fRadi = 0.13; // The radius of the flower dots

        StdDraw.filledCircle(4, 4, fRadi);
        StdDraw.filledCircle(10, 4, fRadi);
        StdDraw.filledCircle(16, 4, fRadi);
        StdDraw.filledCircle(4, 10, fRadi);
        StdDraw.filledCircle(10, 10, fRadi);
        StdDraw.filledCircle(16, 10, fRadi);
        StdDraw.filledCircle(4, 16, fRadi);
        StdDraw.filledCircle(10, 16, fRadi);
        StdDraw.filledCircle(16, 16, fRadi);

        // Coordinate numbers on the sides
        StdDraw.textRight(0.3, 1, "19");
        int numY = 1;
        for (int i = 1; i < 19; i++) {
            numY += 1;
            StdDraw.textRight(0.3, numY, String.valueOf(19 - i));
        }

        StdDraw.text(1, 19.7, "1");
        int numX = 1;
        for (int i = 1; i < 19; i++) {
            numX += 1;
            StdDraw.text(numX, 19.7, String.valueOf(i + 1));
        }
    }

    public static void drawPieces(int[][] board, int[][] nums) {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (board[i][j] == 1) {
                    StdDraw.setPenColor(StdDraw.WHITE);
                    StdDraw.filledCircle(i + 1, j + 1, 0.43);
                    StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE); // Black border
                    StdDraw.circle(i + 1, j + 1, 0.43);
                    Font font = new Font("Dialog", Font.BOLD, 9);
                    StdDraw.setFont(font);
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.text(i + 1, j + 1 - 0.05, String.valueOf(nums[i][j]));
                }
                else if (board[i][j] == 2) {
                    StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                    StdDraw.filledCircle(i + 1, j + 1, 0.43);
                    StdDraw.circle(i + 1, j + 1, 0.43);
                    Font font = new Font("Dialog", Font.BOLD, 9);
                    StdDraw.setFont(font);
                    StdDraw.setPenColor(StdDraw.BLACK);
                    StdDraw.text(i + 1, j + 1 - 0.05, String.valueOf(nums[i][j]));
                }
            }
        }
    }

    public static boolean isNeighbor(int[][] go, int x, int y) {
        int stoneColor = go[x - 1][y - 1]; // color of current stone
        // The difference between array coordinates and Go board coordinates is 1
        if (stoneColor == 0) {
            return false; // if spot has no stone, we don't care about it
        }
        int ctr = 0; // number of stones of current color (as defined by side)
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (go[i][j] == stoneColor) {
                    ctr++; // increment if spot on board is current color
                }
            }
        }
        int[][] checkCluster = new int[19][19]; // new array for finding cluster
        checkCluster[x - 1][y - 1] = 1;
        // set spot corresponding to piece on cluster map = 1 for the presence
        // of a piece

        // check board once for each tile b/c the cluster potentially grows each iteration
        for (int k = 0; k < ctr; k++) {
            for (int i = 0; i < 19; i++) {
                for (int j = 0; j < 19; j++) {

                    // adjacency: searching for spots adjacent to current cluster
                    boolean adjacent
                            = false; // 이웃 확인 0 돌 없음 1 이면 돌있음 만약에 모든 면이 둘러쌓여 있지 않으면 죽지 않음
                    if (i > 0 && checkCluster[i - 1][j] == 1) adjacent = true;
                    else if (i < 18 && checkCluster[i + 1][j] == 1) adjacent = true;
                    else if (j > 0 && checkCluster[i][j - 1] == 1) adjacent = true;
                    else if (j < 18 && checkCluster[i][j + 1] == 1) adjacent = true;

                    // if we are adjacent to a cluster AND stone in original board is the same color
                    // we grow our cluster.
                    if (go[i][j] == stoneColor && adjacent) {
                        checkCluster[i][j] = 1;
                    }
                }
            }
        }

        // now that we have the cluster, let's check if it's surrounded.
        // instead of checking if the surroundings are the opposite color,
        // check if they are empty. if NONE of surroundings are empty,
        // the cluster MUST be surrounded by opposite color.

        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (checkCluster[i][j] == 1) {
                    // cluster is not surrounded if any neighbors are empty!
                    // short circuit by making sure we're in bounds
                    // then check each of the surrounding spots
                    if (i > 0 && go[i - 1][j] == 0) return false;
                    else if (i < 18 && go[i + 1][j] == 0) return false;
                    else if (j > 0 && go[i][j - 1] == 0) return false;
                    else if (j < 18 && go[i][j + 1] == 0) return false;
                }
            }
        }

        // because we checked for the opposite case (surrounded by nothing)
        // and returned false for these instances,
        // it must be true that our cluster is surrounded by
        // the opposite color
        return true;
    }

    public static int[][] kill(int[][] go) {
        int[][] toDie = new int[19][19];
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (isNeighbor(go, i + 1, j + 1)) toDie[i][j] = 1;
            }
        }
        return toDie;
    }

    public static void main(String[] args) {

        int ctr = 2;
        // // Canvas size

        // Create 2D Array to keep track of game pieces
        int[][] board = new int[19][19];
        // 0: empty, 1: white, 2: black

        // create another 2d array to keep track of order pieces are placed
        int[][] order = new int[19][19];

        // Scaling the Go board

        drawBoard();

        double pRadi = 0.43; // The Radius of the stones
        boolean gameOn = true;

        while (gameOn) {
            // if the user presses the 'a' key on their keyboard, the board will
            // redraw and the counter will reset

            if (StdDraw.isKeyPressed(KeyEvent.VK_A)) {
                StdDraw.clear();
                Font font = new Font("Dialog", Font.BOLD, 9);
                StdDraw.setFont(font);
                drawBoard();
                ctr = 2;
                board = new int[19][19];
                order = new int[19][19];

            }
            else {

                if (StdDraw.isMousePressed()) {
                    double mouseX = StdDraw.mouseX();
                    double mouseY = StdDraw.mouseY();
                    int newX = (int) Math.round(mouseX);
                    int newY = (int) Math.round(mouseY);

                    // Only let the stones be placed inside the board (in-bound)
                    boolean inBound = (newY > 0 && newY < 20) && (newX > 0 && newX < 20);
                    if (inBound && board[newX - 1][newY - 1] == 0) {


                        // Drawing the played stones
                        if (ctr % 2 == 0) {

                            StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                            StdDraw.filledCircle(newX, newY, pRadi);
                            board[newX - 1][newY - 1] = 2; // Black (coordinates+1 = array)
                            order[newX - 1][newY - 1] = ctr - 1;

                            StdDraw.setPenColor(StdDraw.BLACK);
                            StdDraw.text(newX, newY - 0.05, String.valueOf(ctr - 1));

                            CountDownTimer(10);
                            System.out.println("WHITE: 30 Seconds Remaining");
                        }
                        else {

                            StdDraw.setPenColor(StdDraw.WHITE);
                            StdDraw.filledCircle(newX, newY, pRadi);
                            StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE); // Black border
                            StdDraw.circle(newX, newY, pRadi);
                            board[newX - 1][newY - 1] = 1; // White
                            order[newX - 1][newY - 1] = ctr - 1;

                            StdDraw.setPenColor(StdDraw.BLACK);
                            StdDraw.text(newX, newY - 0.05, String.valueOf(ctr - 1));

                            t.cancel();
                            CountDownTimer(10);
                            System.out.println("BLACK: 30 Seconds Remaining");

                        }

                        // only need to redraw pieces after removing smthn
                        boolean reDraw = false;

                        // create an array of pieces to be removed
                        int[][] tempAr = kill(board);
                        for (int i = 0; i < 19; i++) {
                            for (int j = 0; j < 19; j++) {
                                if (tempAr[i][j] == 1) {
                                    board[i][j] = 0;
                                    order[i][j] = 0;
                                    reDraw = true;
                                }
                            }
                        }

                        if (reDraw) {
                            t.cancel();
                            StdDraw.clear();
                            drawBoard();
                            drawPieces(board, order);
                        }

                        ctr++;
                    }
                }
            }
        }
    }
}
