import Controller.SnakeGame;
import ObserverClass.ConcreteClass.ConlseObserver;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
            int width = 20;
            int height = 15;
            // Define some food positions (more can be generated during gameplay)
            int[][] foodPositions = {
                    {0, 1}, // Initial food
                    {10, 8}, // Second food
                    {3, 12}, // Third food
                    {8, 17}, // Fourth food
                    {12, 3} // Fifth food
            };
            
            // Initialize the game
            SnakeGame game = new SnakeGame(width, height, foodPositions);
            // Display game instructions
            System.out.println("===== SNAKE GAME =====");
            System.out.println(
                    "Controls: W (Up), S (Down), A (Left), D (Right), Q (Quit)");
            System.out.println("Eat food to grow your snake and increase your score.");
            System.out.println("Don't hit the walls or bite yourself!");
            game.addObserver(new ConlseObserver());
            Scanner scanner = new Scanner(System.in);
            boolean gameRunning = true;
            int score = 0;
            while(gameRunning){
                displayGameState(game);
            System.out.print("Enter move (W/A/S/D) or Q to quit: ");
            String input = scanner.nextLine().toUpperCase();

            if (input.equals("Q")) {
                    System.out.println("Game ended by player. Final score: " + score);
                    gameRunning = false;
                    continue;
                }

            String direction = convertInput(input);
                // Skip invalid inputs
                if (direction.isEmpty()) {
                    System.out.println("Invalid input! Use W/A/S/D to move or Q to quit.");
                    continue;
                }
            score = game.move(direction);
                // Check for game over
                if (score == -1) {
                    System.out.println("GAME OVER! You hit a wall or bit yourself.");
                    System.out.println("Final score: " + (game.getSnake().size() - 1));
                    gameRunning = false;
                } else {
                    System.out.println("Score: " + score);
                }
            }
            scanner.close();
    }

private static String convertInput(String input) {
            switch (input) {
                case "W":
                    return "U"; // Up
                case "S":
                    return "D"; // Down
                case "A":
                    return "L"; // Left
                case "D":
                    return "R"; // Right
                default:
                    return ""; // Invalid input
            }
        }
        // A simple method to display the game state in the console
        // In a real implementation, this would be replaced with graphics
        private static void displayGameState(SnakeGame game) {
            // This is a placeholder - in a real implementation, you would
            // access the game's state and render it appropriately
            System.out.println("nCurrent snake length: " + game.getSnake().size());
            // In a complete implementation, you would render the board with the
            // snake, food, and boundaries visually
        }

}
            

    
