package ChessGame;


enum GameState {
    ACTIVE,
    DRAW,
    CHECKMATE
}

enum PieceColor {
    WHITE,
    BLACK
}

enum PieceType {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
}

class Game {
    private final Board board;
    private final Player[] player;
    private GameState state;
    private Player currentPlayer;

    public Game() {
        this.board = new Board();
        this.player = new Player[2];
        player[0] = new Player(PieceColor.WHITE);
        player[1] = new Player(PieceColor.BLACK);
        this.state = GameState.ACTIVE;
    }

    public boolean makeMove() {
        return true;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private void switchPlayer() {
        this.currentPlayer = (this.currentPlayer.getColor() == PieceColor.WHITE) ? player[1] : player[0] ;
        System.out.println("Next turn: " + this.currentPlayer.getColor());
    }

    public boolean playerMove(int firtsX, int firstY, int secondX, int secondY){
        Spot startSpot = board.getSpot(firtsX, firstY);
        Spot endSpot = board.getSpot(secondX, secondY);

        if(startSpot==null || endSpot==null || startSpot.getPiece()==null)
        {
            System.out.println("not valid input");
            return false;
        }
        Piece sourcePiece = startSpot.getPiece();
        if (sourcePiece.getPieceColor() != currentPlayer.getColor()) {
            System.out.println("Not your piece to move.");
            return false;
        }

        // 2. Check if the move is valid for the specific piece
        if (!sourcePiece.canMove(board, startSpot, endSpot)) {
            System.out.println("Invalid move for this piece.");
            return false;
        }
        
        // 3. Check if the destination has a friendly piece
        Piece destPiece = endSpot.getPiece();
        if (destPiece != null && destPiece.getPieceColor() == currentPlayer.getColor()) {
            System.out.println("Cannot capture your own piece.");
            return false;
        }

        // 4. More advanced validation would go here:
        // - Check if the move puts the current player's king in check.
        // This is a complex check that often involves simulating the move.
        // For simplicity, we are skipping it.

        // 5. Make the move
        if (destPiece != null) {
            // Handle capture
            System.out.println(sourcePiece.getPieceType() + " captures " + destPiece.getPieceType());
        }
        endSpot.setPiece(sourcePiece);
        startSpot.setPiece(null);
        sourcePiece.setHasMoved(true);

        // 6. Update game state
        // - Check for check/checkmate on the opponent.
        // - For now, just switch the player.
        this.switchPlayer();
        
        System.out.println("Move successful.");
        return true;
    }
}

class Board {
    private Spot[][] spots;

    public Board() {
        spots = new Spot[8][8];
        resetBoard();
    }

    public Spot getSpot(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7)
            return null;
        return spots[x][y];
    }

    public void resetBoard(){

        spots[0][4] = new Spot(0,0, new King(PieceColor.WHITE));
        spots[7][3] = new Spot(0,0, new King(PieceColor.BLACK));

        for(int i = 0;i<8;i++){
            spots[1][i] = new Spot(0, i, new Pawn(PieceColor.WHITE));
            spots[6][i] = new Spot(6, i, new Pawn(PieceColor.BLACK));
        }

    }

}

class Player {
    private final PieceColor pieceColor;
    public Player(PieceColor pieceColor){
        this.pieceColor =pieceColor;
    }

    public PieceColor getColor(){
        return this.pieceColor;
    }

}

class Spot {
    private final int x;
    private final int y;
    private Piece piece;

    public Spot(int x, int y, Piece piece) {
        this.x = x;
        this.y = y;
        this.piece = piece;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void setPiece(Piece p) {
        this.piece = p;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}

abstract class Piece {
    private PieceColor pieceColor;
    private PieceType pieceType;
    private boolean hasMoved;

    public Piece(PieceColor pieceColor, PieceType pieceType) {
        this.pieceColor = pieceColor;
        this.pieceType = pieceType;
        hasMoved = false;
    }

    public PieceColor getPieceColor() {
        return pieceColor;
    }

    public void setPieceColor(PieceColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(PieceType pieceType) {
        this.pieceType = pieceType;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }
    
    public abstract boolean canMove(Board board, Spot start, Spot end);
}

class King extends Piece {
    public King(PieceColor pieceColor){
        super(pieceColor, PieceType.KING);
    }
    @Override
    public boolean canMove(Board board, Spot start, Spot end){
        return true;
    }
}

class Pawn extends Piece {
    public Pawn(PieceColor pieceColor){
        super(pieceColor, PieceType.PAWN);
    }

    public boolean canMove(Board board, Spot start, Spot end){
        return true;
    }
    
}

public class ChessGame {

    public static void main(String[] args) {
            Game game = new Game();

    game.playerMove(0,1,2,2);
    game.playerMove(2,2,4,3);

    }

}
