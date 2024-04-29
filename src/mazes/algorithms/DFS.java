package mazes.algorithms;

import mazes.components.Coordinate;
import mazes.utils.MazeSaver;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Depth-first-search maze generation
 * - Tends to produce long corridors.
 * - Tends to be easier to solve.
 * - Not a true uniform distribution of all mazes.
 */
public class DFS {
    public static void main(String[] args) {
        int rows = 64;
        int cols = 64;
        // array size = 2n+1
        int arrayRows = 2*rows+1;
        int arrayCols = 2*cols+1;

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

        int startX = 1;
        int startY = 1;

        Stack<Coordinate> history = new Stack<>();
        history.push(new Coordinate(startX,startY));
        while (true){
            Coordinate pop = history.peek();
            int x = pop.getX();
            int y = pop.getY();

            maze[x][y] = "c"; // symbol for visited

            List<Integer> decisions = new ArrayList<>();
            // Check up-left-down-right
            if(y >=2 && maze[x][y-2].equals("C")){ // up
                decisions.add(0);
            }
            if (x >= 2 && maze[x-2][y].equals("C")){ // left
                decisions.add(1);
            }
            if(y < arrayCols-2 && maze[x][y+2].equals("C")){ // down
                decisions.add(2);
            }
            if(x < arrayRows-2 && maze[x+2][y].equals("C")){ // right
                decisions.add(3);
            }

            if(decisions.isEmpty()){
                history.pop();
                if(history.isEmpty()){
                    // Done.
                    break;
                }
                // Backtrack
                continue;
            }

            int decision = decisions.get(ThreadLocalRandom.current().nextInt(decisions.size()));
            switch (decision){
                case 0 -> {
                    history.push(new Coordinate(x,y-2));
                    maze[x][y-1] = "c";
                }
                case 1 -> {
                    history.push(new Coordinate(x-2,y));
                    maze[x-1][y] = "c";
                }
                case 2 -> {
                    history.push(new Coordinate(x,y+2));
                    maze[x][y+1] = "c";
                }
                case 3 -> {
                    history.push(new Coordinate(x+2,y));
                    maze[x+1][y] = "c";
                }
            }
        }

        for (String[] strings : maze) {
            System.out.println(Arrays.toString(strings));
        }

        BufferedImage image = MazeSaver.getAsImage(arrayCols, arrayRows, maze,"c");
        MazeSaver.saveLocally(image,"maze_dfs");
    }
}
