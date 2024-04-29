package mazes.components;

public class Wall {
    public boolean destroyed = false;

    public void breakDown(){
        destroyed = true;
    }
}
