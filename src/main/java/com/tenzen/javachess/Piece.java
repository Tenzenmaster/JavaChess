package com.tenzen.javachess;

import javafx.scene.image.ImageView;

public class Piece extends ImageView {
    private Kind kind;
    private Color color;

    public Piece(Kind kind, Color color) {
        super(Resources.getImage(kind, color));
        setFitHeight(64);
        setFitWidth(64);

        this.kind = kind;
        this.color = color;
    }

    public static Piece fromChar(char ch) {
        Color color = Character.isUpperCase(ch) ? Color.WHITE : Color.BLACK;
        Kind kind = switch(Character.toLowerCase(ch)) {
            case 'b' -> Kind.BISHOP;
            case 'k' -> Kind.KING;
            case 'n' -> Kind.KNIGHT;
            case 'p' -> Kind.PAWN;
            case 'q' -> Kind.QUEEN;
            case 'r' -> Kind.ROOK;
            default -> throw new IllegalStateException("Unexpected value: " + ch);
        };
        return new Piece(kind, color);
    }

    public Kind getKind() {
        return kind;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return String.format("Piece[kind: %s, color: %s]", kind, color);
    }

    public enum Kind {
        BISHOP,
        KING,
        KNIGHT,
        PAWN,
        QUEEN,
        ROOK,
    }

    public enum Color {
        BLACK,
        WHITE,
    }
}
