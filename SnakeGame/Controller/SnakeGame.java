package Controller;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import MovementStrategy.MovementStartegy;
import MovementStrategy.ConcrreteClass.HumanStrategy;
import ObserverClass.GameObserver;
import Utility.Board;
import Utility.Pair;
import Utility.Snake;

public class SnakeGame {
    private Board board;
    // private Snake snake;
    private MovementStartegy startegy;
    private int[][] food;
    private int foodIndex;
    public Deque<Pair> snake;
    private Map<Pair, Boolean> snakeMap;
    private List<GameObserver> observers;

    public SnakeGame(int breadth, int height, int[][] food) {
        this.board = board.getInstance(breadth, height);
        // this.snake = new Snake();
        this.startegy = new HumanStrategy();
        this.food = food;
        this.foodIndex = 0;

        this.snake = new LinkedList<>();
        this.snakeMap = new HashMap<>();
        Pair initialPos = new Pair(0, 0);
        this.snake.offerFirst(initialPos);
        this.snakeMap.put(initialPos, true);
        this.observers = new ArrayList<>();

    }

    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    private void notifyMoveMade(Pair newHead) {
        for (GameObserver observer : observers) {
            observer.onMoveMade(newHead);
        }
    }

    // Notify observers of food eaten
    private void notifyFoodEaten(int foodIndex, int newScore) {
        for (GameObserver observer : observers) {
            observer.onFoodEaten(foodIndex, newScore);
        }
    }

    // Notify observers of game over
    private void notifyGameOver(int finalScore) {
        for (GameObserver observer : observers) {
            observer.onGameOver(finalScore);
        }
    }

    public void setMovementStrategy(MovementStartegy startegy) {
        this.startegy = startegy;
    }

    public int move(String direction) {

        Pair currentHead = this.snake.peekFirst();

        Pair newHead = this.startegy.getNextPosition(currentHead, direction);
        int newHeadRow = newHead.getRow();
        int newHeadCol = newHead.getCol();

        boolean boundaryCross = newHeadRow < 0 || newHeadCol < 0 || newHeadCol > this.board.getHeight()
                || newHeadRow > this.board.getBreadth();

        Pair currentTail = this.snake.peekLast();
        boolean biteItself = this.snakeMap.containsKey(newHead)
                || !(currentTail.getCol() == newHeadCol && currentTail.getRow() == newHeadRow);

        if (boundaryCross || biteItself){
            notifyGameOver(this.snake.size() - 1);
            return -1;
        }
            

        boolean ateFood = (this.foodIndex < this.food.length) &&
                this.food[this.foodIndex][0] == newHeadRow &&
                this.food[this.foodIndex][1] == newHeadCol;

        if (ateFood) {
            this.foodIndex++;
        } else {
            this.snake.pollLast();
            snakeMap.remove(currentTail);
        }

        this.snake.addFirst(newHead);
        this.snakeMap.put(newHead, true);

        return this.snake.size() - 1;
    }

    public Deque<Pair> getSnake() {
        return this.snake;
    }
}
