package TicTocGame;

import java.util.Scanner; // For reading user input

// --- Enums ---

/**
 * Represents the current state of the Tic-Tac-Toe game.
 */
enum GameState {
    IN_PROGRESS, // Game is still ongoing
    PLAYER_X_WINS, // Player X has won
    PLAYER_O_WINS, // Player O has won
    DRAW // Game ended in a draw
}

// --- Player Class ---

/**
 * Represents a player in the Tic-Tac-Toe game.
 */
class Player {
    private String name; // Name of the player (e.g., "Player 1")
    private char mark;   // Mark of the player ('X' or 'O')

    /**
     * Constructor for the Player class.
     * @param name The name of the player.
     * @param mark The mark ('X' or 'O') assigned to the player.
     */
    public Player(String name, char mark) {
        this.name = name;
        this.mark = mark;
    }

    /**
     * Gets the name of the player.
     * @return The player's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the mark of the player.
     * @return The player's mark ('X' or 'O').
     */
    public char getMark() {
        return mark;
    }
}

// --- Board Class ---

/**
 * Represents the Tic-Tac-Toe game board.
 */
class Board {
    private char[][] cells; // 2D array to store 'X', 'O', or ' ' (empty)
    private int size;       // Size of the board (e.g., 3 for 3x3)

    /**
     * Constructor for the Board class.
     * Initializes the board with empty cells.
     * @param size The dimension of the square board (e.g., 3 for 3x3).
     */
    public Board(int size) {
        this.size = size;
        this.cells = new char[size][size];
        initializeBoard();
    }

    /**
     * Initializes all cells on the board to empty (' ').
     */
    private void initializeBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                cells[i][j] = ' '; // ' ' represents an empty cell
            }
        }
    }

    /**
     * Checks if a given row and column are within the board bounds
     * and if the cell at that position is empty.
     * @param row The row index.
     * @param col The column index.
     * @return true if the move is valid, false otherwise.
     */
    public boolean isValidMove(int row, int col) {
        // Check if within bounds
        if (row < 0 || row >= size || col < 0 || col >= size) {
            System.out.println("Invalid move: Row or column out of bounds.");
            return false;
        }
        // Check if the cell is empty
        if (cells[row][col] != ' ') {
            System.out.println("Invalid move: Cell is already occupied.");
            return false;
        }
        return true;
    }

    /**
     * Places a player's mark on the specified cell.
     * Assumes the move has already been validated.
     * @param row The row index.
     * @param col The column index.
     * @param mark The player's mark ('X' or 'O').
     */
    public void placeMark(int row, int col, char mark) {
        cells[row][col] = mark;
    }

    /**
     * Checks if all cells on the board are occupied.
     * @return true if the board is full, false otherwise.
     */
    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (cells[i][j] == ' ') {
                    return false; // Found an empty cell, so board is not full
                }
            }
        }
        return true; // All cells are occupied
    }

    /**
     * Prints the current state of the board to the console.
     */
    public void printBoard() {
        System.out.println("\n--- Current Board ---");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(" " + cells[i][j] + (j == size - 1 ? "" : " |"));
            }
            System.out.println();
            if (i < size - 1) {
                for (int k = 0; k < size; k++) {
                    System.out.print("---" + (k == size - 1 ? "" : "+"));
                }
                System.out.println();
            }
        }
        System.out.println("---------------------\n");
    }

    /**
     * Gets the size of the board.
     * @return The board size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the mark at a specific cell.
     * @param row The row index.
     * @param col The column index.
     * @return The character ('X', 'O', or ' ') at the specified cell.
     */
    public char getMark(int row, int col) {
        return cells[row][col];
    }
}

// --- TicTacToeGame Class ---

/**
 * Orchestrates the Tic-Tac-Toe game flow and logic.
 */
class TicTacToeGame {
    private Board board;
    private Player player1;
    private Player player2;
    private Player currentPlayer; // The player whose turn it is
    private GameState currentState; // Current state of the game
    private int movesCount;       // Number of moves made so far
    private Scanner scanner;      // For reading player input

    /**
     * Constructor for the TicTacToeGame.
     * @param size The size of the board (e.g., 3 for 3x3).
     * @param player1 The first player (typically 'X').
     * @param player2 The second player (typically 'O').
     */
    public TicTacToeGame(int size, Player player1, Player player2) {
        this.board = new Board(size);
        this.player1 = player1;
        this.player2 = player2;
        this.currentPlayer = player1; // Player 1 starts
        this.currentState = GameState.IN_PROGRESS;
        this.movesCount = 0;
        this.scanner = new Scanner(System.in);
        System.out.println("Tic-Tac-Toe game initialized!");
    }

