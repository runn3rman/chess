package chess;

import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard board;
    private TeamColor currentTeamTurn;
    public ChessGame() {
        //this.currentTeamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.currentTeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTeamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            // No piece at the given position
            return null;
        }

        return piece.pieceMoves(board, startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null || piece.getTeamColor() != currentTeamTurn) {
            throw new InvalidMoveException("Invalid move: No piece at the starting position or not your turn.");
        }

        if (!isValidDestination(move, piece)) {
            throw new InvalidMoveException("Invalid move: Destination is not valid for the given piece.");
        }

        // If there's a piece at the destination position, it's captured (can add logic to keep track of captured pieces if needed)
        ChessPiece pieceAtDestination = board.getPiece(move.getEndPosition());
        if (pieceAtDestination != null && pieceAtDestination.getTeamColor() != piece.getTeamColor()) {
            // Capture the piece (in this basic implementation, just remove it from the board)
            board.addPiece(move.getEndPosition(), null);
        }

        // Move the piece to the new position
        board.addPiece(move.getStartPosition(), null); // Remove from the original position
        board.addPiece(move.getEndPosition(), piece); // Place at the new position

        // Switch turn to the other player
        currentTeamTurn = (currentTeamTurn == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    private boolean isValidDestination(ChessMove move, ChessPiece piece) {
        // Implement the logic to check if the destination is a valid move for the piece
        // use the piece.pieceMoves() method
        // replace with real validation logic
        return true;
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor);
        if (kingPosition == null) {
            // King not found, should not happen if the board is in a valid state
            return false;
        }

        // Check if any of the opposing team's pieces can capture the King
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    // It's an opposing piece, check its valid moves
                    Collection<ChessMove> moves = validMoves(position);
                    for (ChessMove move : moves) {
                        if (move.getEndPosition().equals(kingPosition)) {
                            // King can be captured by this move
                            return true;
                        }
                    }
                }
            }
        }

        // No opposing pieces can capture the King
        return false;
    }

    private ChessPosition findKingPosition(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition position = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    return position; // Found the King
                }
            }
        }
        return null; // King not found
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
