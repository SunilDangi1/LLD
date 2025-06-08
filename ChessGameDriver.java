

// --- Enums for constants ---

enum Color {
    WHITE, BLACK
}

enum GameStatus {
    ACTIVE, CHECK, CHECKMATE, STALEMATE
}

enum PieceType {
    KING, QUEEN, ROOK, BISHOP, KNIGHT, PAWN
}


// --- Core Data Structures ---

/**
 * Represents a single square on the board.
 */
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


/**
 * Abstract class for all chess pieces.
 */
abstract class Piece {
    protected final Color color;
    protected final PieceType type;
    protected boolean hasMoved;

    public Piece(Color color, PieceType type) {
        this.color = color;
        this.type = type;
        this.hasMoved = false;
    }

    public Color getColor() {
        return color;
    }

    public PieceType getType() {
        return type;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    /**
     * Checks if this piece can legally move from start to end spot.
     * This is an abstract method to be implemented by each piece subclass.
     * Note: This method does NOT check for conditions like checkmate.
     */
    public abstract boolean canMove(Board board, Spot start, Spot end);
}


// --- Concrete Piece Implementations ---

class King extends Piece {
    public King(Color color) {
        super(color, PieceType.KING);
    }
    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // A king can move one square in any direction.
        int dx = Math.abs(start.getX() - end.getX());
        int dy = Math.abs(start.getY() - end.getY());
        if (dx <= 1 && dy <= 1) {
            // TODO: Implement castling logic
            // TODO: Check if the move puts the king in check
            return true;
        }
        return false;
    }
}

class Rook extends Piece {
    public Rook(Color color) {
        super(color, PieceType.ROOK);
    }
    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // A rook can move horizontally or vertically.
        // It cannot jump over other pieces.
        if (start.getX() == end.getX()) { // Vertical move
            // TODO: Check if path is clear
            return true;
        }
        if (start.getY() == end.getY()) { // Horizontal move
            // TODO: Check if path is clear
            return true;
        }
        return false;
    }
}

class Knight extends Piece {
    public Knight(Color color) {
        super(color, PieceType.KNIGHT);
    }
    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // A knight moves in an 'L' shape: 2 squares in one direction and 1 in a perpendicular direction.
        int dx = Math.abs(start.getX() - end.getX());
        int dy = Math.abs(start.getY() - end.getY());
        return (dx == 1 && dy == 2) || (dx == 2 && dy == 1);
    }
}

class Pawn extends Piece {
    public Pawn(Color color) {
        super(color, PieceType.PAWN);
    }
    @Override
    public boolean canMove(Board board, Spot start, Spot end) {
        // TODO: Implement pawn's unique logic:
        // 1. Forward move (1 square)
        // 2. First move (2 squares)
        // 3. Diagonal capture
        // 4. En passant
        // 5. Promotion
        return true; // Simplified for now
    }
}

// NOTE: Bishop and Queen would be implemented similarly.


// --- Player and Board Management ---

/**
 * Represents a player in the game.
 */
class Player {
    private final Color color;

    public Player(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}

/**
 * Represents the chessboard and its state.
 */
class Board {
    private final Spot[][] spots;

    public Board() {
        this.spots = new Spot[8][8];
        resetBoard();
    }

    public Spot getSpot(int x, int y) {
        if (x < 0 || x > 7 || y < 0 || y > 7) {
            return null; // Out of bounds
        }
        return spots[x][y];
    }

    public void resetBoard() {
        // Initialize white pieces
        spots[0][0] = new Spot(0, 0, new Rook(Color.WHITE));
        spots[0][1] = new Spot(0, 1, new Knight(Color.WHITE));
        // ... and so on for all white pieces
        for (int i = 0; i < 8; i++) {
            spots[1][i] = new Spot(1, i, new Pawn(Color.WHITE));
        }
        spots[0][7] = new Spot(0, 7, new Rook(Color.WHITE));
        // ... King, Queen, Bishop

        // Initialize black pieces
        spots[7][0] = new Spot(7, 0, new Rook(Color.BLACK));
        spots[7][1] = new Spot(7, 1, new Knight(Color.BLACK));
        // ... and so on for all black pieces
        for (int i = 0; i < 8; i++) {
            spots[6][i] = new Spot(6, i, new Pawn(Color.BLACK));
        }
        spots[7][7] = new Spot(7, 7, new Rook(Color.BLACK));
        // ... King, Queen, Bishop

        // Initialize empty spots
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                spots[i][j] = new Spot(i, j, null);
            }
        }
    }
}


// --- Main Game Controller ---

/**
 * The main orchestrator of the game.
 */
class Game {
    private final Board board;
    private final Player[] players;
    private Player currentPlayer;
    private GameStatus status;

    public Game() {
        this.board = new Board();
        this.players = new Player[2];
        this.players[0] = new Player(Color.WHITE);
        this.players[1] = new Player(Color.BLACK);
        this.currentPlayer = players[0]; // White starts
        this.status = GameStatus.ACTIVE;
    }

    /**
     * Attempts to make a move for the current player.
     * @return true if the move was successful, false otherwise.
     */
    public boolean playerMove(int startX, int startY, int endX, int endY) {
        Spot startSpot = board.getSpot(startX, startY);
        Spot endSpot = board.getSpot(endX, endY);

        // 1. Basic validation
        if (startSpot == null || endSpot == null || startSpot.getPiece() == null) {
            System.out.println("Invalid start or end spot.");
            return false;
        }

        Piece sourcePiece = startSpot.getPiece();
        if (sourcePiece.getColor() != currentPlayer.getColor()) {
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
        if (destPiece != null && destPiece.getColor() == currentPlayer.getColor()) {
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
            System.out.println(sourcePiece.getType() + " captures " + destPiece.getType());
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

    private void switchPlayer() {
        this.currentPlayer = (this.currentPlayer.getColor() == Color.WHITE) ? players[1] : players[0];
        System.out.println("Next turn: " + this.currentPlayer.getColor());
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}

// --- Driver Class to run the game ---
public class ChessGameDriver {
    public static void main(String[] args) {
        Game game = new Game();
        System.out.println("Game started. Current player: " + game.getCurrentPlayer().getColor());

        // Example move: White moves a knight from (0,1) to (2,2)
        // In a real application, these inputs would come from a UI.
        System.out.println("\nAttempting to move Knight from (0,1) to (2,2)...");
        game.playerMove(0, 1, 2, 2);

        // Example invalid move: Black tries to move White's piece
        System.out.println("\nAttempting to move Knight from (2,2) with Black's turn...");
        game.playerMove(2, 2, 4, 3); // Fails because it's Black's turn

        // Example move: Black moves a knight from (7,1) to (5,2)
        System.out.println("\nAttempting to move Knight from (7,1) to (5,2)...");
        game.playerMove(7, 1, 5, 2);
    }
}