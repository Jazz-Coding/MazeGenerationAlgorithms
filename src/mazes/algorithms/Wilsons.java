package mazes.algorithms;

import mazes.components.Coordinate;
import mazes.components.Step;
import mazes.utils.MazeSaver;

import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Wilson's maze generation algorithm
 * - Relies on loop-erased random walks.
 * - Produces a uniform sample over all possible mazes.
 * - Tends to produce complex, difficult to solve mazes.
 */
public class Wilsons {
    private static String pickAString(List<String> options, Random RNG){
        return options.get(RNG.nextInt(options.size()));
    }

    private static Coordinate pickACoordinate(List<Coordinate> coordinates, Random RNG){
        return coordinates.get(RNG.nextInt(coordinates.size()));
    }

    public static void walk(String[][] maze, int rows, int cols, Random RNG){
        int realCols = 2*cols+1;
        int realRows = 2*rows+1;

        // Select random start point not in the maze.
        List<Coordinate> candidates = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int realX = j*2+1;
                int realY = i*2+1;
                if(!maze[realY][realX].equals("M")){
                    candidates.add(new Coordinate(realX,realY));
                }
            }
        }

        Coordinate start = pickACoordinate(candidates,RNG);
        int startX = start.getX();
        int startY = start.getY();
        int t = 0;

        Stack<Step> history = new Stack<>();
        history.push(new Step(startX,startY,t,"S"));

        t=1;
        while (true){
            Step lastStep = history.peek();
            int x = lastStep.getX();
            int y = lastStep.getY();

            // Check if already in the maze.
            if(maze[y][x].equals("M")){
                // If so, add anything in history before this point to the maze.
                for (Step historicalStep : history) {
                    maze[historicalStep.getY()][historicalStep.getX()] = "M";
                    if (!historicalStep.getLastMove().equals("S")) {
                        // Break down the wall on the way.
                        switch (historicalStep.getLastMove()) {
                            case "R" -> maze[historicalStep.getY()][historicalStep.getX() - 1] = "M";
                            case "L" -> maze[historicalStep.getY()][historicalStep.getX() + 1] = "M";
                            case "U" -> maze[historicalStep.getY() + 1][historicalStep.getX()] = "M";
                            case "D" -> maze[historicalStep.getY() - 1][historicalStep.getX()] = "M";
                        }
                    }
                }
                break;
            } else {
                // Check if already in the path (in the history somewhere).
                int backtrackSteps = 0;
                for (int i = 1; i < history.size()-1; i++) {
                    Step historicalStep = history.get(i);
                    if(historicalStep.equals(lastStep)){
                        // Queue an erasure of the loop up to this point.
                        int targetTime = historicalStep.getTime();
                        backtrackSteps = t - targetTime;
                        //System.out.println(t + "-" + targetTime + "=" + backtrackSteps);
                        break;
                    }
                }

                // Erase any loops.
                if(backtrackSteps != 0) {
                    for (int i = 0; i < backtrackSteps; i++) {
                        history.pop();
                        t--;
                    }
                } else {
                    // Move randomly.
                    List<String> moveOptions = new ArrayList<>();
                    if (x > 1) moveOptions.add("L");
                    if (x < realCols - 2) moveOptions.add("R");
                    if (y > 1) moveOptions.add("U");
                    if (y < realRows - 2) moveOptions.add("D");

                    int nextX = -1;
                    int nextY = -1;

                    String move = pickAString(moveOptions, RNG);
                    switch (move) {
                        case "U" -> {
                            nextX = x;
                            nextY = y - 2;
                        }
                        case "D" -> {
                            nextX = x;
                            nextY = y + 2;
                        }
                        case "L" -> {
                            nextX = x - 2;
                            nextY = y;
                        }
                        case "R" -> {
                            nextX = x + 2;
                            nextY = y;
                        }
                    }

                    history.push(new Step(nextX, nextY, t + 1, move));
                    t++;
                }
            }
        }
    }

    /**
     * Checks if the maze has been completed.
     * Asks "have any squares not yet been traversed by a walk?", if so, the maze is
     * not yet complete and further walks must be taken.
     */
    private static boolean mazeComplete(String[][] maze){
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if(maze[i][j].equals("C")){
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        // Maze dimensions.
        int rows = 64;
        int cols = 64;

        // array size = 2n+1
        int arrayRows = 2*rows+1;
        int arrayCols = 2*cols+1;

        // Maze initialization.
        String[][] maze = new String[arrayRows][arrayCols];
        for (int i = 0; i < arrayRows; i++) {
            boolean oddRow = i%2==0;
            for (int j = 0; j < arrayCols; j++) {
                boolean oddCol = j%2==0;
                if(oddRow && oddCol){
                    maze[i][j] = " ";
                } else if(!oddRow && !oddCol){
                    maze[i][j] = "C";
                } else {
                    maze[i][j] = "W";
                }
            }
        }

        // Select a random starting location for the random walk.
        Random RNG = new SecureRandom();
        maze[RNG.nextInt(cols)*2 + 1][RNG.nextInt(rows)*2 + 1] = "M";

        // Begin the recursive walking procedure (implemented with a stack).
        do {
            walk(maze, rows, cols, RNG);
        } while (!mazeComplete(maze));

        // Save as an image file.
        BufferedImage mazeImage = MazeSaver.getAsImage(arrayCols, arrayRows, maze,"M");
        MazeSaver.saveLocally(mazeImage,"maze_wilsons");
    }
}
