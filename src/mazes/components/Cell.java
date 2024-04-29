package mazes.components;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    public int x, y;
    public Wall north, east, south, west;
    public boolean visited = false;
    public List<Cell> neighbors;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.neighbors = new ArrayList<>();

        north = new Wall();
        east = new Wall();
        south = new Wall();
        west = new Wall();
    }

    public void addNeighbor(Cell cell){
        neighbors.add(cell);
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }

    public List<Cell> getNeighbors() {
        return neighbors;
    }
}
