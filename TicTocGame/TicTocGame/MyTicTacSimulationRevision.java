package TicTocGame;

import java.util.ArrayList;
import java.util.Scanner;

enum GameState{
    IN_PROG, DRAW, X_WIN, O_WIN
}
class Board{
    private char[][] cells;
    int size;
    public Board(){
        this.size = 3;
        this.cells =  new char[size][size];
        for(int i = 0;i<size;i++){
            for(int j = 0;j<size;j++){
                cells[i][j] = ' ';
            }
        }
    }
    public boolean isValidMove(int row, int col){
        if(row> size && row<0 && col > size && col <0) return false;
        return true;
    }
    public void printBoard(){
        for(int i = 0;i<size;i++){
            for(int j = 0;j<size;j++)
            {
                System.out.print(cells[i][j]+" ");
            }
            System.out.println("+++++++++++++");
        }
    }

}
class Player{
    private String name ;
    public char symbol;
    public MoveStategy moveStategy;
    public Player(String name, char symbol, MoveStategy moveStategy){
        this.name = name;
        this.symbol = symbol;
        this.moveStategy = moveStategy;
    }
    int [] getMove(Board board, char symbol){
        return moveStategy.makeMove(board, symbol);
    }

}
interface MoveStategy{
    int[] makeMove(Board board, char symbol);
}
class HumanStrategy implements MoveStategy{
    private Scanner scanner;
    public HumanStrategy(){
        this.scanner = scanner;
    }
    @Override
    public int[] makeMove(Board board, char symbol){
        int row = -1;
        int col = -1;
        boolean isValidMove = false;
        while(!isValidMove){
            try {
            System.out.println("inout x:  and  y: ");
             row = scanner.nextInt();
             col = scanner.nextInt();
             isValidMove = true;
            } catch (Exception e) {
               System.out.println("invalid input...try again");
               scanner.next();
            }  
        }
        return new int[]{row,col};
    }

}

class TicTocGame{
    private Board board;
    private Player player1;
    private Player player2;
    public Player currPlayer;
    public GameState currGameState;
    public TicTocGame(Player p1, Player p2){
        this.player1 = p1;
        this.player2 = p2;
        this.currPlayer = this.player1;
        this.currGameState = GameState.IN_PROG;
        this.board = new Board();
    } 
    void start(){
        while(currGameState.equals(GameState.IN_PROG)){
            boolean isValidMove =  false;
            while(!isValidMove){
                try {
                    int[] move = currPlayer.getMove(board, currPlayer.symbol);
                    isValidMove = board.isValidMove(move[0], move[1]);
                    processMove(move[0],move[1]);
                } catch (Exception e) {
                    System.out.println("invalid move");
                }
                
            }
            
        }
        void processMove(int r, int c){
            
        }
    }
}
public class MyTicTacSimulationRevision {
    public static void main(String[] args) {
        Player p1 = new Player("Sunil", 'O', new HumanStrategy());
        Player p2 = new Player("Siya", 'X', new HumanStrategy());
        TicTocGame game = new TicTocGame(p1, p2);
        game.start();
    }
    }
}
