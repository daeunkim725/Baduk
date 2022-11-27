import java.awt.Font;

public class Baduk {

    // Method that evaluates if there is a white(1) or black(2) stone
    // on all coordinates (x,y). If there are no stones, will return 0.
    // public boolean isNeighbor(int[][] g, int x, int y) {
    //
    //
    //     return true;
    // }


    public static void main(String[] args) {

        // Canvas size
        int cX = 900;
        int cY = 700;

        // Create 2D Array
        int[][] board = new int[19][19];
        // 0: empty, 1: white, 2: black

        StdDraw.setCanvasSize(cX, cY);

        // Scaling the Go board
        StdDraw.setXscale(-1, 28);
        StdDraw.setYscale(-1, 21);

        StdDraw.setPenRadius(0.003);
        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);

        Font font = new Font("Arial", Font.ITALIC, 13);
        StdDraw.setFont(font);
        boolean gameOn = true;

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
        double pRadi = 0.43; // The Radius of the stones
        int ctr = 0;

        while (gameOn) {
            if (StdDraw.isMousePressed()) {

                double Mx = StdDraw.mouseX();
                double My = StdDraw.mouseY();
                int Nx = (int) Math.round(Mx);
                int Ny = (int) Math.round(My);

                // Only let the stones be placed inside the board (in-bound)
                boolean inBound = (Ny > 0 && Ny < 20) && (Nx > 0 && Nx < 20);
                if (inBound && board[Nx - 1][Ny - 1] == 0) {

                    // Drawing the played stones
                    if (ctr % 2 == 0) {
                        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                        StdDraw.filledCircle(Nx, Ny, pRadi);
                        board[Nx - 1][Ny - 1] = 2; // Black (coordinates+1 = array)
                    }
                    else {
                        StdDraw.setPenColor(StdDraw.WHITE);
                        StdDraw.filledCircle(Nx, Ny, pRadi);
                        StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE); // Black border
                        StdDraw.circle(Nx, Ny, pRadi);
                        board[Nx - 1][Ny - 1] = 1; // White
                    }
                    ctr++;
                }
            }
        }
    }
}
