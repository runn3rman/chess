package chess;
import java.util.ArrayList;
import java.util.Collection;
public abstract class ChessPieceMovement {
    protected ChessBoard board;
    protected ChessPosition position;

    public ChessPieceMovement(ChessBoard board, ChessPosition position) {
        this.board = board;
        this.position = position;
    }

    public abstract Collection<ChessMove> pieceMoves();

    protected void generateDirectionalMoves(Collection<ChessMove> possibilities, int[][] directions) {
        for (int[] direction : directions) {
            int currentRow = position.getRow();
            int currentColumn = position.getColumn();
            while (true) {
                currentRow += direction[0];
                currentColumn += direction[1];
                ChessPosition newPosition = new ChessPosition(currentRow, currentColumn);
                if (!addMoveIfBlocked(possibilities, newPosition)) {
                    break;
                }
            }
        }
    }
    private boolean addMoveIfBlocked(Collection<ChessMove> possibilities, ChessPosition newPosition) {
        if (newPosition.getRow() <= 0 || newPosition.getRow() > 8 || newPosition.getColumn() <= 0 || newPosition.getColumn() > 8) {
            return false;
        }
        ChessPiece pieceAtNewPosition = board.getPiece(newPosition);
        if (pieceAtNewPosition != null) {
            if (pieceAtNewPosition.getTeamColor() != this.board.getPiece(this.position).getTeamColor()) {
                possibilities.add(new ChessMove(this.position, newPosition, null));
            }
            return false;
        }
        possibilities.add(new ChessMove(this.position, newPosition, null));
        return true;
    }
}
class RookMovement extends ChessPieceMovement {
    public RookMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> possibilities = new ArrayList<>();
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}}; // Cross directions
        generateDirectionalMoves(possibilities, directions);
        return possibilities;
    }
}
class BishopMovement extends ChessPieceMovement {
    public BishopMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> possibilities = new ArrayList<>();
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}}; // Diagonal directions
        generateDirectionalMoves(possibilities, directions);
        return possibilities;
    }
}
class QueenMovement extends ChessPieceMovement {
    public QueenMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> possibilities = new ArrayList<>();
        int[][] directions = {{1, 1}, {1, -1}, {-1, 1}, {-1, -1}, {0, 1}, {1, 0}, {-1, 0}, {0, -1}}; // Cross and Diagonal directions
        generateDirectionalMoves(possibilities, directions);
        return possibilities;
    }
}
class PawnMovement extends ChessPieceMovement {
    public PawnMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> possibilities = new ArrayList<>();
        ChessPiece currentPawn = board.getPiece(position);
        int direction = currentPawn.getTeamColor() == ChessGame.TeamColor.WHITE ? 1 : -1;
        int startRow = currentPawn.getTeamColor() == ChessGame.TeamColor.WHITE ? 2 : 7;
        int promotionRow = currentPawn.getTeamColor() == ChessGame.TeamColor.WHITE ? 8 : 1;

        // Forward move (1)
        addAValidMove(possibilities, position.getRow() + direction, position.getColumn(), promotionRow);

        // Initial double move
        if (position.getRow() == startRow && isPathClear(direction)) {
            addAValidMove(possibilities, position.getRow() + 2 * direction, position.getColumn(), promotionRow);
        }

        // Capture possibilities (diagonal) and promotion captures
        addCaptureMoveIfValid(possibilities, position.getRow() + direction, position.getColumn() + 1, promotionRow);
        addCaptureMoveIfValid(possibilities, position.getRow() + direction, position.getColumn() - 1, promotionRow);

        return possibilities;
    }

    private void addAValidMove(Collection<ChessMove> possibilities, int row, int col, int promotionRow) {
        if (row <= 0 || row > 8 || col <= 0 || col > 8 || board.getPiece(new ChessPosition(row, col)) != null) {
            return;
        }
        addPromotionMove(possibilities, row, col, promotionRow);
    }

    private void addPromotionMove(Collection<ChessMove> possibilities, int row, int col, int promotionRow) {
        if (row == promotionRow) {
            // Add a move for each possible promotion type
            for (ChessPiece.PieceType promotionType : ChessPiece.PieceType.values()) {
                if (promotionType != ChessPiece.PieceType.PAWN && promotionType != ChessPiece.PieceType.KING) {
                    possibilities.add(new ChessMove(position, new ChessPosition(row, col), promotionType));
                }
            }
        } else {
            possibilities.add(new ChessMove(position, new ChessPosition(row, col), null)); // No promotion
        }
    }

    private void addCaptureMoveIfValid(Collection<ChessMove> possibilities, int row, int col, int promotionRow) {
        if (row <= 0 || row > 8 || col <= 0 || col > 8) {
            return;
        }
        ChessPiece targetPiece = board.getPiece(new ChessPosition(row, col));
        if (targetPiece != null && targetPiece.getTeamColor() != board.getPiece(position).getTeamColor()) {
            addPromotionMove(possibilities, row, col, promotionRow);
        }
    }

    private boolean isPathClear(int direction) {
        ChessPosition positionInFront = new ChessPosition(position.getRow() + direction, position.getColumn());
        return board.getPiece(positionInFront) == null;
    }
}
class KnightMovement extends ChessPieceMovement {
    public KnightMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> possibilities = new ArrayList<>();
        int[][] directions = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}}; // knight directions

        for (int[] direction : directions) {
            int currentRow = position.getRow() + direction[0];
            int currentColumn = position.getColumn() + direction[1];

            if (currentRow > 0 && currentRow <= 8 && currentColumn > 0 && currentColumn <= 8) {
                ChessPosition newPosition = new ChessPosition(currentRow, currentColumn);
                ChessPiece pieceAtNewPosition = board.getPiece(newPosition);

                if (pieceAtNewPosition == null || pieceAtNewPosition.getTeamColor() != this.board.getPiece(this.position).getTeamColor()) {
                    possibilities.add(new ChessMove(this.position, newPosition, null));
                }
            }
        }

        return possibilities;
    }
}
class KingMovement extends ChessPieceMovement {
    public KingMovement(ChessBoard board, ChessPosition position) {
        super(board, position);
    }

    @Override
    public Collection<ChessMove> pieceMoves() {
        Collection<ChessMove> possibilities = new ArrayList<>();

        int[][] directions = {
                {1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}
        }; // All 8 directions around the king

        for (int[] direction : directions) {
            int currentRow = position.getRow() + direction[0];
            int currentColumn = position.getColumn() + direction[1];
            if (currentRow <= 0 || currentRow > 8 || currentColumn <= 0 || currentColumn > 8) {
                continue; // Skip if it's off the board
            }
            ChessPosition newPosition = new ChessPosition(currentRow, currentColumn);
            ChessPiece pieceAtNewPosition = board.getPiece(newPosition);
            if (pieceAtNewPosition != null) {
                // If the piece is of the same color, it's blocked
                if (pieceAtNewPosition.getTeamColor() == this.board.getPiece(this.position).getTeamColor()) {
                    continue;
                }
                // If the piece is of the opposite color, it can be captured
                possibilities.add(new ChessMove(this.position, newPosition, null));
                continue;
            }
            // Add the move if the square is empty
            possibilities.add(new ChessMove(this.position, newPosition, null));
        }
        return possibilities;
    }
}