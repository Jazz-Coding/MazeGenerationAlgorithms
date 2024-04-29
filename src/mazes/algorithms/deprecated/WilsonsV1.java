package mazes.algorithms.deprecated;

import mazes.components.Coordinate;
import mazes.components.Step;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Wilson's maze generation algorithm
 * DEPRECATED - Uses a non-OOP approach.
 */
public class WilsonsV1 {
    private static void walk(String[][] maze){
        // Identity start candidates.
        List<Coordinate> outside = new ArrayList<>();

        int rows = maze.length;
        int cols = maze[0].length;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String cell = maze[i][j];
                if(cell.equals("C")){
                    outside.add(new Coordinate(i,j));
                }
            }
        }

        Random RNG = ThreadLocalRandom.current();
        Coordinate start = outside.get(RNG.nextInt(outside.size()));

        // Begin a random walk from this point.
        Stack<Step> history = new Stack<>();
        int t =  0;
        history.push(new Step(start.getX(),start.getY(),t,"S"));
        while (true){
            int x = start.getX();
            int y = start.getY();
            maze[x][y] = "p"; // symbol for "in path"

            // Check up-left-down-right
            Coordinate up = new Coordinate(x, y - 2);
            Coordinate left = new Coordinate(x-2, y);
            Coordinate down = new Coordinate(x, y + 2);
            Coordinate right = new Coordinate(x-2, y);

            List<Coordinate> valid = new ArrayList<>();

            // Rule out impossible moves.
            if(y>=2) valid.add(up);
            if(x>=2) valid.add(left);
            if(y<cols-2) valid.add(down);
            if(x<rows-2) valid.add(right);

            // Select one at random.
            Coordinate choice = valid.get(RNG.nextInt(valid.size()));
            int choiceX = choice.getX();
            int choiceY = choice.getY();

            // Depending on the value of this choice, act differently.
            String mazeValue = maze[choiceX][choiceY];
            if(mazeValue.equals("p")){
                // Loop created, erase the loop...
            } else if(mazeValue.equals("c")){
                // Maze reached, add path to the maze and terminate.
                break;
            } else {
                // New territory, add to path and continue.
                history.push(new Step(choiceX,choiceY,t,"S"));
            }

            t++;
        }

        // Add the path to the maze.
        for (Step step : history) {
            int sx = step.getX();
            int sy = step.getY();
            maze[sx][sy] = "c";
        }
    }

    public static void main(String[] args) {
        int rows = 15;
        int cols = 15;

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

        int startX = 7;
        int startY = 7;
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
                //history.pop();
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

        BufferedImage image = new BufferedImage(arrayCols, arrayRows,BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < arrayRows; i++) {
            for (int j = 0; j < arrayCols; j++) {
                String mazeElement = maze[i][j];
                if(!mazeElement.equals("c")){
                    image.setRGB(j,i,0);
                } else {
                    image.setRGB(j,i,(255 << 16 | 255 << 8 | 255));
                }
            }
        }

        try {
            File outputFile = new File("maze.png");
            outputFile.createNewFile();
            ImageIO.write(image,"png",outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
