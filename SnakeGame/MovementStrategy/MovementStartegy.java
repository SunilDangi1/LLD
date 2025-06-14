package MovementStrategy;

import Utility.Pair;

public interface MovementStartegy {

    Pair getNextPosition(Pair currentHead, String direction);
} 
