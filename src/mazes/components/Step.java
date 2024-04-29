package mazes.components;

import java.util.Objects;

public class Step {
    private int x;
    private int y;
    private int time;
    private String lastMove;

    public Step(int x, int y, int time, String lastMove) {
        this.x = x;
        this.y = y;
        this.time = time;
        this.lastMove = lastMove;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Step step = (Step) o;
        return x == step.x && y == step.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public String getLastMove() {
        return lastMove;
    }
}
