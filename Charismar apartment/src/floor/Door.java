package floor;

public class Door {
     private int x, y;
    private boolean isLeftDoor;
    
    public Door(int x, int y, boolean isLeftDoor) {
        this.x = x;
        this.y = y;
        this.isLeftDoor = isLeftDoor;
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isLeftDoor() { return isLeftDoor; }
}

