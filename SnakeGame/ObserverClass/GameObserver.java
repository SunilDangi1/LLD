package ObserverClass;

import Utility.Pair;

public interface GameObserver {
    void onMoveMade(Pair newHeadPosition);
    void onFoodEaten(int foodIndex, int score);
    void onGameOver(int finalScore);
}