    /**
     * Starts the main game loop.
     */
    public void start() {
        System.out.println("Game started! " + currentPlayer.getName() + " goes first.");

        while (currentState == GameState.IN_PROGRESS) {
            board.printBoard();
            System.out.println(currentPlayer.getName() + "'s turn (" + currentPlayer.getMark() + "). Enter row and column (0-" + (board.getSize() - 1) + "):");

            int row = -1;
            int col = -1;
            boolean validInput = false;

            // Loop until valid input is received
            while (!validInput) {
                try {
                    System.out.print("Row: ");
                    row = scanner.nextInt();
                    System.out.print("Col: ");
                    col = scanner.nextInt();
                    validInput = true; // Input format is correct
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Invalid input. Please enter numbers for row and column.");
                    scanner.next(); // Consume the invalid input
                }
            }

            // Process the move if it's valid
            if (board.isValidMove(row, col)) {
                processMove(row, col);
            } else {
                // If move is invalid, the current player tries again
                System.out.println("Please try again.");
            }
        }

        // Game has ended, print final board and result
        board.printBoard();
        switch (currentState) {
            case PLAYER_X_WINS:
                System.out.println("Game Over! " + player1.getName() + " (" + player1.getMark() + ") wins!");
                break;
            case PLAYER_O_WINS:
                System.out.println("Game Over! " + player2.getName() + " (" + player2.getMark() + ") wins!");
                break;
            case DRAW:
                System.out.println("Game Over! It's a Draw!");
                break;
            default:
                // Should not happen
                break;
        }
        scanner.close(); // Close the scanner when the game ends
    }

    /**
     * Processes a player's move: places the mark, checks for win/draw, and switches player.
     * @param row The row where the mark is placed.
     * @param col The column where the mark is placed.
     */
    private void processMove(int row, int col) {
        board.placeMark(row, col, currentPlayer.getMark());
        movesCount++;

        if (checkForWin(row, col, currentPlayer.getMark())) {
            currentState = (currentPlayer.getMark() == 'X') ? GameState.PLAYER_X_WINS : GameState.PLAYER_O_WINS;
        } else if (board.isFull()) {
            currentState = GameState.DRAW;
        } else {
            switchPlayer(); // Only switch if game is still in progress
        }
    }

    /**
     * Switches the current player.
     */
    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        System.out.println(currentPlayer.getName() + "'s turn.");
    }

    /**
     * Checks if the last move resulted in a win for the current player.
     * This is optimized to only check the row, column, and diagonals affected by the last move.
     * @param row The row of the last move.
     * @param col The column of the last move.
     * @param mark The mark of the current player.
     * @return true if the current player has won, false otherwise.
     */
    private boolean checkForWin(int row, int col, char mark) {
        // Check row
        if (checkLine(row, -1, 0, mark)) return true; // -1 for column means check entire row

        // Check column
        if (checkLine(-1, col, 1, mark)) return true; // -1 for row means check entire column

        // Check main diagonal (top-left to bottom-right)
        if (row == col) {
            if (checkLine(0, 0, 2, mark)) return true; // 2 for main diagonal
        }

        // Check anti-diagonal (top-right to bottom-left)
        if (row + col == board.getSize() - 1) {
            if (checkLine(0, board.getSize() - 1, 3, mark)) return true; // 3 for anti-diagonal
        }

        return false;
    }

    /**
     * Helper method to check a line (row, column, or diagonal) for a win.
     * @param startRow For row check, this is the row index. For column/diagonal, it's a starting point.
     * @param startCol For column check, this is the col index. For row/diagonal, it's a starting point.
     * @param checkType 0 for row, 1 for column, 2 for main diagonal, 3 for anti-diagonal.
     * @param mark The mark to check for.
     * @return true if the line contains 3 consecutive marks, false otherwise.
     */
    private boolean checkLine(int startRow, int startCol, int checkType, char mark) {
        int count = 0;
        int size = board.getSize();

        if (checkType == 0) { // Check row
            for (int c = 0; c < size; c++) {
                if (board.getMark(startRow, c) == mark) {
                    count++;
                } else {
                    count = 0; // Reset count if mark doesn't match
                }
                if (count == size) return true;
            }
        } else if (checkType == 1) { // Check column
            for (int r = 0; r < size; r++) {
                if (board.getMark(r, startCol) == mark) {
                    count++;
                } else {
                    count = 0; // Reset count
                }
                if (count == size) return true;
            }
        } else if (checkType == 2) { // Check main diagonal
            for (int i = 0; i < size; i++) {
                if (board.getMark(i, i) == mark) {
                    count++;
                } else {
                    count = 0; // Reset count
                }
                if (count == size) return true;
            }
        } else if (checkType == 3) { // Check anti-diagonal
            for (int i = 0; i < size; i++) {
                if (board.getMark(i, size - 1 - i) == mark) {
                    count++;
                } else {
                    count = 0; // Reset count
                }
                if (count == size) return true;
            }
        }
        return false;
    }
}

// --- Main Simulation Class ---

/**
 * Main class to run the Tic-Tac-Toe game simulation.
 */
public class TicTacToeSimulation {
    public static void main(String[] args) {
        // Define players
        Player playerX = new Player("Player 1", 'X');
        Player playerO = new Player("Player 2", 'O');

        // Create a new Tic-Tac-Toe game instance (3x3 board)
        TicTacToeGame game = new TicTacToeGame(3, playerX, playerO);

        // Start the game
        game.start();
    }
}

