package MovementStrategy.ConcrreteClass;

import MovementStrategy.MovementStartegy;
import Utility.Pair;

public class HumanStrategy implements MovementStartegy {
    
    @Override
    public Pair getNextPosition(Pair currentHead, String direction){
        int row = currentHead.getRow();
        int col = currentHead.getCol();
        switch (direction) {
            case "L": return new Pair(row-1, col);
            case "R": return new Pair(row+1, col);
            case "U": return new Pair(row, col+1);
            case "D": return new Pair(row, col-1);
            default:  return currentHead;
        }
    }
}
