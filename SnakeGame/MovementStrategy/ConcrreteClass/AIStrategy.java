package MovementStrategy.ConcrreteClass;

import MovementStrategy.MovementStartegy;
import Utility.Pair;

public class AIStrategy implements MovementStartegy {

    @Override
    public Pair getNextPosition(Pair currentHead, String direction) {
        return currentHead;
    }
    
}
