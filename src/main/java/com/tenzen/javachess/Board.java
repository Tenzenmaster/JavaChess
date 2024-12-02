package com.tenzen.javachess;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Board extends Group {
    public final static String STARTING_FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";
    private final static Paint LIGHT_COLOR = Color.BISQUE;
    private final static Paint DARK_COLOR = Color.BURLYWOOD;
    private final static Paint LIGHT_COLOR_HL = Color.LIGHTYELLOW;
    private final static Paint DARK_COLOR_HL = Color.YELLOW;
    private Position selectedPosition = null;
    private final Square[][] squares = new Square[8][8];
    private final Piece[][] pieces = new Piece[8][8];
    public Game game;

    public Board() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                int x = (i * 8 + j) + (i % 2 == 0 ? 0 : 1);
                Square square = new Square(new Position(i, j), x % 2 == 0 ? LIGHT_COLOR : DARK_COLOR);
                setSquare(new Position(i, j), square);
                Logger.trace(square.toString());
            }
        }
    }

    public void loadFen(String fen) {
        clearPieces();

        String[] split = fen.split(" ");
        String[] piecesSplit = split[0].split("/");

        int rank = 0;
        for (String pieceString : piecesSplit) {
            int file = 0;
            for (char ch : pieceString.toCharArray()) {
                if (Character.isDigit(ch)) {
                    file += Character.getNumericValue(ch);
                } else {
                    Position position = new Position(rank, file);
                    Piece piece = Piece.fromChar(ch);
                    setPiece(position, piece);
                    ++file;
                }
            }
            if (file != 8) {
                Logger.warning(String.format("Fen[rank: %d, file: %d] reached", rank, file));
            }
            ++rank;
        }
    }

    public void clearPieces() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                removePiece(new Position(i, j));
            }
        }
    }

    public void setPiece(Position position, Piece piece) {
        removePiece(position);

        piece.setX(position.getFile() * 64);
        piece.setY(position.getRank() * 64);
        piece.setOnMouseClicked(event -> {
            handleClick(position);
        });

        Logger.debug("Set piece: " + piece + " at " + position);

        pieces[position.getRank()][position.getFile()] = piece;
        getChildren().add(piece);
    }

    public boolean removePiece(Position position) {
        Piece piece = getPiece(position);
        if (piece != null) {
            getChildren().remove(piece);
            Logger.debug(piece + " was removed at " + position);
            pieces[position.getRank()][position.getFile()] = null;
            return true;
        } else {
            return false;
        }
    }

    public Piece getPiece(Position position) {
        return pieces[position.getRank()][position.getFile()];
    }

    private void setSquare(Position position, Square square) {
        square.setX(position.getFile() * 64);
        square.setY(position.getRank() * 64);
        square.setOnMouseClicked(event -> {
            handleClick(position);
        });

        squares[position.getRank()][position.getFile()] = square;
        getChildren().add(square);
    }

    private Square getSquare(Position position) {
        return squares[position.getRank()][position.getFile()];
    }

    private void handleClick(Position position) {
        Piece piece = getPiece(position);
        selectSquare(position);
    }

    // Note: Should probably iterate over square 2d array instead
    private void deselectAll() {
        selectedPosition = null;
        for (Node child : getChildren()) {
            if (child instanceof Square square) {
                if (square.getFill() == LIGHT_COLOR_HL) {
                    square.setFill(LIGHT_COLOR);
                } else if (square.getFill() == DARK_COLOR_HL) {
                    square.setFill(DARK_COLOR);
                }
            }
        }
    }

    private void selectSquare(Position position) {
        Logger.debug(position.toString() + "is now");
        if (selectedPosition != null) {
            Piece piece = getPiece(selectedPosition);
            List<Position> moves = getPieceMoves(selectedPosition);
            boolean contains = false;
            for (Position move : moves) {
                if (move.getRank() == position.getRank() && move.getFile() == position.getFile()) {
                    contains = true;
                }
            }

            for (Position pos : moves) {
                Logger.debug("Available move: " + pos.toString());
            }

            if (piece != null && contains) {
                game.requestPossibleMove(selectedPosition, position);
            } else {
                Logger.debug(position.toString() + " not ocntainer");
            }
            deselectAll();
            return;
        }

        deselectAll();
        selectedPosition = position;

        Square square = getSquare(position);
        if (square.getFill() == LIGHT_COLOR) {
            square.setFill(LIGHT_COLOR_HL);
        } else if (square.getFill() == DARK_COLOR) {
            square.setFill(DARK_COLOR_HL);
        }
    }

    public HalfTurn movePiece(Position from, Position to) {
        Piece piece = getPiece(from);
        if (piece == null) {
            return null;
        }

        HalfTurn halfTurn = new HalfTurn();
        halfTurn.from = from;
        halfTurn.to = to;
        halfTurn.kind = piece.getKind();
        halfTurn.color = piece.getColor();
        halfTurn.capture = removePiece(to);

        removePiece(from);
        setPiece(to, piece);

        return halfTurn;
    }

    public List<Position> getPieceMoves(Position position) {
        Piece piece = getPiece(position);
        if (piece == null) {
            return new ArrayList<Position>();
        }

        return switch (piece.getKind()) {
            case Piece.Kind.BISHOP -> getBishopMoves(position, piece.getColor());
            case Piece.Kind.KING -> getKingMoves(position, piece.getColor());
            case Piece.Kind.KNIGHT -> getKnightMoves(position, piece.getColor());
            case Piece.Kind.PAWN -> getPawnMoves(position, piece.getColor());
            case Piece.Kind.QUEEN -> getQueenMoves(position, piece.getColor());
            case Piece.Kind.ROOK -> getRookMoves(position, piece.getColor());
        };
    }

    private List<Position> getMovesInLine(Position start, int addX, int addY, Piece.Color color) {
        List<Position> moves = new ArrayList<Position>();
        Position last = start.copy();
        for (;;) {
            int newRank = last.getRank() + addY;
            int newFile = last.getFile() + addX;
            try {
                last = new Position(newRank, newFile);
            } catch (RuntimeException _) {
                break;
            }

            Piece potentialPiece = getPiece(last);
            if (potentialPiece != null) {
                if (potentialPiece.getColor() != color) {
                    moves.add(last.copy());
                }
                break;
            } else {
                moves.add(last.copy());
            }
        }

        return moves;
    }

    private Position tryGetMove(Position from, int addX, int addY, Piece.Color color) {
        try {
            int newRank = from.getRank() + addY;
            int newFile = from.getFile() + addX;
            Position to = new Position(newRank, newFile);

            Piece piece = getPiece(to);
            if (piece != null) {
                if (piece.getColor() != color) {
                    return to;
                } else {
                    return null;
                }
            } else {
                return to;
            }
        } catch (RuntimeException _) {
            return null;
        }
    }

    private List<Position> getBishopMoves(Position start, Piece.Color color) {
        List<Position> moves = new ArrayList<>();
        moves.addAll(getMovesInLine(start, 1, -1, color));
        moves.addAll(getMovesInLine(start, -1, -1, color));
        moves.addAll(getMovesInLine(start, 1, 1, color));
        moves.addAll(getMovesInLine(start, -1, 1, color));
        return moves;
    }

    private List<Position> getRookMoves(Position start, Piece.Color color) {
        List<Position> moves = new ArrayList<>();
        moves.addAll(getMovesInLine(start, 1, 0, color));
        moves.addAll(getMovesInLine(start, -1, 0, color));
        moves.addAll(getMovesInLine(start, 0, 1, color));
        moves.addAll(getMovesInLine(start, 0, -1, color));
        return moves;
    }

    private List<Position> getQueenMoves(Position start, Piece.Color color) {
        List<Position> moves = new ArrayList<>();
        moves.addAll(getBishopMoves(start, color));
        moves.addAll(getRookMoves(start, color));
        return moves;
    }

    private List<Position> getKnightMoves(Position start, Piece.Color color) {
        List<Position> moves = new ArrayList<>();
        Vec2[] jumps = {
                new Vec2(1, -2),
                new Vec2(1, 2),
                new Vec2(-1, -2),
                new Vec2(-1, 2),
                new Vec2(2, 1),
                new Vec2(2, -1),
                new Vec2(-2, 1),
                new Vec2(-2, -1),
        };
        for (Vec2 jump : jumps) {
            Position position = tryGetMove(start, jump.x, jump.y, color);
            if (position != null) {
                moves.add(position);
            }
        }
        return moves;
    }

    private List<Position> getKingMoves(Position start, Piece.Color color) {
        List<Position> moves = new ArrayList<>();
        Vec2[] jumps = {
                new Vec2(1, -1),
                new Vec2(1, 0),
                new Vec2(1, 1),
                new Vec2(-1, -1),
                new Vec2(-1, 0),
                new Vec2(-1, 1),
                new Vec2(0, -1),
                new Vec2(0, 1),
        };
        for (Vec2 jump : jumps) {
            Position position = tryGetMove(start, jump.x, jump.y, color);
            if (position != null) {
                moves.add(position);
            }
        }
        return moves;
    }

    private List<Position> getPawnMoves(Position start, Piece.Color color) {
        List<Position> moves = new ArrayList<>();
        switch (color) {
            case Piece.Color.BLACK -> {
                Position oneForward = tryGetMove(start, 0, 1, color);
                if (oneForward != null && getPiece(oneForward) == null) {
                    moves.add(oneForward);
                }
                if (start.getRank() == 1) {
                    Position twoForward = tryGetMove(start, 0, 2, color);
                    if (twoForward != null && getPiece(twoForward) == null) {
                        moves.add(twoForward);
                    }
                }
                Position forwardRight = tryGetMove(start, 1, 1, color);
                if (forwardRight != null) {
                    Piece piece = getPiece(forwardRight);
                    if (piece != null) {
                        moves.add(forwardRight);
                    }
                }
                Position forwardLeft = tryGetMove(start, -1, 1, color);
                if (forwardLeft != null) {
                    Piece piece = getPiece(forwardLeft);
                    if (piece != null) {
                        moves.add(forwardLeft);
                    }
                }
            }
            case Piece.Color.WHITE -> {
                Position oneForward = tryGetMove(start, 0, -1, color);
                if (oneForward != null && getPiece(oneForward) == null) {
                    moves.add(oneForward);
                }
                if (start.getRank() == 6) {
                    Position twoForward = tryGetMove(start, 0, -2, color);
                    if (twoForward != null && getPiece(twoForward) == null) {
                        moves.add(twoForward);
                    }
                }
                Position forwardRight = tryGetMove(start, 1, -1, color);
                if (forwardRight != null) {
                    Piece piece = getPiece(forwardRight);
                    if (piece != null) {
                        moves.add(forwardRight);
                    }
                }
                Position forwardLeft = tryGetMove(start, -1, -1, color);
                if (forwardLeft != null) {
                    Piece piece = getPiece(forwardLeft);
                    if (piece != null) {
                        moves.add(forwardLeft);
                    }
                }
            }
        }
        return moves;
    }

    private static class Square extends Rectangle {
        public final Position position;

        public Square(Position position, Paint paint) {
            super(position.getFile() * 64, position.getRank() * 64, 64, 64);
            setFill(paint);
            this.position = position;
        }
    }
}
