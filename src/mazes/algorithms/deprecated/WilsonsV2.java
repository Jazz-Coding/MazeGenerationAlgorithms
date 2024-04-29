package mazes.algorithms.deprecated;

import mazes.components.Cell;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Wilson's maze generation algorithm
 * DEPRECATED
 */
public class WilsonsV2 {
    private Cell[][] grid;
    private int width, height;
    private Random RNG;

    public WilsonsV2(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];
        this.RNG = ThreadLocalRandom.current();

        // ... initialize cells
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = new Cell(i,j);
            }
        }

        // ... and their neighbors
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Cell cell = grid[i][j];
                if(j>0){
                    // add up
                    cell.addNeighbor(grid[i][j-1]);
                }
                if(i > 0){
                    // add left
                    cell.addNeighbor(grid[i-1][j]);
                }
                if(j < height-1){
                    // add down
                    cell.addNeighbor(grid[i][j+1]);
                }
                if(i < width-1){
                    // add right
                    cell.addNeighbor(grid[i+1][j]);
                }
            }
        }
    }

    private Cell randomNeighbor(Cell cell) {
        List<Cell> neighbors = cell.getNeighbors();
        return neighbors.get(RNG.nextInt(neighbors.size()));
        // ... return a random unvisited neighbor of the cell
    }

    public void generate() {
        // Start with a random cell and add it to the tree
        int x_initial = RNG.nextInt(width);
        int y_initial = RNG.nextInt(height);

        Cell initial = grid[x_initial][y_initial];
        initial.setVisited(true);

        List<Cell> unvisitedCells = new ArrayList<>();// ... initialize with all cells except the initial one;
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if(!(i==x_initial && j == y_initial)){
                    unvisitedCells.add(grid[i][j]);
                }
            }
        }


        while (!unvisitedCells.isEmpty()) {
            Cell current = unvisitedCells.get(RNG.nextInt(unvisitedCells.size()));// ... select a random unvisited cell;
            Stack<Cell> path = new Stack<>();
            while (!current.isVisited()) {
                path.push(current);
                Cell next = randomNeighbor(current);

                if (path.contains(next)) {
                    // Loop detected; pop until the looped cell is reached
                    while (path.peek() != next) {
                        path.pop();
                    }
                } else {
                    removeWallBetween(current, next);
                }

                current = next;
            }

            while (!path.isEmpty()) {
                Cell nextInPath = path.pop();
                nextInPath.setVisited(true);
                unvisitedCells.remove(nextInPath);
            }
        }

        String[][] strArray = new String[2 * width + 1][2 * height + 1];
        for (int i = 0; i < strArray.length; i++) {
            for (int j = 0; j < strArray[0].length; j++) {
                strArray[i][j]="W";
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int cellMiddleI = i*2+1;
                int cellMiddleJ = j*2+1;
                strArray[cellMiddleI][cellMiddleJ] = "C";
                Cell cell = grid[i][j];

                if(cell.north.destroyed){
                    strArray[cellMiddleI][cellMiddleJ-1] = "C";
                }
                if(cell.east.destroyed){
                    strArray[cellMiddleI+1][cellMiddleJ] = "C";
                }
                if(cell.south.destroyed){
                    strArray[cellMiddleI][cellMiddleJ+1] = "C";
                }
                if(cell.west.destroyed){
                    strArray[cellMiddleI-1][cellMiddleJ] = "C";
                }
            }
        }

        for (String[] strings : strArray) {
            System.out.println(Arrays.toString(strings));
        }

        BufferedImage image = new BufferedImage(2*width+1, 2*height+1,BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < 2*width+1; i++) {
            for (int j = 0; j < 2*height+1; j++) {
                String mazeElement = strArray[i][j];
                if(!mazeElement.equals("C")){
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

    private void removeWallBetween(Cell a, Cell b) {
        if (a.x < b.x) {  // b is to the right of a
            a.east.breakDown();
            b.west.breakDown();  // break the corresponding wall of b
        } else if (a.x > b.x) {  // b is to the left of a
            a.west.breakDown();
            b.east.breakDown();  // break the corresponding wall of b
        } else if (a.y < b.y) {  // b is below a
            a.south.breakDown();
            b.north.breakDown();  // break the corresponding wall of b
        } else {  // b is above a
            a.north.breakDown();
            b.south.breakDown();  // break the corresponding wall of b
        }
    }


    public static void main(String[] args) {
        WilsonsV2 wilson = new WilsonsV2(16, 16);
        wilson.generate();
    }
}
