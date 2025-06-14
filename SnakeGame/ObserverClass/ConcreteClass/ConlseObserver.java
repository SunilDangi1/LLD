package ObserverClass.ConcreteClass;

import ObserverClass.GameObserver;
import Utility.Pair;

public class ConlseObserver implements GameObserver {

    @Override
    public void onMoveMade(Pair newHeadPosition) {
        System.out.println("Snake moved to position: [" + 
                           newHeadPosition.getRow() + ", " + 
                           newHeadPosition.getCol() + "]");
    }

    @Override
    public void onFoodEaten(int foodIndex, int score) {
        System.out.println("Food eaten! Current score: " + score);
    }

    @Override
    public void onGameOver(int finalScore) {
        System.out.println("Game Over! Final score: " + finalScore);
    }
    
}
