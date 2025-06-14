package TicTocGame;

import java.util.Scanner;

enum GameState{
    IN_PROGRESS,
    PLAYER_X_WINS,
    PLAYER_O_WINS,
    DRAW
}
class Board {
    private int size ;
    private char[][] cells;
    
    public Board(int size){
        this.size = size;
        this.cells = new char[size][size];
        intialize();
    }
    private void intialize(){
        for(int i = 0;i<size;i++){
            for(int j = 0;j<size;j++){
                cells[i][j] = ' ';
            }
        }
    }
    public boolean isValidMove(int row, int col){
        if(row >= size || row < 0 || col >=size || col <0 || cells[row][col]!=' '){
            System.out.println("Invalid move: Row or column out of bounds.");
            return false;
        }
            
        return true;
    }
    public void placeMark(int row, int col, char mark){
        cells[row][col] = mark;

    }
    public char getMark(int row, int col){
        return cells[row][col];
    }
    public boolean isFull(){
        for(int i = 0;i<size;i++){
            for(int j = 0;j<size;j++){
                if(cells[i][j] == ' ')
                    return false;
            }
        }
        return true;
    }
    public int getSize(){
        return size;
    }
    public void printBoard(){
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
    
}
interface MoveStrategy {

    int[] makemove(Board board, char mark);
}
class HumanStrategy implements MoveStrategy{
    private Scanner scanner;

    public HumanStrategy(Scanner scanner){
        this.scanner = scanner;
    }
    
    @Override
    public int[] makemove(Board board, char mark) {

        int row =-1;
        int col = -1;
        boolean validInput = false;
        // board.printBoard();
       while (!validInput) {
        try {
            System.out.print("Row: ");
            row = scanner.nextInt();
            System.out.print("Col: ");
            col = scanner.nextInt();
            validInput = true; 
        } catch (Exception e) {
            System.out.println("Invalid input. Please enter numbers for row and column.");
            scanner.next(); // Consume the invalid input
        }
       }
        return new int[]{row,col};
    }
    
}
class Player{
    private String name;
    private char mark;
    private MoveStrategy moveStrategy;
    public Player (String name, char mark, MoveStrategy moveStrategy){
        this.name = name;
        this.mark = mark;
        this.moveStrategy = moveStrategy;
    }
    public String getName(){
        return name;
    } 

    public char getMark(){
        return mark;
    }

    public int[] getMove(Board board){
        return moveStrategy.makemove(board, this.mark);
    }

}
class TicTacToeGame{
    private Board board;
    private int size;
    private Player p1;
    private Player p2;
    private int movesCount;
    private Player currentPlayer;
    private GameState currGameState;
    public Scanner scanner;
    public TicTacToeGame(int size, Player p1, Player p2){
        this.board =  new Board(size);
        this.size = size;
        this.p1 =p1;
        this.p2 = p2;
        this.currGameState = GameState.IN_PROGRESS;
        this.currentPlayer = p1;
        this.movesCount = 0;
        this.scanner = new Scanner(System.in);
        System.out.println("Tic-Tac-Toe game initialized!");
    }

    
    public void start(){
        while (currGameState==GameState.IN_PROGRESS) {
            board.printBoard();
            System.out.println(currentPlayer.getName() + "'s turn (" + currentPlayer.getMark() + "). Enter row and column (0-" + (board.getSize() - 1) + "):");

            // int row = -1;
            // int col = -1;
            // boolean validInput = false;
            //   while (!validInput) {
            //     try {
            //         System.out.print("Row: ");
            //         row = scanner.nextInt();
            //         System.out.print("Col: ");
            //         col = scanner.nextInt();
            //         validInput = true; // Input format is correct
            //     } catch (java.util.InputMismatchException e) {
            //         System.out.println("Invalid input. Please enter numbers for row and column.");
            //         scanner.next(); // Consume the invalid input
            //     }
            // }
            
            int[] move;
            boolean moveMode =  false;
            while(!moveMode){
                move = currentPlayer.getMove(board);
                int row = move[0];
                int col = move[1];

                if (board.isValidMove(row, col)) {
                processMove(row, col);
                moveMode = true;
                } else {
                   // If move is invalid, the current player tries again
                    System.out.println("Please try again.");
                }
            }

            

            board.printBoard();
            switch (currGameState) {
                case PLAYER_X_WINS:
                System.out.println("Game Over! " + p2.getName() + " (" + p2.getMark() + ") wins!");
                break;
            case PLAYER_O_WINS:
                System.out.println("Game Over! " + p1.getName() + " (" + p1.getMark() + ") wins!");
                break;
            case DRAW:
                System.out.println("Game Over! It's a Draw!");
                break;
            default:
                // Should not happen
                break;
            }

        }
        scanner.close();
    }
    
    private void switchPlayer(){
        currentPlayer = (currentPlayer == p1) ? p2 : p1; 
        System.out.println(currentPlayer.getName() + "'s turn.");
    }
    
    private void processMove(int rol , int col ){
        board.placeMark(rol, col, currentPlayer.getMark());
        movesCount++;

        if(checkForWin(rol, col, currentPlayer.getMark())){
            currGameState = (currentPlayer.getMark()=='X') ? GameState.PLAYER_X_WINS : GameState.PLAYER_O_WINS;
        }
        else if(board.isFull())
        {
            currGameState = GameState.DRAW;
        }
        else{
            switchPlayer();
        }
    }

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
public class MyTicTacSimulation {
    public static void  main(String[] args){
        Scanner scanner = new Scanner(System.in);
        Player P0 = new Player("Player 1",'O', new HumanStrategy(scanner));
        Player PX = new Player("Player 2",'X', new HumanStrategy(scanner));

        TicTacToeGame game = new TicTacToeGame(3, P0, PX);
        game.start();
        scanner.close();
    }
}
