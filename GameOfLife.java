import java.awt.*;

public class GameOfLife {
    public static void main(String[] args) {
        // Initilization phase
        if (args.length != 3){
            System.err.println("Please enter 3 arguments, one for number of copies, one for number of cells, and one for cell size");
            System.exit(1);
        }
        int numCopies = Integer.parseInt(args[0]);
        int numCells = Integer.parseInt(args[1]);
        double cellSize = Double.parseDouble(args[2]);
        if (numCopies <= 0){
            System.err.println("Please enter a valid number of copies");
            System.exit(1);
        }
        if (numCells < 1){
            System.err.println("Please enter a valid number of cells");
            System.exit(1);
        }
        if (cellSize <= 0){
            System.err.println("Please enter a valid cell size");
            System.exit(1);
        }
        StdDraw.setXscale(0,numCells);
        StdDraw.setYscale(0,numCells);
        StdDraw.setPenColor(51,204,255);
        int[][] state;
        state = new int[numCells][numCells];
        for (int i=0; i<numCells;i++){
            for (int g=0;g<numCells;g++){
                state[i][g] = 0;
            }
        }
        StdDraw.enableDoubleBuffering();
        for (int g=0;g<numCopies;g++){
            glider(state, (int)(Math.random() * (numCells)),(int)(Math.random() * (numCells)));
            toad(state, (int)(Math.random() * (numCells)),(int)(Math.random() * (numCells)));
            beacon(state, (int)(Math.random() * (numCells)),(int)(Math.random() * (numCells)));
            tenCellRow(state, (int)(Math.random() * (numCells)),(int)(Math.random() * (numCells)));
            //smallExploder(state, (int)(Math.random() * (r)),(int)(Math.random() * (c)));
        }
        draw(state, cellSize);
        StdDraw.show();
        StdDraw.pause(100);
        StdDraw.clear();
        while (true){
            draw(update(state), cellSize);
            StdDraw.show();
            StdDraw.pause(100);
            StdDraw.clear();
        }
    }
    public static void draw(int state[][], double cellSize){
        for (int i=0;i< state.length;i++){
            for(int j=0;j< state.length;j++){
                if (state[i][j] == 1){
                    StdDraw.filledSquare(i, j, cellSize/2);
                }
            }
        }
    }
    public static int[][] update(int state[][]){
        // Create a new grid of same size as previous one
        int[][] update;
        update = new int[state.length][state.length];
        // Look at state of corresponding cell in previous grid and its neighbors
        for (int i=0;i< state.length;i++){
            for (int j=0;j< state.length;j++){
                int rightTopNeighbor = state[caseCheckerX(i+1, state)][caseCheckerY(j+1, state)];
                int rightNeighbor  = state[caseCheckerX(i+1, state)][caseCheckerY(j, state)];
                int rightBottomNeighbor = state[caseCheckerX(i+1, state)][caseCheckerY(j-1, state)];
                int bottomNeighbor = state[caseCheckerX(i, state)][caseCheckerY(j-1, state)];
                int leftBottomNeighbor = state[caseCheckerX(i-1, state)][caseCheckerY(j-1, state)];
                int leftNeighbor = state[caseCheckerX(i-1, state)][caseCheckerY(j, state)];
                int leftTopNeighbor = state[caseCheckerX(i-1, state)][caseCheckerY(j+1, state)];
                int topNeighbor = state[caseCheckerX(i, state)][caseCheckerY(j+1, state)];
                int neighborhoodValue = rightTopNeighbor + rightNeighbor +rightBottomNeighbor + bottomNeighbor + leftBottomNeighbor +leftNeighbor + leftTopNeighbor +topNeighbor;
                // Use rules of game to determine state of cell in new state
                // Any live cell with fewer than two live neighbours dies, as if by underpopulation.
                if (state[i][j] == 1 && neighborhoodValue < 2){
                    update[i][j] = 0;
                }
                // Any live cell with two or three live neighbours lives on to the next generation.
                if (state[i][j] == 1 && (neighborhoodValue == 2 || neighborhoodValue == 3)){
                    update[i][j] = 1;
                }
                // Any live cell with more than three live neighbours dies, as if by overpopulation.
                if (state[i][j] == 1 && neighborhoodValue > 3){
                    update[i][j] = 0;
                }
                // Any dead cell with exactly three live neighbours becomes a live cell, as if by reproduction.
                if (state[i][j] == 0 && neighborhoodValue == 3){
                    update[i][j] = 1;
                }
            }
        }
        for (int i=0; i<state.length;i++){
            for (int g=0;g<state.length;g++){
                state[i][g] = update[i][g];
            }
        }
        return update;
    }
    public static void beacon(int state[][], int x, int y){
        state[caseCheckerX(x, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x+1, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x+1, state)][caseCheckerY(y-1, state)]=1;
        state[caseCheckerX(x, state)][caseCheckerY(y-1, state)] = 1;
        state[caseCheckerX(x-1, state)][caseCheckerY(y+1, state)] = 1;
        state[caseCheckerX(x-1, state)][caseCheckerY(y+2, state)] = 1;
        state[caseCheckerX(x-2, state)][caseCheckerY(y+1, state)] = 1;
        state[caseCheckerX(x-2, state)][caseCheckerY(y+2, state)] = 1;
    }
    public static void toad(int state[][], int x, int y){
        state[caseCheckerX(x, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x-1, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x+1, state)][caseCheckerY(y, state)] =1;
        state[caseCheckerX(x, state)][caseCheckerY(y+1, state)] = 1;
        state[caseCheckerX(x+1, state)][caseCheckerY(y+1, state)]= 1;
        state[caseCheckerX(x+2, state)][caseCheckerY(y+1, state)] = 1;

    }
    public static void glider(int state[][], int x, int y){
        state[caseCheckerX(x, state)][caseCheckerY(y,state)] = 1;
        state[caseCheckerX(x, state)][caseCheckerY(y+1, state)] = 1;
        state[caseCheckerX(x-1,state)][caseCheckerY(y+2, state)] = 1;
        state[caseCheckerX(x-1, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x-2, state)][caseCheckerY(y, state)] = 1;
    }
    public static void tenCellRow(int state[][], int x, int y){
        state[caseCheckerX(x, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x+1, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x+2, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x+3, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x+4, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x+5, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x+6, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x+7, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x+8, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x+9, state)][caseCheckerY(y, state)] = 1;
    }
    public static void smallExploder(int state[][], int x, int y){
        state[caseCheckerX(x, state)][caseCheckerY(y, state)] = 1;
        state[caseCheckerX(x+1, state)][caseCheckerY(y+1, state)] = 1;
        state[caseCheckerX(x-1, state)][caseCheckerY(y+1, state)] = 1;
        state[caseCheckerX(x+1, state)][caseCheckerY(y+2, state)] = 1;
        state[caseCheckerX(x-1, state)][caseCheckerY(y+2, state)] = 1;
        state[caseCheckerX(x, state)][caseCheckerY(y+2, state)] = 1;
        state[caseCheckerX(x, state)][caseCheckerY(y+3, state)] = 1;
    }
    public static int caseCheckerX(int x, int state[][]){
        if (x >= state.length) {
            return x % state.length;
        }
        if (x < 0){
            return state.length + x;
        }
        return x;
    }
    public static int caseCheckerY(int y, int state[][])
    {
        if (y >= state.length) {
            return y % state.length;
        }
        if (y < 0){
            return state.length + y;
        }
        return y;
    }}
