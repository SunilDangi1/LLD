package Utility;

public class Board {
    private static Board instance;
    private int breadth;
    private int height;
    private Board(int breadth, int height) {
        this.breadth = breadth;
        this.height = height;
    }
    public static Board getInstance(int breadth, int height) {
        if(instance == null){
            instance = new Board(breadth, height);
        }
        return instance;
    }

    public int getBreadth() {
        return breadth;
    }
    public void setBreadth(int breadth) {
        this.breadth = breadth;
    }
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    
}