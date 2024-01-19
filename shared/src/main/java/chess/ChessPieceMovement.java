package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
public abstract class ChessPieceMovement {
    protected ChessBoard board;
    protected ChessPosition position;

    public ChessPieceMovement(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public abstract Collection<ChessMove> pieceMoves();
}

class PawnMovement extends ChessPieceMovement {
    public PawnMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece currentPawn = board.getPiece(position);
        int direction = currentPawn.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        int startRow = currentPawn.getTeamColor() == ChessGame.TeamColor.WHITE ? 2 : 7;
        int promotionRow = currentPawn.getTeamColor() == ChessGame.TeamColor.WHITE ? 8 : 1;

        // Forward move (1 square)
        addMoveIfValid(moves, position.getRow() + direction, position.getColumn(), false, promotionRow);

        // Initial double move
        if (position.getRow() == startRow && isPathClear(direction)) {
            addMoveIfValid(moves, position.getRow() + 2 * direction, position.getColumn(), true, promotionRow);
        }

        // Capture moves (diagonal) and promotion captures
        addCaptureMoveIfValid(moves, position.getRow() + direction, position.getColumn() + 1, promotionRow);
        addCaptureMoveIfValid(moves, position.getRow() + direction, position.getColumn() - 1, promotionRow);

        return moves;
    }

    private void addMoveIfValid(Collection<ChessMove> moves, int row, int col, boolean isDoubleMove, int promotionRow) {
        if (row <= 0 || row > 8 || col <= 0 || col > 8 || board.getPiece(new ChessPosition(row, col)) != null) {
            return;
        }
        if (row == promotionRow) {
            // Add a move for each possible promotion type
            for (ChessPiece.PieceType promotionType : ChessPiece.PieceType.values()) {
                if (promotionType != ChessPiece.PieceType.PAWN && promotionType != ChessPiece.PieceType.KING) {
                    moves.add(new ChessMove(position, new ChessPosition(row, col), promotionType));
                }
            }
        } else {
            moves.add(new ChessMove(position, new ChessPosition(row, col), null)); // No promotion
        }
    }

    private void addCaptureMoveIfValid(Collection<ChessMove> moves, int row, int col, int promotionRow) {
        if (row <= 0 || row > 8 || col <= 0 || col > 8) {
            return;
        }
        ChessPiece targetPiece = board.getPiece(new ChessPosition(row, col));
        if (targetPiece != null && targetPiece.getTeamColor() != board.getPiece(position).getTeamColor()) {
            if (row == promotionRow) {
                // Add a capture move for each possible promotion type
                for (ChessPiece.PieceType promotionType : ChessPiece.PieceType.values()) {
                    if (promotionType != ChessPiece.PieceType.PAWN && promotionType != ChessPiece.PieceType.KING) {
                        moves.add(new ChessMove(position, new ChessPosition(row, col), promotionType));
                    }
                }
            } else {
                moves.add(new ChessMove(position, new ChessPosition(row, col), null)); // No promotion
            }
        }
    }

    private boolean isPathClear(int direction) {
        ChessPosition positionInFront = new ChessPosition(position.getRow() + direction, position.getColumn());
        return board.getPiece(positionInFront) == null;
    }
}



